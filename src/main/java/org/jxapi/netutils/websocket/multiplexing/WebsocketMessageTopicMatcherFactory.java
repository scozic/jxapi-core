package org.jxapi.netutils.websocket.multiplexing;

import java.util.List;

/**
 * Factory interface for creating {@link WebsocketMessageTopicMatcher} instances.
 */
public interface WebsocketMessageTopicMatcherFactory {

  /**
   * @return a {@link WebsocketMessageTopicMatcher} instance ready to match
   *         against an incoming websocket message fields.
   */
  WebsocketMessageTopicMatcher createWebsocketMessageTopicMatcher();
  
  /**
   * Factory method to create a {@link WebsocketMessageTopicMatcherFactory}
   * instance
   * that instantiaties {@link WebsocketMessageTopicMatcher} intances that will
   * attempt to match a list of fields, see {@link WebsocketMessageTopicMatcher}.
   * <p>
   * An empty list of fields will means matcher will match against any incoming
   * field/value pair, see {@link WebsocketMessageTopicMatcher#ANY_MATCHER}.
   * 
   * @param fieldNamesAndValues A list of field names and values to match against
   *                            incoming websocket messages.
   * @return a {@link WebsocketMessageTopicMatcherFactory} instance that will
   *         create {@link WebsocketMessageTopicMatcher} instances that will match
   *         against the given fields.
   * 
   * @see DefaultWebsocketMessageTopicMatcher
   */
  @Deprecated
  static WebsocketMessageTopicMatcherFactory create(String... fieldNamesAndValues) {
    if (fieldNamesAndValues.length <= 0) {
      return ANY_MATCHER_FACTORY;
    }
    List<WebsocketMessageTopicMatcherField> fieldList = WebsocketMessageTopicMatcherField.createList(fieldNamesAndValues);
    return () -> new DefaultWebsocketMessageTopicMatcher(fieldList);
  }

  /**
   * Factory that always returns {@link WebsocketMessageTopicMatcher#ANY_MATCHER}
   */
  WebsocketMessageTopicMatcherFactory ANY_MATCHER_FACTORY = () -> WebsocketMessageTopicMatcher.ANY_MATCHER;
  

}
