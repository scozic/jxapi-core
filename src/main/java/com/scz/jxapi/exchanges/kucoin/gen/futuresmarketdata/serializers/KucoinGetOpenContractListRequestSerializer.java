package com.scz.jxapi.exchanges.kucoin.gen.futuresmarketdata.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jxapi.exchanges.kucoin.gen.futuresmarketdata.pojo.KucoinGetOpenContractListRequest;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jxapi.exchanges.kucoin.gen.futuresmarketdata.pojo.KucoinGetOpenContractListRequest
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinGetOpenContractListRequest
 */
public class KucoinGetOpenContractListRequestSerializer extends StdSerializer<KucoinGetOpenContractListRequest> {
  public KucoinGetOpenContractListRequestSerializer() {
    super(KucoinGetOpenContractListRequest.class);
  }
  
  @Override
  public void serialize(KucoinGetOpenContractListRequest value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    gen.writeEndObject();
  }
}
