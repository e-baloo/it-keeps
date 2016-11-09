
package org.ebaloo.itkeeps.domain;

import org.ebaloo.itkeeps.database.DatabaseEdge;

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
