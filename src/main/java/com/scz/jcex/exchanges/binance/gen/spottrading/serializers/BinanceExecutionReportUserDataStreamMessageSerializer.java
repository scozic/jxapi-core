package com.scz.jcex.exchanges.binance.gen.spottrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.exchanges.binance.gen.spottrading.pojo.BinanceExecutionReportUserDataStreamMessage;
import com.scz.jcex.util.EncodingUtil;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.binance.gen.spottrading.pojo.BinanceExecutionReportUserDataStreamMessage
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
    gen.writeStringField("e", String.valueOf(value.getEventType()));
    gen.writeNumberField("E", value.getEventTime());
    gen.writeStringField("s", String.valueOf(value.getSymbol()));
    gen.writeStringField("c", String.valueOf(value.getClientOrderID()));
    gen.writeStringField("S", String.valueOf(value.getSide()));
    gen.writeStringField("o", String.valueOf(value.getOrderType()));
    gen.writeStringField("f", String.valueOf(value.getTimeInForce()));
    gen.writeStringField("q", EncodingUtil.bigDecimalToString(value.getOrderQuantity()));
    gen.writeStringField("p", EncodingUtil.bigDecimalToString(value.getOrderPrice()));
    gen.writeStringField("P", EncodingUtil.bigDecimalToString(value.getStopPrice()));
    gen.writeNumberField("d", value.getTrailingDelta());
    gen.writeStringField("F", EncodingUtil.bigDecimalToString(value.getIcebergQuantity()));
    gen.writeNumberField("g", value.getOrderListId());
    gen.writeStringField("C", String.valueOf(value.getOrigClientOrderID()));
    gen.writeStringField("x", String.valueOf(value.getCurrentExecutionType()));
    gen.writeStringField("X", String.valueOf(value.getCurrentOrderStatus()));
    gen.writeStringField("r", String.valueOf(value.getOrderRejectReason()));
    gen.writeStringField("i", String.valueOf(value.getOrderID()));
    gen.writeStringField("l", EncodingUtil.bigDecimalToString(value.getLastExecQty()));
    gen.writeStringField("z", EncodingUtil.bigDecimalToString(value.getCumQty()));
    gen.writeStringField("L", EncodingUtil.bigDecimalToString(value.getLastExecPrice()));
    gen.writeNumberField("n", value.getCommissionAmount());
    gen.writeStringField("N", String.valueOf(value.getComissionAsset()));
    gen.writeNumberField("T", value.getTransactionTime());
    gen.writeNumberField("t", value.getTradeID());
    gen.writeNumberField("v", value.getPreventedMatchID());
    gen.writeNumberField("I", value.getIgnore0());
    gen.writeBooleanField("w", value.isOrderOnBook());
    gen.writeBooleanField("m", value.isTradeMakerSide());
    gen.writeBooleanField("M", value.isIgnore1());
    gen.writeNumberField("O", value.getOrderCreationTime());
    gen.writeStringField("Z", EncodingUtil.bigDecimalToString(value.getCumQuoteQty()));
    gen.writeStringField("Y", EncodingUtil.bigDecimalToString(value.getLastQuoteQty()));
    gen.writeStringField("Q", EncodingUtil.bigDecimalToString(value.getQuoteQty()));
    gen.writeNumberField("D", value.getTrailingTime());
    gen.writeNumberField("j", value.getStrategyID());
    gen.writeNumberField("J", value.getStrategyType());
    gen.writeNumberField("W", value.getWorkingTime());
    gen.writeStringField("V", String.valueOf(value.getSelfTradePreventionMode()));
    gen.writeNumberField("u", value.getTradeGroupId());
    gen.writeNumberField("U", value.getCounterOrderId());
    gen.writeStringField("A", EncodingUtil.bigDecimalToString(value.getPreventedQty()));
    gen.writeStringField("B", EncodingUtil.bigDecimalToString(value.getLastPreventedQty()));
    gen.writeEndObject();
  }
}
