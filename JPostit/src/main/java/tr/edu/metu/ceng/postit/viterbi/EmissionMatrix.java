package tr.edu.metu.ceng.postit.viterbi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tr.edu.metu.ceng.postit.data.Corpus;
import tr.edu.metu.ceng.postit.data.Sentence;
import tr.edu.metu.ceng.postit.data.Token;

/**
 * Likelihood matrix
 */
public class EmissionMatrix extends Matrix {

	public EmissionMatrix(Corpus corpus) {
		super(corpus);
		// initialize transition matrix
		initialize();
	}

	private void initialize() {
		List<Sentence> sentences = getCorpus().getSentences();
		for (Sentence sentence : sentences) {
			for (Token token : sentence.getTokens()) {
				String word = token.getWord();
				String tag = token.getTag();
				if (bigramMatrix.get(tag) == null) {
					Map<String, Double> priorMap = new HashMap<String, Double>();
					bigramMatrix.put(tag, priorMap);
				}
				if (bigramMatrix.get(tag).get(word) == null) {
					Double value = 0.0;
					bigramMatrix.get(tag).put(word, value);
				}
				Double value = bigramMatrix.get(tag).get(word);
				bigramMatrix.get(tag).put(word, ++value);
			}
		}
	}

	@Override
	public double getItem(String state, String word) {
		// check that sentence is out of vocabulary.
		boolean oov = !(getCorpus().containsWord(word));
		if (oov) {
			// XXX: here, we can define some rules that if given word not found
			// in vocabulary, we need to assign a sensible probability.
			return 0.00001d;

		} else {
			return super.getItem(state, word);
		}
	}

}
