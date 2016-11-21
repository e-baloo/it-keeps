
package org.ebaloo.itkeeps.core.domain.edge;

import org.ebaloo.itkeeps.core.database.annotation.DatabaseEdge;

import com.tinkerpop.blueprints.impls.orient.OrientEdge;

@DatabaseEdge()
public class eRelation {

	protected OrientEdge oe = null;

	public eRelation(OrientEdge oe) {
		this.setOrientEdge(oe);
	}
	
	public eRelation() {
		
	}

	public String getType() {
		this.checkOrientEdge();
		
		return oe.getType().getName();
	}

	public void setOrientEdge(OrientEdge oe) {
		if(this.oe != null) {
			// TODO
			throw new RuntimeException(new Exception("TODO"));
		}
		
		this.oe = oe;
	}
	
	
	protected void checkOrientEdge() {
		if(this.oe == null) {
			// TODO
			throw new RuntimeException(new Exception("TODO"));
		}
	}
	
}
