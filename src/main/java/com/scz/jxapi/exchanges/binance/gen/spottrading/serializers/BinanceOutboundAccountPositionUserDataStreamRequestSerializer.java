package com.scz.jxapi.exchanges.binance.gen.spottrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jxapi.exchanges.binance.gen.spottrading.pojo.BinanceOutboundAccountPositionUserDataStreamRequest;

import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.exchanges.binance.gen.spottrading.pojo.BinanceOutboundAccountPositionUserDataStreamRequest
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see BinanceOutboundAccountPositionUserDataStreamRequest
 */
public class BinanceOutboundAccountPositionUserDataStreamRequestSerializer extends StdSerializer<BinanceOutboundAccountPositionUserDataStreamRequest> {
  public BinanceOutboundAccountPositionUserDataStreamRequestSerializer() {
    super(BinanceOutboundAccountPositionUserDataStreamRequest.class);
  }
  
  @Override
  public void serialize(BinanceOutboundAccountPositionUserDataStreamRequest value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    gen.writeEndObject();
  }
}
