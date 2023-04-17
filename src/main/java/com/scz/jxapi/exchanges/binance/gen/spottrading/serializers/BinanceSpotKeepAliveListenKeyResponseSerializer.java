package com.scz.jxapi.exchanges.binance.gen.spottrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jxapi.exchanges.binance.gen.spottrading.pojo.BinanceSpotKeepAliveListenKeyResponse;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jxapi.exchanges.binance.gen.spottrading.pojo.BinanceSpotKeepAliveListenKeyResponse
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see BinanceSpotKeepAliveListenKeyResponse
 */
public class BinanceSpotKeepAliveListenKeyResponseSerializer extends StdSerializer<BinanceSpotKeepAliveListenKeyResponse> {
  public BinanceSpotKeepAliveListenKeyResponseSerializer() {
    super(BinanceSpotKeepAliveListenKeyResponse.class);
  }
  
  @Override
  public void serialize(BinanceSpotKeepAliveListenKeyResponse value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    gen.writeEndObject();
  }
}
