package com.scz.jcex.binance.gen.spottrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.binance.gen.spottrading.serializers.BinanceBalanceUpdateUserDataStreamMessageSerializer;
import com.scz.jcex.util.EncodingUtil;
import java.math.BigDecimal;

/**
 * Message disseminated upon subscription to Binance SpotTrading API balanceUpdateUserDataStream websocket endpoint request<br/>User data Stream.<br/>See <a href="https://binance-docs.github.io/apidocs/spot/en/#user-data-streams">API</a><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = BinanceBalanceUpdateUserDataStreamMessageSerializer.class)
public class BinanceBalanceUpdateUserDataStreamMessage {
  private long E;
  private long T;
  private String a;
  private BigDecimal d;
  private String e;
  
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
   * @return Clear Time
   */
  public long getT(){
    return T;
  }
  
  /**
   * @param T Clear Time
   */
  public void setT(long T) {
    this.T = T;
  }
  
  /**
   * @return Asset
   */
  public String getA(){
    return a;
  }
  
  /**
   * @param a Asset
   */
  public void setA(String a) {
    this.a = a;
  }
  
  /**
   * @return Balance delta
   */
  public BigDecimal getD(){
    return d;
  }
  
  /**
   * @param d Balance delta
   */
  public void setD(BigDecimal d) {
    this.d = d;
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
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
