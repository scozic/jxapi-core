package com.scz.jcex.binance.gen.spottrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.binance.gen.spottrading.serializers.BinanceAccountResponseCommissionRatesSerializer;
import com.scz.jcex.util.EncodingUtil;
import java.math.BigDecimal;

/**
 * 
 */
@JsonSerialize(using = BinanceAccountResponseCommissionRatesSerializer.class)
public class BinanceAccountResponseCommissionRates {
  private BigDecimal buyer;
  private BigDecimal maker;
  private BigDecimal seller;
  private BigDecimal taker;
  
  /**
   * @return 
   */
  public BigDecimal getBuyer(){
    return buyer;
  }
  
  /**
   * @param buyer 
   */
  public void setBuyer(BigDecimal buyer) {
    this.buyer = buyer;
  }
  
  /**
   * @return 
   */
  public BigDecimal getMaker(){
    return maker;
  }
  
  /**
   * @param maker 
   */
  public void setMaker(BigDecimal maker) {
    this.maker = maker;
  }
  
  /**
   * @return 
   */
  public BigDecimal getSeller(){
    return seller;
  }
  
  /**
   * @param seller 
   */
  public void setSeller(BigDecimal seller) {
    this.seller = seller;
  }
  
  /**
   * @return 
   */
  public BigDecimal getTaker(){
    return taker;
  }
  
  /**
   * @param taker 
   */
  public void setTaker(BigDecimal taker) {
    this.taker = taker;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
