
package org.ebaloo.itkeeps.core.domain.edge;

import org.ebaloo.itkeeps.core.database.annotation.DatabaseEdge;

import com.tinkerpop.blueprints.impls.orient.OrientEdge;

@DatabaseEdge()
public class TraverseInGroup extends Traverse {

	public TraverseInGroup(OrientEdge oe) throws Exception {
		super(oe);
	}
	
	public TraverseInGroup() {
		super();
	}

}
