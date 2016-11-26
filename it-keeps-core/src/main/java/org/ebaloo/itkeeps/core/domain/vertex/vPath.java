

package org.ebaloo.itkeeps.core.domain.vertex;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.ebaloo.itkeeps.Guid;
import org.ebaloo.itkeeps.api.model.jBaseLight;
import org.ebaloo.itkeeps.api.model.jPath;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseVertrex;
import org.ebaloo.itkeeps.core.domain.edge.DirectionType;
import org.ebaloo.itkeeps.core.domain.edge.traverse.eInPath;

import com.tinkerpop.blueprints.impls.orient.OrientBaseGraph;




/**
 * 
 *
 */
@DatabaseVertrex()
public final class vPath extends vBaseChildAcl {

	protected vPath() {
		super();
	}
	
	
	
	/*
	 * PARENT GROUP
	 */
	
	public vPath getParent() {
		return this.getEdge(vPath.class, DirectionType.PARENT, false, eInPath.class);
	}
	
	public void setParent(final vPath path) {
		setEdges(this.getGraph(), vPath.class, this, vPath.class, path, DirectionType.PARENT, eInPath.class, false);
	}

	private void setParent(final jBaseLight path) {
		setParent(get(this.getGraph(), vPath.class, path, false));
	}

	
	
	
	/*
	 * CHILD GROUP
	 */
	
	protected List<vPath> getChildsGroup() {
		return this.getEdges(vPath.class, DirectionType.CHILD, false, eInPath.class);
	}
	
	protected void setChildsGroup(List<vPath> list) {
		setEdges(this.getGraph(), vPath.class, this, vPath.class, list, DirectionType.CHILD, eInPath.class, false);
	}

	private void setChildsJBL(List<jBaseLight> list) {
		
		if(list == null) 
			list = new ArrayList<jBaseLight>();

		// Optimization
		OrientBaseGraph graph = this.getGraph();
		
		setChildsGroup(list.stream().map(e -> get(graph, vPath.class, e, false)).collect(Collectors.toList())); 
	}

	
	
	

	
	
	// API
	
	public vPath(final jPath j) {
		super(j);
		

		try {
		this.setParent(j.getParent());
		this.setChildsJBL(j.getChilds());

		} catch (Exception e) {
			this.delete();
			throw e;
		}
	}

	

	public jPath read(Guid requesteurGuid) {
		
		jPath j = new jPath();

		this.readBaseStandard(j);

		jPath jpath = (jPath) j;
		
		jpath.setParent(getJBaseLight(this.getParent()));
		jpath.setChilds(this.getChildsGroup().stream().map(e -> getJBaseLight(e)).collect(Collectors.toList()));
		
		return j;
	}

	public jPath update(jPath j, Guid requesteurGuid) {
		
		vUser requesterUser = vUser.get(this.getGraph(), vUser.class, requesteurGuid, false);

		switch(requesterUser.getRole().value()) {

		case ROOT:
			// -> Ok is root
			break;

		case ADMIN:
			//TODO Check it requesterUser have the right to update this
			break;
			
		case USER:
		case GUEST:
		default:
			throw new RuntimeException("TODO"); //TODO
		}
		
		this.checkVersion(j);
		
		this.updateBaseStandard(j);

		jPath jpath = (jPath) j;
		
		if(jpath.isPresentParent())
			this.setParent(jpath.getParent());

		if(jpath.isPresentChilds())
			this.setChildsJBL(jpath.getChilds());
		
		return read(requesteurGuid);
	}
}



