package tr.edu.metu.ceng.postit.viterbi;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import tr.edu.metu.ceng.postit.data.Corpus;
import tr.edu.metu.ceng.postit.data.CorpusParser;
import tr.edu.metu.ceng.postit.data.Sentence;
import tr.edu.metu.ceng.postit.data.Token;
import tr.edu.metu.ceng.postit.evaluation.Evaluation;
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

	public Evaluation evaluate(String developmentFilePath)
			throws FileNotFoundException, CloneNotSupportedException {
		//parse corpus and get development sentences.
		List<Sentence> developmentSentences = new CorpusParser(posTagger,
				developmentFilePath).parse().getSentences();
		
		//initialize the viterbi with predefined prior and likelihood matrices.
		Viterbi viterbi = new Viterbi(priorMatrix, likelihoodMatrix);
		
		int trueTagged = 0;
		int falseTagged = 0;
		int totalNumberOfTagged = 0;
		int numberOfSentences = developmentSentences.size();
		
		for (Sentence sentence : developmentSentences) {
			Sentence originalSentence = sentence.clone().sort();
			Sentence decodedSentence = viterbi.decode(sentence).sort();
			
			for (int i=0;i<originalSentence.getTokens().size();i++) {
				Token originalToken = originalSentence.getTokens().get(i);
				Token decodedToken = null;
				try {
					decodedToken = decodedSentence.getTokens().get(i);
				} catch (Exception e) {
					System.out.println();
					
				}
				
				if(originalToken.getTag().equalsIgnoreCase(decodedToken.getTag())){
					trueTagged++;
				}else {
					falseTagged++;
				}
				totalNumberOfTagged++;
			}
		}

		return new Evaluation(trueTagged, falseTagged, totalNumberOfTagged, numberOfSentences);
	}
}
