package com.scz.jxapi.exchanges.binance.gen.spottrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jxapi.exchanges.binance.gen.spottrading.serializers.BinanceListStatusUserDataStreamMessageSerializer;
import com.scz.jxapi.util.EncodingUtil;

import java.math.BigDecimal;

/**
 * Message disseminated upon subscription to Binance SpotTrading API listStatusUserDataStream websocket endpoint request<br/>User data Stream.<br/>See <a href="https://binance-docs.github.io/apidocs/spot/en/#user-data-streams">API</a><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = BinanceListStatusUserDataStreamMessageSerializer.class)
public class BinanceListStatusUserDataStreamMessage {
  private String asset;
  private BigDecimal balanceDelta;
  private Long clearTime;
  private Long eventTime;
  private String eventType;
  
  /**
   * @return Asset. Message field <strong>a</strong>
   */
  public String getAsset(){
    return asset;
  }
  
  /**
   * @param asset Asset. Message field <strong>a</strong>
   */
  public void setAsset(String asset) {
    this.asset = asset;
  }
  
  /**
   * @return Balance delta. Message field <strong>d</strong>
   */
  public BigDecimal getBalanceDelta(){
    return balanceDelta;
  }
  
  /**
   * @param balanceDelta Balance delta. Message field <strong>d</strong>
   */
  public void setBalanceDelta(BigDecimal balanceDelta) {
    this.balanceDelta = balanceDelta;
  }
  
  /**
   * @return Clear Time. Message field <strong>T</strong>
   */
  public Long getClearTime(){
    return clearTime;
  }
  
  /**
   * @param clearTime Clear Time. Message field <strong>T</strong>
   */
  public void setClearTime(Long clearTime) {
    this.clearTime = clearTime;
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
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
