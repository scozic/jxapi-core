package com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGetAllTickersResponseDataTicker;
import com.scz.jcex.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jcex.netutils.serialization.json.JsonParserUtil;
import java.io.IOException;
import static com.scz.jcex.util.EncodingUtil.toBigDecimal;

/**
 * Parses incoming JSON messages into com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGetAllTickersResponseDataTicker instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGetAllTickersResponseDataTicker
 */
public class KucoinGetAllTickersResponseDataTickerDeserializer extends AbstractJsonMessageDeserializer<KucoinGetAllTickersResponseDataTicker> {
  
  @Override
  public KucoinGetAllTickersResponseDataTicker deserialize(JsonParser parser) throws IOException {
    KucoinGetAllTickersResponseDataTicker msg = new KucoinGetAllTickersResponseDataTicker();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      case "symbol":
        msg.setSymbol(parser.nextTextValue());
      break;
      case "symbolName":
        msg.setSymbolName(parser.nextTextValue());
      break;
      case "buy":
        msg.setBuy(toBigDecimal(parser.nextTextValue()));
      break;
      case "sell":
        msg.setSell(toBigDecimal(parser.nextTextValue()));
      break;
      case "changeRate":
        msg.setChangeRate(toBigDecimal(parser.nextTextValue()));
      break;
      case "changePrice":
        msg.setChangePrice(toBigDecimal(parser.nextTextValue()));
      break;
      case "high":
        msg.setHigh(toBigDecimal(parser.nextTextValue()));
      break;
      case "low":
        msg.setLow(toBigDecimal(parser.nextTextValue()));
      break;
      case "vol":
        msg.setVol(toBigDecimal(parser.nextTextValue()));
      break;
      case "volValue":
        msg.setVolValue(toBigDecimal(parser.nextTextValue()));
      break;
      case "last":
        msg.setLast(toBigDecimal(parser.nextTextValue()));
      break;
      case "averagePrice":
        msg.setAveragePrice(toBigDecimal(parser.nextTextValue()));
      break;
      case "takerFeeRate":
        msg.setTakerFeeRate(toBigDecimal(parser.nextTextValue()));
      break;
      case "makerFeeRate":
        msg.setMakerFeeRate(toBigDecimal(parser.nextTextValue()));
      break;
      case "takerCoefficient":
        msg.setTakerCoefficient(toBigDecimal(parser.nextTextValue()));
      break;
      case "makerCoefficient":
        msg.setMakerCoefficient(toBigDecimal(parser.nextTextValue()));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
