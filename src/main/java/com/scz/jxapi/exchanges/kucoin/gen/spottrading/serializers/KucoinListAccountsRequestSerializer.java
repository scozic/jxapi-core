package com.scz.jxapi.exchanges.kucoin.gen.spottrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinListAccountsRequest;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinListAccountsRequest
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinListAccountsRequest
 */
public class KucoinListAccountsRequestSerializer extends StdSerializer<KucoinListAccountsRequest> {
  public KucoinListAccountsRequestSerializer() {
    super(KucoinListAccountsRequest.class);
  }
  
  @Override
  public void serialize(KucoinListAccountsRequest value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getType() != null){
      gen.writeStringField("type", String.valueOf(value.getType()));
    }
    gen.writeEndObject();
  }
}
