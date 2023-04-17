package com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jxapi.exchanges.kucoin.gen.futurestrading.serializers.KucoinGetUntriggeredStopOrderListResponseSerializer;
import com.scz.jxapi.util.EncodingUtil;

/**
 * Response to Kucoin FuturesTrading API GetUntriggeredStopOrderList REST endpoint request<br/>Get the un-triggered stop orders list. <br/>See <a href="https://docs.kucoin.com/futures/#get-untriggered-stop-order-list">API</a><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = KucoinGetUntriggeredStopOrderListResponseSerializer.class)
public class KucoinGetUntriggeredStopOrderListResponse {
  private String code;
  private KucoinGetUntriggeredStopOrderListResponseData data;
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
  public KucoinGetUntriggeredStopOrderListResponseData getData(){
    return data;
  }
  
  /**
   * @param data Response payload
   */
  public void setData(KucoinGetUntriggeredStopOrderListResponseData data) {
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
