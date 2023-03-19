package com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGetAllTickersResponseData;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGetAllTickersResponseData
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinGetAllTickersResponseData
 */
public class KucoinGetAllTickersResponseDataSerializer extends StdSerializer<KucoinGetAllTickersResponseData> {
  public KucoinGetAllTickersResponseDataSerializer() {
    super(KucoinGetAllTickersResponseData.class);
  }
  
  @Override
  public void serialize(KucoinGetAllTickersResponseData value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    gen.writeNumberField("time", value.getTime());
    gen.writeObjectField("ticker", value.getTicker());
    gen.writeEndObject();
  }
}
