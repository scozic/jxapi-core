package org.jxapi.netutils.websocket.multiplexing;

import java.util.List;

/**
 * Static helper class for WebSocket Message Topic Matcher Factory creation.
 * @see WebsocketMessageTopicMatcherFactory
 */
public class WSMTMFUtil {
  
  private WSMTMFUtil() {
    // Static helper class
  }
  
  /**
   * Creates a factory for an AND matcher composed of the given operand factories.
   * @param operandFactories the operand matcher factories
   * @return the AND matcher factory
   * @see AndWebsocketMessageTopicMatcher
   */
  public static WebsocketMessageTopicMatcherFactory and(List<WebsocketMessageTopicMatcherFactory> operandFactories) {
    return () -> {
      List<WebsocketMessageTopicMatcher> matchers = operandFactories.stream()
          .map(WebsocketMessageTopicMatcherFactory::createWebsocketMessageTopicMatcher)
          .toList();
      return new AndWebsocketMessageTopicMatcher(matchers);
    };
  }
  
  /**
   * Creates a factory for an OR matcher composed of the given operand factories.
   * 
   * @param operandFactories the operand matcher factories
   * @return the OR matcher factory
   * @see OrWebsocketMessageTopicMatcher
   */
  public static WebsocketMessageTopicMatcherFactory or(List<WebsocketMessageTopicMatcherFactory> operandFactories) {
    return () -> {
      List<WebsocketMessageTopicMatcher> matchers = operandFactories.stream()
          .map(WebsocketMessageTopicMatcherFactory::createWebsocketMessageTopicMatcher)
          .toList();
      return new OrWebsocketMessageTopicMatcher(matchers);
    };
  }
  
  /**
   * Creates a factory for matching a specific value of a specified field.
   * 
   * @param fieldName  the field name to match
   * @param fieldValue the field value to match
   * @return the field value matcher factory
   * @see FieldValueWebsocketMessageTopicMatcher
   */
  public static WebsocketMessageTopicMatcherFactory value(String fieldName, Object fieldValue) {
    String value = fieldValue != null ? fieldValue.toString() : null;
    return () -> new FieldValueWebsocketMessageTopicMatcher(fieldName, value);
  }
  
  /**
   * Creates a factory for matching a specific value of a specified field using a
   * regular expression.
   * 
   * @param fieldName  the field name to match
   * @param fieldValue the field regexp to match
   * @return the field regexp matcher factory
   * @see FieldRegexpWebsocketMessageTopicMatcher
   */
  public static WebsocketMessageTopicMatcherFactory regexp(String fieldName, String fieldValue) {
    return () -> new FieldRegexpWebsocketMessageTopicMatcher(fieldName, fieldValue);
  }
}
