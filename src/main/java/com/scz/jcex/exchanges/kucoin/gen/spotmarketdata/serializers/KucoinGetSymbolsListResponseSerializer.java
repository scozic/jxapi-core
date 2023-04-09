package com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGetSymbolsListResponse;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGetSymbolsListResponse
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinGetSymbolsListResponse
 */
public class KucoinGetSymbolsListResponseSerializer extends StdSerializer<KucoinGetSymbolsListResponse> {
  public KucoinGetSymbolsListResponseSerializer() {
    super(KucoinGetSymbolsListResponse.class);
  }
  
  @Override
  public void serialize(KucoinGetSymbolsListResponse value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getCode() != null){
      gen.writeStringField("code", String.valueOf(value.getCode()));
    }
    if (value.getMsg() != null){
      gen.writeStringField("msg", String.valueOf(value.getMsg()));
    }
    if (value.getData() != null){
      gen.writeObjectField("data", value.getData());
    }
    gen.writeEndObject();
  }
}
