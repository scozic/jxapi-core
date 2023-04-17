package com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.serializers.KucoinApplyConnectTokenPrivateResponseDataSerializer;
import com.scz.jxapi.util.EncodingUtil;

import java.util.List;

/**
 * Response payload
 */
@JsonSerialize(using = KucoinApplyConnectTokenPrivateResponseDataSerializer.class)
public class KucoinApplyConnectTokenPrivateResponseData {
  private List<KucoinApplyConnectTokenPrivateResponseDataInstanceServers> instanceServers;
  private String token;
  
  /**
   * @return List of market information for each market symbol
   */
  public List<KucoinApplyConnectTokenPrivateResponseDataInstanceServers> getInstanceServers(){
    return instanceServers;
  }
  
  /**
   * @param instanceServers List of market information for each market symbol
   */
  public void setInstanceServers(List<KucoinApplyConnectTokenPrivateResponseDataInstanceServers> instanceServers) {
    this.instanceServers = instanceServers;
  }
  
  /**
   * @return Token
   */
  public String getToken(){
    return token;
  }
  
  /**
   * @param token Token
   */
  public void setToken(String token) {
    this.token = token;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
