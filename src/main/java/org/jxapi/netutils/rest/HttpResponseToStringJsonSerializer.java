package org.jxapi.netutils.rest;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.jxapi.util.CollectionUtil;
import org.jxapi.util.EncodingUtil;
import org.jxapi.util.JsonUtil;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * Jackson JSON Serializer for {@link HttpResponse} that serializes the response
 * as a string representation of the HTTP response, including the response code,
 * providing fields in most relevant order for reading and debugging.
 * Long strings are pretty-printed to ensure readability.
 */
public class HttpResponseToStringJsonSerializer extends StdSerializer<HttpResponse> {

  private static final long serialVersionUID = -5016692140979711182L;

  /**
   * Default constructor
   */
  public HttpResponseToStringJsonSerializer() {
    super(HttpResponse.class);
  }

  @Override
  public void serialize(HttpResponse httpResponse, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    gen.writeNumberField("responseCode", httpResponse.getResponseCode());
    JsonUtil.writeStringField(gen, "exception", httpResponse.getException() == null ? null : httpResponse.getException().toString());
    HttpRequest request = httpResponse.getRequest();
    if (request != null) {
      gen.writeObjectField("request", request);
    }
    String body = httpResponse.getBody();
    if (body != null) {
      gen.writeStringField("body", "length=" + body.length());
    }
    Map<String, List<String>> headers = httpResponse.getHeaders();
    if (!CollectionUtil.isEmptyMap(headers)) {
      gen.writeObjectField("headers", headers);
    }
    Date time = httpResponse.getTime();
    if (time != null) {
      JsonUtil.writeStringField(gen, "time", EncodingUtil.formatTimestamp(time));
    }
    gen.writeNumberField("roundTrip", httpResponse.getRoundTrip());
    gen.writeEndObject();
  }

}
