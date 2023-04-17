package com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGetTickerResponseData;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jxapi.netutils.serialization.json.JsonParserUtil;

import static com.scz.jxapi.util.EncodingUtil.readNextBigDecimal;
import static com.scz.jxapi.util.EncodingUtil.readNextLong;

import java.io.IOException;

/**
 * Parses incoming JSON messages into com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGetTickerResponseData instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGetTickerResponseData
 */
public class KucoinGetTickerResponseDataDeserializer extends AbstractJsonMessageDeserializer<KucoinGetTickerResponseData> {
  
  @Override
  public KucoinGetTickerResponseData deserialize(JsonParser parser) throws IOException {
    KucoinGetTickerResponseData msg = new KucoinGetTickerResponseData();
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
      case "time":
        msg.setTime(readNextLong(parser));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
