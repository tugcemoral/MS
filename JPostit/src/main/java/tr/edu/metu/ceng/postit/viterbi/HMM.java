package tr.edu.metu.ceng.postit.viterbi;

import java.io.IOException;

import tr.edu.metu.ceng.postit.data.Corpus;
import tr.edu.metu.ceng.postit.data.CorpusParser;
import tr.edu.metu.ceng.postit.data.Sentence;
import tr.edu.metu.ceng.postit.tagger.IPosTagger;

public class HMM {

	// start state symbol
	public static final String START_SYMBOL = "<S>";

	// end state symbol
	public static final String END_SYMBOL = "<E>";

	private IPosTagger posTagger;

	protected Corpus corpus = null;

	// prior probability matrix
	protected Matrix priorMatrix = null;

	// likelihood matrix
	protected Matrix likelihoodMatrix = null;

	public HMM(IPosTagger posTagger) {
		this.posTagger = posTagger;
	}

	/**
	 * Model training Compute probability matrices
	 * 
	 * @param filePath
	 *            training corpus file path
	 * @throws IOException
	 *             IOException
	 */
	public void train(String filePath) throws IOException {
		// construct corpus from given file path.
		corpus = new CorpusParser(posTagger, filePath).parse();
		// initalize prior (transition) and likelihood (emission) matrices.
		priorMatrix = new TransitionMatrix(corpus);
		likelihoodMatrix = new EmissionMatrix(corpus);
	}

	/**
	 * Part-of-Speech tagging, based on training corpus
	 * 
	 * @param sentence
	 *            given string sentence.
	 * @return corresponding tags sentence.
	 * @throws Exception
	 *             throws exception when file IO exception occurs
	 */
	public Sentence tag(String sentence) throws IOException {
		// initialize viterbi algorithm.
		Viterbi viterbi = new Viterbi(priorMatrix, likelihoodMatrix);
		// decode given sentence and tag words.
		return viterbi.decode(CorpusParser.parseSentence(sentence)).sort();
	}
}
