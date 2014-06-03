package tr.edu.metu.ceng.postit.tagger;

public class PosTagger1 extends AbstractPosTagger {

	@Override
	protected String extractTag(String tagSentence) {

		// check for modified tag.
		if (tagSentence.contains("^DB")) {

//			tagSentence.substring(tagSentence.lastIndexOf("^DB"))
			return null;
			
		}else {
			return tagSentence.split("\\W")[0];
		}
	}

}
