package org.jxapi.netutils.websocket.multiplexing;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Static helper class for WebSocket Message Topic Matcher Factory creation.
 * @see WebsocketMessageTopicMatcherFactory
 */
public class WSMTMFUtil {
  
  private WSMTMFUtil() {
    // Static helper class
  }
  
  public static WebsocketMessageTopicMatcherFactory and(List<WebsocketMessageTopicMatcherFactory> operandFactories) {
    return () -> {
      List<WebsocketMessageTopicMatcher> matchers = operandFactories.stream()
          .map(WebsocketMessageTopicMatcherFactory::createWebsocketMessageTopicMatcher)
          .collect(Collectors.toList());
      return new AndWebsocketMessageTopicMatcher(matchers);
    };
  }
  
  public static WebsocketMessageTopicMatcherFactory or(List<WebsocketMessageTopicMatcherFactory> operandFactories) {
    return () -> {
      List<WebsocketMessageTopicMatcher> matchers = operandFactories.stream()
          .map(WebsocketMessageTopicMatcherFactory::createWebsocketMessageTopicMatcher)
          .collect(Collectors.toList());
      return new OrWebsocketMessageTopicMatcher(matchers);
    };
  }
  
  public static WebsocketMessageTopicMatcherFactory value(String fieldName, Object fieldValue) {
    String value = fieldValue != null ? fieldValue.toString() : null;
    return () -> new FieldValueWebsocketMessageTopicMatcher(fieldName, value);
  }
  
  public static WebsocketMessageTopicMatcherFactory regexp(String fieldName, String fieldValue) {
    return () -> new FieldRegexpWebsocketMessageTopicMatcher(fieldName, fieldValue);
  }
}
