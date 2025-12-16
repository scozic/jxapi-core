package org.jxapi.exchanges.demo.gen;

import java.util.List;
import java.util.Properties;

import javax.annotation.processing.Generated;
import org.jxapi.pojo.descriptor.Type;
import org.jxapi.util.CollectionUtil;
import org.jxapi.util.ConfigProperty;
import org.jxapi.util.DefaultConfigProperty;
import org.jxapi.util.PropertiesUtil;

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
 *     <td>baseHttpUrl</td>
 *     <td>STRING</td>
 *     <td>Mock HTTP server base API URL</td>
 *     <td>http://localhost:8080</td>
 *   </tr>
 *   <tr>
 *     <td>baseWebsocketUrl</td>
 *     <td>STRING</td>
 *     <td>Mock websocket server base API URL</td>
 *     <td>ws://localhost:8090</td>
 *   </tr>
 *   <tr>
 *     <td>ws.websocketHeartBeatInterval</td>
 *     <td>INT</td>
 *     <td>Mock websocket server expected heartBeat interval</td>
 *     <td>-1</td>
 *   </tr>
 * </table>
 * <br>
 * Exposes helper methods are available to retrieve value of each of these properties with right type, returning default value if not present in properties.
 * @see ConfigProperty
 */
@Generated("org.jxapi.generator.java.exchange.properties.PropertiesClassGenerator")
public class DemoExchangeProperties {
  
  private DemoExchangeProperties(){}
  
  /**
   * Mock HTTP server base API URL
   */
  public static final ConfigProperty BASE_HTTP_URL = DefaultConfigProperty.create(
    "baseHttpUrl",
    Type.STRING,
    "Mock HTTP server base API URL",
    "http://localhost:8080");
  
  /**
   * Mock websocket server base API URL
   */
  public static final ConfigProperty BASE_WEBSOCKET_URL = DefaultConfigProperty.create(
    "baseWebsocketUrl",
    Type.STRING,
    "Mock websocket server base API URL",
    "ws://localhost:8090");
  
  /**
   * Mock websocket server related properties
   */
  @Generated("org.jxapi.generator.java.exchange.properties.PropertiesClassGenerator")
  public static class Ws {
    
    private Ws(){}
    
    /**
     * Mock websocket server expected heartBeat interval
     */
    public static final ConfigProperty WEBSOCKET_HEART_BEAT_INTERVAL = DefaultConfigProperty.create(
      "ws.websocketHeartBeatInterval",
      Type.INT,
      "Mock websocket server expected heartBeat interval",
      "-1");
    
    /**
     * Retrieves value of 'websocketHeartBeatInterval' property.
     * @param properties Properties to look for value of 'websocketHeartBeatInterval' property into.
     * @return Value found in properties or default value <i>-1</i> if not found.
     */
    public static Integer getWebsocketHeartBeatInterval(Properties properties) {return PropertiesUtil.getInt(properties, WEBSOCKET_HEART_BEAT_INTERVAL);}
    /**
     * List of all configuration properties defined in this class
     */
    public static final List<ConfigProperty> ALL = List.copyOf(CollectionUtil.mergeLists(List.of(
      List.of(
        WEBSOCKET_HEART_BEAT_INTERVAL))));
  }
  
  /**
   * Retrieves value of 'baseHttpUrl' property.
   * @param properties Properties to look for value of 'baseHttpUrl' property into.
   * @return Value found in properties or default value <i>http://localhost:8080</i> if not found.
   */
  public static String getBaseHttpUrl(Properties properties) {return PropertiesUtil.getString(properties, BASE_HTTP_URL);}
  
  /**
   * Retrieves value of 'baseWebsocketUrl' property.
   * @param properties Properties to look for value of 'baseWebsocketUrl' property into.
   * @return Value found in properties or default value <i>ws://localhost:8090</i> if not found.
   */
  public static String getBaseWebsocketUrl(Properties properties) {return PropertiesUtil.getString(properties, BASE_WEBSOCKET_URL);}
  /**
   * List of all configuration properties defined in this class
   */
  public static final List<ConfigProperty> ALL = List.copyOf(CollectionUtil.mergeLists(List.of(
    List.of(
      BASE_HTTP_URL,
      BASE_WEBSOCKET_URL
    ),
    Ws.ALL)));
}
