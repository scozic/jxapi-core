package com.scz.jcex.exchanges.kucoin.gen.spottrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinListAccountsResponseData;
import com.scz.jcex.util.EncodingUtil;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinListAccountsResponseData
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinListAccountsResponseData
 */
public class KucoinListAccountsResponseDataSerializer extends StdSerializer<KucoinListAccountsResponseData> {
  public KucoinListAccountsResponseDataSerializer() {
    super(KucoinListAccountsResponseData.class);
  }
  
  @Override
  public void serialize(KucoinListAccountsResponseData value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    gen.writeStringField("id", String.valueOf(value.getId()));
    gen.writeStringField("currency", String.valueOf(value.getCurrency()));
    gen.writeStringField("type", String.valueOf(value.getType()));
    gen.writeStringField("balance", EncodingUtil.bigDecimalToString(value.getBalance()));
    gen.writeStringField("available", EncodingUtil.bigDecimalToString(value.getAvailable()));
    gen.writeStringField("holds", EncodingUtil.bigDecimalToString(value.getHolds()));
    gen.writeEndObject();
  }
}
