package com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.serializers.KucoinCancelSingleOrderByClientOidResponseDataSerializer;
import com.scz.jcex.util.EncodingUtil;

/**
 * Response payload
 */
@JsonSerialize(using = KucoinCancelSingleOrderByClientOidResponseDataSerializer.class)
public class KucoinCancelSingleOrderByClientOidResponseData {
  private String cancelledOrderId;
  private String clientOid;
  
  /**
   * @return Order ID of cancelled order
   */
  public String getCancelledOrderId(){
    return cancelledOrderId;
  }
  
  /**
   * @param cancelledOrderId Order ID of cancelled order
   */
  public void setCancelledOrderId(String cancelledOrderId) {
    this.cancelledOrderId = cancelledOrderId;
  }
  
  /**
   * @return Unique ID of the cancelled order
   */
  public String getClientOid(){
    return clientOid;
  }
  
  /**
   * @param clientOid Unique ID of the cancelled order
   */
  public void setClientOid(String clientOid) {
    this.clientOid = clientOid;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
