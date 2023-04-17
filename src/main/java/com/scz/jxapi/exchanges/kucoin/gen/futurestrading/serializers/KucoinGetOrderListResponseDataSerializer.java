package com.scz.jxapi.exchanges.kucoin.gen.futurestrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetOrderListResponseData;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetOrderListResponseData
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinGetOrderListResponseData
 */
public class KucoinGetOrderListResponseDataSerializer extends StdSerializer<KucoinGetOrderListResponseData> {
  public KucoinGetOrderListResponseDataSerializer() {
    super(KucoinGetOrderListResponseData.class);
  }
  
  @Override
  public void serialize(KucoinGetOrderListResponseData value, JsonGenerator gen, SerializerProvider provider) throws IOException {
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
