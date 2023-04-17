package com.scz.jcex.exchanges.kucoin.gen.spottrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinCancelAllOrdersResponseData;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinCancelAllOrdersResponseData
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinCancelAllOrdersResponseData
 */
public class KucoinCancelAllOrdersResponseDataSerializer extends StdSerializer<KucoinCancelAllOrdersResponseData> {
  public KucoinCancelAllOrdersResponseDataSerializer() {
    super(KucoinCancelAllOrdersResponseData.class);
  }
  
  @Override
  public void serialize(KucoinCancelAllOrdersResponseData value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getCancelledOrderIds() != null){
      gen.writeObjectField("cancelledOrderIds", value.getCancelledOrderIds());
    }
    gen.writeEndObject();
  }
}
