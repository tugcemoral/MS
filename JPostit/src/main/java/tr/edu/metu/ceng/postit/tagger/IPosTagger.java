package tr.edu.metu.ceng.postit.tagger;

public interface IPosTagger {

	public WordTagPair convert(String analysis);
	
	public void tag(String sentence);
	
}
