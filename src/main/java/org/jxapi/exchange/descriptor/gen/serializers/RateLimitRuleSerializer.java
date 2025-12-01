package org.jxapi.exchange.descriptor.gen.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import javax.annotation.processing.Generated;
import org.jxapi.exchange.descriptor.gen.RateLimitRule;

/**
 * Jackson JSON Serializer for org.jxapi.exchange.descriptor.gen.RateLimitRule
 * @see RateLimitRule
 */
@Generated("org.jxapi.generator.java.pojo.JsonPojoSerializerGenerator")
public class RateLimitRuleSerializer extends StdSerializer<RateLimitRule> {
  /**
   * Constructor
   */
  public RateLimitRuleSerializer() {
    super(RateLimitRule.class);
  }
  
  @Override
  public void serialize(RateLimitRule value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getId() != null){
      gen.writeStringField("id", String.valueOf(value.getId()));
    }
    if (value.getTimeFrame() != null){
      gen.writeNumberField("timeFrame", value.getTimeFrame());
    }
    if (value.getMaxTotalWeight() != null){
      gen.writeNumberField("maxTotalWeight", value.getMaxTotalWeight());
    }
    if (value.getMaxRequestCount() != null){
      gen.writeNumberField("maxRequestCount", value.getMaxRequestCount());
    }
    if (value.getGranularity() != null){
      gen.writeNumberField("granularity", value.getGranularity());
    }
    gen.writeEndObject();
  }
}
