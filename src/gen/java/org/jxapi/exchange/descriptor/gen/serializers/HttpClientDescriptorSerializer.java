package org.jxapi.exchange.descriptor.gen.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import javax.annotation.processing.Generated;
import org.jxapi.exchange.descriptor.gen.HttpClientDescriptor;
import org.jxapi.netutils.serialization.json.AbstractJsonValueSerializer;
import static org.jxapi.util.JsonUtil.writeLongField;
import static org.jxapi.util.JsonUtil.writeStringField;

/**
 * Jackson JSON Serializer for org.jxapi.exchange.descriptor.gen.HttpClientDescriptor
 * @see HttpClientDescriptor
 */
@Generated("org.jxapi.generator.java.pojo.JsonPojoSerializerGenerator")
public class HttpClientDescriptorSerializer extends AbstractJsonValueSerializer<HttpClientDescriptor> {
  
  private static final long serialVersionUID = 6465082548405787209L;
  
  /**
   * Constructor
   */
  public HttpClientDescriptorSerializer() {
    super(HttpClientDescriptor.class);
  }
  
  
  @Override
  public void serialize(HttpClientDescriptor value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    writeStringField(gen, "name", value.getName());
    writeStringField(gen, "httpRequestExecutorFactory", value.getHttpRequestExecutorFactory());
    writeStringField(gen, "httpRequestInterceptorFactory", value.getHttpRequestInterceptorFactory());
    writeLongField(gen, "httpRequestTimeout", value.getHttpRequestTimeout());
    gen.writeEndObject();
  }
}
