package tr.edu.metu.ceng.postit.tagger;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.regex.Pattern;

import tr.edu.metu.ceng.postit.data.Sentence;
import tr.edu.metu.ceng.postit.data.WordTagPair;
import tr.edu.metu.ceng.postit.viterbi.HMM;

public abstract class AbstractPosTagger implements IPosTagger {

	private static final String MORPH_ANALYSIS_TRAINING_FILE_PATH = "/training.txt";

	@Override
	public WordTagPair convert(String analysis) {
		// parse word from analysis sentence.
		String word = analysis.split("\\s+")[0];

		boolean hasPunctuation = Pattern
				.compile("\\p{Punct}", Pattern.CASE_INSENSITIVE).matcher(word)
				.find();
		boolean isPunctuation = (hasPunctuation && (word.length() == 1));

		String tag;
		// extract tag wrt given model.
		// String possibleTagFragment = analysis.substring(analysis.indexOf("+")
		// + 1);
		String possibleTagFragment = findPossibleTagFragment(analysis);

		if (isPunctuation) {
			tag = possibleTagFragment;
		} else if (hasPunctuation) {
			// if word has punctuation, get rid of appendix.
			String[] probableStemWithAppendix = word.split("\\W");
			// oops, that would be alphabet punctuation longer than 1. (like
			// "...")
			if (probableStemWithAppendix.length == 0) {
				tag = possibleTagFragment;
			} else {
				// no problem, we can get the stem w/o appendix.
				word = probableStemWithAppendix[0];
				tag = extractTag(possibleTagFragment);
			}
		} else {
			tag = extractTag(possibleTagFragment);
		}
		// initialize and return alphabet word-tag pair.
		return new WordTagPair(word, tag);
	}
	
	@Override
	public String tag(String sentence) throws IOException {
		// create an hmm instance.
		HMM hmm = new HMM(this);
		// train wrt train document.
		System.out.print(MessageFormat.format(
				"Training Viterbi HMM with {0} wrt training file... ", this
						.getClass().getSimpleName()));
		long t1 = System.currentTimeMillis();
		hmm.train(getClass().getResource(MORPH_ANALYSIS_TRAINING_FILE_PATH)
				.getFile());
		long t2 = System.currentTimeMillis();
		System.out.println(MessageFormat.format("Done training in {0} ms.",
				(t2 - t1)));

		// do the actual tagging!
		Sentence taggedSentence = hmm.tag(sentence);
		return taggedSentence.toString();
	}

	private String findPossibleTagFragment(String analysis) {
		String possibleTagFragment = analysis
				.substring(analysis.indexOf("+") + 1);
		if (possibleTagFragment.startsWith("sH)")) {
			return possibleTagFragment.substring(possibleTagFragment
					.indexOf("+") + 1);
		} else {
			return possibleTagFragment;
		}
	}

	protected abstract String extractTag(String tagSentence);

}
