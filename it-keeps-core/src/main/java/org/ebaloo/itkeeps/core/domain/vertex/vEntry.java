
package org.ebaloo.itkeeps.core.domain.vertex;



import org.ebaloo.itkeeps.Rid;
import org.ebaloo.itkeeps.api.model.jBaseLight;
import org.ebaloo.itkeeps.api.model.jEntry;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseVertrex;
import org.ebaloo.itkeeps.core.domain.edge.DirectionType;
import org.ebaloo.itkeeps.core.domain.edge.traverse.eInPath;
import org.ebaloo.itkeeps.core.domain.vertex.SecurityFactory.SecurityAcl;
import org.ebaloo.itkeeps.core.domain.vertex.SecurityFactory.RID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@DatabaseVertrex()
public final class vEntry extends vBaseChildAcl {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(vEntry.class);

	
	protected vEntry() {
		super();
	}
	
	

	/*
	 * PATH
	 */
	
	public vPath getPath() {
		return this.getEdge(vPath.class, DirectionType.PARENT, false, eInPath.class);
	}
	
	protected void setPath(vPath path) {
		setEdges(this.getGraph(), vEntry.class, this, vPath.class, path, DirectionType.PARENT, eInPath.class, false);
	}

	protected void setPath(jBaseLight path) {
		setPath(get(this.getGraph(), vPath.class, path, false)); 
	}

	

	// API

	public vEntry(final jEntry j) {
		super(j);

		try {
		if(j.isPresentPath())
			this.setPath(j.getPath());

	} catch (Exception e) {
		this.delete();
		throw e;
	}

	}

	
	public jEntry read(Rid requesteurRid) {
		
		@SuppressWarnings("unused")
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, RID.get(this));
		
		// TODO Security
				
		
		jEntry j = new jEntry();

		this.readBaseStandard(j);
		
		j.setPath(getJBaseLight(this.getPath()));
		
		this.readAcl(j);
		
		return j;
	}
	
	public jEntry update(jEntry j, Rid requesteurRid) {
		return this.update(j, requesteurRid, false);
	}

	private jEntry update(jEntry j, Rid requesteurRid, boolean force) {

		
		@SuppressWarnings("unused")
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(requesteurRid, RID.get(this));

		//TODO Security

		this.checkVersion(j);
		
		this.updateBaseStandard(j);

		
		if(j.isPresentPath())
			this.setPath(j.getPath());

		return read(requesteurRid);
	}


	
}



