

package org.ebaloo.itkeeps.core.domain.vertex;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
final class vPath extends vBaseChildAcl {

	vPath() {
		super();
	}
	
	
	
	/*
	 * PARENT GROUP
	 */
	
	private jBaseLight getParent() {
		vPath parent = this.getEdge(vPath.class, DirectionType.PARENT, false, eInPath.class);
		return parent == null ? null : getJBaseLight(parent);
	}
	
	private void setParent(final jBaseLight path) {
		setEdges(this.getGraph(), vPath.class, this, vPath.class, get(this.getGraph(), vPath.class, path, false), DirectionType.PARENT, eInPath.class, false);
	}

	
	
	
	/*
	 * CHILD GROUP
	 */
	
	private List<jBaseLight> getChilds() {
		return this.getEdges(
				vPath.class,
				DirectionType.CHILD,
				false,
				eInPath.class
				).stream()
				.map(e -> getJBaseLight(e)).collect(Collectors.toList());
	}
	
	private void setChilds(List<jBaseLight> list) {
		
		if(list == null) 
			list = new ArrayList<jBaseLight>();

		// Optimization
		OrientBaseGraph graph = this.getGraph();
		
		setEdges(
				graph, 
				vPath.class, 
				this, 
				vPath.class, 
				list.stream().map(e -> get(graph, vPath.class, e, false)).collect(Collectors.toList()), 
				DirectionType.CHILD, 
				eInPath.class, 
				false);
	}

	
	
	

	
	
	// API
	
	vPath(final jPath j) {
		super(j);
		

		try {
			
			this.update(j);
			
		} catch (Exception e) {
			this.delete();
			throw e;
		}
	}

	

	jPath read() {
		
		jPath j = new jPath();

		this.readBaseStandard(j);

		j.setParent(this.getParent());
		j.setChilds(this.getChilds());
		
		this.readAcl(j);
		
		return j;
	}

	void update(jPath j) {
		
		this.updateBaseStandard(j);

		if(j.isPresentParent())
			this.setParent(j.getParent());

		if(j.isPresentChilds())
			this.setChilds(j.getChilds());
				
	}

}



