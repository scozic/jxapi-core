package org.jxapi.exchanges.demo.gen;

import javax.annotation.processing.Generated;

/**
 * Constants used in {@link org.jxapi.exchanges.demo.gen.DemoExchangeExchange} API wrapper
 */
@Generated("org.jxapi.generator.java.exchange.constants.ConstantsClassGenerator")
public class DemoExchangeConstants {
  
  private DemoExchangeConstants(){}
  
  /**
   * Ping message
   */
  public static final String PING_MESSAGE = "Ping!";
  
  /**
   * Pong message
   */
  public static final String PONG_MESSAGE = "Pong!";
  
  /**
   * Message to send to websocket server after connecting to complete handshake
   */
  public static final String WEBSOCKET_LOGIN_MESSAGE = "Hello!";
  
  /**
   * Message to send to websocket server before disconnecting
   */
  public static final String WEBSOCKET_LOGOUT_MESSAGE = "Bye!";
  
  /**
   * Possible value in <i>responseCode</i> field of rest request response: Successful response
   */
  public static final Integer RESPONSE_CODE_OK = Integer.valueOf("200");
  
  /**
   * Possible value in <i>responseCode</i> field of rest request response: Server internal error
   */
  public static final Integer RESPONSE_CODE_INTERNAL_SERVER_ERROR = Integer.valueOf("500");
}
