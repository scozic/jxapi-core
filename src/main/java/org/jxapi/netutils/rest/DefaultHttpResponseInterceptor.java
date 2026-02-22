package org.jxapi.netutils.rest;

/**
 * Default implementation of {@link HttpResponseInterceptor} that deserializes
 * response body if a message deserializer is provided in the response. <br>
 * This means that if a response has a message deserializer must be set when and
 * only when the response body should be deserialized from JSON as response body.
 */
public class DefaultHttpResponseInterceptor implements HttpResponseInterceptor {

  @Override
  public void intercept(HttpResponse response) {
    HttpRequestUtil.deserializeResponseBody(response);
  }

}
