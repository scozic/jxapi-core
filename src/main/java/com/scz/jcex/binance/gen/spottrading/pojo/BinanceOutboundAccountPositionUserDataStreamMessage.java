package com.scz.jcex.binance.gen.spottrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.binance.gen.spottrading.serializers.BinanceOutboundAccountPositionUserDataStreamMessageSerializer;
import com.scz.jcex.util.EncodingUtil;
import java.util.List;

/**
 * Message disseminated upon subscription to Binance SpotTrading API outboundAccountPositionUserDataStream websocket endpoint request<br/>User data Stream.<br/>See <a href="https://binance-docs.github.io/apidocs/spot/en/#user-data-streams">API</a><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = BinanceOutboundAccountPositionUserDataStreamMessageSerializer.class)
public class BinanceOutboundAccountPositionUserDataStreamMessage {
  private List<BinanceOutboundAccountPositionUserDataStreamMessageB> B;
  private long E;
  private String e;
  private long u;
  
  /**
   * @return Balances array
   */
  public List<BinanceOutboundAccountPositionUserDataStreamMessageB> getB(){
    return B;
  }
  
  /**
   * @param B Balances array
   */
  public void setB(List<BinanceOutboundAccountPositionUserDataStreamMessageB> B) {
    this.B = B;
  }
  
  /**
   * @return Event time
   */
  public long getE(){
    return E;
  }
  
  /**
   * @param E Event time
   */
  public void setE(long E) {
    this.E = E;
  }
  
  /**
   * @return Event type
   */
  public String gete(){
    return e;
  }
  
  /**
   * @param e Event type
   */
  public void sete(String e) {
    this.e = e;
  }
  
  /**
   * @return Time of last account update
   */
  public long getU(){
    return u;
  }
  
  /**
   * @param u Time of last account update
   */
  public void setU(long u) {
    this.u = u;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
