package com.scz.jcex.exchanges.binance.gen.spotmarketdata.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.exchanges.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationRequest;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.exchanges.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationRequest
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see BinanceExchangeInformationRequest
 */
public class BinanceExchangeInformationRequestSerializer extends StdSerializer<BinanceExchangeInformationRequest> {
  public BinanceExchangeInformationRequestSerializer() {
    super(BinanceExchangeInformationRequest.class);
  }
  
  @Override
  public void serialize(BinanceExchangeInformationRequest value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getSymbols() != null){
      gen.writeObjectField("symbols", value.getSymbols());
    }
    gen.writeEndObject();
  }
}
