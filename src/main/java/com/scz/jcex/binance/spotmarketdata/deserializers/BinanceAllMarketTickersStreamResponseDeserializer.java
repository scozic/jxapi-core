package com.scz.jcex.binance.spotmarketdata.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jcex.binance.spotmarketdata.pojo.BinanceAllMarketTickersStreamResponse;
import com.scz.jcex.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * Parses incoming JSON messages into com.scz.jcex.binance.spotmarketdata.pojo.BinanceAllMarketTickersStreamResponse instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jcex.binance.spotmarketdata.pojo.BinanceAllMarketTickersStreamResponse
 */
public class BinanceAllMarketTickersStreamResponseDeserializer extends AbstractJsonMessageDeserializer<BinanceAllMarketTickersStreamResponse> {
  
  @Override
  public BinanceAllMarketTickersStreamResponse deserialize(JsonParser parser) throws IOException {
    BinanceAllMarketTickersStreamResponse msg = new BinanceAllMarketTickersStreamResponse();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      case "e":
        msg.sete(parser.nextTextValue());
      break;
      case "E":
        msg.setE(parser.nextLongValue(0L));
      break;
      case "s":
        msg.setS(parser.nextTextValue());
      break;
      case "p":
        msg.setp(new BigDecimal(parser.nextTextValue()));
      break;
      case "P":
        msg.setP(new BigDecimal(parser.nextTextValue()));
      break;
      case "o":
        msg.seto(new BigDecimal(parser.nextTextValue()));
      break;
      case "h":
        msg.setH(new BigDecimal(parser.nextTextValue()));
      break;
      case "l":
        msg.setl(new BigDecimal(parser.nextTextValue()));
      break;
      case "c":
        msg.setc(new BigDecimal(parser.nextTextValue()));
      break;
      case "w":
        msg.setW(new BigDecimal(parser.nextTextValue()));
      break;
      case "v":
        msg.setV(new BigDecimal(parser.nextTextValue()));
      break;
      case "q":
        msg.setQ(new BigDecimal(parser.nextTextValue()));
      break;
      case "O":
        msg.setO(parser.nextLongValue(0L));
      break;
      case "C":
        msg.setC(parser.nextLongValue(0L));
      break;
      case "F":
        msg.setF(parser.nextLongValue(0));
      break;
      case "L":
        msg.setL(parser.nextLongValue(0));
      break;
      case "n":
        msg.setN(parser.nextLongValue(0));
      break;
      default:
      }
    }
    
     return msg;
  }
  
}
