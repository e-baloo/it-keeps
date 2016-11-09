
package org.ebaloo.itkeeps.domain.edge;

import org.ebaloo.itkeeps.database.annotation.DatabaseEdge;

import com.tinkerpop.blueprints.impls.orient.OrientEdge;

@DatabaseEdge()
public class Relation implements RelationInterface {

	protected OrientEdge oe = null;

	public Relation(OrientEdge oe) {
		this.setOrientEdge(oe);
	}
	
	public Relation() {
		
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
