

package org.ebaloo.itkeeps.core.domain.vertex;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.ebaloo.itkeeps.Guid;
import org.ebaloo.itkeeps.api.model.jBase;
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
		return this.getEdgeByClassesNames(vPath.class, DirectionType.PARENT, false, eInPath.class);
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
		return this.getEdgesByClassesNames(vPath.class, DirectionType.CHILD, false, eInPath.class);
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
		this(j, true);
	}
	
	protected vPath(final jPath j, final boolean f) {
		super(j, false);
		
		this.commit();
		this.reload();
		
		this.setParent(j.getParent());
		this.setChildsJBL(j.getChilds());

		if(f)
			this.setEnable(Boolean.TRUE);
	}

	

	@SuppressWarnings("unchecked")
	@Override
	public <T extends jBase> T read(T j, Guid requesteurGuid) {
		
		if(j == null)
			j = (T) new jPath();
		
		if(!(j instanceof jPath))
			throw new RuntimeException("TODO"); //TODO
		
		super.read(j, requesteurGuid);

		jPath jpath = (jPath) j;
		
		jpath.setParent(getJBaseLight(this.getParent()));
		jpath.setChilds(this.getChildsGroup().stream().map(e -> getJBaseLight(e)).collect(Collectors.toList()));
		
		return j;
	}
	
	@Override
	public <T extends jBase> T update(T j, Guid requesteurGuid) {
		
		if(!(j instanceof jPath))
			throw new RuntimeException("TODO"); //TODO

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
		
		super.update(j, requesteurGuid);

		jPath jpath = (jPath) j;
		
		if(jpath.isPresentParent())
			this.setParent(jpath.getParent());

		if(jpath.isPresentChilds())
			this.setChildsJBL(jpath.getChilds());
		
		return read(null, requesteurGuid);
	}
}



