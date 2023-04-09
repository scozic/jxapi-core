package com.scz.jcex.exchanges.binance.gen.spottrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.exchanges.binance.gen.spottrading.pojo.BinanceOutboundAccountPositionUserDataStreamMessageBalancesArray;
import com.scz.jcex.util.EncodingUtil;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.exchanges.binance.gen.spottrading.pojo.BinanceOutboundAccountPositionUserDataStreamMessageBalancesArray
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
    if (value.getAsset() != null){
      gen.writeStringField("a", String.valueOf(value.getAsset()));
    }
    if (value.getFree() != null){
      gen.writeStringField("f", EncodingUtil.bigDecimalToString(value.getFree()));
    }
    if (value.getLocked() != null){
      gen.writeStringField("l", EncodingUtil.bigDecimalToString(value.getLocked()));
    }
    gen.writeEndObject();
  }
}
