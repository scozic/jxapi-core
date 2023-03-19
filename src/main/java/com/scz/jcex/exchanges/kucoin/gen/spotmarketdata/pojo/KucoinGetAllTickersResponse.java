package com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.serializers.KucoinGetAllTickersResponseSerializer;
import com.scz.jcex.util.EncodingUtil;
import java.util.List;

/**
 * Response to Kucoin SpotMarketData API getAllTickers REST endpoint request<br/>Request via this endpoint to get Level 1 Market Data. The returned value includes the best bid price and size, the best ask price and size as well as the last traded price and the last traded size.<br/>See <a href="https://docs.kucoin.com/#get-all-tickers">API</a><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = KucoinGetAllTickersResponseSerializer.class)
public class KucoinGetAllTickersResponse {
  private String code;
  private List<KucoinGetAllTickersResponseData> data;
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
  public List<KucoinGetAllTickersResponseData> getData(){
    return data;
  }
  
  /**
   * @param data List of market information for each market symbol
   */
  public void setData(List<KucoinGetAllTickersResponseData> data) {
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
