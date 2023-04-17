package com.scz.jxapi.exchanges.kucoin.gen.futuresmarketdata.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jxapi.exchanges.kucoin.gen.futuresmarketdata.serializers.KucoinGetOpenContractListResponseSerializer;
import com.scz.jxapi.util.EncodingUtil;

import java.util.List;

/**
 * Response to Kucoin FuturesMarketData API GetOpenContractList REST endpoint request<br/>Submit request to get the info of all open contracts.<br/>See <a href="https://docs.kucoin.com/futures/#get-open-contract-list">API</a><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = KucoinGetOpenContractListResponseSerializer.class)
public class KucoinGetOpenContractListResponse {
  private String code;
  private List<KucoinGetOpenContractListResponseData> data;
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
  public List<KucoinGetOpenContractListResponseData> getData(){
    return data;
  }
  
  /**
   * @param data List of market information for each market symbol
   */
  public void setData(List<KucoinGetOpenContractListResponseData> data) {
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
