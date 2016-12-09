

package org.ebaloo.itkeeps.core.domain.vertex;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.ebaloo.itkeeps.api.model.jBaseLight;
import org.ebaloo.itkeeps.api.model.jGroup;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseVertrex;
import org.ebaloo.itkeeps.core.domain.edge.DirectionType;
import org.ebaloo.itkeeps.core.domain.edge.traverse.eInGroup;

import com.tinkerpop.blueprints.impls.orient.OrientBaseGraph;


/**
 * 
 *
 */
@DatabaseVertrex()
public final class vGroup extends vBaseChildAcl {

	vGroup() {
		super();
	}
	
	/*
	 * PARENT
	 */

	private jBaseLight getParent() {
		vGroup child = this.getEdge(vGroup.class, DirectionType.CHILD, false, eInGroup.class);
		return child == null ? null : getJBaseLight(child);
	}

	
	private void setParent(final jBaseLight group) {
		vGroup child = get(this.getGraph(), vGroup.class, group, false);
		setEdges(this.getGraph(), vGroup.class, this, vGroup.class, child, DirectionType.CHILD, eInGroup.class, false);
	}

	
	/*
	 * CHILDS
	 */
	
	private List<jBaseLight> getChilds() {
		return this.getEdges(vGroup.class, DirectionType.PARENT, false, eInGroup.class).stream()
				.map(vBase::getJBaseLight).collect(Collectors.toList());

	}
	
	private void setChilds(List<jBaseLight> list) {

		if (list == null)
			list = new ArrayList<>();

		// Optimization
		OrientBaseGraph graph = this.getGraph();

		setEdges(graph, vGroup.class, this, vGroup.class,
				list.stream().map(e -> get(graph, vGroup.class, e, false)).collect(Collectors.toList()),
				DirectionType.PARENT, eInGroup.class, false);

	}
	
	
	// API
	
	vGroup(final jGroup j) {
		super(j);

		try {
		this.setParent(j.getParent());
		this.setChilds(j.getChilds());
	} catch (Exception e) {
		this.delete();
		throw e;
	}
	}

	

	final jGroup read() {
		
		jGroup j = new jGroup();
		this.readBaseStandard(j);
		
		j.setParent(this.getParent());
		j.setChilds(this.getChilds());
		
		this.readAcl(j);
		
		return j;
	}

	final void update(jGroup j) {
		
		this.updateBaseStandard(j);
			
		if(j.isPresentParent())
			this.setParent(j.getParent());

		if(j.isPresentChilds())
			this.setChilds(j.getChilds());
	
	}

}



