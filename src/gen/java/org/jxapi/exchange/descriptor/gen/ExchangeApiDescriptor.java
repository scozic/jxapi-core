package org.jxapi.exchange.descriptor.gen;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import javax.annotation.processing.Generated;
import org.jxapi.exchange.descriptor.gen.deserializers.ExchangeApiDescriptorDeserializer;
import org.jxapi.exchange.descriptor.gen.serializers.ExchangeApiDescriptorSerializer;
import org.jxapi.util.CollectionUtil;
import org.jxapi.util.CompareUtil;
import org.jxapi.util.DeepCloneable;
import org.jxapi.util.EncodingUtil;
import org.jxapi.util.Pojo;

/**
 * Part of a JSON document descriptor that describes a group of REST and or
 * Websocket endpoints. Child element of ExchangeDescriptor. <br>
 * 
 * <h2>Constants</h2>
 * <ul>
 * <li>Can be specified a List of constants that are used in context of this API group of the exchange wrapper, for instance specific values for some APIs request/response/message properties.</li>
 * <li>Each constant is described as a {@link org.jxapi.exchange.descriptor.Constant}</li>
 * </ul>
 * <h2>REST endpoints</h2>
 * <ul>
 * <li>There can be multiple REST endpoints, or no such endpoint, in which case <code>restEndpoints</code> property can be <code>null</code></li>
 * <li>Each REST endpoint is described as a {@link org.jxapi.exchange.descriptor.gen.RestEndpointDescriptor}</li>
 * <li>Each REST endpoint share the same HttpRequestExecutor and HttpRequestInterceptor, that are created from factories classes provided 
 * in <code>httpRequestExecutorFactory</code> and <code>httpRequestInterceptorFactory</code> properties </li>
 * <li><code>httpRequestExecutorFactory</code> property may be supplied to specify a factory class that creates HttpRequestExecutor instances, see {@link org.jxapi.netutils.rest.HttpRequestExecutorFactory}. When property is not set, default {@link org.jxapi.netutils.rest.javanet.JavaNetHttpRequestExecutor} is used</li>
 * <li><code>httpRequestInterceptorFactory</code> property may be supplied to specify a factory class that creates HttpRequestInterceptor instances, see {@link org.jxapi.netutils.rest.HttpRequestInterceptorFactory}. When property is not set, no request interceptor is used</li>
 * <li>API global Rate limits can be specified for the REST endpoints in <code>rateLimits</code> property. Those limits are shared among all defined REST endpoints.</li>
 * <li>Rate limits from enclosing exchange descriptor are inherited by the API descriptor. Exchange global limit are shared among all REST endpoints of every API specified in exchange</li>
 * </ul>
 * <h2>Websocket endpoints</h2>
 * <ul>
 * <li>There can be multiple Websocket endpoints, or no such endpoint, in which case <code>websocketEndpoints</code> property can be <code>null</code></li>
 * <li>Each Websocket endpoint is described as a {@link org.jxapi.exchange.descriptor.gen.WebsocketEndpointDescriptor}</li>
 * <li>Each Websocket endpoint share the same WebsocketFactory and WebsocketHook, that are created from factories classes provided in <code>websocketFactory</code> and <code>websocketHookFactory</code> properties</li>
 * <li><code>websocketFactory</code> property may be supplied to specify a factory class that creates Websocket instances, see {@link org.jxapi.netutils.websocket.WebsocketFactory}. When property is not set, default {@link org.jxapi.netutils.websocket.DefaultWebsocketFactory} is used</li>
 * <li><code>websocketHookFactory</code> property may be supplied to specify a factory class that creates WebsocketHook instances, see {@link org.jxapi.netutils.websocket.WebsocketHookFactory}. When property is not set, no websocket hook is used. Such hook is needed though to implement specific handshake, heartbeat management and socket multiplexing (e.g. subscribing to multiple topics using same socket)</li>
 * <li><code>websocketUrl</code> property may be supplied to specify the URL of the websocket endpoint. When property is not set, the URL is expected to be set by WebsocketHook on WebsocketClient during {@link org.jxapi.netutils.websocket.WebsocketHook#init(org.jxapi.netutils.websocket.WebsocketClient)} or {@link org.jxapi.netutils.websocket.WebsocketHook#beforeConnect()}</li>
 * </ul>
 * 
 * @see org.jxapi.exchange.descriptor.gen.ExchangeDescriptor
 * @see org.jxapi.exchange.descriptor.gen.RestEndpointDescriptor
 * @see org.jxapi.exchange.descriptor.gen.WebsocketEndpointDescriptor
 * @see org.jxapi.netutils.rest.ratelimits.RateLimitRule
 * @see org.jxapi.netutils.rest.HttpRequestInterceptorFactory
 * @see org.jxapi.netutils.rest.HttpRequestExecutorFactory
 * @see org.jxapi.netutils.websocket.WebsocketFactory
 * @see org.jxapi.netutils.websocket.WebsocketHookFactory
 * @see org.jxapi.exchange.descriptor.Constant
 * 
 */
@Generated("org.jxapi.generator.java.pojo.PojoGenerator")
@JsonSerialize(using = ExchangeApiDescriptorSerializer.class)
@JsonDeserialize(using = ExchangeApiDescriptorDeserializer.class)
public class ExchangeApiDescriptor implements Pojo<ExchangeApiDescriptor> {
  
  private static final long serialVersionUID = 5336020728346609319L;
  
  /**
   * @return A new builder to build {@link ExchangeApiDescriptor} objects
   */
  public static Builder builder() {
    return new Builder();
  }
  
  private String name;
  private String description;
  private String httpUrl;
  private String defaultHttpClient;
  private String defaultWebsocketClient;
  private List<RestEndpointDescriptor> restEndpoints;
  private List<WebsocketEndpointDescriptor> websocketEndpoints;
  
  /**
   * @return The unique name of the API group within the exchange
   */
  public String getName() {
    return name;
  }
  
  /**
   * @param name The unique name of the API group within the exchange
   */
  public void setName(String name) {
    this.name = name;
  }
  
  /**
   * @return The description of the API group
   */
  public String getDescription() {
    return description;
  }
  
  /**
   * @param description The description of the API group
   */
  public void setDescription(String description) {
    this.description = description;
  }
  
  /**
   * @return The base URL of the HTTP (REST) API of this API group. This URL can be
   * concateneted to endpoint URL when endpoint URL is not absolute.
   * 
   */
  public String getHttpUrl() {
    return httpUrl;
  }
  
  /**
   * @param httpUrl The base URL of the HTTP (REST) API of this API group. This URL can be
   * concateneted to endpoint URL when endpoint URL is not absolute.
   * 
   */
  public void setHttpUrl(String httpUrl) {
    this.httpUrl = httpUrl;
  }
  
  /**
   * @return The name of the default HTTP client to use for REST endpoints of this API group,
   * as defined in exchange 'network' section. When not set, first client in the list is used.
   * 
   */
  public String getDefaultHttpClient() {
    return defaultHttpClient;
  }
  
  /**
   * @param defaultHttpClient The name of the default HTTP client to use for REST endpoints of this API group,
   * as defined in exchange 'network' section. When not set, first client in the list is used.
   * 
   */
  public void setDefaultHttpClient(String defaultHttpClient) {
    this.defaultHttpClient = defaultHttpClient;
  }
  
  /**
   * @return The name of the default Websocket client to use for Websocket endpoints of this API group,
   * as defined in exchange 'network' section. When not set, first client in the list is used.
   * 
   */
  public String getDefaultWebsocketClient() {
    return defaultWebsocketClient;
  }
  
  /**
   * @param defaultWebsocketClient The name of the default Websocket client to use for Websocket endpoints of this API group,
   * as defined in exchange 'network' section. When not set, first client in the list is used.
   * 
   */
  public void setDefaultWebsocketClient(String defaultWebsocketClient) {
    this.defaultWebsocketClient = defaultWebsocketClient;
  }
  
  /**
   * @return The list of REST endpoints that belong to this API group.
   * 
   */
  public List<RestEndpointDescriptor> getRestEndpoints() {
    return restEndpoints;
  }
  
  /**
   * @param restEndpoints The list of REST endpoints that belong to this API group.
   * 
   */
  public void setRestEndpoints(List<RestEndpointDescriptor> restEndpoints) {
    this.restEndpoints = restEndpoints;
  }
  
  /**
   * @return The list of Websocket endpoints that belong to this API group.
   * 
   */
  public List<WebsocketEndpointDescriptor> getWebsocketEndpoints() {
    return websocketEndpoints;
  }
  
  /**
   * @param websocketEndpoints The list of Websocket endpoints that belong to this API group.
   * 
   */
  public void setWebsocketEndpoints(List<WebsocketEndpointDescriptor> websocketEndpoints) {
    this.websocketEndpoints = websocketEndpoints;
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
    ExchangeApiDescriptor o = (ExchangeApiDescriptor) other;
    return Objects.equals(this.name, o.name)
        && Objects.equals(this.description, o.description)
        && Objects.equals(this.httpUrl, o.httpUrl)
        && Objects.equals(this.defaultHttpClient, o.defaultHttpClient)
        && Objects.equals(this.defaultWebsocketClient, o.defaultWebsocketClient)
        && Objects.equals(this.restEndpoints, o.restEndpoints)
        && Objects.equals(this.websocketEndpoints, o.websocketEndpoints);
  }
  
  @Override
  public int compareTo(ExchangeApiDescriptor other) {
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
    res = CompareUtil.compare(this.httpUrl, other.httpUrl);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compare(this.defaultHttpClient, other.defaultHttpClient);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compare(this.defaultWebsocketClient, other.defaultWebsocketClient);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compareLists(this.restEndpoints, other.restEndpoints, CompareUtil::compare);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compareLists(this.websocketEndpoints, other.websocketEndpoints, CompareUtil::compare);
    return res;
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(name, description, httpUrl, defaultHttpClient, defaultWebsocketClient, restEndpoints, websocketEndpoints);
  }
  
  @Override
  public ExchangeApiDescriptor deepClone() {
    ExchangeApiDescriptor clone = new ExchangeApiDescriptor();
    clone.name = this.name;
    clone.description = this.description;
    clone.httpUrl = this.httpUrl;
    clone.defaultHttpClient = this.defaultHttpClient;
    clone.defaultWebsocketClient = this.defaultWebsocketClient;
    clone.restEndpoints = CollectionUtil.deepCloneList(this.restEndpoints, DeepCloneable::deepClone);
    clone.websocketEndpoints = CollectionUtil.deepCloneList(this.websocketEndpoints, DeepCloneable::deepClone);
    return clone;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
  
  /**
   * Builder for {@link ExchangeApiDescriptor}
   */
  @Generated("org.jxapi.generator.java.JavaTypeGenerator")
  public static class Builder {
    
    private String name;
    private String description;
    private String httpUrl;
    private String defaultHttpClient;
    private String defaultWebsocketClient;
    private List<RestEndpointDescriptor> restEndpoints;
    private List<WebsocketEndpointDescriptor> websocketEndpoints;
    
    /**
     * Will set the value of <code>name</code> field in the builder
     * @param name The unique name of the API group within the exchange
     * @return Builder instance
     * @see #setName(String)
     */
    public Builder name(String name)  {
      this.name = name;
      return this;
    }
    
    /**
     * Will set the value of <code>description</code> field in the builder
     * @param description The description of the API group
     * @return Builder instance
     * @see #setDescription(String)
     */
    public Builder description(String description)  {
      this.description = description;
      return this;
    }
    
    /**
     * Will set the value of <code>httpUrl</code> field in the builder
     * @param httpUrl The base URL of the HTTP (REST) API of this API group. This URL can be
     * concateneted to endpoint URL when endpoint URL is not absolute.
     * 
     * @return Builder instance
     * @see #setHttpUrl(String)
     */
    public Builder httpUrl(String httpUrl)  {
      this.httpUrl = httpUrl;
      return this;
    }
    
    /**
     * Will set the value of <code>defaultHttpClient</code> field in the builder
     * @param defaultHttpClient The name of the default HTTP client to use for REST endpoints of this API group,
     * as defined in exchange 'network' section. When not set, first client in the list is used.
     * 
     * @return Builder instance
     * @see #setDefaultHttpClient(String)
     */
    public Builder defaultHttpClient(String defaultHttpClient)  {
      this.defaultHttpClient = defaultHttpClient;
      return this;
    }
    
    /**
     * Will set the value of <code>defaultWebsocketClient</code> field in the builder
     * @param defaultWebsocketClient The name of the default Websocket client to use for Websocket endpoints of this API group,
     * as defined in exchange 'network' section. When not set, first client in the list is used.
     * 
     * @return Builder instance
     * @see #setDefaultWebsocketClient(String)
     */
    public Builder defaultWebsocketClient(String defaultWebsocketClient)  {
      this.defaultWebsocketClient = defaultWebsocketClient;
      return this;
    }
    
    /**
     * Will set the value of <code>restEndpoints</code> field in the builder
     * @param restEndpoints The list of REST endpoints that belong to this API group.
     * 
     * @return Builder instance
     * @see #setRestEndpoints(List<RestEndpointDescriptor>)
     */
    public Builder restEndpoints(List<RestEndpointDescriptor> restEndpoints)  {
      this.restEndpoints = restEndpoints;
      return this;
    }
    
    
    /**
     * Will add an item to the <code>restEndpoints</code> list.
     * @param item Item to add to current <code>restEndpoints</code> list
     * @return Builder instance
     * @see ExchangeApiDescriptor#setRestEndpoints(List)
     */
    public Builder addToRestEndpoints(RestEndpointDescriptor item) {
      if (this.restEndpoints == null) {
        this.restEndpoints = CollectionUtil.createList();
      }
      this.restEndpoints.add(item);
      return this;
    }
    
    /**
     * Will set the value of <code>websocketEndpoints</code> field in the builder
     * @param websocketEndpoints The list of Websocket endpoints that belong to this API group.
     * 
     * @return Builder instance
     * @see #setWebsocketEndpoints(List<WebsocketEndpointDescriptor>)
     */
    public Builder websocketEndpoints(List<WebsocketEndpointDescriptor> websocketEndpoints)  {
      this.websocketEndpoints = websocketEndpoints;
      return this;
    }
    
    
    /**
     * Will add an item to the <code>websocketEndpoints</code> list.
     * @param item Item to add to current <code>websocketEndpoints</code> list
     * @return Builder instance
     * @see ExchangeApiDescriptor#setWebsocketEndpoints(List)
     */
    public Builder addToWebsocketEndpoints(WebsocketEndpointDescriptor item) {
      if (this.websocketEndpoints == null) {
        this.websocketEndpoints = CollectionUtil.createList();
      }
      this.websocketEndpoints.add(item);
      return this;
    }
    
    /**
     * @return a new instance of ExchangeApiDescriptor using the values set in this builder
     */
    public ExchangeApiDescriptor build() {
      ExchangeApiDescriptor res = new ExchangeApiDescriptor();
      res.name = this.name;
      res.description = this.description;
      res.httpUrl = this.httpUrl;
      res.defaultHttpClient = this.defaultHttpClient;
      res.defaultWebsocketClient = this.defaultWebsocketClient;
      res.restEndpoints = CollectionUtil.deepCloneList(this.restEndpoints, DeepCloneable::deepClone);
      res.websocketEndpoints = CollectionUtil.deepCloneList(this.websocketEndpoints, DeepCloneable::deepClone);
      return res;
    }
  }
}
