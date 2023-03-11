package com.scz.jcex.binance.gen.spottrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.binance.gen.spottrading.serializers.BinanceOutboundAccountPositionUserDataStreamMessageBSerializer;
import com.scz.jcex.util.EncodingUtil;
import java.math.BigDecimal;

/**
 * Balances array
 */
@JsonSerialize(using = BinanceOutboundAccountPositionUserDataStreamMessageBSerializer.class)
public class BinanceOutboundAccountPositionUserDataStreamMessageB {
  private String a;
  private BigDecimal f;
  private BigDecimal l;
  
  /**
   * @return asset
   */
  public String getA(){
    return a;
  }
  
  /**
   * @param a asset
   */
  public void setA(String a) {
    this.a = a;
  }
  
  /**
   * @return free
   */
  public BigDecimal getF(){
    return f;
  }
  
  /**
   * @param f free
   */
  public void setF(BigDecimal f) {
    this.f = f;
  }
  
  /**
   * @return locked
   */
  public BigDecimal getL(){
    return l;
  }
  
  /**
   * @param l locked
   */
  public void setL(BigDecimal l) {
    this.l = l;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
