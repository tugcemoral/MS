package tr.edu.metu.ceng.postit.viterbi;

import java.util.HashMap;
import java.util.Map;

import tr.edu.metu.ceng.postit.data.Corpus;

/**
 * Base matrix for transmission and emission matrix
 */
public abstract class Matrix {

	// transition probability matrix <K,V> = <Tag,<Following Tag,Probability>>
	protected Map<String, Map<String, Double>> bigramMatrix = new HashMap<String, Map<String, Double>>();

	private final Corpus corpus;

	protected Double oovForBigram = 0.0;

	public Matrix(Corpus corpus) {
		this.corpus = corpus;
	}

	protected Corpus getCorpus() {
		return this.corpus;
	}

	/**
	 * bigram probablity, with add-one smoothing
	 */
	public double getItem(String row, String col) {
		Map<String, Double> rowMap = this.getRow(row);
		if (rowMap == null) {
			return oovForBigram;
		}
		Double value = rowMap.get(col);
		return (value == null) ? oovForBigram : value;
	}

	public Map<String, Double> getRow(String row) {
		return bigramMatrix.get(row);
	}

	public String[] getKeys() {
		return bigramMatrix.keySet().toArray(new String[0]);
	}
}
