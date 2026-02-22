package org.jxapi.netutils.rest;

/**
 * Default implementation of {@link HttpRequestInterceptor} that sets content
 * type header to JSON and serializes request body if a message serializer is
 * provided in the request. <br>
 * This means that if a request has a message serializer must be set when and
 * only when the request body should be serialized to JSON as request body.
 */
public class DefaultHttpRequestInterceptor implements HttpRequestInterceptor {

  @Override
  public void intercept(HttpRequest request) {
    if (request.getRequestSerializer() != null) {
      HttpRequestUtil.setContentTypeHeaderToJson(request);
      HttpRequestUtil.serializeRequestBody(request);
    }
  }

  
}
