package com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGetAllTickersResponseDataTicker;
import com.scz.jcex.util.EncodingUtil;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGetAllTickersResponseDataTicker
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinGetAllTickersResponseDataTicker
 */
public class KucoinGetAllTickersResponseDataTickerSerializer extends StdSerializer<KucoinGetAllTickersResponseDataTicker> {
  public KucoinGetAllTickersResponseDataTickerSerializer() {
    super(KucoinGetAllTickersResponseDataTicker.class);
  }
  
  @Override
  public void serialize(KucoinGetAllTickersResponseDataTicker value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    gen.writeStringField("symbol", String.valueOf(value.getSymbol()));
    gen.writeStringField("symbolName", String.valueOf(value.getSymbolName()));
    gen.writeStringField("buy", EncodingUtil.bigDecimalToString(value.getBuy()));
    gen.writeStringField("sell", EncodingUtil.bigDecimalToString(value.getSell()));
    gen.writeStringField("changeRate", EncodingUtil.bigDecimalToString(value.getChangeRate()));
    gen.writeStringField("changePrice", EncodingUtil.bigDecimalToString(value.getChangePrice()));
    gen.writeStringField("high", EncodingUtil.bigDecimalToString(value.getHigh()));
    gen.writeStringField("low", EncodingUtil.bigDecimalToString(value.getLow()));
    gen.writeStringField("vol", EncodingUtil.bigDecimalToString(value.getVol()));
    gen.writeStringField("volValue", EncodingUtil.bigDecimalToString(value.getVolValue()));
    gen.writeStringField("last", EncodingUtil.bigDecimalToString(value.getLast()));
    gen.writeStringField("averagePrice", EncodingUtil.bigDecimalToString(value.getAveragePrice()));
    gen.writeStringField("takerFeeRate", EncodingUtil.bigDecimalToString(value.getTakerFeeRate()));
    gen.writeStringField("makerFeeRate", EncodingUtil.bigDecimalToString(value.getMakerFeeRate()));
    gen.writeStringField("takerCoefficient", EncodingUtil.bigDecimalToString(value.getTakerCoefficient()));
    gen.writeStringField("makerCoefficient", EncodingUtil.bigDecimalToString(value.getMakerCoefficient()));
    gen.writeEndObject();
  }
}
