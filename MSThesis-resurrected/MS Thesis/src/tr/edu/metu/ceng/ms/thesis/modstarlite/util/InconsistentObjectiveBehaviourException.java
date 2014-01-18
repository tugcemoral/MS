package tr.edu.metu.ceng.ms.thesis.modstarlite.util;

public class InconsistentObjectiveBehaviourException extends RuntimeException {

	private static final long serialVersionUID = -7719158500690308756L;

	public InconsistentObjectiveBehaviourException() {
		super("Objective Behaviours are inconsistent!");
	}

}
