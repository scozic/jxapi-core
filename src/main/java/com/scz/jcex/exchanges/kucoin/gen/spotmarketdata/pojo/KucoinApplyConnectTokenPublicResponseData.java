package com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.serializers.KucoinApplyConnectTokenPublicResponseDataSerializer;
import com.scz.jcex.util.EncodingUtil;
import java.util.List;

/**
 * Response payload
 */
@JsonSerialize(using = KucoinApplyConnectTokenPublicResponseDataSerializer.class)
public class KucoinApplyConnectTokenPublicResponseData {
  private List<KucoinApplyConnectTokenPublicResponseDataInstanceServers> instanceServers;
  private String token;
  
  /**
   * @return List of market information for each market symbol
   */
  public List<KucoinApplyConnectTokenPublicResponseDataInstanceServers> getInstanceServers(){
    return instanceServers;
  }
  
  /**
   * @param instanceServers List of market information for each market symbol
   */
  public void setInstanceServers(List<KucoinApplyConnectTokenPublicResponseDataInstanceServers> instanceServers) {
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
