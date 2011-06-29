package tr.edu.metu.ceng.wbsa.validator2.domain;

public class ResourceValidationInfo {

	private String url;

	private boolean isValid;

	private ResourceType resourceType;

	private String message;

	public ResourceValidationInfo(String url, boolean isValid,
			ResourceType resourceType, String message) {
		this.url = url;
		this.isValid = isValid;
		this.resourceType = resourceType;
		this.message = message;
	}

	public ResourceValidationInfo() {
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	public ResourceType getResourceType() {
		return resourceType;
	}

	public void setResourceType(ResourceType resourceType) {
		this.resourceType = resourceType;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
