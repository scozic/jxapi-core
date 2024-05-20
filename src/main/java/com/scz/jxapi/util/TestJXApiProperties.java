package com.scz.jxapi.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

public class TestJXApiProperties {
	
	public static final String DEMO_API_PROPERTIES_FILE = "test-jxapi.properties";
	
	public static final String DEMO_WS_SUBSCRIPTION_DURATION_SYS_PROP = "jxapi.demo.ws.subscriptionDuration";
	public static final long DEMO_WS_DEFAULT_SUBSCRIPTION_DURATION = 60000L;
	public static final long DEMO_WS_SUBSCRIPTION_DURATION = getLongSysProp(DEMO_WS_SUBSCRIPTION_DURATION_SYS_PROP, DEMO_WS_DEFAULT_SUBSCRIPTION_DURATION);
	public static final String DEMO_WS_DELAY_BEFORE_EXIT_AFTER_UNSUBSCRIPTION_SYS_PROP = "jxapi.demo.ws.delayBeforeExitAfterUnsubscription";
	public static final long DEMO_WS_DEFAULT_DELAY_BEFORE_EXIT_AFTER_UNSUBSCRIPTION = 1000L;
	public static final long DEMO_WS_DELAY_BEFORE_EXIT_AFTER_UNSUBSCRIPTION = getLongSysProp(DEMO_WS_DELAY_BEFORE_EXIT_AFTER_UNSUBSCRIPTION_SYS_PROP, 
						   																	 DEMO_WS_DEFAULT_DELAY_BEFORE_EXIT_AFTER_UNSUBSCRIPTION);
	
	private static final Properties props = loadProperties();
	
	private static final long getLongSysProp(String key, long def) {
		try {
			return Long.parseLong(System.getProperty(key, "" + def));
		} catch (Exception ex) {
			throw new RuntimeException("Invalid value for property '" + key + "'", ex);
		}
	}

	private static Properties loadProperties() {
		Properties props = new Properties();
		try {
		File propsFile = new File(TestJXApiProperties.class.getClassLoader().getResource(DEMO_API_PROPERTIES_FILE).getFile());
		try (InputStream in = new BufferedInputStream(new FileInputStream(propsFile))) {
			props.load(in);
		}
		} catch (Exception ex) {
			throw new RuntimeException("Failed to load " + DEMO_API_PROPERTIES_FILE + " properties file", ex);
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
