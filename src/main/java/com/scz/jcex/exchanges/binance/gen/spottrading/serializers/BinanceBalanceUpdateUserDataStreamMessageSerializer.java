package com.scz.jcex.exchanges.binance.gen.spottrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.exchanges.binance.gen.spottrading.pojo.BinanceBalanceUpdateUserDataStreamMessage;
import com.scz.jcex.util.EncodingUtil;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.exchanges.binance.gen.spottrading.pojo.BinanceBalanceUpdateUserDataStreamMessage
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see BinanceBalanceUpdateUserDataStreamMessage
 */
public class BinanceBalanceUpdateUserDataStreamMessageSerializer extends StdSerializer<BinanceBalanceUpdateUserDataStreamMessage> {
  public BinanceBalanceUpdateUserDataStreamMessageSerializer() {
    super(BinanceBalanceUpdateUserDataStreamMessage.class);
  }
  
  @Override
  public void serialize(BinanceBalanceUpdateUserDataStreamMessage value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getEventType() != null){
      gen.writeStringField("e", String.valueOf(value.getEventType()));
    }
    if (value.getEventTime() != null){
      gen.writeNumberField("E", value.getEventTime());
    }
    if (value.getAsset() != null){
      gen.writeStringField("a", String.valueOf(value.getAsset()));
    }
    if (value.getBalanceDelta() != null){
      gen.writeStringField("d", EncodingUtil.bigDecimalToString(value.getBalanceDelta()));
    }
    if (value.getClearTime() != null){
      gen.writeNumberField("T", value.getClearTime());
    }
    gen.writeEndObject();
  }
}
