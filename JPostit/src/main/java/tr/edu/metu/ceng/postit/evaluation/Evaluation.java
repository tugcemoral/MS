package tr.edu.metu.ceng.postit.evaluation;

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
		double tt = (double)trueTagged/(double)totalNumberOfTagged*100;
		double ft = (double)falseTagged/(double)totalNumberOfTagged*100;
		return "[ " + tt + " % ]"; 
//		return "[ True(" + tt + " %): " + trueTagged + " False(" + ft + " %) : " + falseTagged
//				+ " Total Tagged: " + totalNumberOfTagged
//				+ " Number of Sentences : " + numberOfSentences + " ]";
	}

}
