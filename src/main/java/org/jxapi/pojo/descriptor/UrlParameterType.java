package org.jxapi.pojo.descriptor;

/**
 * Enum representing types of URL parameters.
 * <p>
 * The request URL can contain two types of parameters:
 * <ul>
 * <li>PATH parameters: These are part of the URL path itself.</li>
 * <li>QUERY parameters: These are appended to the URL after a '?' character.</li>
 * </ul>
 */
public enum UrlParameterType {
  
  /**
   * Path parameter type, part of the URL path.
   */
  PATH, 
  
  /**
   * Query parameter type, appended to the URL after '?'.
   */
  QUERY
}
