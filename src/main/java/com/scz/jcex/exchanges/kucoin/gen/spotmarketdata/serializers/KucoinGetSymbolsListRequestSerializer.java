package com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGetSymbolsListRequest;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGetSymbolsListRequest
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinGetSymbolsListRequest
 */
public class KucoinGetSymbolsListRequestSerializer extends StdSerializer<KucoinGetSymbolsListRequest> {
  public KucoinGetSymbolsListRequestSerializer() {
    super(KucoinGetSymbolsListRequest.class);
  }
  
  @Override
  public void serialize(KucoinGetSymbolsListRequest value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    gen.writeEndObject();
  }
}
