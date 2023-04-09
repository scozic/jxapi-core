package com.scz.jcex.exchanges.binance.gen.spottrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.exchanges.binance.gen.spottrading.serializers.BinanceOutboundAccountPositionUserDataStreamMessageSerializer;
import com.scz.jcex.util.EncodingUtil;
import java.util.List;

/**
 * Message disseminated upon subscription to Binance SpotTrading API outboundAccountPositionUserDataStream websocket endpoint request<br/>User data Stream.<br/>See <a href="https://binance-docs.github.io/apidocs/spot/en/#user-data-streams">API</a><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = BinanceOutboundAccountPositionUserDataStreamMessageSerializer.class)
public class BinanceOutboundAccountPositionUserDataStreamMessage {
  private List<BinanceOutboundAccountPositionUserDataStreamMessageBalancesArray> balancesArray;
  private Long eventTime;
  private String eventType;
  private Long lastAccountUpdateTime;
  
  /**
   * @return Balances array. Message field <strong>B</strong>
   */
  public List<BinanceOutboundAccountPositionUserDataStreamMessageBalancesArray> getBalancesArray(){
    return balancesArray;
  }
  
  /**
   * @param balancesArray Balances array. Message field <strong>B</strong>
   */
  public void setBalancesArray(List<BinanceOutboundAccountPositionUserDataStreamMessageBalancesArray> balancesArray) {
    this.balancesArray = balancesArray;
  }
  
  /**
   * @return Event time. Message field <strong>E</strong>
   */
  public Long getEventTime(){
    return eventTime;
  }
  
  /**
   * @param eventTime Event time. Message field <strong>E</strong>
   */
  public void setEventTime(Long eventTime) {
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
   * @return Time of last account update. Message field <strong>u</strong>
   */
  public Long getLastAccountUpdateTime(){
    return lastAccountUpdateTime;
  }
  
  /**
   * @param lastAccountUpdateTime Time of last account update. Message field <strong>u</strong>
   */
  public void setLastAccountUpdateTime(Long lastAccountUpdateTime) {
    this.lastAccountUpdateTime = lastAccountUpdateTime;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
