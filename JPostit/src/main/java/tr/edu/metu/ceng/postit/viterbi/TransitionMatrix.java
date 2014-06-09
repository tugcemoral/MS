package tr.edu.metu.ceng.postit.viterbi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import tr.edu.metu.ceng.postit.data.Corpus;
import tr.edu.metu.ceng.postit.data.Sentence;
import tr.edu.metu.ceng.postit.data.Token;

/**
 * Transition Probability a.k.a Prior Matrix
 */
public class TransitionMatrix extends Matrix {

	private List<String[]> bigrams = new ArrayList<String[]>();

	public TransitionMatrix(Corpus corpus) {
		super(corpus);
		// initialize bigrams model and transition matrix.
		initializeModel();
		// initialize matrix
		initializeBigramMatrix();
		// compute probability
		normalizeBigramTransition();
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
			}
		}
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
	
	/**
	 * Normalize matrix cells, counting probability
	 */
	private void normalizeBigramTransition() {
		Iterator<Map.Entry<String, Map<String, Double>>> it = bigramMatrix
				.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Map<String, Double>> entry = it.next();
			Map<String, Double> colList = entry.getValue();
			double count = 0;
			for (Entry<String, Double> ent : colList.entrySet()) {
				count += ent.getValue();
			}
			oovForBigram = 1.0 / (count + getCorpus().vocabularySize());
			// normalize
			for (Entry<String, Double> ent : colList.entrySet()) {
				// add 1 smoothing
				Double prob = (ent.getValue() + 1)
						/ (count + getCorpus().vocabularySize());
				// Double prob = (ent.getValue() / count);
				ent.setValue(prob);
			}
		}
	}

}
