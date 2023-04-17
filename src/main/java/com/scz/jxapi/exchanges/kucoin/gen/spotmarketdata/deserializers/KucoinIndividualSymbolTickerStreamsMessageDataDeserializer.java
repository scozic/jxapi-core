package com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinIndividualSymbolTickerStreamsMessageData;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jxapi.netutils.serialization.json.JsonParserUtil;
import java.io.IOException;
import static com.scz.jxapi.util.EncodingUtil.readNextBigDecimal;

/**
 * Parses incoming JSON messages into com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinIndividualSymbolTickerStreamsMessageData instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinIndividualSymbolTickerStreamsMessageData
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
        msg.setBestAsk(readNextBigDecimal(parser));
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
      case "bestBid":
        msg.setBestBid(readNextBigDecimal(parser));
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
