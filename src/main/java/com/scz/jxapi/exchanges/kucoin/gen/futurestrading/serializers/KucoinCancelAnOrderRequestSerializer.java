package com.scz.jxapi.exchanges.kucoin.gen.futurestrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinCancelAnOrderRequest;

import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinCancelAnOrderRequest
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinCancelAnOrderRequest
 */
public class KucoinCancelAnOrderRequestSerializer extends StdSerializer<KucoinCancelAnOrderRequest> {
  public KucoinCancelAnOrderRequestSerializer() {
    super(KucoinCancelAnOrderRequest.class);
  }
  
  @Override
  public void serialize(KucoinCancelAnOrderRequest value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getOrderId() != null){
      gen.writeStringField("orderId", String.valueOf(value.getOrderId()));
    }
    gen.writeEndObject();
  }
}
