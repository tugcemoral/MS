package tr.edu.metu.ceng.wbsa.htmlparser;

import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;

import org.htmlparser.Parser;
import org.htmlparser.filters.LinkStringFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;

public class BadLinks {

	private static HashSet<String> visited = new HashSet<String>();
	private static HashSet<String> toVisit = new HashSet<String>();
	private static HashSet<String> badLinks = new HashSet<String>();

	public static void visitLink(String URL, String domain) throws Exception {
		visited.add(URL);
		toVisit.remove(URL);
		try {
			Parser parser = new Parser(URL);
			NodeList list = parser.parse(new LinkStringFilter("http:"));
			for (int i = 0; i < list.size(); i++) {
				String st = ((LinkTag) (list.elementAt(i))).extractLink();
				if (st.contains(domain) && !visited.contains(st))
					toVisit.add(st);
				System.out.println(st);
			}
		}

		catch (Exception e) {
			try {
				URL u = new URL(URL);
				u.openStream();
			} catch (Exception f) {
				System.err.println(URL);
				badLinks.add(URL);
			}
		}
	}

	public static void main(String args[]) throws Exception {
		toVisit.add(args[0]);
		Iterator<String> it = toVisit.iterator();
		while (it.hasNext()) {
			visitLink(it.next(), args[1]);
			it = toVisit.iterator();
		}
		for (String s : badLinks)
			System.out.println("The Bad Link is " + s);
	}
}
