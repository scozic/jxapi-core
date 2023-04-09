package com.scz.jcex.exchanges.kucoin.gen.spottrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinListOrdersRequest;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinListOrdersRequest
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinListOrdersRequest
 */
public class KucoinListOrdersRequestSerializer extends StdSerializer<KucoinListOrdersRequest> {
  public KucoinListOrdersRequestSerializer() {
    super(KucoinListOrdersRequest.class);
  }
  
  @Override
  public void serialize(KucoinListOrdersRequest value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    gen.writeNumberField("currentPage", value.getCurrentPage());
    gen.writeNumberField("pageSize", value.getPageSize());
    gen.writeStringField("status", String.valueOf(value.getStatus()));
    gen.writeStringField("symbol", String.valueOf(value.getSymbol()));
    gen.writeStringField("side", String.valueOf(value.getSide()));
    gen.writeStringField("type", String.valueOf(value.getType()));
    gen.writeStringField("tradeType", String.valueOf(value.getTradeType()));
    gen.writeNumberField("startAt", value.getStartAt());
    gen.writeNumberField("endAt", value.getEndAt());
    gen.writeEndObject();
  }
}
