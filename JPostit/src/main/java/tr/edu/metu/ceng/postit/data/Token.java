package tr.edu.metu.ceng.postit.data;


/**
 * Token Model. A list of tokens form a sentence.
 */
public class Token implements Comparable<Token> {

	// index from original input
	private int index;

	private WordTagPair wordTagPair;

	public Token(int index, WordTagPair wordTagPair) {
		this.index = index;
		this.wordTagPair = wordTagPair;
	}

	public String getWord() {
		return wordTagPair.getWord();
	}

	public String getTag() {
		return wordTagPair.getTag();
	}

	public void setTag(String tag) {
		wordTagPair.setTag(tag);
	}

	@Override
	public int compareTo(Token o) {
		return (this.index < o.index) ? -1 : 1;
	}

	@Override
	public String toString() {
		return wordTagPair.toString();
	}
}
