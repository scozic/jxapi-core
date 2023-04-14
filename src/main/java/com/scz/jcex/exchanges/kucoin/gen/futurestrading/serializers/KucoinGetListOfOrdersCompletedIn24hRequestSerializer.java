package com.scz.jcex.exchanges.kucoin.gen.futurestrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetListOfOrdersCompletedIn24hRequest;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetListOfOrdersCompletedIn24hRequest
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinGetListOfOrdersCompletedIn24hRequest
 */
public class KucoinGetListOfOrdersCompletedIn24hRequestSerializer extends StdSerializer<KucoinGetListOfOrdersCompletedIn24hRequest> {
  public KucoinGetListOfOrdersCompletedIn24hRequestSerializer() {
    super(KucoinGetListOfOrdersCompletedIn24hRequest.class);
  }
  
  @Override
  public void serialize(KucoinGetListOfOrdersCompletedIn24hRequest value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getSymbol() != null){
      gen.writeStringField("symbol", String.valueOf(value.getSymbol()));
    }
    gen.writeEndObject();
  }
}
