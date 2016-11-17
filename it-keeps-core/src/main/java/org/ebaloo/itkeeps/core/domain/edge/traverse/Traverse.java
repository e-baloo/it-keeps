
package org.ebaloo.itkeeps.core.domain.edge.traverse;

import org.ebaloo.itkeeps.core.database.annotation.DatabaseEdge;
import org.ebaloo.itkeeps.core.domain.edge.Relation;

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
