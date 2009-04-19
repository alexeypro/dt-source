package com.objecty.dtsource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class PropertiesLoader {

	// Instantiate Logger class
	private static final Logger log = Logger.getLogger(PropertiesLoader.class);

	// Properties we should load once
	static private Properties myProperties = null;

	public PropertiesLoader() {

		if (myProperties == null) {
			myProperties = new Properties();

			try {
				InputStream is = PropertiesLoader.class.getClassLoader().getResourceAsStream(Constants.DEFAULT_PROPERTIES);
				if (is == null) {
					log.debug("Properties file " + Constants.DEFAULT_PROPERTIES + " is empty");
				} else {
					myProperties.load(is);
				}
			}
			catch (IOException e) {
				log.debug("Properties file " + Constants.DEFAULT_PROPERTIES + " could not be opened");
			}
		}

	}

	// To get some property
	public String getProperty(String key) {
		return PropertiesLoader.myProperties.getProperty(key);
	}

}
