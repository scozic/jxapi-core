package com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jxapi.exchanges.kucoin.gen.futurestrading.serializers.KucoinGetAccountOverviewResponseDataSerializer;
import com.scz.jxapi.util.EncodingUtil;

import java.math.BigDecimal;

/**
 * List of market information for each market symbol
 */
@JsonSerialize(using = KucoinGetAccountOverviewResponseDataSerializer.class)
public class KucoinGetAccountOverviewResponseData {
  private BigDecimal accountEquity;
  private BigDecimal availableBalance;
  private String currency;
  private BigDecimal frozenFunds;
  private BigDecimal marginBalance;
  private BigDecimal orderMargin;
  private BigDecimal positionMargin;
  private BigDecimal unrealisedPNL;
  
  /**
   * @return Account equity = marginBalance + Unrealised PNL
   */
  public BigDecimal getAccountEquity(){
    return accountEquity;
  }
  
  /**
   * @param accountEquity Account equity = marginBalance + Unrealised PNL
   */
  public void setAccountEquity(BigDecimal accountEquity) {
    this.accountEquity = accountEquity;
  }
  
  /**
   * @return Available balance
   */
  public BigDecimal getAvailableBalance(){
    return availableBalance;
  }
  
  /**
   * @param availableBalance Available balance
   */
  public void setAvailableBalance(BigDecimal availableBalance) {
    this.availableBalance = availableBalance;
  }
  
  /**
   * @return Currency code
   */
  public String getCurrency(){
    return currency;
  }
  
  /**
   * @param currency Currency code
   */
  public void setCurrency(String currency) {
    this.currency = currency;
  }
  
  /**
   * @return Frozen funds for withdrawal and out-transfer
   */
  public BigDecimal getFrozenFunds(){
    return frozenFunds;
  }
  
  /**
   * @param frozenFunds Frozen funds for withdrawal and out-transfer
   */
  public void setFrozenFunds(BigDecimal frozenFunds) {
    this.frozenFunds = frozenFunds;
  }
  
  /**
   * @return Margin balance = positionMargin + orderMargin + frozenFunds + availableBalance - unrealisedPNL
   */
  public BigDecimal getMarginBalance(){
    return marginBalance;
  }
  
  /**
   * @param marginBalance Margin balance = positionMargin + orderMargin + frozenFunds + availableBalance - unrealisedPNL
   */
  public void setMarginBalance(BigDecimal marginBalance) {
    this.marginBalance = marginBalance;
  }
  
  /**
   * @return Order margin
   */
  public BigDecimal getOrderMargin(){
    return orderMargin;
  }
  
  /**
   * @param orderMargin Order margin
   */
  public void setOrderMargin(BigDecimal orderMargin) {
    this.orderMargin = orderMargin;
  }
  
  /**
   * @return Position margin
   */
  public BigDecimal getPositionMargin(){
    return positionMargin;
  }
  
  /**
   * @param positionMargin Position margin
   */
  public void setPositionMargin(BigDecimal positionMargin) {
    this.positionMargin = positionMargin;
  }
  
  /**
   * @return Unrealised profit and loss
   */
  public BigDecimal getUnrealisedPNL(){
    return unrealisedPNL;
  }
  
  /**
   * @param unrealisedPNL Unrealised profit and loss
   */
  public void setUnrealisedPNL(BigDecimal unrealisedPNL) {
    this.unrealisedPNL = unrealisedPNL;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
