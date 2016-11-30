
package org.ebaloo.itkeeps.core.domain.vertex;


import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.ebaloo.itkeeps.Rid;
import org.ebaloo.itkeeps.api.model.jEncryptedEntry;
import org.ebaloo.itkeeps.commons.ConfigFactory;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseVertrex;
import org.ebaloo.itkeeps.core.domain.vertex.SecurityFactory.SecurityAcl;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;


@DatabaseVertrex()
public final class vEncryptedEntry extends vBaseSysteme {

	
	protected vEncryptedEntry() {}

	vEncryptedEntry(final SecurityAcl sAcl, final jEncryptedEntry j) {
		super(j);

		try {
		
		this.setMediaType(j.getMediaType());
		this.setData(sAcl, j.getData());
		
	} catch (Exception e) {
		this.delete();
		throw e;
	}
		
	}
	

	/*
	 * DATA
	 */
	
	public String getData(SecurityAcl sAcl) {
		
		if(sAcl.isRoleAdmin())
			return StringUtils.EMPTY;
		
		if(!sAcl.isDataEncryptedEntryRead())
			return StringUtils.EMPTY;
		
		String encData = this.getProperty(jEncryptedEntry.DATA);
		
		if(StringUtils.isEmpty(encData))
			return StringUtils.EMPTY;
		
		String data = getEncryptor().decrypt(encData);
		
		return data;
	}

	public void setData(SecurityAcl sAcl, String value) {

		if(sAcl.isRoleAdmin())
			return;
		
		if(!sAcl.isDataEncryptedEntryUpdate())
			return;

		if(StringUtils.isEmpty(value)) {
			this.setProperty(jEncryptedEntry.DATA, value);
			return;
		}

		String encData = getEncryptor().encrypt(value);
		
		this.setProperty(jEncryptedEntry.DATA, encData);
		
	}
		
	
	/*
	 * MEDIA_TYPE
	 */
	
	public String getMediaType() {
		return this.getProperty(jEncryptedEntry.MEDIA_TYPE);
	}


	public void setMediaType(String value) {
		if(StringUtils.isEmpty(value))
			value = StringUtils.EMPTY;
		
		this.setProperty(jEncryptedEntry.MEDIA_TYPE, value);
	}
	

	

	
	
	// API
	
	public jEncryptedEntry read(Rid requesteurRid) {
		return read(requesteurRid, true);
	}

	
	public jEncryptedEntry read(Rid requesteurRid, final boolean full) {

		jEncryptedEntry	j = new jEncryptedEntry();

		this.readBase(j);
		
		jEncryptedEntry jimage = (jEncryptedEntry) j;
		
		//jimage.setMediaType(this.getImageType());
		
		//if(full)
		//	jimage.setBase64(this.getBase64());
		
		return j;
	}

	
	public jEncryptedEntry update(jEncryptedEntry j, Rid requesteurRid) {

		this.updateBase(j);
		
		jEncryptedEntry jimage = (jEncryptedEntry) j;
		
		
		
		
		/*
		if(jimage.isPresentImageType())
			this.setMediaType(this.getImageType());

		if(jimage.isPresentBase64())
			this.setBase64(this.getBase64());
		*/
		
		return read(requesteurRid);
	}
	
	
	//---------------------------------------------------------------------------------------------
	
	private static StandardPBEStringEncryptor encryptor = null;
	
	private static final StringEncryptor getEncryptor() {
		
		if(encryptor == null) {

			try {

			StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
			
			String algorithm = ConfigFactory.getString("encryptor.algorithm", "PBEWithMD5AndTripleDES");
			String password64 = ConfigFactory.getString("encryptor.password64", "Q15KWVdrJlBhNSZSWnJuYkRhNnpYUCE3LXZ6UTgtYnY=");
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
	
	
	
	
	
}
