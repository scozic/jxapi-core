package com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.serializers.KucoinApplyConnectTokenPrivateResponseDataInstanceServersSerializer;
import com.scz.jcex.util.EncodingUtil;

/**
 * List of market information for each market symbol
 */
@JsonSerialize(using = KucoinApplyConnectTokenPrivateResponseDataInstanceServersSerializer.class)
public class KucoinApplyConnectTokenPrivateResponseDataInstanceServers {
  private Boolean encrypt;
  private String endpoint;
  private Long pingInterval;
  private Long pingTimeout;
  private String protocol;
  
  /**
   * @return Indicate whether SSL encryption is used
   */
  public Boolean isEncrypt(){
    return encrypt;
  }
  
  /**
   * @param encrypt Indicate whether SSL encryption is used
   */
  public void setEncrypt(Boolean encrypt) {
    this.encrypt = encrypt;
  }
  
  /**
   * @return Websocket server address for establishing connection
   */
  public String getEndpoint(){
    return endpoint;
  }
  
  /**
   * @param endpoint Websocket server address for establishing connection
   */
  public void setEndpoint(String endpoint) {
    this.endpoint = endpoint;
  }
  
  /**
   * @return Recommended to send ping interval in millisecond
   */
  public Long getPingInterval(){
    return pingInterval;
  }
  
  /**
   * @param pingInterval Recommended to send ping interval in millisecond
   */
  public void setPingInterval(Long pingInterval) {
    this.pingInterval = pingInterval;
  }
  
  /**
   * @return Recommended to send ping interval in millisecond
   */
  public Long getPingTimeout(){
    return pingTimeout;
  }
  
  /**
   * @param pingTimeout Recommended to send ping interval in millisecond
   */
  public void setPingTimeout(Long pingTimeout) {
    this.pingTimeout = pingTimeout;
  }
  
  /**
   * @return Protocol supported
   */
  public String getProtocol(){
    return protocol;
  }
  
  /**
   * @param protocol Protocol supported
   */
  public void setProtocol(String protocol) {
    this.protocol = protocol;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
