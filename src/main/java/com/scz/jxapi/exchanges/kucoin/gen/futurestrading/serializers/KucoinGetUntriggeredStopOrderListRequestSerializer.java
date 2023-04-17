package com.scz.jxapi.exchanges.kucoin.gen.futurestrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetUntriggeredStopOrderListRequest;

import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetUntriggeredStopOrderListRequest
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinGetUntriggeredStopOrderListRequest
 */
public class KucoinGetUntriggeredStopOrderListRequestSerializer extends StdSerializer<KucoinGetUntriggeredStopOrderListRequest> {
  public KucoinGetUntriggeredStopOrderListRequestSerializer() {
    super(KucoinGetUntriggeredStopOrderListRequest.class);
  }
  
  @Override
  public void serialize(KucoinGetUntriggeredStopOrderListRequest value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getCurrentPage() != null){
      gen.writeNumberField("currentPage", value.getCurrentPage());
    }
    if (value.getPageSize() != null){
      gen.writeNumberField("pageSize", value.getPageSize());
    }
    if (value.getStatus() != null){
      gen.writeStringField("status", String.valueOf(value.getStatus()));
    }
    if (value.getSymbol() != null){
      gen.writeStringField("symbol", String.valueOf(value.getSymbol()));
    }
    if (value.getSide() != null){
      gen.writeStringField("side", String.valueOf(value.getSide()));
    }
    if (value.getType() != null){
      gen.writeStringField("type", String.valueOf(value.getType()));
    }
    if (value.getStartAt() != null){
      gen.writeNumberField("startAt", value.getStartAt());
    }
    if (value.getEndAt() != null){
      gen.writeNumberField("endAt", value.getEndAt());
    }
    gen.writeEndObject();
  }
}
