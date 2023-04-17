package com.scz.jcex.exchanges.binance.gen.spottrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.exchanges.binance.gen.spottrading.pojo.BinanceSpotDeleteListenKeyResponse;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.exchanges.binance.gen.spottrading.pojo.BinanceSpotDeleteListenKeyResponse
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see BinanceSpotDeleteListenKeyResponse
 */
public class BinanceSpotDeleteListenKeyResponseSerializer extends StdSerializer<BinanceSpotDeleteListenKeyResponse> {
  public BinanceSpotDeleteListenKeyResponseSerializer() {
    super(BinanceSpotDeleteListenKeyResponse.class);
  }
  
  @Override
  public void serialize(BinanceSpotDeleteListenKeyResponse value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    gen.writeEndObject();
  }
}
