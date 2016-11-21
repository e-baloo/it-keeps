
package org.ebaloo.itkeeps.core.domain.edge.traverse;

import org.ebaloo.itkeeps.core.database.annotation.DatabaseEdge;

import com.tinkerpop.blueprints.impls.orient.OrientEdge;

@DatabaseEdge()
public class eAclRelation extends eTraverse {

	public eAclRelation(OrientEdge oe) throws Exception {
		super(oe);
	}
	
	public eAclRelation() {
		super();
	}

}
