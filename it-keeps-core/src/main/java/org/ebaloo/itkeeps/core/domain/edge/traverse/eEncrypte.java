
package org.ebaloo.itkeeps.core.domain.edge.traverse;

import org.ebaloo.itkeeps.core.database.annotation.DatabaseEdge;

import com.tinkerpop.blueprints.impls.orient.OrientEdge;

@DatabaseEdge()
public class eEncrypte extends eTraverse {

	public eEncrypte(OrientEdge oe) throws Exception {
		super(oe);
	}
	
	public eEncrypte() {
		super();
	}

}
