package com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jxapi.exchanges.kucoin.gen.futurestrading.serializers.KucoinAccountBalanceEventsMessageDataSerializer;
import com.scz.jxapi.util.EncodingUtil;
import java.math.BigDecimal;

/**
 * 
 */
@JsonSerialize(using = KucoinAccountBalanceEventsMessageDataSerializer.class)
public class KucoinAccountBalanceEventsMessageData {
  private BigDecimal availableBalance;
  private String currency;
  private BigDecimal holdBalance;
  private BigDecimal orderMargin;
  private Long timestamp;
  private BigDecimal withdrawHold;
  
  /**
   * @return Provided when <strong>subject</strong> is <strong>availableBalance.change</strong>. Current available amount
   */
  public BigDecimal getAvailableBalance(){
    return availableBalance;
  }
  
  /**
   * @param availableBalance Provided when <strong>subject</strong> is <strong>availableBalance.change</strong>. Current available amount
   */
  public void setAvailableBalance(BigDecimal availableBalance) {
    this.availableBalance = availableBalance;
  }
  
  /**
   * @return Currency
   */
  public String getCurrency(){
    return currency;
  }
  
  /**
   * @param currency Currency
   */
  public void setCurrency(String currency) {
    this.currency = currency;
  }
  
  /**
   * @return Provided when <strong>subject</strong> is <strong>availableBalance.change</strong>. Current available amount
   */
  public BigDecimal getHoldBalance(){
    return holdBalance;
  }
  
  /**
   * @param holdBalance Provided when <strong>subject</strong> is <strong>availableBalance.change</strong>. Current available amount
   */
  public void setHoldBalance(BigDecimal holdBalance) {
    this.holdBalance = holdBalance;
  }
  
  /**
   * @return Provided when <subject>subject</strong> is <strong>orderMargin.change</strong>. Current order margin
   */
  public BigDecimal getOrderMargin(){
    return orderMargin;
  }
  
  /**
   * @param orderMargin Provided when <subject>subject</strong> is <strong>orderMargin.change</strong>. Current order margin
   */
  public void setOrderMargin(BigDecimal orderMargin) {
    this.orderMargin = orderMargin;
  }
  
  /**
   * @return Timestamp
   */
  public Long getTimestamp(){
    return timestamp;
  }
  
  /**
   * @param timestamp Timestamp
   */
  public void setTimestamp(Long timestamp) {
    this.timestamp = timestamp;
  }
  
  /**
   * @return Provided when <strong>subject</strong> is <strong>withdrawHold.change</strong>. Current frozen amount for withdrawal
   */
  public BigDecimal getWithdrawHold(){
    return withdrawHold;
  }
  
  /**
   * @param withdrawHold Provided when <strong>subject</strong> is <strong>withdrawHold.change</strong>. Current frozen amount for withdrawal
   */
  public void setWithdrawHold(BigDecimal withdrawHold) {
    this.withdrawHold = withdrawHold;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
