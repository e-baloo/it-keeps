

package org.ebaloo.itkeeps.core.domain.vertex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.tinkerpop.blueprints.Direction;
import org.apache.commons.lang.StringUtils;
import org.ebaloo.itkeeps.Rid;
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
    	if(baselight == null || !baselight.isPresentRid())
    		return null;
    	
		return get(graph, target, baselight.getRid(), isInstanceof);
	}
	
	
   

	public static final <T extends vBaseAbstract> T get(final OrientBaseGraph graph, final Class<T> target, final Rid rid, boolean isInstanceof)  
	{
    	if(rid == null)
    		return null;
		
		String cmdSQL = "SELECT FROM " + rid.get() + " WHERE " + WhereClause.IsntanceOf(target, isInstanceof);
		
		List<T> list = vBaseAbstract.commandBaseAbstract(graph, target, cmdSQL);
		
		if(list.size() == 0) {
			return null;
		}
		
		if(list.size() > 1) {
			throw new RuntimeException(String.format("rid is not unique : %s", rid));
		}
		
		return list.get(0);
	}
	
	
	public static final <T extends vBaseAbstract> T get(final OrientBaseGraph graph, final Class<T> target, final String name, boolean isInstanceof)  
	{
    	if(StringUtils.isEmpty(name))
    		return null;
		
    	if(Rid.is(name))
    		return get(graph, target, new Rid(name), isInstanceof); 
    	
		String cmdSQL = "SELECT FROM " + target.getSimpleName() + " WHERE " + WhereClause.IsntanceOf(target, isInstanceof) + " AND (" + jBase.NAME + "= ?)";
		
		List<T> list = vBaseAbstract.commandBaseAbstract(graph, target, cmdSQL, name);
		
		if(list.size() == 0) {
			return null;
		}
		
		if(list.size() > 1) {
			throw new RuntimeException(String.format("name is not unique : %s @class:%s", name, target.getSimpleName()));
		}
		
		return list.get(0);
	}

	
	
    
    protected static <T extends vBaseAbstract> List<T> getListBaseAbstract(OrientBaseGraph graph, final Class<T> target, final List<Rid> ridList) {

    	StringBuilder request = new StringBuilder();
    	
    	request.append("SELECT FROM [");
    	request.append(ridList.stream().map(e -> e.get()).collect(Collectors.joining(",")));
    	request.append("] WHERE @class INSTANCEOF ");
    	request.append(vBase.class.getSimpleName());

		return vBaseAbstract.commandBaseAbstract(graph, target, request.toString());
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

	protected final static <T extends vBaseAbstract, D extends vBaseAbstract, R extends eRelation> void setEdges(OrientBaseGraph graph, final Class<T> srcTarget, T src, final Class<D> dstTarget, D dst, DirectionType direction, Class<R> relation, boolean instanceOf) {

		List<T> listSrc = new ArrayList<T>();
		if(src != null)
			listSrc.add(src);

		List<D> listDst = new ArrayList<D>();
		if(dst != null)
			listDst.add(dst);

		setEdges(graph, srcTarget, listSrc, dstTarget, listDst, direction, relation, instanceOf);
	}

	protected final static <T extends vBaseAbstract, D extends vBaseAbstract, R extends eRelation> void setEdges(OrientBaseGraph graph, final Class<T> srcTarget, T src, final Class<D> dstTarget, List<D> dst, DirectionType direction, Class<R> relation, boolean instanceOf) {

		List<T> listSrc = new ArrayList<T>();
		if(src != null)
			listSrc.add(src);
		
		setEdges(graph, srcTarget, listSrc, dstTarget, dst, direction, relation, instanceOf);
	}
	
	protected final static <T extends vBaseAbstract, D extends vBaseAbstract, R extends eRelation> void setEdges(OrientBaseGraph graph, final Class<T> srcTarget, List<T> src, final Class<D> dstTarget, List<D> dst, DirectionType direction, Class<R> relation, boolean instanceOf) {

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

			StringBuilder sb = new StringBuilder();
			
			sb.append("SELECT FROM (TRAVERSE ");
			sb.append(direction.getDirection().toString());
			sb.append("('");
			sb.append(relation.getSimpleName());
			sb.append("') FROM [");
			sb.append(StringUtils.join(srcOrid, ","));
			sb.append("] WHILE $depth <= 1) WHERE @rid NOT IN [");
			sb.append(StringUtils.join(srcOrid, ","));
			sb.append("]");

			if(dstTarget != null) {
				sb.append(" AND ");
				sb.append(WhereClause.IsntanceOf(dstTarget, instanceOf));
			}
			
			
			ovDstBck = command(graph, sb.toString()).stream().map(e -> e.getIdentity().toString()).collect(Collectors.toList());
			
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
				
				StringBuilder sb1 = new StringBuilder();
				
				sb1.append("DELETE EDGE FROM [");
				sb1.append(StringUtils.join(ovSrc, ","));
				sb1.append("] TO [");
				sb1.append(StringUtils.join(ovDstRemove, ","));
				sb1.append("] WHERE @class = '");
				sb1.append(relation.getSimpleName());
				sb1.append("'");
				
				vCommon.executeNoReturn(graph, sb1.toString());
			}
			
			if(ovDstAdd.size() > 0) {
				
				StringBuilder sb2 = new StringBuilder();

				sb2.append("CREATE EDGE ");
				sb2.append(relation.getSimpleName());
				sb2.append(" FROM [");
				sb2.append(StringUtils.join(ovSrc, ","));
				sb2.append("] TO [");
				sb2.append(StringUtils.join(ovDstAdd, ","));
				sb2.append("]");
				
				vCommon.executeNoReturn(graph, sb2.toString());
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


	
	protected final <T extends vBaseAbstract, R extends eRelation> T getEdge(final Class<T> target, final DirectionType direction, final boolean isInstanceof, Class<R> relation) {
		
		List<T> list = getEdges(target, direction, isInstanceof, relation);
		
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
	
	
	protected final <T extends vBaseAbstract, R extends eRelation> List<T> getEdges(final Class<T> target, final DirectionType relationship, final boolean isInstanceof, Class<R> relation) {

			if((target == null) || (relationship == null)) {
				throw new RuntimeException("TODO"); //TODO
			}
	
			
			StringBuilder request = new StringBuilder();
			
			request.append("SELECT FROM ");
			request.append("(TRAVERSE " + relationship.getDirection().toString() + "('" + (relation != null ? relation.getSimpleName() : "") + "') FROM " + this.getORID() + " WHILE $depth <= 1)");
			request.append(" WHERE ");
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
	
	private Rid rid = null;
	
	public final Rid getRid() {
		
		if(rid == null)
			rid = new Rid(this.getORID());
		
		return rid;
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

			logger.error(String.format("targer = %s", target));

			if(logger.isDebugEnabled())
				e.printStackTrace();
			
			throw new RuntimeException(e);
		}
		
	}

	
	public static class WhereClause {
		
		public final static <T extends vBaseAbstract> String IsntanceOf(final Class<T> target, final boolean instanceOf) {
			return String.format(" (@class %s '%s') ", instanceOf ? "INSTANCEOF" : "=", target.getSimpleName());
		}
		
	}


	/*
	 * 
	 */
	protected void delete() {
		this.reload();
		logger.info("delete : " + this.toString());
		this.getOrientVertex().remove();
	}
	


}

