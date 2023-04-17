package com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.serializers.KucoinListAccountsResponseDataSerializer;
import com.scz.jxapi.util.EncodingUtil;
import java.math.BigDecimal;

/**
 * Response payload
 */
@JsonSerialize(using = KucoinListAccountsResponseDataSerializer.class)
public class KucoinListAccountsResponseData {
  private BigDecimal available;
  private BigDecimal balance;
  private String currency;
  private BigDecimal holds;
  private String id;
  private String type;
  
  /**
   * @return Funds available to withdraw or trade
   */
  public BigDecimal getAvailable(){
    return available;
  }
  
  /**
   * @param available Funds available to withdraw or trade
   */
  public void setAvailable(BigDecimal available) {
    this.available = available;
  }
  
  /**
   * @return Total funds in the account
   */
  public BigDecimal getBalance(){
    return balance;
  }
  
  /**
   * @param balance Total funds in the account
   */
  public void setBalance(BigDecimal balance) {
    this.balance = balance;
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
   * @return Funds on hold (not available for use)
   */
  public BigDecimal getHolds(){
    return holds;
  }
  
  /**
   * @param holds Funds on hold (not available for use)
   */
  public void setHolds(BigDecimal holds) {
    this.holds = holds;
  }
  
  /**
   * @return The ID of the account
   */
  public String getId(){
    return id;
  }
  
  /**
   * @param id The ID of the account
   */
  public void setId(String id) {
    this.id = id;
  }
  
  /**
   * @return Account type: <strong>main</strong>, <strong>trade</strong>, <strong>margin</strong>
   */
  public String getType(){
    return type;
  }
  
  /**
   * @param type Account type: <strong>main</strong>, <strong>trade</strong>, <strong>margin</strong>
   */
  public void setType(String type) {
    this.type = type;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
