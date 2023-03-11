package com.scz.jcex.exchanges.binance.gen.spotmarketdata.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jcex.exchanges.binance.gen.spotmarketdata.pojo.BinanceAllMarketTickersStreamMessage;
import com.scz.jcex.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jcex.netutils.serialization.json.JsonParserUtil;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * Parses incoming JSON messages into com.scz.jcex.binance.gen.spotmarketdata.pojo.BinanceAllMarketTickersStreamMessage instances
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
        msg.setPriceChange(new BigDecimal(parser.nextTextValue()));
      break;
      case "P":
        msg.setPriceChangePercent(new BigDecimal(parser.nextTextValue()));
      break;
      case "o":
        msg.setOpenPrice(new BigDecimal(parser.nextTextValue()));
      break;
      case "h":
        msg.setHighPrice(new BigDecimal(parser.nextTextValue()));
      break;
      case "l":
        msg.setLowPrice(new BigDecimal(parser.nextTextValue()));
      break;
      case "c":
        msg.setLastPrice(new BigDecimal(parser.nextTextValue()));
      break;
      case "w":
        msg.setWeightedAvgPrice(new BigDecimal(parser.nextTextValue()));
      break;
      case "v":
        msg.setBaseAssetVolume(new BigDecimal(parser.nextTextValue()));
      break;
      case "q":
        msg.setQuoteAssetVolume(new BigDecimal(parser.nextTextValue()));
      break;
      case "O":
        msg.setOpenTime(parser.nextLongValue(0L));
      break;
      case "C":
        msg.setCloseTime(parser.nextLongValue(0L));
      break;
      case "F":
        msg.setFirstTradeID(parser.nextLongValue(0));
      break;
      case "L":
        msg.setLastTradeID(parser.nextLongValue(0));
      break;
      case "n":
        msg.setTradeCount(parser.nextLongValue(0));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
