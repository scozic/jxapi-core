package com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.serializers.KucoinPositionChangeEventsMessageSerializer;
import com.scz.jcex.util.EncodingUtil;

/**
 * Message disseminated upon subscription to Kucoin FuturesTrading API PositionChangeEvents websocket endpoint request<br/>Stop Order Lifecycle Event websocket stream.<br/>See <a href="https://docs.kucoin.com/futures/#account-balance-events">API</a><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = KucoinPositionChangeEventsMessageSerializer.class)
public class KucoinPositionChangeEventsMessage {
  private String channelType;
  private KucoinPositionChangeEventsMessageData data;
  private String subject;
  private String topic;
  private String type;
  private String userId;
  
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
  public KucoinPositionChangeEventsMessageData getData(){
    return data;
  }
  
  /**
   * @param data 
   */
  public void setData(KucoinPositionChangeEventsMessageData data) {
    this.data = data;
  }
  
  /**
   * @return Either <strong>position.change</strong>, <strong>position.settlement</strong> or <strong>position.adjustRiskLimit</strong>
   */
  public String getSubject(){
    return subject;
  }
  
  /**
   * @param subject Either <strong>position.change</strong>, <strong>position.settlement</strong> or <strong>position.adjustRiskLimit</strong>
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
  
  /**
   * @return Deprecated, will delete later
   */
  public String getUserId(){
    return userId;
  }
  
  /**
   * @param userId Deprecated, will delete later
   */
  public void setUserId(String userId) {
    this.userId = userId;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
