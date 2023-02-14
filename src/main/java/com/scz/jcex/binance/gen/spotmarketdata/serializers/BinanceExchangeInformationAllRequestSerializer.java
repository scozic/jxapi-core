package com.scz.jcex.binance.gen.spotmarketdata.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationAllRequest;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationAllRequest
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see BinanceExchangeInformationAllRequest
 */
public class BinanceExchangeInformationAllRequestSerializer extends StdSerializer<BinanceExchangeInformationAllRequest> {
  public BinanceExchangeInformationAllRequestSerializer() {
    super(BinanceExchangeInformationAllRequest.class);
  }
  
  @Override
  public void serialize(BinanceExchangeInformationAllRequest value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    gen.writeEndObject();
  }
  
}
