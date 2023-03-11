package com.scz.jcex.binance.gen.spottrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.binance.gen.spottrading.serializers.BinanceOutboundAccountPositionUserDataStreamMessageBalancesArraySerializer;
import com.scz.jcex.util.EncodingUtil;
import java.math.BigDecimal;

/**
 * Balances array.
 */
@JsonSerialize(using = BinanceOutboundAccountPositionUserDataStreamMessageBalancesArraySerializer.class)
public class BinanceOutboundAccountPositionUserDataStreamMessageBalancesArray {
  private String asset;
  private BigDecimal free;
  private BigDecimal locked;
  
  /**
   * @return asset. Message field <strong>a</strong>
   */
  public String getAsset(){
    return asset;
  }
  
  /**
   * @param asset asset. Message field <strong>a</strong>
   */
  public void setAsset(String asset) {
    this.asset = asset;
  }
  
  /**
   * @return free. Message field <strong>f</strong>
   */
  public BigDecimal getFree(){
    return free;
  }
  
  /**
   * @param free free. Message field <strong>f</strong>
   */
  public void setFree(BigDecimal free) {
    this.free = free;
  }
  
  /**
   * @return locked. Message field <strong>l</strong>
   */
  public BigDecimal getLocked(){
    return locked;
  }
  
  /**
   * @param locked locked. Message field <strong>l</strong>
   */
  public void setLocked(BigDecimal locked) {
    this.locked = locked;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
