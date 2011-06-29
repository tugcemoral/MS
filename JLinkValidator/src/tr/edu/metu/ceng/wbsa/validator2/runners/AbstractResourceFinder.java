package tr.edu.metu.ceng.wbsa.validator2.runners;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Vector;

import tr.edu.metu.ceng.wbsa.validator2.JLinkValidator;
import tr.edu.metu.ceng.wbsa.validator2.domain.ResourceType;
import tr.edu.metu.ceng.wbsa.validator2.domain.ResourceValidationInfo;

public abstract class AbstractResourceFinder {

	public static List<String> visitedImages = new Vector<String>();

	public static List<String> visitedScripts = new Vector<String>();
	
	protected static String rootURL;

	protected String baseURL;

	protected String domain;

	public AbstractResourceFinder(String baseURL, String domain) {
		rootURL = baseURL;
		this.baseURL = baseURL;
		this.domain = (domain == null) ? parseDomain() : domain;
	}

	protected ResourceValidationInfo validateURL(String url,
			ResourceType resourceType) {
		// create an image rvi...
		ResourceValidationInfo info = new ResourceValidationInfo();
		info.setResourceType(resourceType);
		try {
			// try to open a connection...
			HttpURLConnection connection = (HttpURLConnection) new URL(url)
					.openConnection();
			// make a HEAD request and get the response code...
			connection.setRequestMethod("HEAD");
			int responseCode = connection.getResponseCode();

			// no problem if HTTP_OK returned...
			if (responseCode == HttpURLConnection.HTTP_OK) {
				info.setValid(true);
				info.setUrl(url);
				info.setMessage("OK");
			} else {
				info.setUrl(url);
				info.setValid(false);
				info.setMessage(connection.getResponseMessage());
			}
		} catch (ClassCastException cce) {
			info.setUrl(url);
			info.setValid(false);
			info.setMessage("Only HTTP Connections are supported.");
		} catch (ProtocolException pe) {
			info.setUrl(url);
			info.setValid(false);
			info.setMessage("PKIX path building failed: unable to find valid certification path to requested target");
		} catch (MalformedURLException mue) {
			info.setUrl(url);
			info.setValid(false);
			info.setMessage(mue.getMessage());
		} catch (FileNotFoundException fnfe) {
			info.setUrl(url);
			info.setValid(false);
			info.setMessage(fnfe.getMessage());
		} catch (IOException ioe) {
			info.setUrl(url);
			info.setValid(false);
			info.setMessage(ioe.getMessage());
		}
		return info;
	}

	protected synchronized void addNewRVIToTable(ResourceValidationInfo rvi) {
		// add rvi to table...
		JLinkValidator.getInstance().addResourceValidationInfo(rvi);
	}

	private String parseDomain() {
		String domain = null;
		String withoutHttp = baseURL;
		if (baseURL.contains("http://")) {
			withoutHttp = baseURL.substring(baseURL.indexOf("/") + 2);
		}

		// if has a backslash, remove it and rest of it.
		if (withoutHttp.contains("/")) {
			withoutHttp = withoutHttp.substring(0, withoutHttp.indexOf("/"));
		}

		// if url contains www, remove it and extract domain.
		if (withoutHttp.contains("www")) {
			domain = withoutHttp.substring(withoutHttp.indexOf("w") + 4);
		} else {
			domain = withoutHttp;
		}

		return domain;
	}

}
