package org.jxapi.exchange.descriptor.gen;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import javax.annotation.processing.Generated;
import org.jxapi.exchange.descriptor.gen.deserializers.ExchangeDescriptorDeserializer;
import org.jxapi.exchange.descriptor.gen.serializers.ExchangeDescriptorSerializer;
import org.jxapi.util.CollectionUtil;
import org.jxapi.util.CompareUtil;
import org.jxapi.util.DeepCloneable;
import org.jxapi.util.EncodingUtil;
import org.jxapi.util.Pojo;

/**
 * Root element of a JSON Exchange descriptor.<br>
 * This class describes an exchange and its APIs<br>
 * API will be described in groups of endpoints, as
 * {@link org.jxapi.exchange.descriptor.gen.ExchangeApiDescriptor} list.<br>
 * Rate limits will be described as {@link org.jxapi.netutils.rest.ratelimits.RateLimitRule} list. These limits
 * will be applied to all endpoints of each API group of the exchange.
 * 
 * JSON example:
 * 
 * <pre>
 * {
 *     "name": "Binance",
 *     "ID": "BINANCE",
 *     "description": "Binance exchange",
 *     "docUrl": "https://binance-docs.github.io/apidocs",
 *     "basePackage": "org.jxapi.exchange.binance",
 *     "properties": [
 *       {
 *         "name": "apiKey",
 *         "type": "STRING",
 *         "description": "API key for authentication",
 *       }
 *     ],
 *     "constants": [
 *       {
 *         "name": "API_BASE_URL",
 *         "description": "Base URL of the API",
 *         "type": "STRING",
 *         "value": "https://api.myexchange.com"
 *       }
 *     "apis": [
 *       {
 *         "name": "Spot",
 *         "description": "Spot trading API",
 *         "httpRequestExecutorFactory": "org.jxapi.netutils.rest.mock.MockHttpRequestExecutorFactory",
 *         "httpRequestInterceptorFactory": "org.jxapi.netutils.rest.mock.MockHttpRequestInterceptorFactory",
 *         "restEndpoints": [
 *           // RESTendpoints here
 *         ],
 *         "websocketEndpoints": [
 *           // Websocket endpoints here
 *         ],
 *       }
 *     ],
 *     "rateLimits": [
 *       {
 *         "timeFrame": 60000,
 *             "maxRequestCount": 1200,
 *             "maxTotalWeight": 1200
 *       }
 *     ]
 * }
 * </pre>
 * 
 * <ul>
 * <li>name: the name of the exchange. Remark that this is map to the ID of the
 * exchange.</li>
 * <li>description: the description of the exchange.</li>
 * <li>basePackage: the base package of the exchange implementation
 * classes.</li>
 * <li>apis: the list of API groups of the exchange. See
 * {@link org.jxapi.exchange.descriptor.gen.ExchangeApiDescriptor}</li>
 * <li>rateLimits: the list of rate limits of the exchange. These limits will be
 * applied to all endpoints of each API group of the exchange. See
 * {@link org.jxapi.netutils.rest.ratelimits.RateLimitRule}</li>
 * <li>constants: list of constants that are used in context of the exchange
 * wrapper, for instance specific values for some APIs request/response/message
 * properties. See {@link org.jxapi.exchange.descriptor.Constant}</li>
 * <li>properties: list of configuration properties that can be configured for
 * the exchange wrapper, for instance API keys, secret keys, etc. See
 * {@link org.jxapi.util.DefaultConfigProperty}</li>
 * <li><code>httpRequestInterceptorFactory</code>,
 * <code>httpRequestExecutorFactory</code>, <code>httpRequestTimeout</code>,
 * <code>websocketFactory</code>, <code>websocketHookFactory</code> properties
 * can be set with default values for corresponding properties in
 * {@link org.jxapi.exchange.descriptor.gen.ExchangeApiDescriptor}: value defined in 
 * {@link org.jxapi.exchange.descriptor.gen.ExchangeDescriptor} is
 * used when undefined in {@link org.jxapi.exchange.descriptor.gen.ExchangeApiDescriptor}.
 * <li>httpUrl: the base URL of the HTTP (REST) API of the exchange. This URL
 * can be concateneted to websocket API group <code>httpUrl</code> property (see
 * {@link org.jxapi.exchange.descriptor.gen.ExchangeApiDescriptor#getHttpUrl()} ) and endpoint URL (see
 * {@link org.jxapi.exchange.descriptor.gen.RestEndpointDescriptor#getUrl()} when one of these properties are not
 * absolute)</li>
 * <li>websocketUrl: the base URL of the Websocket API of the exchange. This URL
 * can be concateneted to websocket API group <code>websocketUrl</code> property
 * if API group <code>websocketUrl</code> is not not absolute</li>
 * </ul>
 * 
 * This class is used to map the JSON descriptor of an exchange. It is used to
 * generate code for the {@link org.jxapi.exchange.Exchange} interface and implementation.
 * 
 * @see org.jxapi.exchange.Exchange
 * @see org.jxapi.exchange.descriptor.gen.ExchangeApiDescriptor
 * 
 */
@Generated("org.jxapi.generator.java.pojo.PojoGenerator")
@JsonSerialize(using = ExchangeDescriptorSerializer.class)
@JsonDeserialize(using = ExchangeDescriptorDeserializer.class)
public class ExchangeDescriptor implements Pojo<ExchangeDescriptor> {
  
  private static final long serialVersionUID = -4300521477549623759L;
  
  /**
   * @return A new builder to build {@link ExchangeDescriptor} objects
   */
  public static Builder builder() {
    return new Builder();
  }
  
  private String id;
  private String jxapi;
  private String version;
  private String description;
  private String docUrl;
  private String basePackage;
  private String httpUrl;
  private String afterInitHookFactory;
  private List<ConfigPropertyDescriptor> properties;
  private List<ConstantDescriptor> constants;
  private List<RateLimitRuleDescriptor> rateLimits;
  private NetworkDescriptor network;
  private List<ExchangeApiDescriptor> apis;
  
  /**
   * @return The ID of the exchange
   */
  public String getId() {
    return id;
  }
  
  /**
   * @param id The ID of the exchange
   */
  public void setId(String id) {
    this.id = id;
  }
  
  /**
   * @return The JXAPI version supported by the exchange
   */
  public String getJxapi() {
    return jxapi;
  }
  
  /**
   * @param jxapi The JXAPI version supported by the exchange
   */
  public void setJxapi(String jxapi) {
    this.jxapi = jxapi;
  }
  
  /**
   * @return The version of the exchange descriptor
   */
  public String getVersion() {
    return version;
  }
  
  /**
   * @param version The version of the exchange descriptor
   */
  public void setVersion(String version) {
    this.version = version;
  }
  
  /**
   * @return The description of the exchange
   */
  public String getDescription() {
    return description;
  }
  
  /**
   * @param description The description of the exchange
   */
  public void setDescription(String description) {
    this.description = description;
  }
  
  /**
   * @return The documentation URL of the exchange
   */
  public String getDocUrl() {
    return docUrl;
  }
  
  /**
   * @param docUrl The documentation URL of the exchange
   */
  public void setDocUrl(String docUrl) {
    this.docUrl = docUrl;
  }
  
  /**
   * @return The base package of the exchange implementation classes
   */
  public String getBasePackage() {
    return basePackage;
  }
  
  /**
   * @param basePackage The base package of the exchange implementation classes
   */
  public void setBasePackage(String basePackage) {
    this.basePackage = basePackage;
  }
  
  /**
   * @return The base URL of the HTTP (REST) API of the exchange. This URL can be
   * concateneted to API group <code>httpUrl</code> and endpoint URL when one
   * of these properties are not absolute.
   * 
   */
  public String getHttpUrl() {
    return httpUrl;
  }
  
  /**
   * @param httpUrl The base URL of the HTTP (REST) API of the exchange. This URL can be
   * concateneted to API group <code>httpUrl</code> and endpoint URL when one
   * of these properties are not absolute.
   * 
   */
  public void setHttpUrl(String httpUrl) {
    this.httpUrl = httpUrl;
  }
  
  /**
   * @return The fully qualified class name of the
   * {@link org.jxapi.exchange.ExchangeHookFactory} to be used
   * to create an
   * {@link org.jxapi.exchange.ExchangeHook} that will be
   * executed just after the exchange wrapper initialization.
   * 
   */
  public String getAfterInitHookFactory() {
    return afterInitHookFactory;
  }
  
  /**
   * @param afterInitHookFactory The fully qualified class name of the
   * {@link org.jxapi.exchange.ExchangeHookFactory} to be used
   * to create an
   * {@link org.jxapi.exchange.ExchangeHook} that will be
   * executed just after the exchange wrapper initialization.
   * 
   */
  public void setAfterInitHookFactory(String afterInitHookFactory) {
    this.afterInitHookFactory = afterInitHookFactory;
  }
  
  /**
   * @return Represents a configuration property or a group of properties of an exchange like authentication credentials 
   * (API Key,secret) the wrapper client should provide to instantiate a wrapper.<br>
   * Exchange descriptor may contain a list of such properties as value of
   * 'properties' property of exchange.<p>
   * The name of a property should be spelled 'camelCase' like a Java variable
   * name.
   * <p>
   * The value of a property can be a 'primitive' type e.g. {@link org.jxapi.pojo.descriptor.Type#STRING},
   * {@link org.jxapi.pojo.descriptor.Type#INT}, {@link org.jxapi.pojo.descriptor.Type#BOOLEAN}, 
   * {@link org.jxapi.pojo.descriptor.Type#BIGDECIMAL}, {@link org.jxapi.pojo.descriptor.Type#LONG}. 
   * It can't be a list, map, or object.<p>
   * The properties will be exposed as static properties of a generated Java class named [exchangeId]Constants. 
   * That class wlll list constants for property names and default values, and default 'getter' methods for 
   * retrieving their values from properties
   * <p>
   * The properties can be grouped together. For example, authentication
   * credentials can be grouped into a 'group' property called 'auth' with
   * sub-properties for API key, secret, etc listed. Those properties can be
   * referenced with key auth.apiKey, auth.apiSecret, etc. Groups may contain
   * other groups, so the structure is hierarchical.
   * 
   */
  public List<ConfigPropertyDescriptor> getProperties() {
    return properties;
  }
  
  /**
   * @param properties Represents a configuration property or a group of properties of an exchange like authentication credentials 
   * (API Key,secret) the wrapper client should provide to instantiate a wrapper.<br>
   * Exchange descriptor may contain a list of such properties as value of
   * 'properties' property of exchange.<p>
   * The name of a property should be spelled 'camelCase' like a Java variable
   * name.
   * <p>
   * The value of a property can be a 'primitive' type e.g. {@link org.jxapi.pojo.descriptor.Type#STRING},
   * {@link org.jxapi.pojo.descriptor.Type#INT}, {@link org.jxapi.pojo.descriptor.Type#BOOLEAN}, 
   * {@link org.jxapi.pojo.descriptor.Type#BIGDECIMAL}, {@link org.jxapi.pojo.descriptor.Type#LONG}. 
   * It can't be a list, map, or object.<p>
   * The properties will be exposed as static properties of a generated Java class named [exchangeId]Constants. 
   * That class wlll list constants for property names and default values, and default 'getter' methods for 
   * retrieving their values from properties
   * <p>
   * The properties can be grouped together. For example, authentication
   * credentials can be grouped into a 'group' property called 'auth' with
   * sub-properties for API key, secret, etc listed. Those properties can be
   * referenced with key auth.apiKey, auth.apiSecret, etc. Groups may contain
   * other groups, so the structure is hierarchical.
   * 
   */
  public void setProperties(List<ConfigPropertyDescriptor> properties) {
    this.properties = properties;
  }
  
  /**
   * @return The list of constants that are used in context of the exchange.wrapper, for instance specific values for some APIs request/response/message properties.<br>
   * See {@link org.jxapi.exchange.descriptor.Constant} for details.
   * 
   */
  public List<ConstantDescriptor> getConstants() {
    return constants;
  }
  
  /**
   * @param constants The list of constants that are used in context of the exchange.wrapper, for instance specific values for some APIs request/response/message properties.<br>
   * See {@link org.jxapi.exchange.descriptor.Constant} for details.
   * 
   */
  public void setConstants(List<ConstantDescriptor> constants) {
    this.constants = constants;
  }
  
  /**
   * @return The list of rate limit rules that apply to all API groups of the exchange.<br>
   * See {@link org.jxapi.netutils.rest.ratelimits.RateLimitRule} for details.
   * 
   */
  public List<RateLimitRuleDescriptor> getRateLimits() {
    return rateLimits;
  }
  
  /**
   * @param rateLimits The list of rate limit rules that apply to all API groups of the exchange.<br>
   * See {@link org.jxapi.netutils.rest.ratelimits.RateLimitRule} for details.
   * 
   */
  public void setRateLimits(List<RateLimitRuleDescriptor> rateLimits) {
    this.rateLimits = rateLimits;
  }
  
  /**
   * @return Represents network configuration with HTTP clients for REST APIs and
   * Websocket clients for Websocket APIs of the exchange wrapper.
   * 
   */
  public NetworkDescriptor getNetwork() {
    return network;
  }
  
  /**
   * @param network Represents network configuration with HTTP clients for REST APIs and
   * Websocket clients for Websocket APIs of the exchange wrapper.
   * 
   */
  public void setNetwork(NetworkDescriptor network) {
    this.network = network;
  }
  
  /**
   * @return The list of API groups of the exchange. Each API group describes a set of
   * REST and/or Websocket endpoints that share common configuration like base
   * URL, HTTP client, Websocket client, etc.<br>
   * See {@link org.jxapi.exchange.descriptor.gen.ExchangeApiDescriptor} for details.
   * 
   */
  public List<ExchangeApiDescriptor> getApis() {
    return apis;
  }
  
  /**
   * @param apis The list of API groups of the exchange. Each API group describes a set of
   * REST and/or Websocket endpoints that share common configuration like base
   * URL, HTTP client, Websocket client, etc.<br>
   * See {@link org.jxapi.exchange.descriptor.gen.ExchangeApiDescriptor} for details.
   * 
   */
  public void setApis(List<ExchangeApiDescriptor> apis) {
    this.apis = apis;
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
    ExchangeDescriptor o = (ExchangeDescriptor) other;
    return Objects.equals(this.id, o.id)
        && Objects.equals(this.jxapi, o.jxapi)
        && Objects.equals(this.version, o.version)
        && Objects.equals(this.description, o.description)
        && Objects.equals(this.docUrl, o.docUrl)
        && Objects.equals(this.basePackage, o.basePackage)
        && Objects.equals(this.httpUrl, o.httpUrl)
        && Objects.equals(this.afterInitHookFactory, o.afterInitHookFactory)
        && Objects.equals(this.properties, o.properties)
        && Objects.equals(this.constants, o.constants)
        && Objects.equals(this.rateLimits, o.rateLimits)
        && Objects.equals(this.network, o.network)
        && Objects.equals(this.apis, o.apis);
  }
  
  @Override
  public int compareTo(ExchangeDescriptor other) {
    if (other == null) {
      return 1;
    }
    if (this == other) {
      return 0;
    }
    int res = 0;
    res = CompareUtil.compare(this.id, other.id);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compare(this.jxapi, other.jxapi);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compare(this.version, other.version);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compare(this.description, other.description);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compare(this.docUrl, other.docUrl);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compare(this.basePackage, other.basePackage);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compare(this.httpUrl, other.httpUrl);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compare(this.afterInitHookFactory, other.afterInitHookFactory);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compareLists(this.properties, other.properties, CompareUtil::compare);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compareLists(this.constants, other.constants, CompareUtil::compare);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compareLists(this.rateLimits, other.rateLimits, CompareUtil::compare);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compare(this.network, other.network);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compareLists(this.apis, other.apis, CompareUtil::compare);
    return res;
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(id, jxapi, version, description, docUrl, basePackage, httpUrl, afterInitHookFactory, properties, constants, rateLimits, network, apis);
  }
  
  @Override
  public ExchangeDescriptor deepClone() {
    ExchangeDescriptor clone = new ExchangeDescriptor();
    clone.id = this.id;
    clone.jxapi = this.jxapi;
    clone.version = this.version;
    clone.description = this.description;
    clone.docUrl = this.docUrl;
    clone.basePackage = this.basePackage;
    clone.httpUrl = this.httpUrl;
    clone.afterInitHookFactory = this.afterInitHookFactory;
    clone.properties = CollectionUtil.deepCloneList(this.properties, DeepCloneable::deepClone);
    clone.constants = CollectionUtil.deepCloneList(this.constants, DeepCloneable::deepClone);
    clone.rateLimits = CollectionUtil.deepCloneList(this.rateLimits, DeepCloneable::deepClone);
    clone.network = this.network != null ? this.network.deepClone() : null;
    clone.apis = CollectionUtil.deepCloneList(this.apis, DeepCloneable::deepClone);
    return clone;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
  
  /**
   * Builder for {@link ExchangeDescriptor}
   */
  @Generated("org.jxapi.generator.java.JavaTypeGenerator")
  public static class Builder {
    
    private String id;
    private String jxapi;
    private String version;
    private String description;
    private String docUrl;
    private String basePackage;
    private String httpUrl;
    private String afterInitHookFactory;
    private List<ConfigPropertyDescriptor> properties;
    private List<ConstantDescriptor> constants;
    private List<RateLimitRuleDescriptor> rateLimits;
    private NetworkDescriptor network;
    private List<ExchangeApiDescriptor> apis;
    
    /**
     * Will set the value of <code>id</code> field in the builder
     * @param id The ID of the exchange
     * @return Builder instance
     * @see #setId(String)
     */
    public Builder id(String id)  {
      this.id = id;
      return this;
    }
    
    /**
     * Will set the value of <code>jxapi</code> field in the builder
     * @param jxapi The JXAPI version supported by the exchange
     * @return Builder instance
     * @see #setJxapi(String)
     */
    public Builder jxapi(String jxapi)  {
      this.jxapi = jxapi;
      return this;
    }
    
    /**
     * Will set the value of <code>version</code> field in the builder
     * @param version The version of the exchange descriptor
     * @return Builder instance
     * @see #setVersion(String)
     */
    public Builder version(String version)  {
      this.version = version;
      return this;
    }
    
    /**
     * Will set the value of <code>description</code> field in the builder
     * @param description The description of the exchange
     * @return Builder instance
     * @see #setDescription(String)
     */
    public Builder description(String description)  {
      this.description = description;
      return this;
    }
    
    /**
     * Will set the value of <code>docUrl</code> field in the builder
     * @param docUrl The documentation URL of the exchange
     * @return Builder instance
     * @see #setDocUrl(String)
     */
    public Builder docUrl(String docUrl)  {
      this.docUrl = docUrl;
      return this;
    }
    
    /**
     * Will set the value of <code>basePackage</code> field in the builder
     * @param basePackage The base package of the exchange implementation classes
     * @return Builder instance
     * @see #setBasePackage(String)
     */
    public Builder basePackage(String basePackage)  {
      this.basePackage = basePackage;
      return this;
    }
    
    /**
     * Will set the value of <code>httpUrl</code> field in the builder
     * @param httpUrl The base URL of the HTTP (REST) API of the exchange. This URL can be
     * concateneted to API group <code>httpUrl</code> and endpoint URL when one
     * of these properties are not absolute.
     * 
     * @return Builder instance
     * @see #setHttpUrl(String)
     */
    public Builder httpUrl(String httpUrl)  {
      this.httpUrl = httpUrl;
      return this;
    }
    
    /**
     * Will set the value of <code>afterInitHookFactory</code> field in the builder
     * @param afterInitHookFactory The fully qualified class name of the
     * {@link org.jxapi.exchange.ExchangeHookFactory} to be used
     * to create an
     * {@link org.jxapi.exchange.ExchangeHook} that will be
     * executed just after the exchange wrapper initialization.
     * 
     * @return Builder instance
     * @see #setAfterInitHookFactory(String)
     */
    public Builder afterInitHookFactory(String afterInitHookFactory)  {
      this.afterInitHookFactory = afterInitHookFactory;
      return this;
    }
    
    /**
     * Will set the value of <code>properties</code> field in the builder
     * @param properties Represents a configuration property or a group of properties of an exchange like authentication credentials 
     * (API Key,secret) the wrapper client should provide to instantiate a wrapper.<br>
     * Exchange descriptor may contain a list of such properties as value of
     * 'properties' property of exchange.<p>
     * The name of a property should be spelled 'camelCase' like a Java variable
     * name.
     * <p>
     * The value of a property can be a 'primitive' type e.g. {@link org.jxapi.pojo.descriptor.Type#STRING},
     * {@link org.jxapi.pojo.descriptor.Type#INT}, {@link org.jxapi.pojo.descriptor.Type#BOOLEAN}, 
     * {@link org.jxapi.pojo.descriptor.Type#BIGDECIMAL}, {@link org.jxapi.pojo.descriptor.Type#LONG}. 
     * It can't be a list, map, or object.<p>
     * The properties will be exposed as static properties of a generated Java class named [exchangeId]Constants. 
     * That class wlll list constants for property names and default values, and default 'getter' methods for 
     * retrieving their values from properties
     * <p>
     * The properties can be grouped together. For example, authentication
     * credentials can be grouped into a 'group' property called 'auth' with
     * sub-properties for API key, secret, etc listed. Those properties can be
     * referenced with key auth.apiKey, auth.apiSecret, etc. Groups may contain
     * other groups, so the structure is hierarchical.
     * 
     * @return Builder instance
     * @see #setProperties(List<ConfigPropertyDescriptor>)
     */
    public Builder properties(List<ConfigPropertyDescriptor> properties)  {
      this.properties = properties;
      return this;
    }
    
    
    /**
     * Will add an item to the <code>properties</code> list.
     * @param item Item to add to current <code>properties</code> list
     * @return Builder instance
     * @see ExchangeDescriptor#setProperties(List)
     */
    public Builder addToProperties(ConfigPropertyDescriptor item) {
      if (this.properties == null) {
        this.properties = CollectionUtil.createList();
      }
      this.properties.add(item);
      return this;
    }
    
    /**
     * Will set the value of <code>constants</code> field in the builder
     * @param constants The list of constants that are used in context of the exchange.wrapper, for instance specific values for some APIs request/response/message properties.<br>
     * See {@link org.jxapi.exchange.descriptor.Constant} for details.
     * 
     * @return Builder instance
     * @see #setConstants(List<ConstantDescriptor>)
     */
    public Builder constants(List<ConstantDescriptor> constants)  {
      this.constants = constants;
      return this;
    }
    
    
    /**
     * Will add an item to the <code>constants</code> list.
     * @param item Item to add to current <code>constants</code> list
     * @return Builder instance
     * @see ExchangeDescriptor#setConstants(List)
     */
    public Builder addToConstants(ConstantDescriptor item) {
      if (this.constants == null) {
        this.constants = CollectionUtil.createList();
      }
      this.constants.add(item);
      return this;
    }
    
    /**
     * Will set the value of <code>rateLimits</code> field in the builder
     * @param rateLimits The list of rate limit rules that apply to all API groups of the exchange.<br>
     * See {@link org.jxapi.netutils.rest.ratelimits.RateLimitRule} for details.
     * 
     * @return Builder instance
     * @see #setRateLimits(List<RateLimitRuleDescriptor>)
     */
    public Builder rateLimits(List<RateLimitRuleDescriptor> rateLimits)  {
      this.rateLimits = rateLimits;
      return this;
    }
    
    
    /**
     * Will add an item to the <code>rateLimits</code> list.
     * @param item Item to add to current <code>rateLimits</code> list
     * @return Builder instance
     * @see ExchangeDescriptor#setRateLimits(List)
     */
    public Builder addToRateLimits(RateLimitRuleDescriptor item) {
      if (this.rateLimits == null) {
        this.rateLimits = CollectionUtil.createList();
      }
      this.rateLimits.add(item);
      return this;
    }
    
    /**
     * Will set the value of <code>network</code> field in the builder
     * @param network Represents network configuration with HTTP clients for REST APIs and
     * Websocket clients for Websocket APIs of the exchange wrapper.
     * 
     * @return Builder instance
     * @see #setNetwork(NetworkDescriptor)
     */
    public Builder network(NetworkDescriptor network)  {
      this.network = network;
      return this;
    }
    
    /**
     * Will set the value of <code>apis</code> field in the builder
     * @param apis The list of API groups of the exchange. Each API group describes a set of
     * REST and/or Websocket endpoints that share common configuration like base
     * URL, HTTP client, Websocket client, etc.<br>
     * See {@link org.jxapi.exchange.descriptor.gen.ExchangeApiDescriptor} for details.
     * 
     * @return Builder instance
     * @see #setApis(List<ExchangeApiDescriptor>)
     */
    public Builder apis(List<ExchangeApiDescriptor> apis)  {
      this.apis = apis;
      return this;
    }
    
    
    /**
     * Will add an item to the <code>apis</code> list.
     * @param item Item to add to current <code>apis</code> list
     * @return Builder instance
     * @see ExchangeDescriptor#setApis(List)
     */
    public Builder addToApis(ExchangeApiDescriptor item) {
      if (this.apis == null) {
        this.apis = CollectionUtil.createList();
      }
      this.apis.add(item);
      return this;
    }
    
    /**
     * @return a new instance of ExchangeDescriptor using the values set in this builder
     */
    public ExchangeDescriptor build() {
      ExchangeDescriptor res = new ExchangeDescriptor();
      res.id = this.id;
      res.jxapi = this.jxapi;
      res.version = this.version;
      res.description = this.description;
      res.docUrl = this.docUrl;
      res.basePackage = this.basePackage;
      res.httpUrl = this.httpUrl;
      res.afterInitHookFactory = this.afterInitHookFactory;
      res.properties = CollectionUtil.deepCloneList(this.properties, DeepCloneable::deepClone);
      res.constants = CollectionUtil.deepCloneList(this.constants, DeepCloneable::deepClone);
      res.rateLimits = CollectionUtil.deepCloneList(this.rateLimits, DeepCloneable::deepClone);
      res.network = this.network != null ? this.network.deepClone() : null;
      res.apis = CollectionUtil.deepCloneList(this.apis, DeepCloneable::deepClone);
      return res;
    }
  }
}
