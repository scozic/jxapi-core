package com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.serializers.KucoinIndividualSymbolTickerStreamsMessageSerializer;
import com.scz.jxapi.util.EncodingUtil;

/**
 * Message disseminated upon subscription to Kucoin SpotMarketData API IndividualSymbolTickerStreams websocket endpoint request<br/>24hr rolling window ticker statistics for a single symbol. These are NOT the statistics of the UTC day, but a 24hr rolling window for the previous 24hrs.<br/>See <a href="https://docs.kucoin.com/#symbol-ticker">API</a><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = KucoinIndividualSymbolTickerStreamsMessageSerializer.class)
public class KucoinIndividualSymbolTickerStreamsMessage {
  private KucoinIndividualSymbolTickerStreamsMessageData data;
  private String subject;
  private String topic;
  private String type;
  
  /**
   * @return 
   */
  public KucoinIndividualSymbolTickerStreamsMessageData getData(){
    return data;
  }
  
  /**
   * @param data 
   */
  public void setData(KucoinIndividualSymbolTickerStreamsMessageData data) {
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
