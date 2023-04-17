package com.scz.jcex.exchanges.kucoin.gen.futuresmarketdata.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.exchanges.kucoin.gen.futuresmarketdata.serializers.KucoinRealTimeSymbolTickerV2MessageSerializer;
import com.scz.jcex.util.EncodingUtil;

/**
 * Message disseminated upon subscription to Kucoin FuturesMarketData API RealTimeSymbolTickerV2 websocket endpoint request<br/>24hr rolling window ticker statistics for a single symbol. These are NOT the statistics of the UTC day, but a 24hr rolling window for the previous 24hrs.<br/>See <a href="https://docs.kucoin.com/#symbol-ticker">API</a><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = KucoinRealTimeSymbolTickerV2MessageSerializer.class)
public class KucoinRealTimeSymbolTickerV2Message {
  private KucoinRealTimeSymbolTickerV2MessageData data;
  private String subject;
  private String topic;
  
  /**
   * @return 
   */
  public KucoinRealTimeSymbolTickerV2MessageData getData(){
    return data;
  }
  
  /**
   * @param data 
   */
  public void setData(KucoinRealTimeSymbolTickerV2MessageData data) {
    this.data = data;
  }
  
  /**
   * @return Symbol
   */
  public String getSubject(){
    return subject;
  }
  
  /**
   * @param subject Symbol
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
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
