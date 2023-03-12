package com.scz.jcex.exchanges.binance.gen.spottrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.exchanges.binance.gen.spottrading.pojo.BinanceOutboundAccountPositionUserDataStreamMessage;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.exchanges.binance.gen.spottrading.pojo.BinanceOutboundAccountPositionUserDataStreamMessage
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see BinanceOutboundAccountPositionUserDataStreamMessage
 */
public class BinanceOutboundAccountPositionUserDataStreamMessageSerializer extends StdSerializer<BinanceOutboundAccountPositionUserDataStreamMessage> {
  public BinanceOutboundAccountPositionUserDataStreamMessageSerializer() {
    super(BinanceOutboundAccountPositionUserDataStreamMessage.class);
  }
  
  @Override
  public void serialize(BinanceOutboundAccountPositionUserDataStreamMessage value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    gen.writeStringField("e", String.valueOf(value.getEventType()));
    gen.writeNumberField("E", value.getEventTime());
    gen.writeNumberField("u", value.getLastAccountUpdateTime());
    gen.writeObjectField("B", value.getBalancesArray());
    gen.writeEndObject();
  }
}
