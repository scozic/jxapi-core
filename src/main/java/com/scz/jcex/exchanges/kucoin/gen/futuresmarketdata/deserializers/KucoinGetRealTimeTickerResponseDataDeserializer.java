package com.scz.jcex.exchanges.kucoin.gen.futuresmarketdata.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jcex.exchanges.kucoin.gen.futuresmarketdata.pojo.KucoinGetRealTimeTickerResponseData;
import com.scz.jcex.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jcex.netutils.serialization.json.JsonParserUtil;
import java.io.IOException;
import static com.scz.jcex.util.EncodingUtil.readNextBigDecimal;
import static com.scz.jcex.util.EncodingUtil.readNextLong;

/**
 * Parses incoming JSON messages into com.scz.jcex.exchanges.kucoin.gen.futuresmarketdata.pojo.KucoinGetRealTimeTickerResponseData instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jcex.exchanges.kucoin.gen.futuresmarketdata.pojo.KucoinGetRealTimeTickerResponseData
 */
public class KucoinGetRealTimeTickerResponseDataDeserializer extends AbstractJsonMessageDeserializer<KucoinGetRealTimeTickerResponseData> {
  
  @Override
  public KucoinGetRealTimeTickerResponseData deserialize(JsonParser parser) throws IOException {
    KucoinGetRealTimeTickerResponseData msg = new KucoinGetRealTimeTickerResponseData();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      case "sequence":
        msg.setSequence(readNextLong(parser));
      break;
      case "symbol":
        msg.setSymbol(parser.nextTextValue());
      break;
      case "side":
        msg.setSide(parser.nextTextValue());
      break;
      case "size":
        msg.setSize(readNextBigDecimal(parser));
      break;
      case "price":
        msg.setPrice(readNextBigDecimal(parser));
      break;
      case "bestBidSize":
        msg.setBestBidSize(readNextBigDecimal(parser));
      break;
      case "bestBidPrice":
        msg.setBestBidPrice(readNextBigDecimal(parser));
      break;
      case "bestAskPrice":
        msg.setBestAskPrice(readNextBigDecimal(parser));
      break;
      case "bestAskSize":
        msg.setBestAskSize(readNextBigDecimal(parser));
      break;
      case "tradeId":
        msg.setTradeId(parser.nextTextValue());
      break;
      case "ts":
        msg.setTs(readNextLong(parser));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
