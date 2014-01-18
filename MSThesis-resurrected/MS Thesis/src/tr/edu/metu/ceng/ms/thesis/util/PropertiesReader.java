package tr.edu.metu.ceng.ms.thesis.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Properties;

public class PropertiesReader {

	private static final String PROP_FILE_NAME = "env.properties";

	private static PropertiesReader reader;

	private static Properties properties;

	public static PropertiesReader getInstance() {
		if (reader == null) {
			// initialize the properties reader.
			reader = new PropertiesReader();
			// read the properties file.
			readProperties();
		}
		return reader;
	}

	/**
	 * Reads the properties and initializes {@link #properties} instance. Loads
	 * one, uses more than one.
	 */
	private static void readProperties() {
		try {
			// try to get the properties file path.
			String propertiesPath = URLDecoder.decode(new File(
					PropertiesReader.class.getResource("").getFile())
					.getParentFile().getPath()
					+ File.separator + PROP_FILE_NAME, "UTF-8");
			// load the properties file...
			properties = new Properties();
			properties.load(new FileInputStream(propertiesPath));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private PropertiesReader() {
	}

	public String getProperty(String key) throws PropertyNotFoundException {
		String property = properties.getProperty(key);
		if (property == null)
			throw new PropertyNotFoundException(property);
		return property;
	}

	public int getIntProperty(String key) throws NumberFormatException,
			PropertyNotFoundException {
		return Integer.parseInt(this.getProperty(key));
	}

}
