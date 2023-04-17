package com.scz.jcex.exchanges.binance.gen.spottrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.exchanges.binance.gen.spottrading.serializers.BinanceAccountResponseBalancesSerializer;
import com.scz.jcex.util.EncodingUtil;
import java.math.BigDecimal;

/**
 * Balances array
 */
@JsonSerialize(using = BinanceAccountResponseBalancesSerializer.class)
public class BinanceAccountResponseBalances {
  private String asset;
  private BigDecimal free;
  private BigDecimal locked;
  
  /**
   * @return 
   */
  public String getAsset(){
    return asset;
  }
  
  /**
   * @param asset 
   */
  public void setAsset(String asset) {
    this.asset = asset;
  }
  
  /**
   * @return 
   */
  public BigDecimal getFree(){
    return free;
  }
  
  /**
   * @param free 
   */
  public void setFree(BigDecimal free) {
    this.free = free;
  }
  
  /**
   * @return 
   */
  public BigDecimal getLocked(){
    return locked;
  }
  
  /**
   * @param locked 
   */
  public void setLocked(BigDecimal locked) {
    this.locked = locked;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
