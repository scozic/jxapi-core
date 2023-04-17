package com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.serializers.KucoinAccountBalanceEventsMessageSerializer;
import com.scz.jcex.util.EncodingUtil;

/**
 * Message disseminated upon subscription to Kucoin FuturesTrading API AccountBalanceEvents websocket endpoint request<br/>Stop Order Lifecycle Event websocket stream.<br/>See <a href="https://docs.kucoin.com/futures/#account-balance-events">API</a><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = KucoinAccountBalanceEventsMessageSerializer.class)
public class KucoinAccountBalanceEventsMessage {
  private KucoinAccountBalanceEventsMessageData data;
  private String subject;
  private String topic;
  private String userId;
  
  /**
   * @return 
   */
  public KucoinAccountBalanceEventsMessageData getData(){
    return data;
  }
  
  /**
   * @param data 
   */
  public void setData(KucoinAccountBalanceEventsMessageData data) {
    this.data = data;
  }
  
  /**
   * @return Either <strong>orderMargin.change</strong>, <strong>availableBalance.change</strong> or <strong>withdrawHold.change</strong>
   */
  public String getSubject(){
    return subject;
  }
  
  /**
   * @param subject Either <strong>orderMargin.change</strong>, <strong>availableBalance.change</strong> or <strong>withdrawHold.change</strong>
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
