package tr.edu.metu.ceng.ms.thesis.modstarlite.util;

public class InconsistentObjectiveSizeException extends RuntimeException {

	private static final long serialVersionUID = 3312452442369966346L;

	public InconsistentObjectiveSizeException() {
		super("Objective Array sizes are different!");
	}

}
