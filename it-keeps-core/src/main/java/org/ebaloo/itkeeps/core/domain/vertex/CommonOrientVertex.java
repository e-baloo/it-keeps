
package org.ebaloo.itkeeps.core.domain.vertex;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.impls.orient.OrientBaseGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.ebaloo.itkeeps.core.database.GraphFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.orientechnologies.orient.core.metadata.schema.OClass;

/**
 * 
 *
 */
public abstract class CommonOrientVertex {

	private static Logger logger = LoggerFactory.getLogger(CommonOrientVertex.class);

	protected static List<OrientVertex> command(OrientBaseGraph graph, String cmdSQL, Object... args) {
		return GraphFactory.command(graph, cmdSQL, args);
	}

	protected static int execute(OrientBaseGraph graph, String cmdSQL, Object... args) {
		return GraphFactory.execute(graph, cmdSQL, args);
	}

	protected static void executeNoReturn(OrientBaseGraph graph, String cmdSQL, Object... args) {
		GraphFactory.executeNoReturn(graph, cmdSQL, args);
	}

	protected static final List<String> getAllSuperClassesNames(OrientVertex ov) {
		List<String> list = new ArrayList<String>();

		for (OClass oc : ov.getType().getAllSuperClasses()) {
			list.add(oc.getName());
		}

		return list;
	}

	public static final boolean isORID(String str) {
		return str.matches("#\\d+:\\d+");
	}


	private OrientVertex orientVertex = null;


	protected final void checkOrientVertex() {
		if (this.orientVertex == null) {
			
			if(logger.isDebugEnabled())
				logger.debug("checkOrientVertex() : 2nd chance!");
			
			this.reload();

			if (this.orientVertex == null) {
				throw new RuntimeException("The \"OrientVertex value\" value is null!");
			}
		}
	}

	public final void commit() {

		this.getOrientVertex().getGraph().commit();

	}

	protected final int deleteAllEdges(final Direction direction) {

		String cmd = "DELETE EDGE ";

		switch (direction) {

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

		int ret = execute(this.getGraph(), cmd);

		this.reload();

		return ret;
	}

	protected final OrientBaseGraph getGraph() {

		if (!this.hasOrientVertex()) {
			return GraphFactory.getOrientBaseGraph();
		}

		return this.getOrientVertex().getGraph();
	}

	/*
	 * ORID
	 */
	
	private String orid = null;

	protected final String getORID() {

		if (StringUtils.isBlank(orid))
			this.orid = this.getOrientVertex().getIdentity().toString();

		return orid;
	}

	
	
	
	protected final int getObjectVersion() {

		return this.getProperty("@version");
	}


	protected final OrientVertex getOrientVertex() {
		this.checkOrientVertex();
		return this.orientVertex;
	}

	protected final String getOrientVertexType() {
		return this.getOrientVertex().getType().getName();
	}

	protected final <T> T getProperty(final String key) {
		return this.getOrientVertex().getProperty(key);
	}

	protected final OrientVertexType getVertexType() {

		OrientVertexType ovt = this.getGraph().getVertexType(this.getClass().getSimpleName());

		if (ovt == null) {

			throw new RuntimeException("The \"ovt\" is null for @" + this.getClass().getSimpleName() + "!");
		}

		return ovt;
	}

	protected final boolean hasOrientVertex() {

		return this.orientVertex != null;

	}

	protected final String OrientVertexToJson() {
		return this.getOrientVertex().getRecord().toJSON();
	}

	protected final void reload() {

		String cmdSQL = "SELECT FROM " + this.getORID();

		List<OrientVertex> list = command(this.getGraph(), cmdSQL);

		if (list.size() == 0) {
			this.orientVertex = null;
			return;
		}

		if (list.size() > 1) {
			throw new RuntimeException(new Exception(String.format("ORID is not unique : %s", this.getORID())));
		}

		this.orientVertex = list.get(0);

	}



	protected final void setOrientVertex(CommonOrientVertex commonOrientVertrex) {

		if (commonOrientVertrex == null)
			throw new RuntimeException("The \"commonOrientVertrex\" value is null!");
		

		this.setOrientVertex(commonOrientVertrex.getOrientVertex());

	}

	protected final void setOrientVertex(OrientVertex ov) {
		
		if (ov == null) 
			throw new RuntimeException("The \"ov\" value is null!");
		

		if (this.orientVertex != null) 
			throw new RuntimeException("The \"OrientVertex\" value is not null!");
		

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

	protected final void setProperty(final String key, final Object iValue) {

		Object value = iValue;

		if (value instanceof String) {
			value = value == null ? "" : StringUtils.strip((String) value);
		}
		this.getOrientVertex().setProperty(key, value);

		this.commit();
	}

	public String toString() {

		if (this.orientVertex == null) {
			return this.getClass().getSimpleName() + " [ov:error]";
		}

		return this.getOrientVertex().toString();

	}

}
