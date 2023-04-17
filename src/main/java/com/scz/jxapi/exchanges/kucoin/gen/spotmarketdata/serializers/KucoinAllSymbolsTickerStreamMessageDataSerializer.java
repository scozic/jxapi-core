package com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinAllSymbolsTickerStreamMessageData;
import com.scz.jxapi.util.EncodingUtil;

import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinAllSymbolsTickerStreamMessageData
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinAllSymbolsTickerStreamMessageData
 */
public class KucoinAllSymbolsTickerStreamMessageDataSerializer extends StdSerializer<KucoinAllSymbolsTickerStreamMessageData> {
  public KucoinAllSymbolsTickerStreamMessageDataSerializer() {
    super(KucoinAllSymbolsTickerStreamMessageData.class);
  }
  
  @Override
  public void serialize(KucoinAllSymbolsTickerStreamMessageData value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getSequence() != null){
      gen.writeStringField("sequence", String.valueOf(value.getSequence()));
    }
    if (value.getBestAsk() != null){
      gen.writeStringField("bestAsk", EncodingUtil.bigDecimalToString(value.getBestAsk()));
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
    if (value.getBestBid() != null){
      gen.writeStringField("bestBid", EncodingUtil.bigDecimalToString(value.getBestBid()));
    }
    if (value.getBestAskSize() != null){
      gen.writeStringField("bestAskSize", EncodingUtil.bigDecimalToString(value.getBestAskSize()));
    }
    gen.writeEndObject();
  }
}
