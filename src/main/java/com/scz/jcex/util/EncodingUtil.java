package com.scz.jcex.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.text.StringSubstitutor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

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
			ObjectMapper om = new ObjectMapper();
			om.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
			return om.writeValueAsString(pojo);
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
	
	/**
	 * 
	 * @param keysAndValues <pre>key1,value1,key2,value2...
	 * @return "{"key1": "value1", "key2: "value2"...}"
	 */
	public static String formatArgsToJsonStruct(Object...keysAndValues) {
		StringBuilder s = new StringBuilder();
		s.append("{");
		for (int i = 0; i < keysAndValues.length; i++) {
			if (i > 1)
				s.append(", ");
			s.append(fmt(keysAndValues[i++]));
			s.append(":");
			s.append(fmt(keysAndValues[i]));
		}
		return s.append("}").toString();
	}
	
	private static String fmt(Object o) {
		if (o == null)
			return "\"null\"";
		if (o instanceof Integer) {
			return "" + ((Integer) o).intValue();
		}
		if (o instanceof Long) {
			return "" + ((Long) o).longValue();
		}
		return "\"" + String.valueOf(o) + "\"";
			
	}
}
