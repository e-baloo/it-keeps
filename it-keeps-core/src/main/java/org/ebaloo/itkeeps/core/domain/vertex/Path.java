

package org.ebaloo.itkeeps.core.domain.vertex;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.ebaloo.itkeeps.Guid;
import org.ebaloo.itkeeps.api.model.JBase;
import org.ebaloo.itkeeps.api.model.JBaseLight;
import org.ebaloo.itkeeps.api.model.JPath;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseVertrex;
import org.ebaloo.itkeeps.core.domain.annotation.ModelClassAnnotation;
import org.ebaloo.itkeeps.core.domain.edge.DirectionType;
import org.ebaloo.itkeeps.core.domain.edge.traverse.InGroup;

import com.tinkerpop.blueprints.impls.orient.OrientBaseGraph;




/**
 * 
 *
 */
@DatabaseVertrex()
@ModelClassAnnotation()
public class Path extends BaseStandard {

	protected Path() {
		super();
	}
	
	/*
	public Path(final BaseAbstract abase) {
		super(abase);
	}
	*/
	
	protected Path(String name) {
		super(name);
		
	}

	
	
	/*
	 * PARENT GROUP
	 */
	
	public Path getParent() {
		return this.getEdgeByClassesNames(Path.class, DirectionType.PARENT, false, InGroup.class);
	}
	
	public void setParent(final Path group) {
		setEdges(this.getGraph(), Path.class, this, group, DirectionType.PARENT, InGroup.class, false);
	}

	private void setParent(final JBaseLight path) {
		setParent(get(this.getGraph(), Path.class, path, false));
	}

	
	
	
	/*
	 * CHILD GROUP
	 */
	
	public List<Path> getChildGroup() {
		return this.getEdgesByClassesNames(Path.class, DirectionType.CHILD, false, InGroup.class);
	}
	
	public void setChildGroup(List<Path> list) {
		setEdges(this.getGraph(), Path.class, this, list, DirectionType.CHILD, InGroup.class, false);
	}

	private void setChildsJBL(List<JBaseLight> list) {
		
		if(list == null) 
			list = new ArrayList<JBaseLight>();

		// Optimization
		OrientBaseGraph graph = this.getGraph();
		
		setChildGroup(list.stream().map(e -> get(graph, Path.class, e, false)).collect(Collectors.toList())); 
	}

	
	
	

	
	
	// API
	
	public Path(final JPath j) {
		this(j, true);
	}
	
	protected Path(final JPath j, final boolean f) {
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
	public <T extends JBase> T read(T j, Guid requesteurGuid) {
		
		if(j == null)
			j = (T) new JPath();
		
		if(!(j instanceof JPath))
			throw new RuntimeException("TODO"); //TODO
		
		super.read(j, requesteurGuid);

		JPath jpath = (JPath) j;
		
		jpath.setParent(getJBaseLight(this.getParent()));
		jpath.setChilds(this.getChildGroup().stream().map(e -> getJBaseLight(e)).collect(Collectors.toList()));
		
		return j;
	}
	
	@Override
	public <T extends JBase> T update(T j, Guid requesteurGuid) {
		
		if(!(j instanceof JPath))
			throw new RuntimeException("TODO"); //TODO

		super.update(j, requesteurGuid);
		
		User requesterUser = User.get(this.getGraph(), User.class, requesteurGuid, false);

		switch(requesterUser.getRole()) {

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
		

		JPath jpath = (JPath) j;
		
		
		if(jpath.isPresentParent())
			this.setParent(jpath.getParent());

		if(jpath.isPresentChilds())
			this.setChildsJBL(jpath.getChilds());
		
		
		return read(null, requesteurGuid);
	}
}



