
package org.ebaloo.itkeeps.core.domain.vertex;



import org.ebaloo.itkeeps.Guid;
import org.ebaloo.itkeeps.api.model.jBaseLight;
import org.ebaloo.itkeeps.api.model.jEntry;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseVertrex;
import org.ebaloo.itkeeps.core.domain.edge.DirectionType;
import org.ebaloo.itkeeps.core.domain.edge.traverse.eInPath;
import org.ebaloo.itkeeps.core.domain.vertex.SecurityFactory.SecurityAcl;
import org.ebaloo.itkeeps.core.domain.vertex.SecurityFactory.oRID;
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
		this(j, true);
	}

	
	protected vEntry(final jEntry j, final boolean f) {
		super(j, false);
		
		/*
		this.commit();
		this.reload();
		*/

		if(j.isPresentPath())
			this.setPath(j.getPath());

		if(f)
			this.setEnable(Boolean.TRUE);
	}

	
	public jEntry read(Guid requesteurGuid) {
		
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(new oRID(requesteurGuid), new oRID(this));
		
		// TODO Security
				
		
		jEntry j = new jEntry();

		this.readBaseStandard(j);
		
		j.setPath(getJBaseLight(this.getPath()));
		
		return j;
	}
	
	public jEntry update(jEntry j, Guid requesteurGuid) {
		return this.update(j, requesteurGuid, false);
	}

	private jEntry update(jEntry j, Guid requesteurGuid, boolean force) {

		
		SecurityAcl sAcl = SecurityFactory.getSecurityAcl(new oRID(requesteurGuid), new oRID(this));

		//TODO Security

		this.checkVersion(j);
		
		this.updateBaseStandard(j);

		
		if(j.isPresentPath())
			this.setPath(j.getPath());

		return read(requesteurGuid);
	}


	
}



