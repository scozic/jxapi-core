package com.scz.jcex.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.text.StringSubstitutor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Helper methods around String encoding.
 */
public class EncodingUtil {

	private EncodingUtil() {}
	
	public static String pojoToString(Object pojo) {
		return pojo.getClass().getSimpleName() + pojoToJsonString(pojo); 
	}
	
	public static String pojoToJsonString(Object pojo) { 
		try {
			return new ObjectMapper().writeValueAsString(pojo);
		} catch (JsonProcessingException e) {
			throw new IllegalArgumentException("Error while trying to serialize " + pojo.getClass().getName() + " instance to JSON", e);
		}
	}
	
	/**
	 * Substitures properties from a map using provided key values pairs.<br/>
	 * Example:
	 * <pre>
	 * substituteArguments("Hello ${stranger}, I am ${me}", "stranger", "Bob", "me", "Roger")
	 * </pre>
	 * returns:
	 * <pre>
	 * "Hello Bob, I am Roger"
	 * </pre>
	 * @param template the template String to perform that contains variables
	 * @param keysAndValues flat list of key, value pair for variable substitutions
	 * @return
	 */
	public static String substituteArguments(String template, Object... keysAndValues) {
		if (keysAndValues.length == 0) {
			return template;
		}
		Map<String, Object> m = new HashMap<>(keysAndValues.length / 2);
		for(int i = 0; i < keysAndValues.length;) {
			m.put(String.valueOf(keysAndValues[i++]), keysAndValues[i++]);
		}
		StringSubstitutor sub = new StringSubstitutor(m);
		return sub.replace(template);
	}
}
