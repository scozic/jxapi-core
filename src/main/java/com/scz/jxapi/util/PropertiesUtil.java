package com.scz.jxapi.util;

import java.math.BigDecimal;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

/**
 * Helper methods around properties management.
 */
public class PropertiesUtil {

	private PropertiesUtil() {}
	
	/**
	 * Filters the properties by namespace, for instance "namespace.key=value".
	 * 
	 * @param source          The source properties
	 * @param namespace       The namespace to filter for instance "namespace"
	 * @param removeNamespace If true, the namespace is removed from the keys
	 * @return new Properties instance containing only the properties with the given
	 *         namespace. The namespace can be removed from the keys if
	 *         <code>removeNamespace</code> is <code>true</code>.
	 */
	public static Properties filterProperties(Properties source, 
											  String namespace, 
											  boolean removeNamespace) {
		if (source == null) {
			return null;
		}
		Properties res = new Properties();
		String prefix = namespace + ".";
		source.forEach((key, value) -> {
			if (key instanceof String) {
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
	
	public static String getStringProperty(Properties properties, String key, String defaultValue) {
		Object v = properties.get(key);
		if (v != null) {
			return v.toString();
		}
		return defaultValue;
	}
	
	public static Integer getIntProperty(Properties properties, String key, Integer defaultValue) {
		Object v = properties.get(key);
		if (v != null) {
			return Integer.valueOf(v.toString());
		}
		return defaultValue;
	}
	
	public static Long getLongProperty(Properties properties, String key, Long defaultValue) {
		Object v = properties.get(key);
		if (v != null) {
			if("now()".equals(v)) {
				return Long.valueOf(System.currentTimeMillis());
			}
			return Long.valueOf(v.toString());
		}
		return defaultValue;
	}
	
	public static BigDecimal getBigDecimalProperty(Properties properties, String key, BigDecimal defaultValue) {
		Object v = properties.get(key);
		if (v != null) {
			return new BigDecimal(v.toString());
		}
		return defaultValue;
	}
	
	public static Boolean getBooleanProperty(Properties properties, String key, Boolean defaultValue) {
		Object v = properties.get(key);
		if (v != null) {
			return Boolean.valueOf(v.toString());
		}
		return defaultValue;
	}

}
