
package org.ebaloo.itkeeps.core.domain.edge.notraverse;

import org.ebaloo.itkeeps.core.database.annotation.DatabaseEdge;
import org.ebaloo.itkeeps.core.domain.edge.eRelation;

import com.tinkerpop.blueprints.impls.orient.OrientEdge;

@DatabaseEdge()
public class eNoTraverse extends eRelation {

	public eNoTraverse(OrientEdge oe) throws Exception {
		super(oe);
	}
	
	public eNoTraverse() {
		super();
	}

}
