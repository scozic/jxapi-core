package org.jxapi.exchange.descriptor.gen.deserializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import javax.annotation.processing.Generated;
import org.jxapi.exchange.descriptor.gen.ConfigPropertyDescriptor;
import org.jxapi.exchange.descriptor.gen.ConstantDescriptor;
import org.jxapi.exchange.descriptor.gen.ExchangeApiDescriptor;
import org.jxapi.exchange.descriptor.gen.ExchangeDescriptor;
import org.jxapi.exchange.descriptor.gen.RateLimitRuleDescriptor;
import org.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import org.jxapi.netutils.deserialization.json.field.ListJsonFieldDeserializer;
import static org.jxapi.util.JsonUtil.readNextLong;
import static org.jxapi.util.JsonUtil.readNextString;
import static org.jxapi.util.JsonUtil.skipNextValue;

/**
 * Parses incoming JSON messages into org.jxapi.exchange.descriptor.gen.ExchangeDescriptor instances
 * @see org.jxapi.exchange.descriptor.gen.ExchangeDescriptor
 */
@Generated("org.jxapi.generator.java.pojo.JsonMessageDeserializerGenerator")
public class ExchangeDescriptorDeserializer extends AbstractJsonMessageDeserializer<ExchangeDescriptor> {
  private ListJsonFieldDeserializer<ConfigPropertyDescriptor> propertiesDeserializer;
  private ListJsonFieldDeserializer<ConstantDescriptor> constantsDeserializer;
  private ListJsonFieldDeserializer<ExchangeApiDescriptor> apisDeserializer;
  private ListJsonFieldDeserializer<RateLimitRuleDescriptor> rateLimitsDeserializer;
  
  @Override
  public ExchangeDescriptor deserialize(JsonParser parser) throws IOException {
    ExchangeDescriptor msg = new ExchangeDescriptor();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      case "id":
        msg.setId(readNextString(parser));
      break;
      case "jxapi":
        msg.setJxapi(readNextString(parser));
      break;
      case "version":
        msg.setVersion(readNextString(parser));
      break;
      case "description":
        msg.setDescription(readNextString(parser));
      break;
      case "docUrl":
        msg.setDocUrl(readNextString(parser));
      break;
      case "basePackage":
        msg.setBasePackage(readNextString(parser));
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
      case "websocketUrl":
        msg.setWebsocketUrl(readNextString(parser));
      break;
      case "websocketFactory":
        msg.setWebsocketFactory(readNextString(parser));
      break;
      case "websocketHookFactory":
        msg.setWebsocketHookFactory(readNextString(parser));
      break;
      case "httpRequestTimeout":
        msg.setHttpRequestTimeout(readNextLong(parser));
      break;
      case "afterInitHookFactory":
        msg.setAfterInitHookFactory(readNextString(parser));
      break;
      case "properties":
        parser.nextToken();
        if(propertiesDeserializer == null) {
          propertiesDeserializer = new ListJsonFieldDeserializer<>(new ConfigPropertyDescriptorDeserializer());
        }
        msg.setProperties(propertiesDeserializer.deserialize(parser));
      break;
      case "constants":
        parser.nextToken();
        if(constantsDeserializer == null) {
          constantsDeserializer = new ListJsonFieldDeserializer<>(new ConstantDescriptorDeserializer());
        }
        msg.setConstants(constantsDeserializer.deserialize(parser));
      break;
      case "rateLimits":
        parser.nextToken();
        if(rateLimitsDeserializer == null) {
          rateLimitsDeserializer = new ListJsonFieldDeserializer<>(new RateLimitRuleDescriptorDeserializer());
        }
        msg.setRateLimits(rateLimitsDeserializer.deserialize(parser));
      break;
      case "apis":
        parser.nextToken();
        if(apisDeserializer == null) {
          apisDeserializer = new ListJsonFieldDeserializer<>(new ExchangeApiDescriptorDeserializer());
        }
        msg.setApis(apisDeserializer.deserialize(parser));
      break;
      default:
        skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
