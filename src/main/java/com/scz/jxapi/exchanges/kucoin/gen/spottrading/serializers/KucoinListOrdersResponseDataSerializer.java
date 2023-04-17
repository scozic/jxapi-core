package com.scz.jxapi.exchanges.kucoin.gen.spottrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinListOrdersResponseData;

import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinListOrdersResponseData
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinListOrdersResponseData
 */
public class KucoinListOrdersResponseDataSerializer extends StdSerializer<KucoinListOrdersResponseData> {
  public KucoinListOrdersResponseDataSerializer() {
    super(KucoinListOrdersResponseData.class);
  }
  
  @Override
  public void serialize(KucoinListOrdersResponseData value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getCurrentPage() != null){
      gen.writeNumberField("currentPage", value.getCurrentPage());
    }
    if (value.getPageSize() != null){
      gen.writeNumberField("pageSize", value.getPageSize());
    }
    if (value.getTotalPages() != null){
      gen.writeNumberField("totalPages", value.getTotalPages());
    }
    if (value.getTotalNum() != null){
      gen.writeNumberField("totalNum", value.getTotalNum());
    }
    if (value.getItems() != null){
      gen.writeObjectField("items", value.getItems());
    }
    gen.writeEndObject();
  }
}
