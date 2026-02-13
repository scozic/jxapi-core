package org.jxapi.exchanges.demo.gen;

import javax.annotation.processing.Generated;
import org.jxapi.util.EncodingUtil;

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
   * Default age
   */
  public static final Integer DEFAULT_AGE = Integer.valueOf("18");
  
  /**
   * User data
   */
  @Generated("org.jxapi.generator.java.exchange.constants.ConstantsClassGenerator")
  public static class User {
    
    private User(){}
    
    /**
     * User ID
     */
    public static final String ID = "user1";
    
    /**
     * User age
     */
    public static final Integer AGE = Integer.valueOf(EncodingUtil.substituteArguments("${constants.defaultAge}", "constants.defaultAge", DemoExchangeConstants.DEFAULT_AGE));
  }
  
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
  
  /**
   * Constant for subscribing to all tickers
   */
  public static final String ALL_TICKERS = "ticker@all";
  
  /**
   * Default symbol to use in ticker related messages
   */
  public static final String DEFAULT_SYMBOL = "BTC_USDT";
}
