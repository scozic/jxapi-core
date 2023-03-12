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
    gen.writeStringField("e", String.valueOf(value.getEventType()));
    gen.writeNumberField("E", value.getEventTime());
    gen.writeStringField("s", String.valueOf(value.getSymbol()));
    gen.writeStringField("p", EncodingUtil.bigDecimalToString(value.getPriceChange()));
    gen.writeStringField("P", EncodingUtil.bigDecimalToString(value.getPriceChangePercent()));
    gen.writeStringField("o", EncodingUtil.bigDecimalToString(value.getOpenPrice()));
    gen.writeStringField("h", EncodingUtil.bigDecimalToString(value.getHighPrice()));
    gen.writeStringField("l", EncodingUtil.bigDecimalToString(value.getLowPrice()));
    gen.writeStringField("c", EncodingUtil.bigDecimalToString(value.getLastPrice()));
    gen.writeStringField("w", EncodingUtil.bigDecimalToString(value.getWeightedAvgPrice()));
    gen.writeStringField("v", EncodingUtil.bigDecimalToString(value.getBaseAssetVolume()));
    gen.writeStringField("q", EncodingUtil.bigDecimalToString(value.getQuoteAssetVolume()));
    gen.writeNumberField("O", value.getOpenTime());
    gen.writeNumberField("C", value.getCloseTime());
    gen.writeNumberField("F", value.getFirstTradeID());
    gen.writeNumberField("L", value.getLastTradeID());
    gen.writeNumberField("n", value.getTradeCount());
    gen.writeEndObject();
  }
}
