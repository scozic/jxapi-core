package org.jxapi.netutils.websocket.multiplexing;

/**
 * Factory interface for creating {@link WebsocketMessageTopicMatcher} instances.
 * <p>
 * Remark: Implementations of this interface must be thread-safe.
 * Usually, instances are created using {@link WSMTMFUtil} static methods.
 */
public interface WebsocketMessageTopicMatcherFactory {

  /**
   * @return a {@link WebsocketMessageTopicMatcher} instance ready to match
   *         against an incoming websocket message fields.
   */
  WebsocketMessageTopicMatcher createWebsocketMessageTopicMatcher();

  /**
   * Factory that always returns {@link WebsocketMessageTopicMatcher#ANY_MATCHER}
   */
  WebsocketMessageTopicMatcherFactory ANY_MATCHER_FACTORY = () -> WebsocketMessageTopicMatcher.ANY_MATCHER;
  

}
