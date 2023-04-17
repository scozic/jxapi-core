package com.scz.jcex.exchanges.kucoin.gen.futuresmarketdata.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.exchanges.kucoin.gen.futuresmarketdata.pojo.KucoinRealTimeSymbolTickerV2Request;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.exchanges.kucoin.gen.futuresmarketdata.pojo.KucoinRealTimeSymbolTickerV2Request
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinRealTimeSymbolTickerV2Request
 */
public class KucoinRealTimeSymbolTickerV2RequestSerializer extends StdSerializer<KucoinRealTimeSymbolTickerV2Request> {
  public KucoinRealTimeSymbolTickerV2RequestSerializer() {
    super(KucoinRealTimeSymbolTickerV2Request.class);
  }
  
  @Override
  public void serialize(KucoinRealTimeSymbolTickerV2Request value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getSymbol() != null){
      gen.writeStringField("symbol", String.valueOf(value.getSymbol()));
    }
    gen.writeEndObject();
  }
}
