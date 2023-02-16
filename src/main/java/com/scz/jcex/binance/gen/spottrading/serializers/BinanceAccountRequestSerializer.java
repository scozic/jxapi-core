package com.scz.jcex.binance.gen.spottrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.binance.gen.spottrading.pojo.BinanceAccountRequest;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.binance.gen.spottrading.pojo.BinanceAccountRequest
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see BinanceAccountRequest
 */
public class BinanceAccountRequestSerializer extends StdSerializer<BinanceAccountRequest> {
  public BinanceAccountRequestSerializer() {
    super(BinanceAccountRequest.class);
  }
  
  @Override
  public void serialize(BinanceAccountRequest value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    gen.writeNumberField("recvWindow", value.getRecvWindow());
    gen.writeNumberField("timestamp", value.getTimestamp());
    gen.writeEndObject();
  }
  
}
