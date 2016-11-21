
package org.ebaloo.itkeeps.core.domain.edge.traverse;

import org.ebaloo.itkeeps.core.database.annotation.DatabaseEdge;

import com.tinkerpop.blueprints.impls.orient.OrientEdge;

@DatabaseEdge()
public class eInGroup extends eAclRelation {

	public eInGroup(OrientEdge oe) throws Exception {
		super(oe);
	}
	
	public eInGroup() {
		super();
	}

}
