package org.jxapi.exchange.descriptor.gen.deserializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import javax.annotation.processing.Generated;
import org.jxapi.exchange.descriptor.gen.HttpClientDescriptor;
import org.jxapi.exchange.descriptor.gen.NetworkDescriptor;
import org.jxapi.exchange.descriptor.gen.WebsocketClientDescriptor;
import org.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import org.jxapi.netutils.deserialization.json.field.ListJsonFieldDeserializer;
import static org.jxapi.util.JsonUtil.skipNextValue;

/**
 * Parses incoming JSON messages into org.jxapi.exchange.descriptor.gen.NetworkDescriptor instances
 * @see org.jxapi.exchange.descriptor.gen.NetworkDescriptor
 */
@Generated("org.jxapi.generator.java.pojo.JsonPojoDeserializerGenerator")
public class NetworkDescriptorDeserializer extends AbstractJsonMessageDeserializer<NetworkDescriptor> {
  private ListJsonFieldDeserializer<HttpClientDescriptor> httpClientsDeserializer;
  private ListJsonFieldDeserializer<WebsocketClientDescriptor> websocketClientsDeserializer;
  
  @Override
  public NetworkDescriptor deserialize(JsonParser parser) throws IOException {
    NetworkDescriptor msg = new NetworkDescriptor();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.currentName()) {
      case "httpClients":
        parser.nextToken();
        if(httpClientsDeserializer == null) {
          httpClientsDeserializer = new ListJsonFieldDeserializer<>(new HttpClientDescriptorDeserializer());
        }
        msg.setHttpClients(httpClientsDeserializer.deserialize(parser));
      break;
      case "websocketClients":
        parser.nextToken();
        if(websocketClientsDeserializer == null) {
          websocketClientsDeserializer = new ListJsonFieldDeserializer<>(new WebsocketClientDescriptorDeserializer());
        }
        msg.setWebsocketClients(websocketClientsDeserializer.deserialize(parser));
      break;
      default:
        skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
