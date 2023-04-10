package com.scz.jcex.exchanges.kucoin.gen.futuresmarketdata.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jcex.exchanges.kucoin.gen.futuresmarketdata.pojo.KucoinRealTimeSymbolTickerV2MessageData;
import com.scz.jcex.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jcex.netutils.serialization.json.JsonParserUtil;
import java.io.IOException;
import static com.scz.jcex.util.EncodingUtil.readNextBigDecimal;

/**
 * Parses incoming JSON messages into com.scz.jcex.exchanges.kucoin.gen.futuresmarketdata.pojo.KucoinRealTimeSymbolTickerV2MessageData instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jcex.exchanges.kucoin.gen.futuresmarketdata.pojo.KucoinRealTimeSymbolTickerV2MessageData
 */
public class KucoinRealTimeSymbolTickerV2MessageDataDeserializer extends AbstractJsonMessageDeserializer<KucoinRealTimeSymbolTickerV2MessageData> {
  
  @Override
  public KucoinRealTimeSymbolTickerV2MessageData deserialize(JsonParser parser) throws IOException {
    KucoinRealTimeSymbolTickerV2MessageData msg = new KucoinRealTimeSymbolTickerV2MessageData();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      case "symbol":
        msg.setSymbol(parser.nextTextValue());
      break;
      case "bestAskPrice":
        msg.setBestAskPrice(readNextBigDecimal(parser));
      break;
      case "bestBidSize":
        msg.setBestBidSize(readNextBigDecimal(parser));
      break;
      case "bestBidPrice":
        msg.setBestBidPrice(readNextBigDecimal(parser));
      break;
      case "bestAskSize":
        msg.setBestAskSize(readNextBigDecimal(parser));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
