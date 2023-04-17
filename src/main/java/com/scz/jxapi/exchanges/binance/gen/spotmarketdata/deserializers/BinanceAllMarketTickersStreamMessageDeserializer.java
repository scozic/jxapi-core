package com.scz.jxapi.exchanges.binance.gen.spotmarketdata.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jxapi.exchanges.binance.gen.spotmarketdata.pojo.BinanceAllMarketTickersStreamMessage;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jxapi.netutils.serialization.json.JsonParserUtil;
import java.io.IOException;
import static com.scz.jxapi.util.EncodingUtil.readNextBigDecimal;
import static com.scz.jxapi.util.EncodingUtil.readNextLong;

/**
 * Parses incoming JSON messages into com.scz.jxapi.exchanges.binance.gen.spotmarketdata.pojo.BinanceAllMarketTickersStreamMessage instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jxapi.exchanges.binance.gen.spotmarketdata.pojo.BinanceAllMarketTickersStreamMessage
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
        msg.setEventTime(readNextLong(parser));
      break;
      case "s":
        msg.setSymbol(parser.nextTextValue());
      break;
      case "p":
        msg.setPriceChange(readNextBigDecimal(parser));
      break;
      case "P":
        msg.setPriceChangePercent(readNextBigDecimal(parser));
      break;
      case "o":
        msg.setOpenPrice(readNextBigDecimal(parser));
      break;
      case "h":
        msg.setHighPrice(readNextBigDecimal(parser));
      break;
      case "l":
        msg.setLowPrice(readNextBigDecimal(parser));
      break;
      case "c":
        msg.setLastPrice(readNextBigDecimal(parser));
      break;
      case "w":
        msg.setWeightedAvgPrice(readNextBigDecimal(parser));
      break;
      case "v":
        msg.setBaseAssetVolume(readNextBigDecimal(parser));
      break;
      case "q":
        msg.setQuoteAssetVolume(readNextBigDecimal(parser));
      break;
      case "O":
        msg.setOpenTime(readNextLong(parser));
      break;
      case "C":
        msg.setCloseTime(readNextLong(parser));
      break;
      case "F":
        msg.setFirstTradeID(readNextLong(parser));
      break;
      case "L":
        msg.setLastTradeID(readNextLong(parser));
      break;
      case "n":
        msg.setTradeCount(readNextLong(parser));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
