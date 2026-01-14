package org.jxapi.netutils.rest;

import java.io.IOException;

import org.jxapi.util.JsonUtil;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * * Jackson JSON Serializer for {@link RestResponse} that serializes the
 * response as a string representation of the HTTP response, including the
 * response code, providing fields in most relevant order for reading and
 * debugging. Long strings are pretty-printed to ensure readability.
 */
@SuppressWarnings("rawtypes")
public class RestResponseToStringJsonSerializer extends StdSerializer<RestResponse> {

  private static final long serialVersionUID = 6975581442680256049L;

  /**
   * Default constructor
   */
  public RestResponseToStringJsonSerializer() {
    super(RestResponse.class);
  }

  @Override
  public void serialize(RestResponse response, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    JsonUtil.writeObjectField(gen, "httpResponse", response.getHttpResponse());
    JsonUtil.writeBooleanField(gen, "paginated", response.isPaginated());
    gen.writeEndObject();
  }

}
