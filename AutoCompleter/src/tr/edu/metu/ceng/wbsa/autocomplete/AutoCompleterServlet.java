package tr.edu.metu.ceng.wbsa.autocomplete;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class AutoCompleterServlet extends HttpServlet {

	private static final long serialVersionUID = -3853227165072869306L;

	private static List<City> cities;

	// note that xml is only read once and set it to specified objects.
	static {
		try {
			// get the xml file...
			File xmlFile = new File(AutoCompleterServlet.class.getResource(
					"cities.xml").getPath());
			// create the doc builder factory...
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			// parse xml file and generate document.
			Document doc = db.parse(xmlFile);
			doc.getDocumentElement().normalize();

			// get city nodes...
			NodeList cityNodes = doc.getElementsByTagName("city");

			for (int i = 0; i < cityNodes.getLength(); i++) {
				// get current city node...
				Node cityNode = cityNodes.item(i);

				// create a new city with name property...
				City cityObj = new City();
				cityObj.setName(cityNode.getAttributes().getNamedItem("name")
						.getNodeValue());

				// get the county nodes...
				NodeList countyNodes = cityNode.getChildNodes();
				for (int j = 0; j < countyNodes.getLength(); j++) {
					Node countyNode = countyNodes.item(j);
					// add new county to this city...
					String textContent = countyNode.getTextContent();
					if (!((textContent == null) || (textContent.trim()
							.equals("")))) {
						cityObj.addCounty(textContent.trim());
					}
				}

				// add city object to cities list...
				getCities().add(cityObj);
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/xml");
		response.setHeader("Cache-Control", "no-cache");

		PrintWriter out = response.getWriter();
		String targetStartPrefix = URLDecoder.decode(
				request.getParameter("name"), "UTF-8");

		if (request.getParameter("type").equals("completion")) {
			if (targetStartPrefix != null)
				targetStartPrefix = targetStartPrefix.trim().toLowerCase();
			StringBuffer sb = new StringBuffer();
			Iterator<City> it = getCities().iterator();
			while (it.hasNext()) {
				City city = it.next();
				if (targetStartPrefix != null
						&& city.getName().toLowerCase()
								.startsWith(targetStartPrefix)) {
					sb.append("<city name=\"" + city.getName() + "\"></city>");
				}
			}
			out.write("<cities>" + sb.toString() + "</cities>");
		} else {
			if (targetStartPrefix != null)
				targetStartPrefix = targetStartPrefix.trim().toLowerCase();
			StringBuffer sb = new StringBuffer();
			Iterator<City> it = getCities().iterator();
			while (it.hasNext()) {
				City city = it.next();
				if (targetStartPrefix != null
						&& city.getName().toLowerCase()
								.equals(targetStartPrefix)) {
					sb.append("<city name=\"" + city.getName() + "\">");
					for (String county : city.getCounties()) {
						sb.append("<county>" + county + "</county>");
					}
					sb.append("</city>");
					break;
				}
			}
			out.write("<cities>" + sb.toString() + "</cities>");
		}
		out.close();
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}

	private static List<City> getCities() {
		if (cities == null) {
			cities = new Vector<City>();
		}
		return cities;
	}

}
