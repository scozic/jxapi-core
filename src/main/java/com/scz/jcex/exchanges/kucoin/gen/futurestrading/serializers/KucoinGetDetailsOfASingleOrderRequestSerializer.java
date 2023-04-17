package com.scz.jcex.exchanges.kucoin.gen.futurestrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetDetailsOfASingleOrderRequest;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetDetailsOfASingleOrderRequest
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinGetDetailsOfASingleOrderRequest
 */
public class KucoinGetDetailsOfASingleOrderRequestSerializer extends StdSerializer<KucoinGetDetailsOfASingleOrderRequest> {
  public KucoinGetDetailsOfASingleOrderRequestSerializer() {
    super(KucoinGetDetailsOfASingleOrderRequest.class);
  }
  
  @Override
  public void serialize(KucoinGetDetailsOfASingleOrderRequest value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getOrderId() != null){
      gen.writeStringField("orderId", String.valueOf(value.getOrderId()));
    }
    gen.writeEndObject();
  }
}
