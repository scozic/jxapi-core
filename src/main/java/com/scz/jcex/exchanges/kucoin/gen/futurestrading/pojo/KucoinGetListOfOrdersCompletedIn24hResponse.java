package com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.serializers.KucoinGetListOfOrdersCompletedIn24hResponseSerializer;
import com.scz.jcex.util.EncodingUtil;
import java.util.List;

/**
 * Response to Kucoin FuturesTrading API GetListOfOrdersCompletedIn24h REST endpoint request<br/>Get a list of recent 1000 orders in the last 24 hours.<br/>If you need to get your recent traded order history with low latency, you may query this endpoint.<br/>See <a href="https://docs.kucoin.com/futures/#get-list-of-orders-completed-in-24h">API</a><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = KucoinGetListOfOrdersCompletedIn24hResponseSerializer.class)
public class KucoinGetListOfOrdersCompletedIn24hResponse {
  private String code;
  private List<KucoinGetListOfOrdersCompletedIn24hResponseData> data;
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
   * @return Order list
   */
  public List<KucoinGetListOfOrdersCompletedIn24hResponseData> getData(){
    return data;
  }
  
  /**
   * @param data Order list
   */
  public void setData(List<KucoinGetListOfOrdersCompletedIn24hResponseData> data) {
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
