package com.scz.jcex.exchanges.binance.gen.spottrading.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jcex.exchanges.binance.gen.spottrading.pojo.BinanceExecutionReportUserDataStreamMessage;
import com.scz.jcex.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jcex.netutils.serialization.json.JsonParserUtil;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * Parses incoming JSON messages into com.scz.jcex.binance.gen.spottrading.pojo.BinanceExecutionReportUserDataStreamMessage instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jcex.exchanges.binance.gen.spottrading.pojo.BinanceExecutionReportUserDataStreamMessage
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
        msg.setEventTime(parser.nextLongValue(0L));
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
        msg.setOrderQuantity(new BigDecimal(parser.nextTextValue()));
      break;
      case "p":
        msg.setOrderPrice(new BigDecimal(parser.nextTextValue()));
      break;
      case "P":
        msg.setStopPrice(new BigDecimal(parser.nextTextValue()));
      break;
      case "d":
        msg.setTrailingDelta(parser.nextIntValue(0));
      break;
      case "F":
        msg.setIcebergQuantity(new BigDecimal(parser.nextTextValue()));
      break;
      case "g":
        msg.setOrderListId(parser.nextIntValue(0));
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
        msg.setLastExecQty(new BigDecimal(parser.nextTextValue()));
      break;
      case "z":
        msg.setCumQty(new BigDecimal(parser.nextTextValue()));
      break;
      case "L":
        msg.setLastExecPrice(new BigDecimal(parser.nextTextValue()));
      break;
      case "n":
        msg.setCommissionAmount(parser.nextIntValue(0));
      break;
      case "N":
        msg.setComissionAsset(parser.nextTextValue());
      break;
      case "T":
        msg.setTransactionTime(parser.nextLongValue(0L));
      break;
      case "t":
        msg.setTradeID(parser.nextLongValue(0));
      break;
      case "v":
        msg.setPreventedMatchID(parser.nextLongValue(0));
      break;
      case "I":
        msg.setIgnore0(parser.nextLongValue(0));
      break;
      case "w":
        msg.setOrderOnBook(parser.nextBooleanValue());
      break;
      case "m":
        msg.setTradeMakerSide(parser.nextBooleanValue());
      break;
      case "M":
        msg.setIgnore1(parser.nextBooleanValue());
      break;
      case "O":
        msg.setOrderCreationTime(parser.nextLongValue(0L));
      break;
      case "Z":
        msg.setCumQuoteQty(new BigDecimal(parser.nextTextValue()));
      break;
      case "Y":
        msg.setLastQuoteQty(new BigDecimal(parser.nextTextValue()));
      break;
      case "Q":
        msg.setQuoteQty(new BigDecimal(parser.nextTextValue()));
      break;
      case "D":
        msg.setTrailingTime(parser.nextLongValue(0L));
      break;
      case "j":
        msg.setStrategyID(parser.nextLongValue(0));
      break;
      case "J":
        msg.setStrategyType(parser.nextLongValue(0));
      break;
      case "W":
        msg.setWorkingTime(parser.nextLongValue(0L));
      break;
      case "V":
        msg.setSelfTradePreventionMode(parser.nextTextValue());
      break;
      case "u":
        msg.setTradeGroupId(parser.nextLongValue(0));
      break;
      case "U":
        msg.setCounterOrderId(parser.nextLongValue(0));
      break;
      case "A":
        msg.setPreventedQty(new BigDecimal(parser.nextTextValue()));
      break;
      case "B":
        msg.setLastPreventedQty(new BigDecimal(parser.nextTextValue()));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
