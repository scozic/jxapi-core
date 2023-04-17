package com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.serializers.KucoinGet24hrStatsResponseSerializer;
import com.scz.jxapi.util.EncodingUtil;

/**
 * Response to Kucoin SpotMarketData API get24hrStats REST endpoint request<br/>Request via this endpoint to get the statistics of the specified ticker in the last 24 hours.<br/>See <a href="https://docs.kucoin.com/#get-24hr-stats">API</a><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = KucoinGet24hrStatsResponseSerializer.class)
public class KucoinGet24hrStatsResponse {
  private String code;
  private KucoinGet24hrStatsResponseData data;
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
  public KucoinGet24hrStatsResponseData getData(){
    return data;
  }
  
  /**
   * @param data List of market information for each market symbol
   */
  public void setData(KucoinGet24hrStatsResponseData data) {
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
