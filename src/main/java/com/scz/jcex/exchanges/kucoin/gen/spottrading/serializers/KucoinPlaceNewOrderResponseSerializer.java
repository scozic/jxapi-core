package com.scz.jcex.exchanges.kucoin.gen.spottrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinPlaceNewOrderResponse;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinPlaceNewOrderResponse
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinPlaceNewOrderResponse
 */
public class KucoinPlaceNewOrderResponseSerializer extends StdSerializer<KucoinPlaceNewOrderResponse> {
  public KucoinPlaceNewOrderResponseSerializer() {
    super(KucoinPlaceNewOrderResponse.class);
  }
  
  @Override
  public void serialize(KucoinPlaceNewOrderResponse value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getOrderId() != null){
      gen.writeStringField("orderId", String.valueOf(value.getOrderId()));
    }
    gen.writeEndObject();
  }
}
