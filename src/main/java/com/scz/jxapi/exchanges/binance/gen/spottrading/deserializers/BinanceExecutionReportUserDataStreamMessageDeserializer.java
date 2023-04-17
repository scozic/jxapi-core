package com.scz.jxapi.exchanges.binance.gen.spottrading.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jxapi.exchanges.binance.gen.spottrading.pojo.BinanceExecutionReportUserDataStreamMessage;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jxapi.netutils.serialization.json.JsonParserUtil;
import java.io.IOException;
import static com.scz.jxapi.util.EncodingUtil.readNextBigDecimal;
import static com.scz.jxapi.util.EncodingUtil.readNextInteger;
import static com.scz.jxapi.util.EncodingUtil.readNextLong;

/**
 * Parses incoming JSON messages into com.scz.jxapi.exchanges.binance.gen.spottrading.pojo.BinanceExecutionReportUserDataStreamMessage instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jxapi.exchanges.binance.gen.spottrading.pojo.BinanceExecutionReportUserDataStreamMessage
 */
public class BinanceExecutionReportUserDataStreamMessageDeserializer extends AbstractJsonMessageDeserializer<BinanceExecutionReportUserDataStreamMessage> {
  
  @Override
  public BinanceExecutionReportUserDataStreamMessage deserialize(JsonParser parser) throws IOException {
    BinanceExecutionReportUserDataStreamMessage msg = new BinanceExecutionReportUserDataStreamMessage();
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
      case "c":
        msg.setClientOrderID(parser.nextTextValue());
      break;
      case "S":
        msg.setSide(parser.nextTextValue());
      break;
      case "o":
        msg.setOrderType(parser.nextTextValue());
      break;
      case "f":
        msg.setTimeInForce(parser.nextTextValue());
      break;
      case "q":
        msg.setOrderQuantity(readNextBigDecimal(parser));
      break;
      case "p":
        msg.setOrderPrice(readNextBigDecimal(parser));
      break;
      case "P":
        msg.setStopPrice(readNextBigDecimal(parser));
      break;
      case "d":
        msg.setTrailingDelta(readNextInteger(parser));
      break;
      case "F":
        msg.setIcebergQuantity(readNextBigDecimal(parser));
      break;
      case "g":
        msg.setOrderListId(readNextInteger(parser));
      break;
      case "C":
        msg.setOrigClientOrderID(parser.nextTextValue());
      break;
      case "x":
        msg.setCurrentExecutionType(parser.nextTextValue());
      break;
      case "X":
        msg.setCurrentOrderStatus(parser.nextTextValue());
      break;
      case "r":
        msg.setOrderRejectReason(parser.nextTextValue());
      break;
      case "i":
        msg.setOrderID(parser.nextTextValue());
      break;
      case "l":
        msg.setLastExecQty(readNextBigDecimal(parser));
      break;
      case "z":
        msg.setCumQty(readNextBigDecimal(parser));
      break;
      case "L":
        msg.setLastExecPrice(readNextBigDecimal(parser));
      break;
      case "n":
        msg.setCommissionAmount(readNextInteger(parser));
      break;
      case "N":
        msg.setComissionAsset(parser.nextTextValue());
      break;
      case "T":
        msg.setTransactionTime(readNextLong(parser));
      break;
      case "t":
        msg.setTradeID(readNextLong(parser));
      break;
      case "v":
        msg.setPreventedMatchID(readNextLong(parser));
      break;
      case "I":
        msg.setIgnore0(readNextLong(parser));
      break;
      case "w":
        msg.setOrderOnBook(Boolean.valueOf(parser.nextBooleanValue()));
      break;
      case "m":
        msg.setTradeMakerSide(Boolean.valueOf(parser.nextBooleanValue()));
      break;
      case "M":
        msg.setIgnore1(Boolean.valueOf(parser.nextBooleanValue()));
      break;
      case "O":
        msg.setOrderCreationTime(readNextLong(parser));
      break;
      case "Z":
        msg.setCumQuoteQty(readNextBigDecimal(parser));
      break;
      case "Y":
        msg.setLastQuoteQty(readNextBigDecimal(parser));
      break;
      case "Q":
        msg.setQuoteQty(readNextBigDecimal(parser));
      break;
      case "D":
        msg.setTrailingTime(readNextLong(parser));
      break;
      case "j":
        msg.setStrategyID(readNextLong(parser));
      break;
      case "J":
        msg.setStrategyType(readNextLong(parser));
      break;
      case "W":
        msg.setWorkingTime(readNextLong(parser));
      break;
      case "V":
        msg.setSelfTradePreventionMode(parser.nextTextValue());
      break;
      case "u":
        msg.setTradeGroupId(readNextLong(parser));
      break;
      case "U":
        msg.setCounterOrderId(readNextLong(parser));
      break;
      case "A":
        msg.setPreventedQty(readNextBigDecimal(parser));
      break;
      case "B":
        msg.setLastPreventedQty(readNextBigDecimal(parser));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
