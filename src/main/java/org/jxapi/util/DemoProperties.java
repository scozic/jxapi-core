package org.jxapi.util;

import java.util.List;
import java.util.Properties;

import org.jxapi.pojo.descriptor.Type;

/**
 * Lists configuration properties uused in demo snippets
 */
public class DemoProperties {
  
  private DemoProperties() {}
  
  /**
   * System property to specify the properties file to use for running the demo snippets.
   */
  public static final String DEMO_API_PROPERTIES_FILE_SYSTEM_PROPERTY = "jxapi.testProperties";

  /**
   * Property controlling the duration of the subscription in
   * WebSocket endpoint demo classes.
   */
  public static final ConfigProperty DEMO_WS_SUBSCRIPTION_DURATION_PROPERTY = DefaultConfigProperty.create(
      "jxapi.demo.ws.subscriptionDuration", 
      Type.LONG, 
      "The duration in ms of the subscription in websocket endpoint demo classes", 
      30000L);
  
  /**
   * Property controlling the delay before exiting program after
   * unsubscribing in WebSocket endpoint demo classes.
   */
  public static final ConfigProperty DEMO_WS_DELAY_BEFORE_EXIT_AFTER_UNSUBSCRIPTION_PROPERTY = DefaultConfigProperty.create(
      "jxapi.demo.ws.delayBeforeExitAfterUnsubscription", 
      Type.LONG, 
      "Delay in ms before exiting program after unsubscribing in websocked endpoint demo classes.", 
      1000L);
  
  /**
   * List of all configuration properties used in demo snippets
   */
  public static final List<ConfigProperty> ALL = List.of(
      DEMO_WS_SUBSCRIPTION_DURATION_PROPERTY, 
      DEMO_WS_DELAY_BEFORE_EXIT_AFTER_UNSUBSCRIPTION_PROPERTY);
  
  /**
   * Returns the value of the property controlling the duration of the
   * subscription in WebSocket endpoint demo classes.
   * 
   * @param properties Properties object to read the value from
   * @return The value of the property controlling the duration of the
   *         subscription in WebSocket endpoint demo classes.
   */
  public static final long getWebsocketSubscriptionDuration(Properties properties) {
    return PropertiesUtil.getLong(
          properties, 
          DEMO_WS_SUBSCRIPTION_DURATION_PROPERTY.getName(), 
          DEMO_WS_SUBSCRIPTION_DURATION_PROPERTY.getDefaultValue());
  }
  
  /**
   * Returns the value of the property controlling the delay before exiting
   * program after unsubscribing in WebSocket endpoint demo classes.
   * 
   * @param properties Properties object to read the value from
   * @return The value of the property controlling the delay before exiting
   *         program after unsubscribing in WebSocket endpoint demo classes.
   */
  public static final long getWebsocketDelayBeforeExit(Properties properties) {
    return PropertiesUtil.getLong(
          properties, 
          DEMO_WS_DELAY_BEFORE_EXIT_AFTER_UNSUBSCRIPTION_PROPERTY.getName(), 
          DEMO_WS_DELAY_BEFORE_EXIT_AFTER_UNSUBSCRIPTION_PROPERTY.getDefaultValue());
  }
}
