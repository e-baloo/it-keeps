

package org.ebaloo.itkeeps.domain.vertex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.orientechnologies.orient.core.command.OCommandPredicate;
import com.orientechnologies.orient.core.db.record.OIdentifiable;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.impls.orient.OrientBaseGraph;
import org.apache.commons.lang.StringUtils;
import org.ebaloo.itkeeps.database.GraphFactory;
import org.ebaloo.itkeeps.database.TraverseField;
import org.ebaloo.itkeeps.database.annotation.DatabaseProperty;
import org.ebaloo.itkeeps.domain.BaseUtils;
import org.ebaloo.itkeeps.domain.Guid;
import org.ebaloo.itkeeps.domain.ModelFactory;
import org.ebaloo.itkeeps.domain.edge.Relation;
import org.ebaloo.itkeeps.domain.edge.RelationInterface;
import org.ebaloo.itkeeps.domain.edge.RelationTools;
import org.ebaloo.itkeeps.domain.edge.RelationType;
import org.ebaloo.itkeeps.domain.edge.Traverse;
import org.ebaloo.itkeeps.tools.MetricsFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.Timer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

/**
 * 
 *
 */


public abstract class BaseAbstract extends CommonOrientVertex implements Comparable<BaseAbstract> {

	public static class BaseQuery {

		private OrientBaseGraph graph;

		private Map<String, BaseAbstract> objects = new HashMap<>();

		public BaseQuery() {
			this(null);
		}

		public BaseQuery(OrientBaseGraph graph) {
			this.graph = graph;
		}

		public final List<BaseAbstract> commandBaseAbstract(final String cmdSQL, final Object... args) {

			return parse(BaseAbstract.command(cmdSQL, args));
		}

		private OrientBaseGraph getGraph() {
			if (graph == null) {
				graph = GraphFactory.getOrientBaseGraph();
			}
			return graph;
		}

		public final List<BaseAbstract> parse(Iterable<?> ref) {
			List<BaseAbstract> list = new ArrayList<>();

			for(Object obj : ref) {
				OrientVertex ov;
				if(obj instanceof OrientVertex) {
					ov = (OrientVertex) obj;
				} else if(obj instanceof OIdentifiable) {
					ov = new OrientVertex(getGraph(), ((OIdentifiable) obj).getRecord());
				} else {
					logger.error("BaseQuery.parse: Unable to cast '" + obj.getClass().getName() + "' to OrientVertex");
					continue;
				}
				String guid = ov.getProperty(GUID);

				if (!objects.containsKey(guid)) {
					try {
						BaseAbstract baseAbstract;
						baseAbstract = ModelFactory.get(ov.getType().getName()).newInstance();
						baseAbstract.setOrientVertex(ov);
						baseAbstract.setBaseQuery(this);
						objects.put(ov.getProperty(GUID), baseAbstract);

//                            System.out.println("StoreCache: " + ov.getProperty(Base.GUID));
					} catch (Exception e) {
						logger.error(ov.getType().getName());
						throw new RuntimeException(e);
					}
//					} else {
//						System.out.println("UseCache: " + ov.getProperty(Base.GUID));
				}

				list.add(objects.get(guid));
			}

			return list;
		}

	}

//	protected final static ObjectMapper JMapper = new ObjectMapper();

	public static final String CLASS_TYPE = "classType";
	public static final String GUID = "guid";

	
	/*
	static {
		JMapper.enable(SerializationFeature.INDENT_OUTPUT);
		JMapper.setSerializationInclusion(Include.NON_NULL);
	}
	*/

	
	private static Logger logger = LoggerFactory.getLogger(BaseAbstract.class);
	
	public static final Timer TIMER_DATABASE_REQUEST_CODE = MetricsFactory.getMetricRegistry().timer("database.request.code");	
	
	public final static List<BaseAbstract> commandBaseAbstract(final String cmdSQL, Object... args) {
        return (new BaseQuery()).commandBaseAbstract(cmdSQL, args);
	}

	@JsonIgnore
	public static BaseQuery createBaseQuery() {
		return new BaseQuery();
	}
	
    // Imported from IBase //

    public static BaseAbstract getBaseAbstract(final Guid guid)  
	{
		
		String cmdSQL = "SELECT FROM " + Base.class.getSimpleName() + " WHERE " + BaseUtils.WhereClause.enable() + " AND (guid = ?)";
		
		
		List<BaseAbstract> list = BaseAbstract.commandBaseAbstract(cmdSQL, guid.toString());
		
		if(list.size() == 0) {
			return null;
		}
		
		if(list.size() > 1) {
			throw new RuntimeException(new Exception(String.format("guid is not unique : %s", guid)));
			// TODO
		}
		
		return list.get(0);
	}
    
	@JsonIgnore
    private BaseAbstract _objParent = null;

	@JsonIgnore
    private boolean _objParentInit = false;
    
	@JsonIgnore
	private BaseQuery baseQuery;
    

    // -----------------------------------------------------------------

	@JsonProperty(required = true, value = GUID)
	protected Guid guid = null; 
	
	@JsonIgnore
	public boolean loadRelationshipOnce = false;
	
	@JsonIgnore
	private final Map<RelationType, List<BaseAbstract>> objectCache = new HashMap<>();


	
	public BaseAbstract() {
		;
	}
	public BaseAbstract(final BaseAbstract abase) {
		this.setOrientVertex(abase.getOrientVertex());
	}
	
	protected BaseAbstract(boolean bool) {
		
		newOrientVertex();

	}

	protected final <T extends BaseAbstract> void addEdge(BaseAbstract newIBase, RelationType relationship, Class<? extends RelationInterface> relation) {
		
		this.checkOrientVertex();
		
		
		if(newIBase == null) {
			return;
		}
		
    	if(this.getOrientVertex().getEdges(newIBase.getOrientVertex(), relationship.getDirection()).iterator().hasNext()) {
    		
    		for(Edge e : this.getOrientVertex().getEdges(newIBase.getOrientVertex(), relationship.getDirection())) {

    			if(relation == null || e.getLabel().equals(relation.getSimpleName())) {
    	    		if(logger.isTraceEnabled())
    	    			logger.trace("Link exist! [" + this.toString() + "] " + RelationTools.toLogger(relationship.getDirection()) + " [" + newIBase.toString() + "]");
    	    		return;
    			}
    		}
    		
    		
    		
    	}
    	
    	BaseAbstract ovSrc = null;
    	BaseAbstract ovDst =  null;

    	switch(relationship) {
    	
    	case PARENT:
        	ovSrc = this;
        	ovDst =  newIBase;
    		break;
    		
    	case CHILD:
        	ovSrc = newIBase;
        	ovDst =  this; 
    		break;
    		
		default:
			// TODO
			throw new RuntimeException(new Exception("BOTH relation is not autorized!"));
    		
    	}

    	// Create Relation
    	
    	String cmd = "CREATE EDGE " + relation.getSimpleName() + " FROM " + ovSrc.getORID() + " TO " + ovDst.getORID();
		CommonOrientVertex.executeNoReturn(cmd);

    	
		if(logger.isDebugEnabled()) 
			logger.debug("Add Link! [@" + this.toString() + "] " + RelationTools.toLogger(relationship.getDirection()) + "(@" + relation.getSimpleName() + ")" + " [@" + newIBase.toString() + "]");
	}
	
	
	protected final void addEmbeddedListString(final String property, final String value) {

		List<String> list = this.getProperty(property);
		
		if(list == null) {
			list = new ArrayList<String>();
		}

		if (!list.contains(value)) {

			list.add(value);

			this.setProperty(property, list);
		}
	}

	protected final void putEmbeddedListString(final String property, List<String> list) {
		
		if(list == null)
			list = new ArrayList<String>();

		this.setProperty(property, list);
	}
	
	protected final void addEmbeddedMapString(final String property, final String key, final String value) {

		Map<String, String> map = this.getEmbeddedMapString(property);

		if((!map.containsKey(key)) || (!map.get(key).equals(value))) {
			map.put(key, value);
			
			this.setProperty(property, map);
		}
	}

	protected final void putEmbeddedMapString(final String property, Map<String, String> map) {

		if(map == null)
			map = new HashMap<String, String>();
		
		this.setProperty(property, map);
	}

	
	protected boolean checkUserUpdateLink(final BaseAbstract ovDst) {
		 return true;
	 }

	@Override
	public int compareTo(BaseAbstract obj) {
		if(obj == null) {
			return 0;
		}

		if(StringUtils.isBlank(obj.getName())) {
			return 0;
		}
		
		if(StringUtils.isBlank(this.getName())) {
			return 0;
		}

		// Tri en fonction du Name //
		return this.getName().compareTo(((BaseAbstract)obj).getName());
	}
		

	
	/*
	 * 
	 * TAG
	 * 
	 */
	

	/* TODO
	public final List<Tag> getTags() {
		this.checkOrientVertex();
		
		List<Tag> list = new ArrayList<Tag>();

		for (OrientVertex myOv : VertexUtils.getVertexByClassesNames(this.getOrientVertex(), Tag.class.getSimpleName(), Direction.IN)) {
			try {
				
				Tag t = new Tag(myOv);
				list.add(t);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return list;
	}
	
	
	
	public final void addTag(Tag tag) {
		tag.add(this);
	}

	public final void removeTag(Tag tag) {
		tag.remove(this);
	}
	
	*/
	
	@Override
    public final boolean equals(Object obj) {
          try {
                // Verrifie par Guid si l'objet est du type Base
                return (obj instanceof BaseAbstract) && this.getGuid().equals(((BaseAbstract)obj).getGuid());
          } catch (Throwable e) {
                return false;
          }
    }

	public Map<String, List<BaseStandard>>  getAllLink(RelationType relationShip) {
		return this.getAllLink(relationShip, Traverse.class);
	}

	
	public Map<String, List<BaseStandard>>  getAllLink(RelationType relationship, Class <? extends Relation> relation)  {
		
		if(relationship == null) {
			// TODO
			throw new RuntimeException("TODO");
		}
					
		HashMap<String, List<BaseStandard>> map = new HashMap<>();
		
		StringBuilder request = new StringBuilder();
		
		request.append("SELECT FROM ");
		request.append("(TRAVERSE " + relationship.getDirection().toString() + "('" + relation.getSimpleName() + "') FROM " + this.getORID() + " WHILE $depth <= 1)");
		request.append(" WHERE ");
		BaseUtils.WhereClause.enable(request);
		request.append(" AND ");
		BaseUtils.WhereClause.classIsntanceOf(BaseStandard.class, true, request);
		request.append(" AND ");
		request.append("(@rid <> " + this.getORID() + ")");
				
		for(BaseAbstract ba : this.getBaseQuery().commandBaseAbstract(request.toString())) {
			
			if(!map.containsKey(ba.getType())) {
				map.put(ba.getType(), new ArrayList<>());
			}
			
			map.get(ba.getType()).add((BaseStandard) ba);
			
		}

		return map;
	}
	
	

	protected BaseQuery getBaseQuery() {
		
		if(baseQuery == null) {
			this.baseQuery = new BaseQuery();
		}
		
		return this.baseQuery;
	}
	
	/*
	protected final <T extends BaseAbstract> List<T> getChainEdge(final UserContextInterface context, final Class<T> targetClass, final ERelationship relationship, final boolean isInstanceof) {
		return getChainEdge(context, targetClass, relationship, isInstanceof, false);
	}
	*/
	

	protected final <T extends BaseAbstract> List<T> getChainEdge(final Class<T> targetClass, final RelationType relationship, final boolean isInstanceof, final boolean revers) {
		
		if((targetClass == null) || (relationship == null)) {
			// TODO
			throw new RuntimeException(new Exception("TODO"));
		}

		this.checkOrientVertex();

		StringBuilder request = new StringBuilder();

		request.append("SELECT FROM ");
		request.append("(TRAVERSE " + relationship.getDirection().toString() + "('" + Traverse.class.getSimpleName() + "') FROM " + this.getORID() + ")");
		request.append(" WHERE ");
		BaseUtils.WhereClause.enable(request);
		request.append(" AND ");
		request.append("(@class " + (isInstanceof ? "INSTANCEOF" : "=") + " '" + targetClass.getSimpleName() + "')");

		List<T> list = this.baseQuery.commandBaseAbstract(request.toString()).stream().map(targetClass::cast).collect(Collectors.toList());

		if(revers) {
			java.util.Collections.reverse(list);
		}

		return list;
		
	}

	
	

	/**
	 * 
	 * @param targetClass
	 * @param relationship
	 * @return
	 * @throws Exception
	 */

	protected final <T extends BaseAbstract> T getEdgeByClassesNames(final Class<T> targetClass, final RelationType relationship, final boolean isInstanceof) {
		return getEdgeByClassesNames(targetClass, relationship, isInstanceof, Traverse.class) ;
	}

	/* MDO 2016-01-15
	protected final <T> T getProperty(String property) {
		return this.getOrientVertex().getProperty(property);
 	}
	*/


	
	protected final <T extends BaseAbstract> T getEdgeByClassesNames(final Class<T> targetClass, final RelationType relationship, final boolean isInstanceof, Class<? extends RelationInterface> relation) {
		
		List<T> list = getEdgesByClassesNames(targetClass, relationship, isInstanceof, relation);
		
		if(list.size() < 1) {
			return null;
		}
		
		if(list.size() > 1) {
			logger.warn("For ["  + this.toString() + "] have note unique Edge for " + RelationTools.toLogger(relationship.getDirection()) + " " + targetClass.getSimpleName());
		}
			
		return list.get(0);
	}

	/*
	 * 
	 */
	
	@Deprecated
	protected final <T extends BaseAbstract> List<T> getEdgesByClassesNames(final Class<T> targetClass, final RelationType relationship, final boolean isInstanceof) {
		return getEdgesByClassesNames(targetClass, relationship, isInstanceof, Traverse.class);
	}
	
	@SuppressWarnings("unchecked")
	protected final <T extends BaseAbstract> List<T> getEdgesByClassesNames(final Class<T> targetClass, final RelationType relationship, final boolean isInstanceof, Class<? extends RelationInterface> relation) {

		final Timer.Context timerContext = TIMER_DATABASE_REQUEST_CODE.time();
		
		try {
			if((targetClass == null) || (relationship == null)) {
				throw new RuntimeException(new Exception("TODO"));
			}
	
			if(loadRelationshipOnce) { //TODO /!\ Ne prend pas en compte relation (Mettre un cache plus bas (this.getVertices))
				if(!objectCache.containsKey(relationship)) {
					loadAllRelationship(relationship);
				}
				return (List<T>) objectCache.get(relationship).stream().filter(e -> (isInstanceof ? targetClass.isInstance(e) : targetClass.equals(e.getClass()))).collect(Collectors.toList());
			}
	        if(logger.isTraceEnabled()) {
				logger.trace(String.format("[getEdgesByClassesNames] Get edges of %s '%s' (target: %s, direction: %s, instanceOf: %b, relation: %s)",
						this.getGuid(), this.getName(), targetClass, relationship, isInstanceof, relation));
			}
			return (List<T>) this.getVertices(relationship, relation)
						.filter(BaseUtils.WhereClause.enableFilter())
						.filter(BaseUtils.WhereClause.classInstanceOf(targetClass, isInstanceof))
						.collect(Collectors.toList());
		} finally {
			timerContext.stop();
		}
    }

	protected final List<String> getEmbeddedListString(final String property) {

		List<String> list = this.getProperty(property);
		
		if(list == null) {
			list = new ArrayList<String>();
		}
		
		return list;
	}
	
	

	
	/*
	 * 
	 */
	
	protected final Map<String, String> getEmbeddedMapString(final String property) {

		Map<String, String> map = this.getProperty(property);
		
		if(map == null) {
			map = new HashMap<String, String>();
		}
		
		return map;
	}
	
	@DatabaseProperty(name = GUID, isNotNull = true, isReadOnly = true)
	public final Guid getGuid() {
		
		if(this.guid == null) {
			
			guid = new Guid(this.getProperty(GUID).toString());
			
		}
		
		return guid;
	}

	
	public ModelFactory.ModelClass<BaseAbstract> getModelClass() {
		return ModelFactory.get(this.getClass());
	}
	

	 
	 
		// TODO: GetParent() existe déjà //
		// Utilisait avant la fonction : getChainEdge() et donc faisait un Traverse (normalement pas d'impact)
		public BaseAbstract getObjectParent() {
		    if(!_objParentInit) {
		        _objParentInit = true;
				if(logger.isTraceEnabled()) {
//	            System.out.println("Load parent of " + this.getGuid() + " " + this.getName() + " " + this.getClass().getSimpleName() + " " + this.baseQuery.hashCode());
					logger.trace(this.baseQuery.hashCode() + " - Load parent of " + this.getGuid());
				}
		        //TODO: Si passage sur des lightweight edges, a voir l'implementation d'un cache InternalId sur BaseQuery
		        _objParent = this.getVertices(RelationType.PARENT, Traverse.class)
						.filter(BaseUtils.WhereClause.enableFilter())
						.filter(BaseUtils.WhereClause.classInstanceOf(this.getClass(), false))
						.findFirst().orElse(null);
		    }
		    return _objParent;
		}
		
		
	public final Map<String, Object> getProperties() {
		return this.getOrientVertex().getProperties();
	}
		
	public final String getType() {
		
		OrientVertexType type;
		try {
			type = this.getOrientVertex().getType();
		} catch (Exception e) {
			logger.trace("getType() is null -> 2nd chance! [" + e.getMessage() + "]");
			this.reload();
			type = this.getOrientVertex().getType();
		}
		
		
		return type.getName();
	}

	
	
	
		protected Stream<BaseAbstract> getVertices(final RelationType direction, final Class<? extends RelationInterface> relation) {
			if(logger.isTraceEnabled()) {
				logger.trace(String.format("getVertices - Object: %s '%s' (%s) ; Direction: %s ; Relation: %s",
						this.getGuid(), this.getName(), this.getORID(), direction, relation));
			}
			return this.getBaseQuery().parse(this.getOrientVertex().getVertices(direction.getDirection(), relation.getSimpleName())).stream();
		}

		protected List<BaseAbstract> getVerticesList(final RelationType direction, final Class<? extends RelationInterface> relation) {
			return this.getVertices(direction, relation).collect(Collectors.toList());
		}

		public boolean hasGuid() {
			
			if(guid != null) {
				return true;
			}
			
			Object obj = this.getProperty(GUID);
			
			if(obj == null) {
				return false;
			}
			
			return Guid.isGuid(this.getProperty(GUID).toString());
			
		}

		public boolean hasParent() {
		    return getObjectParent() != null;
		}

		public boolean isInstanceOf(Class<?> clazz) {
			return clazz.isInstance(this);
		}

		public void loadAllRelationship(final RelationType relationship) {
		    //TODO /!\ Ne prend pas en compte relation (label)
			objectCache.put(relationship, this.getBaseQuery().parse(this.getOrientVertex().getVertices(relationship.getDirection())));
		}

		public void loadRelationshipOnce() {
			loadRelationshipOnce = true;
		}
		
		


		/*
		 * 
		 */
		
		// TODO celan 
		// DELETE EDGE FROM (SELECT FROM BASE WHERE guid = 'd56e7480-31bb-4d71-bb68-ab4fcc72e2d4') TO (SELECT FROM VersionDatabase)

	public void newOrientVertex() {

		if(!this.hasOrientVertex()) {
			OrientVertexType ovt = this.getVertexType();
	
			if (ovt == null) {
				throw new RuntimeException("The class \"" + this.getClass().getSimpleName() + "\" is note defined in the database");
			}
			
			this.setOrientVertex(this.getGraph().addVertex("class:" + ovt.getName()));
		}
			
	}

	protected final <T extends BaseAbstract> void removeEdge(BaseAbstract oldBaseAbstract, RelationType relationship,
			Class<? extends RelationInterface> relation) {

		if (oldBaseAbstract == null) {
			return;
		}

    	BaseAbstract ovSrc = null;
    	BaseAbstract ovDst =  null;

    	switch(relationship) {
    	
    	case PARENT:
        	ovSrc = this;
        	ovDst =  oldBaseAbstract;
    		break;
    		
    	case CHILD:
        	ovSrc = oldBaseAbstract;
        	ovDst =  this; 
    		break;
    		
		default:
			// TODO
			throw new RuntimeException(new Exception("BOTH relation is not autorized!"));
    		
    	}

    	
    	String cmd = "DELETE EDGE FROM " + ovSrc.getORID() + " TO " + ovDst.getORID();
    	if(relation != null) {
    		cmd += " WHERE @class = '" + relation.getSimpleName() + "'";
    	}
    	
    	
		
		int ret = CommonOrientVertex.execute(cmd);

		if (ret > 0) {

			ovSrc.reload();
			ovDst.reload();



			if(logger.isTraceEnabled()) {
				String comment = "Remove link ! [@" + this.toString() + "] "
						+ RelationTools.toLogger(relationship.getDirection()) + " [@" + oldBaseAbstract.toString() + "]";

				logger.trace(comment);
			}

		}

		/*
		 * 
		 * if(!this.getOrientVertex().getEdges(oldBaseAbstract.getOrientVertex()
		 * , relationship.getDirection()).iterator().hasNext()) { logger.trace(
		 * "Link not exist! [@" + this.toString() + "] " +
		 * DirectionUtils.toLogger(relationship.getDirection()) + " [@" +
		 * oldBaseAbstract.toString() + "]"); return; }
		 * 
		 * Iterable<Edge> iterable = null;
		 * 
		 * if(relation == null) { iterable =
		 * this.getOrientVertex().getEdges(oldBaseAbstract.getOrientVertex(),
		 * relationship.getDirection()); } else { iterable =
		 * this.getOrientVertex().getEdges(oldBaseAbstract.getOrientVertex(),
		 * relationship.getDirection(), relation.getSimpleName()); }
		 * 
		 * for(Edge e : iterable) { String comment = "Remove link ! [@" +
		 * this.toString() + "] " +
		 * DirectionUtils.toLogger(relationship.getDirection()) + " [@" +
		 * oldBaseAbstract.toString() + "]";
		 * 
		 * /* this.setUpdateDate(); this.commit();
		 * this.addUserUpdate(COMMENT_LINK, null, comment); this.commit();
		 * 
		 * 
		 * if(!(oldBaseAbstract instanceof Referential || oldBaseAbstract
		 * instanceof Organization)) { oldBaseAbstract.setUpdateDate();
		 * oldBaseAbstract.commit(); oldBaseAbstract.addUserUpdate(COMMENT_LINK,
		 * null, comment); oldBaseAbstract.commit(); }
		 */

		// e.remove();
		// this.reload();

		// }

		// this.commit();
		// oldBaseAbstract.commit();
	}

    protected final void removeEmbeddedListString(final String property, final String value) {

		List<String> list = this.getProperty(property);
		
		if(list == null) {
			list = new ArrayList<String>();
		}

		if (list.contains(value)) {

			list.remove(value);

			this.setProperty(property, list);
		}
	}
    protected final void removeEmbeddedMapString(final String property, final String key) {

		Map<String, String> map = this.getEmbeddedMapString(property);
		
		if(map.containsKey(key)) {
			map.remove(key);
			this.setProperty(property, map);
		}
	}

    /*
    protected final <T> void setBaseProperty(String property, T newValue) {

		T oldValue = this.getProperty(property);

		if(oldValue == null && newValue == null) {
			return;
		}
		
		if(oldValue != null && newValue != null) {
			 if(newValue.equals(oldValue)) {
				 return;
			 }
		}

		this.setProperty(property, newValue);
 	}
 	*/

    protected void setBaseQuery(BaseQuery obj) {
		
		if(obj != null) {
			this.baseQuery = obj;
		}
	}

    protected final <T extends BaseAbstract> void setEdge(
			final Class<? extends T> targetClass, final RelationType relationship, final boolean isInstanceof, final  T newParent, final Class<? extends RelationInterface> relation) {
		

		T oldParnet = this.getEdgeByClassesNames(targetClass, relationship, isInstanceof, relation);

		
		if(newParent == null && oldParnet == null) {
			
			if(logger.isTraceEnabled())
				logger.trace("Old and new value is null!");
			return;
		}

		
		if(newParent != null && oldParnet != null && newParent.getOrientVertex().equals(oldParnet.getOrientVertex()))
		{
			if(logger.isTraceEnabled())
				logger.trace("Link exist ! [@" + this.toString() + "] " + RelationTools.toLogger(relationship.getDirection()) + " [@" + newParent.toString() + "]");
			return; 
		}
		
		if(oldParnet != null) {
			removeEdge(oldParnet, relationship, relation);
		}
		
    	addEdge(newParent, relationship, relation);
    	
    	//this.commit();
    	
    	if(oldParnet != null) {
    		oldParnet.commit();
    	}
    	
    	if(newParent != null) {
    		newParent.commit();
    	}
	}
    

    
	/*
	 * 
	 */
	
	// Usage Specifique //
    public void setObjectParent(BaseAbstract obj) {
        _objParentInit = true;
        _objParent = obj;
    }
	

	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(this.getType());
			sb.append("/");
			sb.append(this.getGuid());
			sb.append("/");
			sb.append(this.getName());
		} catch ( Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return sb.toString();
	}

	/*
	 * GUID
	 */
	
	protected Stream<BaseAbstract> traverse(final RelationType direction, final Class<? extends RelationInterface> relation) {
		return this.traverse(direction, relation, null);
	}
	
	protected Stream<BaseAbstract> traverse(final RelationType direction, final Class<? extends RelationInterface> relation, OCommandPredicate predicate) {
		if(logger.isTraceEnabled()) {
			logger.trace(String.format("Traverse - Object: %s '%s' (%s) ; Direction: %s ; Relation: %s ; Predicate: %s",
					this.getGuid(), this.getName(), this.getORID(), direction, relation, predicate));
		}
		return this.getBaseQuery().parse(this.getOrientVertex().traverse().field(new TraverseField(getGraph(), direction.getDirection(), relation.getSimpleName()))
				.predicate(predicate))
				.stream();
	}

	
	protected List<BaseAbstract> traverseList(final RelationType direction, final Class<? extends RelationInterface> relation) {
		return this.traverse(direction, relation).collect(Collectors.toList());
	}
	
	
	/*
	 * NAME
	 */
	
	public static final String NAME = "name";
	
	@DatabaseProperty(name = NAME, isNotNull = true)
	public String getName() {
		return this.getProperty(NAME);
	}

	public void setName(String newValue) {
		this.setProperty(NAME, newValue);
	}

	/*
	 * ENABLE
	 */

	public static final String ENABLE = "enable";
	
	@DatabaseProperty(name = ENABLE, type = OType.BOOLEAN)
	public Boolean isEnable() {
		return (Boolean) this.getProperty(ENABLE);
	}

	/*
	 * DISABLE
	 */

	public boolean disable() {
		
		this.deleteAllEdges(Direction.IN);
		this.deleteAllEdges(Direction.OUT);
		this.setProperty(ENABLE, false);
		this.commit();
		return true;
	}
}

