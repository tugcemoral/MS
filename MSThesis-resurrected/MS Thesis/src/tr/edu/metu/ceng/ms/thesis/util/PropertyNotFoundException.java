package tr.edu.metu.ceng.ms.thesis.util;

public class PropertyNotFoundException extends Exception {

	private static final long serialVersionUID = -8079334388716677706L;

	public PropertyNotFoundException() {
		super();
	}

	public PropertyNotFoundException(String message) {
		super("Property " + message + " not found in .properties file!");
	}

}
