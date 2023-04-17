package com.scz.jxapi.exchanges.binance.gen.spottrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jxapi.exchanges.binance.gen.spottrading.pojo.BinanceExecutionReportUserDataStreamMessage;
import com.scz.jxapi.util.EncodingUtil;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jxapi.exchanges.binance.gen.spottrading.pojo.BinanceExecutionReportUserDataStreamMessage
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see BinanceExecutionReportUserDataStreamMessage
 */
public class BinanceExecutionReportUserDataStreamMessageSerializer extends StdSerializer<BinanceExecutionReportUserDataStreamMessage> {
  public BinanceExecutionReportUserDataStreamMessageSerializer() {
    super(BinanceExecutionReportUserDataStreamMessage.class);
  }
  
  @Override
  public void serialize(BinanceExecutionReportUserDataStreamMessage value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getEventType() != null){
      gen.writeStringField("e", String.valueOf(value.getEventType()));
    }
    if (value.getEventTime() != null){
      gen.writeNumberField("E", value.getEventTime());
    }
    if (value.getSymbol() != null){
      gen.writeStringField("s", String.valueOf(value.getSymbol()));
    }
    if (value.getClientOrderID() != null){
      gen.writeStringField("c", String.valueOf(value.getClientOrderID()));
    }
    if (value.getSide() != null){
      gen.writeStringField("S", String.valueOf(value.getSide()));
    }
    if (value.getOrderType() != null){
      gen.writeStringField("o", String.valueOf(value.getOrderType()));
    }
    if (value.getTimeInForce() != null){
      gen.writeStringField("f", String.valueOf(value.getTimeInForce()));
    }
    if (value.getOrderQuantity() != null){
      gen.writeStringField("q", EncodingUtil.bigDecimalToString(value.getOrderQuantity()));
    }
    if (value.getOrderPrice() != null){
      gen.writeStringField("p", EncodingUtil.bigDecimalToString(value.getOrderPrice()));
    }
    if (value.getStopPrice() != null){
      gen.writeStringField("P", EncodingUtil.bigDecimalToString(value.getStopPrice()));
    }
    if (value.getTrailingDelta() != null){
      gen.writeNumberField("d", value.getTrailingDelta());
    }
    if (value.getIcebergQuantity() != null){
      gen.writeStringField("F", EncodingUtil.bigDecimalToString(value.getIcebergQuantity()));
    }
    if (value.getOrderListId() != null){
      gen.writeNumberField("g", value.getOrderListId());
    }
    if (value.getOrigClientOrderID() != null){
      gen.writeStringField("C", String.valueOf(value.getOrigClientOrderID()));
    }
    if (value.getCurrentExecutionType() != null){
      gen.writeStringField("x", String.valueOf(value.getCurrentExecutionType()));
    }
    if (value.getCurrentOrderStatus() != null){
      gen.writeStringField("X", String.valueOf(value.getCurrentOrderStatus()));
    }
    if (value.getOrderRejectReason() != null){
      gen.writeStringField("r", String.valueOf(value.getOrderRejectReason()));
    }
    if (value.getOrderID() != null){
      gen.writeStringField("i", String.valueOf(value.getOrderID()));
    }
    if (value.getLastExecQty() != null){
      gen.writeStringField("l", EncodingUtil.bigDecimalToString(value.getLastExecQty()));
    }
    if (value.getCumQty() != null){
      gen.writeStringField("z", EncodingUtil.bigDecimalToString(value.getCumQty()));
    }
    if (value.getLastExecPrice() != null){
      gen.writeStringField("L", EncodingUtil.bigDecimalToString(value.getLastExecPrice()));
    }
    if (value.getCommissionAmount() != null){
      gen.writeNumberField("n", value.getCommissionAmount());
    }
    if (value.getComissionAsset() != null){
      gen.writeStringField("N", String.valueOf(value.getComissionAsset()));
    }
    if (value.getTransactionTime() != null){
      gen.writeNumberField("T", value.getTransactionTime());
    }
    if (value.getTradeID() != null){
      gen.writeNumberField("t", value.getTradeID());
    }
    if (value.getPreventedMatchID() != null){
      gen.writeNumberField("v", value.getPreventedMatchID());
    }
    if (value.getIgnore0() != null){
      gen.writeNumberField("I", value.getIgnore0());
    }
    if (value.isOrderOnBook() != null){
      gen.writeBooleanField("w", value.isOrderOnBook());
    }
    if (value.isTradeMakerSide() != null){
      gen.writeBooleanField("m", value.isTradeMakerSide());
    }
    if (value.isIgnore1() != null){
      gen.writeBooleanField("M", value.isIgnore1());
    }
    if (value.getOrderCreationTime() != null){
      gen.writeNumberField("O", value.getOrderCreationTime());
    }
    if (value.getCumQuoteQty() != null){
      gen.writeStringField("Z", EncodingUtil.bigDecimalToString(value.getCumQuoteQty()));
    }
    if (value.getLastQuoteQty() != null){
      gen.writeStringField("Y", EncodingUtil.bigDecimalToString(value.getLastQuoteQty()));
    }
    if (value.getQuoteQty() != null){
      gen.writeStringField("Q", EncodingUtil.bigDecimalToString(value.getQuoteQty()));
    }
    if (value.getTrailingTime() != null){
      gen.writeNumberField("D", value.getTrailingTime());
    }
    if (value.getStrategyID() != null){
      gen.writeNumberField("j", value.getStrategyID());
    }
    if (value.getStrategyType() != null){
      gen.writeNumberField("J", value.getStrategyType());
    }
    if (value.getWorkingTime() != null){
      gen.writeNumberField("W", value.getWorkingTime());
    }
    if (value.getSelfTradePreventionMode() != null){
      gen.writeStringField("V", String.valueOf(value.getSelfTradePreventionMode()));
    }
    if (value.getTradeGroupId() != null){
      gen.writeNumberField("u", value.getTradeGroupId());
    }
    if (value.getCounterOrderId() != null){
      gen.writeNumberField("U", value.getCounterOrderId());
    }
    if (value.getPreventedQty() != null){
      gen.writeStringField("A", EncodingUtil.bigDecimalToString(value.getPreventedQty()));
    }
    if (value.getLastPreventedQty() != null){
      gen.writeStringField("B", EncodingUtil.bigDecimalToString(value.getLastPreventedQty()));
    }
    gen.writeEndObject();
  }
}
