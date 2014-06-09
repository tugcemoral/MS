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

	private List<String[]> bigrams = new ArrayList<String[]>();

	private Map<String, Double> unigram = new HashMap<String, Double>();

	public TransitionMatrix(Corpus corpus) {
		super(corpus);
		// initialize transition matrix
		initialize();
		// compute probability
		normalize();
	}

	private void initialize() {
		// initialize bigrams model and unigram model
		initializeModel();
		// initialize matrix
		initializeMatrix();
	}

	/**
	 * Initialize bigrams model for tags
	 */
	private void initializeModel() {
		List<Sentence> sentences = getCorpus().getSentences();
		for (Sentence sentence : sentences) {
			List<Token> tokens = sentence.getTokens();
			for (int i = -1; i < tokens.size(); i++) {
				//find (n-1)th and nth tag values.
				String tag_n_1 = (i == -1) ? HMM.START_SYMBOL : tokens.get(i).getTag();
				String tag_n = (i == tokens.size() - 1) ? HMM.END_SYMBOL : tokens.get(i + 1).getTag();  
				//add tag sequence to bigrams.
				bigrams.add(new String[] { tag_n_1, tag_n });
				
				//initiate unigram count.
				Double count = 0.0;
				if (unigram.containsKey(tag_n_1)) {
					count = unigram.get(tag_n_1);
				}
				unigram.put(tag_n_1, ++count);
			}
		}
	}
	
	private void initializeMatrix() {
		initializeBigramMatrix();
		initializeUnigramMatrix();
	}
	
	private void initializeBigramMatrix() {
		for (String[] biStr : bigrams) {
			// first tag.
			String n_1 = biStr[0];
			// following tag
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
	
	private void initializeUnigramMatrix() {
		super.unigramMatrix = this.unigram;
	}

}
