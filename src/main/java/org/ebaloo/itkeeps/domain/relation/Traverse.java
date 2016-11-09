
package org.ebaloo.itkeeps.domain.relation;

import org.ebaloo.itkeeps.database.annotation.DatabaseEdge;

import com.tinkerpop.blueprints.impls.orient.OrientEdge;

@DatabaseEdge()
public class Traverse extends Relation {

	public Traverse(OrientEdge oe) throws Exception {
		super(oe);
	}
	
	public Traverse() {
		super();
	}

}
