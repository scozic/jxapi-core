package org.jxapi.exchange.descriptor.gen.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import javax.annotation.processing.Generated;
import org.jxapi.exchange.descriptor.gen.HttpClientDescriptor;
import org.jxapi.exchange.descriptor.gen.NetworkDescriptor;
import org.jxapi.exchange.descriptor.gen.WebsocketClientDescriptor;
import org.jxapi.netutils.serialization.json.AbstractJsonValueSerializer;
import org.jxapi.netutils.serialization.json.ListJsonValueSerializer;
import static org.jxapi.util.JsonUtil.writeCustomSerializerField;

/**
 * Jackson JSON Serializer for org.jxapi.exchange.descriptor.gen.NetworkDescriptor
 * @see NetworkDescriptor
 */
@Generated("org.jxapi.generator.java.pojo.JsonPojoSerializerGenerator")
public class NetworkDescriptorSerializer extends AbstractJsonValueSerializer<NetworkDescriptor> {
  
  private static final long serialVersionUID = -709715477753710339L;
  
  /**
   * Constructor
   */
  public NetworkDescriptorSerializer() {
    super(NetworkDescriptor.class);
  }
  
  private ListJsonValueSerializer<HttpClientDescriptor> httpClientsSerializer;
  private ListJsonValueSerializer<WebsocketClientDescriptor> websocketClientsSerializer;
  
  @Override
  public void serialize(NetworkDescriptor value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if(httpClientsSerializer == null) {
      httpClientsSerializer = new ListJsonValueSerializer<>(new HttpClientDescriptorSerializer());
    }
    writeCustomSerializerField(gen, "httpClients", value.getHttpClients(), httpClientsSerializer, provider);
    if(websocketClientsSerializer == null) {
      websocketClientsSerializer = new ListJsonValueSerializer<>(new WebsocketClientDescriptorSerializer());
    }
    writeCustomSerializerField(gen, "websocketClients", value.getWebsocketClients(), websocketClientsSerializer, provider);
    gen.writeEndObject();
  }
}
