package com.scz.jcex.binance.gen.spotmarketdata.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.binance.gen.spotmarketdata.pojo.BinanceAllMarketTickersStreamResponse;
import com.scz.jcex.util.EncodingUtil;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.binance.gen.spotmarketdata.pojo.BinanceAllMarketTickersStreamResponse
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
    gen.writeStringField("p", EncodingUtil.bigDecimalToString(value.getp()));
    gen.writeStringField("P", EncodingUtil.bigDecimalToString(value.getP()));
    gen.writeStringField("o", EncodingUtil.bigDecimalToString(value.geto()));
    gen.writeStringField("h", EncodingUtil.bigDecimalToString(value.getH()));
    gen.writeStringField("l", EncodingUtil.bigDecimalToString(value.getl()));
    gen.writeStringField("c", EncodingUtil.bigDecimalToString(value.getc()));
    gen.writeStringField("w", EncodingUtil.bigDecimalToString(value.getW()));
    gen.writeStringField("v", EncodingUtil.bigDecimalToString(value.getV()));
    gen.writeStringField("q", EncodingUtil.bigDecimalToString(value.getQ()));
    gen.writeNumberField("O", value.getO());
    gen.writeNumberField("C", value.getC());
    gen.writeNumberField("F", value.getF());
    gen.writeNumberField("L", value.getL());
    gen.writeNumberField("n", value.getN());
    gen.writeEndObject();
  }
  
}
