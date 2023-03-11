package com.scz.jcex.binance.gen.spottrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.binance.gen.spottrading.pojo.BinanceOutboundAccountPositionUserDataStreamMessageB;
import com.scz.jcex.util.EncodingUtil;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.binance.gen.spottrading.pojo.BinanceOutboundAccountPositionUserDataStreamMessageB
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see BinanceOutboundAccountPositionUserDataStreamMessageB
 */
public class BinanceOutboundAccountPositionUserDataStreamMessageBSerializer extends StdSerializer<BinanceOutboundAccountPositionUserDataStreamMessageB> {
  public BinanceOutboundAccountPositionUserDataStreamMessageBSerializer() {
    super(BinanceOutboundAccountPositionUserDataStreamMessageB.class);
  }
  
  @Override
  public void serialize(BinanceOutboundAccountPositionUserDataStreamMessageB value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    gen.writeStringField("a", String.valueOf(value.getA()));
    gen.writeStringField("f", EncodingUtil.bigDecimalToString(value.getF()));
    gen.writeStringField("l", EncodingUtil.bigDecimalToString(value.getL()));
    gen.writeEndObject();
  }
}
