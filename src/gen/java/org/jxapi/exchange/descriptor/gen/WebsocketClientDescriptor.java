package org.jxapi.exchange.descriptor.gen;

import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import javax.annotation.processing.Generated;
import org.jxapi.exchange.descriptor.gen.deserializers.WebsocketClientDescriptorDeserializer;
import org.jxapi.exchange.descriptor.gen.serializers.WebsocketClientDescriptorSerializer;
import org.jxapi.util.CompareUtil;
import org.jxapi.util.EncodingUtil;
import org.jxapi.util.Pojo;

/**
 * Part of JSON document describing network configuration of a 
 * exchange wrapper, describes a Websocket client that can be used by
 * Websocket endpoints of the exchange wrapper.
 * 
 */
@Generated("org.jxapi.generator.java.pojo.PojoGenerator")
@JsonSerialize(using = WebsocketClientDescriptorSerializer.class)
@JsonDeserialize(using = WebsocketClientDescriptorDeserializer.class)
public class WebsocketClientDescriptor implements Pojo<WebsocketClientDescriptor> {
  
  private static final long serialVersionUID = -8847912951975013818L;
  
  /**
   * @return A new builder to build {@link WebsocketClientDescriptor} objects
   */
  public static Builder builder() {
    return new Builder();
  }
  
  private String name;
  private String websocketUrl;
  private String websocketFactory;
  private String websocketHookFactory;
  
  /**
   * @return The unique name of the Websocket client
   */
  public String getName() {
    return name;
  }
  
  /**
   * @param name The unique name of the Websocket client
   */
  public void setName(String name) {
    this.name = name;
  }
  
  /**
   * @return The URL (ws:// or wss://) of the Websocket connection.
   * 
   */
  public String getWebsocketUrl() {
    return websocketUrl;
  }
  
  /**
   * @param websocketUrl The URL (ws:// or wss://) of the Websocket connection.
   * 
   */
  public void setWebsocketUrl(String websocketUrl) {
    this.websocketUrl = websocketUrl;
  }
  
  /**
   * @return The fully qualified class name of the
   * {@link org.jxapi.netutils.websocket.WebsocketFactory} to use for
   * Websocket endpoints using this Websocket client.
   * 
   */
  public String getWebsocketFactory() {
    return websocketFactory;
  }
  
  /**
   * @param websocketFactory The fully qualified class name of the
   * {@link org.jxapi.netutils.websocket.WebsocketFactory} to use for
   * Websocket endpoints using this Websocket client.
   * 
   */
  public void setWebsocketFactory(String websocketFactory) {
    this.websocketFactory = websocketFactory;
  }
  
  /**
   * @return The fully qualified class name of the
   * {@link org.jxapi.netutils.websocket.WebsocketHookFactory} to use
   * for Websocket endpoints using this Websocket client.
   * 
   */
  public String getWebsocketHookFactory() {
    return websocketHookFactory;
  }
  
  /**
   * @param websocketHookFactory The fully qualified class name of the
   * {@link org.jxapi.netutils.websocket.WebsocketHookFactory} to use
   * for Websocket endpoints using this Websocket client.
   * 
   */
  public void setWebsocketHookFactory(String websocketHookFactory) {
    this.websocketHookFactory = websocketHookFactory;
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
    WebsocketClientDescriptor o = (WebsocketClientDescriptor) other;
    return Objects.equals(this.name, o.name)
        && Objects.equals(this.websocketUrl, o.websocketUrl)
        && Objects.equals(this.websocketFactory, o.websocketFactory)
        && Objects.equals(this.websocketHookFactory, o.websocketHookFactory);
  }
  
  @Override
  public int compareTo(WebsocketClientDescriptor other) {
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
    res = CompareUtil.compare(this.websocketUrl, other.websocketUrl);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compare(this.websocketFactory, other.websocketFactory);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compare(this.websocketHookFactory, other.websocketHookFactory);
    return res;
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(name, websocketUrl, websocketFactory, websocketHookFactory);
  }
  
  @Override
  public WebsocketClientDescriptor deepClone() {
    WebsocketClientDescriptor clone = new WebsocketClientDescriptor();
    clone.name = this.name;
    clone.websocketUrl = this.websocketUrl;
    clone.websocketFactory = this.websocketFactory;
    clone.websocketHookFactory = this.websocketHookFactory;
    return clone;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
  
  /**
   * Builder for {@link WebsocketClientDescriptor}
   */
  @Generated("org.jxapi.generator.java.JavaTypeGenerator")
  public static class Builder {
    
    private String name;
    private String websocketUrl;
    private String websocketFactory;
    private String websocketHookFactory;
    
    /**
     * Will set the value of <code>name</code> field in the builder
     * @param name The unique name of the Websocket client
     * @return Builder instance
     * @see #setName(String)
     */
    public Builder name(String name)  {
      this.name = name;
      return this;
    }
    
    /**
     * Will set the value of <code>websocketUrl</code> field in the builder
     * @param websocketUrl The URL (ws:// or wss://) of the Websocket connection.
     * 
     * @return Builder instance
     * @see #setWebsocketUrl(String)
     */
    public Builder websocketUrl(String websocketUrl)  {
      this.websocketUrl = websocketUrl;
      return this;
    }
    
    /**
     * Will set the value of <code>websocketFactory</code> field in the builder
     * @param websocketFactory The fully qualified class name of the
     * {@link org.jxapi.netutils.websocket.WebsocketFactory} to use for
     * Websocket endpoints using this Websocket client.
     * 
     * @return Builder instance
     * @see #setWebsocketFactory(String)
     */
    public Builder websocketFactory(String websocketFactory)  {
      this.websocketFactory = websocketFactory;
      return this;
    }
    
    /**
     * Will set the value of <code>websocketHookFactory</code> field in the builder
     * @param websocketHookFactory The fully qualified class name of the
     * {@link org.jxapi.netutils.websocket.WebsocketHookFactory} to use
     * for Websocket endpoints using this Websocket client.
     * 
     * @return Builder instance
     * @see #setWebsocketHookFactory(String)
     */
    public Builder websocketHookFactory(String websocketHookFactory)  {
      this.websocketHookFactory = websocketHookFactory;
      return this;
    }
    
    /**
     * @return a new instance of WebsocketClientDescriptor using the values set in this builder
     */
    public WebsocketClientDescriptor build() {
      WebsocketClientDescriptor res = new WebsocketClientDescriptor();
      res.name = this.name;
      res.websocketUrl = this.websocketUrl;
      res.websocketFactory = this.websocketFactory;
      res.websocketHookFactory = this.websocketHookFactory;
      return res;
    }
  }
}
