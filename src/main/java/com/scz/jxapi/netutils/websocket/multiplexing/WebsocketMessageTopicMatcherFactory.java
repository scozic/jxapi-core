package com.scz.jxapi.netutils.websocket.multiplexing;

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
	 * Factory that always returns {@link WebsocketMessageTopicMatcher#ANY_MATCHER}
	 */
	WebsocketMessageTopicMatcherFactory ANY_MATCHER_FACTORY = () -> WebsocketMessageTopicMatcher.ANY_MATCHER;
	
}
