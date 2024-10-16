package com.scz.jxapi.util;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

public class PropertiesUtil {

	private PropertiesUtil() {}
	
	public static Properties filterProperties(Properties source, String namespace, boolean removeNamespace) {
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
