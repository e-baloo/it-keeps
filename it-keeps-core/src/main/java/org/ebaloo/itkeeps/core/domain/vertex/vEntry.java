
package org.ebaloo.itkeeps.core.domain.vertex;



import org.apache.commons.lang3.StringUtils;
import org.ebaloo.itkeeps.api.model.jBaseLight;
import org.ebaloo.itkeeps.api.model.jEncryptedEntry;
import org.ebaloo.itkeeps.api.model.jEntry;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseVertrex;
import org.ebaloo.itkeeps.core.domain.edge.DirectionType;
import org.ebaloo.itkeeps.core.domain.edge.traverse.eEncrypte;
import org.ebaloo.itkeeps.core.domain.edge.traverse.eInPath;
import org.ebaloo.itkeeps.core.domain.vertex.SecurityFactory.SecurityAcl;


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

	
	/*
	 * PATH
	 */
	
	private vEncryptedEntry getEncrypted() {
		
		vEncryptedEntry enc = this.getEdge(vEncryptedEntry.class, DirectionType.CHILD, false, eEncrypte.class);

		if(enc == null) {
			
			jEncryptedEntry j = new jEncryptedEntry();
			j.setName(String.format("[[%s]]", this.getName()));
			
			enc = new vEncryptedEntry(j);

			setEdges(
					this.getGraph(),
					vEntry.class, 
					this, 
					vEncryptedEntry.class, 
					enc, 
					DirectionType.CHILD, 
					eEncrypte.class, 
					false);
			
		}
		
		return enc;
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
	
	
	final jEncryptedEntry readEncrypted(final SecurityAcl sAcl) {
		return this.getEncrypted().read(sAcl);
	}
	
	
	final void update(jEntry j) {
		this.updateBaseStandard(j);
		if(j.isPresentPath())
			this.setPath(j.getPath());
	}

	final void updateEncrypted(final SecurityAcl sAcl, jEncryptedEntry j) {
		this.getEncrypted().update(sAcl, j);
	}
	

	@Override
	protected void delete() {
		this.getEncrypted().delete();
		super.delete();
	}

	
	/*
	 * Algorithm : https://www.bouncycastle.org/specifications.html
	 * 
	 * 
	 * 
	 */
	@DatabaseVertrex()
	public final static class vEncryptedEntry extends vBaseSysteme {



		protected vEncryptedEntry() {
			super();
		}

		
		private vEncryptedEntry(final jEncryptedEntry j) {
			super(j);

			try {

				this.setMediaType(j.getMediaType());
				//this.setData(sAcl, j.getData());

			} catch (Exception e) {
				this.delete();
				throw e;
			}

		}
	

		private final String getData64(SecurityAcl sAcl) {

			if (sAcl.isRoleRoot())
				return StringUtils.EMPTY;

			if (!sAcl.isDataEncryptedEntryRead())
				return StringUtils.EMPTY;

			String encData = this.getProperty(jEncryptedEntry.DATA64);

			if (StringUtils.isEmpty(encData))
				return StringUtils.EMPTY;

			
			String data = SecurityFactory.getEntryEncryptor().decrypt(encData);

			return data;
		}

		private String getMediaType() {

			return this.getProperty(jEncryptedEntry.MEDIA_TYPE);

		}

		// API

		private jEncryptedEntry read(SecurityAcl sAcl) {

			jEncryptedEntry j = new jEncryptedEntry();

			this.readBase(j);

			j.setData64(this.getData64(sAcl));
			j.setMediaType(this.getMediaType());

			return j;
		}

		private void setData64(SecurityAcl sAcl, String value) {

			if (sAcl.isRoleRoot())
				return;

			if (!sAcl.isDataEncryptedEntryUpdate())
				return;

			if (StringUtils.isEmpty(value)) {
				this.setProperty(jEncryptedEntry.DATA64, StringUtils.EMPTY);
				return;
			}
			
			String encData = SecurityFactory.getEntryEncryptor().encrypt(value);

			this.setProperty(jEncryptedEntry.DATA64, encData);

		}

		
		private void setMediaType(String value) {
			this.setProperty(jEncryptedEntry.MEDIA_TYPE, StringUtils.isEmpty(value) ? StringUtils.EMPTY : value);
		}

		
		private void update(SecurityAcl sAcl, jEncryptedEntry j) {
			this.updateBase(j);
			this.setData64(sAcl, j.getData64());
			this.setMediaType(j.getMediaType());
		}

	}
}



