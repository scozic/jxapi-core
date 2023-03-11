package com.scz.jcex.exchanges.binance.gen.spottrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.exchanges.binance.gen.spottrading.pojo.BinanceSpotDeleteListenKeyRequest;

import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.binance.gen.spottrading.pojo.BinanceSpotDeleteListenKeyRequest
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see BinanceSpotDeleteListenKeyRequest
 */
public class BinanceSpotDeleteListenKeyRequestSerializer extends StdSerializer<BinanceSpotDeleteListenKeyRequest> {
  public BinanceSpotDeleteListenKeyRequestSerializer() {
    super(BinanceSpotDeleteListenKeyRequest.class);
  }
  
  @Override
  public void serialize(BinanceSpotDeleteListenKeyRequest value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    gen.writeStringField("listenKey", String.valueOf(value.getListenKey()));
    gen.writeEndObject();
  }
}
