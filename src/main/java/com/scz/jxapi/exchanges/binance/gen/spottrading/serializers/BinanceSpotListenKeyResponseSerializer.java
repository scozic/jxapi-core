package com.scz.jxapi.exchanges.binance.gen.spottrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jxapi.exchanges.binance.gen.spottrading.pojo.BinanceSpotListenKeyResponse;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jxapi.exchanges.binance.gen.spottrading.pojo.BinanceSpotListenKeyResponse
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see BinanceSpotListenKeyResponse
 */
public class BinanceSpotListenKeyResponseSerializer extends StdSerializer<BinanceSpotListenKeyResponse> {
  public BinanceSpotListenKeyResponseSerializer() {
    super(BinanceSpotListenKeyResponse.class);
  }
  
  @Override
  public void serialize(BinanceSpotListenKeyResponse value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getListenKey() != null){
      gen.writeStringField("listenKey", String.valueOf(value.getListenKey()));
    }
    gen.writeEndObject();
  }
}
