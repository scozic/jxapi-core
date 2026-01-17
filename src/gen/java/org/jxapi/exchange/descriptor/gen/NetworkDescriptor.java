package org.jxapi.exchange.descriptor.gen;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import javax.annotation.processing.Generated;
import org.jxapi.exchange.descriptor.gen.deserializers.NetworkDescriptorDeserializer;
import org.jxapi.exchange.descriptor.gen.serializers.NetworkDescriptorSerializer;
import org.jxapi.util.CollectionUtil;
import org.jxapi.util.CompareUtil;
import org.jxapi.util.DeepCloneable;
import org.jxapi.util.EncodingUtil;
import org.jxapi.util.Pojo;

/**
 * Represents network configuration with HTTP clients for REST APIs and
 * Websocket clients for Websocket APIs of the exchange wrapper.
 * 
 */
@Generated("org.jxapi.generator.java.pojo.PojoGenerator")
@JsonSerialize(using = NetworkDescriptorSerializer.class)
@JsonDeserialize(using = NetworkDescriptorDeserializer.class)
public class NetworkDescriptor implements Pojo<NetworkDescriptor> {
  
  private static final long serialVersionUID = -5968448183252402477L;
  
  /**
   * @return A new builder to build {@link NetworkDescriptor} objects
   */
  public static Builder builder() {
    return new Builder();
  }
  
  private List<HttpClientDescriptor> httpClients;
  private List<WebsocketClientDescriptor> websocketClients;
  
  /**
   * @return The list of HTTP clients that can be used by REST endpoints of the exchange wrapper.
   * 
   */
  public List<HttpClientDescriptor> getHttpClients() {
    return httpClients;
  }
  
  /**
   * @param httpClients The list of HTTP clients that can be used by REST endpoints of the exchange wrapper.
   * 
   */
  public void setHttpClients(List<HttpClientDescriptor> httpClients) {
    this.httpClients = httpClients;
  }
  
  /**
   * @return The list of Websocket clients that can be used by Websocket endpoints of the exchange wrapper.
   * Each client provides implementation of Websocket factory and hook factories.
   * Factory can be a reference to another client defined in the list.
   * 
   */
  public List<WebsocketClientDescriptor> getWebsocketClients() {
    return websocketClients;
  }
  
  /**
   * @param websocketClients The list of Websocket clients that can be used by Websocket endpoints of the exchange wrapper.
   * Each client provides implementation of Websocket factory and hook factories.
   * Factory can be a reference to another client defined in the list.
   * 
   */
  public void setWebsocketClients(List<WebsocketClientDescriptor> websocketClients) {
    this.websocketClients = websocketClients;
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
    NetworkDescriptor o = (NetworkDescriptor) other;
    return Objects.equals(this.httpClients, o.httpClients)
        && Objects.equals(this.websocketClients, o.websocketClients);
  }
  
  @Override
  public int compareTo(NetworkDescriptor other) {
    if (other == null) {
      return 1;
    }
    if (this == other) {
      return 0;
    }
    int res = 0;
    res = CompareUtil.compareLists(this.httpClients, other.httpClients, CompareUtil::compare);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compareLists(this.websocketClients, other.websocketClients, CompareUtil::compare);
    return res;
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(httpClients, websocketClients);
  }
  
  @Override
  public NetworkDescriptor deepClone() {
    NetworkDescriptor clone = new NetworkDescriptor();
    clone.httpClients = CollectionUtil.deepCloneList(this.httpClients, DeepCloneable::deepClone);
    clone.websocketClients = CollectionUtil.deepCloneList(this.websocketClients, DeepCloneable::deepClone);
    return clone;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
  
  /**
   * Builder for {@link NetworkDescriptor}
   */
  @Generated("org.jxapi.generator.java.JavaTypeGenerator")
  public static class Builder {
    
    private List<HttpClientDescriptor> httpClients;
    private List<WebsocketClientDescriptor> websocketClients;
    
    /**
     * Will set the value of <code>httpClients</code> field in the builder
     * @param httpClients The list of HTTP clients that can be used by REST endpoints of the exchange wrapper.
     * 
     * @return Builder instance
     * @see #setHttpClients(List<HttpClientDescriptor>)
     */
    public Builder httpClients(List<HttpClientDescriptor> httpClients)  {
      this.httpClients = httpClients;
      return this;
    }
    
    
    /**
     * Will add an item to the <code>httpClients</code> list.
     * @param item Item to add to current <code>httpClients</code> list
     * @return Builder instance
     * @see NetworkDescriptor#setHttpClients(List)
     */
    public Builder addToHttpClients(HttpClientDescriptor item) {
      if (this.httpClients == null) {
        this.httpClients = CollectionUtil.createList();
      }
      this.httpClients.add(item);
      return this;
    }
    
    /**
     * Will set the value of <code>websocketClients</code> field in the builder
     * @param websocketClients The list of Websocket clients that can be used by Websocket endpoints of the exchange wrapper.
     * Each client provides implementation of Websocket factory and hook factories.
     * Factory can be a reference to another client defined in the list.
     * 
     * @return Builder instance
     * @see #setWebsocketClients(List<WebsocketClientDescriptor>)
     */
    public Builder websocketClients(List<WebsocketClientDescriptor> websocketClients)  {
      this.websocketClients = websocketClients;
      return this;
    }
    
    
    /**
     * Will add an item to the <code>websocketClients</code> list.
     * @param item Item to add to current <code>websocketClients</code> list
     * @return Builder instance
     * @see NetworkDescriptor#setWebsocketClients(List)
     */
    public Builder addToWebsocketClients(WebsocketClientDescriptor item) {
      if (this.websocketClients == null) {
        this.websocketClients = CollectionUtil.createList();
      }
      this.websocketClients.add(item);
      return this;
    }
    
    /**
     * @return a new instance of NetworkDescriptor using the values set in this builder
     */
    public NetworkDescriptor build() {
      NetworkDescriptor res = new NetworkDescriptor();
      res.httpClients = CollectionUtil.deepCloneList(this.httpClients, DeepCloneable::deepClone);
      res.websocketClients = CollectionUtil.deepCloneList(this.websocketClients, DeepCloneable::deepClone);
      return res;
    }
  }
}
