package com.scz.jcex.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.text.StringSubstitutor;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
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
	
	public static BigDecimal readNextBigDecimal(JsonParser parser) throws IOException {
		parser.nextToken();
		switch (parser.currentToken()) {
		case VALUE_NUMBER_FLOAT:
			return parser.getDecimalValue();
		case VALUE_STRING:
			return toBigDecimal(parser.getText());
		case VALUE_NULL:
			return null;
		case VALUE_NUMBER_INT:
			return new BigDecimal(parser.getLongValue());
		default:
			throw new JsonParseException(parser, "Expected floating point value, but got " + parser.currentToken());
		}
	}
	
	public static Integer readNextInteger(JsonParser parser) throws IOException {
		parser.nextToken();
		switch (parser.currentToken()) {
		case VALUE_STRING:
			return Integer.valueOf(parser.getText());
		case VALUE_NULL:
			return null;
		case VALUE_NUMBER_INT:
			return Integer.valueOf(parser.getIntValue());
		default:
			throw new JsonParseException(parser, "Expected integer value, but got " + parser.currentToken());
		}
	}
	
	public static Long readNextLong(JsonParser parser) throws IOException {
		parser.nextToken();
		switch (parser.currentToken()) {
		case VALUE_STRING:
			return Long.valueOf(parser.getText());
		case VALUE_NULL:
			return null;
		case VALUE_NUMBER_INT:
			return Long.valueOf(parser.getLongValue());
		default:
			throw new JsonParseException(parser, "Expected long value, but got " + parser.currentToken());
		}
	}
	
	public static BigDecimal toBigDecimal(String s) {
		if (s == null || s.isEmpty())
			return null;
		return new BigDecimal(s);
	}
	
	public static List<String> splitJsonArrayStr(String jsonArrayStr) {
		if (jsonArrayStr == null || jsonArrayStr.isEmpty()) {
			return List.of();
		}
		if (!jsonArrayStr.startsWith("[")) {
			return List.of(jsonArrayStr);
		}
		List<String> res = new ArrayList<>();
		int depth = 0;
		int off = 1;
		boolean inStr = false;
		StringBuilder sb = new StringBuilder();
		for (int i = 1; i < jsonArrayStr.length(); i++) {
			char c = jsonArrayStr.charAt(i);
			if (c == '"' && !(inStr && jsonArrayStr.charAt(i - 1) == '\\')) {
				inStr = !inStr;
			} else if (!inStr) {
				if (depth == 0 && ( c == ']' || c == ',')) {
					res.add(jsonArrayStr.substring(off, i));
					off = i + 1;
					sb.delete(0, sb.length());
					continue;
				} else if (c == '{' || c == '[') {
					depth++;
				} else if (c == '}' || c == ']') {
					depth--;
				}
			}
			sb.append(c);
		}
		
		return res;
	}
}
