package tr.edu.metu.ceng.wbsa.validator2.runners;

import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.ScriptTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import tr.edu.metu.ceng.wbsa.validator2.domain.ResourceType;
import tr.edu.metu.ceng.wbsa.validator2.domain.ResourceValidationInfo;

public class ScriptFinderThread extends AbstractResourceFinder implements
		Runnable {

	public ScriptFinderThread(String baseURL, String domain) {
		super(baseURL, domain);
	}

	@Override
	public void run() {
		try {
			// create a script parser...
			Parser scriptParser = new Parser(baseURL);
			// parse and get the script list which has src attribute...
			NodeList scriptList = scriptParser
					.parse(new AndFilter(new TagNameFilter("script"),
							new HasAttributeFilter("src")));

			for (int i = 0; i < scriptList.size(); i++) {
				// downcast to script tag...
				ScriptTag scriptTag = (ScriptTag) scriptList.elementAt(i);
				// get script name
				String scriptName = scriptTag.getAttribute("src");

				// means external script library.
				if (scriptName.contains("http://")
						|| scriptName.contains("https://")
						|| scriptName.contains("www")) {
					// create a rvi...
					ResourceValidationInfo rvi = new ResourceValidationInfo(
							scriptName, false, ResourceType.image,
							"External Reference.");
					// add rvi to table...
					addNewRVIToTable(rvi);
				} else {
					if (scriptName.startsWith("/")) {
						// means that it's under root.
						scriptName = rootURL + scriptName;
					} else {
						// means that its under current dir.
						scriptName = baseURL + scriptName;
					}
					// check that it has not visited before...
					if (!visitedScripts.contains(scriptName)) {
						visitedScripts.add(scriptName);
						ResourceValidationInfo info = validateURL(scriptName,
								ResourceType.script);
						// add rvi to table...
						addNewRVIToTable(info);
					}
				}
			}
		} catch (ParserException e) {
			// sssh.
		}
	}
}
