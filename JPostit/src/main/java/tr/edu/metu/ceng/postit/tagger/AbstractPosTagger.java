package tr.edu.metu.ceng.postit.tagger;

import java.util.regex.Pattern;

public abstract class AbstractPosTagger implements IPosTagger {

	@Override
	public WordTagPair convert(String analysis) {
		// parse word from analysis sentence.
		String word = analysis.split("\\s+")[0];

		boolean isPunctuation = Pattern
				.compile("\\p{Punct}", Pattern.CASE_INSENSITIVE).matcher(word)
				.find();

		// extract tag wrt given model.
		String possibleTagFragment = analysis.substring(analysis.indexOf("+") + 1);
		String tag = isPunctuation ? possibleTagFragment : extractTag(analysis
				.substring(analysis.indexOf("+") + 1));

		return new WordTagPair(word, tag);
	}

	@Override
	public void tag(String sentence) {

	}

	protected abstract String extractTag(String tagSentence);

}
