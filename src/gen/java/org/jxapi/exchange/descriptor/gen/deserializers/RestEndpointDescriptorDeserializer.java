package org.jxapi.exchange.descriptor.gen.deserializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import javax.annotation.processing.Generated;
import org.jxapi.exchange.descriptor.gen.RestEndpointDescriptor;
import org.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import org.jxapi.netutils.deserialization.json.field.ListJsonFieldDeserializer;
import org.jxapi.netutils.deserialization.json.field.StringJsonFieldDeserializer;
import static org.jxapi.util.JsonUtil.readNextBoolean;
import static org.jxapi.util.JsonUtil.readNextInteger;
import static org.jxapi.util.JsonUtil.readNextObject;
import static org.jxapi.util.JsonUtil.readNextString;
import static org.jxapi.util.JsonUtil.skipNextValue;

/**
 * Parses incoming JSON messages into org.jxapi.exchange.descriptor.gen.RestEndpointDescriptor instances
 * @see org.jxapi.exchange.descriptor.gen.RestEndpointDescriptor
 */
@Generated("org.jxapi.generator.java.pojo.JsonPojoDeserializerGenerator")
public class RestEndpointDescriptorDeserializer extends AbstractJsonMessageDeserializer<RestEndpointDescriptor> {
  private final ListJsonFieldDeserializer<String> rateLimitsDeserializer = new ListJsonFieldDeserializer<>(StringJsonFieldDeserializer.getInstance());
  
  @Override
  public RestEndpointDescriptor deserialize(JsonParser parser) throws IOException {
    RestEndpointDescriptor msg = new RestEndpointDescriptor();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.currentName()) {
      case "name":
        msg.setName(readNextString(parser));
      break;
      case "description":
        msg.setDescription(readNextString(parser));
      break;
      case "httpMethod":
        msg.setHttpMethod(readNextString(parser));
      break;
      case "url":
        msg.setUrl(readNextString(parser));
      break;
      case "docUrl":
        msg.setDocUrl(readNextString(parser));
      break;
      case "httpClient":
        msg.setHttpClient(readNextString(parser));
      break;
      case "requestWeight":
        msg.setRequestWeight(readNextInteger(parser));
      break;
      case "rateLimits":
        parser.nextToken();
        msg.setRateLimits(rateLimitsDeserializer.deserialize(parser));
      break;
      case "paginated":
        msg.setPaginated(readNextBoolean(parser));
      break;
      case "requestHasBody":
        msg.setRequestHasBody(readNextBoolean(parser));
      break;
      case "request":
        msg.setRequest(readNextObject(parser, org.jxapi.pojo.descriptor.Field.class));
      break;
      case "response":
        msg.setResponse(readNextObject(parser, org.jxapi.pojo.descriptor.Field.class));
      break;
      default:
        skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
