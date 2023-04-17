package com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.serializers.KucoinAccountBalanceNoticeMessageDataRelationContextSerializer;
import com.scz.jxapi.util.EncodingUtil;

/**
 * The context of trade event
 */
@JsonSerialize(using = KucoinAccountBalanceNoticeMessageDataRelationContextSerializer.class)
public class KucoinAccountBalanceNoticeMessageDataRelationContext {
  private String orderId;
  private String symbol;
  private String tradeId;
  
  /**
   * @return 
   */
  public String getOrderId(){
    return orderId;
  }
  
  /**
   * @param orderId 
   */
  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }
  
  /**
   * @return 
   */
  public String getSymbol(){
    return symbol;
  }
  
  /**
   * @param symbol 
   */
  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }
  
  /**
   * @return 
   */
  public String getTradeId(){
    return tradeId;
  }
  
  /**
   * @param tradeId 
   */
  public void setTradeId(String tradeId) {
    this.tradeId = tradeId;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
