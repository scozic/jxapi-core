package com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.serializers.KucoinListAccountsResponseSerializer;
import com.scz.jcex.util.EncodingUtil;
import java.util.List;

/**
 * Response to Kucoin SpotTrading API ListAccounts REST endpoint request<br/>Get a list of accounts. Please deposit funds to the main account firstly, then transfer the funds to the trade account via Inner Transfer before transaction..<br/>See <a href="https://docs.kucoin.com/#list-accounts">API</a><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = KucoinListAccountsResponseSerializer.class)
public class KucoinListAccountsResponse {
  private String code;
  private List<KucoinListAccountsResponseData> data;
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
  public List<KucoinListAccountsResponseData> getData(){
    return data;
  }
  
  /**
   * @param data Response payload
   */
  public void setData(List<KucoinListAccountsResponseData> data) {
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
