
package org.ebaloo.itkeeps.core.domain.vertex;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.ebaloo.itkeeps.api.model.jBaseChildAcl;
import org.ebaloo.itkeeps.api.model.jBaseLight;
import org.ebaloo.itkeeps.api.model.jBaseStandard;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseVertrex;
import org.ebaloo.itkeeps.core.domain.edge.DirectionType;
import org.ebaloo.itkeeps.core.domain.edge.traverse.eAclRelation;

import com.tinkerpop.blueprints.impls.orient.OrientBaseGraph;

@DatabaseVertrex()
public class vBaseChildAcl extends vBaseStandard {

	
	protected vBaseChildAcl() {
		super();
	}

	protected vBaseChildAcl(jBaseStandard j) {
		super(j);
	}

	
	private final List<vAcl> _getAcls() {
		return this.getEdges(vAcl.class, DirectionType.PARENT, false, eAclRelation.class);
	}
	
	protected final List<jBaseLight> getAcls() {
		return _getAcls().stream()
				.map(e -> getJBaseLight(e)).collect(Collectors.toList());

	}
	
	protected final void setAcls(List<jBaseLight> list) {

		if (list == null)
			list = new ArrayList<jBaseLight>();

		// Optimization
		OrientBaseGraph graph = this.getGraph();
		
		setEdges(graph, vBaseChildAcl.class, (vBaseChildAcl) this, vAcl.class,
				list.stream().map(e -> (vAcl) get(graph, vAcl.class, e, false)).collect(Collectors.toList()),
				DirectionType.PARENT, eAclRelation.class, false);

	}
	
	protected final <T extends jBaseChildAcl> void readAcl(T j) {
		j.setAcls(this.getAcls());
	}
	
	protected void delete() {
		_getAcls().forEach(e -> e.delete());
		
		super.delete();
	}
	
}



