
package org.ebaloo.itkeeps.core.domain.vertex;



import org.ebaloo.itkeeps.Guid;
import org.ebaloo.itkeeps.api.model.jBase;
import org.ebaloo.itkeeps.api.model.jBaseLight;
import org.ebaloo.itkeeps.api.model.jEntry;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseVertrex;
import org.ebaloo.itkeeps.core.domain.edge.DirectionType;
import org.ebaloo.itkeeps.core.domain.edge.traverse.eInPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@DatabaseVertrex()
public class vEntry extends vBaseChildAcl {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(vEntry.class);

	
	protected vEntry() {
		super();
	}
	
	

	/*
	 * PATH
	 */
	
	public vPath getPath() {
		return this.getEdgeByClassesNames(vPath.class, DirectionType.PARENT, false, eInPath.class);
	}
	
	protected void setPath(vPath path) {
		setEdges(this.getGraph(), vEntry.class, this, path, DirectionType.PARENT, eInPath.class, false);
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
		
		this.commit();
		this.reload();


		if(j.isPresentPath())
			this.setPath(j.getPath());

		if(f)
			this.setEnable(Boolean.TRUE);
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends jBase> T read(T j, Guid requesteurGuid) {
		
		if(j == null)
			j = (T) new jEntry();
		
		if(!(j instanceof jEntry))
			throw new RuntimeException("TODO"); //TODO
		
		super.read(j, requesteurGuid);

		jEntry jentry = (jEntry) j;

		jentry.setPath(getJBaseLight(this.getPath()));
		
		return j;
	}
	
	@Override
	public <T extends jBase> T update(T obj, Guid requesteurGuid) {
		
		if(!(obj instanceof jEntry))
			throw new RuntimeException("TODO"); //TODO

		vUser requesterUser = vUser.get(this.getGraph(), vUser.class, requesteurGuid, false);

		switch(requesterUser.getRole()) {

		case ROOT:
			// -> Ok is root
			break;

		case ADMIN:
			//TODO Check it requesterUser have the right to update this
			break;
			
		case USER:
			// Check Role TODO
			break;

		case GUEST:
		default:
			throw new RuntimeException("TODO"); //TODO
		}
		
		super.update(obj, requesteurGuid);

		jEntry jentry = (jEntry) obj;
		
		if(jentry.isPresentPath())
			this.setPath(jentry.getPath());

		return read(null, requesteurGuid);
	}


	
}



