
package org.ebaloo.itkeeps.core.domain.vertex;


import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.ebaloo.itkeeps.Guid;
import org.ebaloo.itkeeps.api.model.JBase;
import org.ebaloo.itkeeps.api.model.JImage;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseProperty;
import org.ebaloo.itkeeps.core.database.annotation.DatabaseVertrex;
import org.ebaloo.itkeeps.core.domain.ModelFactory;
import org.ebaloo.itkeeps.core.domain.annotation.ModelClassAnnotation;
import org.ebaloo.itkeeps.core.tools.MetricsFactory;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;


import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;

@ModelClassAnnotation()
@DatabaseVertrex()
public class Image extends BaseSysteme {

	
	public Image() {}

	public Image(final BaseAbstract abase) {
		super(abase);
	}

	public Image(final String name) {
		super(name);
	}
	
	public Image(final String name, final String imageType, final String base64) {
		super(name);
		
		this.setImageType(imageType);
		this.setBase64(base64);
	}
	
	
//	public static String ICON = "ico:";
	public static String DEFAULT_ICON = ICON_NAME_PREFIX + "default";
	
	

	/*
	 * BASE64
	 */
	

	@DatabaseProperty(name = JImage.BASE64)
	public String getBase64() 
	{
		return this.getProperty(JImage.BASE64);
	}

	public void setBase64(String value) {
		
		 this.setProperty(JImage.BASE64,value);
		 
		getCache().remove(this.getName());
		getCache().remove(this.getGuid().toString());
		
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
	

	@DatabaseProperty(name=JImage.IMAGE_TYPE)
	public String getImageType() 
	{
		if(StringUtils.isBlank(typeImg)) {
			typeImg = this.getProperty(JImage.IMAGE_TYPE);
		}
		
		return typeImg;
	}


	public void setImageType(String value) {
		 
		this.setProperty(JImage.IMAGE_TYPE, value);
		 
		getCache().remove(this.getName());
		getCache().remove(this.getGuid().toString());

	}
	
	
	
	
	
	public synchronized static Image getImage(String id) {

		Image image = null;
		
		if(!Guid.isGuid(id) && getCache().isKeyInCache(id)) {
			id = (String) getCache().get(id).getObjectValue();
		}
			
		if(Guid.isGuid(id) && getCache().isKeyInCache(id)) {
			return (Image) getCache().get(id).getObjectValue();
		}
		
		
		if(Guid.isGuid(id)) {
			image = (Image) ModelFactory.getBaseAbstract(new Guid(id));
		} else {
			image = getByName(ModelFactory.get(Image.class), id);
		}

		if(image != null) {
			
			getCache().remove(image.getName());
			getCache().remove(image.getGuid().toString());
			
			getCache().put(new Element(image.getName(), image.getGuid().toString()));
			getCache().put(new Element(image.getGuid().toString(), image));
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
			  new CacheConfiguration(Image.class.getSimpleName(), 500)
			    .memoryStoreEvictionPolicy(MemoryStoreEvictionPolicy.LFU)
			    .eternal(false)
			    .timeToLiveSeconds(60 * 60)
			    .timeToIdleSeconds(60 * 60)
			    );
			
			
			MetricsFactory.getMetricRegistry().register(MetricRegistry.name(Image.class.getSimpleName(), "images", "cache", "count"),
                    new Gauge<Integer>() {
                        @Override
                        public Integer getValue() {
                            return cache.getSize();
                        }
                    });
			
			manager.addCache(cache);
			
		}
		
		cache.evictExpiredElements();

		return cache;
	}

	
	// API
	
	@Override
	public <T extends JBase> void apiFill(T obj, Guid requesteurGuid) {
		apiFill(obj, requesteurGuid, true);
	}

	
	public <T extends JBase> void apiFill(T obj, Guid requesteurGuid, final boolean full) {

		if(!(obj instanceof JImage))
			throw new RuntimeException("TODO"); //TODO

		super.apiFill(obj, requesteurGuid);
		
		JImage jimage = (JImage) obj;
		
		jimage.setImageType(this.getImageType());
		
		if(full)
			jimage.setBase64(this.getBase64());
	}

	
	@Override
	public <T extends JBase> void apiUpdate(T obj, Guid requesteurGuid) {
		
		if(!(obj instanceof JImage))
			throw new RuntimeException("TODO"); //TODO

		super.apiUpdate(obj, requesteurGuid);
		
		JImage jimage = (JImage) obj;
		
		
		if(jimage.isPresentImageType())
			this.setImageType(this.getImageType());

		if(jimage.isPresentBase64())
			this.setBase64(this.getBase64());
		
		
	}
	
	
}
