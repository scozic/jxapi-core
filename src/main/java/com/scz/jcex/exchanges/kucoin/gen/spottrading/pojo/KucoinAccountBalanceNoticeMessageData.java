package com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.serializers.KucoinAccountBalanceNoticeMessageDataSerializer;
import com.scz.jcex.util.EncodingUtil;
import java.math.BigDecimal;

/**
 * 
 */
@JsonSerialize(using = KucoinAccountBalanceNoticeMessageDataSerializer.class)
public class KucoinAccountBalanceNoticeMessageData {
  private BigDecimal available;
  private BigDecimal availableChange;
  private String currency;
  private BigDecimal hold;
  private BigDecimal holdChange;
  private KucoinAccountBalanceNoticeMessageDataRelationContext relationContext;
  private String relationEvent;
  private Long time;
  private BigDecimal total;
  
  /**
   * @return Available balance
   */
  public BigDecimal getAvailable(){
    return available;
  }
  
  /**
   * @param available Available balance
   */
  public void setAvailable(BigDecimal available) {
    this.available = available;
  }
  
  /**
   * @return The change of available balance
   */
  public BigDecimal getAvailableChange(){
    return availableChange;
  }
  
  /**
   * @param availableChange The change of available balance
   */
  public void setAvailableChange(BigDecimal availableChange) {
    this.availableChange = availableChange;
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
   * @return Hold amount
   */
  public BigDecimal getHold(){
    return hold;
  }
  
  /**
   * @param hold Hold amount
   */
  public void setHold(BigDecimal hold) {
    this.hold = hold;
  }
  
  /**
   * @return The change of hold balance
   */
  public BigDecimal getHoldChange(){
    return holdChange;
  }
  
  /**
   * @param holdChange The change of hold balance
   */
  public void setHoldChange(BigDecimal holdChange) {
    this.holdChange = holdChange;
  }
  
  /**
   * @return The context of trade event
   */
  public KucoinAccountBalanceNoticeMessageDataRelationContext getRelationContext(){
    return relationContext;
  }
  
  /**
   * @param relationContext The context of trade event
   */
  public void setRelationContext(KucoinAccountBalanceNoticeMessageDataRelationContext relationContext) {
    this.relationContext = relationContext;
  }
  
  /**
   * @return Relation event, see <a href="https://docs.kucoin.com/#account-balance-notice">API</a>
   */
  public String getRelationEvent(){
    return relationEvent;
  }
  
  /**
   * @param relationEvent Relation event, see <a href="https://docs.kucoin.com/#account-balance-notice">API</a>
   */
  public void setRelationEvent(String relationEvent) {
    this.relationEvent = relationEvent;
  }
  
  /**
   * @return 
   */
  public Long getTime(){
    return time;
  }
  
  /**
   * @param time 
   */
  public void setTime(Long time) {
    this.time = time;
  }
  
  /**
   * @return Total balance
   */
  public BigDecimal getTotal(){
    return total;
  }
  
  /**
   * @param total Total balance
   */
  public void setTotal(BigDecimal total) {
    this.total = total;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
