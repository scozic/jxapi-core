package com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.serializers.KucoinCancelAnOrderResponseDataSerializer;
import com.scz.jcex.util.EncodingUtil;
import java.util.List;

/**
 * List of market information for each market symbol
 */
@JsonSerialize(using = KucoinCancelAnOrderResponseDataSerializer.class)
public class KucoinCancelAnOrderResponseData {
  private List<String> cancelledOrderIds;
  
  /**
   * @return ID of order on market
   */
  public List<String> getCancelledOrderIds(){
    return cancelledOrderIds;
  }
  
  /**
   * @param cancelledOrderIds ID of order on market
   */
  public void setCancelledOrderIds(List<String> cancelledOrderIds) {
    this.cancelledOrderIds = cancelledOrderIds;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
