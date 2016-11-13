
package org.ebaloo.itkeeps.core.domain.edge;

import org.ebaloo.itkeeps.core.database.annotation.DatabaseEdge;

import com.tinkerpop.blueprints.impls.orient.OrientEdge;

@DatabaseEdge()
public class NoTraverse extends Relation {

	public NoTraverse(OrientEdge oe) throws Exception {
		super(oe);
	}
	
	public NoTraverse() {
		super();
	}

}
