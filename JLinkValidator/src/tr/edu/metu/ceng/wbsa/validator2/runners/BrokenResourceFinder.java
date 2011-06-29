package tr.edu.metu.ceng.wbsa.validator2.runners;

import java.util.List;
import java.util.Vector;

import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import tr.edu.metu.ceng.wbsa.validator2.JLinkValidator;
import tr.edu.metu.ceng.wbsa.validator2.domain.ResourceType;
import tr.edu.metu.ceng.wbsa.validator2.domain.ResourceValidationInfo;

public class BrokenResourceFinder extends AbstractResourceFinder {

	private static List<String> visitedLinks = new Vector<String>();

	public BrokenResourceFinder(String baseURL, String domain) {
		super(baseURL, domain);
	}

	public void findAllBrokenResources() throws Exception {
		this.visitLink(baseURL, domain);
		// when finished, set progress info...
		JLinkValidator.getInstance().updateProgressInfo(
				"Web Crawling finished.");
	}

	private void visitLink(String baseURL, String domain) {
		visitedLinks.add(baseURL);
		try {
			// find images and scripts on this page...
			findImages(baseURL, domain);
			findScripts(baseURL, domain);

			Parser parser = new Parser(baseURL);
			NodeList list = parser.parse(new TagNameFilter("a"));
			for (int i = 0; i < list.size(); i++) {
				// extract the link tag...
				LinkTag linkTag = (LinkTag) list.elementAt(i);
				if (!linkTag.isMailLink()) {
					// extract the url...
					String url = linkTag.extractLink();

					if (!url.endsWith("/")) {
						// extract the url patch.
						String urlPatch = url
								.substring(url.lastIndexOf("/") + 1);
						if (!urlPatch.contains(".")) {
							url += "/";
						}
					}
					boolean isExecutable = checkExecutableScripts(url);

					if (url.contains(domain) && !visitedLinks.contains(url)) {
						if (isExecutable) {
							if (!visitedScripts.contains(url)) {
								// add it to scripts and do not recurse.
								visitedScripts.add(url);
								// validate it and add to table...
								ResourceValidationInfo validationInfo = validateURL(
										url, ResourceType.script);
								addNewRVIToTable(validationInfo);
							}
						} else {
							// create new rvi and add to table...
							ResourceValidationInfo validationInfo = validateURL(
									url, ResourceType.link);
							addNewRVIToTable(validationInfo);
							// recurse..
							visitLink(url, domain);
						}
					}
				}
			}
		} catch (ParserException e) {
			// silently ignore...
		}
	}

	private boolean checkExecutableScripts(String url) {
		// ignore .js, .php, .jsp, .asp, .aspx
		if (url.contains(".js") || url.contains(".php") || url.contains(".jsp")
				|| url.contains(".asp") || url.contains(".aspx")
				|| url.contains("?")) {
			return true;
		}
		return false;
	}

	private void findScripts(String baseURL, String domain)
			throws ParserException {
		// create a new image finder thread and start it...
		Thread scriptFinderThread = new Thread(new ScriptFinderThread(baseURL,
				domain));
		scriptFinderThread.start();

	}

	private void findImages(String baseURL, String domain)
			throws ParserException {
		// create a new image finder thread and start it...
		Thread imageFinderThread = new Thread(new ImageFinderThread(baseURL,
				domain));
		imageFinderThread.start();
	}

}
