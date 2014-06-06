package tr.edu.metu.ceng.postit.tagger;

import java.util.List;
import java.util.Vector;

public class PosTagger3 extends AbstractPosTagger {

	@Override
	protected String extractTag(String tagSentence) {

		List<String> tags = new Vector<String>();

		// check for modified tag.
		if (tagSentence.contains("^DB+")) {
			// split tag locations accordingly.
			String[] splittedTagLocations = tagSentence.split("\\^DB\\+");

			// get first splitted element's first splitted element(first tag).
			String firstTag = splittedTagLocations[0].split("\\W")[0];
			// add to tags.
			tags.add(firstTag);

			// extract next tags.
			for (int i = 1; i < splittedTagLocations.length; i++) {
				String nextTag = splittedTagLocations[i].split("\\W")[0];
				if (!tags.contains(nextTag)) {
					tags.add(nextTag);
				}
			}

		} else {
			// only add single tag.
			tags.add(tagSentence.split("\\W")[0]);
		}

		// if the list itself contains "noun", add additional case information.
		if (containsIgnoreCase(tags, "noun")) {
			tags.add(tagSentence.substring(tagSentence.lastIndexOf("+")+1));
		}
		return stringifyTagList(tags);
	}

	private String stringifyTagList(List<String> tags) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < tags.size(); i++) {
			if (i < tags.size() - 1) {
				sb.append(tags.get(i)).append("+");
			} else {
				sb.append(tags.get(i));
			}
		}
		return sb.toString();
	}

	private boolean containsIgnoreCase(List<String> list, String s) {
		for (String element : list) {
			if (element.equalsIgnoreCase(s)) {
				return true;
			}
		}
		return false;
	}

}
