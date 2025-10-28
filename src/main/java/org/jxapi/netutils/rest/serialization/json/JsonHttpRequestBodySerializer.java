package org.jxapi.netutils.rest.serialization.json;

import org.jxapi.netutils.rest.HttpRequest;
import org.jxapi.netutils.rest.serialization.HttpRequestBodySerializer;
import org.jxapi.util.JsonUtil;

/**
 * Default {@link HttpRequestBodySerializer} implementation that serializes the body as
 * a JSON string.
 */
public class JsonHttpRequestBodySerializer implements HttpRequestBodySerializer{
  
  private static final JsonHttpRequestBodySerializer INSTANCE = new JsonHttpRequestBodySerializer();
  
  public static JsonHttpRequestBodySerializer getInstance() {
    return INSTANCE;
  }
  
  private JsonHttpRequestBodySerializer() {
  }

  @Override
  public void serializeBody(HttpRequest request) {
    request.setBody(JsonUtil.pojoToJsonString(request.getRequest()));
  }

}
