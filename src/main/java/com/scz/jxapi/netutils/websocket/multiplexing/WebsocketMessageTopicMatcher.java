package com.scz.jxapi.netutils.websocket.multiplexing;

/**
 * As for sake of performance, websockets are multiplexed and messages of different topics will can received on same socket, each Websocket Endpoint is expected to 
 * provide an implementation of this parser interface to route relevant messages to it. 
 * <p>
 * 
 */
public interface WebsocketMessageTopicMatcher {
	
	/**
	 * Upon reception of a message on a websocket, iteration on message fields will
	 * be performed and this method will be called on every
	 * {@link WebsocketMessageTopicMatcher} (one for each endpoint), for each field
	 * of message with is value, until it returns either
	 * {@link WebsocketMessageTopicMatchStatus#MATCHED} or
	 * {@link WebsocketMessageTopicMatchStatus#CANT_MATCH} for one endpoint.<br/>
	 * Implementations may match a message across multiple fields.
	 * {@link WebsocketMessageTopicMatchStatus#MATCHED}. should be returned only
	 * when all fields have been matched.
	 * 
	 * @param fieldName
	 * @param value
	 */
	WebsocketMessageTopicMatchStatus matches(String fieldName, String value);
	
	/**
	 * @return Current status of parser, value returned upon last call to {@link #matches(String, String)}
	 */
	WebsocketMessageTopicMatchStatus getStatus();
	
	/**
	 * Specific matcher that matches any field/value pair and is always in {@link WebsocketMessageTopicMatchStatus#MATCHED} state.
	 */
	WebsocketMessageTopicMatcher ANY_MATCHER = new WebsocketMessageTopicMatcher() {
		
		@Override
		public WebsocketMessageTopicMatchStatus matches(String fieldName, String value) {
			return WebsocketMessageTopicMatchStatus.MATCHED;
		}
		
		@Override
		public WebsocketMessageTopicMatchStatus getStatus() {
			return WebsocketMessageTopicMatchStatus.MATCHED;
		}
	};

}
