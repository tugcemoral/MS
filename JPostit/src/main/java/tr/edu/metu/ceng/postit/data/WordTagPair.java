package tr.edu.metu.ceng.postit.data;

import java.util.Locale;

public class WordTagPair {

	//lowercased tag
	private String word;

	private String originalWord;
	
	//part-of-speech tag.
	private String tag;

	public WordTagPair(String word, String tag) {
		this.originalWord = word;
		this.word = word.toLowerCase(new Locale("tr", "TR"));
		this.tag = tag == null ? "" : tag;
	}

	public String getOriginalWord(){
		return originalWord;
	}
	
	public String getWord() {
		return word;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
	
	@Override
	public String toString() {
		return String.format("(%s: %s)", getOriginalWord(), getTag());
	}


}
