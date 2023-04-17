package com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.serializers.KucoinCancelAllOrdersResponseDataSerializer;
import com.scz.jxapi.util.EncodingUtil;

import java.util.List;

/**
 * Response payload
 */
@JsonSerialize(using = KucoinCancelAllOrdersResponseDataSerializer.class)
public class KucoinCancelAllOrdersResponseData {
  private List<String> cancelledOrderIds;
  
  /**
   * @return Unique ID of the cancelled order
   */
  public List<String> getCancelledOrderIds(){
    return cancelledOrderIds;
  }
  
  /**
   * @param cancelledOrderIds Unique ID of the cancelled order
   */
  public void setCancelledOrderIds(List<String> cancelledOrderIds) {
    this.cancelledOrderIds = cancelledOrderIds;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
