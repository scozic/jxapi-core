package com.scz.jcex.exchanges.binance.gen.spotmarketdata.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jcex.exchanges.binance.gen.spotmarketdata.pojo.BinanceAllMarketTickersStreamMessage;
import com.scz.jcex.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jcex.netutils.serialization.json.JsonParserUtil;
import java.io.IOException;
import static com.scz.jcex.util.EncodingUtil.toBigDecimal;

/**
 * Parses incoming JSON messages into com.scz.jcex.exchanges.binance.gen.spotmarketdata.pojo.BinanceAllMarketTickersStreamMessage instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jcex.exchanges.binance.gen.spotmarketdata.pojo.BinanceAllMarketTickersStreamMessage
 */
public class BinanceAllMarketTickersStreamMessageDeserializer extends AbstractJsonMessageDeserializer<BinanceAllMarketTickersStreamMessage> {
  
  @Override
  public BinanceAllMarketTickersStreamMessage deserialize(JsonParser parser) throws IOException {
    BinanceAllMarketTickersStreamMessage msg = new BinanceAllMarketTickersStreamMessage();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      case "e":
        msg.setEventType(parser.nextTextValue());
      break;
      case "E":
        msg.setEventTime(parser.nextLongValue(0L));
      break;
      case "s":
        msg.setSymbol(parser.nextTextValue());
      break;
      case "p":
        msg.setPriceChange(toBigDecimal(parser.nextTextValue()));
      break;
      case "P":
        msg.setPriceChangePercent(toBigDecimal(parser.nextTextValue()));
      break;
      case "o":
        msg.setOpenPrice(toBigDecimal(parser.nextTextValue()));
      break;
      case "h":
        msg.setHighPrice(toBigDecimal(parser.nextTextValue()));
      break;
      case "l":
        msg.setLowPrice(toBigDecimal(parser.nextTextValue()));
      break;
      case "c":
        msg.setLastPrice(toBigDecimal(parser.nextTextValue()));
      break;
      case "w":
        msg.setWeightedAvgPrice(toBigDecimal(parser.nextTextValue()));
      break;
      case "v":
        msg.setBaseAssetVolume(toBigDecimal(parser.nextTextValue()));
      break;
      case "q":
        msg.setQuoteAssetVolume(toBigDecimal(parser.nextTextValue()));
      break;
      case "O":
        msg.setOpenTime(parser.nextLongValue(0L));
      break;
      case "C":
        msg.setCloseTime(parser.nextLongValue(0L));
      break;
      case "F":
        msg.setFirstTradeID(Long.valueOf(parser.nextLongValue(0)));
      break;
      case "L":
        msg.setLastTradeID(Long.valueOf(parser.nextLongValue(0)));
      break;
      case "n":
        msg.setTradeCount(Long.valueOf(parser.nextLongValue(0)));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
