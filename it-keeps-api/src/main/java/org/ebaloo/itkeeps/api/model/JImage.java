package org.ebaloo.itkeeps.api.model;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JImage extends JBase {
	
	public static final String BASE64 = "base64";
	public static final String IMAGE_TYPE = "imageType";
	
	public JImage() {
		super();
	};
	

	// IAMGE TYPE
	
	@JsonIgnore
	private Optional<String> imageType = Optional.empty();
	
	@JsonProperty(IMAGE_TYPE)
	public final String getImageType() {
		return imageType.orElse(null);
	}

	@JsonProperty(IMAGE_TYPE)
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
	
	@JsonProperty(BASE64)
	public final String getBase64() {
		return base64.orElse(null);
	}

	@JsonProperty(BASE64)
	public final void setBase64(String base64) {
		this.base64 = Optional.of(base64 == null ? "" : base64);
	}

	@JsonIgnore
	public final boolean isPresentBase64() {
		return this.base64.isPresent();
	}
	
}