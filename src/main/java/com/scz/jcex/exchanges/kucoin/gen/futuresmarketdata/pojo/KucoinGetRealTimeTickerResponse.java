package com.scz.jcex.exchanges.kucoin.gen.futuresmarketdata.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.exchanges.kucoin.gen.futuresmarketdata.serializers.KucoinGetRealTimeTickerResponseSerializer;
import com.scz.jcex.util.EncodingUtil;

/**
 * Response to Kucoin FuturesMarketData API GetRealTimeTicker REST endpoint request<br/>The real-time ticker includes the last traded price, the last traded size, transaction ID, the side of liquidity taker, the best bid price and size, the best ask price and size as well as the transaction time of the orders. These messages can also be obtained through Websocket. The Sequence Number is used to judge whether the messages pushed by Websocket is continuous.<br/>See <a href="https://docs.kucoin.com/futures/#get-real-time-ticker">API</a><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = KucoinGetRealTimeTickerResponseSerializer.class)
public class KucoinGetRealTimeTickerResponse {
  private String code;
  private KucoinGetRealTimeTickerResponseData data;
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
   * @return List of market information for each market symbol
   */
  public KucoinGetRealTimeTickerResponseData getData(){
    return data;
  }
  
  /**
   * @param data List of market information for each market symbol
   */
  public void setData(KucoinGetRealTimeTickerResponseData data) {
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
