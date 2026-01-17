package org.jxapi.exchange;

import java.io.IOException;

import org.jxapi.util.EncodingUtil;
import org.jxapi.util.JsonUtil;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * Specific Serializer for {@link ExchangeEvent} for serialization to JSON from toString() method.
 * 
 */
public class ExchangeApiEventToStringJsonSerializer extends StdSerializer<ExchangeEvent> {

  private static final long serialVersionUID = -2846854545550509909L;

  /**
   * Constructor that initializes the serializer for {@link ExchangeEvent}.
   */
  public ExchangeApiEventToStringJsonSerializer() {
    super(ExchangeEvent.class);
  }

  @Override
  public void serialize(ExchangeEvent value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    JsonUtil.writeStringField(gen, "type", value.getType().name());
    JsonUtil.writeStringField(gen, "exchangeName", value.getExchangeName());
    JsonUtil.writeStringField(gen, "exchangId", value.getExchangeId());
    JsonUtil.writeStringField(gen, "exchangeName", value.getExchangeName());
    JsonUtil.writeStringField(gen, "exchangApiName", value.getExchangeApiName());
    JsonUtil.writeStringField(gen, "endpoint", value.getEndpoint());
    JsonUtil.writeObjectField(gen, "httpRequest", value.getHttpRequest());
    JsonUtil.writeObjectField(gen, "httpResponse", value.getHttpResponse());
    JsonUtil.writeObjectField(gen, "websocketSubscribeRequest", value.getWebsocketSubscribeRequest());
    JsonUtil.writeObjectField(gen, "websocketMessage", EncodingUtil.prettyPrintLongString(value.getWebsocketMessage()));
    Exception websocketError = value.getWebsocketError();
    JsonUtil.writeStringField(gen, "websocketError", websocketError == null? null:  websocketError.toString());
    JsonUtil.writeStringField(gen, "websocketSubscriptionId", value.getWebsocketSubscriptionId());
    gen.writeEndObject();
    
  }

}
