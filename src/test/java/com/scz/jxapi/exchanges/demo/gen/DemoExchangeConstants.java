package com.scz.jxapi.exchanges.demo.gen;


/**
 * Constants used in {@link com.scz.jxapi.exchanges.demo.gen.DemoExchangeExchange} API wrapper
 */
public interface DemoExchangeConstants {
  
  /**
   * Ping message
   */
  String PING_MESSAGE = "Ping!";
  
  /**
   * Pong message
   */
  String PONG_MESSAGE = "Pong!";
  
  /**
   * Value to substitute with actual HTTP server host in httpUrl
   */
  String HTTP_SERVER_HOST_WILDCARD = "HTTPSERVERHOST";
  
  /**
   * Value to substitute with actual HTTP server host in httpUrl
   */
  String HTTP_SERVER_PORT_WILDCARD = "8080";
  
  /**
   * Value to substitute with actual HTTP server host in httpUrl
   */
  String WEBSOCKET_SERVER_HOST_WILDCARD = "MOCKWEBSOCKETSERVERHOST";
  
  /**
   * Value to substitute with actual HTTP server host in httpUrl
   */
  String WEBSOCKET_SERVER_PORT_WILDCARD = "8090";
  
  /**
   * Message to send to websocket server after connecting to complete handshake
   */
  String WEBSOCKET_LOGIN_MESSAGE = "Hello!";
  
  /**
   * Message to send to websocket server before disconnecting
   */
  String WEBSOCKET_LOGOUT_MESSAGE = "Bye!";
  
  /**
   * Possible value in <i>responseCode</i> field of rest request response: Successful response
   */
  Integer RESPONSE_CODE_OK = Integer.valueOf(200);
  
  /**
   * Possible value in <i>responseCode</i> field of rest request response: Server internal error
   */
  Integer RESPONSE_CODE_INTERNAL_SERVER_ERROR = Integer.valueOf(500);
}
