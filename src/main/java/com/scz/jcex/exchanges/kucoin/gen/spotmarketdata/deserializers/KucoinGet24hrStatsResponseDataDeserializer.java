package com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGet24hrStatsResponseData;
import com.scz.jcex.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jcex.netutils.serialization.json.JsonParserUtil;
import java.io.IOException;
import static com.scz.jcex.util.EncodingUtil.readNextBigDecimal;
import static com.scz.jcex.util.EncodingUtil.readNextLong;

/**
 * Parses incoming JSON messages into com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGet24hrStatsResponseData instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGet24hrStatsResponseData
 */
public class KucoinGet24hrStatsResponseDataDeserializer extends AbstractJsonMessageDeserializer<KucoinGet24hrStatsResponseData> {
  
  @Override
  public KucoinGet24hrStatsResponseData deserialize(JsonParser parser) throws IOException {
    KucoinGet24hrStatsResponseData msg = new KucoinGet24hrStatsResponseData();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      case "time":
        msg.setTime(readNextLong(parser));
      break;
      case "symbol":
        msg.setSymbol(parser.nextTextValue());
      break;
      case "buy":
        msg.setBuy(readNextBigDecimal(parser));
      break;
      case "sell":
        msg.setSell(readNextBigDecimal(parser));
      break;
      case "changeRate":
        msg.setChangeRate(readNextBigDecimal(parser));
      break;
      case "changePrice":
        msg.setChangePrice(readNextBigDecimal(parser));
      break;
      case "high":
        msg.setHigh(readNextBigDecimal(parser));
      break;
      case "low":
        msg.setLow(readNextBigDecimal(parser));
      break;
      case "vol":
        msg.setVol(readNextBigDecimal(parser));
      break;
      case "volValue":
        msg.setVolValue(readNextBigDecimal(parser));
      break;
      case "last":
        msg.setLast(readNextBigDecimal(parser));
      break;
      case "averagePrice":
        msg.setAveragePrice(readNextBigDecimal(parser));
      break;
      case "takerFeeRate":
        msg.setTakerFeeRate(readNextBigDecimal(parser));
      break;
      case "makerFeeRate":
        msg.setMakerFeeRate(readNextBigDecimal(parser));
      break;
      case "takerCoefficient":
        msg.setTakerCoefficient(readNextBigDecimal(parser));
      break;
      case "makerCoefficient":
        msg.setMakerCoefficient(readNextBigDecimal(parser));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
