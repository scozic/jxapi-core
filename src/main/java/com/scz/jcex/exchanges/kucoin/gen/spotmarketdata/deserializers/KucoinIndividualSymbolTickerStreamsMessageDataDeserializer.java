package com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinIndividualSymbolTickerStreamsMessageData;
import com.scz.jcex.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jcex.netutils.serialization.json.JsonParserUtil;
import java.io.IOException;
import static com.scz.jcex.util.EncodingUtil.toBigDecimal;

/**
 * Parses incoming JSON messages into com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinIndividualSymbolTickerStreamsMessageData instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinIndividualSymbolTickerStreamsMessageData
 */
public class KucoinIndividualSymbolTickerStreamsMessageDataDeserializer extends AbstractJsonMessageDeserializer<KucoinIndividualSymbolTickerStreamsMessageData> {
  
  @Override
  public KucoinIndividualSymbolTickerStreamsMessageData deserialize(JsonParser parser) throws IOException {
    KucoinIndividualSymbolTickerStreamsMessageData msg = new KucoinIndividualSymbolTickerStreamsMessageData();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      case "sequence":
        msg.setSequence(parser.nextTextValue());
      break;
      case "bestAsk":
        msg.setBestAsk(toBigDecimal(parser.nextTextValue()));
      break;
      case "size":
        msg.setSize(toBigDecimal(parser.nextTextValue()));
      break;
      case "price":
        msg.setPrice(toBigDecimal(parser.nextTextValue()));
      break;
      case "bestBidSize":
        msg.setBestBidSize(toBigDecimal(parser.nextTextValue()));
      break;
      case "bestBid":
        msg.setBestBid(toBigDecimal(parser.nextTextValue()));
      break;
      case "bestAskSize":
        msg.setBestAskSize(toBigDecimal(parser.nextTextValue()));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
