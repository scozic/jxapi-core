package org.jxapi.exchange.descriptor.gen.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import javax.annotation.processing.Generated;
import org.jxapi.exchange.descriptor.gen.RestEndpointDescriptor;

/**
 * Jackson JSON Serializer for org.jxapi.exchange.descriptor.gen.RestEndpointDescriptor
 * @see RestEndpointDescriptor
 */
@Generated("org.jxapi.generator.java.pojo.JsonPojoSerializerGenerator")
public class RestEndpointDescriptorSerializer extends StdSerializer<RestEndpointDescriptor> {
  /**
   * Constructor
   */
  public RestEndpointDescriptorSerializer() {
    super(RestEndpointDescriptor.class);
  }
  
  @Override
  public void serialize(RestEndpointDescriptor value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getName() != null){
      gen.writeStringField("name", String.valueOf(value.getName()));
    }
    if (value.getDescription() != null){
      gen.writeStringField("description", String.valueOf(value.getDescription()));
    }
    if (value.getHttpMethod() != null){
      gen.writeStringField("httpMethod", String.valueOf(value.getHttpMethod()));
    }
    if (value.getUrl() != null){
      gen.writeStringField("url", String.valueOf(value.getUrl()));
    }
    if (value.getDocUrl() != null){
      gen.writeStringField("docUrl", String.valueOf(value.getDocUrl()));
    }
    if (value.getHttpRequestTimeout() != null){
      gen.writeNumberField("httpRequestTimeout", value.getHttpRequestTimeout());
    }
    if (value.getRequestWeight() != null){
      gen.writeNumberField("requestWeight", value.getRequestWeight());
    }
    if (value.getRateLimits() != null){
      gen.writeObjectField("rateLimits", value.getRateLimits());
    }
    if (value.isPaginated() != null){
      gen.writeBooleanField("paginated", value.isPaginated());
    }
    if (value.isRequestHasBody() != null){
      gen.writeBooleanField("requestHasBody", value.isRequestHasBody());
    }
    if (value.getRequest() != null){
      gen.writeObjectField("request", value.getRequest());
    }
    if (value.getResponse() != null){
      gen.writeObjectField("response", value.getResponse());
    }
    gen.writeEndObject();
  }
}
