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
    gen.writeStringField("e", String.valueOf(value.getEventType()));
    gen.writeNumberField("E", value.getEventTime());
    gen.writeStringField("a", String.valueOf(value.getAsset()));
    gen.writeStringField("d", EncodingUtil.bigDecimalToString(value.getBalanceDelta()));
    gen.writeNumberField("T", value.getClearTime());
    gen.writeEndObject();
  }
}
