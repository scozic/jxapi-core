package com.scz.jcex.binance.gen.spotmarketdata.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationAllResponse;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationAllResponse
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see BinanceExchangeInformationAllResponse
 */
public class BinanceExchangeInformationAllResponseSerializer extends StdSerializer<BinanceExchangeInformationAllResponse> {
  public BinanceExchangeInformationAllResponseSerializer() {
    super(BinanceExchangeInformationAllResponse.class);
  }
  
  @Override
  public void serialize(BinanceExchangeInformationAllResponse value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    gen.writeStringField("timezone", String.valueOf(value.getTimezone()));
    gen.writeNumberField("serverTime", value.getServerTime());
    gen.writeObjectField("symbols", value.getSymbols());
    gen.writeEndObject();
  }
}
