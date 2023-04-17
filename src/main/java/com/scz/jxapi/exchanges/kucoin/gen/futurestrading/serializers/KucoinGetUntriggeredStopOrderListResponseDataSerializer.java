package com.scz.jxapi.exchanges.kucoin.gen.futurestrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetUntriggeredStopOrderListResponseData;

import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetUntriggeredStopOrderListResponseData
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinGetUntriggeredStopOrderListResponseData
 */
public class KucoinGetUntriggeredStopOrderListResponseDataSerializer extends StdSerializer<KucoinGetUntriggeredStopOrderListResponseData> {
  public KucoinGetUntriggeredStopOrderListResponseDataSerializer() {
    super(KucoinGetUntriggeredStopOrderListResponseData.class);
  }
  
  @Override
  public void serialize(KucoinGetUntriggeredStopOrderListResponseData value, JsonGenerator gen, SerializerProvider provider) throws IOException {
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
