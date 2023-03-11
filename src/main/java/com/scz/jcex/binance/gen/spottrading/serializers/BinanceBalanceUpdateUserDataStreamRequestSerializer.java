package com.scz.jcex.binance.gen.spottrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.binance.gen.spottrading.pojo.BinanceBalanceUpdateUserDataStreamRequest;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.binance.gen.spottrading.pojo.BinanceBalanceUpdateUserDataStreamRequest
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see BinanceBalanceUpdateUserDataStreamRequest
 */
public class BinanceBalanceUpdateUserDataStreamRequestSerializer extends StdSerializer<BinanceBalanceUpdateUserDataStreamRequest> {
  public BinanceBalanceUpdateUserDataStreamRequestSerializer() {
    super(BinanceBalanceUpdateUserDataStreamRequest.class);
  }
  
  @Override
  public void serialize(BinanceBalanceUpdateUserDataStreamRequest value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    gen.writeEndObject();
  }
}
