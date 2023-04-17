package com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinAllSymbolsTickerStreamMessage;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinAllSymbolsTickerStreamMessage
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinAllSymbolsTickerStreamMessage
 */
public class KucoinAllSymbolsTickerStreamMessageSerializer extends StdSerializer<KucoinAllSymbolsTickerStreamMessage> {
  public KucoinAllSymbolsTickerStreamMessageSerializer() {
    super(KucoinAllSymbolsTickerStreamMessage.class);
  }
  
  @Override
  public void serialize(KucoinAllSymbolsTickerStreamMessage value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getType() != null){
      gen.writeStringField("type", String.valueOf(value.getType()));
    }
    if (value.getTopic() != null){
      gen.writeNumberField("topic", value.getTopic());
    }
    if (value.getSubject() != null){
      gen.writeStringField("subject", String.valueOf(value.getSubject()));
    }
    if (value.getData() != null){
      gen.writeObjectField("data", value.getData());
    }
    gen.writeEndObject();
  }
}
