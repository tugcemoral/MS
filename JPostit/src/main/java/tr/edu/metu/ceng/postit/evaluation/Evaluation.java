package tr.edu.metu.ceng.postit.evaluation;

import java.text.DecimalFormat;
import java.text.MessageFormat;

public class Evaluation {

	private final int trueTagged;
	private final int falseTagged;
	private final int totalNumberOfTagged;
	private final int numberOfSentences;

	public Evaluation(int trueTagged, int falseTagged, int totalNumberOfTagged,
			int numberOfSentences) {
		this.trueTagged = trueTagged;
		this.falseTagged = falseTagged;
		this.totalNumberOfTagged = totalNumberOfTagged;
		this.numberOfSentences = numberOfSentences;
	}

	public int getTrueTagged() {
		return trueTagged;
	}

	public int getFalseTagged() {
		return falseTagged;
	}

	public int getTotalNumberOfTagged() {
		return totalNumberOfTagged;
	}

	public int getNumberOfSentences() {
		return numberOfSentences;
	}

	@Override
	public String toString() {

		DecimalFormat df = new DecimalFormat("#.###");

		double tt = (double) trueTagged / (double) totalNumberOfTagged * 100;
//		double ft = (double) falseTagged / (double) totalNumberOfTagged * 100;

		return MessageFormat.format(
				"True tagged: {0}% ({1} / {2})\tout of {3} sentences",
				df.format(tt), trueTagged, totalNumberOfTagged,
				numberOfSentences);
	}

}
