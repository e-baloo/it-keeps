

package org.ebaloo.itkeeps.core.domain.vertex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.orientechnologies.orient.core.metadata.schema.OType;
import com.tinkerpop.blueprints.Direction;
import org.apache.commons.lang.StringUtils;
import org.ebaloo.itkeeps.Guid;
import org.ebaloo.itkeeps.api.model.JBase;
import org.ebaloo.itkeeps.api.model.JBaseLight;
import org.ebaloo.itkeeps.core.database.GraphFactory;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseProperty;
import org.ebaloo.itkeeps.core.domain.BaseUtils;
import org.ebaloo.itkeeps.core.domain.ModelFactory;
import org.ebaloo.itkeeps.core.domain.annotation.ModelClassAnnotation;
import org.ebaloo.itkeeps.core.domain.edge.RelationInterface;
import org.ebaloo.itkeeps.core.domain.edge.RelationTools;
import org.ebaloo.itkeeps.core.domain.edge.DirectionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tinkerpop.blueprints.impls.orient.OrientBaseGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

/**
 * 
 *
 */


public abstract class BaseAbstract extends CommonOrientVertex implements Comparable<BaseAbstract> {

	private static Logger logger = LoggerFactory.getLogger(BaseAbstract.class);
	

	protected final static <T extends BaseAbstract> List<T> commandBaseAbstract(OrientBaseGraph graph, final ModelClass<T> target, final String cmdSQL, Object... args) {

		/*
		List<T> list = new ArrayList<T>();
		
		for(OrientVertex ov   : GraphFactory.command(graph, cmdSQL, args)) {
			list.add(newInstance(target, ov));
		}
		
		return list;
		*/
		
		return GraphFactory.command(graph, cmdSQL, args).stream().map(e -> BaseAbstract.getInstance(target, e)).collect(Collectors.toList());
	}

	
	public static final <T extends BaseAbstract> T get(OrientBaseGraph graph, final ModelClass<T> target, final JBaseLight baselight, boolean isInstanceof)  
 	{
    	if(baselight == null || !baselight.isPresentGuid())
    		return null;
    	
		return get(graph, target, baselight.getGuid(), isInstanceof);
	}
	
	
   

	public static final <T extends BaseAbstract> T get(final OrientBaseGraph graph, final ModelClass<T> target, final Guid guid, boolean isInstanceof)  
	{
    	if(guid == null)
    		return null;
		
		String cmdSQL = "SELECT FROM " + target.getClassName() + " WHERE " + BaseUtils.WhereClause.WHERE_CLAUSE__ENABLE_IS_TRUE + " AND " + BaseUtils.WhereClause.classIsntanceOf(target, isInstanceof) + " AND (" + JBase.GUID + "= ?)";
		
		List<T> list = BaseAbstract.commandBaseAbstract(graph, target, cmdSQL, guid.toString());
		
		if(list.size() == 0) {
			return null;
		}
		
		if(list.size() > 1) {
			throw new RuntimeException(String.format("guid is not unique : %s", guid));
		}
		
		return list.get(0);
	}
	
	
	public static final <T extends BaseAbstract> T get(final OrientBaseGraph graph, final ModelClass<T> target, final String name, boolean isInstanceof)  
	{
    	if(StringUtils.isEmpty(name))
    		return null;
		
    	if(Guid.isGuid(name))
    		return get(graph, target, new Guid(name), isInstanceof); 
    	
		String cmdSQL = "SELECT FROM " + target.getClassName() + " WHERE " + BaseUtils.WhereClause.WHERE_CLAUSE__ENABLE_IS_TRUE + " AND " + BaseUtils.WhereClause.classIsntanceOf(target, isInstanceof) + " AND (" + JBase.NAME + "= ?)";
		
		List<T> list = BaseAbstract.commandBaseAbstract(graph, target, cmdSQL, name);
		
		if(list.size() == 0) {
			return null;
		}
		
		if(list.size() > 1) {
			throw new RuntimeException(String.format("name is not unique : %s @class:%s", name, target.getClassName()));
		}
		
		return list.get(0);
	}

	
	
    
    protected static <T extends BaseAbstract> List<T> getListBaseAbstract(OrientBaseGraph graph, final ModelClass<T> target, final List<Guid> guid) {

		String cmdSQL = "SELECT FROM " + Base.class.getSimpleName() + " WHERE " + BaseUtils.WhereClause.WHERE_CLAUSE__ENABLE_IS_TRUE
				+ " AND (guid IN [" + guid.stream().map(e -> "'" + e.toString() + "'").collect(Collectors.joining(","))
				+ "])";

		return BaseAbstract.commandBaseAbstract(graph, target, cmdSQL);
	}
    
    
	
	
	protected BaseAbstract() {
		;
	}
	protected BaseAbstract(final BaseAbstract abase) {
		this.setOrientVertex(abase.getOrientVertex());
	}
	
	protected BaseAbstract(boolean bool) {
		
		newOrientVertex();

	}

	
	/*
	 * 
	 */

	protected final static void setEdges(OrientBaseGraph graph, final ModelClass<? extends BaseAbstract> target, BaseAbstract src, BaseAbstract dst, DirectionType direction, Class<? extends RelationInterface> relation, boolean instanceOf) {

		List<BaseAbstract> listSrc = new ArrayList<>();
		if(src != null)
			listSrc.add(src);

		List<BaseAbstract> listDst = new ArrayList<>();
		if(dst != null)
			listDst.add(dst);

		setEdges(graph, target, listSrc, listDst, direction, relation, instanceOf);
	}

	protected final static void setEdges(OrientBaseGraph graph, final ModelClass<? extends BaseAbstract> target, BaseAbstract src, List<? extends BaseAbstract> dst, DirectionType direction, Class<? extends RelationInterface> relation, boolean instanceOf) {

		List<BaseAbstract> listSrc = new ArrayList<>();
		if(src != null)
			listSrc.add(src);
		
		setEdges(graph, target, listSrc, dst, direction, relation, instanceOf);
	}
	
	protected final static void setEdges(OrientBaseGraph graph, final ModelClass<? extends BaseAbstract> target, List<? extends BaseAbstract> src, List<? extends BaseAbstract> dst, DirectionType direction, Class<? extends RelationInterface> relation, boolean instanceOf) {

		if(direction == null) 
			throw new RuntimeException("direction is null!");

		if(relation == null) 
			throw new RuntimeException("relation is null!");

		if(src == null)
			src = new ArrayList<BaseAbstract>();
		
		if(dst == null)
			dst = new ArrayList<BaseAbstract>();
		

		List<String> ovSrc = null;
		List<String> ovDstNew =  null;
		List<String> ovDstBck =  null;
		List<String> ovDstRemove = new ArrayList<String>();
		List<String> ovDstAdd = new ArrayList<String>();

			
			List<String> srcOrid = src.stream().map(e -> e.getORID()).collect(Collectors.toList());
			List<String> dstOrid = dst.stream().map(e -> e.getORID()).collect(Collectors.toList());

			String cmdSQL = ""
					+ "SELECT FROM "
					+ " (TRAVERSE "
					+ "     " + direction.getDirection().toString() + "('" + relation.getSimpleName() + "') "
					+ "     FROM [" + StringUtils.join(srcOrid, ", ") + "] "
					+ "     WHILE $depth <= 1) "
					+ "WHERE @rid NOT IN [" + StringUtils.join(srcOrid, ", ") + "]";

			cmdSQL = cmdSQL + " AND " + BaseUtils.WhereClause.WHERE_CLAUSE__ENABLE_IS_TRUE;  

			if(target != null)
				cmdSQL = cmdSQL + " AND " + BaseUtils.WhereClause.classIsntanceOf(target, instanceOf);  
			
			
			ovDstBck = command(graph, cmdSQL).stream().map(e -> e.getIdentity().toString()).collect(Collectors.toList());
			
	    	switch(direction) {
	    	
	    	case PARENT:
	        	ovSrc = srcOrid;
	        	ovDstNew = dstOrid;
	    		break;
	    		
	    	case CHILD:
	        	ovSrc = dstOrid;
	        	ovDstNew = srcOrid; 
	    		break;
	    		
			default:
				throw new RuntimeException("relation [" + direction.toString() + "] is not autorized!");
	    		
	    	}
	    	

	    	for(String s : ovDstBck)
	    		if(!ovDstNew.contains(s))
	    			ovDstRemove.add(s);
	    	
	    	for(String s : ovDstNew)
	    		if(!ovDstBck.contains(s))
	    			ovDstAdd.add(s);


	    	if(logger.isTraceEnabled()) {
				logger.trace("ovSrc       : " + ovSrc);
				logger.trace("ovDst       : " + ovDstNew);
				logger.trace("ovDstBck    : " + ovDstBck);
				logger.trace("ovDstRemove : " + ovDstRemove);
				logger.trace("ovDstAdd    : " + ovDstAdd);
	    	}
		

		if(ovSrc.size() > 0) {
			if(ovDstRemove.size() > 0) {
		    	String cmd = "DELETE EDGE FROM [" + StringUtils.join(ovSrc, ", ") + "] TO [" + StringUtils.join(ovDstRemove, ", ") + "] ";
		   		cmd += " WHERE @class = '" + relation.getSimpleName() + "'";
				CommonOrientVertex.executeNoReturn(graph, cmd);
			}
			
			if(ovDstAdd.size() > 0) {
		    	String cmd = "CREATE EDGE " + relation.getSimpleName() + " FROM [" + StringUtils.join(ovSrc, ", ") + "] TO [" + StringUtils.join(ovDstAdd, ", ") + "] ";
				CommonOrientVertex.executeNoReturn(graph, cmd);
			}
		}
		
	}
		
	
	


	/*
	 * List<String>
	 */
	
	protected final  List<String> getListString(final String property) {
		
		List<String> list = this.getProperty(property);
		
		if(list == null)
			list = new ArrayList<String>();
		
		return list;
	}

	protected final void setListString(final String property, List<String> list) {
		
		if(list == null)
			list = new ArrayList<String>();

		this.setProperty(property, list);
	}

	/*
	 * Map<String, String>
	 */
	
	protected final Map<String, String> getMapString(final String property) {
		
		Map<String, String> map = this.getProperty(property);
		
		if(map == null)
			map = new HashMap<String, String>();
		
		return map;
	}
	
	protected final void setMapString(final String property, Map<String, String> map) {

		if(map == null)
			map = new HashMap<String, String>();
		
		this.setProperty(property, map);
	}

	
	/*
	 * 
	 * 
	 */
	

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
		
			if(obj == null)
				return false;
			
			if(!(obj instanceof BaseAbstract))
				return false;
			
			return this.getORID().equals(((BaseAbstract)obj).getORID());
			
			
          } catch (Throwable e) {
                return false;
          }
    }


	
	protected final <T extends BaseAbstract> T getEdgeByClassesNames(final ModelClass<T> target, final DirectionType direction, final boolean isInstanceof, Class<? extends RelationInterface> relation) {
		
		List<T> list = getEdgesByClassesNames(target, direction, isInstanceof, relation);
		
		if(list.size() < 1) {
			return null;
		}
		
		if(list.size() > 1) {
			throw new RuntimeException("For ["  + this.toString() + "] have note unique Edge for " + RelationTools.toLogger(direction.getDirection()) + " " + target.getClassName());
		}
			
		return list.get(0);
	}

	/*
	 * 
	 */
	
	
	protected final <T extends BaseAbstract> List<T> getEdgesByClassesNames(final ModelClass<T> target, final DirectionType relationship, final boolean isInstanceof, Class<? extends RelationInterface> relation) {

			if((target == null) || (relationship == null)) {
				throw new RuntimeException("TODO"); //TODO
			}
	
			
			StringBuilder request = new StringBuilder();
			
			request.append("SELECT FROM ");
			request.append("(TRAVERSE " + relationship.getDirection().toString() + "('" + (relation != null ? relation.getSimpleName() : "") + "') FROM " + this.getORID() + " WHILE $depth <= 1)");
			request.append(" WHERE ");
			request.append(BaseUtils.WhereClause.WHERE_CLAUSE__ENABLE_IS_TRUE);
			request.append(" AND ");
			BaseUtils.WhereClause.classIsntanceOf(target, isInstanceof, request);
			request.append(" AND ");
			request.append("(@rid <> " + this.getORID() + ")");
			
			
			
			return  BaseAbstract.commandBaseAbstract(this.getGraph(), target, request.toString());
			
    }

	/*
	protected final List<String> getEmbeddedListString(final String property) {

		List<String> list = this.getProperty(property);
		
		if(list == null) {
			list = new ArrayList<String>();
		}
		
		return list;
	}
	*/
	
	

	
	/*
	 * 
	 */
	
	/*
	protected final Map<String, String> getEmbeddedMapString(final String property) {

		Map<String, String> map = this.getProperty(property);
		
		if(map == null) {
			map = new HashMap<String, String>();
		}
		
		return map;
	}
*/
	

	
	protected ModelClass<BaseAbstract> getModelClass() {
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


		protected boolean isInstanceOf(Class<?> clazz) {
			return clazz.isInstance(this);
		}



		/*
		 * 
		 */
		

		protected void newOrientVertex() {

		if(!this.hasOrientVertex()) {
			OrientVertexType ovt = this.getVertexType();
	
			if (ovt == null) {
				throw new RuntimeException("The class \"" + this.getClass().getSimpleName() + "\" is note defined in the database");
			}
			
			this.setOrientVertex(this.getGraph().addVertex("class:" + ovt.getName()));
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

	
	// TODO
	// TODO
	// TODO
	// TODO
    protected final <T extends BaseAbstract> void setEdge(
    		final ModelClass<T> target, final DirectionType relationship, final boolean isInstanceof, final  T newParent, final Class<? extends RelationInterface> relation) {
		

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
			//removeEdge(oldParnet, relationship, relation);
		}
		
    	//addEdge(newParent, relationship, relation);
    	
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
	 * GUID 
	 */
	
	private Guid guid = null; 

	
	@DatabaseProperty(name = JBase.GUID, isNotNull = true, isReadOnly = true)
	public final Guid getGuid() {
		
		if(this.guid == null) {
			guid = new Guid(this.getProperty(JBase.GUID).toString());
		}
		
		return guid;
	}


	/*
	 * NAME
	 */
	
	private String name = null;
	
	@DatabaseProperty(name = JBase.NAME, isNotNull = true)
	public String getName() {
		
		if(this.name == null) {
			this.name = this.getProperty(JBase.NAME);
		}
		
		return this.name;
	}

	protected void setName(String value) {
		
		this.name = value;
		this.setProperty(JBase.NAME, this.name);
		
	}

	/*
	 * ENABLE
	 */

	
	@DatabaseProperty(name = JBase.ENABLE, type = OType.BOOLEAN)
	protected void setEnable(Boolean enable) {
		
		this.reload();
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
	
	
	/*
	 * 
	 */
	
	
	public final static <T extends BaseAbstract> T getInstance(final ModelClass<T> target, final OrientVertex ov){
		
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

	public static class ModelClass<T extends BaseAbstract> {

		private final Class<T> clasz;
		private final ModelClassAnnotation modelClassAnnotation;

		public ModelClass(Class<T> clasz) {

			this.clasz = clasz;
			this.modelClassAnnotation = this.clasz.getAnnotation(ModelClassAnnotation.class);
		}

		public String getClassName() {
			return this.clasz.getSimpleName();
		}

		public Class<T> getClasz() {
			return this.clasz;
		}

		public T newInstance() throws IllegalAccessException, InstantiationException {
			return this.clasz.newInstance();
		}

		public boolean isAbstract() {
			return this.modelClassAnnotation.isAbstract();
		}

		public boolean isInstance(BaseAbstract baseAbstract) {
			return this.clasz.isInstance(baseAbstract);
		}

	}
}

