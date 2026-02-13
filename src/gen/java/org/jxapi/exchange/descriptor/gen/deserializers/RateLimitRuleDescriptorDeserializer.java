package org.jxapi.exchange.descriptor.gen.deserializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import javax.annotation.processing.Generated;
import org.jxapi.exchange.descriptor.gen.RateLimitRuleDescriptor;
import org.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import static org.jxapi.util.JsonUtil.readNextInteger;
import static org.jxapi.util.JsonUtil.readNextLong;
import static org.jxapi.util.JsonUtil.readNextString;
import static org.jxapi.util.JsonUtil.skipNextValue;

/**
 * Parses incoming JSON messages into org.jxapi.exchange.descriptor.gen.RateLimitRuleDescriptor instances
 * @see org.jxapi.exchange.descriptor.gen.RateLimitRuleDescriptor
 */
@Generated("org.jxapi.generator.java.pojo.JsonPojoDeserializerGenerator")
public class RateLimitRuleDescriptorDeserializer extends AbstractJsonMessageDeserializer<RateLimitRuleDescriptor> {
  
  @Override
  public RateLimitRuleDescriptor deserialize(JsonParser parser) throws IOException {
    RateLimitRuleDescriptor msg = new RateLimitRuleDescriptor();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.currentName()) {
      case "id":
        msg.setId(readNextString(parser));
      break;
      case "timeFrame":
        msg.setTimeFrame(readNextLong(parser));
      break;
      case "maxTotalWeight":
        msg.setMaxTotalWeight(readNextInteger(parser));
      break;
      case "maxRequestCount":
        msg.setMaxRequestCount(readNextInteger(parser));
      break;
      case "granularity":
        msg.setGranularity(readNextInteger(parser));
      break;
      default:
        skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
