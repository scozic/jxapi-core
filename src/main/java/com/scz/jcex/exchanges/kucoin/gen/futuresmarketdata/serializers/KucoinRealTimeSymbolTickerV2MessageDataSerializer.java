package com.scz.jcex.exchanges.kucoin.gen.futuresmarketdata.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.exchanges.kucoin.gen.futuresmarketdata.pojo.KucoinRealTimeSymbolTickerV2MessageData;
import com.scz.jcex.util.EncodingUtil;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.exchanges.kucoin.gen.futuresmarketdata.pojo.KucoinRealTimeSymbolTickerV2MessageData
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinRealTimeSymbolTickerV2MessageData
 */
public class KucoinRealTimeSymbolTickerV2MessageDataSerializer extends StdSerializer<KucoinRealTimeSymbolTickerV2MessageData> {
  public KucoinRealTimeSymbolTickerV2MessageDataSerializer() {
    super(KucoinRealTimeSymbolTickerV2MessageData.class);
  }
  
  @Override
  public void serialize(KucoinRealTimeSymbolTickerV2MessageData value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getSymbol() != null){
      gen.writeStringField("symbol", String.valueOf(value.getSymbol()));
    }
    if (value.getBestAskPrice() != null){
      gen.writeStringField("bestAskPrice", EncodingUtil.bigDecimalToString(value.getBestAskPrice()));
    }
    if (value.getBestBidSize() != null){
      gen.writeStringField("bestBidSize", EncodingUtil.bigDecimalToString(value.getBestBidSize()));
    }
    if (value.getBestBidPrice() != null){
      gen.writeStringField("bestBidPrice", EncodingUtil.bigDecimalToString(value.getBestBidPrice()));
    }
    if (value.getBestAskSize() != null){
      gen.writeStringField("bestAskSize", EncodingUtil.bigDecimalToString(value.getBestAskSize()));
    }
    gen.writeEndObject();
  }
}
