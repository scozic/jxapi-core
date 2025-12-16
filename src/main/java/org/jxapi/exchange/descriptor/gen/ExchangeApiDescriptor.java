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
 * Part of a JSON document descriptor that describes a group of REST and or Websocket endpoints. Child element of ExchangeDescriptor. <br>
 * <h1>Constants</h1> <ul> <li>Can be specified a List of constants that are used in context of this API group of the exchange wrapper, for instance specific values for some APIs request/response/message properties.</li> <li>Each constant is described as a {@link org.jxapi.exchange.descriptor.Constant}</li> </ul> <h1>REST endpoints</h1> <ul> <li>There can be multiple REST endpoints, or no such endpoint, in which case <code>restEndpoints</code> property can be <code>null</code></li> <li>Each REST endpoint is described as a {@link org.jxapi.exchange.descriptor.gen.RestEndpointDescriptor}</li> <li>Each REST endpoint share the same HttpRequestExecutor and HttpRequestInterceptor, that are created from factories classes provided  in <code>httpRequestExecutorFactory</code> and <code>httpRequestInterceptorFactory</code> properties </li> <li><code>httpRequestExecutorFactory</code> property may be supplied to specify a factory class that creates HttpRequestExecutor instances, see {@link org.jxapi.netutils.rest.HttpRequestExecutorFactory}. When property is not set, default {@link JavaNetHttpRequestExecutor} is used</li> <li><code>httpRequestInterceptorFactory</code> property may be supplied to specify a factory class that creates HttpRequestInterceptor instances, see {@link org.jxapi.netutils.rest.HttpRequestInterceptorFactory}. When property is not set, no request interceptor is used</li> <li>API global Rate limits can be specified for the REST endpoints in <code>rateLimits</code> property. Those limits are shared among all defined REST endpoints.</li> <li>Rate limits from enclosing exchange descriptor are inherited by the API descriptor. Exchange global limit are shared among all REST endpoints of every API specified in exchange</li> </ul> <h1>Websocket endpoints</h1> <ul> <li>There can be multiple Websocket endpoints, or no such endpoint, in which case <code>websocketEndpoints</code> property can be <code>null</code></li> <li>Each Websocket endpoint is described as a {@link org.jxapi.exchange.descriptor.gen.WebsocketEndpointDescriptor}</li> <li>Each Websocket endpoint share the same WebsocketFactory and WebsocketHook, that are created from factories classes provided in <code>websocketFactory</code> and <code>websocketHookFactory</code> properties</li> <li><code>websocketFactory</code> property may be supplied to specify a factory class that creates Websocket instances, see {@link org.jxapi.netutils.websocket.WebsocketFactory}. When property is not set, default {@link org.jxapi.netutils.websocket.DefaultWebsocketFactory} is used</li> <li><code>websocketHookFactory</code> property may be supplied to specify a factory class that creates WebsocketHook instances, see {@link org.jxapi.netutils.websocket.WebsocketHookFactory}. When property is not set, no websocket hook is used. Such hook is needed though to implement specific handshake, heartbeat management and socket multiplexing (e.g. subscribing to multiple topics using same socket)</li> <li><code>websocketUrl</code> property may be supplied to specify the URL of the websocket endpoint. When property is not set, the URL is expected to be set by WebsocketHook on WebsocketManager during {@link org.jxapi.netutils.websocket.WebsocketHook#init(org.jxapi.netutils.websocket.WebsocketManager)} or {@link org.jxapi.netutils.websocket.WebsocketHook#beforeConnect()}</li> </ul>
 * <h2>Example of corresponding JSON, with sample REST endpoint and WebsocketEndpoint</h2> <pre> { 
 *   "name": "MarketData",
 *   "description": "The market data API of MyTestExchange",
 *   "httpRequestInterceptorFactory": "com.foo.bar.BarHttpRequestInterceptorFactory",
 *   "rateLimits": [
 *    {"id": "customRule", "timeFrame": 1500,  "maxTotalWeight": 300}
 *  ],
 *  "constants": 
 *     {"name":"responseCodeOk", "type": "INT", "description":"Value for REST response <i>responseCode</i> field. Success", "value":0},
 *     {"name":"responseCodeError", "type": "INT", "description":"Value for REST response <i>responseCode</i> field. Error", "value":-1}
 *   ],
 *   "restEndpoints": [
 *     {
 *       "name": "exchangeInfo",
 *       "httpMethod": "GET",
 *       "description": "Fetch market information of symbols that can be traded",
 *       "url": "https://com.sample.mycex/exchangeInfo",
 *       "request":{
 *         "properties": [
 *           {"name":"symbols", "type": "STRING_LIST", "description":"The list of symbol to fetch market information for. Leave empty to fetch all markets", "sampleValue":"[\"BTC\", \"ETH\"]"}
 *         ]
 *       },
 *       "response":{ 
 *         "properties": [
 *           {"name":"responseCode", "type": "INT", "description":"Request response code", "sampleValue":"0"},
 *           {"name":"payload", "type": "OBJECT_LIST", "description":"List of market information for each requested symbol", "properties":[
 *               {"name":"symbol", "type": "STRING", "description":"Market symbol", "sampleValue":"BTC_USDT"},
 *               {"name":"minOrderSize", "type": "BIGDECIMAL", "description":"Minimum order amount", "sampleValue":"0.0001"},
 *               {"name":"levels", "type": "INT_LIST", "description":"Amount precision levels", "sampleValue":[1,10,500]}
 *             ]
 *           }
 *         ]
 *       }
 *     },
 *     {
 *       "name": "tickers",
 *       "httpMethod": "GET",
 *       "description": "Fetch current tickers",
 *       "url": "https://com.sample.mycex/tickers",
 *       "request":{
 *         "properties": []
 *       },
 *       "response": { 
 *         "properties": [
 *           {"name":"responseCode", "type": "INT", "description":"Request response code", "sampleValue":"0"},
 *           {"name":"payload", "type": "OBJECT_MAP", "description":"Tickers for each symbol", "properties":[
 *               {"name":"last", "type": "BIGDECIMAL", "description":"Last traded price", "sampleValue":10.0}
 *             ]
 *           }
 *         ]
 *       }
 *     }
 *   ],
 *   "websocketUrl": "wss://com.foo.exchange/ws",
 *   "websocketFactory": "com.foo.bar.BarWebsocketFactory",
 *   "websocketHookFactory": "com.foo.bar.BarWebsocketHookFactory",
 *   "websocketEndpoints": [
 *     {
 *       "name": "tickerStream",
 *       "topic": "${symbol}@ticker",
 *       "description": "Subscribe to ticker stream",
 *       "request": {
 *         "properties": [
 *           {"name": "symbol", "type":"STRING", "description":"Symbol to subscribe to ticker stream of", "sampleValue":"BTC_USDT"}
 *         ]
 *       },
 *       "topicParametersListSeparator": "|",
 *       "messageTopicMatcherFields": [
 *         {"name": "topic",  "value": "ticker"},
 *         {"name": "symbol",  "value": "${symbol}"}
 *       ],
 *       "message": { 
 *         "properties": [
 *           {"name":"topic", "msgField":"t", "type": "STRING", "description":"Topic", "sampleValue":"ticker"},
 *           {"name":"symbol", "msgField":"s", "type": "STRING", "description":"Symbol name", "sampleValue":"BTC_USDT"},
 *           {"name":"last", "msgField":"p", "type": "BIGDECIMAL", "description":"Last traded price", "sampleValue":"16000.00"}
 *         ]
 *       }
 *     }
 *   ]
 * } </pre>
 * @see ExchangeDescriptor @see RestEndpointDescriptor @see WebsocketEndpointDescriptor @see RateLimitRule @see org.jxapi.netutils.rest.HttpRequestInterceptorFactory @see org.jxapi.netutils.rest.HttpRequestExecutorFactory @see org.jxapi.netutils.websocket.WebsocketFactory @see org.jxapi.netutils.websocket.WebsocketHookFactory @see Constant
 * 
 */
@Generated("org.jxapi.generator.java.pojo.PojoGenerator")
@JsonSerialize(using = ExchangeApiDescriptorSerializer.class)
@JsonDeserialize(using = ExchangeApiDescriptorDeserializer.class)
public class ExchangeApiDescriptor implements Pojo<ExchangeApiDescriptor> {
  
  private static final long serialVersionUID = 798372772153268035L;
  
  /**
   * @return A new builder to build {@link ExchangeApiDescriptor} objects
   */
  public static Builder builder() {
    return new Builder();
  }
  
  private String name;
  private String description;
  private String httpRequestExecutorFactory;
  private String httpRequestInterceptorFactory;
  private String httpUrl;
  private Long httpRequestTimeout;
  private String websocketFactory;
  private String websocketHookFactory;
  private String websocketUrl;
  private List<RateLimitRuleDescriptor> rateLimits;
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
   * @return The fully qualified class name of the {@link org.jxapi.netutils.rest.HttpRequestExecutorFactory} to use by default for all REST endpoints of this API group, unless overridden at endpoint level.
   * 
   */
  public String getHttpRequestExecutorFactory() {
    return httpRequestExecutorFactory;
  }
  
  /**
   * @param httpRequestExecutorFactory The fully qualified class name of the {@link org.jxapi.netutils.rest.HttpRequestExecutorFactory} to use by default for all REST endpoints of this API group, unless overridden at endpoint level.
   * 
   */
  public void setHttpRequestExecutorFactory(String httpRequestExecutorFactory) {
    this.httpRequestExecutorFactory = httpRequestExecutorFactory;
  }
  
  /**
   * @return The fully qualified class name of the {@link org.jxapi.netutils.rest.HttpRequestInterceptorFactory} to use by default for all REST endpoints of this API group, unless overridden at endpoint level.
   * 
   */
  public String getHttpRequestInterceptorFactory() {
    return httpRequestInterceptorFactory;
  }
  
  /**
   * @param httpRequestInterceptorFactory The fully qualified class name of the {@link org.jxapi.netutils.rest.HttpRequestInterceptorFactory} to use by default for all REST endpoints of this API group, unless overridden at endpoint level.
   * 
   */
  public void setHttpRequestInterceptorFactory(String httpRequestInterceptorFactory) {
    this.httpRequestInterceptorFactory = httpRequestInterceptorFactory;
  }
  
  /**
   * @return The base URL of the HTTP (REST) API of this API group. This URL can be concateneted to endpoint URL when endpoint URL is not absolute.
   * 
   */
  public String getHttpUrl() {
    return httpUrl;
  }
  
  /**
   * @param httpUrl The base URL of the HTTP (REST) API of this API group. This URL can be concateneted to endpoint URL when endpoint URL is not absolute.
   * 
   */
  public void setHttpUrl(String httpUrl) {
    this.httpUrl = httpUrl;
  }
  
  /**
   * @return The default HTTP request timeout in milliseconds to use by default for all REST endpoints of this API group, unless overridden at endpoint level.
   * 
   */
  public Long getHttpRequestTimeout() {
    return httpRequestTimeout;
  }
  
  /**
   * @param httpRequestTimeout The default HTTP request timeout in milliseconds to use by default for all REST endpoints of this API group, unless overridden at endpoint level.
   * 
   */
  public void setHttpRequestTimeout(Long httpRequestTimeout) {
    this.httpRequestTimeout = httpRequestTimeout;
  }
  
  /**
   * @return The fully qualified class name of the {@link org.jxapi.netutils.ws.WebsocketFactory} to use by default for all Websocket endpoints of this API group, unless overridden at endpoint level. May be null, in which case the default {@link org.jxapi.netutils.websocket.DefaultWebsocketFactory} is used. 
   * 
   */
  public String getWebsocketFactory() {
    return websocketFactory;
  }
  
  /**
   * @param websocketFactory The fully qualified class name of the {@link org.jxapi.netutils.ws.WebsocketFactory} to use by default for all Websocket endpoints of this API group, unless overridden at endpoint level. May be null, in which case the default {@link org.jxapi.netutils.websocket.DefaultWebsocketFactory} is used. 
   * 
   */
  public void setWebsocketFactory(String websocketFactory) {
    this.websocketFactory = websocketFactory;
  }
  
  /**
   * @return The fully qualified class name of the {@link org.jxapi.netutils.ws.WebsocketHookFactory} to use by default for all Websocket endpoints of this API group, unless overridden at endpoint level.
   * 
   */
  public String getWebsocketHookFactory() {
    return websocketHookFactory;
  }
  
  /**
   * @param websocketHookFactory The fully qualified class name of the {@link org.jxapi.netutils.ws.WebsocketHookFactory} to use by default for all Websocket endpoints of this API group, unless overridden at endpoint level.
   * 
   */
  public void setWebsocketHookFactory(String websocketHookFactory) {
    this.websocketHookFactory = websocketHookFactory;
  }
  
  /**
   * @return The base URL of the Websocket API of this API group. This URL can be concateneted to endpoint URL when endpoint URL is not absolute.  
   * 
   */
  public String getWebsocketUrl() {
    return websocketUrl;
  }
  
  /**
   * @param websocketUrl The base URL of the Websocket API of this API group. This URL can be concateneted to endpoint URL when endpoint URL is not absolute.  
   * 
   */
  public void setWebsocketUrl(String websocketUrl) {
    this.websocketUrl = websocketUrl;
  }
  
  public List<RateLimitRuleDescriptor> getRateLimits() {
    return rateLimits;
  }
  
  public void setRateLimits(List<RateLimitRuleDescriptor> rateLimits) {
    this.rateLimits = rateLimits;
  }
  
  /**
   * @return Describes a REST endpoint as part of a larger exchange API defined in a JSON document. These endpoints handle HTTP requests to a specific URL, process request parameters, and return a response.
   * <h2>Endpoint Properties</h2> The endpoint is defined by the following properties: <ul>
   *   <li>{@code name}: A unique identifier for the endpoint.</li>
   *   <li>{@code description}: A brief summary of the endpoint's purpose.</li>
   *   <li>{@code url}: The URL of the endpoint.</li>
   *   <li>{@code httpMethod}: The HTTP method for the request (e.g., GET, POST).</li>
   *   <li>{@code request}: The data structure for the request.</li>
   *   <li>{@code response}: The data structure for the response.</li>
   *   <li>{@code requestWeight}: The cost of a request, used for rate limiting.</li>
   *   <li>{@code rateLimits}: The rate limits applied to this endpoint.</li>
   * </ul>
   * <h2>Request Serialization</h2> <strong>Serialization to URL Parameters:</strong> <ul>
   *   <li>For HTTP methods that do not have a request body (e.g., {@code GET}, {@code DELETE}), the request is serialized into either URL path parameters (e.g., {@code /value1/value2}) or query parameters (e.g., {@code ?param1=value1}).</li>
   *   <li>By default, request fields are serialized as query parameters. To serialize them as URL path parameters, set the {@link org.jxapi.pojo.descriptor.Field#getIn()} property to {@link org.jxapi.pojo.descriptor.UrlParameterType#PATH}.</li>
   *   <li>If the request field is a primitive type, its value is URL-encoded and used as a single parameter.</li>
   *   <li>If the request field is an object without the {@code in} property set, its child fields are serialized as parameters.</li>
   *   <li>If the request field is an object with the {@code in} property set, or if it is a {@code MAP} or {@code LIST}, it is serialized as a URL-encoded JSON object.</li>
   *   <li>All field values are URL-encoded.</li>
   * </ul>
   * <h2>Code Generation</h2> API endpoints are defined as child elements of an {@code api} element in the JSON document (see {@link org.jxapi.exchange.descriptor.gen.ExchangeApiDescriptor}). This descriptor is used to generate method declarations in the API interface and their implementations.
   * @see org.jxapi.exchange.descriptor.gen.ExchangeApiInterfaceImplementationGenerator @see org.jxapi.exchange.descriptor.gen.ExchangeApiClassesGenerator
   * <h2>Request and Response Properties</h2> <ul>
   *   <li>Both {@code request} and {@code response} are described as {@link org.jxapi.pojo.descriptor.Field} objects.</li>
   *   <li>The name of the {@code request} field becomes the argument name in the generated API method.</li>
   *   <li>The name of the {@code response} field is not used in code generation.</li>
   *   <li>If {@code request} is omitted, the generated method will have no arguments.</li>
   *   <li>If {@code response} is omitted, the method will return a {@code STRING} containing the raw response body. This is useful when only the status code is needed and the body is empty.</li>
   * </ul>
   * @see Field @see RateLimitRule @see RestEndpointClassesGenerator @see Type @see HttpMethod
   * 
   */
  public List<RestEndpointDescriptor> getRestEndpoints() {
    return restEndpoints;
  }
  
  /**
   * @param restEndpoints Describes a REST endpoint as part of a larger exchange API defined in a JSON document. These endpoints handle HTTP requests to a specific URL, process request parameters, and return a response.
   * <h2>Endpoint Properties</h2> The endpoint is defined by the following properties: <ul>
   *   <li>{@code name}: A unique identifier for the endpoint.</li>
   *   <li>{@code description}: A brief summary of the endpoint's purpose.</li>
   *   <li>{@code url}: The URL of the endpoint.</li>
   *   <li>{@code httpMethod}: The HTTP method for the request (e.g., GET, POST).</li>
   *   <li>{@code request}: The data structure for the request.</li>
   *   <li>{@code response}: The data structure for the response.</li>
   *   <li>{@code requestWeight}: The cost of a request, used for rate limiting.</li>
   *   <li>{@code rateLimits}: The rate limits applied to this endpoint.</li>
   * </ul>
   * <h2>Request Serialization</h2> <strong>Serialization to URL Parameters:</strong> <ul>
   *   <li>For HTTP methods that do not have a request body (e.g., {@code GET}, {@code DELETE}), the request is serialized into either URL path parameters (e.g., {@code /value1/value2}) or query parameters (e.g., {@code ?param1=value1}).</li>
   *   <li>By default, request fields are serialized as query parameters. To serialize them as URL path parameters, set the {@link org.jxapi.pojo.descriptor.Field#getIn()} property to {@link org.jxapi.pojo.descriptor.UrlParameterType#PATH}.</li>
   *   <li>If the request field is a primitive type, its value is URL-encoded and used as a single parameter.</li>
   *   <li>If the request field is an object without the {@code in} property set, its child fields are serialized as parameters.</li>
   *   <li>If the request field is an object with the {@code in} property set, or if it is a {@code MAP} or {@code LIST}, it is serialized as a URL-encoded JSON object.</li>
   *   <li>All field values are URL-encoded.</li>
   * </ul>
   * <h2>Code Generation</h2> API endpoints are defined as child elements of an {@code api} element in the JSON document (see {@link org.jxapi.exchange.descriptor.gen.ExchangeApiDescriptor}). This descriptor is used to generate method declarations in the API interface and their implementations.
   * @see org.jxapi.exchange.descriptor.gen.ExchangeApiInterfaceImplementationGenerator @see org.jxapi.exchange.descriptor.gen.ExchangeApiClassesGenerator
   * <h2>Request and Response Properties</h2> <ul>
   *   <li>Both {@code request} and {@code response} are described as {@link org.jxapi.pojo.descriptor.Field} objects.</li>
   *   <li>The name of the {@code request} field becomes the argument name in the generated API method.</li>
   *   <li>The name of the {@code response} field is not used in code generation.</li>
   *   <li>If {@code request} is omitted, the generated method will have no arguments.</li>
   *   <li>If {@code response} is omitted, the method will return a {@code STRING} containing the raw response body. This is useful when only the status code is needed and the body is empty.</li>
   * </ul>
   * @see Field @see RateLimitRule @see RestEndpointClassesGenerator @see Type @see HttpMethod
   * 
   */
  public void setRestEndpoints(List<RestEndpointDescriptor> restEndpoints) {
    this.restEndpoints = restEndpoints;
  }
  
  /**
   * @return Part of JSON document describing a crypo exchange API, describes a websocket endpoint where clients subscription can be performed using specified topic and eventual additional parameters. The structure of additional subscription parameters and response format are described as {@link org.jxapi.pojo.descriptor.Field} lists.
   * 
   */
  public List<WebsocketEndpointDescriptor> getWebsocketEndpoints() {
    return websocketEndpoints;
  }
  
  /**
   * @param websocketEndpoints Part of JSON document describing a crypo exchange API, describes a websocket endpoint where clients subscription can be performed using specified topic and eventual additional parameters. The structure of additional subscription parameters and response format are described as {@link org.jxapi.pojo.descriptor.Field} lists.
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
        && Objects.equals(this.httpRequestExecutorFactory, o.httpRequestExecutorFactory)
        && Objects.equals(this.httpRequestInterceptorFactory, o.httpRequestInterceptorFactory)
        && Objects.equals(this.httpUrl, o.httpUrl)
        && Objects.equals(this.httpRequestTimeout, o.httpRequestTimeout)
        && Objects.equals(this.websocketFactory, o.websocketFactory)
        && Objects.equals(this.websocketHookFactory, o.websocketHookFactory)
        && Objects.equals(this.websocketUrl, o.websocketUrl)
        && Objects.equals(this.rateLimits, o.rateLimits)
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
    res = CompareUtil.compare(this.httpRequestExecutorFactory, other.httpRequestExecutorFactory);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compare(this.httpRequestInterceptorFactory, other.httpRequestInterceptorFactory);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compare(this.httpUrl, other.httpUrl);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compare(this.httpRequestTimeout, other.httpRequestTimeout);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compare(this.websocketFactory, other.websocketFactory);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compare(this.websocketHookFactory, other.websocketHookFactory);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compare(this.websocketUrl, other.websocketUrl);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compareLists(this.rateLimits, other.rateLimits, CompareUtil::compare);
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
    return Objects.hash(name, description, httpRequestExecutorFactory, httpRequestInterceptorFactory, httpUrl, httpRequestTimeout, websocketFactory, websocketHookFactory, websocketUrl, rateLimits, restEndpoints, websocketEndpoints);
  }
  
  @Override
  public ExchangeApiDescriptor deepClone() {
    ExchangeApiDescriptor clone = new ExchangeApiDescriptor();
    clone.name = this.name;
    clone.description = this.description;
    clone.httpRequestExecutorFactory = this.httpRequestExecutorFactory;
    clone.httpRequestInterceptorFactory = this.httpRequestInterceptorFactory;
    clone.httpUrl = this.httpUrl;
    clone.httpRequestTimeout = this.httpRequestTimeout;
    clone.websocketFactory = this.websocketFactory;
    clone.websocketHookFactory = this.websocketHookFactory;
    clone.websocketUrl = this.websocketUrl;
    clone.rateLimits = CollectionUtil.deepCloneList(this.rateLimits, DeepCloneable::deepClone);
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
    private String httpRequestExecutorFactory;
    private String httpRequestInterceptorFactory;
    private String httpUrl;
    private Long httpRequestTimeout;
    private String websocketFactory;
    private String websocketHookFactory;
    private String websocketUrl;
    private List<RateLimitRuleDescriptor> rateLimits;
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
     * Will set the value of <code>httpRequestExecutorFactory</code> field in the builder
     * @param httpRequestExecutorFactory The fully qualified class name of the {@link org.jxapi.netutils.rest.HttpRequestExecutorFactory} to use by default for all REST endpoints of this API group, unless overridden at endpoint level.
     * 
     * @return Builder instance
     * @see #setHttpRequestExecutorFactory(String)
     */
    public Builder httpRequestExecutorFactory(String httpRequestExecutorFactory)  {
      this.httpRequestExecutorFactory = httpRequestExecutorFactory;
      return this;
    }
    
    /**
     * Will set the value of <code>httpRequestInterceptorFactory</code> field in the builder
     * @param httpRequestInterceptorFactory The fully qualified class name of the {@link org.jxapi.netutils.rest.HttpRequestInterceptorFactory} to use by default for all REST endpoints of this API group, unless overridden at endpoint level.
     * 
     * @return Builder instance
     * @see #setHttpRequestInterceptorFactory(String)
     */
    public Builder httpRequestInterceptorFactory(String httpRequestInterceptorFactory)  {
      this.httpRequestInterceptorFactory = httpRequestInterceptorFactory;
      return this;
    }
    
    /**
     * Will set the value of <code>httpUrl</code> field in the builder
     * @param httpUrl The base URL of the HTTP (REST) API of this API group. This URL can be concateneted to endpoint URL when endpoint URL is not absolute.
     * 
     * @return Builder instance
     * @see #setHttpUrl(String)
     */
    public Builder httpUrl(String httpUrl)  {
      this.httpUrl = httpUrl;
      return this;
    }
    
    /**
     * Will set the value of <code>httpRequestTimeout</code> field in the builder
     * @param httpRequestTimeout The default HTTP request timeout in milliseconds to use by default for all REST endpoints of this API group, unless overridden at endpoint level.
     * 
     * @return Builder instance
     * @see #setHttpRequestTimeout(Long)
     */
    public Builder httpRequestTimeout(Long httpRequestTimeout)  {
      this.httpRequestTimeout = httpRequestTimeout;
      return this;
    }
    
    /**
     * Will set the value of <code>websocketFactory</code> field in the builder
     * @param websocketFactory The fully qualified class name of the {@link org.jxapi.netutils.ws.WebsocketFactory} to use by default for all Websocket endpoints of this API group, unless overridden at endpoint level. May be null, in which case the default {@link org.jxapi.netutils.websocket.DefaultWebsocketFactory} is used. 
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
     * @param websocketHookFactory The fully qualified class name of the {@link org.jxapi.netutils.ws.WebsocketHookFactory} to use by default for all Websocket endpoints of this API group, unless overridden at endpoint level.
     * 
     * @return Builder instance
     * @see #setWebsocketHookFactory(String)
     */
    public Builder websocketHookFactory(String websocketHookFactory)  {
      this.websocketHookFactory = websocketHookFactory;
      return this;
    }
    
    /**
     * Will set the value of <code>websocketUrl</code> field in the builder
     * @param websocketUrl The base URL of the Websocket API of this API group. This URL can be concateneted to endpoint URL when endpoint URL is not absolute.  
     * 
     * @return Builder instance
     * @see #setWebsocketUrl(String)
     */
    public Builder websocketUrl(String websocketUrl)  {
      this.websocketUrl = websocketUrl;
      return this;
    }
    
    /**
     * Will set the value of <code>rateLimits</code> field in the builder
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
     * @see ExchangeApiDescriptor#setRateLimits(List)
     */
    public Builder addToRateLimits(RateLimitRuleDescriptor item) {
      if (this.rateLimits == null) {
        this.rateLimits = CollectionUtil.createList();
      }
      this.rateLimits.add(item);
      return this;
    }
    
    /**
     * Will set the value of <code>restEndpoints</code> field in the builder
     * @param restEndpoints Describes a REST endpoint as part of a larger exchange API defined in a JSON document. These endpoints handle HTTP requests to a specific URL, process request parameters, and return a response.
     * <h2>Endpoint Properties</h2> The endpoint is defined by the following properties: <ul>
     *   <li>{@code name}: A unique identifier for the endpoint.</li>
     *   <li>{@code description}: A brief summary of the endpoint's purpose.</li>
     *   <li>{@code url}: The URL of the endpoint.</li>
     *   <li>{@code httpMethod}: The HTTP method for the request (e.g., GET, POST).</li>
     *   <li>{@code request}: The data structure for the request.</li>
     *   <li>{@code response}: The data structure for the response.</li>
     *   <li>{@code requestWeight}: The cost of a request, used for rate limiting.</li>
     *   <li>{@code rateLimits}: The rate limits applied to this endpoint.</li>
     * </ul>
     * <h2>Request Serialization</h2> <strong>Serialization to URL Parameters:</strong> <ul>
     *   <li>For HTTP methods that do not have a request body (e.g., {@code GET}, {@code DELETE}), the request is serialized into either URL path parameters (e.g., {@code /value1/value2}) or query parameters (e.g., {@code ?param1=value1}).</li>
     *   <li>By default, request fields are serialized as query parameters. To serialize them as URL path parameters, set the {@link org.jxapi.pojo.descriptor.Field#getIn()} property to {@link org.jxapi.pojo.descriptor.UrlParameterType#PATH}.</li>
     *   <li>If the request field is a primitive type, its value is URL-encoded and used as a single parameter.</li>
     *   <li>If the request field is an object without the {@code in} property set, its child fields are serialized as parameters.</li>
     *   <li>If the request field is an object with the {@code in} property set, or if it is a {@code MAP} or {@code LIST}, it is serialized as a URL-encoded JSON object.</li>
     *   <li>All field values are URL-encoded.</li>
     * </ul>
     * <h2>Code Generation</h2> API endpoints are defined as child elements of an {@code api} element in the JSON document (see {@link org.jxapi.exchange.descriptor.gen.ExchangeApiDescriptor}). This descriptor is used to generate method declarations in the API interface and their implementations.
     * @see org.jxapi.exchange.descriptor.gen.ExchangeApiInterfaceImplementationGenerator @see org.jxapi.exchange.descriptor.gen.ExchangeApiClassesGenerator
     * <h2>Request and Response Properties</h2> <ul>
     *   <li>Both {@code request} and {@code response} are described as {@link org.jxapi.pojo.descriptor.Field} objects.</li>
     *   <li>The name of the {@code request} field becomes the argument name in the generated API method.</li>
     *   <li>The name of the {@code response} field is not used in code generation.</li>
     *   <li>If {@code request} is omitted, the generated method will have no arguments.</li>
     *   <li>If {@code response} is omitted, the method will return a {@code STRING} containing the raw response body. This is useful when only the status code is needed and the body is empty.</li>
     * </ul>
     * @see Field @see RateLimitRule @see RestEndpointClassesGenerator @see Type @see HttpMethod
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
     * @param websocketEndpoints Part of JSON document describing a crypo exchange API, describes a websocket endpoint where clients subscription can be performed using specified topic and eventual additional parameters. The structure of additional subscription parameters and response format are described as {@link org.jxapi.pojo.descriptor.Field} lists.
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
      res.httpRequestExecutorFactory = this.httpRequestExecutorFactory;
      res.httpRequestInterceptorFactory = this.httpRequestInterceptorFactory;
      res.httpUrl = this.httpUrl;
      res.httpRequestTimeout = this.httpRequestTimeout;
      res.websocketFactory = this.websocketFactory;
      res.websocketHookFactory = this.websocketHookFactory;
      res.websocketUrl = this.websocketUrl;
      res.rateLimits = CollectionUtil.deepCloneList(this.rateLimits, DeepCloneable::deepClone);
      res.restEndpoints = CollectionUtil.deepCloneList(this.restEndpoints, DeepCloneable::deepClone);
      res.websocketEndpoints = CollectionUtil.deepCloneList(this.websocketEndpoints, DeepCloneable::deepClone);
      return res;
    }
  }
}
