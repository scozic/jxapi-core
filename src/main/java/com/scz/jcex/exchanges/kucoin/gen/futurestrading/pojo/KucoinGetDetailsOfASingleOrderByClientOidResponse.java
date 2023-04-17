package com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.serializers.KucoinGetDetailsOfASingleOrderByClientOidResponseSerializer;
import com.scz.jcex.util.EncodingUtil;

/**
 * Response to Kucoin FuturesTrading API GetDetailsOfASingleOrderByClientOid REST endpoint request<br/>Get a single order by order id (including a stop order), querying it by its client side assigned <strong>clientOid</strong>.<br/>See <a href="https://docs.kucoin.com/futures/#get-details-of-a-single-order">API</a><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = KucoinGetDetailsOfASingleOrderByClientOidResponseSerializer.class)
public class KucoinGetDetailsOfASingleOrderByClientOidResponse {
  private String code;
  private KucoinGetDetailsOfASingleOrderByClientOidResponseData data;
  private String msg;
  
  /**
   * @return System error code, see <a href="https://docs.kucoin.com/#request">API</a>
   */
  public String getCode(){
    return code;
  }
  
  /**
   * @param code System error code, see <a href="https://docs.kucoin.com/#request">API</a>
   */
  public void setCode(String code) {
    this.code = code;
  }
  
  /**
   * @return Response payload
   */
  public KucoinGetDetailsOfASingleOrderByClientOidResponseData getData(){
    return data;
  }
  
  /**
   * @param data Response payload
   */
  public void setData(KucoinGetDetailsOfASingleOrderByClientOidResponseData data) {
    this.data = data;
  }
  
  /**
   * @return System error code description, provided in error response when code is not '0', see <a href="https://docs.kucoin.com/#request">API</a>
   */
  public String getMsg(){
    return msg;
  }
  
  /**
   * @param msg System error code description, provided in error response when code is not '0', see <a href="https://docs.kucoin.com/#request">API</a>
   */
  public void setMsg(String msg) {
    this.msg = msg;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
