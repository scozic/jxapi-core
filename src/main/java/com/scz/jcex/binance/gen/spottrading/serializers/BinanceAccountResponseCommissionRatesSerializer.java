package com.scz.jcex.binance.gen.spottrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.binance.gen.spottrading.pojo.BinanceAccountResponseCommissionRates;
import com.scz.jcex.util.EncodingUtil;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.binance.gen.spottrading.pojo.BinanceAccountResponseCommissionRates
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see BinanceAccountResponseCommissionRates
 */
public class BinanceAccountResponseCommissionRatesSerializer extends StdSerializer<BinanceAccountResponseCommissionRates> {
  public BinanceAccountResponseCommissionRatesSerializer() {
    super(BinanceAccountResponseCommissionRates.class);
  }
  
  @Override
  public void serialize(BinanceAccountResponseCommissionRates value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    gen.writeStringField("maker", EncodingUtil.bigDecimalToString(value.getMaker()));
    gen.writeStringField("taker", EncodingUtil.bigDecimalToString(value.getTaker()));
    gen.writeStringField("buyer", EncodingUtil.bigDecimalToString(value.getBuyer()));
    gen.writeStringField("seller", EncodingUtil.bigDecimalToString(value.getSeller()));
    gen.writeEndObject();
  }
}
