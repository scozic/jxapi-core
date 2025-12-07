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
 * Root element of a JSON Exchange descriptor.<br> This class describes an exchange and its APIs<br> API will be described in groups of endpoints, as {@link ExchangeApiDescriptor} list.<br> Rate limits will be described as {@link RateLimitRule} list. These limits will be applied to all endpoints of each API group of the exchange.
 * JSON example:
 * <pre> {
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
 * } </pre>
 * <ul> <li>name: the name of the exchange. Remark that this is map to the ID of the exchange.</li> <li>description: the description of the exchange.</li> <li>basePackage: the base package of the exchange implementation classes.</li> <li>apis: the list of API groups of the exchange. See {@link ExchangeApiDescriptor}</li> <li>rateLimits: the list of rate limits of the exchange. These limits will be applied to all endpoints of each API group of the exchange. See {@link RateLimitRule}</li> <li>constants: list of constants that are used in context of the exchange wrapper, for instance specific values for some APIs request/response/message properties. See {@link Constant}</li> <li>properties: list of configuration properties that can be configured for the exchange wrapper, for instance API keys, secret keys, etc. See {@link DefaultConfigProperty}</li> <li><code>httpRequestInterceptorFactory</code>, <code>httpRequestExecutorFactory</code>, <code>httpRequestTimeout</code>, <code>websocketFactory</code>, <code>websocketHookFactory</code> properties can be set with default values for corresponding properties in {@link ExchangeApiDescriptor}: value defined in {@link ExchangeDescriptor} is used when undefined in {@link ExchangeApiDescriptor}. <li>httpUrl: the base URL of the HTTP (REST) API of the exchange. This URL can be concateneted to websocket API group <code>httpUrl</code> property (see {@link ExchangeApiDescriptor#getHttpUrl()} ) and endpoint URL (see {@link RestEndpointDescriptor#getUrl()} when one of these properties are not absolute)</li> <li>websocketUrl: the base URL of the Websocket API of the exchange. This URL can be concateneted to websocket API group <code>websocketUrl</code> property if API group <code>websocketUrl</code> is not not absolute</li> </ul>
 * This class is used to map the JSON descriptor of an exchange. It is used to generate code for the {@link Exchange} interface and implementation.
 * @see Exchange @see ExchangeApiDescriptor
 * 
 */
@Generated("org.jxapi.generator.java.pojo.PojoGenerator")
@JsonSerialize(using = ExchangeDescriptorSerializer.class)
@JsonDeserialize(using = ExchangeDescriptorDeserializer.class)
public class ExchangeDescriptor implements Pojo<ExchangeDescriptor> {
  
  private static final long serialVersionUID = -5091655605456715066L;
  
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
  private String httpRequestExecutorFactory;
  private String httpRequestInterceptorFactory;
  private String httpUrl;
  private String websocketUrl;
  private String websocketFactory;
  private String websocketHookFactory;
  private Long httpRequestTimeout;
  private String afterInitHookFactory;
  private List<ConfigPropertyDescriptor> properties;
  private List<ConstantDescriptor> constants;
  private List<RateLimitRuleDescriptor> rateLimits;
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
   * @return The fully qualified class name of the {@link org.jxapi.netutils.rest.HttpRequestExecutorFactory} to use by default for all REST endpoints of all API groups of the exchange, unless overridden at API group level.
   * 
   */
  public String getHttpRequestExecutorFactory() {
    return httpRequestExecutorFactory;
  }
  
  /**
   * @param httpRequestExecutorFactory The fully qualified class name of the {@link org.jxapi.netutils.rest.HttpRequestExecutorFactory} to use by default for all REST endpoints of all API groups of the exchange, unless overridden at API group level.
   * 
   */
  public void setHttpRequestExecutorFactory(String httpRequestExecutorFactory) {
    this.httpRequestExecutorFactory = httpRequestExecutorFactory;
  }
  
  /**
   * @return The fully qualified class name of the {@link org.jxapi.netutils.rest.HttpRequestInterceptorFactory} to use by default for all REST endpoints of all API groups of the exchange, unless overridden at API group level.
   * 
   */
  public String getHttpRequestInterceptorFactory() {
    return httpRequestInterceptorFactory;
  }
  
  /**
   * @param httpRequestInterceptorFactory The fully qualified class name of the {@link org.jxapi.netutils.rest.HttpRequestInterceptorFactory} to use by default for all REST endpoints of all API groups of the exchange, unless overridden at API group level.
   * 
   */
  public void setHttpRequestInterceptorFactory(String httpRequestInterceptorFactory) {
    this.httpRequestInterceptorFactory = httpRequestInterceptorFactory;
  }
  
  /**
   * @return The base URL of the HTTP (REST) API of the exchange. This URL can be concateneted to API group <code>httpUrl</code> and endpoint URL when one of these properties are not absolute.
   * 
   */
  public String getHttpUrl() {
    return httpUrl;
  }
  
  /**
   * @param httpUrl The base URL of the HTTP (REST) API of the exchange. This URL can be concateneted to API group <code>httpUrl</code> and endpoint URL when one of these properties are not absolute.
   * 
   */
  public void setHttpUrl(String httpUrl) {
    this.httpUrl = httpUrl;
  }
  
  /**
   * @return The base URL of the Websocket API of the exchange. This URL can be concateneted to API group <code>websocketUrl</code> if API group <code>websocketUrl</code> is not not absolute.  
   * 
   */
  public String getWebsocketUrl() {
    return websocketUrl;
  }
  
  /**
   * @param websocketUrl The base URL of the Websocket API of the exchange. This URL can be concateneted to API group <code>websocketUrl</code> if API group <code>websocketUrl</code> is not not absolute.  
   * 
   */
  public void setWebsocketUrl(String websocketUrl) {
    this.websocketUrl = websocketUrl;
  }
  
  /**
   * @return The fully qualified class name of the {@link org.jxapi.netutils.ws.WebsocketFactory} to use by default for all Websocket endpoints of all API groups of the exchange, unless overridden at API group level.
   * 
   */
  public String getWebsocketFactory() {
    return websocketFactory;
  }
  
  /**
   * @param websocketFactory The fully qualified class name of the {@link org.jxapi.netutils.ws.WebsocketFactory} to use by default for all Websocket endpoints of all API groups of the exchange, unless overridden at API group level.
   * 
   */
  public void setWebsocketFactory(String websocketFactory) {
    this.websocketFactory = websocketFactory;
  }
  
  /**
   * @return The fully qualified class name of the {@link org.jxapi.netutils.ws.WebsocketHookFactory} to use by default for all Websocket endpoints of all API groups of the exchange, unless overridden at API group level.
   * 
   */
  public String getWebsocketHookFactory() {
    return websocketHookFactory;
  }
  
  /**
   * @param websocketHookFactory The fully qualified class name of the {@link org.jxapi.netutils.ws.WebsocketHookFactory} to use by default for all Websocket endpoints of all API groups of the exchange, unless overridden at API group level.
   * 
   */
  public void setWebsocketHookFactory(String websocketHookFactory) {
    this.websocketHookFactory = websocketHookFactory;
  }
  
  /**
   * @return The default HTTP request timeout in milliseconds to use by default for all REST endpoints of all API groups of the exchange, unless overridden at API group level.
   * 
   */
  public Long getHttpRequestTimeout() {
    return httpRequestTimeout;
  }
  
  /**
   * @param httpRequestTimeout The default HTTP request timeout in milliseconds to use by default for all REST endpoints of all API groups of the exchange, unless overridden at API group level.
   * 
   */
  public void setHttpRequestTimeout(Long httpRequestTimeout) {
    this.httpRequestTimeout = httpRequestTimeout;
  }
  
  /**
   * @return The fully qualified class name of the {@link org.jxapi.exchange.ExchangetHookFactory} to be used to create an {@link org.jxapi.exchange.hooks.AfterExchangeInitHook} that will be executed just after the exchange wrapper initialization.
   * 
   */
  public String getAfterInitHookFactory() {
    return afterInitHookFactory;
  }
  
  /**
   * @param afterInitHookFactory The fully qualified class name of the {@link org.jxapi.exchange.ExchangetHookFactory} to be used to create an {@link org.jxapi.exchange.hooks.AfterExchangeInitHook} that will be executed just after the exchange wrapper initialization.
   * 
   */
  public void setAfterInitHookFactory(String afterInitHookFactory) {
    this.afterInitHookFactory = afterInitHookFactory;
  }
  
  /**
   * @return Represents a configuration property or a group of properties of an exchange like authentication credentials (API Key,secret) the wraooer client should provide to instantiate a wrapper.br> Exchange descriptor may contain a list of such properties as value of 'properties' property of exchange.<p> The name of a property should be spelled 'camelCase' like a Java variable name.<p> The value of a property can be a 'primitive' type e.g. {@link Type#STRING}, {@link Type#INT}, {@link Type#BOOLEAN}, {@link Type#BIGDECIMAL}, {@link Type#LONG}. It can't be a list, map, or object.<p> The properties will be exposed as static properties of a generated Java class named [exchangeId]Constants. That class wlll list constants for property names and default values, and default 'getter' methods for retrieving there values from properties <p> The properties can be grouped together. For example, authentication credentials can be grouped into a 'group' property called 'auth' with sub-properties for API key, secret, etc listed. Those properties can be referenced with key auth.apiKey, auth.apiSecret, etc. Groups may contain other groups, so the structure is hierarchical.
   * 
   */
  public List<ConfigPropertyDescriptor> getProperties() {
    return properties;
  }
  
  /**
   * @param properties Represents a configuration property or a group of properties of an exchange like authentication credentials (API Key,secret) the wraooer client should provide to instantiate a wrapper.br> Exchange descriptor may contain a list of such properties as value of 'properties' property of exchange.<p> The name of a property should be spelled 'camelCase' like a Java variable name.<p> The value of a property can be a 'primitive' type e.g. {@link Type#STRING}, {@link Type#INT}, {@link Type#BOOLEAN}, {@link Type#BIGDECIMAL}, {@link Type#LONG}. It can't be a list, map, or object.<p> The properties will be exposed as static properties of a generated Java class named [exchangeId]Constants. That class wlll list constants for property names and default values, and default 'getter' methods for retrieving there values from properties <p> The properties can be grouped together. For example, authentication credentials can be grouped into a 'group' property called 'auth' with sub-properties for API key, secret, etc listed. Those properties can be referenced with key auth.apiKey, auth.apiSecret, etc. Groups may contain other groups, so the structure is hierarchical.
   * 
   */
  public void setProperties(List<ConfigPropertyDescriptor> properties) {
    this.properties = properties;
  }
  
  /**
   * @return Represents a constant value used across APIs of an exchange or a group of such constants.<br> Exchange descriptor may contain a list of such constants as value of 'constants' property of exchange<br> Such constants will be exposed as static final fields in a generated Java class of the generated Java wrapper class for the exchange.<br> A constant may not be a final constant but a 'group' of constants that functionally come together. For example, when an exchange uses constants to represent bid or ask a side of,  it makes sense to group them together in a single constant group. In this case, final constants of the group will be exposed as static field of a nested public class in the main constant class, named after group name. A constant represents a group when its  <code>constants</code> property is set with a non empty list of nested constants<br> The name of a constant should be a valid camel case Java identifier.<br> The value of a constant must be a 'primitive' type e.g. {@link Type#STRING}, {@link Type#INT}, {@link Type#BOOLEAN}, {@link Type#BIGDECIMAL}, {@link Type#LONG}. It can't be a list, map, or object. <code>value</code> and <code>type</code> properties are relevant only when constant is not a group.<br> The name of a constant should provide a more readable name for the value. The description allows to provide semantic details.<br>
   * 
   */
  public List<ConstantDescriptor> getConstants() {
    return constants;
  }
  
  /**
   * @param constants Represents a constant value used across APIs of an exchange or a group of such constants.<br> Exchange descriptor may contain a list of such constants as value of 'constants' property of exchange<br> Such constants will be exposed as static final fields in a generated Java class of the generated Java wrapper class for the exchange.<br> A constant may not be a final constant but a 'group' of constants that functionally come together. For example, when an exchange uses constants to represent bid or ask a side of,  it makes sense to group them together in a single constant group. In this case, final constants of the group will be exposed as static field of a nested public class in the main constant class, named after group name. A constant represents a group when its  <code>constants</code> property is set with a non empty list of nested constants<br> The name of a constant should be a valid camel case Java identifier.<br> The value of a constant must be a 'primitive' type e.g. {@link Type#STRING}, {@link Type#INT}, {@link Type#BOOLEAN}, {@link Type#BIGDECIMAL}, {@link Type#LONG}. It can't be a list, map, or object. <code>value</code> and <code>type</code> properties are relevant only when constant is not a group.<br> The name of a constant should provide a more readable name for the value. The description allows to provide semantic details.<br>
   * 
   */
  public void setConstants(List<ConstantDescriptor> constants) {
    this.constants = constants;
  }
  
  /**
   * @return Represents a rate limit rule that applies to all API groups of the exchange.<br> Exchange descriptor may contain a list of such rate limit rules as value of 'rateLimits' property of exchange.<br> Such rate limit rules will be applied to all endpoints of each API group of the exchange.<br> See {@link RateLimitRule} for details.
   * 
   */
  public List<RateLimitRuleDescriptor> getRateLimits() {
    return rateLimits;
  }
  
  /**
   * @param rateLimits Represents a rate limit rule that applies to all API groups of the exchange.<br> Exchange descriptor may contain a list of such rate limit rules as value of 'rateLimits' property of exchange.<br> Such rate limit rules will be applied to all endpoints of each API group of the exchange.<br> See {@link RateLimitRule} for details.
   * 
   */
  public void setRateLimits(List<RateLimitRuleDescriptor> rateLimits) {
    this.rateLimits = rateLimits;
  }
  
  /**
   * @return Part of a JSON document descriptor that describes a group of REST and or Websocket endpoints. Child element of ExchangeDescriptor. <br>
   * <h2>Constants</h2> <ul> <li>Can be specified a List of constants that are used in context of this API group of the exchange wrapper, for instance specific values for some APIs request/response/message properties.</li> <li>Each constant is described as a {@link Constant}</li> </ul> <h2>REST endpoints</h2> <ul> <li>There can be multiple REST endpoints, or no such endpoint, in which case <code>restEndpoints</code> property can be <code>null</code></li> <li>Each REST endpoint is described as a {@link RestEndpointDescriptor}</li> <li>Each REST endpoint share the same HttpRequestExecutor and HttpRequestInterceptor, that are created from factories classes provided  in <code>httpRequestExecutorFactory</code> and <code>httpRequestInterceptorFactory</code> properties </li> <li><code>httpRequestExecutorFactory</code> property may be supplied to specify a factory class that creates HttpRequestExecutor instances, see {@link HttpRequestExecutorFactory}. When property is not set, default {@link JavaNetHttpRequestExecutor} is used</li> <li><code>httpRequestInterceptorFactory</code> property may be supplied to specify a factory class that creates HttpRequestInterceptor instances, see {@link HttpRequestInterceptorFactory}. When property is not set, no request interceptor is used</li> <li>API global Rate limits can be specified for the REST endpoints in <code>rateLimits</code> property. Those limits are shared among all defined REST endpoints.</li> <li>Rate limits from enclosing exchange descriptor are inherited by the API descriptor. Exchange global limit are shared among all REST endpoints of every API specified in exchange</li> </ul> <h2>Websocket endpoints</h2> <ul> <li>There can be multiple Websocket endpoints, or no such endpoint, in which case <code>websocketEndpoints</code> property can be <code>null</code></li> <li>Each Websocket endpoint is described as a {@link WebsocketEndpointDescriptor}</li> <li>Each Websocket endpoint share the same WebsocketFactory and WebsocketHook, that are created from factories classes provided in <code>websocketFactory</code> and <code>websocketHookFactory</code> properties</li> <li><code>websocketFactory</code> property may be supplied to specify a factory class that creates Websocket instances, see {@link WebsocketFactory}. When property is not set, default {@link DefaultWebsocketFactory} is used</li> <li><code>websocketHookFactory</code> property may be supplied to specify a factory class that creates WebsocketHook instances, see {@link WebsocketHookFactory}. When property is not set, no websocket hook is used. Such hook is needed though to implement specific handshake, heartbeat management and socket multiplexing (e.g. subscribing to multiple topics using same socket)</li> <li><code>websocketUrl</code> property may be supplied to specify the URL of the websocket endpoint. When property is not set, the URL is expected to be set by WebsocketHook on WebsocketManager during {@link WebsocketHook#init(org.jxapi.netutils.websocket.WebsocketManager)} or {@link WebsocketHook#beforeConnect()}</li> </ul>
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
  public List<ExchangeApiDescriptor> getApis() {
    return apis;
  }
  
  /**
   * @param apis Part of a JSON document descriptor that describes a group of REST and or Websocket endpoints. Child element of ExchangeDescriptor. <br>
   * <h2>Constants</h2> <ul> <li>Can be specified a List of constants that are used in context of this API group of the exchange wrapper, for instance specific values for some APIs request/response/message properties.</li> <li>Each constant is described as a {@link Constant}</li> </ul> <h2>REST endpoints</h2> <ul> <li>There can be multiple REST endpoints, or no such endpoint, in which case <code>restEndpoints</code> property can be <code>null</code></li> <li>Each REST endpoint is described as a {@link RestEndpointDescriptor}</li> <li>Each REST endpoint share the same HttpRequestExecutor and HttpRequestInterceptor, that are created from factories classes provided  in <code>httpRequestExecutorFactory</code> and <code>httpRequestInterceptorFactory</code> properties </li> <li><code>httpRequestExecutorFactory</code> property may be supplied to specify a factory class that creates HttpRequestExecutor instances, see {@link HttpRequestExecutorFactory}. When property is not set, default {@link JavaNetHttpRequestExecutor} is used</li> <li><code>httpRequestInterceptorFactory</code> property may be supplied to specify a factory class that creates HttpRequestInterceptor instances, see {@link HttpRequestInterceptorFactory}. When property is not set, no request interceptor is used</li> <li>API global Rate limits can be specified for the REST endpoints in <code>rateLimits</code> property. Those limits are shared among all defined REST endpoints.</li> <li>Rate limits from enclosing exchange descriptor are inherited by the API descriptor. Exchange global limit are shared among all REST endpoints of every API specified in exchange</li> </ul> <h2>Websocket endpoints</h2> <ul> <li>There can be multiple Websocket endpoints, or no such endpoint, in which case <code>websocketEndpoints</code> property can be <code>null</code></li> <li>Each Websocket endpoint is described as a {@link WebsocketEndpointDescriptor}</li> <li>Each Websocket endpoint share the same WebsocketFactory and WebsocketHook, that are created from factories classes provided in <code>websocketFactory</code> and <code>websocketHookFactory</code> properties</li> <li><code>websocketFactory</code> property may be supplied to specify a factory class that creates Websocket instances, see {@link WebsocketFactory}. When property is not set, default {@link DefaultWebsocketFactory} is used</li> <li><code>websocketHookFactory</code> property may be supplied to specify a factory class that creates WebsocketHook instances, see {@link WebsocketHookFactory}. When property is not set, no websocket hook is used. Such hook is needed though to implement specific handshake, heartbeat management and socket multiplexing (e.g. subscribing to multiple topics using same socket)</li> <li><code>websocketUrl</code> property may be supplied to specify the URL of the websocket endpoint. When property is not set, the URL is expected to be set by WebsocketHook on WebsocketManager during {@link WebsocketHook#init(org.jxapi.netutils.websocket.WebsocketManager)} or {@link WebsocketHook#beforeConnect()}</li> </ul>
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
        && Objects.equals(this.httpRequestExecutorFactory, o.httpRequestExecutorFactory)
        && Objects.equals(this.httpRequestInterceptorFactory, o.httpRequestInterceptorFactory)
        && Objects.equals(this.httpUrl, o.httpUrl)
        && Objects.equals(this.websocketUrl, o.websocketUrl)
        && Objects.equals(this.websocketFactory, o.websocketFactory)
        && Objects.equals(this.websocketHookFactory, o.websocketHookFactory)
        && Objects.equals(this.httpRequestTimeout, o.httpRequestTimeout)
        && Objects.equals(this.afterInitHookFactory, o.afterInitHookFactory)
        && Objects.equals(this.properties, o.properties)
        && Objects.equals(this.constants, o.constants)
        && Objects.equals(this.rateLimits, o.rateLimits)
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
    res = CompareUtil.compare(this.websocketUrl, other.websocketUrl);
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
    res = CompareUtil.compare(this.httpRequestTimeout, other.httpRequestTimeout);
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
    res = CompareUtil.compareLists(this.apis, other.apis, CompareUtil::compare);
    return res;
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(id, jxapi, version, description, docUrl, basePackage, httpRequestExecutorFactory, httpRequestInterceptorFactory, httpUrl, websocketUrl, websocketFactory, websocketHookFactory, httpRequestTimeout, afterInitHookFactory, properties, constants, rateLimits, apis);
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
    clone.httpRequestExecutorFactory = this.httpRequestExecutorFactory;
    clone.httpRequestInterceptorFactory = this.httpRequestInterceptorFactory;
    clone.httpUrl = this.httpUrl;
    clone.websocketUrl = this.websocketUrl;
    clone.websocketFactory = this.websocketFactory;
    clone.websocketHookFactory = this.websocketHookFactory;
    clone.httpRequestTimeout = this.httpRequestTimeout;
    clone.afterInitHookFactory = this.afterInitHookFactory;
    clone.properties = CollectionUtil.deepCloneList(this.properties, DeepCloneable::deepClone);
    clone.constants = CollectionUtil.deepCloneList(this.constants, DeepCloneable::deepClone);
    clone.rateLimits = CollectionUtil.deepCloneList(this.rateLimits, DeepCloneable::deepClone);
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
    private String httpRequestExecutorFactory;
    private String httpRequestInterceptorFactory;
    private String httpUrl;
    private String websocketUrl;
    private String websocketFactory;
    private String websocketHookFactory;
    private Long httpRequestTimeout;
    private String afterInitHookFactory;
    private List<ConfigPropertyDescriptor> properties;
    private List<ConstantDescriptor> constants;
    private List<RateLimitRuleDescriptor> rateLimits;
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
     * Will set the value of <code>httpRequestExecutorFactory</code> field in the builder
     * @param httpRequestExecutorFactory The fully qualified class name of the {@link org.jxapi.netutils.rest.HttpRequestExecutorFactory} to use by default for all REST endpoints of all API groups of the exchange, unless overridden at API group level.
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
     * @param httpRequestInterceptorFactory The fully qualified class name of the {@link org.jxapi.netutils.rest.HttpRequestInterceptorFactory} to use by default for all REST endpoints of all API groups of the exchange, unless overridden at API group level.
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
     * @param httpUrl The base URL of the HTTP (REST) API of the exchange. This URL can be concateneted to API group <code>httpUrl</code> and endpoint URL when one of these properties are not absolute.
     * 
     * @return Builder instance
     * @see #setHttpUrl(String)
     */
    public Builder httpUrl(String httpUrl)  {
      this.httpUrl = httpUrl;
      return this;
    }
    
    /**
     * Will set the value of <code>websocketUrl</code> field in the builder
     * @param websocketUrl The base URL of the Websocket API of the exchange. This URL can be concateneted to API group <code>websocketUrl</code> if API group <code>websocketUrl</code> is not not absolute.  
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
     * @param websocketFactory The fully qualified class name of the {@link org.jxapi.netutils.ws.WebsocketFactory} to use by default for all Websocket endpoints of all API groups of the exchange, unless overridden at API group level.
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
     * @param websocketHookFactory The fully qualified class name of the {@link org.jxapi.netutils.ws.WebsocketHookFactory} to use by default for all Websocket endpoints of all API groups of the exchange, unless overridden at API group level.
     * 
     * @return Builder instance
     * @see #setWebsocketHookFactory(String)
     */
    public Builder websocketHookFactory(String websocketHookFactory)  {
      this.websocketHookFactory = websocketHookFactory;
      return this;
    }
    
    /**
     * Will set the value of <code>httpRequestTimeout</code> field in the builder
     * @param httpRequestTimeout The default HTTP request timeout in milliseconds to use by default for all REST endpoints of all API groups of the exchange, unless overridden at API group level.
     * 
     * @return Builder instance
     * @see #setHttpRequestTimeout(Long)
     */
    public Builder httpRequestTimeout(Long httpRequestTimeout)  {
      this.httpRequestTimeout = httpRequestTimeout;
      return this;
    }
    
    /**
     * Will set the value of <code>afterInitHookFactory</code> field in the builder
     * @param afterInitHookFactory The fully qualified class name of the {@link org.jxapi.exchange.ExchangetHookFactory} to be used to create an {@link org.jxapi.exchange.hooks.AfterExchangeInitHook} that will be executed just after the exchange wrapper initialization.
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
     * @param properties Represents a configuration property or a group of properties of an exchange like authentication credentials (API Key,secret) the wraooer client should provide to instantiate a wrapper.br> Exchange descriptor may contain a list of such properties as value of 'properties' property of exchange.<p> The name of a property should be spelled 'camelCase' like a Java variable name.<p> The value of a property can be a 'primitive' type e.g. {@link Type#STRING}, {@link Type#INT}, {@link Type#BOOLEAN}, {@link Type#BIGDECIMAL}, {@link Type#LONG}. It can't be a list, map, or object.<p> The properties will be exposed as static properties of a generated Java class named [exchangeId]Constants. That class wlll list constants for property names and default values, and default 'getter' methods for retrieving there values from properties <p> The properties can be grouped together. For example, authentication credentials can be grouped into a 'group' property called 'auth' with sub-properties for API key, secret, etc listed. Those properties can be referenced with key auth.apiKey, auth.apiSecret, etc. Groups may contain other groups, so the structure is hierarchical.
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
     * @see ExchangeDescriptor#setProperties(ConfigPropertyDescriptor)
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
     * @param constants Represents a constant value used across APIs of an exchange or a group of such constants.<br> Exchange descriptor may contain a list of such constants as value of 'constants' property of exchange<br> Such constants will be exposed as static final fields in a generated Java class of the generated Java wrapper class for the exchange.<br> A constant may not be a final constant but a 'group' of constants that functionally come together. For example, when an exchange uses constants to represent bid or ask a side of,  it makes sense to group them together in a single constant group. In this case, final constants of the group will be exposed as static field of a nested public class in the main constant class, named after group name. A constant represents a group when its  <code>constants</code> property is set with a non empty list of nested constants<br> The name of a constant should be a valid camel case Java identifier.<br> The value of a constant must be a 'primitive' type e.g. {@link Type#STRING}, {@link Type#INT}, {@link Type#BOOLEAN}, {@link Type#BIGDECIMAL}, {@link Type#LONG}. It can't be a list, map, or object. <code>value</code> and <code>type</code> properties are relevant only when constant is not a group.<br> The name of a constant should provide a more readable name for the value. The description allows to provide semantic details.<br>
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
     * @see ExchangeDescriptor#setConstants(ConstantDescriptor)
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
     * @param rateLimits Represents a rate limit rule that applies to all API groups of the exchange.<br> Exchange descriptor may contain a list of such rate limit rules as value of 'rateLimits' property of exchange.<br> Such rate limit rules will be applied to all endpoints of each API group of the exchange.<br> See {@link RateLimitRule} for details.
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
     * @see ExchangeDescriptor#setRateLimits(RateLimitRuleDescriptor)
     */
    public Builder addToRateLimits(RateLimitRuleDescriptor item) {
      if (this.rateLimits == null) {
        this.rateLimits = CollectionUtil.createList();
      }
      this.rateLimits.add(item);
      return this;
    }
    
    /**
     * Will set the value of <code>apis</code> field in the builder
     * @param apis Part of a JSON document descriptor that describes a group of REST and or Websocket endpoints. Child element of ExchangeDescriptor. <br>
     * <h2>Constants</h2> <ul> <li>Can be specified a List of constants that are used in context of this API group of the exchange wrapper, for instance specific values for some APIs request/response/message properties.</li> <li>Each constant is described as a {@link Constant}</li> </ul> <h2>REST endpoints</h2> <ul> <li>There can be multiple REST endpoints, or no such endpoint, in which case <code>restEndpoints</code> property can be <code>null</code></li> <li>Each REST endpoint is described as a {@link RestEndpointDescriptor}</li> <li>Each REST endpoint share the same HttpRequestExecutor and HttpRequestInterceptor, that are created from factories classes provided  in <code>httpRequestExecutorFactory</code> and <code>httpRequestInterceptorFactory</code> properties </li> <li><code>httpRequestExecutorFactory</code> property may be supplied to specify a factory class that creates HttpRequestExecutor instances, see {@link HttpRequestExecutorFactory}. When property is not set, default {@link JavaNetHttpRequestExecutor} is used</li> <li><code>httpRequestInterceptorFactory</code> property may be supplied to specify a factory class that creates HttpRequestInterceptor instances, see {@link HttpRequestInterceptorFactory}. When property is not set, no request interceptor is used</li> <li>API global Rate limits can be specified for the REST endpoints in <code>rateLimits</code> property. Those limits are shared among all defined REST endpoints.</li> <li>Rate limits from enclosing exchange descriptor are inherited by the API descriptor. Exchange global limit are shared among all REST endpoints of every API specified in exchange</li> </ul> <h2>Websocket endpoints</h2> <ul> <li>There can be multiple Websocket endpoints, or no such endpoint, in which case <code>websocketEndpoints</code> property can be <code>null</code></li> <li>Each Websocket endpoint is described as a {@link WebsocketEndpointDescriptor}</li> <li>Each Websocket endpoint share the same WebsocketFactory and WebsocketHook, that are created from factories classes provided in <code>websocketFactory</code> and <code>websocketHookFactory</code> properties</li> <li><code>websocketFactory</code> property may be supplied to specify a factory class that creates Websocket instances, see {@link WebsocketFactory}. When property is not set, default {@link DefaultWebsocketFactory} is used</li> <li><code>websocketHookFactory</code> property may be supplied to specify a factory class that creates WebsocketHook instances, see {@link WebsocketHookFactory}. When property is not set, no websocket hook is used. Such hook is needed though to implement specific handshake, heartbeat management and socket multiplexing (e.g. subscribing to multiple topics using same socket)</li> <li><code>websocketUrl</code> property may be supplied to specify the URL of the websocket endpoint. When property is not set, the URL is expected to be set by WebsocketHook on WebsocketManager during {@link WebsocketHook#init(org.jxapi.netutils.websocket.WebsocketManager)} or {@link WebsocketHook#beforeConnect()}</li> </ul>
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
     * @see ExchangeDescriptor#setApis(ExchangeApiDescriptor)
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
      res.httpRequestExecutorFactory = this.httpRequestExecutorFactory;
      res.httpRequestInterceptorFactory = this.httpRequestInterceptorFactory;
      res.httpUrl = this.httpUrl;
      res.websocketUrl = this.websocketUrl;
      res.websocketFactory = this.websocketFactory;
      res.websocketHookFactory = this.websocketHookFactory;
      res.httpRequestTimeout = this.httpRequestTimeout;
      res.afterInitHookFactory = this.afterInitHookFactory;
      res.properties = CollectionUtil.deepCloneList(this.properties, DeepCloneable::deepClone);
      res.constants = CollectionUtil.deepCloneList(this.constants, DeepCloneable::deepClone);
      res.rateLimits = CollectionUtil.deepCloneList(this.rateLimits, DeepCloneable::deepClone);
      res.apis = CollectionUtil.deepCloneList(this.apis, DeepCloneable::deepClone);
      return res;
    }
  }
}
