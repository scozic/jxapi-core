package org.jxapi.netutils.rest;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.jxapi.netutils.rest.ratelimits.RateLimitRule;
import org.jxapi.util.CollectionUtil;
import org.jxapi.util.EncodingUtil;
import org.jxapi.util.JsonUtil;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * Json serializer for {@link HttpRequest} that serializes the request as a readable string with properties in JSON format ordered by importance.
 * <p>
 * The body and request properties are serialized as pretty-printed strings (limited length) to enhance readability.
 */
public class HttpRequestToStringJsonSerializer extends StdSerializer<HttpRequest> {

  private static final long serialVersionUID = -2440320091598738159L;

  /**
   * Default constructor
   */
  public HttpRequestToStringJsonSerializer() {
    super(HttpRequest.class);
  }

  @Override
  public void serialize(HttpRequest httpRequest, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    JsonUtil.writeStringField(gen, "endpoint", httpRequest.getEndpoint());
    HttpMethod httpMethod = httpRequest.getHttpMethod();
    JsonUtil.writeStringField(gen, "httpMethod", httpMethod == null ? null : httpMethod.name());
    JsonUtil.writeStringField(gen, "url", httpRequest.getUrl());
    Object request = httpRequest.getRequest();
    if (request != null) {
      JsonUtil.writeStringField(gen, "request", EncodingUtil.prettyPrintLongString(JsonUtil.pojoToJsonString(request)));
    }
    String body = httpRequest.getBody();
    if (body != null) {
      gen.writeStringField("body", "length=" + body.length());
    }
    List<RateLimitRule> rateLimits = httpRequest.getRateLimits();
    if(!CollectionUtil.isEmpty(rateLimits)) {
      gen.writeObjectField("rateLimits", rateLimits);
    }
    int weight = httpRequest.getWeight();
    if (weight > 0) {
      gen.writeNumberField("weight", weight);
    }
    Date time = httpRequest.getTime();
    if (time != null) {
      JsonUtil.writeStringField(gen, "time", EncodingUtil.formatTimestamp(time));
    }
    long throttledTime = httpRequest.getThrottledTime();
    if (throttledTime > 0) {
      gen.writeNumberField("throttledTime", throttledTime);
    }
    gen.writeEndObject();
  }
  
  

}
