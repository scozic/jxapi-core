package org.jxapi.exchange.descriptor.gen.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import javax.annotation.processing.Generated;
import org.jxapi.exchange.descriptor.gen.RateLimitRuleDescriptor;
import org.jxapi.netutils.serialization.json.AbstractJsonValueSerializer;
import static org.jxapi.util.JsonUtil.writeIntField;
import static org.jxapi.util.JsonUtil.writeLongField;
import static org.jxapi.util.JsonUtil.writeStringField;

/**
 * Jackson JSON Serializer for org.jxapi.exchange.descriptor.gen.RateLimitRuleDescriptor
 * @see RateLimitRuleDescriptor
 */
@Generated("org.jxapi.generator.java.pojo.JsonPojoSerializerGenerator")
public class RateLimitRuleDescriptorSerializer extends AbstractJsonValueSerializer<RateLimitRuleDescriptor> {
  
  private static final long serialVersionUID = -6068811743425871939L;
  
  /**
   * Constructor
   */
  public RateLimitRuleDescriptorSerializer() {
    super(RateLimitRuleDescriptor.class);
  }
  
  
  @Override
  public void serialize(RateLimitRuleDescriptor value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    writeStringField(gen, "id", value.getId());
    writeLongField(gen, "timeFrame", value.getTimeFrame());
    writeIntField(gen, "maxTotalWeight", value.getMaxTotalWeight());
    writeIntField(gen, "maxRequestCount", value.getMaxRequestCount());
    writeIntField(gen, "granularity", value.getGranularity());
    gen.writeEndObject();
  }
}
