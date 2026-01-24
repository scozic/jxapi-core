package org.jxapi.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.text.StringSubstitutor;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * Helper methods around String encoding.
 */
public class EncodingUtil {
  
  /**
   * Default maximum length for a long string to be pretty printed.
   * <p>
   * Used by {@link #prettyPrintLongString(String)}.
   */
  public static final int DEFAULT_PRETTY_PRINT_LONG_STRING_MAX_LENGTH = 200;
  
  /**
   * Default date format used for timestamp formatting, in ISO 8601 format:
   * <code>yyyy-MM-dd'T'HH:mm:ss.SSSZ</code>.
   * <p>
   * Used by {@link #formatTimestamp(long)} and {@link #formatTimestamp(Date)}.
   */
  public static final String DATE_FORMAT_ISO_8601 = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
  
  private static final ObjectMapper DEFAULT_TOSTRING_OBJECT_MAPPER = createDefaultPojoToToStringObjectMapper();
  
  /**
   * Creates a default {@link ObjectMapper} for POJO to string conversion for display in logs, with
   * the following settings:
   * <ul>
   * <li>SerializationFeature.FAIL_ON_EMPTY_BEANS disabled</li>
   * <li>SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS enabled</li>
   * <li>Include.NON_NULL inclusion for serialization</li>
   * </ul>
   * Additional serializers can be provided as var args.
   * These are tyupically custom serializers for specific POJOs that need special handling.
   * 
   * @param additionalSerializers additional serializers to register in the object
   *                              mapper
   * @return the created object mapper
   */
  public static ObjectMapper createDefaultPojoToToStringObjectMapper(JsonSerializer<?>... additionalSerializers) {

    SimpleModule serializerModule = new SimpleModule();
    Arrays.stream(additionalSerializers).forEach(serializerModule::addSerializer);

    return JsonMapper.builder()
     .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
     .enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS)
     .defaultPropertyInclusion(
         JsonInclude.Value.construct(Include.NON_NULL, Include.NON_NULL) 
       )
     .addModule(serializerModule)
     .build();
}

  
  /**
   * Separator used to reduce a long string to a maximum length by keeping the
   * first and last characters and replacing the middle by this separator.
   * 
   * @see #prettyPrintLongString(String, int)
   */
  public static final String PRETTY_PRINT_LONG_STRING_REDUCE_SEPARATOR = "....";
  
  private static final DateTimeFormatter DEFAULT_TIMESTAMP_FORMAT = DateTimeFormatter
      .ofPattern(DATE_FORMAT_ISO_8601)
      .withZone(ZoneId.systemDefault());

  private EncodingUtil() {
  }

  /**
   * Formats a timestamp string using the default format
   * "yyyy-MM-dd'T'HH:mm:ss.SSSZ" (ISO 8601 format).
   * 
   * @param timestamp the timestamp to format, in milliseconds since epoch
   * @return formatted timestamp string
   */
  public static String formatTimestamp(long timestamp) {
    return formatTimestamp(new Date(timestamp));
  }
  
  /**
   * Formats a {@link Date} to a timestamp string using the default format
   * "yyyy-MM-dd'T'HH:mm:ss.SSSZ" (ISO 8601 format).
   * 
   * @param date the date to format
   * @return formatted timestamp string
   */
  public static String formatTimestamp(Date date) {
    if (date == null) {
      return null;
    }
    return DEFAULT_TIMESTAMP_FORMAT.format(date.toInstant());
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
    return substituteArguments(template, CollectionUtil.createMap(keysAndValues));
  }
  
  /**
   * Substitutes properties from a map using provided key values pairs.<br>
   * Example:
   * 
   * <pre>
   * substituteArguments("Hello ${stranger}, I am ${me}", Map.of("stranger", "Bob", "me", "Roger"))
   * </pre>
   * 
   * returns:
   * 
   * <pre>
   * "Hello Bob, I am Roger"
   * </pre>
   * 
   * @param template     the template String to perform that contains variables
   * @param replacements map of key, value pairs for variable substitutions
   * @return Template with properties substituted
   */
  public static String substituteArguments(String template, Map<String, Object> replacements) {
    StringSubstitutor sub = new StringSubstitutor(replacements);
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
    for (int i = 0; i < keysAndValues.length; i += 2) {
      Object key = keysAndValues[i];
      if (key == null) {
        throw new IllegalArgumentException("null key in for parameter #" + (i / 2) + " in:" + Arrays.toString(keysAndValues));
      }
      Object value = keysAndValues[i + 1];
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
   * Creates a URL path from a series of values. Each value is URL-encoded and
   * prefixed with a forward slash.
   *
   * @param values The values to be converted to path parameters.
   * @return The resulting URL path string.
   */
  public static String createUrlPathParameters(Object... values) {
    StringBuilder s = new StringBuilder();
    for (Object value: values) {
      if (value == null) {
        continue;
      }
      s.append("/")
       .append(urlEncode(String.valueOf(value)));
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
    return pojo.getClass().getSimpleName() +  JsonUtil.pojoToJsonString(pojo, DEFAULT_TOSTRING_OBJECT_MAPPER); 
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
  
  /**
   * Shortens a long string to a maximum length like using
   * {@link #prettyPrintLongString(String, int)} with
   * {@link #DEFAULT_PRETTY_PRINT_LONG_STRING_MAX_LENGTH} as maxLength.
   * 
   * @param longString the string to shorten
   * @return the shortened string, or the original string if it is already shorter
   *         than {@link #DEFAULT_PRETTY_PRINT_LONG_STRING_MAX_LENGTH}, or
   *         <code>null</code> if the input string is <code>null</code>.
   * @see #prettyPrintLongString(String, int)        
   */
  public static String prettyPrintLongString(String longString) {
    return prettyPrintLongString(longString, DEFAULT_PRETTY_PRINT_LONG_STRING_MAX_LENGTH);
  }

  /**
   * Checks if a given URL is absolute or relative, e.g. if it starts with a scheme like "http://".
   * 
   * @param url the URL to check
   * @return <code>true</code> if the URL is absolute, <code>false</code> if it is
   *         relative
   */
  public static boolean isAbsoluteUrl(String url) {
    return URI.create(url).getScheme() != null;
  }
  
  /**
   * Removes a prefix from a string if it starts with that prefix.
   * 
   * @param s      the string to modify
   * @param prefix the prefix to remove
   * @return the modified string without the prefix, or <code>null</code> if the
   *         string does not start with the prefix
   */
  public static String removePrefix(String s, String prefix) {
    if (prefix == null) {
      throw new IllegalArgumentException("prefix cannot be null");
    }
    s = Optional.ofNullable(s).orElse("");
    if (s.startsWith(prefix)) {
      return s.substring(prefix.length());
    }
    return null;
  }
  
  /**
   * Builds a URL from the given parts. This method contatenates the parts, unless
   * a part is an absolute URL, in which case it is considered as a base URL, and
   * the result will be the concatenation of that base URL and the remaining
   * parts.
   * <p>
   * Examples:
   * <ul>
   * <li>buildUrl("http://example.com", "path", "to", "resource") returns
   * "http://example.com/path/to/resource"</li>
   * <li>buildUrl("http://example.com", "path", "to", "resource",
   * "http://another.com") returns "http://another.com"</li>
   * </ul>
   * Rmark: Consistency of returned URL is not guaranteed, as the method does not
   * check for duplicate slashes or other URL formatting issues. Returned URL may
   * not be valid. If both parts are empty or <code>null</code>, an empty string
   * is returned.
   * 
   * @param urlParts the parts of the URL to concatenate
   * @return the concatenated URL
   */
  public static String buildUrl(String... urlParts) {
    if (urlParts.length == 0) {
      return "";
    }
    if (urlParts.length == 1) {
      return Optional.ofNullable(urlParts[0]).orElse("");
    }
    StringBuilder sb = new StringBuilder();
    for (String part: urlParts) {
      if (part == null) {
        continue;
      }
      if (isAbsoluteUrl(part)) {
        sb.setLength(0);
        sb.append(part);
      } else {
        sb.append(part);
      }
    }
    return sb.toString();
  }
}
