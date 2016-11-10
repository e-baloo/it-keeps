package org.ebaloo.itkeeps.restapp.api.pojo;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JImage extends JBase {
	
	// IAMGE TYPE
	
	@JsonIgnore
	private Optional<String> imageType = Optional.empty();
	
	@JsonProperty("imageType")
	public final String getImageType() {
		return imageType.orElse(null);
	}

	@JsonProperty("imageType")
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