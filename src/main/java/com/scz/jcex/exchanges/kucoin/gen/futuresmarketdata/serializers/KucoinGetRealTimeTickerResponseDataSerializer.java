package com.scz.jcex.exchanges.kucoin.gen.futuresmarketdata.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.exchanges.kucoin.gen.futuresmarketdata.pojo.KucoinGetRealTimeTickerResponseData;
import com.scz.jcex.util.EncodingUtil;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.exchanges.kucoin.gen.futuresmarketdata.pojo.KucoinGetRealTimeTickerResponseData
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinGetRealTimeTickerResponseData
 */
public class KucoinGetRealTimeTickerResponseDataSerializer extends StdSerializer<KucoinGetRealTimeTickerResponseData> {
  public KucoinGetRealTimeTickerResponseDataSerializer() {
    super(KucoinGetRealTimeTickerResponseData.class);
  }
  
  @Override
  public void serialize(KucoinGetRealTimeTickerResponseData value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getSequence() != null){
      gen.writeNumberField("sequence", value.getSequence());
    }
    if (value.getSymbol() != null){
      gen.writeStringField("symbol", String.valueOf(value.getSymbol()));
    }
    if (value.getSide() != null){
      gen.writeStringField("side", String.valueOf(value.getSide()));
    }
    if (value.getSize() != null){
      gen.writeStringField("size", EncodingUtil.bigDecimalToString(value.getSize()));
    }
    if (value.getPrice() != null){
      gen.writeStringField("price", EncodingUtil.bigDecimalToString(value.getPrice()));
    }
    if (value.getBestBidSize() != null){
      gen.writeStringField("bestBidSize", EncodingUtil.bigDecimalToString(value.getBestBidSize()));
    }
    if (value.getBestBidPrice() != null){
      gen.writeStringField("bestBidPrice", EncodingUtil.bigDecimalToString(value.getBestBidPrice()));
    }
    if (value.getBestAskPrice() != null){
      gen.writeStringField("bestAskPrice", EncodingUtil.bigDecimalToString(value.getBestAskPrice()));
    }
    if (value.getBestAskSize() != null){
      gen.writeStringField("bestAskSize", EncodingUtil.bigDecimalToString(value.getBestAskSize()));
    }
    if (value.getTradeId() != null){
      gen.writeStringField("tradeId", String.valueOf(value.getTradeId()));
    }
    if (value.getTs() != null){
      gen.writeNumberField("ts", value.getTs());
    }
    gen.writeEndObject();
  }
}
