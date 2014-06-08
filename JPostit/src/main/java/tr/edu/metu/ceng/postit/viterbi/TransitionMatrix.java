package tr.edu.metu.ceng.postit.viterbi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tr.edu.metu.ceng.postit.data.Corpus;
import tr.edu.metu.ceng.postit.data.Sentence;
import tr.edu.metu.ceng.postit.data.Token;

/**
 * Transition Probability Matrix for HMM
 */
public class TransitionMatrix extends Matrix {

	private List<String[]> bigram = new ArrayList<String[]>();

	private Map<String, Double> unigram = new HashMap<String, Double>();

	public TransitionMatrix(Corpus corpus) {
		super(corpus);
		// initialize transition matrix
		initialize();
		// compute probability
		normalize();
	}

	private void initialize() {
		// initialize bigram model and unigram model
		initializeModel();
		// initialize matrix
		initializeMatrix();
	}

	private void initializeMatrix() {
		initializeBigramMatrix();
		initializeUnigramMatrix();
	}

	private void initializeUnigramMatrix() {
		super.unigramMatrix = this.unigram;
	}

	private void initializeBigramMatrix() {
		for (String[] biStr : bigram) {
			// Tag
			String n_1 = biStr[0];
			// Following Tag
			String n = biStr[1];
			if (bigramMatrix.get(n_1) == null) {
				Map<String, Double> priorMap = new HashMap<String, Double>();
				bigramMatrix.put(n_1, priorMap);
			}
			if (bigramMatrix.get(n_1).get(n) == null) {
				Double value = 0.0;
				bigramMatrix.get(n_1).put(n, value);
			}
			Double value = bigramMatrix.get(n_1).get(n);
			bigramMatrix.get(n_1).put(n, ++value);
		}
	}

	/**
	 * Initialize bigram model for tags
	 */
	private void initializeModel() {
		List<Sentence> sentences = getCorpus().getSentences();
		for (Sentence sentence : sentences) {
			List<Token> tokens = sentence.getTokens();
			for (int i = -1; i < tokens.size(); i++) {
				String tag_n_1 = "";
				String tag_n = "";
				// add in a start symbol
				if (i == -1) {
					tag_n_1 = HMM.START_SYMBOL;
				} else {
					Token token_n_1 = tokens.get(i);
					tag_n_1 = token_n_1.getTag();
				}
				// append an end symbol
				if (i == tokens.size() - 1) {
					tag_n = HMM.END_SYMBOL;
				} else {
					Token token_n = tokens.get(i + 1);
					tag_n = token_n.getTag();
				}
				String[] biStr = new String[] { tag_n_1, tag_n };
				bigram.add(biStr);
				// initialize unigram
				tag_n_1 = tag_n_1.toUpperCase();
				Double count = 0.0;
				if (unigram.containsKey(tag_n_1)) {
					count = unigram.get(tag_n_1);
				}
				unigram.put(tag_n_1, ++count);
			}
		}
	}
}
