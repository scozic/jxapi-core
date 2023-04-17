package com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jxapi.exchanges.kucoin.gen.futurestrading.serializers.KucoinGetDetailsOfASingleOrderResponseSerializer;
import com.scz.jxapi.util.EncodingUtil;

/**
 * Response to Kucoin FuturesTrading API GetDetailsOfASingleOrder REST endpoint request<br/>Get a single order by order id (including a stop order), querying it by its exchange assigned <strong>orderId</strong>.<br/>See <a href="https://docs.kucoin.com/futures/#get-details-of-a-single-order">API</a><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = KucoinGetDetailsOfASingleOrderResponseSerializer.class)
public class KucoinGetDetailsOfASingleOrderResponse {
  private String code;
  private KucoinGetDetailsOfASingleOrderResponseData data;
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
  public KucoinGetDetailsOfASingleOrderResponseData getData(){
    return data;
  }
  
  /**
   * @param data Response payload
   */
  public void setData(KucoinGetDetailsOfASingleOrderResponseData data) {
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
