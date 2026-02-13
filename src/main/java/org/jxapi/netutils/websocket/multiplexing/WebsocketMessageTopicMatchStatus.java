package org.jxapi.netutils.websocket.multiplexing;

/**
 * Possible states of {@link WebsocketMessageTopicMatcher}
 */
public enum WebsocketMessageTopicMatchStatus {
  /** Parser has not matched yet, but it could still match*/
  NO_MATCH,
  
  /** Parser has matched message*/
  MATCHED,
  
  /**
   * Parser has not matched and will not matched against current input, because
   * one of selecting fields has been found, with invalid value
   */
  CANT_MATCH
}
