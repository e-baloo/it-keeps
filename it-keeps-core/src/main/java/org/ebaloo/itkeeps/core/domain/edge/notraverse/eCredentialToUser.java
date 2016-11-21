
package org.ebaloo.itkeeps.core.domain.edge.notraverse;

import org.ebaloo.itkeeps.core.database.annotation.DatabaseEdge;

import com.tinkerpop.blueprints.impls.orient.OrientEdge;

@DatabaseEdge()
public class eCredentialToUser extends eNoTraverse {

	public eCredentialToUser(OrientEdge oe) throws Exception {
		super(oe);
	}
	
	public eCredentialToUser() {
		super();
	}

}
