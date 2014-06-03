package tr.edu.metu.ceng.postit.tagger;

public class WordTagPair {

	private String word;

	private String tag;

	public WordTagPair(String word, String tag) {
		this.word = word;
		this.tag = tag;
	}

	public String getWord() {
		return word;
	}

	public String getTag() {
		return tag;
	}

	@Override
	public String toString() {
		return String.format("[%s, %s]", getWord(), getTag());
	}

}
