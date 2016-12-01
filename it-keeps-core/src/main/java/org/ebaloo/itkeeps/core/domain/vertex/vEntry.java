
package org.ebaloo.itkeeps.core.domain.vertex;



import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.ebaloo.itkeeps.api.model.jBaseLight;
import org.ebaloo.itkeeps.api.model.jEncryptedEntry;
import org.ebaloo.itkeeps.api.model.jEntry;
import org.ebaloo.itkeeps.commons.ConfigFactory;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseVertrex;
import org.ebaloo.itkeeps.core.domain.edge.DirectionType;
import org.ebaloo.itkeeps.core.domain.edge.traverse.eEncrypte;
import org.ebaloo.itkeeps.core.domain.edge.traverse.eInPath;
import org.ebaloo.itkeeps.core.domain.vertex.SecurityFactory.SecurityAcl;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;


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
			
			enc = new vEncryptedEntry();
			
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

	@DatabaseVertrex()
	public final static class vEncryptedEntry extends vBaseSysteme {

		private static StandardPBEStringEncryptor encryptor = null;

		private static final StringEncryptor getEncryptor() {

			if (encryptor == null) {

				try {

					StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();

					String algorithm = ConfigFactory.getString("encryptor.algorithm", "PBEWithMD5AndTripleDES");
					String password64 = ConfigFactory.getString("encryptor.password64",
							"Q15KWVdrJlBhNSZSWnJuYkRhNnpYUCE3LXZ6UTgtYnY=");
					int iterations = ConfigFactory.getInt("encryptor.iterations", 500);

					byte[] decoded = Base64.decodeBase64(password64);
					String password = new String(decoded, "UTF-8");

					encryptor.setAlgorithm(algorithm);
					encryptor.setPassword(password);
					encryptor.setKeyObtentionIterations(iterations);

				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}

			}
			return encryptor;
		}

		private vEncryptedEntry() {
		}

		/*
		private vEncryptedEntry(final SecurityAcl sAcl, final jEncryptedEntry j) {
			super(j);

			try {

				this.setMediaType(j.getMediaType());
				this.setData(sAcl, j.getData());

			} catch (Exception e) {
				this.delete();
				throw e;
			}

		}
		*/

		private final String getData(SecurityAcl sAcl) {

			if (sAcl.isRoleRoot())
				return StringUtils.EMPTY;

			if (!sAcl.isDataEncryptedEntryRead())
				return StringUtils.EMPTY;

			String encData = this.getProperty(jEncryptedEntry.DATA);

			if (StringUtils.isEmpty(encData))
				return StringUtils.EMPTY;

			String data = getEncryptor().decrypt(encData);

			return data;
		}

		private String getMediaType() {

			return this.getProperty(jEncryptedEntry.MEDIA_TYPE);

		}

		// API

		private jEncryptedEntry read(SecurityAcl sAcl) {

			jEncryptedEntry j = new jEncryptedEntry();

			this.readBase(j);

			j.setData(this.getData(sAcl));
			j.setMediaType(this.getMediaType());

			return j;
		}

		private void setData(SecurityAcl sAcl, String value) {

			if (sAcl.isRoleRoot())
				return;

			if (!sAcl.isDataEncryptedEntryUpdate())
				return;

			if (StringUtils.isEmpty(value)) {
				this.setProperty(jEncryptedEntry.DATA, StringUtils.EMPTY);
				return;
			}

			String encData = getEncryptor().encrypt(value);

			this.setProperty(jEncryptedEntry.DATA, encData);

		}

		
		private void setMediaType(String value) {
			this.setProperty(jEncryptedEntry.MEDIA_TYPE, StringUtils.isEmpty(value) ? StringUtils.EMPTY : value);
		}

		
		private void update(SecurityAcl sAcl, jEncryptedEntry j) {
			this.updateBase(j);
			this.setData(sAcl, j.getData());
			this.setMediaType(j.getMediaType());
		}

	}
}



