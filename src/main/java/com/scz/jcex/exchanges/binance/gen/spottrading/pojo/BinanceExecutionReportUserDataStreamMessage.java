package com.scz.jcex.exchanges.binance.gen.spottrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.exchanges.binance.gen.spottrading.serializers.BinanceExecutionReportUserDataStreamMessageSerializer;
import com.scz.jcex.util.EncodingUtil;
import java.math.BigDecimal;

/**
 * Message disseminated upon subscription to Binance SpotTrading API executionReportUserDataStream websocket endpoint request<br/>User data Stream.<br/>See <a href="https://binance-docs.github.io/apidocs/spot/en/#user-data-streams">API</a><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = BinanceExecutionReportUserDataStreamMessageSerializer.class)
public class BinanceExecutionReportUserDataStreamMessage {
  private String clientOrderID;
  private String comissionAsset;
  private int commissionAmount;
  private long counterOrderId;
  private BigDecimal cumQty;
  private BigDecimal cumQuoteQty;
  private String currentExecutionType;
  private String currentOrderStatus;
  private long eventTime;
  private String eventType;
  private BigDecimal icebergQuantity;
  private long ignore0;
  private boolean ignore1;
  private BigDecimal lastExecPrice;
  private BigDecimal lastExecQty;
  private BigDecimal lastPreventedQty;
  private BigDecimal lastQuoteQty;
  private long orderCreationTime;
  private String orderID;
  private int orderListId;
  private boolean orderOnBook;
  private BigDecimal orderPrice;
  private BigDecimal orderQuantity;
  private String orderRejectReason;
  private String orderType;
  private String origClientOrderID;
  private long preventedMatchID;
  private BigDecimal preventedQty;
  private BigDecimal quoteQty;
  private String selfTradePreventionMode;
  private String side;
  private BigDecimal stopPrice;
  private long strategyID;
  private long strategyType;
  private String symbol;
  private String timeInForce;
  private long tradeGroupId;
  private long tradeID;
  private boolean tradeMakerSide;
  private int trailingDelta;
  private long trailingTime;
  private long transactionTime;
  private long workingTime;
  
  /**
   * @return Client order ID. Message field <strong>c</strong>
   */
  public String getClientOrderID(){
    return clientOrderID;
  }
  
  /**
   * @param clientOrderID Client order ID. Message field <strong>c</strong>
   */
  public void setClientOrderID(String clientOrderID) {
    this.clientOrderID = clientOrderID;
  }
  
  /**
   * @return Commission asset. Message field <strong>N</strong>
   */
  public String getComissionAsset(){
    return comissionAsset;
  }
  
  /**
   * @param comissionAsset Commission asset. Message field <strong>N</strong>
   */
  public void setComissionAsset(String comissionAsset) {
    this.comissionAsset = comissionAsset;
  }
  
  /**
   * @return Commission amount. Message field <strong>n</strong>
   */
  public int getCommissionAmount(){
    return commissionAmount;
  }
  
  /**
   * @param commissionAmount Commission amount. Message field <strong>n</strong>
   */
  public void setCommissionAmount(int commissionAmount) {
    this.commissionAmount = commissionAmount;
  }
  
  /**
   * @return CounterOrderId; This is only visible if the order expired due to STP trigger. Message field <strong>U</strong>
   */
  public long getCounterOrderId(){
    return counterOrderId;
  }
  
  /**
   * @param counterOrderId CounterOrderId; This is only visible if the order expired due to STP trigger. Message field <strong>U</strong>
   */
  public void setCounterOrderId(long counterOrderId) {
    this.counterOrderId = counterOrderId;
  }
  
  /**
   * @return Cumulative filled quantity. Message field <strong>z</strong>
   */
  public BigDecimal getCumQty(){
    return cumQty;
  }
  
  /**
   * @param cumQty Cumulative filled quantity. Message field <strong>z</strong>
   */
  public void setCumQty(BigDecimal cumQty) {
    this.cumQty = cumQty;
  }
  
  /**
   * @return Cumulative quote asset transacted quantity Message field <strong>Z</strong>
   */
  public BigDecimal getCumQuoteQty(){
    return cumQuoteQty;
  }
  
  /**
   * @param cumQuoteQty Cumulative quote asset transacted quantity Message field <strong>Z</strong>
   */
  public void setCumQuoteQty(BigDecimal cumQuoteQty) {
    this.cumQuoteQty = cumQuoteQty;
  }
  
  /**
   * @return Current execution type. Message field <strong>x</strong>
   */
  public String getCurrentExecutionType(){
    return currentExecutionType;
  }
  
  /**
   * @param currentExecutionType Current execution type. Message field <strong>x</strong>
   */
  public void setCurrentExecutionType(String currentExecutionType) {
    this.currentExecutionType = currentExecutionType;
  }
  
  /**
   * @return Current order status. Message field <strong>X</strong>
   */
  public String getCurrentOrderStatus(){
    return currentOrderStatus;
  }
  
  /**
   * @param currentOrderStatus Current order status. Message field <strong>X</strong>
   */
  public void setCurrentOrderStatus(String currentOrderStatus) {
    this.currentOrderStatus = currentOrderStatus;
  }
  
  /**
   * @return Event time. Message field <strong>E</strong>
   */
  public long getEventTime(){
    return eventTime;
  }
  
  /**
   * @param eventTime Event time. Message field <strong>E</strong>
   */
  public void setEventTime(long eventTime) {
    this.eventTime = eventTime;
  }
  
  /**
   * @return Event type. Message field <strong>e</strong>
   */
  public String getEventType(){
    return eventType;
  }
  
  /**
   * @param eventType Event type. Message field <strong>e</strong>
   */
  public void setEventType(String eventType) {
    this.eventType = eventType;
  }
  
  /**
   * @return Iceberg quantity. Message field <strong>F</strong>
   */
  public BigDecimal getIcebergQuantity(){
    return icebergQuantity;
  }
  
  /**
   * @param icebergQuantity Iceberg quantity. Message field <strong>F</strong>
   */
  public void setIcebergQuantity(BigDecimal icebergQuantity) {
    this.icebergQuantity = icebergQuantity;
  }
  
  /**
   * @return Ignore Message field <strong>I</strong>
   */
  public long getIgnore0(){
    return ignore0;
  }
  
  /**
   * @param ignore0 Ignore Message field <strong>I</strong>
   */
  public void setIgnore0(long ignore0) {
    this.ignore0 = ignore0;
  }
  
  /**
   * @return Ignore Message field <strong>M</strong>
   */
  public boolean isIgnore1(){
    return ignore1;
  }
  
  /**
   * @param ignore1 Ignore Message field <strong>M</strong>
   */
  public void setIgnore1(boolean ignore1) {
    this.ignore1 = ignore1;
  }
  
  /**
   * @return Last executed price. Message field <strong>L</strong>
   */
  public BigDecimal getLastExecPrice(){
    return lastExecPrice;
  }
  
  /**
   * @param lastExecPrice Last executed price. Message field <strong>L</strong>
   */
  public void setLastExecPrice(BigDecimal lastExecPrice) {
    this.lastExecPrice = lastExecPrice;
  }
  
  /**
   * @return Last executed quantity. Message field <strong>l</strong>
   */
  public BigDecimal getLastExecQty(){
    return lastExecQty;
  }
  
  /**
   * @param lastExecQty Last executed quantity. Message field <strong>l</strong>
   */
  public void setLastExecQty(BigDecimal lastExecQty) {
    this.lastExecQty = lastExecQty;
  }
  
  /**
   * @return Last Prevented Quantity; This is only visible if the order expired due to STP trigger. Message field <strong>B</strong>
   */
  public BigDecimal getLastPreventedQty(){
    return lastPreventedQty;
  }
  
  /**
   * @param lastPreventedQty Last Prevented Quantity; This is only visible if the order expired due to STP trigger. Message field <strong>B</strong>
   */
  public void setLastPreventedQty(BigDecimal lastPreventedQty) {
    this.lastPreventedQty = lastPreventedQty;
  }
  
  /**
   * @return Last quote asset transacted quantity (i.e. lastPrice * lastQty). Message field <strong>Y</strong>
   */
  public BigDecimal getLastQuoteQty(){
    return lastQuoteQty;
  }
  
  /**
   * @param lastQuoteQty Last quote asset transacted quantity (i.e. lastPrice * lastQty). Message field <strong>Y</strong>
   */
  public void setLastQuoteQty(BigDecimal lastQuoteQty) {
    this.lastQuoteQty = lastQuoteQty;
  }
  
  /**
   * @return Order creation time. Message field <strong>O</strong>
   */
  public long getOrderCreationTime(){
    return orderCreationTime;
  }
  
  /**
   * @param orderCreationTime Order creation time. Message field <strong>O</strong>
   */
  public void setOrderCreationTime(long orderCreationTime) {
    this.orderCreationTime = orderCreationTime;
  }
  
  /**
   * @return Order ID Message field <strong>i</strong>
   */
  public String getOrderID(){
    return orderID;
  }
  
  /**
   * @param orderID Order ID Message field <strong>i</strong>
   */
  public void setOrderID(String orderID) {
    this.orderID = orderID;
  }
  
  /**
   * @return OrderListId Message field <strong>g</strong>
   */
  public int getOrderListId(){
    return orderListId;
  }
  
  /**
   * @param orderListId OrderListId Message field <strong>g</strong>
   */
  public void setOrderListId(int orderListId) {
    this.orderListId = orderListId;
  }
  
  /**
   * @return Is the order on the book? Message field <strong>w</strong>
   */
  public boolean isOrderOnBook(){
    return orderOnBook;
  }
  
  /**
   * @param orderOnBook Is the order on the book? Message field <strong>w</strong>
   */
  public void setOrderOnBook(boolean orderOnBook) {
    this.orderOnBook = orderOnBook;
  }
  
  /**
   * @return Order price. Message field <strong>p</strong>
   */
  public BigDecimal getOrderPrice(){
    return orderPrice;
  }
  
  /**
   * @param orderPrice Order price. Message field <strong>p</strong>
   */
  public void setOrderPrice(BigDecimal orderPrice) {
    this.orderPrice = orderPrice;
  }
  
  /**
   * @return Order quantity. Message field <strong>q</strong>
   */
  public BigDecimal getOrderQuantity(){
    return orderQuantity;
  }
  
  /**
   * @param orderQuantity Order quantity. Message field <strong>q</strong>
   */
  public void setOrderQuantity(BigDecimal orderQuantity) {
    this.orderQuantity = orderQuantity;
  }
  
  /**
   * @return Order reject reason, ; will be an error code. Message field <strong>r</strong>
   */
  public String getOrderRejectReason(){
    return orderRejectReason;
  }
  
  /**
   * @param orderRejectReason Order reject reason, ; will be an error code. Message field <strong>r</strong>
   */
  public void setOrderRejectReason(String orderRejectReason) {
    this.orderRejectReason = orderRejectReason;
  }
  
  /**
   * @return Order type. Message field <strong>o</strong>
   */
  public String getOrderType(){
    return orderType;
  }
  
  /**
   * @param orderType Order type. Message field <strong>o</strong>
   */
  public void setOrderType(String orderType) {
    this.orderType = orderType;
  }
  
  /**
   * @return Original client order ID; This is the ID of the order being canceled Message field <strong>C</strong>
   */
  public String getOrigClientOrderID(){
    return origClientOrderID;
  }
  
  /**
   * @param origClientOrderID Original client order ID; This is the ID of the order being canceled Message field <strong>C</strong>
   */
  public void setOrigClientOrderID(String origClientOrderID) {
    this.origClientOrderID = origClientOrderID;
  }
  
  /**
   * @return Prevented Match Id; This is only visible if the order expire due to STP trigger. Message field <strong>v</strong>
   */
  public long getPreventedMatchID(){
    return preventedMatchID;
  }
  
  /**
   * @param preventedMatchID Prevented Match Id; This is only visible if the order expire due to STP trigger. Message field <strong>v</strong>
   */
  public void setPreventedMatchID(long preventedMatchID) {
    this.preventedMatchID = preventedMatchID;
  }
  
  /**
   * @return Prevented Quantity; This is only visible if the order expired due to STP trigger. Message field <strong>A</strong>
   */
  public BigDecimal getPreventedQty(){
    return preventedQty;
  }
  
  /**
   * @param preventedQty Prevented Quantity; This is only visible if the order expired due to STP trigger. Message field <strong>A</strong>
   */
  public void setPreventedQty(BigDecimal preventedQty) {
    this.preventedQty = preventedQty;
  }
  
  /**
   * @return Quote Order Quantity Message field <strong>Q</strong>
   */
  public BigDecimal getQuoteQty(){
    return quoteQty;
  }
  
  /**
   * @param quoteQty Quote Order Quantity Message field <strong>Q</strong>
   */
  public void setQuoteQty(BigDecimal quoteQty) {
    this.quoteQty = quoteQty;
  }
  
  /**
   * @return selfTradePreventionMode Message field <strong>V</strong>
   */
  public String getSelfTradePreventionMode(){
    return selfTradePreventionMode;
  }
  
  /**
   * @param selfTradePreventionMode selfTradePreventionMode Message field <strong>V</strong>
   */
  public void setSelfTradePreventionMode(String selfTradePreventionMode) {
    this.selfTradePreventionMode = selfTradePreventionMode;
  }
  
  /**
   * @return Side Message field <strong>S</strong>
   */
  public String getSide(){
    return side;
  }
  
  /**
   * @param side Side Message field <strong>S</strong>
   */
  public void setSide(String side) {
    this.side = side;
  }
  
  /**
   * @return Stop price. Message field <strong>P</strong>
   */
  public BigDecimal getStopPrice(){
    return stopPrice;
  }
  
  /**
   * @param stopPrice Stop price. Message field <strong>P</strong>
   */
  public void setStopPrice(BigDecimal stopPrice) {
    this.stopPrice = stopPrice;
  }
  
  /**
   * @return Strategy ID; This is only visible if the strategyId parameter was provided upon order placement. Message field <strong>j</strong>
   */
  public long getStrategyID(){
    return strategyID;
  }
  
  /**
   * @param strategyID Strategy ID; This is only visible if the strategyId parameter was provided upon order placement. Message field <strong>j</strong>
   */
  public void setStrategyID(long strategyID) {
    this.strategyID = strategyID;
  }
  
  /**
   * @return Strategy Type; This is only visible if the strategyType parameter was provided upon order placement. Message field <strong>J</strong>
   */
  public long getStrategyType(){
    return strategyType;
  }
  
  /**
   * @param strategyType Strategy Type; This is only visible if the strategyType parameter was provided upon order placement. Message field <strong>J</strong>
   */
  public void setStrategyType(long strategyType) {
    this.strategyType = strategyType;
  }
  
  /**
   * @return Symbol. Message field <strong>s</strong>
   */
  public String getSymbol(){
    return symbol;
  }
  
  /**
   * @param symbol Symbol. Message field <strong>s</strong>
   */
  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }
  
  /**
   * @return Time in force. Message field <strong>f</strong>
   */
  public String getTimeInForce(){
    return timeInForce;
  }
  
  /**
   * @param timeInForce Time in force. Message field <strong>f</strong>
   */
  public void setTimeInForce(String timeInForce) {
    this.timeInForce = timeInForce;
  }
  
  /**
   * @return TradeGroupId; This is only visible if the account is part of a trade group and the order expired due to STP trigger. Message field <strong>u</strong>
   */
  public long getTradeGroupId(){
    return tradeGroupId;
  }
  
  /**
   * @param tradeGroupId TradeGroupId; This is only visible if the account is part of a trade group and the order expired due to STP trigger. Message field <strong>u</strong>
   */
  public void setTradeGroupId(long tradeGroupId) {
    this.tradeGroupId = tradeGroupId;
  }
  
  /**
   * @return Trade ID Message field <strong>t</strong>
   */
  public long getTradeID(){
    return tradeID;
  }
  
  /**
   * @param tradeID Trade ID Message field <strong>t</strong>
   */
  public void setTradeID(long tradeID) {
    this.tradeID = tradeID;
  }
  
  /**
   * @return Is this trade the maker side? Message field <strong>m</strong>
   */
  public boolean isTradeMakerSide(){
    return tradeMakerSide;
  }
  
  /**
   * @param tradeMakerSide Is this trade the maker side? Message field <strong>m</strong>
   */
  public void setTradeMakerSide(boolean tradeMakerSide) {
    this.tradeMakerSide = tradeMakerSide;
  }
  
  /**
   * @return Trailing Delta; This is only visible if the order was a trailing stop order. Message field <strong>d</strong>
   */
  public int getTrailingDelta(){
    return trailingDelta;
  }
  
  /**
   * @param trailingDelta Trailing Delta; This is only visible if the order was a trailing stop order. Message field <strong>d</strong>
   */
  public void setTrailingDelta(int trailingDelta) {
    this.trailingDelta = trailingDelta;
  }
  
  /**
   * @return Trailing Time; This is only visible if the trailing stop order has been activated. Message field <strong>D</strong>
   */
  public long getTrailingTime(){
    return trailingTime;
  }
  
  /**
   * @param trailingTime Trailing Time; This is only visible if the trailing stop order has been activated. Message field <strong>D</strong>
   */
  public void setTrailingTime(long trailingTime) {
    this.trailingTime = trailingTime;
  }
  
  /**
   * @return Transaction time Message field <strong>T</strong>
   */
  public long getTransactionTime(){
    return transactionTime;
  }
  
  /**
   * @param transactionTime Transaction time Message field <strong>T</strong>
   */
  public void setTransactionTime(long transactionTime) {
    this.transactionTime = transactionTime;
  }
  
  /**
   * @return Working Time; This is only visible if the order has been placed on the book. Message field <strong>W</strong>
   */
  public long getWorkingTime(){
    return workingTime;
  }
  
  /**
   * @param workingTime Working Time; This is only visible if the order has been placed on the book. Message field <strong>W</strong>
   */
  public void setWorkingTime(long workingTime) {
    this.workingTime = workingTime;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
