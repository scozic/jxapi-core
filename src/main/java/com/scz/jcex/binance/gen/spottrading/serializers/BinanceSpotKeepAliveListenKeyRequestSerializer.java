package com.scz.jcex.binance.gen.spottrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.binance.gen.spottrading.pojo.BinanceSpotKeepAliveListenKeyRequest;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.binance.gen.spottrading.pojo.BinanceSpotKeepAliveListenKeyRequest
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see BinanceSpotKeepAliveListenKeyRequest
 */
public class BinanceSpotKeepAliveListenKeyRequestSerializer extends StdSerializer<BinanceSpotKeepAliveListenKeyRequest> {
  public BinanceSpotKeepAliveListenKeyRequestSerializer() {
    super(BinanceSpotKeepAliveListenKeyRequest.class);
  }
  
  @Override
  public void serialize(BinanceSpotKeepAliveListenKeyRequest value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    gen.writeStringField("listenKey", String.valueOf(value.getListenKey()));
    gen.writeEndObject();
  }
}
