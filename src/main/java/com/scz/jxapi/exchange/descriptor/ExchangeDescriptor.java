package com.scz.jxapi.exchange.descriptor;

import java.util.List;

import com.scz.jxapi.exchange.Exchange;
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
 * <li>properties: list of properties that can be configured for the exchange
 * wrapper, for instance API keys, secret keys, etc. See {@link ConfigProperty}</li>
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
	private String DocUrl;
	private String basePackage;
	
	private List<ExchangeApiDescriptor> apis;
	
	private List<RateLimitRule> rateLimits;
	
	private List<Constant> constants;
	
	private List<ConfigProperty> properties;

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
		return DocUrl;
	}

	/**
	 * @param excgangeUrl Exchange's website API documentation home page
	 */
	public void setDocUrl(String excgangeUrl) {
		this.DocUrl = excgangeUrl;
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
	 * List of constants that are used in context of the exchange wrapper, for
	 * instance specific values for some APIs request/response/message properties.
	 * 
	 * @see Constant
	 * @return
	 */
	public List<Constant> getConstants() {
		return constants;
	}

	/**
	 * List of constants that are used in context of the exchange wrapper, for
	 * instance specific values for some APIs request/response/message properties.
	 * 
	 * @see Constant
	 * @param constants
	 */
	public void setConstants(List<Constant> constants) {
		this.constants = constants;
	}

	/**
	 * List of properties that can be configured for the exchange wrapper, for
	 * instance API keys, secret keys, etc.
	 * 
	 * @see Properties
	 * @return
	 */
	public List<ConfigProperty> getProperties() {
		return properties;
	}

	/**
	 * List of properties that can be configured for the exchange wrapper, for
	 * instance API keys, secret keys, etc.
	 * 
	 * @see Properties
	 * @param properties
	 */
	public void setProperties(List<ConfigProperty> properties) {
		this.properties = properties;
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
