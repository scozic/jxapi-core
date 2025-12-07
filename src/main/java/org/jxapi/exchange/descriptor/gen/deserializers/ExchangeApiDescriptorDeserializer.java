package org.jxapi.exchange.descriptor.gen.deserializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import javax.annotation.processing.Generated;
import org.jxapi.exchange.descriptor.gen.ExchangeApiDescriptor;
import org.jxapi.exchange.descriptor.gen.RateLimitRuleDescriptor;
import org.jxapi.exchange.descriptor.gen.RestEndpointDescriptor;
import org.jxapi.exchange.descriptor.gen.WebsocketEndpointDescriptor;
import org.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import org.jxapi.netutils.deserialization.json.field.ListJsonFieldDeserializer;
import static org.jxapi.util.JsonUtil.readNextLong;
import static org.jxapi.util.JsonUtil.readNextString;
import static org.jxapi.util.JsonUtil.skipNextValue;

/**
 * Parses incoming JSON messages into org.jxapi.exchange.descriptor.gen.ExchangeApiDescriptor instances
 * @see org.jxapi.exchange.descriptor.gen.ExchangeApiDescriptor
 */
@Generated("org.jxapi.generator.java.pojo.JsonMessageDeserializerGenerator")
public class ExchangeApiDescriptorDeserializer extends AbstractJsonMessageDeserializer<ExchangeApiDescriptor> {
  private ListJsonFieldDeserializer<RateLimitRuleDescriptor> rateLimitsDeserializer;
  private ListJsonFieldDeserializer<RestEndpointDescriptor> restEndpointsDeserializer;
  private ListJsonFieldDeserializer<WebsocketEndpointDescriptor> websocketEndpointsDeserializer;
  
  @Override
  public ExchangeApiDescriptor deserialize(JsonParser parser) throws IOException {
    ExchangeApiDescriptor msg = new ExchangeApiDescriptor();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      case "name":
        msg.setName(readNextString(parser));
      break;
      case "description":
        msg.setDescription(readNextString(parser));
      break;
      case "httpRequestExecutorFactory":
        msg.setHttpRequestExecutorFactory(readNextString(parser));
      break;
      case "httpRequestInterceptorFactory":
        msg.setHttpRequestInterceptorFactory(readNextString(parser));
      break;
      case "httpUrl":
        msg.setHttpUrl(readNextString(parser));
      break;
      case "httpRequestTimeout":
        msg.setHttpRequestTimeout(readNextLong(parser));
      break;
      case "websocketFactory":
        msg.setWebsocketFactory(readNextString(parser));
      break;
      case "websocketHookFactory":
        msg.setWebsocketHookFactory(readNextString(parser));
      break;
      case "websocketUrl":
        msg.setWebsocketUrl(readNextString(parser));
      break;
      case "rateLimits":
        parser.nextToken();
        if(rateLimitsDeserializer == null) {
          rateLimitsDeserializer = new ListJsonFieldDeserializer<>(new RateLimitRuleDescriptorDeserializer());
        }
        msg.setRateLimits(rateLimitsDeserializer.deserialize(parser));
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
