

package org.ebaloo.itkeeps.core.domain.vertex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.orientechnologies.orient.core.metadata.schema.OType;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import org.apache.commons.lang.StringUtils;
import org.ebaloo.itkeeps.Guid;
import org.ebaloo.itkeeps.api.model.JBase;
import org.ebaloo.itkeeps.api.model.JBaseLight;
import org.ebaloo.itkeeps.core.database.GraphFactory;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseProperty;
import org.ebaloo.itkeeps.core.domain.BaseUtils;
import org.ebaloo.itkeeps.core.domain.ModelFactory;
import org.ebaloo.itkeeps.core.domain.ModelFactory.ModelClass;
import org.ebaloo.itkeeps.core.domain.edge.RelationInterface;
import org.ebaloo.itkeeps.core.domain.edge.RelationTools;
import org.ebaloo.itkeeps.core.domain.edge.RelationType;
import org.ebaloo.itkeeps.core.domain.edge.Traverse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

/**
 * 
 *
 */


public abstract class BaseAbstract extends CommonOrientVertex implements Comparable<BaseAbstract> {

	
	
	private static Logger logger = LoggerFactory.getLogger(BaseAbstract.class);
	

	public final static <T extends BaseAbstract> List<T> commandBaseAbstract(final ModelClass<T> target, final String cmdSQL, Object... args) {
        
		try {
		
			List<OrientVertex> listOV = GraphFactory.command(cmdSQL, args);
			List<T> listBA = new ArrayList<T>();
			
			
			for(OrientVertex ov : listOV) {
				
				listBA.add(BaseAbstract.newInstance(target, ov));
			}
		
		return listBA;
		} catch (Exception e) {
		throw new RuntimeException(cmdSQL, e);
	}
		
	}

	
	
	
	
	public static <T extends BaseAbstract> T getBaseAbstract(final ModelClass<T> target, final JBaseLight baselight)  
 	{
 
    	if(baselight == null)
    		return null;
    	
    	if(!baselight.isPresentGuid())
    		return null;
    	
		Guid guid = baselight.getGuid();

		return getBaseAbstract(target, guid);

	}
	
	
    

    public static  <T extends BaseAbstract> T getBaseAbstract(final ModelClass<T> target, final Guid guid)  
	{
    	if(guid == null)
    		return null;
		
		String cmdSQL = "SELECT FROM " + Base.class.getSimpleName() + " WHERE " + BaseUtils.WhereClause.enable() + " AND (guid = ?)";
		
		
		List<T> list = BaseAbstract.commandBaseAbstract(target, cmdSQL, guid.toString());
		
		if(list.size() == 0) {
			return null;
		}
		
		if(list.size() > 1) {
			throw new RuntimeException(String.format("guid is not unique : %s", guid));
		}
		
		return list.get(0);
	}
    
    
	protected Guid guid = null; 
	
	
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
	
	
	/*
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
	*/

	protected final void setEmbeddedListString(final String property, List<String> list) {
		
		if(list == null)
			list = new ArrayList<String>();

		this.setProperty(property, list);
	}

	/*
	protected final void addEmbeddedMapString(final String property, final String key, final String value) {

		Map<String, String> map = this.getEmbeddedMapString(property);

		if((!map.containsKey(key)) || (!map.get(key).equals(value))) {
			map.put(key, value);
			
			this.setProperty(property, map);
		}
	}
	*/

	protected final void setEmbeddedMapString(final String property, Map<String, String> map) {

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
		


	
	@Override
    public final boolean equals(Object obj) {
          try {
                return (obj instanceof BaseAbstract) && this.getGuid().equals(((BaseAbstract)obj).getGuid());
          } catch (Throwable e) {
                return false;
          }
    }



	protected final <T extends BaseAbstract> T getEdgeByClassesNames(final ModelClass<T> target, final RelationType relationship, final boolean isInstanceof) {
		return getEdgeByClassesNames(target, relationship, isInstanceof, Traverse.class) ;
	}

	
	protected final <T extends BaseAbstract> T getEdgeByClassesNames(final ModelClass<T> target, final RelationType relationship, final boolean isInstanceof, Class<? extends RelationInterface> relation) {
		
		List<T> list = getEdgesByClassesNames(target, relationship, isInstanceof, relation);
		
		if(list.size() < 1) {
			return null;
		}
		
		if(list.size() > 1) {
			logger.warn("For ["  + this.toString() + "] have note unique Edge for " + RelationTools.toLogger(relationship.getDirection()) + " " + target.getClassName());
		}
			
		return list.get(0);
	}

	/*
	 * 
	 */
	
	
	@SuppressWarnings("unchecked")
	protected final <T extends BaseAbstract> List<T> getEdgesByClassesNames(final ModelClass<T> target, final RelationType relationship, final boolean isInstanceof, Class<? extends RelationInterface> relation) {

			if((target == null) || (relationship == null)) {
				throw new RuntimeException("TODO"); //TODO
			}
	
			
			StringBuilder request = new StringBuilder();
			
			request.append("SELECT FROM ");
			request.append("(TRAVERSE " + relationship.getDirection().toString() + "('" + (relation != null ? relation.getSimpleName() : "") + "') FROM " + this.getORID() + " WHILE $depth <= 1)");
			request.append(" WHERE ");
			BaseUtils.WhereClause.enable(request);
			request.append(" AND ");
			BaseUtils.WhereClause.classIsntanceOf(BaseStandard.class, true, request);
			request.append(" AND ");
			request.append("(@rid <> " + this.getORID() + ")");
			
			
			
			return  BaseAbstract.commandBaseAbstract(target, request.toString());
			
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
	
	@DatabaseProperty(name = JBase.GUID, isNotNull = true, isReadOnly = true)
	public final Guid getGuid() {
		
		if(this.guid == null) {
			
			guid = new Guid(this.getProperty(JBase.GUID).toString());
			
		}
		
		return guid;
	}

	
	public ModelFactory.ModelClass<BaseAbstract> getModelClass() {
		return ModelFactory.get(this.getClass());
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

	
	
		public boolean hasGuid() {
			
			if(guid != null) {
				return true;
			}
			
			Object obj = this.getProperty(JBase.GUID);
			
			if(obj == null) {
				return false;
			}
			
			return Guid.isGuid(this.getProperty(JBase.GUID).toString());
			
		}


		public boolean isInstanceOf(Class<?> clazz) {
			return clazz.isInstance(this);
		}



		/*
		 * 
		 */
		

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

	
	}

	/*
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
    */
    
    /*
    protected final void removeEmbeddedMapString(final String property, final String key) {

		Map<String, String> map = this.getEmbeddedMapString(property);
		
		if(map.containsKey(key)) {
			map.remove(key);
			this.setProperty(property, map);
		}
	}
	*/

 
    protected final <T extends BaseAbstract> void setEdge(
    		final ModelClass<T> target, final RelationType relationship, final boolean isInstanceof, final  T newParent, final Class<? extends RelationInterface> relation) {
		

		T oldParnet = this.getEdgeByClassesNames(target, relationship, isInstanceof, relation);

		
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
    	
    	if(oldParnet != null) {
    		oldParnet.commit();
    	}
    	
    	if(newParent != null) {
    		newParent.commit();
    	}
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
	 * NAME
	 */
	
	
	@DatabaseProperty(name = JBase.NAME, isNotNull = true)
	public String getName() {
		return this.getProperty(JBase.NAME);
	}

	public void setName(String newValue) {
		this.setProperty(JBase.NAME, newValue);
	}

	/*
	 * ENABLE
	 */

	
	@DatabaseProperty(name = JBase.ENABLE, type = OType.BOOLEAN)
	protected void setEnable(Boolean enable) {
		this.setProperty(JBase.ENABLE, enable);
	}

	public Boolean isEnable() {
		return (Boolean) this.getProperty(JBase.ENABLE);
	}

	/*
	 * DISABLE
	 */

	public boolean disable() {
		
		this.deleteAllEdges(Direction.IN);
		this.deleteAllEdges(Direction.OUT);
		this.setProperty(JBase.ENABLE, false);
		this.commit();
		return true;
	}
	
	
	protected final static <T extends BaseAbstract> T newInstance(final ModelClass<T> target, final OrientVertex ov){
		
		try {
			
			@SuppressWarnings("unchecked")
			ModelClass<T> modelClass = (ModelClass<T>) ModelFactory.get(ov.getType().getName());

			T baseAbstract;

			baseAbstract = modelClass.newInstance();
			baseAbstract.setOrientVertex(ov);
			
			return baseAbstract;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}
	
}

