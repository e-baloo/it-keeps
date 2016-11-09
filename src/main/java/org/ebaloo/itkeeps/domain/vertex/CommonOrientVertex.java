
package org.ebaloo.itkeeps.domain.vertex;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.impls.orient.OrientBaseGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

import java.util.ArrayList;
import java.util.List;


import org.apache.commons.lang.StringUtils;
import org.ebaloo.itkeeps.database.GraphFactory;
import org.ebaloo.itkeeps.domain.edge.RelationInterface;
import org.ebaloo.itkeeps.domain.edge.RelationTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.orientechnologies.orient.core.metadata.schema.OClass;

/**
 * 
 *
 */
public abstract class CommonOrientVertex  {

	private static Logger logger = LoggerFactory.getLogger(CommonOrientVertex.class);

	private OrientVertex orientVertex = null;

	
	protected final void checkOrientVertex() {
        if (this.orientVertex == null) {
            throw new RuntimeException("The \"OrientVertex value\" value is null!");
        }
	}

	public static boolean isORID(String str) {
		return str.matches("#\\d+:\\d+");
	}
	
	public final OrientVertex getOrientVertex() {
		try {
			
			this.checkOrientVertex();
			
		} catch (Throwable e) {
			
			throw new RuntimeException(e);
			
		}
		
		return this.orientVertex;
	}

	
	protected final void setOrientVertex(OrientVertex ov) {
		if(ov == null) {
			throw new RuntimeException("The \"ov\" value is null!");
		}

		if (this.orientVertex != null) {
			throw new RuntimeException("The \"OrientVertex\" value is not null!");
		}

		String classSimpleName = this.getClass().getSimpleName();

		List<String> listClasses = getAllSuperClassesNames(ov);
		listClasses.add(ov.getType().getName());

		if (listClasses.contains(classSimpleName)) {
			this.orientVertex = ov;
			this.getORID();
		} else {
			throw new RuntimeException("The class \"" + classSimpleName + "\"] is not in " + listClasses);
		}
		
	}
	
	
	protected final boolean hasOrientVertex() {
		
		return this.orientVertex != null;
		
	}
	
	
	public static final List<String> getAllSuperClassesNames(OrientVertex ov)
	{
		List<String> list = new ArrayList<String>();
		
		for(OClass oc : ov.getType().getAllSuperClasses())
		{
			list.add(oc.getName());
		}
		
		return list;
	}
	
	


	protected final void setOrientVertex(CommonOrientVertex commonOrientVertrex) {

		if(commonOrientVertrex == null) {
			throw new RuntimeException("The \"commonOrientVertrex\" value is null!");
		}
		
		this.setOrientVertex(commonOrientVertrex.getOrientVertex());
		
	}

	
	public final String OrientVertexToJson() {
		return this.getOrientVertex().getRecord().toJSON();
	}

	
	
	private String orid = null;
	
	
	public final String getORID() {
		
		if(StringUtils.isBlank(orid)) {
			
			this.orid = this.getOrientVertex().getIdentity().toString();
			
		}
		
		return orid;
	}

	public final <T> T getProperty(final String key) {
			return this.getOrientVertex().getProperty(key);
 	}

	protected final void setProperty(final String key, final Object iValue) {
		
		Object value = iValue;
		
		if(value instanceof String) {
			value = StringUtils.strip((String) value);
		}
		
		
		this.getOrientVertex().setProperty(key, value);
		
		this.commit();
	}

	protected final String getOrientVertexType() {
		return this.getOrientVertex().getType().getName();
	}

	
	protected final OrientBaseGraph getGraph() {
		
		if(!this.hasOrientVertex()) {
			return GraphFactory.getOrientBaseGraph();
		}

		return this.getOrientVertex().getGraph();
	}

	public final void commit() {
		
		this.getOrientVertex().getGraph().commit();
		
	}

	
	protected final OrientVertexType getVertexType() {
		
		OrientVertexType ovt = this.getGraph().getVertexType(this.getClass().getSimpleName());
		
		if (ovt == null) {

			throw new RuntimeException("The \"ovt\" is null for @" + this.getClass().getSimpleName() +  "!");
		}
		
		return ovt;
	}
	
	protected final Iterable<Edge> getEdges(CommonOrientVertex cov, Direction direction) {
		return this.getOrientVertex().getEdges(cov.getOrientVertex(), direction);
	}
	
	protected final Edge addEdge(CommonOrientVertex cov, Class<? extends RelationInterface> relation) {
		
		Edge edge = this.getOrientVertex().addEdge(null, cov.getOrientVertex(), relation.getSimpleName());
		
		this.commit();
		
		return edge;
	}
	
	
	
	protected static int execute(String cmdSQL, Object... args) {
		return GraphFactory.execute(cmdSQL, args);
	}
	
	protected static void executeNoReturn(String cmdSQL, Object... args) {
		GraphFactory.executeNoReturn(cmdSQL, args);
	}
	
	
	public static List<OrientVertex> command(String cmdSQL, Object... args) {
		return GraphFactory.command(cmdSQL, args);
	}
	
	public String toString() {
		
		if(this.orientVertex == null) {
			return this.getClass().getSimpleName() + " [ov:error]";
		}
			
		return this.getOrientVertex().toString();
		
	}
	

	protected final void removeEdge(CommonOrientVertex oldCommonOrientVertex, Direction direction, String relation) {
		
		if(oldCommonOrientVertex == null) {
			return;
		}

		this.removeEdge(oldCommonOrientVertex.getOrientVertex(), direction, relation);
	}
	
	private final void removeEdge(OrientVertex oldOrientVertex, Direction direction, String relation) {
		
		this.checkOrientVertex();
		
		
    	Iterable<Edge> iterable = null;
    	
    	if(StringUtils.isBlank(relation)) {
    		iterable = this.getOrientVertex().getEdges(oldOrientVertex, direction);
    	} else {
    		iterable = this.getOrientVertex().getEdges(oldOrientVertex, direction, relation);
    	}
    	
    	
		for(Edge e : iterable)
		{
			if(logger.isDebugEnabled()) {
				String comment = "Remove link ! [@" + this.toString() + "] " + RelationTools.toLogger(direction) + " [@" + oldOrientVertex.toString() + "]";
				logger.debug(comment);
			}
			
			e.remove();
		}
		
		this.commit();
		if(oldOrientVertex != null) {
			oldOrientVertex.getGraph().commit();
		}
	}

	protected final void addEdge(CommonOrientVertex newCommonOrientVertex, Direction direction, String relation) {
		if(newCommonOrientVertex == null) {
			return;
		}

		this.addEdge(newCommonOrientVertex.getOrientVertex(), direction, relation);
	}
	
	private final void addEdge(OrientVertex orientVertex, Direction direction, String relation) {
		
		if(orientVertex == null) {
			return;
		}
		
		
    	if(this.getOrientVertex().getEdges(orientVertex, direction).iterator().hasNext()) {
    		logger.debug("Link exist! [" + this.toString() + "] " + RelationTools.toLogger(direction) + " [" + orientVertex.toString() + "]");
    		return;
    	}
    	
    	OrientVertex ovSrc = null;
    	OrientVertex ovDst =  null;

    	switch(direction) {
    	
    	case OUT:
        	ovSrc = this.getOrientVertex();
        	ovDst =  orientVertex;
    		break;
    		
    	case IN:
        	ovSrc = orientVertex;
        	ovDst =  this.getOrientVertex(); 
    		break;
    		
		default:
			// TODO
			throw new RuntimeException(new Exception("BOTH relation is not autorized!"));
    		
    	}
    	
		if(logger.isDebugEnabled())  {
			String comment = "Add Link! [@" + this.toString() + "] " + RelationTools.toLogger(direction) + "(@" + relation + ")" + " [@" + orientVertex.toString() + "]";
			logger.debug(comment);
		}
    	
    	ovSrc.addEdge(null, ovDst, relation);
		this.commit();
		orientVertex.getGraph().commit();
		
	}	
	
	
	@SuppressWarnings("unused")
	protected final void setEdge(CommonOrientVertex newCommonOrientVertex, Direction direction, String relation) {
		
		this.checkOrientVertex();

		OrientVertex oldParnet = this.getEdgeByClassesNames(newCommonOrientVertex.getClass().getSimpleName(), direction, true);

		
		if(newCommonOrientVertex == null && oldParnet == null) {
			if(logger.isDebugEnabled()) {
				logger.debug("Old and new value is null!");
			}
			return;
		}

		
		if(newCommonOrientVertex != null && oldParnet != null && newCommonOrientVertex.getOrientVertex().equals(oldParnet))
		{
			if(logger.isDebugEnabled()) {
				logger.debug("Link exist ! [@" + this.toString() + "] " + RelationTools.toLogger(direction) + " [@" + newCommonOrientVertex.toString() + "]");
			}
			return; 
		}
		
		
		removeEdge(oldParnet, direction, null); 
		
    	addEdge(newCommonOrientVertex, direction, relation);
    	
    	this.commit();
    	
    	if(oldParnet != null) {
    		oldParnet.getGraph().commit();
    	}
    	
    	if(newCommonOrientVertex != null) {
    		newCommonOrientVertex.commit();
    	}
	}
	
	protected final OrientVertex getEdgeByClassesNames(final String targetClass, final Direction direction, final boolean isInstanceof) {
		
		List<OrientVertex> list = getEdgesByClassesNames(targetClass, direction, isInstanceof);
		
		if(list.size() < 1) {
			return null;
		}
		
		if(list.size() > 1) {
			logger.warn("For ["  + this.toString() + "] have note unique Edge for " + RelationTools.toLogger(direction) + " " + targetClass);
		}
			
		return list.get(0);
	}

	
	
	protected final List<OrientVertex> getEdgesByClassesNames(final String targetClass, final Direction direction, final boolean isInstanceof) {

		if((targetClass == null) || (direction == null)) {
			throw new RuntimeException( new Exception("TODO"));
		}
			
		StringBuilder request = new StringBuilder();
		
		request.append("SELECT FROM ");
		request.append("(TRAVERSE " + direction.toString() + "() FROM " + this.getORID() + " WHILE $depth <= 1)");
		request.append(" WHERE ");
		request.append("(@class " + (isInstanceof ? "INSTANCEOF" : "=") + " '" + targetClass + "')");
		request.append("AND ");
		request.append("(@rid <> " + this.getORID() + ")");
				
		
		return command(request.toString());

	}
	
	
	public final void reload() {
		
		String cmdSQL = "SELECT FROM " + this.getORID();
		
		List<OrientVertex> list = command(cmdSQL);
		
		if(list.size() == 0) {
			this.orientVertex = null;
			return;
		}
		
		if(list.size() > 1) {
			throw new RuntimeException(new Exception(String.format("ORID is not unique : %s", this.getORID())));
		}
		
		this.orientVertex = list.get(0);
		
	}
	
	
	
	protected final List<String> getOridByDirection(final Direction direction) {
		
		List<String> list = new ArrayList<String>();
		
		String cmd = "SELECT EXPAND(" + direction.toString() + "()) FROM " + this.getORID();
		
		for(OrientVertex ov : command(cmd)) {
			list.add(ov.getIdentity().toString());
		}
		
		return list;
	}
	

	protected final int deleteAllEdges(final Direction direction) {
		
		String cmd = "DELETE EDGE ";

		switch(direction) {
		
		case IN:
			
			cmd += "TO ";
			break;
			
		case OUT:

			cmd += "FROM ";
			break;

		case BOTH:
		default:
			
			new RuntimeException(new Exception("BOTH or emty is not otorized"));
			break;
		
		}

		cmd += this.getORID();
		
		
		int ret = execute(cmd);
		
		this.reload();
		
		return ret;
	}
	
}



