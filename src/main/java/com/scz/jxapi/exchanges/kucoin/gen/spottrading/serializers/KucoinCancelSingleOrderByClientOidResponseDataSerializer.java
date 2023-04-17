package com.scz.jxapi.exchanges.kucoin.gen.spottrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinCancelSingleOrderByClientOidResponseData;

import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinCancelSingleOrderByClientOidResponseData
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinCancelSingleOrderByClientOidResponseData
 */
public class KucoinCancelSingleOrderByClientOidResponseDataSerializer extends StdSerializer<KucoinCancelSingleOrderByClientOidResponseData> {
  public KucoinCancelSingleOrderByClientOidResponseDataSerializer() {
    super(KucoinCancelSingleOrderByClientOidResponseData.class);
  }
  
  @Override
  public void serialize(KucoinCancelSingleOrderByClientOidResponseData value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getCancelledOrderId() != null){
      gen.writeStringField("cancelledOrderId", String.valueOf(value.getCancelledOrderId()));
    }
    if (value.getClientOid() != null){
      gen.writeStringField("clientOid", String.valueOf(value.getClientOid()));
    }
    gen.writeEndObject();
  }
}
