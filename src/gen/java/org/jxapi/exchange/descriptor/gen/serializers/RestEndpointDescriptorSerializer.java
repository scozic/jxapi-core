package org.jxapi.exchange.descriptor.gen.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import javax.annotation.processing.Generated;
import org.jxapi.exchange.descriptor.gen.RestEndpointDescriptor;
import org.jxapi.netutils.serialization.json.AbstractJsonValueSerializer;
import org.jxapi.netutils.serialization.json.ListJsonValueSerializer;
import org.jxapi.netutils.serialization.json.StringJsonValueSerializer;
import static org.jxapi.util.JsonUtil.writeBooleanField;
import static org.jxapi.util.JsonUtil.writeCustomSerializerField;
import static org.jxapi.util.JsonUtil.writeIntField;
import static org.jxapi.util.JsonUtil.writeObjectField;
import static org.jxapi.util.JsonUtil.writeStringField;

/**
 * Jackson JSON Serializer for org.jxapi.exchange.descriptor.gen.RestEndpointDescriptor
 * @see RestEndpointDescriptor
 */
@Generated("org.jxapi.generator.java.pojo.JsonPojoSerializerGenerator")
public class RestEndpointDescriptorSerializer extends AbstractJsonValueSerializer<RestEndpointDescriptor> {
  
  private static final long serialVersionUID = 695972162215070755L;
  
  /**
   * Constructor
   */
  public RestEndpointDescriptorSerializer() {
    super(RestEndpointDescriptor.class);
  }
  
  private final ListJsonValueSerializer<String> rateLimitsSerializer = new ListJsonValueSerializer<>(StringJsonValueSerializer.getInstance());
  
  @Override
  public void serialize(RestEndpointDescriptor value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    writeStringField(gen, "name", value.getName());
    writeStringField(gen, "description", value.getDescription());
    writeStringField(gen, "httpMethod", value.getHttpMethod());
    writeStringField(gen, "url", value.getUrl());
    writeStringField(gen, "docUrl", value.getDocUrl());
    writeStringField(gen, "httpClient", value.getHttpClient());
    writeIntField(gen, "requestWeight", value.getRequestWeight());
    writeCustomSerializerField(gen, "rateLimits", value.getRateLimits(), rateLimitsSerializer, provider);
    writeBooleanField(gen, "paginated", value.isPaginated());
    writeBooleanField(gen, "requestHasBody", value.isRequestHasBody());
    writeObjectField(gen, "request", value.getRequest());
    writeObjectField(gen, "response", value.getResponse());
    gen.writeEndObject();
  }
}
