

package org.ebaloo.itkeeps.core.domain.vertex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.tinkerpop.blueprints.Direction;
import org.apache.commons.lang.StringUtils;
import org.ebaloo.itkeeps.Guid;
import org.ebaloo.itkeeps.api.model.jBase;
import org.ebaloo.itkeeps.api.model.jBaseLight;
import org.ebaloo.itkeeps.core.database.GraphFactory;
import org.ebaloo.itkeeps.core.domain.edge.eRelation;
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


abstract class vBaseAbstract extends vCommon {

	private static Logger logger = LoggerFactory.getLogger(vBaseAbstract.class);
	

	protected final static <T extends vBaseAbstract> List<T> commandBaseAbstract(OrientBaseGraph graph, final Class<T> target, final String cmdSQL, Object... args) {
		return GraphFactory.command(graph, cmdSQL, args).stream().map(e -> vBaseAbstract.getInstance(target, e)).collect(Collectors.toList());
	}

	
	public static final <T extends vBaseAbstract> T get(OrientBaseGraph graph, final Class<T> target, final jBaseLight baselight, boolean isInstanceof)  
 	{
    	if(baselight == null || !baselight.isPresentGuid())
    		return null;
    	
		return get(graph, target, baselight.getGuid(), isInstanceof);
	}
	
	
   

	public static final <T extends vBaseAbstract> T get(final OrientBaseGraph graph, final Class<T> target, final Guid guid, boolean isInstanceof)  
	{
    	if(guid == null)
    		return null;
		
		String cmdSQL = "SELECT FROM " + target.getSimpleName() + " WHERE " + WhereClause.ENABLE_IS_TRUE + " AND " + WhereClause.IsntanceOf(target, isInstanceof) + " AND (" + jBase.GUID + "= ?)";
		
		List<T> list = vBaseAbstract.commandBaseAbstract(graph, target, cmdSQL, guid.toString());
		
		if(list.size() == 0) {
			return null;
		}
		
		if(list.size() > 1) {
			throw new RuntimeException(String.format("guid is not unique : %s", guid));
		}
		
		return list.get(0);
	}
	
	
	public static final <T extends vBaseAbstract> T get(final OrientBaseGraph graph, final Class<T> target, final String name, boolean isInstanceof)  
	{
    	if(StringUtils.isEmpty(name))
    		return null;
		
    	if(Guid.isGuid(name))
    		return get(graph, target, new Guid(name), isInstanceof); 
    	
		String cmdSQL = "SELECT FROM " + target.getSimpleName() + " WHERE " + WhereClause.ENABLE_IS_TRUE + " AND " + WhereClause.IsntanceOf(target, isInstanceof) + " AND (" + jBase.NAME + "= ?)";
		
		List<T> list = vBaseAbstract.commandBaseAbstract(graph, target, cmdSQL, name);
		
		if(list.size() == 0) {
			return null;
		}
		
		if(list.size() > 1) {
			throw new RuntimeException(String.format("name is not unique : %s @class:%s", name, target.getSimpleName()));
		}
		
		return list.get(0);
	}

	
	
    
    protected static <T extends vBaseAbstract> List<T> getListBaseAbstract(OrientBaseGraph graph, final Class<T> target, final List<Guid> guid) {

		String cmdSQL = "SELECT FROM " + vBase.class.getSimpleName() + " WHERE " + WhereClause.ENABLE_IS_TRUE
				+ " AND (guid IN [" + guid.stream().map(e -> "'" + e.toString() + "'").collect(Collectors.joining(","))
				+ "])";

		return vBaseAbstract.commandBaseAbstract(graph, target, cmdSQL);
	}
    
    
	
	
	protected vBaseAbstract() {
		;
	}
	
	protected vBaseAbstract(boolean bool) {
		newOrientVertex();
	}

	
	/*
	 * 
	 */

	protected final static <T extends vBaseAbstract, D extends vBaseAbstract, R extends eRelation> void setEdges(OrientBaseGraph graph, final Class<T> target, T src, D dst, DirectionType direction, Class<R> relation, boolean instanceOf) {

		List<T> listSrc = new ArrayList<T>();
		if(src != null)
			listSrc.add(src);

		List<D> listDst = new ArrayList<D>();
		if(dst != null)
			listDst.add(dst);

		setEdges(graph, target, listSrc, listDst, direction, relation, instanceOf);
	}

	protected final static <T extends vBaseAbstract, D extends vBaseAbstract, R extends eRelation> void setEdges(OrientBaseGraph graph, final Class<T> target, T src, List<D> dst, DirectionType direction, Class<R> relation, boolean instanceOf) {

		List<T> listSrc = new ArrayList<T>();
		if(src != null)
			listSrc.add(src);
		
		setEdges(graph, target, listSrc, dst, direction, relation, instanceOf);
	}
	
	protected final static <T extends vBaseAbstract, D extends vBaseAbstract, R extends eRelation> void setEdges(OrientBaseGraph graph, final Class<T> target, List<T> src, List<D> dst, DirectionType direction, Class<R> relation, boolean instanceOf) {

		if(direction == null) 
			throw new RuntimeException("direction is null!");

		if(relation == null) 
			throw new RuntimeException("relation is null!");

		if(src == null)
			src = new ArrayList<T>();
		
		if(dst == null)
			dst = new ArrayList<D>();
		

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

			cmdSQL = cmdSQL + " AND " + WhereClause.ENABLE_IS_TRUE;  

			if(target != null)
				cmdSQL = cmdSQL + " AND " + WhereClause.IsntanceOf(target, instanceOf);  
			
			
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
				vCommon.executeNoReturn(graph, cmd);
			}
			
			if(ovDstAdd.size() > 0) {
		    	String cmd = "CREATE EDGE " + relation.getSimpleName() + " FROM [" + StringUtils.join(ovSrc, ", ") + "] TO [" + StringUtils.join(ovDstAdd, ", ") + "] ";
				vCommon.executeNoReturn(graph, cmd);
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
    public final boolean equals(Object obj) {
        try {
		
			if(obj == null)
				return false;
			
			if(!(obj instanceof vBaseAbstract))
				return false;
			
			return this.getORID().equals(((vBaseAbstract)obj).getORID());
			
			
          } catch (Throwable e) {
                return false;
          }
    }


	
	protected final <T extends vBaseAbstract, R extends eRelation> T getEdgeByClassesNames(final Class<T> target, final DirectionType direction, final boolean isInstanceof, Class<R> relation) {
		
		List<T> list = getEdgesByClassesNames(target, direction, isInstanceof, relation);
		
		if(list.size() < 1) {
			return null;
		}
		
		if(list.size() > 1) {
			throw new RuntimeException("For ["  + this.toString() + "] have note unique Edge for " + eToLog(direction.getDirection()) + " " + target.getSimpleName());
		}
			
		return list.get(0);
	}

	
	public static final String eToLog(DirectionType dir) {
		return eToLog(dir.getDirection());
	}
	
	public static final String eToLog(Direction dir) {
		switch(dir) {
		
		case IN:
			return "<--";
			
		case OUT:
			return "-->";
			
		case BOTH:
			return "<->";
		}
		
		return "?-?";
	}
	
	
	
	
	/*
	 * 
	 */
	
	
	protected final <T extends vBaseAbstract, R extends eRelation> List<T> getEdgesByClassesNames(final Class<T> target, final DirectionType relationship, final boolean isInstanceof, Class<R> relation) {

			if((target == null) || (relationship == null)) {
				throw new RuntimeException("TODO"); //TODO
			}
	
			
			StringBuilder request = new StringBuilder();
			
			request.append("SELECT FROM ");
			request.append("(TRAVERSE " + relationship.getDirection().toString() + "('" + (relation != null ? relation.getSimpleName() : "") + "') FROM " + this.getORID() + " WHILE $depth <= 1)");
			request.append(" WHERE ");
			request.append(WhereClause.ENABLE_IS_TRUE);
			request.append(" AND ");
			request.append(WhereClause.IsntanceOf(target, isInstanceof));
			request.append(" AND ");
			request.append("(@rid <> " + this.getORID() + ")");
			
			
			
			return  vBaseAbstract.commandBaseAbstract(this.getGraph(), target, request.toString());
			
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



	
	// TODO
	// TODO
	// TODO
	// TODO
		/*
    protected final <T extends BaseAbstract, R extends eRelation> void setEdge(
    		final Class<T> target, final DirectionType relationship, final boolean isInstanceof, final  T newParent, final Class<R> relation) {
		

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
    
*/
    



	
	
	/*
	 * 
	 */
	
	
	public final static <T extends vBaseAbstract> T getInstance(final Class<T> target, final OrientVertex ov){
		
		try {
			
			T baseAbstract;

			baseAbstract = target.newInstance();
			baseAbstract.setOrientVertex(ov);
			
			return baseAbstract;

		} catch (Exception e) {
			
			if(logger.isDebugEnabled())
				e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}

	
	public static class WhereClause {
		
		public static String ENABLE_IS_TRUE = " (" + jBase.ENABLE + " = true) ";
		

		public final static <T extends vBaseAbstract> String IsntanceOf(final Class<T> target, final boolean instanceOf) {
			return String.format(" (@class %s '%s') ", instanceOf ? "INSTANCEOF" : "=", target.getSimpleName());
		}
		
	}


}

