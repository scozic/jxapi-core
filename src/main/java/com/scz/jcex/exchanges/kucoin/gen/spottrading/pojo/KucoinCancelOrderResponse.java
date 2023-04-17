package com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.serializers.KucoinCancelOrderResponseSerializer;
import com.scz.jcex.util.EncodingUtil;

/**
 * Response to Kucoin SpotTrading API CancelOrder REST endpoint request<br/> Request via this endpoint to cancel a single order previously placed.<p><i>This interface is only for cancellation requests. The cancellation result needs to be obtained by querying the order status or subscribing to websocket. It is recommended that you DO NOT cancel the order until receiving the Open message, otherwise the order cannot be cancelled successfully. </i></p> <br/>See <a href="https://docs.kucoin.com/#cancel-an-order">API</a><br/><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = KucoinCancelOrderResponseSerializer.class)
public class KucoinCancelOrderResponse {
  private String code;
  private KucoinCancelOrderResponseData data;
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
  public KucoinCancelOrderResponseData getData(){
    return data;
  }
  
  /**
   * @param data Response payload
   */
  public void setData(KucoinCancelOrderResponseData data) {
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
