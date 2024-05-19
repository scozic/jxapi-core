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
	
	/**
	 * Creates a query param for given list of keys and values. Keys are expected to be plain string that do no contain forbidden URL characters. Values can be of any type, will be converted to string, that string will be URL encoded (see {@link URLEncoder}).
	 * @param keysAndValues List of keys and values, for instance:<code>"id","myId","name","John Doe","age",28,"height":1.70</code>
	 * @return Query parameters string to append as URL suffix, for instance: <code>"?id=myId&name=John%20Doe&age=28height=1.170"</code>
	 */
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
			value = urlEncode(String.valueOf(value));
			s.append(key).append("=").append(value);
		}
		return s.toString();
	}
	
	/**
	 * Converts a list of String to a plain String with items of list concatenated
	 * using given separator.
	 * 
	 * @param items
	 * @param separator
	 * @return
	 */
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
	
	/**
	 * Converts a {@link BigDecimal} to plain String. <code>null</code> value will
	 * be converted to <code>"0"</code>. Other values will be converted as plain
	 * String, see {@link BigDecimal#toPlainString()}
	 * 
	 * @param bd
	 * @return "0" if <code>bd</code> arg is <code>null</code>,
	 *         <code>bd.toPlainString()</code> otherwise
	 */
	public static String bigDecimalToString(BigDecimal bd) {
		return Optional.ofNullable(bd).orElse(BigDecimal.ZERO).toPlainString();
	}
	
	/**
	 * Converts a {@link String} containing floating point numeric value to a
	 * {@link BigDecimal}.
	 * 
	 * @param s {@link String} containing a floating point numeric value
	 * @return <code>null</code> if <code>s</code> is <code>null</code>, result of
	 *         <code>new BigDecimal(s)</code> otherwise.
	 */
	public static BigDecimal toBigDecimal(String s) {
		if (s == null || s.isEmpty())
			return null;
		return new BigDecimal(s);
	}

	/**
	 * Converts a POJO to string, concatenating is simple class name to is JSON
	 * string representation
	 * 
	 * @param pojo POJO to convert
	 * @return concatenation of <code>pojo</code> simple class name and its JSON
	 *         string representation
	 */
	public static String pojoToString(Object pojo) {
		return pojo.getClass().getSimpleName() + JsonUtil.pojoToJsonString(pojo); 
	}
	
	/**
	 * Shortcut for
	 * <code>URLEncoder.encode(value, StandardCharsets.UTF_8.toString());</code>.
	 * The eventual {@link UnsupportedEncodingException} thrown will be wrapped as
	 * an {@link IllegalArgumentException}
	 * 
	 * @param value
	 * @return URL encoded conversion of <code>value</code> See
	 *         {@link URLEncoder#encode(String, java.nio.charset.Charset)}
	 * @throws IllegalArgumentException wrapping eventual
	 *                                  {@link UnsupportedEncodingException} thrown
	 *                                  by
	 *                                  {@link URLEncoder#encode(String, java.nio.charset.Charset)}
	 */
	public static String urlEncode(String value) {
		try {
			if (value == null) {
				return null;
			}
			return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("Error trying to encode url param:[" + value + "]", e);
		}
	}
}
