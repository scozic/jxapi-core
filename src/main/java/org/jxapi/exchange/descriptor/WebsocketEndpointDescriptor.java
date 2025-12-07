package org.jxapi.exchange.descriptor;

import org.jxapi.pojo.descriptor.Field;
import org.jxapi.util.EncodingUtil;

/**
 * Part of JSON document describing a crypo exchange API, describes a websocket
 * endpoint where clients subscription can be performed using specified topic
 * and eventual additional parameters.
 * The structure of additional subscription parameters and response format are
 * described as {@link Field} lists.
 * @deprecated see {@link org.jxapi.exchange.descriptor.gen.WebsocketEndpointDescriptor}
 */
public class WebsocketEndpointDescriptor {

  private String name;

  private String topic;

  private String description;
  
  private String docUrl;

  private Field request;

  private Field message;

  private WebsocketTopicMatcherDescriptor topicMatcher;

  /**
   * @return Websocket api endpoint name.
   */
  public String getName() {
    return name;
  }

  /**
   * Set the name of this websocket api endpoint.
   * 
   * @param name Websocket api endpoint name.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return the topic of the websocket endpoint. Will be used to match messages
   *         associated with this endpoint in the stream of websocket messages
   *         that may disseminate messages for several multiplexed streams.
   */
  public String getTopic() {
    return topic;
  }

  /**
   * Set the topic of the websocket endpoint. Will be used to match messages
   * associated with this endpoint in the stream of websocket messages
   * that may disseminate messages for several multiplexed streams.
   * <br>
   * Topic can contain reference to whole request of request property value for
   * instance
   * <code>ticker.${request}</code> topic means actual topic is
   * <code>ticker.[value_of_subscription_request_as_string]</code>
   * 
   * @param topic the subscription topic of this websocket endpoint.
   */
  public void setTopic(String topic) {
    this.topic = topic;
  }

  /**
   * @return the description (documentation) of the websocket endpoint.
   */
  public String getDescription() {
    return description;
  }
  
  /**
   * @return Exchange website's documentation URL for this API.
   */
  public String getDocUrl() {
    return docUrl;
  }

  /**
   * @param docUrl Exchange website's documentation URL for this API.
   */
  public void setDocUrl(String docUrl) {
    this.docUrl = docUrl;
  }

  /**
   * @param description the description (documentation) of the websocket endpoint
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * @return the data structure of the websocket endpoint subscription request.
   */
  public Field getRequest() {
    return request;
  }

  /**
   * @param request the data structure of the websocket endpoint subscription request.
   */
  public void setRequest(Field request) {
    this.request = request;
  }

  /**
   * @return the data structure of the websocket endpoint message.
   */
  public Field getMessage() {
    return message;
  }

  /**
   * @param message the data structure of the websocket endpoint message.
   */
  public void setMessage(Field message) {
    this.message = message;
  }
  
  /**
   * @return the description of how to match messages against the topic of this
   *         endpoint.
   */
  public WebsocketTopicMatcherDescriptor getTopicMatcher() {
    return topicMatcher;
  }

  /**
   * @param messageTopicMatcher the description of how to match messages against
   *                            the topic of this endpoint.
   */
  public void setTopicMatcher(WebsocketTopicMatcherDescriptor messageTopicMatcher) {
    this.topicMatcher = messageTopicMatcher;
  }

  /**
   * @return a string representation of the object, see
   *         {@link EncodingUtil#pojoToString(Object)}.
   */
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
