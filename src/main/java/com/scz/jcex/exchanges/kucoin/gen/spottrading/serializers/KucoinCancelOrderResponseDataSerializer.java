package com.scz.jcex.exchanges.kucoin.gen.spottrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinCancelOrderResponseData;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinCancelOrderResponseData
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinCancelOrderResponseData
 */
public class KucoinCancelOrderResponseDataSerializer extends StdSerializer<KucoinCancelOrderResponseData> {
  public KucoinCancelOrderResponseDataSerializer() {
    super(KucoinCancelOrderResponseData.class);
  }
  
  @Override
  public void serialize(KucoinCancelOrderResponseData value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    gen.writeObjectField("cancelledOrderIds", value.getCancelledOrderIds());
    gen.writeEndObject();
  }
}
