package tr.edu.metu.ceng.postit.tagger;

public class PosTagger1 extends AbstractPosTagger {

	@Override
	protected String extractTag(String tagSentence) {
		return (tagSentence.contains("^DB") ? tagSentence.substring(tagSentence
				.lastIndexOf("^DB") + ("^DB+".length())).split("\\W")[0] : tagSentence.split("\\W")[0]);

		// expanded version of the case above.
//		// check for modified tag.
//		if (tagSentence.contains("^DB")) {
//			// 5 for ^DB+
//			String lastTagSubstring = tagSentence.substring(tagSentence
//					.lastIndexOf("^DB") + ("^DB+".length()));
//			return lastTagSubstring.split("\\W")[0];
//		} else {
//			return tagSentence.split("\\W")[0];
//		}
	}
}
