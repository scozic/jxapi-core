package org.jxapi.exchange.descriptor.gen.deserializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import javax.annotation.processing.Generated;
import org.jxapi.exchange.descriptor.gen.ExchangeApiDescriptor;
import org.jxapi.exchange.descriptor.gen.RestEndpointDescriptor;
import org.jxapi.exchange.descriptor.gen.WebsocketEndpointDescriptor;
import org.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import org.jxapi.netutils.deserialization.json.field.ListJsonFieldDeserializer;
import static org.jxapi.util.JsonUtil.readNextString;
import static org.jxapi.util.JsonUtil.skipNextValue;

/**
 * Parses incoming JSON messages into org.jxapi.exchange.descriptor.gen.ExchangeApiDescriptor instances
 * @see org.jxapi.exchange.descriptor.gen.ExchangeApiDescriptor
 */
@Generated("org.jxapi.generator.java.pojo.JsonPojoDeserializerGenerator")
public class ExchangeApiDescriptorDeserializer extends AbstractJsonMessageDeserializer<ExchangeApiDescriptor> {
  private ListJsonFieldDeserializer<RestEndpointDescriptor> restEndpointsDeserializer;
  private ListJsonFieldDeserializer<WebsocketEndpointDescriptor> websocketEndpointsDeserializer;
  
  @Override
  public ExchangeApiDescriptor deserialize(JsonParser parser) throws IOException {
    ExchangeApiDescriptor msg = new ExchangeApiDescriptor();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.currentName()) {
      case "name":
        msg.setName(readNextString(parser));
      break;
      case "description":
        msg.setDescription(readNextString(parser));
      break;
      case "httpUrl":
        msg.setHttpUrl(readNextString(parser));
      break;
      case "defaultHttpClient":
        msg.setDefaultHttpClient(readNextString(parser));
      break;
      case "defaultWebsocketClient":
        msg.setDefaultWebsocketClient(readNextString(parser));
      break;
      case "restEndpoints":
        parser.nextToken();
        if(restEndpointsDeserializer == null) {
          restEndpointsDeserializer = new ListJsonFieldDeserializer<>(new RestEndpointDescriptorDeserializer());
        }
        msg.setRestEndpoints(restEndpointsDeserializer.deserialize(parser));
      break;
      case "websocketEndpoints":
        parser.nextToken();
        if(websocketEndpointsDeserializer == null) {
          websocketEndpointsDeserializer = new ListJsonFieldDeserializer<>(new WebsocketEndpointDescriptorDeserializer());
        }
        msg.setWebsocketEndpoints(websocketEndpointsDeserializer.deserialize(parser));
      break;
      default:
        skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
