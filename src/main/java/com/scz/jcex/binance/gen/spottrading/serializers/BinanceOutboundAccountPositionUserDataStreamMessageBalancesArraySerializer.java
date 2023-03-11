package com.scz.jcex.binance.gen.spottrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.binance.gen.spottrading.pojo.BinanceOutboundAccountPositionUserDataStreamMessageBalancesArray;
import com.scz.jcex.util.EncodingUtil;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.binance.gen.spottrading.pojo.BinanceOutboundAccountPositionUserDataStreamMessageBalancesArray
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see BinanceOutboundAccountPositionUserDataStreamMessageBalancesArray
 */
public class BinanceOutboundAccountPositionUserDataStreamMessageBalancesArraySerializer extends StdSerializer<BinanceOutboundAccountPositionUserDataStreamMessageBalancesArray> {
  public BinanceOutboundAccountPositionUserDataStreamMessageBalancesArraySerializer() {
    super(BinanceOutboundAccountPositionUserDataStreamMessageBalancesArray.class);
  }
  
  @Override
  public void serialize(BinanceOutboundAccountPositionUserDataStreamMessageBalancesArray value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    gen.writeStringField("a", String.valueOf(value.getAsset()));
    gen.writeStringField("f", EncodingUtil.bigDecimalToString(value.getFree()));
    gen.writeStringField("l", EncodingUtil.bigDecimalToString(value.getLocked()));
    gen.writeEndObject();
  }
}
