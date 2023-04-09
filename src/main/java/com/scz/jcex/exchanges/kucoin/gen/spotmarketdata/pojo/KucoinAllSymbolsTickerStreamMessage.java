package com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.serializers.KucoinAllSymbolsTickerStreamMessageSerializer;
import com.scz.jcex.util.EncodingUtil;

/**
 * Message disseminated upon subscription to Kucoin SpotMarketData API AllSymbolsTickerStream websocket endpoint request<br/>Subscribe to this topic to get the push of all market symbols BBO change. Push frequency: once every 100ms. <br/>See <a href="https://docs.kucoin.com/#all-symbols-ticker">API</a><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = KucoinAllSymbolsTickerStreamMessageSerializer.class)
public class KucoinAllSymbolsTickerStreamMessage {
  private KucoinAllSymbolsTickerStreamMessageData data;
  private String subject;
  private Long topic;
  private String type;
  
  /**
   * @return 
   */
  public KucoinAllSymbolsTickerStreamMessageData getData(){
    return data;
  }
  
  /**
   * @param data 
   */
  public void setData(KucoinAllSymbolsTickerStreamMessageData data) {
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
   * @return .
   */
  public Long getTopic(){
    return topic;
  }
  
  /**
   * @param topic .
   */
  public void setTopic(Long topic) {
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
