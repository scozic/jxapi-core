package com.scz.jcex.binance.gen.spotmarketdata.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.binance.gen.spotmarketdata.pojo.BinanceAllMarketTickersStreamRequest;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.binance.gen.spotmarketdata.pojo.BinanceAllMarketTickersStreamRequest
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see BinanceAllMarketTickersStreamRequest
 */
public class BinanceAllMarketTickersStreamRequestSerializer extends StdSerializer<BinanceAllMarketTickersStreamRequest> {
  public BinanceAllMarketTickersStreamRequestSerializer() {
    super(BinanceAllMarketTickersStreamRequest.class);
  }
  
  @Override
  public void serialize(BinanceAllMarketTickersStreamRequest value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    gen.writeEndObject();
  }
  
}
