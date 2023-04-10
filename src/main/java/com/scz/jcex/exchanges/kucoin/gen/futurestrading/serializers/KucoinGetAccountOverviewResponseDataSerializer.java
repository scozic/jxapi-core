package com.scz.jcex.exchanges.kucoin.gen.futurestrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetAccountOverviewResponseData;
import com.scz.jcex.util.EncodingUtil;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetAccountOverviewResponseData
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinGetAccountOverviewResponseData
 */
public class KucoinGetAccountOverviewResponseDataSerializer extends StdSerializer<KucoinGetAccountOverviewResponseData> {
  public KucoinGetAccountOverviewResponseDataSerializer() {
    super(KucoinGetAccountOverviewResponseData.class);
  }
  
  @Override
  public void serialize(KucoinGetAccountOverviewResponseData value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getAccountEquity() != null){
      gen.writeStringField("accountEquity", EncodingUtil.bigDecimalToString(value.getAccountEquity()));
    }
    if (value.getUnrealisedPNL() != null){
      gen.writeStringField("unrealisedPNL", EncodingUtil.bigDecimalToString(value.getUnrealisedPNL()));
    }
    if (value.getMarginBalance() != null){
      gen.writeStringField("marginBalance", EncodingUtil.bigDecimalToString(value.getMarginBalance()));
    }
    if (value.getPositionMargin() != null){
      gen.writeStringField("positionMargin", EncodingUtil.bigDecimalToString(value.getPositionMargin()));
    }
    if (value.getOrderMargin() != null){
      gen.writeStringField("orderMargin", EncodingUtil.bigDecimalToString(value.getOrderMargin()));
    }
    if (value.getFrozenFunds() != null){
      gen.writeStringField("frozenFunds", EncodingUtil.bigDecimalToString(value.getFrozenFunds()));
    }
    if (value.getAvailableBalance() != null){
      gen.writeStringField("availableBalance", EncodingUtil.bigDecimalToString(value.getAvailableBalance()));
    }
    if (value.getCurrency() != null){
      gen.writeStringField("currency", String.valueOf(value.getCurrency()));
    }
    gen.writeEndObject();
  }
}
