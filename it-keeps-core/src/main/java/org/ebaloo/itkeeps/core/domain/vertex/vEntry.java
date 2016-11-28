
package org.ebaloo.itkeeps.core.domain.vertex;



import org.ebaloo.itkeeps.api.model.jBaseLight;
import org.ebaloo.itkeeps.api.model.jEntry;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseVertrex;
import org.ebaloo.itkeeps.core.domain.edge.DirectionType;
import org.ebaloo.itkeeps.core.domain.edge.traverse.eInPath;


@DatabaseVertrex()
public final class vEntry extends vBaseChildAcl {

	protected vEntry() {
		super();
	}
	
	

	/*
	 * PATH
	 */
	
	private jBaseLight getPath() {
		
		vPath path = this.getEdge(vPath.class, DirectionType.PARENT, false, eInPath.class);
		return path == null ? null : getJBaseLight(path);
		
	}
	
	private void setPath(jBaseLight path) {
		setEdges(
				this.getGraph(),
				vEntry.class, 
				this, 
				vPath.class, 
				get(this.getGraph(), vPath.class, path, false), 
				DirectionType.PARENT, 
				eInPath.class, 
				false);

	}

	

	// API

	vEntry(final jEntry j) {
		super(j);

		try {
			
			this.update(j);
			
	} catch (Exception e) {
		this.delete();
		throw e;
	}

	}

	
	final jEntry read() {
		
		jEntry j = new jEntry();

		this.readBaseStandard(j);
		
		j.setPath(this.getPath());
		
		this.readAcl(j);
		
		return j;
	}
	
	final void update(jEntry j) {

		this.updateBaseStandard(j);

		if(j.isPresentPath())
			this.setPath(j.getPath());

	}


	
}



