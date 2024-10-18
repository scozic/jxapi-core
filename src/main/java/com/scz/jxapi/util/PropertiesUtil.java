package com.scz.jxapi.util;

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

}
