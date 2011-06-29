package tr.edu.metu.ceng.wbsa.validator2.runners;

import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import tr.edu.metu.ceng.wbsa.validator2.domain.ResourceType;
import tr.edu.metu.ceng.wbsa.validator2.domain.ResourceValidationInfo;

public class ImageFinderThread extends AbstractResourceFinder implements
		Runnable {

	public ImageFinderThread(String baseURL, String domain) {
		super(baseURL, domain);
	}

	@Override
	public void run() {

		try {
			Parser parser = new Parser(baseURL);
			NodeList imgList = parser.parse(new AndFilter(new TagNameFilter(
					"img"), new HasAttributeFilter("src")));

			for (int i = 0; i < imgList.size(); i++) {
				// extract the link tag...
				ImageTag imageTag = (ImageTag) imgList.elementAt(i);
				// extract the url...
				String url = imageTag.getImageURL();

				if (!url.contains(domain)) {
					// create a rvi...
					ResourceValidationInfo rvi = new ResourceValidationInfo(
							url, false, ResourceType.image,
							"External Reference.");
					// add rvi to table...
					addNewRVIToTable(rvi);
				} else {
					if (!BrokenResourceFinder.visitedImages.contains(url)) {
						BrokenResourceFinder.visitedImages.add(url);
						ResourceValidationInfo info = validateURL(url,
								ResourceType.image);
						// add rvi to table...
						super.addNewRVIToTable(info);
					}
				}
			}
		} catch (ParserException e) {
			// sssh.
		}

	}

}
