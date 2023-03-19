package com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinAllSymbolsTickerStreamMessage;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinAllSymbolsTickerStreamMessage
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
    gen.writeStringField("type", String.valueOf(value.getType()));
    gen.writeNumberField("topic", value.getTopic());
    gen.writeStringField("subject", String.valueOf(value.getSubject()));
    gen.writeObjectField("data", value.getData());
    gen.writeEndObject();
  }
}
