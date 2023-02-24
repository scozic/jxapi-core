package com.scz.jcex.netutils.websocket;

/**
 * As for sake of performance, websockets are multiplexed and messages of many topics will be received on same socket, each Websocket Endpoint is expected to 
 * provide an implementation of this parser interface to route relevant messages to it. 
 */
public interface WebsocketMessageTopicMatcher {
	
	/**
	 * Upon reception of a message on a websocket, iteration on message fields will
	 * be performed and this method will be called on every
	 * {@link WebsocketMessageTopicMatcher} (one for each endpoint), for each field
	 * of message with is value, until it returns true for one endpoint.<br/>
	 * Implementations may match a message across multiple fields. <code>true</code>
	 * should be returned when all fields have been matched.
	 * 
	 * @param fieldName
	 * @param value
	 */
	WebsocketMessageTopicMatchStatus matches(String fieldName, String value);
	
	/**
	 * Must called before parsing a message to reinitialize parser
	 */
	void reset();
	
	/**
	 * @return Current status of parser, value returned upon last call to {@link #matches(String, String)}
	 */
	WebsocketMessageTopicMatchStatus getStatus();

}
