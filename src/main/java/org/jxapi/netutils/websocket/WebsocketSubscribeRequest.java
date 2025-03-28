package org.jxapi.netutils.websocket;

import org.jxapi.netutils.websocket.multiplexing.WebsocketMessageTopicMatcherFactory;
import org.jxapi.util.EncodingUtil;

/**
 * Encapsulates a subscription to a websocket endpoint.
 * Such subsription carries a topic that uniquely identifies the subscription
 * stream and can be used in creation of the outgoing subscription message
 * expected to be sent to the websocket to trigger the dispatch of messages for
 * that topic.
 * 
 * @see Websocket
 * @see WebsocketEndpoint#subscribe(WebsocketSubscribeRequest,
 *      WebsocketListener)
 */
public class WebsocketSubscribeRequest {

  /**
   * Factory method
   * 
   * @param request                    The request data, not serialized
   * @param topic                      The topic to subscribe to.
   * @param messageTopicMatcherFactory The message topic matcher
   * @return The subscription request
   */
  public static WebsocketSubscribeRequest create(Object request, 
                           String topic,
                           WebsocketMessageTopicMatcherFactory messageTopicMatcherFactory) {
    WebsocketSubscribeRequest subscribeRequest = new WebsocketSubscribeRequest();
    subscribeRequest.request = request;
    subscribeRequest.topic = topic;
    subscribeRequest.messageTopicMatcherFactory = messageTopicMatcherFactory;
    return subscribeRequest;
  }

  private Object request;
  private WebsocketMessageTopicMatcherFactory messageTopicMatcherFactory;
  private String topic;
  private String enpoint;

  /**
   * @return The request data, not serialized. Used for monitoring purposes.
   */
  public Object getRequest() {
    return request;
  }

  /**
   * Set the request data, not serialized
   * 
   * @param request The request data
   */
  public void setRequest(Object request) {
    this.request = request;
  }

  /**
   * @return The message topic matcher factory used to match incoming messages to
   *         this subscription's topic.
   */
  public WebsocketMessageTopicMatcherFactory getMessageTopicMatcherFactory() {
    return messageTopicMatcherFactory;
  }

  /**
   * Set the message topic matcher factory used to match incoming messages to this
   * subscription's topic. Can be also used to build the subscription message.
   * 
   * @param messageTopicMatcherFactory The message topic matcher factory
   */
  public void setMessageTopicMatcherFactory(WebsocketMessageTopicMatcherFactory messageTopicMatcherFactory) {
    this.messageTopicMatcherFactory = messageTopicMatcherFactory;
  }

  /**
   * Get the topic to subscribe to in case of a multiplexed websocket
   * 
   * @return The topic to subscribe to
   */
  public String getTopic() {
    return topic;
  }

  /**
   * Set the topic to subscribe to in case of a multiplexed websocket
   * 
   * @param topic The topic to subscribe to
   */
  public void setTopic(String topic) {
    this.topic = topic;
  }
  
  /**
   * Get the name of endpoint this subscription is made to.
   * 
   * @return The endpoint name
   */
  public String getEnpoint() {
    return enpoint;
  }

  /**
   * Set the name of endpoint this subscription is made to.<br>
   * Remark: Is set by WebsocketEndpoint upon subscription
   * 
   * @param enpointName The endpoint name
   */
  public void setEnpoint(String enpointName) {
    this.enpoint = enpointName;
  }

  /*
   * See {@link EncodingUtil#pojoToString()}
   */
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }

}
