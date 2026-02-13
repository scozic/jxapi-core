package org.jxapi.netutils.rest;

/**
 * <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods">HTTP methods</a> that can be used in HTTP requests.
 */
public enum HttpMethod {

  /**
   * <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods/GET"><strong>GET</strong> HTTP method</a>
   */
  GET(false, true),
  
  /**
   * <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods/POST"><strong>POST</strong> HTTP method</a>
   */
  POST(true, true),
  
  /**
   * <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods/PUT"><strong>PUT</strong> HTTP method</a>
   */
  PUT(true, false),
  
  /**
   * <a href=
   * "https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods/DELETE"><strong>DELETE</strong>
   * HTTP method</a> Remark: although the HTTP specification does not forbid a
   * body in DELETE requests, it is not common practice to send one. By default,
   * we consider that DELETE requests do not carry a body. Wrappers  can override 
   * this behavior if needed, using {@link HttpRequestInterceptor}.
   */
  DELETE(false, false),
  
  /**
   * <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods/HEAD"><strong>HEAD</strong> HTTP method</a>
   */
  HEAD(false, false),
  
  /**
   * <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods/CONNECT"><strong>CONNECT</strong> HTTP method</a>
   */
  CONNECT(true, true),
  
  /**
   * <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods/OPTIONS"><strong>OPTIONS</strong> HTTP method</a>
   */
  OPTIONS(false, true),
  
  /**
   * <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods/TRACE"><strong>TRACE</strong> HTTP method</a>
   */
  TRACE(false, false),
  
  /**
   * <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods/TRACE"><strong>TRACE</strong> HTTP method</a>
   */
  PATCH(true, false);
  
  /**
   * Flag set to true if request using this method are expected to carry a body. 
   */
  public final boolean requestHasBody;
  
  /**
   * Flag set to true if a successful response to a request using this method is expected to carry a body.
   */
  public final boolean responseHasBody;
  
  private HttpMethod(boolean requestHasBody, boolean responseHasBody) {
    this.requestHasBody = requestHasBody;
    this.responseHasBody = responseHasBody;
  }
}
