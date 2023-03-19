package com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.serializers.KucoinPrivateOrderChangeV2MessageSerializer;
import com.scz.jcex.util.EncodingUtil;

/**
 * Message disseminated upon subscription to Kucoin SpotTrading API PrivateOrderChangeV2 websocket endpoint request<br/>This topic will push all change events of your orders. Compared with v1, v2 adds an Order Status: "new", there is no difference in push speed. <br/>See <a href="https://docs.kucoin.com/#private-order-change-v2">API</a><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = KucoinPrivateOrderChangeV2MessageSerializer.class)
public class KucoinPrivateOrderChangeV2Message {
  private KucoinPrivateOrderChangeV2MessageData data;
  private String subject;
  private long topic;
  private String type;
  
  /**
   * @return 
   */
  public KucoinPrivateOrderChangeV2MessageData getData(){
    return data;
  }
  
  /**
   * @param data 
   */
  public void setData(KucoinPrivateOrderChangeV2MessageData data) {
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
  public long getTopic(){
    return topic;
  }
  
  /**
   * @param topic .
   */
  public void setTopic(long topic) {
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
