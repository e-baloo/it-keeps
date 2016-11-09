
package org.ebaloo.itkeeps.domain.edge;

import org.ebaloo.itkeeps.database.annotation.DatabaseEdge;

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
