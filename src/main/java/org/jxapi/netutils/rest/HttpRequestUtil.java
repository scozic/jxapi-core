package org.jxapi.netutils.rest;

import java.util.Collections;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jxapi.util.CollectionUtil;

/**
 * Helper methods around {@link HttpRequest} processing (for instance in {@link HttpRequestInterceptor} implementations.
 */
public class HttpRequestUtil {

  private HttpRequestUtil() {}
  
  /**
   * Extract query parameters from full URL, that is the substring after first occurrence of '?' character in URL.
   * For instance:
   * <ul>
   * <li><code>http://example.com</code> &rarr; <code>null</code></li>
   * <li><code>http://example.com?</code> &rarr; <code>""</code></li>
   * <li><code>http://example.com?name=foo&amp;age=12</code> &rarr; <code>"name=foo&amp;age=12"</code></li>
   * </ul>
   * @param url URL to extract query parameters from
   * @return Query parameters of <code>url</code>, or <code>null</code> if '?' is not found in <code>url</code>
   */
  public static String getUrlQueryParams(String url) {
    if (url == null || url.isEmpty()) {
      return null; // Return null if URL is null or empty
    }
    int off = url.indexOf('?');
    if (off < 0) {
      return null;
    }
    if (off >= url.length() - 1) {
      return "";
    }
    return url.substring(off + 1);
  }
  
  /**
   * Parse query parameters from URL and return them as a map.
   * <p>
   * For instance:
   * <ul>
   * <li><code>http://example.com</code> &rarr; <code>{}</code></li>
   * <li><code>http://example.com?</code> &rarr; <code>{}</code></li>
   * <li><code>http://example.com?name=foo&amp;age=12</code> &rarr;
   * <code>{"name" : "foo", "age" : "12"}</code></li>
   * </ul>
   * 
   * @param url URL to extract query parameters from
   * @return Map of query parameters, or empty map if no query parameters found.
   *         If query parameters are malformed, an exception is thrown. If
   *         parameter is empty, it is skipped. If it is not empty but has no
   *         value (i.e. no '=' character), it will be considered as a key with an
   *         empty value.
   * @throws IllegalArgumentException If a query parameter is not in
   *                                  <code>key=value</code> format.
   */
  public static Map<String, String> parseUrlQueryParams(String url) {
    String queryParams = getUrlQueryParams(url);
    if (queryParams == null || queryParams.isEmpty()) {
      return Map.of();
    }
    
    Map<String, String> result = CollectionUtil.createMap();
    String[] params = StringUtils.split(queryParams, '&');
    for (String param : params) {
      if (StringUtils.isBlank(param)) {
        continue;
      }
      String[] kv = StringUtils.split(param, '=');
      if (kv.length == 1) {
        result.put(kv[0], "");
      } else if (kv.length == 2) {
        result.put(kv[0], kv[1]);
      } else {
        throw new IllegalArgumentException(
            "Invalid query parameter: " + param + " in URL: " + url);
      }
      
    }
    return Collections.unmodifiableMap(result);
  }
}
