package com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jxapi.exchanges.kucoin.gen.futurestrading.serializers.KucoinTradeOrdersMessageSerializer;
import com.scz.jxapi.util.EncodingUtil;

/**
 * Message disseminated upon subscription to Kucoin FuturesTrading API TradeOrders websocket endpoint request<br/>Trade Orders websocket stream.<br/>See <a href="https://docs.kucoin.com/futures/#trade-orders">API</a><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = KucoinTradeOrdersMessageSerializer.class)
public class KucoinTradeOrdersMessage {
  private String channelType;
  private KucoinTradeOrdersMessageData data;
  private String subject;
  private String topic;
  private String type;
  
  /**
   * @return 
   */
  public String getChannelType(){
    return channelType;
  }
  
  /**
   * @param channelType 
   */
  public void setChannelType(String channelType) {
    this.channelType = channelType;
  }
  
  /**
   * @return 
   */
  public KucoinTradeOrdersMessageData getData(){
    return data;
  }
  
  /**
   * @param data 
   */
  public void setData(KucoinTradeOrdersMessageData data) {
    this.data = data;
  }
  
  /**
   * @return 
   */
  public String getSubject(){
    return subject;
  }
  
  /**
   * @param subject 
   */
  public void setSubject(String subject) {
    this.subject = subject;
  }
  
  /**
   * @return 
   */
  public String getTopic(){
    return topic;
  }
  
  /**
   * @param topic 
   */
  public void setTopic(String topic) {
    this.topic = topic;
  }
  
  /**
   * @return 
   */
  public String getType(){
    return type;
  }
  
  /**
   * @param type 
   */
  public void setType(String type) {
    this.type = type;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
