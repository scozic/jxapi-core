package com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinAllSymbolsTickerStreamRequest;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinAllSymbolsTickerStreamRequest
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinAllSymbolsTickerStreamRequest
 */
public class KucoinAllSymbolsTickerStreamRequestSerializer extends StdSerializer<KucoinAllSymbolsTickerStreamRequest> {
  public KucoinAllSymbolsTickerStreamRequestSerializer() {
    super(KucoinAllSymbolsTickerStreamRequest.class);
  }
  
  @Override
  public void serialize(KucoinAllSymbolsTickerStreamRequest value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    gen.writeEndObject();
  }
}
