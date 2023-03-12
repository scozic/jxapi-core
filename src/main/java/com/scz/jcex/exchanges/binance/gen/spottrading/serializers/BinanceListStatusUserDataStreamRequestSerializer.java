package com.scz.jcex.exchanges.binance.gen.spottrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.exchanges.binance.gen.spottrading.pojo.BinanceListStatusUserDataStreamRequest;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.exchanges.binance.gen.spottrading.pojo.BinanceListStatusUserDataStreamRequest
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see BinanceListStatusUserDataStreamRequest
 */
public class BinanceListStatusUserDataStreamRequestSerializer extends StdSerializer<BinanceListStatusUserDataStreamRequest> {
  public BinanceListStatusUserDataStreamRequestSerializer() {
    super(BinanceListStatusUserDataStreamRequest.class);
  }
  
  @Override
  public void serialize(BinanceListStatusUserDataStreamRequest value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    gen.writeEndObject();
  }
}
