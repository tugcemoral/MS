package tr.edu.metu.ceng.postit.data;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Corpus Model
 */
public class Corpus {

	// sentences from input corpus
	private List<Sentence> sentences = null;

	// distinct words
	private Set<String> vocabulary = null;

	/**
	 * Constructing a corpus
	 * 
	 * @param filePath
	 *            corpus path
	 * @throws IOException
	 */
	public Corpus(List<Sentence> sentences) {
		this.sentences = sentences;
		this.constructVocabulary();
	}

	private void constructVocabulary() {
		vocabulary = new HashSet<String>();
		for (Sentence sentence : sentences) {
			// traversing the words in the current sentence
			// a CoreLabel is a CoreMap with additional token-specific methods
			for (Token token : sentence.getTokens()) {
				// this is the text of the token
				String word = token.getWord();
				// calculate vocabulary size
				vocabulary.add(word);
			}
		}
	}

	/**
	 * @return vocabulary size
	 */
	public Integer vocabularySize() {
		return vocabulary.size();
	}

	/**
	 * @return sentences from input corpus
	 */
	public List<Sentence> getSentences() {
		return sentences;
	}

	public Set<String> getVocabulary() {
		return vocabulary;
	}

	/**
	 * Check if current corpus contains word
	 * 
	 * @param word
	 *            any word
	 * @return true if current corpus contains word
	 */
	public boolean containsWord(String word) {
		if (word == null || word.trim().length() == 0)
			return false;
		return vocabulary.contains(word.toLowerCase());
	}
}
