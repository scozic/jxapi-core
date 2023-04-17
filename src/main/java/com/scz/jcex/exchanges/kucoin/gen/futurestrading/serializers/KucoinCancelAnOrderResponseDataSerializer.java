package com.scz.jcex.exchanges.kucoin.gen.futurestrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinCancelAnOrderResponseData;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinCancelAnOrderResponseData
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinCancelAnOrderResponseData
 */
public class KucoinCancelAnOrderResponseDataSerializer extends StdSerializer<KucoinCancelAnOrderResponseData> {
  public KucoinCancelAnOrderResponseDataSerializer() {
    super(KucoinCancelAnOrderResponseData.class);
  }
  
  @Override
  public void serialize(KucoinCancelAnOrderResponseData value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getCancelledOrderIds() != null){
      gen.writeObjectField("cancelledOrderIds", value.getCancelledOrderIds());
    }
    gen.writeEndObject();
  }
}
