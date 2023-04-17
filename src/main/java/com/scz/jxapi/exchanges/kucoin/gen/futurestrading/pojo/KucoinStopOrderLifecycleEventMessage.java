package com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jxapi.exchanges.kucoin.gen.futurestrading.serializers.KucoinStopOrderLifecycleEventMessageSerializer;
import com.scz.jxapi.util.EncodingUtil;

/**
 * Message disseminated upon subscription to Kucoin FuturesTrading API StopOrderLifecycleEvent websocket endpoint request<br/>Stop Order Lifecycle Event websocket stream.<br/>See <a href="https://docs.kucoin.com/futures/#stop-order-lifecycle-event">API</a><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = KucoinStopOrderLifecycleEventMessageSerializer.class)
public class KucoinStopOrderLifecycleEventMessage {
  private KucoinStopOrderLifecycleEventMessageData data;
  private String subject;
  private String topic;
  private String userId;
  
  /**
   * @return 
   */
  public KucoinStopOrderLifecycleEventMessageData getData(){
    return data;
  }
  
  /**
   * @param data 
   */
  public void setData(KucoinStopOrderLifecycleEventMessageData data) {
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
