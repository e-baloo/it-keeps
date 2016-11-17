
package org.ebaloo.itkeeps.core.domain.edge.notraverse;

import org.ebaloo.itkeeps.core.database.annotation.DatabaseEdge;

import com.tinkerpop.blueprints.impls.orient.OrientEdge;

@DatabaseEdge()
public class CredentialToUser extends NoTraverse {

	public CredentialToUser(OrientEdge oe) throws Exception {
		super(oe);
	}
	
	public CredentialToUser() {
		super();
	}

}
