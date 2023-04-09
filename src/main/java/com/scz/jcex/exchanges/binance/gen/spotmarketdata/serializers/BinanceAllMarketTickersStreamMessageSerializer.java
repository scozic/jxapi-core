package com.scz.jcex.exchanges.binance.gen.spotmarketdata.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.exchanges.binance.gen.spotmarketdata.pojo.BinanceAllMarketTickersStreamMessage;
import com.scz.jcex.util.EncodingUtil;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.exchanges.binance.gen.spotmarketdata.pojo.BinanceAllMarketTickersStreamMessage
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see BinanceAllMarketTickersStreamMessage
 */
public class BinanceAllMarketTickersStreamMessageSerializer extends StdSerializer<BinanceAllMarketTickersStreamMessage> {
  public BinanceAllMarketTickersStreamMessageSerializer() {
    super(BinanceAllMarketTickersStreamMessage.class);
  }
  
  @Override
  public void serialize(BinanceAllMarketTickersStreamMessage value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getEventType() != null){
      gen.writeStringField("e", String.valueOf(value.getEventType()));
    }
    if (value.getEventTime() != null){
      gen.writeNumberField("E", value.getEventTime());
    }
    if (value.getSymbol() != null){
      gen.writeStringField("s", String.valueOf(value.getSymbol()));
    }
    if (value.getPriceChange() != null){
      gen.writeStringField("p", EncodingUtil.bigDecimalToString(value.getPriceChange()));
    }
    if (value.getPriceChangePercent() != null){
      gen.writeStringField("P", EncodingUtil.bigDecimalToString(value.getPriceChangePercent()));
    }
    if (value.getOpenPrice() != null){
      gen.writeStringField("o", EncodingUtil.bigDecimalToString(value.getOpenPrice()));
    }
    if (value.getHighPrice() != null){
      gen.writeStringField("h", EncodingUtil.bigDecimalToString(value.getHighPrice()));
    }
    if (value.getLowPrice() != null){
      gen.writeStringField("l", EncodingUtil.bigDecimalToString(value.getLowPrice()));
    }
    if (value.getLastPrice() != null){
      gen.writeStringField("c", EncodingUtil.bigDecimalToString(value.getLastPrice()));
    }
    if (value.getWeightedAvgPrice() != null){
      gen.writeStringField("w", EncodingUtil.bigDecimalToString(value.getWeightedAvgPrice()));
    }
    if (value.getBaseAssetVolume() != null){
      gen.writeStringField("v", EncodingUtil.bigDecimalToString(value.getBaseAssetVolume()));
    }
    if (value.getQuoteAssetVolume() != null){
      gen.writeStringField("q", EncodingUtil.bigDecimalToString(value.getQuoteAssetVolume()));
    }
    if (value.getOpenTime() != null){
      gen.writeNumberField("O", value.getOpenTime());
    }
    if (value.getCloseTime() != null){
      gen.writeNumberField("C", value.getCloseTime());
    }
    if (value.getFirstTradeID() != null){
      gen.writeNumberField("F", value.getFirstTradeID());
    }
    if (value.getLastTradeID() != null){
      gen.writeNumberField("L", value.getLastTradeID());
    }
    if (value.getTradeCount() != null){
      gen.writeNumberField("n", value.getTradeCount());
    }
    gen.writeEndObject();
  }
}
