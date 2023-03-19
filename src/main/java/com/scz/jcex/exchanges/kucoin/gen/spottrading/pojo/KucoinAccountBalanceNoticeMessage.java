package com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.serializers.KucoinAccountBalanceNoticeMessageSerializer;
import com.scz.jcex.util.EncodingUtil;

/**
 * Message disseminated upon subscription to Kucoin SpotTrading API AccountBalanceNotice websocket endpoint request<br/>You will receive this message when an account balance changes. The message contains the details of the change.<br/>See <a href="https://docs.kucoin.com/#account-balance-notice">API</a><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = KucoinAccountBalanceNoticeMessageSerializer.class)
public class KucoinAccountBalanceNoticeMessage {
  private KucoinAccountBalanceNoticeMessageData data;
  private String subject;
  private String topic;
  private String type;
  
  /**
   * @return 
   */
  public KucoinAccountBalanceNoticeMessageData getData(){
    return data;
  }
  
  /**
   * @param data 
   */
  public void setData(KucoinAccountBalanceNoticeMessageData data) {
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
   * @return .
   */
  public String getTopic(){
    return topic;
  }
  
  /**
   * @param topic .
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
