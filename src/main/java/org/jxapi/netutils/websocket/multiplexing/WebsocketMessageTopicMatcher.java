package org.jxapi.netutils.websocket.multiplexing;

/**
 * As for sake of performance, websockets are multiplexed and messages of different topics will can received on same socket, each Websocket Endpoint is expected to 
 * provide an implementation of this parser interface to route relevant messages to it. 
 * <p>
 * It is expected to be used a follows:<br>
 * Upon each call to  {@link #matches(String, String)}, checks if its status is still {@link WebsocketMessageTopicMatchStatus#NO_MATCH}:
 * <ul>
 *  <li>If not, returns current status (e.g. {@link WebsocketMessageTopicMatchStatus#MATCHED} or {@link WebsocketMessageTopicMatchStatus#CANT_MATCH}</li>
 *  <li>If yes, checks if given fieldName/value pair matches expected criteria. If yes, updates internal state accordingly and returns current status.
 * </ul>
 * <p>
 * Implementation instances of this interface must be reusable and can be reset to initial state (all fields expected) using {@link #reset()} method.
 * 
 *  @see WebsocketMessageTopicMatchStatus
 *  @see FieldValueWebsocketMessageTopicMatcher
 *  @see FieldRegexpWebsocketMessageTopicMatcher
 *  @see AndWebsocketMessageTopicMatcher
 *  @see OrWebsocketMessageTopicMatcher
 *  @see WebsocketMessageTopicMatcherFactory
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
   * @param fieldName Field name
   * @param value     Field value
   * @return {@link WebsocketMessageTopicMatchStatus#MATCHED} if field matches.
   *         {@link WebsocketMessageTopicMatchStatus#NO_MATCH} if field does not
   *         match. {@link WebsocketMessageTopicMatchStatus#CANT_MATCH} if field
   *         matches but not expected value.
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

    @Override
    public void reset() {
      // Nothing, always in terminal state
    }
  };
  
  /**
   * Resets this matcher to its initial state so it can be reused.
   */
  void reset();

}
