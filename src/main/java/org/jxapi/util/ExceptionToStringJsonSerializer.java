package org.jxapi.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * Custom serializer for {@link Exception} objects, that serializes them as a
 * readable string.
 * <p>
 * This serializer is registered in the default {@link ObjectMapper} returned by
 * {@link JsonUtil#createDefaultObjectMapper()}.
 */
public class ExceptionToStringJsonSerializer extends StdSerializer<Exception> {

  /**
   * Creates a new {@link ExceptionToStringJsonSerializer} instance.
   */
  public ExceptionToStringJsonSerializer() {
    super(Exception.class);
  }

  @Override
  public void serialize(Exception value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeString(String.valueOf(value));
  }
}