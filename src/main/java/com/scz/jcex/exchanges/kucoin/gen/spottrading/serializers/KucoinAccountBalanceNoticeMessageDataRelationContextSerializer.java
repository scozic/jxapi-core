package com.scz.jcex.exchanges.kucoin.gen.spottrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinAccountBalanceNoticeMessageDataRelationContext;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinAccountBalanceNoticeMessageDataRelationContext
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinAccountBalanceNoticeMessageDataRelationContext
 */
public class KucoinAccountBalanceNoticeMessageDataRelationContextSerializer extends StdSerializer<KucoinAccountBalanceNoticeMessageDataRelationContext> {
  public KucoinAccountBalanceNoticeMessageDataRelationContextSerializer() {
    super(KucoinAccountBalanceNoticeMessageDataRelationContext.class);
  }
  
  @Override
  public void serialize(KucoinAccountBalanceNoticeMessageDataRelationContext value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    gen.writeStringField("symbol", String.valueOf(value.getSymbol()));
    gen.writeStringField("tradeId", String.valueOf(value.getTradeId()));
    gen.writeStringField("orderId", String.valueOf(value.getOrderId()));
    gen.writeEndObject();
  }
}
