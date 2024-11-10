package com.scz.jxapi.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URI;
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
	
	/**
	 * Separator used to reduce a long string to a maximum length by keeping the
	 * first and last characters and replacing the middle by this separator.
	 * 
	 * @see #prettyPrintLongString(String, int)
	 */
	public static final String PRETTY_PRINT_LONG_STRING_REDUCE_SEPARATOR = "....";

	private EncodingUtil() {
	}

	/**
	 * Substitures properties from a map using provided key values pairs.<br>
	 * Example:
	 * 
	 * <pre>
	 * substituteArguments("Hello ${stranger}, I am ${me}", "stranger", "Bob", "me", "Roger")
	 * </pre>
	 * 
	 * returns:
	 * 
	 * <pre>
	 * "Hello Bob, I am Roger"
	 * </pre>
	 * 
	 * @param template      the template String to perform that contains variables
	 * @param keysAndValues flat list of key, value pair for variable substitutions
	 * @return Template with properties substituted
	 */
	public static String substituteArguments(String template, Object... keysAndValues) {
		if (keysAndValues.length == 0) {
			return template;
		}
		Map<String, Object> m = new HashMap<>(keysAndValues.length / 2);
		for (int i = 0; i < keysAndValues.length; i++) {
			m.put(String.valueOf(keysAndValues[i++]), keysAndValues[i]);
		}
		StringSubstitutor sub = new StringSubstitutor(m);
		return sub.replace(template);
	}

	/**
	 * Creates a query param for given list of keys and values. Keys are expected to
	 * be plain string that do no contain forbidden URL characters. Values can be of
	 * any type, will be converted to string, that string will be URL encoded (see
	 * {@link URLEncoder}).
	 * 
	 * @param keysAndValues List of keys and values, for
	 *                      instance:<code>"id","myId","name","John Doe","age",28,"height":1.70</code>
	 * @return Query parameters string to append as URL suffix, for instance:
	 *         <code>"?id=myId&amp;name=John%20Doe&amp;age=28&amp;height=1.170"</code>
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
	 * @param items     list of items to concatenate
	 * @param separator separator to use between items
	 * @return concatenated items of list using separator between items, for
	 *         instance "item1;item2;item3"
	 */
	public static String listToString(List<String> items, String separator) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < items.size(); i++) {
			sb.append(items.get(i));
			if (i < items.size() - 1) {
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
	 * @param bd {@link BigDecimal} value to encode as string.
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
	 * @param value The string to encode using URL encoding
	 * @return URL encoded conversion of <code>value</code> See
	 *         {@link URLEncoder#encode(String, java.nio.charset.Charset)}
	 * @throws IllegalArgumentException If (unlikely) {@link StandardCharsets#UTF_8} is not supported.
	 */
	public static String urlEncode(String value) {
		try {
			if (value == null) {
				return null;
			}
			return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException e) {
			// Should not be thrown since UTF_8 should be supported
			throw new IllegalArgumentException("Error trying to encode url param:[" + value + "]", e);
		}
	}
	
	/**
	 * Shortens a long string to a maximum length by keeping the first and last
	 * characters and replacing the middle by a separator "...." (see
	 * {@link #PRETTY_PRINT_LONG_STRING_REDUCE_SEPARATOR}).
	 * <br>
	 * If the string is already
	 * shorter than the maximum length, it is returned as is.
	 * <br>
	 * If the maximum length is too small to keep the separator, an
	 * {@link IllegalArgumentException} is thrown.
	 * 
	 * @param longString the string to shorten
	 * @param maxLength  the maximum length of the returned string, should be &gt; 4
	 * @return the shortened string, or the original string if it is already
	 *         shorter, or <code>null</code> if the input string is
	 *         <code>null</code>.
	 * @throws IllegalArgumentException if maxLength is too small
	 */
	public static String prettyPrintLongString(String longString, int maxLength) {
		if (longString == null) {
			return null;
		}
		if (longString.length() <= maxLength) {
			return longString;
		}
		String sep = PRETTY_PRINT_LONG_STRING_REDUCE_SEPARATOR;
		if (maxLength < sep.length()) {
			throw new IllegalArgumentException("maxLength should be > " + sep.length());
		}
		int l = (maxLength - sep.length()) / 2;
		return longString.substring(0, l) + sep + longString.substring(longString.length() - l);
	}
	
	public static boolean isAbsoluteUrl(String url) {
		return URI.create(url).getScheme() != null;
	}
}
