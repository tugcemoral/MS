package tr.edu.metu.ceng.postit.data;

import java.util.Locale;

public class WordTagPair implements Cloneable{

	private static final Locale LOCALE_TR = new Locale("tr", "TR");

	//lowercased tag
	private String word;

	//part-of-speech tag.
	private String tag;

	// original word and tag values, used for representation only.
	private String originalWord;
//	private String originalTag;

	public WordTagPair(String word, String tag) {
		this.originalWord = word;
		this.word = word.toLowerCase(LOCALE_TR);
		
//		this.originalTag = tag == null ? "" : tag;
		this.tag = tag == null ? "" : tag/*.toLowerCase(LOCALE_TR)*/;
	}

	public String getOriginalWord(){
		return originalWord;
	}
	
	public String getWord() {
		return word;
	}
	
//	public String getOriginalTag(){
//		return originalTag;
//	}

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
	
	@Override
	protected WordTagPair clone() throws CloneNotSupportedException {
		return new WordTagPair(this.originalWord, this.tag);
	}


}
