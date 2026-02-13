package org.jxapi.netutils.websocket.mock;

import org.jxapi.netutils.websocket.WebsocketClient;

/**
 * Event that is sent by a {@link MockWebsocketHook} when one of its methods is
 * called.
 */
public class MockWebsocketHookEvent {

  /**
   * @param source           the source of the event
   * @param websocketClient manager associated to this event.
   * @return A new {@link MockWebsocketHookEventType#INIT} event for the given
   *         {@link MockWebsocketHook} and {@link WebsocketClient}.
   */
  public static MockWebsocketHookEvent createInitEvent(MockWebsocketHook source, WebsocketClient websocketClient) {
    MockWebsocketHookEvent res = new MockWebsocketHookEvent(MockWebsocketHookEventType.INIT, source);
    res.setWebsocketClient(websocketClient);
    return res;
  }

  /**
   * @param source the source of the event
   * @return A new {@link MockWebsocketHookEventType#BEFORE_CONNECT} event for the
   *         given {@link MockWebsocketHook}.
   */
  public static MockWebsocketHookEvent createBeforeConnectEvent(MockWebsocketHook source) {
    return new MockWebsocketHookEvent(MockWebsocketHookEventType.BEFORE_CONNECT, source);
  }

  /**
   * @param source the source of the event
   * @return A new {@link MockWebsocketHookEventType#AFTER_CONNECT} event for the
   *         given {@link MockWebsocketHook}.
   */
  public static MockWebsocketHookEvent createAfterConnectEvent(MockWebsocketHook source) {
    return new MockWebsocketHookEvent(MockWebsocketHookEventType.AFTER_CONNECT, source);
  }

  /**
   * @param source the source of the event
   * @return A new {@link MockWebsocketHookEventType#BEFORE_DISCONNECT} event for
   *         the given {@link MockWebsocketHook}.
   */
  public static MockWebsocketHookEvent createBeforeDisconnectEvent(MockWebsocketHook source) {
    return new MockWebsocketHookEvent(MockWebsocketHookEventType.BEFORE_DISCONNECT, source);
  }

  /**
   * @param source           the source of the event
   * @return A new {@link MockWebsocketHookEventType#AFTER_DISCONNECT} event for
   *         the given {@link MockWebsocketHook}.
   */
  public static MockWebsocketHookEvent createAfterDisconnectEvent(MockWebsocketHook source) {
    return new MockWebsocketHookEvent(MockWebsocketHookEventType.AFTER_DISCONNECT, source);
  }

  /**
   * @param source the source of the event
   * @param topic  the websocket subscription topic
   * @return A new {@link MockWebsocketHookEventType#GET_SUBSCRIBE_REQUEST_MESSAGE}
   *         event for the given {@link MockWebsocketHook} and topic.
   */
  public static MockWebsocketHookEvent createGetSubscribeRequestMessageEvent(MockWebsocketHook source, String topic) {
    MockWebsocketHookEvent e = new MockWebsocketHookEvent(MockWebsocketHookEventType.GET_SUBSCRIBE_REQUEST_MESSAGE,
        source);
    e.setTopic(topic);
    return e;
  }

  /**
   * @param source the source of the event
   * @param topic  the websocket subscription topic
   * @return A new
   *         {@link MockWebsocketHookEventType#GET_UNSUBSCRIBE_REQUEST_MESSAGE}
   *         event for the given {@link MockWebsocketHook} and topic.
   */
  public static MockWebsocketHookEvent createGetUnSubscribeRequestMessageEvent(MockWebsocketHook source,
      String topic) {
    MockWebsocketHookEvent e = new MockWebsocketHookEvent(
        MockWebsocketHookEventType.GET_UNSUBSCRIBE_REQUEST_MESSAGE, source);
    e.setTopic(topic);
    return e;
  }

  /**
   * @param source the source of the event
   * @return A new {@link MockWebsocketHookEventType#GET_HEARTBEAT_MESSAGE} event
   *         for the given {@link MockWebsocketHook}.
   */
  public static MockWebsocketHookEvent createGetHeartbeatEvent(MockWebsocketHook source) {
    return new MockWebsocketHookEvent(MockWebsocketHookEventType.GET_HEARTBEAT_MESSAGE, source);
  }

  private final MockWebsocketHookEventType type;

  private final MockWebsocketHook source;

  private WebsocketClient websocketClient;

  private String topic;

  /**
   * Create a new {@link MockWebsocketHookEvent} for the given
   * {@link MockWebsocketHook} and {@link MockWebsocketHookEventType}.
   * 
   * @param type   The evnt type
   * @param source the source of the event
   */
  public MockWebsocketHookEvent(MockWebsocketHookEventType type, MockWebsocketHook source) {
    this.type = type;
    this.source = source;
  }

  /**
   * Retrieve the type of this event.
   * 
   * @return The type of this event.
   * @see MockWebsocketHookEventType
   */
  public MockWebsocketHookEventType getType() {
    return type;
  }

  /**
   * Retrieve the source of this event.
   * 
   * @return The source of this event.
   * @see MockWebsocketHook
   */
  public MockWebsocketHook getSource() {
    return source;
  }

  /**
   * Retrieve the {@link WebsocketClient} associated with this event.
   * This field is set when type is {@link MockWebsocketHookEventType#INIT}.
   * 
   * @return The {@link WebsocketClient} associated with this event.
   */
  public WebsocketClient getWebsocketClient() {
    return websocketClient;
  }

  /**
   * Set the {@link WebsocketClient} associated with this event.
   * 
   * @param websocketClient The {@link WebsocketClient} to set.
   */
  public void setWebsocketClient(WebsocketClient websocketClient) {
    this.websocketClient = websocketClient;
  }

  /**
   * Retrieve the topic associated with this event.
   * This field is set when type is
   * {@link MockWebsocketHookEventType#GET_SUBSCRIBE_REQUEST_MESSAGE} or
   * {@link MockWebsocketHookEventType#GET_UNSUBSCRIBE_REQUEST_MESSAGE}.
   * 
   * @return The topic associated with this event.
   */
  public String getTopic() {
    return topic;
  }

  /**
   * Set the topic associated with this event.
   * 
   * @param topic The topic to set.
   */
  public void setTopic(String topic) {
    this.topic = topic;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "[" + type + "]";
  }
}
