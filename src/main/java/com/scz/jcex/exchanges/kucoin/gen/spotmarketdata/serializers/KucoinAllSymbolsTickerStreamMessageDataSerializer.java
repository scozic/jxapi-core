package com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinAllSymbolsTickerStreamMessageData;
import com.scz.jcex.util.EncodingUtil;
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
    gen.writeStringField("sequence", String.valueOf(value.getSequence()));
    gen.writeStringField("bestAsk", EncodingUtil.bigDecimalToString(value.getBestAsk()));
    gen.writeStringField("size", EncodingUtil.bigDecimalToString(value.getSize()));
    gen.writeStringField("price", EncodingUtil.bigDecimalToString(value.getPrice()));
    gen.writeStringField("bestBidSize", EncodingUtil.bigDecimalToString(value.getBestBidSize()));
    gen.writeStringField("bestBid", EncodingUtil.bigDecimalToString(value.getBestBid()));
    gen.writeStringField("bestAskSize", EncodingUtil.bigDecimalToString(value.getBestAskSize()));
    gen.writeEndObject();
  }
}
