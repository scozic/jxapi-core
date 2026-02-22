package org.jxapi.netutils.rest;

import java.util.Collections;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jxapi.netutils.serialization.MessageSerializer;
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
  
    /**
    * Set the "Content-Type" header of the request to "application/json".
    * @param request the request to set header for, should not be <code>null</code>.
    * @throws IllegalArgumentException if <code>request</code> is <code>null</code>.
    */
  public static void setContentTypeHeaderToJson(HttpRequest request) {
    setContentTypeHeader(request, "application/json");
  }
  
  /**
   * Set the "Content-Type" header of the request to the specified content type.
   * @param request the request to set header for, should not be <code>null</code>.
   * @param contentType the content type to set in header, should not be <code>null</code> or empty.
    * @throws IllegalArgumentException if <code>request</code> is <code>null</code>, or <code>contentType</code> is <code>null</code> or empty.
   */
  public static void setContentTypeHeader(HttpRequest request, String contentType) {
    if (request == null) {
      throw new IllegalArgumentException("HttpRequest cannot be null");
    }
    if (contentType == null || contentType.isEmpty()) {
      throw new IllegalArgumentException("Content-Type cannot be null or empty");
    }
    request.setHeader("Content-Type", contentType);
  }
  
  /**
   * Will set the body of the request by serializing the request object using the
   * message serializer if such serializer is set. If either message serializer or request object is
   * <code>null</code>, the body will not be modified.
   * 
   * @param request the request to serialize body of, should not be <code>null</code>.
   * @see #setRequestSerializer(MessageSerializer)
   * @see #setRequest(Object)
   */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public static void serializeRequestBody(HttpRequest request) {
    Object requestObject = request.getRequest();
    MessageSerializer serializer = request.getRequestSerializer();
    if (requestObject != null && serializer != null) {
      request.setBody(serializer.serialize(requestObject));
    }
  }
  
  /**
   * Will set the response of the response by deserializing the response body using the
   * message deserializer if such deserializer is set in the request that originated this response. If either message deserializer or response body is
   * <code>null</code> or empty, the response will not be modified.
   * 
   * @param response the response to deserialize body of, should not be <code>null</code>.
   * @see HttpResponse#getRequest()
   * @see HttpRequest#getResponseDeserializer()
   */
  public static void deserializeResponseBody(HttpResponse response) {
    HttpRequest request = response.getRequest();
    if (request != null 
        && request.getResponseDeserializer() != null 
        && !StringUtils.isEmpty(response.getBody())) {
      response.setResponse(request.getResponseDeserializer().deserialize(response.getBody()));
    }
  }
  
}
