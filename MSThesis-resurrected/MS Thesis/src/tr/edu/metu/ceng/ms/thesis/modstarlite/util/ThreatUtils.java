package tr.edu.metu.ceng.ms.thesis.modstarlite.util;

public class ThreatUtils {

	public static int generateThreatSize(int width, int height) {
		// multiply (the minimum one's division by 5) with random and return + 1
		return (int) (Math.random() * (((width <= height) ? width : height) / 3d )) + 1;
	}

	public static int generateNumOfThreats(int width, int height) {
		// multiply (the minimum one's division by 10) with random and return + 1
		return (int) (Math.random() * ((width <= height) ? width : height) / 6d) + 1;
	}

}
