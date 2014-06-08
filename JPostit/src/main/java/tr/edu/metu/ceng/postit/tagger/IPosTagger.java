package tr.edu.metu.ceng.postit.tagger;

import java.io.IOException;

import tr.edu.metu.ceng.postit.data.WordTagPair;

public interface IPosTagger {

	public WordTagPair convert(String analysis);
	
	public String tag(String sentence) throws IOException;
	
}
