package org.jxapi.exchange.descriptor.gen;

import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import javax.annotation.processing.Generated;
import org.jxapi.exchange.descriptor.gen.deserializers.WebsocketEndpointDescriptorDeserializer;
import org.jxapi.exchange.descriptor.gen.serializers.WebsocketEndpointDescriptorSerializer;
import org.jxapi.pojo.descriptor.Field;
import org.jxapi.util.CompareUtil;
import org.jxapi.util.EncodingUtil;
import org.jxapi.util.Pojo;

/**
 * Part of JSON document describing a crypo exchange API, describes a websocket
 * endpoint where clients subscription can be performed using specified topic
 * and eventual additional parameters.
 * The structure of additional subscription parameters and response format are
 * described as {@link org.jxapi.pojo.descriptor.Field} lists.
 * 
 */
@Generated("org.jxapi.generator.java.pojo.PojoGenerator")
@JsonSerialize(using = WebsocketEndpointDescriptorSerializer.class)
@JsonDeserialize(using = WebsocketEndpointDescriptorDeserializer.class)
public class WebsocketEndpointDescriptor implements Pojo<WebsocketEndpointDescriptor> {
  
  private static final long serialVersionUID = 325731743287611067L;
  
  /**
   * @return A new builder to build {@link WebsocketEndpointDescriptor} objects
   */
  public static Builder builder() {
    return new Builder();
  }
  
  private String name;
  private String description;
  private String topic;
  private String websocketClient;
  private String docUrl;
  private Field request;
  private Field message;
  private WebsocketTopicMatcherDescriptor topicMatcher;
  
  /**
   * @return The unique name of the Websocket endpoint within the API group
   */
  public String getName() {
    return name;
  }
  
  /**
   * @param name The unique name of the Websocket endpoint within the API group
   */
  public void setName(String name) {
    this.name = name;
  }
  
  /**
   * @return The description of the Websocket endpoint
   */
  public String getDescription() {
    return description;
  }
  
  /**
   * @param description The description of the Websocket endpoint
   */
  public void setDescription(String description) {
    this.description = description;
  }
  
  /**
   * @return The topic to subscribe to for this Websocket endpoint
   */
  public String getTopic() {
    return topic;
  }
  
  /**
   * @param topic The topic to subscribe to for this Websocket endpoint
   */
  public void setTopic(String topic) {
    this.topic = topic;
  }
  
  /**
   * @return The name of the Websocket client to use for this Websocket endpoint, as defined
   * in exchange 'network' section. When not set, default client of API
   * group is used.
   * 
   */
  public String getWebsocketClient() {
    return websocketClient;
  }
  
  /**
   * @param websocketClient The name of the Websocket client to use for this Websocket endpoint, as defined
   * in exchange 'network' section. When not set, default client of API
   * group is used.
   * 
   */
  public void setWebsocketClient(String websocketClient) {
    this.websocketClient = websocketClient;
  }
  
  /**
   * @return The documentation URL of the Websocket endpoint
   */
  public String getDocUrl() {
    return docUrl;
  }
  
  /**
   * @param docUrl The documentation URL of the Websocket endpoint
   */
  public void setDocUrl(String docUrl) {
    this.docUrl = docUrl;
  }
  
  /**
   * @return The request data structure for subscribing to the endpoint
   */
  public Field getRequest() {
    return request;
  }
  
  /**
   * @param request The request data structure for subscribing to the endpoint
   */
  public void setRequest(Field request) {
    this.request = request;
  }
  
  /**
   * @return The message data structure received from the endpoint
   */
  public Field getMessage() {
    return message;
  }
  
  /**
   * @param message The message data structure received from the endpoint
   */
  public void setMessage(Field message) {
    this.message = message;
  }
  
  /**
   * @return The description of how to match incoming messages to this topic.
   * This can be either the value or pattern of a specific field, or a logical combination of field matchers using AND/OR operations.
   * Remarks:
   * <ul>
   * <li>One of <code>fieldName</code> or <code>and</code> or <code>or</code> must be provided.</li>
   * <li>If <code>fieldName</code> is provided, either <code>fieldValue</code> or <code>fieldRegexp</code> must be provided.</li>
   * </ul>
   * 
   */
  public WebsocketTopicMatcherDescriptor getTopicMatcher() {
    return topicMatcher;
  }
  
  /**
   * @param topicMatcher The description of how to match incoming messages to this topic.
   * This can be either the value or pattern of a specific field, or a logical combination of field matchers using AND/OR operations.
   * Remarks:
   * <ul>
   * <li>One of <code>fieldName</code> or <code>and</code> or <code>or</code> must be provided.</li>
   * <li>If <code>fieldName</code> is provided, either <code>fieldValue</code> or <code>fieldRegexp</code> must be provided.</li>
   * </ul>
   * 
   */
  public void setTopicMatcher(WebsocketTopicMatcherDescriptor topicMatcher) {
    this.topicMatcher = topicMatcher;
  }
  
  @Override
  public boolean equals(Object other) {
    if (other == null) {
      return false;
    }
    if (this == other) {
      return true;
    }
    if (!getClass().equals(other.getClass()))
      return false;
    WebsocketEndpointDescriptor o = (WebsocketEndpointDescriptor) other;
    return Objects.equals(this.name, o.name)
        && Objects.equals(this.description, o.description)
        && Objects.equals(this.topic, o.topic)
        && Objects.equals(this.websocketClient, o.websocketClient)
        && Objects.equals(this.docUrl, o.docUrl)
        && Objects.equals(this.request, o.request)
        && Objects.equals(this.message, o.message)
        && Objects.equals(this.topicMatcher, o.topicMatcher);
  }
  
  @Override
  public int compareTo(WebsocketEndpointDescriptor other) {
    if (other == null) {
      return 1;
    }
    if (this == other) {
      return 0;
    }
    int res = 0;
    res = CompareUtil.compare(this.name, other.name);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compare(this.description, other.description);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compare(this.topic, other.topic);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compare(this.websocketClient, other.websocketClient);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compare(this.docUrl, other.docUrl);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compare(this.request, other.request);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compare(this.message, other.message);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compare(this.topicMatcher, other.topicMatcher);
    return res;
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(name, description, topic, websocketClient, docUrl, request, message, topicMatcher);
  }
  
  @Override
  public WebsocketEndpointDescriptor deepClone() {
    WebsocketEndpointDescriptor clone = new WebsocketEndpointDescriptor();
    clone.name = this.name;
    clone.description = this.description;
    clone.topic = this.topic;
    clone.websocketClient = this.websocketClient;
    clone.docUrl = this.docUrl;
    clone.request = this.request != null ? this.request.deepClone() : null;
    clone.message = this.message != null ? this.message.deepClone() : null;
    clone.topicMatcher = this.topicMatcher != null ? this.topicMatcher.deepClone() : null;
    return clone;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
  
  /**
   * Builder for {@link WebsocketEndpointDescriptor}
   */
  @Generated("org.jxapi.generator.java.JavaTypeGenerator")
  public static class Builder {
    
    private String name;
    private String description;
    private String topic;
    private String websocketClient;
    private String docUrl;
    private Field request;
    private Field message;
    private WebsocketTopicMatcherDescriptor topicMatcher;
    
    /**
     * Will set the value of <code>name</code> field in the builder
     * @param name The unique name of the Websocket endpoint within the API group
     * @return Builder instance
     * @see #setName(String)
     */
    public Builder name(String name)  {
      this.name = name;
      return this;
    }
    
    /**
     * Will set the value of <code>description</code> field in the builder
     * @param description The description of the Websocket endpoint
     * @return Builder instance
     * @see #setDescription(String)
     */
    public Builder description(String description)  {
      this.description = description;
      return this;
    }
    
    /**
     * Will set the value of <code>topic</code> field in the builder
     * @param topic The topic to subscribe to for this Websocket endpoint
     * @return Builder instance
     * @see #setTopic(String)
     */
    public Builder topic(String topic)  {
      this.topic = topic;
      return this;
    }
    
    /**
     * Will set the value of <code>websocketClient</code> field in the builder
     * @param websocketClient The name of the Websocket client to use for this Websocket endpoint, as defined
     * in exchange 'network' section. When not set, default client of API
     * group is used.
     * 
     * @return Builder instance
     * @see #setWebsocketClient(String)
     */
    public Builder websocketClient(String websocketClient)  {
      this.websocketClient = websocketClient;
      return this;
    }
    
    /**
     * Will set the value of <code>docUrl</code> field in the builder
     * @param docUrl The documentation URL of the Websocket endpoint
     * @return Builder instance
     * @see #setDocUrl(String)
     */
    public Builder docUrl(String docUrl)  {
      this.docUrl = docUrl;
      return this;
    }
    
    /**
     * Will set the value of <code>request</code> field in the builder
     * @param request The request data structure for subscribing to the endpoint
     * @return Builder instance
     * @see #setRequest(Field)
     */
    public Builder request(Field request)  {
      this.request = request;
      return this;
    }
    
    /**
     * Will set the value of <code>message</code> field in the builder
     * @param message The message data structure received from the endpoint
     * @return Builder instance
     * @see #setMessage(Field)
     */
    public Builder message(Field message)  {
      this.message = message;
      return this;
    }
    
    /**
     * Will set the value of <code>topicMatcher</code> field in the builder
     * @param topicMatcher The description of how to match incoming messages to this topic.
     * This can be either the value or pattern of a specific field, or a logical combination of field matchers using AND/OR operations.
     * Remarks:
     * <ul>
     * <li>One of <code>fieldName</code> or <code>and</code> or <code>or</code> must be provided.</li>
     * <li>If <code>fieldName</code> is provided, either <code>fieldValue</code> or <code>fieldRegexp</code> must be provided.</li>
     * </ul>
     * 
     * @return Builder instance
     * @see #setTopicMatcher(WebsocketTopicMatcherDescriptor)
     */
    public Builder topicMatcher(WebsocketTopicMatcherDescriptor topicMatcher)  {
      this.topicMatcher = topicMatcher;
      return this;
    }
    
    /**
     * @return a new instance of WebsocketEndpointDescriptor using the values set in this builder
     */
    public WebsocketEndpointDescriptor build() {
      WebsocketEndpointDescriptor res = new WebsocketEndpointDescriptor();
      res.name = this.name;
      res.description = this.description;
      res.topic = this.topic;
      res.websocketClient = this.websocketClient;
      res.docUrl = this.docUrl;
      res.request = this.request != null ? this.request.deepClone() : null;
      res.message = this.message != null ? this.message.deepClone() : null;
      res.topicMatcher = this.topicMatcher != null ? this.topicMatcher.deepClone() : null;
      return res;
    }
  }
}
