package com.scz.jxapi.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * Stores access to JXAPI core properties and demo classes default values.
 * <p>
 * The properties are loaded from the {@value #DEMO_API_PROPERTIES_FILE} file.
 * This file is not present initially and should be created from existing
 * 'test-jxapi.properties.dist' template in exchange wrapper 'template'.
 * Developpers should define that file in <code>src/test/resources</code>
 * folder. If file is not found, empty properties are used. Properties are
 * loaded only once when the class is loaded.
 * <p>
 * The properties can be accessed using the {@link #get()} method or by using
 * the {@link #getProperty(String)} method. However, usage of
 * {@link #getProperty(String)} should be preferred as it will look in system
 * properties first, in case for a specific demo a property is overridden in
 * system properties.
 * <p>
 * The properties can be filtered by namespace using the
 * {@link #filterProperties(String, boolean)} method.
 * <p>
 * Though this is used in a 'test' context this class is in main package as it
 * is used in generated demos code
 */
public class TestJXApiProperties {

	/**
	 * The name of the properties file containing the JXAPI core and demo classes
	 * properties.
	 */
	public static final String DEMO_API_PROPERTIES_FILE = "test-jxapi.properties";
	
	private static final Properties props = loadProperties();

	/**
	 * The name of the property containing the duration of the subscription in
	 * WebSocket endpoint demo classes.
	 */
	public static final String DEMO_WS_SUBSCRIPTION_DURATION_SYS_PROP = "jxapi.demo.ws.subscriptionDuration";

	/**
	 * The default duration of the subscription in WebSocket endpoint demo classes
	 * when the property {@value #DEMO_WS_SUBSCRIPTION_DURATION_SYS_PROP} is not
	 * set.
	 */
	public static final long DEMO_WS_DEFAULT_SUBSCRIPTION_DURATION = 60000L;

	/**
	 * The duration of the subscription in WebSocket endpoint demo classes, in
	 * milliseconds.
	 */
	public static final long DEMO_WS_SUBSCRIPTION_DURATION = getLongProp(DEMO_WS_SUBSCRIPTION_DURATION_SYS_PROP,
			DEMO_WS_DEFAULT_SUBSCRIPTION_DURATION);

	/**
	 * The name of the property containing the delay before exiting program after
	 * unsubscribing in WebSocket endpoint demo classes.
	 */
	public static final String DEMO_WS_DELAY_BEFORE_EXIT_AFTER_UNSUBSCRIPTION_SYS_PROP = "jxapi.demo.ws.delayBeforeExitAfterUnsubscription";

	/**
	 * The default delay before exiting program after unsubscribing in WebSocket
	 * endpoint demo classes when the property
	 * {@value #DEMO_WS_DELAY_BEFORE_EXIT_AFTER_UNSUBSCRIPTION_SYS_PROP} is not set.
	 */
	public static final long DEMO_WS_DEFAULT_DELAY_BEFORE_EXIT_AFTER_UNSUBSCRIPTION = 1000L;

	/**
	 * The delay before exiting program after unsubscribing in WebSocket endpoint
	 * demo classes, in milliseconds.
	 */
	public static final long DEMO_WS_DELAY_BEFORE_EXIT_AFTER_UNSUBSCRIPTION = getLongProp(
			DEMO_WS_DELAY_BEFORE_EXIT_AFTER_UNSUBSCRIPTION_SYS_PROP,
			DEMO_WS_DEFAULT_DELAY_BEFORE_EXIT_AFTER_UNSUBSCRIPTION);

	private static final long getLongProp(String key, long def) {
		try {
			String value = getProperty(key);
			if (value == null) {
				return def;
			}
			return Long.parseLong(value);
		} catch (Exception ex) {
			throw new RuntimeException("Invalid value for property '" + key + "'", ex);
		}
	}

	private static Properties loadProperties() {
		Properties props = new Properties();
		try {
			URL url = TestJXApiProperties.class.getClassLoader().getResource(DEMO_API_PROPERTIES_FILE);
			if (url != null) {
				File propsFile = new File(url.getFile());
				if (propsFile.exists()) {
					try (InputStream in = new BufferedInputStream(new FileInputStream(propsFile))) {
						props.load(in);
					}
				}
			}
		} catch (Exception ex) {
			throw new RuntimeException("Failed to load " + DEMO_API_PROPERTIES_FILE + " properties file", ex);
		}
		return props;
	}

	/**
	 * Returns the properties.
	 * 
	 * @return the properties
	 */
	public static Properties get() {
		return props;
	}

	/**
	 * Returns the property value for the given key.
	 * <p>
	 * The property value is looked up in system properties first, then in the loaded properties from the {@value #DEMO_API_PROPERTIES_FILE} file. 
	 * 
	 * @param key The property key
	 * @return the property value or <code>null</code> if the property is not found
	 * 
	 * @see Properties#getProperty(String)
	 */
	public static String getProperty(String key) {
		String v = System.getProperty(key);
		if (v != null) {
			return v;
		}
		return get().getProperty(key);
	}
	/**
	 * Filters the properties by namespace, for instance "namespace.key=value".
	 * 
	 * @param namespace       The namespace to filter for instance "namespace"
	 * @param removeNamespace If true, the namespace is removed from the keys
	 * @return new Properties instance containing only the properties with the given
	 *         namespace. The namespace can be removed from the keys if
	 *         <code>removeNamespace</code> is <code>true</code>.
	 * @see PropertiesUtil#filterProperties(Properties, String, boolean)
	 */
	public static Properties filterProperties(String namespace, boolean removeNamespace) {
		return PropertiesUtil.filterProperties(get(), namespace, removeNamespace);
	}
}
