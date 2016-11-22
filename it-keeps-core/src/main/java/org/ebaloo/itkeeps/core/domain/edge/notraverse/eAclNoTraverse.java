
package org.ebaloo.itkeeps.core.domain.edge.notraverse;

import org.ebaloo.itkeeps.core.database.annotation.DatabaseEdge;

import com.tinkerpop.blueprints.impls.orient.OrientEdge;

@DatabaseEdge()
public class eAclNoTraverse extends eNoTraverse {

	public eAclNoTraverse(OrientEdge oe) throws Exception {
		super(oe);
	}
	
	public eAclNoTraverse() {
		super();
	}

}
