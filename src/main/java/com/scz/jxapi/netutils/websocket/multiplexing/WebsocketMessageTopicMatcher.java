package com.scz.jxapi.netutils.websocket.multiplexing;

/**
 * As for sake of performance, websockets are multiplexed and messages of different topics will can received on same socket, each Websocket Endpoint is expected to 
 * provide an implementation of this parser interface to route relevant messages to it. 
 * <p>
 * It is expected to be used a follows:<br>
 * Upon each call to  {@link #matches(String, String)}, checks if its status is still {@link WebsocketMessageTopicMatchStatus#NO_MATCH}:
 * <ul>
 *  <li>If the list of matching fields configured is emtpty or <code>null</code>, the matcher will match any message field, and will be therefore always in {@link WebsocketMessageTopicMatchStatus#MATCHED} state.  
 *  <li>If it is not, parser is already in a terminal status which is returned.
 *  <li>If it is, checks if field corresponds to an expected one:
 *  <ul>
 *   <li>If not, parser remains in {@link WebsocketMessageTopicMatchStatus#NO_MATCH} status
 *   <li>If it does, expected value is checked against input:
 *   <ul>
 *    <li>If matches expected input, the field has expected value. That field is removed from expected fields, if there are no more expected fields, parser switches to {@link WebsocketMessageTopicMatchStatus#MATCHED} if
 *    <li>If does not, message carries a field with a matching field but not matching expected value, this message cannot be matched and parser switches to {@link WebsocketMessageTopicMatchStatus#CANT_MATCH}.   
 *   </ul>
 *  </ul>
 * </ul>
 * 
 */
public interface WebsocketMessageTopicMatcher {
	
	/**
	 * Upon reception of a message on a websocket, iteration on message fields will
	 * be performed and this method will be called on every
	 * {@link WebsocketMessageTopicMatcher} (one for each endpoint), for each field
	 * of message with is value, until it returns either
	 * {@link WebsocketMessageTopicMatchStatus#MATCHED} or
	 * {@link WebsocketMessageTopicMatchStatus#CANT_MATCH} for one endpoint.<br>
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
