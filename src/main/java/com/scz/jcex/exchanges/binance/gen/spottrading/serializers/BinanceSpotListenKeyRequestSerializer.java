package com.scz.jcex.exchanges.binance.gen.spottrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.exchanges.binance.gen.spottrading.pojo.BinanceSpotListenKeyRequest;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.exchanges.binance.gen.spottrading.pojo.BinanceSpotListenKeyRequest
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see BinanceSpotListenKeyRequest
 */
public class BinanceSpotListenKeyRequestSerializer extends StdSerializer<BinanceSpotListenKeyRequest> {
  public BinanceSpotListenKeyRequestSerializer() {
    super(BinanceSpotListenKeyRequest.class);
  }
  
  @Override
  public void serialize(BinanceSpotListenKeyRequest value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    gen.writeEndObject();
  }
}
