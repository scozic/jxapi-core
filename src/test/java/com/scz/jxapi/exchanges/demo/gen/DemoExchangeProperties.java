package com.scz.jxapi.exchanges.demo.gen;

import java.util.Properties;

import com.scz.jxapi.util.PropertiesUtil;

/**
 * Configurable properties for <strong>DemoExchange</strong> exchange:<br>
 * <table>
 *   <tr>
 *     <th>Name</th>
 *     <th>Type</th>
 *     <th>Description</th>
 *     <th>Default value</th>
 *   </tr>
 *   <tr>
 *     <td>host</td>
 *     <td>STRING</td>
 *     <td>Mock HTTP server host</td>
 *     <td>localhost</td>
 *   </tr>
 *   <tr>
 *     <td>httpPort</td>
 *     <td>INT</td>
 *     <td>Mock HTTP/Websocket server port</td>
 *     <td></td>
 *   </tr>
 *   <tr>
 *     <td>websocketPort</td>
 *     <td>INT</td>
 *     <td>Mock websocket server port</td>
 *     <td></td>
 *   </tr>
 *   <tr>
 *     <td>websocketHeartBeatInterval</td>
 *     <td>INT</td>
 *     <td>Mock websocket server expected heartBeat interval</td>
 *     <td>-1</td>
 *   </tr>
 *   <tr>
 *     <td>responseCodeOk</td>
 *     <td>INT</td>
 *     <td>Possible value in <i>responseCode</i> field of rest request response: Successful response</td>
 *     <td>200</td>
 *   </tr>
 *   <tr>
 *     <td>responseCodeInternalServerError</td>
 *     <td>INT</td>
 *     <td>Possible value in <i>responseCode</i> field of rest request response: Server internal error</td>
 *     <td>500</td>
 *   </tr>
 * </table><br>
 * Exposes helper methods are available to retrieve value of each of these properties with right type, returning default value if not present in properties
 */
public interface DemoExchangeProperties {
  
  /**
   * 'host' property key.<br>
   * Mock HTTP server host<br>
   * Property value type:STRING
   */
  String HOST_PROPERTY = "host";
  
  /**
   * {@link #HOST_PROPERTY} property default value
   */
  String HOST_DEFAULT_VALUE = "localhost";
  
  /**
   * 'httpPort' property key.<br>
   * Mock HTTP/Websocket server port<br>
   * Property value type:INT
   */
  String HTTP_PORT_PROPERTY = "httpPort";
  
  /**
   * 'websocketPort' property key.<br>
   * Mock websocket server port<br>
   * Property value type:INT
   */
  String WEBSOCKET_PORT_PROPERTY = "websocketPort";
  
  /**
   * 'websocketHeartBeatInterval' property key.<br>
   * Mock websocket server expected heartBeat interval<br>
   * Property value type:INT
   */
  String WEBSOCKET_HEART_BEAT_INTERVAL_PROPERTY = "websocketHeartBeatInterval";
  
  /**
   * {@link #WEBSOCKET_HEART_BEAT_INTERVAL_PROPERTY} property default value
   */
  Integer WEBSOCKET_HEART_BEAT_INTERVAL_DEFAULT_VALUE = Integer.valueOf(-1);
  
  /**
   * 'responseCodeOk' property key.<br>
   * Possible value in <i>responseCode</i> field of rest request response: Successful response<br>
   * Property value type:INT
   */
  String RESPONSE_CODE_OK_PROPERTY = "responseCodeOk";
  
  /**
   * {@link #RESPONSE_CODE_OK_PROPERTY} property default value
   */
  Integer RESPONSE_CODE_OK_DEFAULT_VALUE = Integer.valueOf(200);
  
  /**
   * 'responseCodeInternalServerError' property key.<br>
   * Possible value in <i>responseCode</i> field of rest request response: Server internal error<br>
   * Property value type:INT
   */
  String RESPONSE_CODE_INTERNAL_SERVER_ERROR_PROPERTY = "responseCodeInternalServerError";
  
  /**
   * {@link #RESPONSE_CODE_INTERNAL_SERVER_ERROR_PROPERTY} property default value
   */
  Integer RESPONSE_CODE_INTERNAL_SERVER_ERROR_DEFAULT_VALUE = Integer.valueOf(500);
  
  /**
   * Retrieves value of 'host' property.
   * @param properties Properties to look for value of 'host' property into.@return Value found in properties or default value 'localhost' if not found.
   */
  default String getHost(Properties properties) {return PropertiesUtil.getStringProperty(properties, HOST_PROPERTY, HOST_DEFAULT_VALUE);}
  
  /**
   * Retrieves value of 'httpPort' property.
   * @param properties Properties to look for value of 'httpPort' property into.@return Value found in properties or <code>null</code> if not found.
   */
  default Integer getHttpPort(Properties properties) {return PropertiesUtil.getIntProperty(properties, HTTP_PORT_PROPERTY, null);}
  
  /**
   * Retrieves value of 'websocketPort' property.
   * @param properties Properties to look for value of 'websocketPort' property into.@return Value found in properties or <code>null</code> if not found.
   */
  default Integer getWebsocketPort(Properties properties) {return PropertiesUtil.getIntProperty(properties, WEBSOCKET_PORT_PROPERTY, null);}
  
  /**
   * Retrieves value of 'websocketHeartBeatInterval' property.
   * @param properties Properties to look for value of 'websocketHeartBeatInterval' property into.@return Value found in properties or default value '-1' if not found.
   */
  default Integer getWebsocketHeartBeatInterval(Properties properties) {return PropertiesUtil.getIntProperty(properties, WEBSOCKET_HEART_BEAT_INTERVAL_PROPERTY, WEBSOCKET_HEART_BEAT_INTERVAL_DEFAULT_VALUE);}
  
  /**
   * Retrieves value of 'responseCodeOk' property.
   * @param properties Properties to look for value of 'responseCodeOk' property into.@return Value found in properties or default value '200' if not found.
   */
  default Integer getResponseCodeOk(Properties properties) {return PropertiesUtil.getIntProperty(properties, RESPONSE_CODE_OK_PROPERTY, RESPONSE_CODE_OK_DEFAULT_VALUE);}
  
  /**
   * Retrieves value of 'responseCodeInternalServerError' property.
   * @param properties Properties to look for value of 'responseCodeInternalServerError' property into.@return Value found in properties or default value '500' if not found.
   */
  default Integer getResponseCodeInternalServerError(Properties properties) {return PropertiesUtil.getIntProperty(properties, RESPONSE_CODE_INTERNAL_SERVER_ERROR_PROPERTY, RESPONSE_CODE_INTERNAL_SERVER_ERROR_DEFAULT_VALUE);}
}
