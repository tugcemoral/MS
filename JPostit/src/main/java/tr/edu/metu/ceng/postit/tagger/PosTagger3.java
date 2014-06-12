package tr.edu.metu.ceng.postit.tagger;

public class PosTagger3 extends AbstractPosTagger {

	@Override
	protected String extractTag(String tagSentence) {

		// find model1 tag.
		String tag = (tagSentence.contains("^DB") ? tagSentence.substring(
				tagSentence.lastIndexOf("^DB") + ("^DB+".length()))
				.split("\\W")[0] : tagSentence.split("\\W")[0]);

		// also check for nouns.
		String tagWithNoun = tag.equalsIgnoreCase("noun") ? String.format(
				"%s%s", tag,
				tagSentence.substring(tagSentence.lastIndexOf("+"))) : tag;

		// finally add stem information
		String wholeTag = tagSentence.contains("^DB") ? String.format("%s+%s",
				tagSentence.split("\\W")[0], tagWithNoun) : tagWithNoun;

		return wholeTag;
	}
}
