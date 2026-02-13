package org.jxapi.exchange.descriptor.gen.deserializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import javax.annotation.processing.Generated;
import org.jxapi.exchange.descriptor.gen.HttpClientDescriptor;
import org.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import static org.jxapi.util.JsonUtil.readNextLong;
import static org.jxapi.util.JsonUtil.readNextString;
import static org.jxapi.util.JsonUtil.skipNextValue;

/**
 * Parses incoming JSON messages into org.jxapi.exchange.descriptor.gen.HttpClientDescriptor instances
 * @see org.jxapi.exchange.descriptor.gen.HttpClientDescriptor
 */
@Generated("org.jxapi.generator.java.pojo.JsonPojoDeserializerGenerator")
public class HttpClientDescriptorDeserializer extends AbstractJsonMessageDeserializer<HttpClientDescriptor> {
  
  @Override
  public HttpClientDescriptor deserialize(JsonParser parser) throws IOException {
    HttpClientDescriptor msg = new HttpClientDescriptor();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.currentName()) {
      case "name":
        msg.setName(readNextString(parser));
      break;
      case "httpRequestExecutorFactory":
        msg.setHttpRequestExecutorFactory(readNextString(parser));
      break;
      case "httpRequestInterceptorFactory":
        msg.setHttpRequestInterceptorFactory(readNextString(parser));
      break;
      case "httpRequestTimeout":
        msg.setHttpRequestTimeout(readNextLong(parser));
      break;
      default:
        skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
