package com.scz.jcex.binance.gen.spottrading.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jcex.binance.gen.spottrading.pojo.BinanceExecutionReportUserDataStreamMessage;
import com.scz.jcex.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jcex.netutils.serialization.json.JsonParserUtil;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * Parses incoming JSON messages into com.scz.jcex.binance.gen.spottrading.pojo.BinanceExecutionReportUserDataStreamMessage instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jcex.binance.gen.spottrading.pojo.BinanceExecutionReportUserDataStreamMessage
 */
public class BinanceExecutionReportUserDataStreamMessageDeserializer extends AbstractJsonMessageDeserializer<BinanceExecutionReportUserDataStreamMessage> {
  
  @Override
  public BinanceExecutionReportUserDataStreamMessage deserialize(JsonParser parser) throws IOException {
    BinanceExecutionReportUserDataStreamMessage msg = new BinanceExecutionReportUserDataStreamMessage();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      case "e":
        msg.sete(parser.nextTextValue());
      break;
      case "E":
        msg.setE(parser.nextLongValue(0L));
      break;
      case "s":
        msg.sets(parser.nextTextValue());
      break;
      case "c":
        msg.setc(parser.nextTextValue());
      break;
      case "S":
        msg.setS(parser.nextTextValue());
      break;
      case "o":
        msg.seto(parser.nextTextValue());
      break;
      case "f":
        msg.setf(parser.nextTextValue());
      break;
      case "q":
        msg.setq(new BigDecimal(parser.nextTextValue()));
      break;
      case "p":
        msg.setp(new BigDecimal(parser.nextTextValue()));
      break;
      case "P":
        msg.setP(new BigDecimal(parser.nextTextValue()));
      break;
      case "d":
        msg.setd(parser.nextIntValue(0));
      break;
      case "F":
        msg.setF(new BigDecimal(parser.nextTextValue()));
      break;
      case "g":
        msg.setG(parser.nextIntValue(0));
      break;
      case "C":
        msg.setC(parser.nextTextValue());
      break;
      case "x":
        msg.setx(parser.nextTextValue());
      break;
      case "X":
        msg.setX(parser.nextTextValue());
      break;
      case "r":
        msg.setR(parser.nextTextValue());
      break;
      case "i":
        msg.seti(parser.nextTextValue());
      break;
      case "l":
        msg.setl(new BigDecimal(parser.nextTextValue()));
      break;
      case "z":
        msg.setz(new BigDecimal(parser.nextTextValue()));
      break;
      case "L":
        msg.setL(new BigDecimal(parser.nextTextValue()));
      break;
      case "n":
        msg.setn(parser.nextIntValue(0));
      break;
      case "N":
        msg.setN(parser.nextTextValue());
      break;
      case "T":
        msg.setT(parser.nextLongValue(0L));
      break;
      case "t":
        msg.sett(parser.nextLongValue(0));
      break;
      case "v":
        msg.setv(parser.nextLongValue(0));
      break;
      case "I":
        msg.setI(parser.nextLongValue(0));
      break;
      case "w":
        msg.setw(parser.nextBooleanValue());
      break;
      case "m":
        msg.setm(parser.nextBooleanValue());
      break;
      case "M":
        msg.setM(parser.nextBooleanValue());
      break;
      case "O":
        msg.setO(parser.nextLongValue(0L));
      break;
      case "Z":
        msg.setZ(new BigDecimal(parser.nextTextValue()));
      break;
      case "Y":
        msg.setY(new BigDecimal(parser.nextTextValue()));
      break;
      case "Q":
        msg.setQ(new BigDecimal(parser.nextTextValue()));
      break;
      case "D":
        msg.setD(parser.nextLongValue(0L));
      break;
      case "j":
        msg.setj(parser.nextLongValue(0));
      break;
      case "J":
        msg.setJ(parser.nextLongValue(0));
      break;
      case "W":
        msg.setW(parser.nextLongValue(0L));
      break;
      case "V":
        msg.setV(parser.nextTextValue());
      break;
      case "u":
        msg.setu(parser.nextLongValue(0));
      break;
      case "U":
        msg.setU(parser.nextLongValue(0));
      break;
      case "A":
        msg.setA(new BigDecimal(parser.nextTextValue()));
      break;
      case "B":
        msg.setB(new BigDecimal(parser.nextTextValue()));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
