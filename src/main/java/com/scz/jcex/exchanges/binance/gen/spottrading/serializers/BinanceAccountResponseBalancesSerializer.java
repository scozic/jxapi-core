package com.scz.jcex.exchanges.binance.gen.spottrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.exchanges.binance.gen.spottrading.pojo.BinanceAccountResponseBalances;
import com.scz.jcex.util.EncodingUtil;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.exchanges.binance.gen.spottrading.pojo.BinanceAccountResponseBalances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see BinanceAccountResponseBalances
 */
public class BinanceAccountResponseBalancesSerializer extends StdSerializer<BinanceAccountResponseBalances> {
  public BinanceAccountResponseBalancesSerializer() {
    super(BinanceAccountResponseBalances.class);
  }
  
  @Override
  public void serialize(BinanceAccountResponseBalances value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    gen.writeStringField("asset", String.valueOf(value.getAsset()));
    gen.writeStringField("free", EncodingUtil.bigDecimalToString(value.getFree()));
    gen.writeStringField("locked", EncodingUtil.bigDecimalToString(value.getLocked()));
    gen.writeEndObject();
  }
}
