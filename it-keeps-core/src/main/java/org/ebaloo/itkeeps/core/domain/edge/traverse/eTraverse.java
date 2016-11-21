
package org.ebaloo.itkeeps.core.domain.edge.traverse;

import org.ebaloo.itkeeps.core.database.annotation.DatabaseEdge;
import org.ebaloo.itkeeps.core.domain.edge.eRelation;

import com.tinkerpop.blueprints.impls.orient.OrientEdge;

@DatabaseEdge()
public class eTraverse extends eRelation {

	public eTraverse(OrientEdge oe) throws Exception {
		super(oe);
	}
	
	public eTraverse() {
		super();
	}

}
