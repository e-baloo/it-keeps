
package org.ebaloo.itkeeps.core.domain.vertex;


import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.ebaloo.itkeeps.Rid;
import org.ebaloo.itkeeps.api.model.jImage;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseProperty;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseVertrex;
import org.ebaloo.itkeeps.core.tools.MetricsFactory;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;


import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;

@DatabaseVertrex()
public final class vImage extends vBaseSysteme {

	
	protected vImage() {}

	public vImage(final String name, final String imageType, final String base64) {
		super(name);
		
		this.setImageType(imageType);
		this.setBase64(base64);
	}

	protected vImage(final jImage j) {
		super(j);

		try {
		
		if(StringUtils.isEmpty(j.getImageType()) || StringUtils.isEmpty(j.getBase64()))
				throw new RuntimeException("TODO"); //
				
		this.setImageType(j.getImageType());
		this.setBase64(j.getBase64());
	} catch (Exception e) {
		this.delete();
		throw e;
	}
		
	}
	

	
	public static String DEFAULT_ICON = ICON_NAME_PREFIX + "default";
	

	/*
	 * BASE64
	 */
	

	@DatabaseProperty(name = jImage.BASE64)
	public String getBase64() 
	{
		return this.getProperty(jImage.BASE64);
	}

	public void setBase64(String value) {
		
		 this.setProperty(jImage.BASE64,value);
		 
		getCache().remove(this.getName());
		getCache().remove(this.getRid().toString());
		
	}
		
	
	private byte[] decoded = null;
	
	public byte[] getImageBytes() {
		
		if(decoded != null) {
			return decoded;
		}
		
		String value = this.getBase64();
		if(StringUtils.isBlank(value)) {
			return new byte[]{};
		}

		decoded = Base64.decodeBase64(this.getBase64());
		
		return decoded;
	}

	/*
	 * IMAGE_TYPE
	 */
	
	private String typeImg = null; 
	

	@DatabaseProperty(name=jImage.IMAGE_TYPE)
	public String getImageType() 
	{
		if(StringUtils.isBlank(typeImg)) {
			typeImg = this.getProperty(jImage.IMAGE_TYPE);
		}
		
		return typeImg;
	}


	public void setImageType(String value) {
		 
		this.setProperty(jImage.IMAGE_TYPE, value);
		 
		getCache().remove(this.getName());
		getCache().remove(this.getRid().toString());

	}
	
	
	
	
	
	public synchronized static vImage getImage(String id) {

		vImage image;
		
		if(!Rid.is(id) && getCache().isKeyInCache(id)) {
			id = (String) getCache().get(id).getObjectValue();
		}
			
		if(Rid.is(id) && getCache().isKeyInCache(id)) {
			return (vImage) getCache().get(id).getObjectValue();
		}
		
		
		image = vBaseAbstract.get(null, vImage.class, id, false);

		if(image != null) {
			
			getCache().remove(image.getName());
			getCache().remove(image.getRid().toString());
			
			getCache().put(new Element(image.getName(), image.getRid().toString()));
			getCache().put(new Element(image.getRid().toString(), image));
		}

		return image;
		
	}
	

	

	
	private static Cache cache = null;
	
	private static Cache getCache() {
		
		if(cache == null) {
			
			System.setProperty("net.sf.ehcache.skipUpdateCheck", "true");
			
			CacheManager manager = CacheManager.create();

			//Create a Cache specifying its configuration.
			cache = new Cache(
			  new CacheConfiguration(vImage.class.getSimpleName(), 500)
			    .memoryStoreEvictionPolicy(MemoryStoreEvictionPolicy.LFU)
			    .eternal(false)
			    .timeToLiveSeconds(60 * 60)
			    .timeToIdleSeconds(60 * 60)
			    );
			
			
			MetricsFactory.getMetricRegistry().register(MetricRegistry.name(vImage.class.getSimpleName(), "images", "cache", "count"),
					(Gauge<Integer>) () -> cache.getSize());
			
			manager.addCache(cache);
			
		}
		
		cache.evictExpiredElements();

		return cache;
	}

	
	// API
	
	public jImage read(Rid requesteurRid) {
		return read(requesteurRid, true);
	}

	
	public jImage read(Rid requesteurRid, final boolean full) {

		jImage	j = new jImage();

		this.readBase(j);
		
		j.setImageType(this.getImageType());
		
		if(full)
			j.setBase64(this.getBase64());
		
		return j;
	}

	
	public jImage update(jImage j, Rid requesteurRid) {

		this.updateBase(j);
		
		if(j.isPresentImageType())
			this.setImageType(j.getImageType());

		if(j.isPresentBase64()) {
			this.setBase64(j.getBase64());
		}


		return read(requesteurRid);
	}
	
	
}
