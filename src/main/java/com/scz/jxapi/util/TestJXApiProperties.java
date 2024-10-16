package com.scz.jxapi.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * Stores access to JXAPI core properties and demo classes default values.
 * <p>
 * The properties are loaded from the {@value #DEMO_API_PROPERTIES_FILE} file.
 * <p>
 * The properties can be accessed using the {@link #get()} method or by using the {@link #getProperty(String)} method.
 * <p>
 * The properties can be filtered by namespace using the {@link #filterProperties(String, boolean)} method.
 * <p>
 */
public class TestJXApiProperties {
	
	/**
	 * The name of the properties file containing the JXAPI core and demo classes properties.
	 */
	public static final String DEMO_API_PROPERTIES_FILE = "test-jxapi.properties";
	
	/**
	 * The name of the property containing the duration of the subscription in WebSocket endpoint demo classes.
	 */
	public static final String DEMO_WS_SUBSCRIPTION_DURATION_SYS_PROP = "jxapi.demo.ws.subscriptionDuration";

	/**
	 * The default duration of the subscription in WebSocket endpoint demo classes when the property {@value #DEMO_WS_SUBSCRIPTION_DURATION_SYS_PROP} is not set.
	 */
	public static final long DEMO_WS_DEFAULT_SUBSCRIPTION_DURATION = 60000L;

	/**
	 * The duration of the subscription in WebSocket endpoint demo classes, in milliseconds.
	 */
	public static final long DEMO_WS_SUBSCRIPTION_DURATION = getLongSysProp(DEMO_WS_SUBSCRIPTION_DURATION_SYS_PROP, DEMO_WS_DEFAULT_SUBSCRIPTION_DURATION);

	/**
	 * The name of the property containing the delay before exiting program after unsubscribing in WebSocket endpoint demo classes.
	 */
	public static final String DEMO_WS_DELAY_BEFORE_EXIT_AFTER_UNSUBSCRIPTION_SYS_PROP = "jxapi.demo.ws.delayBeforeExitAfterUnsubscription";

	/**
	 * The default delay before exiting program after unsubscribing in WebSocket endpoint demo classes when the property {@value #DEMO_WS_DELAY_BEFORE_EXIT_AFTER_UNSUBSCRIPTION_SYS_PROP} is not set.
	 */
	public static final long DEMO_WS_DEFAULT_DELAY_BEFORE_EXIT_AFTER_UNSUBSCRIPTION = 1000L;

	/**
	 * The delay before exiting program after unsubscribing in WebSocket endpoint demo classes, in milliseconds.
	 */
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
	
	/**
	 * Returns the properties.
	 * @return the properties
	 */
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
		return PropertiesUtil.filterProperties(get(), namespace, removeNamespace);
	}
}
