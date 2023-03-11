package com.scz.jcex.binance.gen.spottrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.binance.gen.spottrading.pojo.BinanceExecutionReportUserDataStreamRequest;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.binance.gen.spottrading.pojo.BinanceExecutionReportUserDataStreamRequest
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see BinanceExecutionReportUserDataStreamRequest
 */
public class BinanceExecutionReportUserDataStreamRequestSerializer extends StdSerializer<BinanceExecutionReportUserDataStreamRequest> {
  public BinanceExecutionReportUserDataStreamRequestSerializer() {
    super(BinanceExecutionReportUserDataStreamRequest.class);
  }
  
  @Override
  public void serialize(BinanceExecutionReportUserDataStreamRequest value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    gen.writeEndObject();
  }
}
