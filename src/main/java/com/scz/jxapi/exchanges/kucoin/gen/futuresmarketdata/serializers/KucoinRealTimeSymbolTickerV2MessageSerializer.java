package com.scz.jxapi.exchanges.kucoin.gen.futuresmarketdata.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jxapi.exchanges.kucoin.gen.futuresmarketdata.pojo.KucoinRealTimeSymbolTickerV2Message;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jxapi.exchanges.kucoin.gen.futuresmarketdata.pojo.KucoinRealTimeSymbolTickerV2Message
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinRealTimeSymbolTickerV2Message
 */
public class KucoinRealTimeSymbolTickerV2MessageSerializer extends StdSerializer<KucoinRealTimeSymbolTickerV2Message> {
  public KucoinRealTimeSymbolTickerV2MessageSerializer() {
    super(KucoinRealTimeSymbolTickerV2Message.class);
  }
  
  @Override
  public void serialize(KucoinRealTimeSymbolTickerV2Message value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getSubject() != null){
      gen.writeStringField("subject", String.valueOf(value.getSubject()));
    }
    if (value.getTopic() != null){
      gen.writeStringField("topic", String.valueOf(value.getTopic()));
    }
    if (value.getData() != null){
      gen.writeObjectField("data", value.getData());
    }
    gen.writeEndObject();
  }
}
