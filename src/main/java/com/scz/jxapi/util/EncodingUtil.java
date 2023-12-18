package com.scz.jxapi.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.text.StringSubstitutor;

/**
 * Helper methods around String encoding.
 */
public class EncodingUtil {

	private EncodingUtil() {}
	
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
	
	public static String createUrlQueryParameters(Object...keysAndValues) {
		if (keysAndValues.length <= 0) {
			return "";
		}
		if (keysAndValues.length % 2 != 0) {
			throw new IllegalArgumentException("Invalid arguments: should even size but got" 
												+ keysAndValues.length + ":" 
												+ Arrays.toString(keysAndValues));
		}
		StringBuilder s = new StringBuilder();
		boolean first = true;
		for (int i = 0; i < keysAndValues.length; i++) {
			Object key = keysAndValues[i++];
			if (key == null) {
				throw new IllegalArgumentException("null key in for parameter #" + (i / 2) + " in:" + Arrays.toString(keysAndValues));
			}
			Object value = keysAndValues[i];
			if (value == null) {
				continue;
			}
			if (first) {
				s.append("?");
				first = false;
			} else {
				s.append("&");
			}
			try {
				value = URLEncoder.encode(String.valueOf(value), StandardCharsets.UTF_8.toString());
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException("Invalid value for parameter:" + key + ":" + keysAndValues[i], e);
			}
			s.append(key).append("=").append(value);
		}
		return s.toString();
	}
	
	/**
	 * @param items Value of an URL param of String list type. Can be
	 *              <code>null</code> if argument is optional for corresponding API,
	 *              in which case <code>null</code> will be returned and URL
	 *              argument will be ignored.
	 * @return String like '["a","b","c"]'. This value is expected to be URL encoded
	 *         in final URL.
	 */
	public static String listToUrlParamString(List<String> items) {
		if (items == null)
			return null;
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < items.size(); i++) {
			
			sb.append("\"").append(items.get(i)).append("\"");
			if (i < items.size() -1) {
				sb.append(",");
			}
		}
		return sb.append("]").toString();
	}
	
	public static String listToString(List<String> items, String separator) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < items.size(); i++) {
			sb.append(items.get(i));
			if (i < items.size() -1) {
				sb.append(separator);
			}
		}
		return sb.toString();
	}
	
	public static String bigDecimalToString(BigDecimal bd) {
		return Optional.ofNullable(bd).orElse(BigDecimal.ZERO).toPlainString();
	}
	
	public static BigDecimal toBigDecimal(String s) {
		if (s == null || s.isEmpty())
			return null;
		return new BigDecimal(s);
	}

	public static String pojoToString(Object pojo) {
		return pojo.getClass().getSimpleName() + JsonUtil.pojoToJsonString(pojo); 
	}
}
