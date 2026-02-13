package org.jxapi.netutils.websocket;

/**
 * Generic interface for a websocket API enppoint.
 * Manages subscriptions to a multiplexed websocket topic.
 *
 * @param <M> The message object disseminated by this websocket endpoint
 */
public interface WebsocketEndpoint<M> {

  /**
   * Get the name of this websocket API endpoint.
   * 
   * @return the name of this endpoint
   */
  String getEndpointName();

  /**
   * Subscribes a listener to a websocket topic. Multiple listeners can be added
   * to the same topic.
   * 
   * @param request  the subscription request, that contains the topic to
   *                 subscribe to.
   * @param listener the listener to be called when a message is received
   * @return A unique subscription id that can be used to unsubscribe from this
   *         topic.
   */
  String subscribe(WebsocketSubscribeRequest request, WebsocketListener<M> listener);

  /**
   * Unsubscribes a listener from a websocket topic.
   * 
   * @param unsubscriptionId the id of the subscription to remove, obtained when
   *                         subscribing
   * @return <code>true</code> if the unsubscription was actually performed,
   *         <code>false</code> if the subscription id was not found.
   */
  boolean unsubscribe(String unsubscriptionId);
}
