package org.ebaloo.itkeeps.domain.pojo;

import java.util.Optional;

import org.ebaloo.itkeeps.domain.vertex.Image;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JImage extends JBase {
	
	
	public JImage() {
		super();
	};
	

	public JImage(final Image image) {
		this(image, true);
	}

	public JImage(final Image image, final boolean full) {
		super(image);
		
		this.setImageType(image.getImageType());
		
		if(full)
			this.setBase64(image.getBase64());
	}
	

	public void update(Image image) {
		super.update(image);
		
		if(this.isPresentImageType())
			image.setImageType(this.getImageType());

		if(this.isPresentBase64())
			image.setBase64(this.getBase64());
		
		
	}
	
	
	
	// IAMGE TYPE
	
	@JsonIgnore
	private Optional<String> imageType = Optional.empty();
	
	@JsonProperty(Image.IMAGE_TYPE)
	public final String getImageType() {
		return imageType.orElse(null);
	}

	@JsonProperty(Image.IMAGE_TYPE)
	public final void setImageType(String imageType) {
		this.imageType = Optional.of(imageType == null ? "" : imageType);
	}

	@JsonIgnore
	public final boolean isPresentImageType() {
		return this.imageType.isPresent();
	}
	
	
	// BASE64
	
	@JsonIgnore
	private Optional<String> base64 = Optional.empty();
	
	@JsonProperty("base64")
	public final String getBase64() {
		return base64.orElse(null);
	}

	@JsonProperty("base64")
	public final void setBase64(String base64) {
		this.base64 = Optional.of(base64 == null ? "" : base64);
	}

	@JsonIgnore
	public final boolean isPresentBase64() {
		return this.base64.isPresent();
	}
	
}