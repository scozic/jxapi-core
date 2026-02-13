package org.jxapi.netutils.rest;

/**
 * Serializer for HTTP request URL parameters.
 *
 * @param <R> the type of the request
 */
public interface HttpRequestUrlParamsSerializer<R> {

  /**
   * Serializes the URL parameters of the given request into a string containing
   * the query part of a URL (path parameters + query parameters).
   *
   * @param request the request whose URL parameters are to be serialized
   * @param baseUrl the base URL to which the serialized parameters will be appended
   * @return the serialized query string
   */
  String serializeUrlParams(R request, String baseUrl);
  
  static <R> HttpRequestUrlParamsSerializer<R> noParams() {
    return (request, url) -> url;
  }
}
