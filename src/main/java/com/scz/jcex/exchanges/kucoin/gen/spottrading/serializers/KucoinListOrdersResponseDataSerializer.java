package com.scz.jcex.exchanges.kucoin.gen.spottrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinListOrdersResponseData;
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
    gen.writeNumberField("currentPage", value.getCurrentPage());
    gen.writeNumberField("pageSize", value.getPageSize());
    gen.writeNumberField("totalPages", value.getTotalPages());
    gen.writeNumberField("totalNum", value.getTotalNum());
    gen.writeObjectField("items", value.getItems());
    gen.writeEndObject();
  }
}
