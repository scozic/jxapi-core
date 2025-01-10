package com.scz.jxapi.exchanges.demo.gen;

import java.util.List;
import java.util.Properties;

import com.scz.jxapi.exchange.descriptor.ConfigProperty;
import com.scz.jxapi.exchange.descriptor.DefaultConfigProperty;
import com.scz.jxapi.exchange.descriptor.Type;
import com.scz.jxapi.util.PropertiesUtil;

/**
 * Configurable properties for <strong>DemoExchange</strong> exchange:<br>
 * <table>
 *   <caption>DemoExchange properties</caption>
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
 * </table>
 * <br>
 * Exposes helper methods are available to retrieve value of each of these properties with right type, returning default value if not present in properties
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see ConfigProperty
 */
public class DemoExchangeProperties {
  
  private DemoExchangeProperties(){}
  
  /**
   * Mock HTTP server host
   */
  public static final ConfigProperty HOST = DefaultConfigProperty.create(
    "host",
    Type.STRING,
    "Mock HTTP server host",
    "localhost");
  
  /**
   * Mock HTTP/Websocket server port
   */
  public static final ConfigProperty HTTP_PORT = DefaultConfigProperty.create(
    "httpPort",
    Type.INT,
    "Mock HTTP/Websocket server port",
    null);
  
  /**
   * Mock websocket server port
   */
  public static final ConfigProperty WEBSOCKET_PORT = DefaultConfigProperty.create(
    "websocketPort",
    Type.INT,
    "Mock websocket server port",
    null);
  
  /**
   * Mock websocket server expected heartBeat interval
   */
  public static final ConfigProperty WEBSOCKET_HEART_BEAT_INTERVAL = DefaultConfigProperty.create(
    "websocketHeartBeatInterval",
    Type.INT,
    "Mock websocket server expected heartBeat interval",
    "-1");
  
  /**
   * Retrieves value of 'host' property.
   * @param properties Properties to look for value of 'host' property into.
   * @return Value found in properties or default value 'localhost' if not found.
   */
  public static String getHost(Properties properties) {return PropertiesUtil.getStringProperty(properties, HOST.getName(), HOST.getDefaultValue());}
  
  /**
   * Retrieves value of 'httpPort' property.
   * @param properties Properties to look for value of 'httpPort' property into.
   * @return Value found in properties or <code>null</code> if not found.
   */
  public static Integer getHttpPort(Properties properties) {return PropertiesUtil.getIntProperty(properties, HTTP_PORT.getName(), HTTP_PORT.getDefaultValue());}
  
  /**
   * Retrieves value of 'websocketPort' property.
   * @param properties Properties to look for value of 'websocketPort' property into.
   * @return Value found in properties or <code>null</code> if not found.
   */
  public static Integer getWebsocketPort(Properties properties) {return PropertiesUtil.getIntProperty(properties, WEBSOCKET_PORT.getName(), WEBSOCKET_PORT.getDefaultValue());}
  
  /**
   * Retrieves value of 'websocketHeartBeatInterval' property.
   * @param properties Properties to look for value of 'websocketHeartBeatInterval' property into.
   * @return Value found in properties or default value '-1' if not found.
   */
  public static Integer getWebsocketHeartBeatInterval(Properties properties) {return PropertiesUtil.getIntProperty(properties, WEBSOCKET_HEART_BEAT_INTERVAL.getName(), WEBSOCKET_HEART_BEAT_INTERVAL.getDefaultValue());}
  
  /**
   * List of all configuration properties defined in this class
   */
  public static final List<ConfigProperty> ALL = List.of(
    HOST, 
    HTTP_PORT, 
    WEBSOCKET_PORT, 
    WEBSOCKET_HEART_BEAT_INTERVAL);
}
