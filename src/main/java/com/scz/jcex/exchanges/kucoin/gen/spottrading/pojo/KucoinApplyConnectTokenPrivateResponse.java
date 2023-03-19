package com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.serializers.KucoinApplyConnectTokenPrivateResponseSerializer;
import com.scz.jcex.util.EncodingUtil;

/**
 * Response to Kucoin SpotTrading API ApplyConnectTokenPrivate REST endpoint request<br/>You need to apply for one of the two tokens below to create a websocket connection..<br/>See <a href="https://docs.kucoin.com/#apply-connect-token">API</a><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = KucoinApplyConnectTokenPrivateResponseSerializer.class)
public class KucoinApplyConnectTokenPrivateResponse {
  private String code;
  private KucoinApplyConnectTokenPrivateResponseData data;
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
  public KucoinApplyConnectTokenPrivateResponseData getData(){
    return data;
  }
  
  /**
   * @param data Response payload
   */
  public void setData(KucoinApplyConnectTokenPrivateResponseData data) {
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
