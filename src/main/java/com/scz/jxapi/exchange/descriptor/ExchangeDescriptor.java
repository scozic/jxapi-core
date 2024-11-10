package com.scz.jxapi.exchange.descriptor;

import java.util.List;

import com.scz.jxapi.exchange.Exchange;
import com.scz.jxapi.exchange.ExchangeApi;
import com.scz.jxapi.netutils.rest.HttpRequestExecutorFactory;
import com.scz.jxapi.netutils.rest.HttpRequestInterceptorFactory;
import com.scz.jxapi.netutils.rest.ratelimits.RateLimitRule;
import com.scz.jxapi.util.EncodingUtil;

/**
 * Root element of a JSON Exchange descriptor.<br>
 * This class describes an exchange and its APIs<br>
 * API will be described in groups of endpoints, as
 * {@link ExchangeApiDescriptor} list.<br>
 * Rate limits will be described as {@link RateLimitRule} list. These limits
 * will be applied to all endpoints of each API group of the exchange.
 * 
 * JSON example:
 * 
 * <pre>
 * {
 * 		"name": "Binance",
 * 		"ID": "BINANCE",
 * 		"description": "Binance exchange",
 * 		"docUrl": "https://binance-docs.github.io/apidocs",
 * 		"basePackage": "com.scz.jxapi.exchange.binance",
 * 		"properties": [
 * 			{
 * 				"name": "apiKey",
 * 				"type": "STRING",
 * 				"description": "API key for authentication",
 * 			}
 * 		],
 * 		"constants": [
 * 			{
 * 				"name": "API_BASE_URL",
 * 				"description": "Base URL of the API",
 * 				"type": "STRING",
 * 				"value": "https://api.myexchange.com"
 * 			}
 * 		"apis": [
 * 			{
 * 				"name": "Spot",
 * 				"description": "Spot trading API",
 * 				"httpRequestExecutorFactory": "com.scz.jxapi.netutils.rest.mock.MockHttpRequestExecutorFactory",
 * 				"httpRequestInterceptorFactory": "com.scz.jxapi.netutils.rest.mock.MockHttpRequestInterceptorFactory",
 * 				"restEndpoints": [
 * 					// RESTendpoints here
 * 				],
 * 				"websocketEndpoints": [
 * 					// Websocket endpoints here
 * 				],
 * 			}
 * 		],
 * 		"rateLimits": [
 * 			{
 * 				"timeFrame": 60000,
 * 		        "maxRequestCount": 1200,
 * 		        "maxTotalWeight": 1200
 * 			}
 * 		]
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
 * {@link ExchangeApiDescriptor}</li>
 * <li>rateLimits: the list of rate limits of the exchange. These limits will be
 * applied to all endpoints of each API group of the exchange. See
 * {@link RateLimitRule}</li>
 * <li>constants: list of constants that are used in context of the exchange
 * wrapper, for instance specific values for some APIs
 * request/response/message properties. See {@link Constant}</li>
 * <li>properties: list of configuration properties that can be configured for
 * the exchange
 * wrapper, for instance API keys, secret keys, etc. See
 * {@link ConfigProperty}</li>
 * <li><code>httpRequestInterceptorFactory</code>,
 * <code>httpRequestExecutorFactory</code>, <code>httpRequestTimeout</code>,
 * <code>websocketFactory</code>, <code>websocketHookFactory</code> properties
 * can be set with default values for corresponding properties in
 * {@link ExchangeApiDescriptor}: value defined in {@link ExchangeDescriptor} is
 * used when undefined in {@link ExchangeApiDescriptor}.
 * <li>httpUrl: the base URL of the HTTP (REST) API of the exchange. This URL
 * can be concateneted to websocket API group <code>httpUrl</code> property (see
 * {@link ExchangeApiDescriptor#getHttpUrl()} ) and endpoint URL (see
 * {@link RestEndpointDescriptor#getUrl()} when one of these properties are not
 * absolute)</li>
 * <li>websocketUrl: the base URL of the Websocket API of the exchange. This URL
 * can be concateneted to websocket API group <code>websocketUrl</code> property
 * if API group <code>websocketUrl</code> is not not absolute</li>
 * </ul>
 * 
 * This class is used to map the JSON descriptor of an exchange. It is used to
 * generate code for the {@link Exchange} interface and implementation.
 * 
 * @see Exchange
 * @see ExchangeApiDescriptor
 */
public class ExchangeDescriptor {
	
	private String name;
	
	private String description;
	
	private String docUrl;
	
	private String basePackage;
	
	private List<ExchangeApiDescriptor> apis;
	
	private List<RateLimitRule> rateLimits;
	
	private List<Constant> constants;
	
	private List<ConfigProperty> properties;
	
	private String httpRequestInterceptorFactory;
	
	private String httpRequestExecutorFactory;
	
	private long httpRequestTimeout = -1L;
	
	private String websocketFactory;
	
	private String websocketHookFactory;
	
	private String httpUrl;
	
	private String websocketUrl;

	/**
	 * Returns the list of APIs of the exchange.
	 * @return the list of APIs of the exchange
	 * @see ExchangeApiDescriptor
	 */
	public List<ExchangeApiDescriptor> getApis() {
		return apis;
	}

	/**
	 * Sets the list of APIs of the exchange.
	 * @param apis the list of APIs of the exchange
	 * @see ExchangeApiDescriptor
	 */
	public void setApis(List<ExchangeApiDescriptor> apis) {
		this.apis = apis;
	}

	/**
	 * Returns the name of the exchange. Remark that this is map to the ID of the exchange.
	 * @return the name of the exchange
	 * @see Exchange#getId()
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the exchange. Remark that this is map to the ID of the exchange.
	 * @param name the name of the exchange
	 * @see Exchange#getId()
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the description of the exchange.
	 * @return the description of the exchange
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description of the exchange.
	 * @param description the description of the exchange
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * @return Exchange's website API documentation home page
	 */
	public String getDocUrl() {
		return docUrl;
	}

	/**
	 * @param excgangeUrl Exchange's website API documentation home page
	 */
	public void setDocUrl(String excgangeUrl) {
		this.docUrl = excgangeUrl;
	}
	
	/**
	 * Returns the base package of the exchange implementation classes.
	 * @return the base package of the exchange
	 */
	public String getBasePackage() {
		return basePackage;
	}

	/**
	 * Sets the base package of the exchange implementation classes.
	 * @param basePackage the base package of the exchange
	 */
	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}
	
	/**
	 * Returns the list of rate limits of the exchange. These limits will be applied
	 * to all endpoints of each API group of the exchange.
	 * 
	 * @return the list of rate limits of the exchange
	 * @see RateLimitRule
	 */
	public List<RateLimitRule> getRateLimits() {
		return rateLimits;
	}

	/**
	 * Sets the list of rate limits of the exchange. These limits will be applied
	 * to all endpoints of each API group of the exchange.
	 * 
	 * @param rateLimits the list of rate limits of the exchange
	 * @see RateLimitRule
	 */
	public void setRateLimits(List<RateLimitRule> rateLimits) {
		this.rateLimits = rateLimits;
	}
	
	/**
	 * @return List of constants that are used in context of the exchange wrapper,
	 *         for instance specific values for some APIs request/response/message
	 *         properties.
	 * @see Constant
	 */
	public List<Constant> getConstants() {
		return constants;
	}

	/**
	 * @param constants List of constants that are used in context of the exchange
	 *                  wrapper, for instance specific values for some APIs
	 *                  request/response/message properties.
	 * @see Constant
	 */
	public void setConstants(List<Constant> constants) {
		this.constants = constants;
	}

	/**
	 * @return List of properties that can be configured for the exchange wrapper,
	 *         for instance API keys, secret keys, etc.
	 * @see ConfigProperty
	 */
	public List<ConfigProperty> getProperties() {
		return properties;
	}

	/**
	 * @param properties List of properties that can be configured for the exchange
	 *                   wrapper, for instance API keys, secret keys, etc.
	 * @see ConfigProperty
	 */
	public void setProperties(List<ConfigProperty> properties) {
		this.properties = properties;
	}
	
	/**
	 * @return Default value for {@link ExchangeApi}
	 *         {@link HttpRequestInterceptorFactory} class
	 * @see ExchangeApiDescriptor#getHttpRequestInterceptorFactory()
	 */
	public String getHttpRequestInterceptorFactory() {
		return httpRequestInterceptorFactory;
	}

	/**
	 * @param httpRequestInterceptorFactory Default value for {@link ExchangeApi}
	 *                                      {@link HttpRequestInterceptorFactory}
	 *                                      class
	 * @see ExchangeApiDescriptor#getHttpRequestInterceptorFactory()
	 */
	public void setHttpRequestInterceptorFactory(String httpRequestInterceptorFactory) {
		this.httpRequestInterceptorFactory = httpRequestInterceptorFactory;
	}

	/**
	 * @return Default value for {@link ExchangeApi}
	 *         {@link HttpRequestExecutorFactory} class
	 * @see ExchangeApiDescriptor#getHttpRequestExecutorFactory()
	 */
	public String getHttpRequestExecutorFactory() {
		return httpRequestExecutorFactory;
	}

	/**
	 * @param httpRequestExecutorFactory Default value for {@link ExchangeApi}
	 *                                   {@link HttpRequestExecutorFactory} class
	 * @see ExchangeApiDescriptor#getHttpRequestExecutorFactory()
	 */
	public void setHttpRequestExecutorFactory(String httpRequestExecutorFactory) {
		this.httpRequestExecutorFactory = httpRequestExecutorFactory;
	}

	/**
	 * @return Default value for {@link ExchangeApi} HTTP request timeout
	 * @see ExchangeApiDescriptor#getHttpRequestTimeout()
	 */
	public long getHttpRequestTimeout() {
		return httpRequestTimeout;
	}

	/**
	 * @param httpRequestTimeout Default value for {@link ExchangeApi} HTTP request
	 *                           timeout
	 * @see ExchangeApiDescriptor#getHttpRequestTimeout()
	 */
	public void setHttpRequestTimeout(long httpRequestTimeout) {
		this.httpRequestTimeout = httpRequestTimeout;
	}
	
	public String getHttpUrl() {
		return httpUrl;
	}

	public void setHttpUrl(String httpUrl) {
		this.httpUrl = httpUrl;
	}

	/**
	 * @return Default value for {@link ExchangeApi} websocket factory class
	 * @see ExchangeApiDescriptor#getWebsocketFactory()
	 */
	public String getWebsocketFactory() {
		return websocketFactory;
	}

	/**
	 * @param websocketFactory Default value for {@link ExchangeApi} websocket
	 *                         factory class
	 * @see ExchangeApiDescriptor#getWebsocketFactory()
	 */
	public void setWebsocketFactory(String websocketFactory) {
		this.websocketFactory = websocketFactory;
	}

	/**
	 * @return Default value for {@link ExchangeApi} websocket hook factory class
	 * @see ExchangeApiDescriptor#getWebsocketHookFactory()
	 */
	public String getWebsocketHookFactory() {
		return websocketHookFactory;
	}

	/**
	 * @param websocketHookFactory Default value for {@link ExchangeApi} websocket
	 *                             hook factory class
	 * @see ExchangeApiDescriptor#getWebsocketHookFactory()
	 */
	public void setWebsocketHookFactory(String websocketHookFactory) {
		this.websocketHookFactory = websocketHookFactory;
	}
	
	public String getWebsocketUrl() {
		return websocketUrl;
	}

	public void setWebsocketUrl(String websocketUrl) {
		this.websocketUrl = websocketUrl;
	}
	
	/**
	 * @return a string representation of the exchange descriptor
	 * @see EncodingUtil#pojoToString(Object)
	 */
	@Override
	public String toString() {
		return EncodingUtil.pojoToString(this);
	}
}
