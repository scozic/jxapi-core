package com.scz.jxapi.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

public class TestJXApiProperties {
	
	public static final String TEST_CEX_API_PROPERTIES_FILE = "test-jxapi.properties";

	private static final Properties props = loadProperties();

	private static Properties loadProperties() {
		Properties props = new Properties();
		try {
		File propsFile = new File(TestJXApiProperties.class.getClassLoader().getResource(TEST_CEX_API_PROPERTIES_FILE).getFile());
		try (InputStream in = new BufferedInputStream(new FileInputStream(propsFile))) {
			props.load(in);
		}
		} catch (Exception ex) {
			throw new RuntimeException("Failed to load " + TEST_CEX_API_PROPERTIES_FILE + " properties file", ex);
		}
		return props;
	}
	
	public static Properties get() {
		return props;
	}
	
	public static String getProperty(String key) {
		return get().getProperty(key);
	}
	
	public static String getProperty(String domain, String key) {
		return getProperty(domain + "." + key);
	}
	
	public static Properties filterProperties(String namespace, boolean removeNamespace) {
		Properties res = new Properties();
		String prefix = namespace + ".";
		get().forEach((key, value) -> {
			if (key != null && (key instanceof String)) {
				String k = key.toString();
				if (k.startsWith(prefix)) {
					if (removeNamespace) {
						k = StringUtils.substringAfter(k, prefix);
					}
					res.put(k, value);
				}
			}
		});
		return res;
	}
}
