package org.jxapi.exchange.descriptor.gen.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import javax.annotation.processing.Generated;
import org.jxapi.exchange.descriptor.gen.ConfigPropertyDescriptor;
import org.jxapi.exchange.descriptor.gen.ConstantDescriptor;
import org.jxapi.exchange.descriptor.gen.ExchangeApiDescriptor;
import org.jxapi.exchange.descriptor.gen.ExchangeDescriptor;
import org.jxapi.exchange.descriptor.gen.RateLimitRuleDescriptor;
import org.jxapi.netutils.serialization.json.AbstractJsonValueSerializer;
import org.jxapi.netutils.serialization.json.ListJsonValueSerializer;
import static org.jxapi.util.JsonUtil.writeCustomSerializerField;
import static org.jxapi.util.JsonUtil.writeStringField;

/**
 * Jackson JSON Serializer for org.jxapi.exchange.descriptor.gen.ExchangeDescriptor
 * @see ExchangeDescriptor
 */
@Generated("org.jxapi.generator.java.pojo.JsonPojoSerializerGenerator")
public class ExchangeDescriptorSerializer extends AbstractJsonValueSerializer<ExchangeDescriptor> {
  
  private static final long serialVersionUID = -1095294357014360112L;
  
  /**
   * Constructor
   */
  public ExchangeDescriptorSerializer() {
    super(ExchangeDescriptor.class);
  }
  
  private ListJsonValueSerializer<ConfigPropertyDescriptor> propertiesSerializer;
  private ListJsonValueSerializer<ConstantDescriptor> constantsSerializer;
  private ListJsonValueSerializer<ExchangeApiDescriptor> apisSerializer;
  private ListJsonValueSerializer<RateLimitRuleDescriptor> rateLimitsSerializer;
  private NetworkDescriptorSerializer networkSerializer;
  
  @Override
  public void serialize(ExchangeDescriptor value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    writeStringField(gen, "id", value.getId());
    writeStringField(gen, "jxapi", value.getJxapi());
    writeStringField(gen, "version", value.getVersion());
    writeStringField(gen, "description", value.getDescription());
    writeStringField(gen, "docUrl", value.getDocUrl());
    writeStringField(gen, "basePackage", value.getBasePackage());
    writeStringField(gen, "httpUrl", value.getHttpUrl());
    writeStringField(gen, "afterInitHookFactory", value.getAfterInitHookFactory());
    if(propertiesSerializer == null) {
      propertiesSerializer = new ListJsonValueSerializer<>(new ConfigPropertyDescriptorSerializer());
    }
    writeCustomSerializerField(gen, "properties", value.getProperties(), propertiesSerializer, provider);
    if(constantsSerializer == null) {
      constantsSerializer = new ListJsonValueSerializer<>(new ConstantDescriptorSerializer());
    }
    writeCustomSerializerField(gen, "constants", value.getConstants(), constantsSerializer, provider);
    if(rateLimitsSerializer == null) {
      rateLimitsSerializer = new ListJsonValueSerializer<>(new RateLimitRuleDescriptorSerializer());
    }
    writeCustomSerializerField(gen, "rateLimits", value.getRateLimits(), rateLimitsSerializer, provider);
    if(networkSerializer == null) {
      networkSerializer = new NetworkDescriptorSerializer();
    }
    writeCustomSerializerField(gen, "network", value.getNetwork(), networkSerializer, provider);
    if(apisSerializer == null) {
      apisSerializer = new ListJsonValueSerializer<>(new ExchangeApiDescriptorSerializer());
    }
    writeCustomSerializerField(gen, "apis", value.getApis(), apisSerializer, provider);
    gen.writeEndObject();
  }
}
