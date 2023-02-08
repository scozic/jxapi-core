package com.scz.jcex.binance.spotmarketdata.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.binance.spotmarketdata.pojo.BinanceAllMarketTickersStreamResponse;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.binance.spotmarketdata.pojo.BinanceAllMarketTickersStreamResponse
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see BinanceAllMarketTickersStreamResponse
 */
public class BinanceAllMarketTickersStreamResponseSerializer extends StdSerializer<BinanceAllMarketTickersStreamResponse> {
  public BinanceAllMarketTickersStreamResponseSerializer() {
    super(BinanceAllMarketTickersStreamResponse.class);
  }
  
  @Override
  public void serialize(BinanceAllMarketTickersStreamResponse value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    gen.writeStringField("e", String.valueOf(value.gete()));
    gen.writeNumberField("E", value.getE());
    gen.writeStringField("s", String.valueOf(value.getS()));
    gen.writeStringField("p", String.valueOf(value.getp()));
    gen.writeStringField("P", String.valueOf(value.getP()));
    gen.writeStringField("o", String.valueOf(value.geto()));
    gen.writeStringField("h", String.valueOf(value.getH()));
    gen.writeStringField("l", String.valueOf(value.getl()));
    gen.writeStringField("c", String.valueOf(value.getc()));
    gen.writeStringField("w", String.valueOf(value.getW()));
    gen.writeStringField("v", String.valueOf(value.getV()));
    gen.writeStringField("q", String.valueOf(value.getQ()));
    gen.writeNumberField("O", value.getO());
    gen.writeNumberField("C", value.getC());
    gen.writeNumberField("F", value.getF());
    gen.writeNumberField("L", value.getL());
    gen.writeNumberField("n", value.getN());
    gen.writeEndObject();
  }
  
}
