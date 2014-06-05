package tr.edu.metu.ceng.postit.tagger;

public class PosTagger2 extends AbstractPosTagger {

	@Override
	protected String extractTag(String tagSentence) {
		// find model1 tag.
		String tag = (tagSentence.contains("^DB") ? tagSentence.substring(
				tagSentence.lastIndexOf("^DB") + ("^DB+".length()))
				.split("\\W")[0] : tagSentence.split("\\W")[0]);

		// also check for nouns.
		return tag.equalsIgnoreCase("noun") ? String.format("%s%s", tag,
				tagSentence.substring(tagSentence.lastIndexOf("+"))) : tag;
	}

}
