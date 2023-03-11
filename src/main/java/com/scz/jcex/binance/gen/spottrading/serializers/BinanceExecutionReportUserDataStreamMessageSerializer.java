package com.scz.jcex.binance.gen.spottrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.binance.gen.spottrading.pojo.BinanceExecutionReportUserDataStreamMessage;
import com.scz.jcex.util.EncodingUtil;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.binance.gen.spottrading.pojo.BinanceExecutionReportUserDataStreamMessage
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see BinanceExecutionReportUserDataStreamMessage
 */
public class BinanceExecutionReportUserDataStreamMessageSerializer extends StdSerializer<BinanceExecutionReportUserDataStreamMessage> {
  public BinanceExecutionReportUserDataStreamMessageSerializer() {
    super(BinanceExecutionReportUserDataStreamMessage.class);
  }
  
  @Override
  public void serialize(BinanceExecutionReportUserDataStreamMessage value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    gen.writeStringField("e", String.valueOf(value.gete()));
    gen.writeNumberField("E", value.getE());
    gen.writeStringField("s", String.valueOf(value.gets()));
    gen.writeStringField("c", String.valueOf(value.getc()));
    gen.writeStringField("S", String.valueOf(value.getS()));
    gen.writeStringField("o", String.valueOf(value.geto()));
    gen.writeStringField("f", String.valueOf(value.getf()));
    gen.writeStringField("q", EncodingUtil.bigDecimalToString(value.getq()));
    gen.writeStringField("p", EncodingUtil.bigDecimalToString(value.getp()));
    gen.writeStringField("P", EncodingUtil.bigDecimalToString(value.getP()));
    gen.writeNumberField("d", value.getd());
    gen.writeStringField("F", EncodingUtil.bigDecimalToString(value.getF()));
    gen.writeNumberField("g", value.getG());
    gen.writeStringField("C", String.valueOf(value.getC()));
    gen.writeStringField("x", String.valueOf(value.getx()));
    gen.writeStringField("X", String.valueOf(value.getX()));
    gen.writeStringField("r", String.valueOf(value.getR()));
    gen.writeStringField("i", String.valueOf(value.geti()));
    gen.writeStringField("l", EncodingUtil.bigDecimalToString(value.getl()));
    gen.writeStringField("z", EncodingUtil.bigDecimalToString(value.getz()));
    gen.writeStringField("L", EncodingUtil.bigDecimalToString(value.getL()));
    gen.writeNumberField("n", value.getn());
    gen.writeStringField("N", String.valueOf(value.getN()));
    gen.writeNumberField("T", value.getT());
    gen.writeNumberField("t", value.gett());
    gen.writeNumberField("v", value.getv());
    gen.writeNumberField("I", value.getI());
    gen.writeBooleanField("w", value.isw());
    gen.writeBooleanField("m", value.ism());
    gen.writeBooleanField("M", value.isM());
    gen.writeNumberField("O", value.getO());
    gen.writeStringField("Z", EncodingUtil.bigDecimalToString(value.getZ()));
    gen.writeStringField("Y", EncodingUtil.bigDecimalToString(value.getY()));
    gen.writeStringField("Q", EncodingUtil.bigDecimalToString(value.getQ()));
    gen.writeNumberField("D", value.getD());
    gen.writeNumberField("j", value.getj());
    gen.writeNumberField("J", value.getJ());
    gen.writeNumberField("W", value.getW());
    gen.writeStringField("V", String.valueOf(value.getV()));
    gen.writeNumberField("u", value.getu());
    gen.writeNumberField("U", value.getU());
    gen.writeStringField("A", EncodingUtil.bigDecimalToString(value.getA()));
    gen.writeStringField("B", EncodingUtil.bigDecimalToString(value.getB()));
    gen.writeEndObject();
  }
}
