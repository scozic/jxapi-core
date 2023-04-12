package com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.serializers.KucoinPlaceAnOrderResponseDataSerializer;
import com.scz.jcex.util.EncodingUtil;

/**
 * List of market information for each market symbol
 */
@JsonSerialize(using = KucoinPlaceAnOrderResponseDataSerializer.class)
public class KucoinPlaceAnOrderResponseData {
  private String orderId;
  
  /**
   * @return ID of order on market
   */
  public String getOrderId(){
    return orderId;
  }
  
  /**
   * @param orderId ID of order on market
   */
  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
