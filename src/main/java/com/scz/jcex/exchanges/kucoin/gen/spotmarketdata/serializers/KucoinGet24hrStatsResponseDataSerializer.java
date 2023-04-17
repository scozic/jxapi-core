package com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGet24hrStatsResponseData;
import com.scz.jcex.util.EncodingUtil;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGet24hrStatsResponseData
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinGet24hrStatsResponseData
 */
public class KucoinGet24hrStatsResponseDataSerializer extends StdSerializer<KucoinGet24hrStatsResponseData> {
  public KucoinGet24hrStatsResponseDataSerializer() {
    super(KucoinGet24hrStatsResponseData.class);
  }
  
  @Override
  public void serialize(KucoinGet24hrStatsResponseData value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getTime() != null){
      gen.writeNumberField("time", value.getTime());
    }
    if (value.getSymbol() != null){
      gen.writeStringField("symbol", String.valueOf(value.getSymbol()));
    }
    if (value.getBuy() != null){
      gen.writeStringField("buy", EncodingUtil.bigDecimalToString(value.getBuy()));
    }
    if (value.getSell() != null){
      gen.writeStringField("sell", EncodingUtil.bigDecimalToString(value.getSell()));
    }
    if (value.getChangeRate() != null){
      gen.writeStringField("changeRate", EncodingUtil.bigDecimalToString(value.getChangeRate()));
    }
    if (value.getChangePrice() != null){
      gen.writeStringField("changePrice", EncodingUtil.bigDecimalToString(value.getChangePrice()));
    }
    if (value.getHigh() != null){
      gen.writeStringField("high", EncodingUtil.bigDecimalToString(value.getHigh()));
    }
    if (value.getLow() != null){
      gen.writeStringField("low", EncodingUtil.bigDecimalToString(value.getLow()));
    }
    if (value.getVol() != null){
      gen.writeStringField("vol", EncodingUtil.bigDecimalToString(value.getVol()));
    }
    if (value.getVolValue() != null){
      gen.writeStringField("volValue", EncodingUtil.bigDecimalToString(value.getVolValue()));
    }
    if (value.getLast() != null){
      gen.writeStringField("last", EncodingUtil.bigDecimalToString(value.getLast()));
    }
    if (value.getAveragePrice() != null){
      gen.writeStringField("averagePrice", EncodingUtil.bigDecimalToString(value.getAveragePrice()));
    }
    if (value.getTakerFeeRate() != null){
      gen.writeStringField("takerFeeRate", EncodingUtil.bigDecimalToString(value.getTakerFeeRate()));
    }
    if (value.getMakerFeeRate() != null){
      gen.writeStringField("makerFeeRate", EncodingUtil.bigDecimalToString(value.getMakerFeeRate()));
    }
    if (value.getTakerCoefficient() != null){
      gen.writeStringField("takerCoefficient", EncodingUtil.bigDecimalToString(value.getTakerCoefficient()));
    }
    if (value.getMakerCoefficient() != null){
      gen.writeStringField("makerCoefficient", EncodingUtil.bigDecimalToString(value.getMakerCoefficient()));
    }
    gen.writeEndObject();
  }
}
