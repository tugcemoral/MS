package tr.edu.metu.ceng.postit.tagger;

public abstract class AbstractPosTagger implements IPosTagger {

	@Override
	public WordTagPair convert(String analysis) {
		// parse word from analysis sentence.
		String word = analysis.split(" ")[0];
		// extract tag wrt given model.
		String tag = extractTag(analysis.substring(analysis.indexOf("+")));

		return new WordTagPair(word, tag);
	}

	@Override
	public void tag(String sentence) {

	}

	protected abstract String extractTag(String tagSentence);

}
