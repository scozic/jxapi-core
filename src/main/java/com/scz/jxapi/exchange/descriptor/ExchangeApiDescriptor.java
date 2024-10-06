package com.scz.jxapi.exchange.descriptor;

import java.util.List;

import com.scz.jxapi.netutils.rest.HttpRequestExecutorFactory;
import com.scz.jxapi.netutils.rest.HttpRequestInterceptorFactory;
import com.scz.jxapi.netutils.rest.javanet.JavaNetHttpRequestExecutor;
import com.scz.jxapi.netutils.rest.ratelimits.RateLimitRule;
import com.scz.jxapi.netutils.websocket.DefaultWebsocketFactory;
import com.scz.jxapi.netutils.websocket.WebsocketFactory;
import com.scz.jxapi.netutils.websocket.WebsocketHook;
import com.scz.jxapi.netutils.websocket.WebsocketHookFactory;
import com.scz.jxapi.util.EncodingUtil;

/**
 * Part of a JSON document descriptor that describes a group of REST and or
 * Websocket endpoints. Child element of ExchangeDescriptor. <br>
 * 
 * <h3>REST endpoints</h3>
 * <ul>
 * <li>There can be multiple REST endpoints, or no such endpoint, in which case <code>restEndpoints</code> property can be <code>null</code></li>
 * <li>Each REST endpoint is described as a {@link RestEndpointDescriptor}</li>
 * <li>Each REST endpoint share the same HttpRequestExecutor and HttpRequestInterceptor, that are created from factories classes provided 
 * in <code>httpRequestExecutorFactory</code> and <code>httpRequestInterceptorFactory</code> properties </li>
 * <li><code>httpRequestExecutorFactory</code> property may be supplied to specify a factory class that creates HttpRequestExecutor instances, see {@link HttpRequestExecutorFactory}. When property is not set, default {@link JavaNetHttpRequestExecutor} is used</li>
 * <li><code>httpRequestInterceptorFactory</code> property may be supplied to specify a factory class that creates HttpRequestInterceptor instances, see {@link HttpRequestInterceptorFactory}. When property is not set, no request interceptor is used</li>
 * <li>API global Rate limits can be specified for the REST endpoints</li> in <code>rateLimits</code> property. Those limits are shared among all defined REST endpoints.
 * <li>Rate limits from enclosing exchange descriptor are inherited by the API descriptor. Exchange global limit are shared among all REST endpoints of every API specified in exchange</li>
 * </ul>
 * <h3>Websocket endpoints</h3>
 * <ul>
 * <li>There can be multiple Websocket endpoints, or no such endpoint, in which case <code>websocketEndpoints</code> property can be <code>null</code></li>
 * <li>Each Websocket endpoint is described as a {@link WebsocketEndpointDescriptor}</li>
 * <li>Each Websocket endpoint share the same WebsocketFactory and WebsocketHook, that are created from factories classes provided in <code>websocketFactory</code> and <code>websocketHookFactory</code> properties</li>
 * <li><code>websocketFactory</code> property may be supplied to specify a factory class that creates Websocket instances, see {@link WebsocketFactory}. When property is not set, default {@link DefaultWebsocketFactory} is used</li>
 * <li><code>websocketHookFactory</code> property may be supplied to specify a factory class that creates WebsocketHook instances, see {@link WebsocketHookFactory}. When property is not set, no websocket hook is used. Such hook is needed though to implement specific handshake, heartbeat management and socket multiplexing (e.g. subscribing to multiple topics using same socket)</li>
 * <li><code>websocketUrl</code> property may be supplied to specify the URL of the websocket endpoint. When property is not set, the URL is expected to be set by WebsocketHook on WebsocketManager during {@link WebsocketHook#init(com.scz.jxapi.netutils.websocket.WebsocketManager)} or {@link WebsocketHook#beforeConnect(com.scz.jxapi.netutils.websocket.WebsocketManager)}</li>
 * </ul>
 * 
 * <h3>Example of corresponding JSON, with sample REST endpoint and WebsocketEndpoint</h3>
 * <pre>
 * { 
 * 	"name": "MarketData",
 * 	"description": "The market data API of MyTestExchange",
 * 	"httpRequestInterceptorFactory": "com.foo.bar.BarHttpRequestInterceptorFactory",
 * 	"rateLimits": [
 *		{"id": "customRule", "timeFrame": 1500,  "maxTotalWeight": 300}
 *	],
 * 	"restEndpoints": [
 * 		{
 * 			"name": "exchangeInfo",
 * 			"httpMethod": "GET",
 * 			"description": "Fetch market information of symbols that can be traded",
 * 			"url": "https://com.sample.mycex/exchangeInfo",
 * 			"request":{
 * 				"parameters": [
 * 					{"name":"symbols", "type": "STRING_LIST", "description":"The list of symbol to fetch market information for. Leave empty to fetch all markets", "sampleValue":"[\"BTC\", \"ETH\"]"}
 * 				]
 * 			},
 * 			"response":{ 
 * 				"parameters": [
 * 					{"name":"responseCode", "type": "INT", "description":"Request response code", "sampleValue":"0"},
 * 					{"name":"payload", "type": "OBJECT_LIST", "description":"List of market information for each requested symbol", "parameters":[
 * 							{"name":"symbol", "type": "STRING", "description":"Market symbol", "sampleValue":"BTC_USDT"},
 * 							{"name":"minOrderSize", "type": "BIGDECIMAL", "description":"Minimum order amount", "sampleValue":"0.0001"},
 * 							{"name":"levels", "type": "INT_LIST", "description":"Amount precision levels", "sampleValue":[1,10,500]}
 * 						]
 * 					}
 * 				]
 * 			}
 * 		},
 * 		{
 * 			"name": "tickers",
 * 			"httpMethod": "GET",
 * 			"description": "Fetch current tickers",
 * 			"url": "https://com.sample.mycex/tickers",
 * 			"request":{
 * 				"parameters": []
 * 			},
 * 			"response": { 
 * 				"parameters": [
 * 					{"name":"responseCode", "type": "INT", "description":"Request response code", "sampleValue":"0"},
 * 					{"name":"payload", "type": "OBJECT_MAP", "description":"Tickers for each symbol", "parameters":[
 * 							{"name":"last", "type": "BIGDECIMAL", "description":"Last traded price", "sampleValue":10.0}
 * 						]
 * 					}
 * 				]
 * 			}
 * 		}
 * 	],
 * 	"websocketUrl": "wss://com.foo.exchange/ws",
 * 	"websocketFactory": "com.foo.bar.BarWebsocketFactory",
 * 	"websocketHookFactory": "com.foo.bar.BarWebsocketHookFactory",
 * 	"websocketEndpoints": [
 * 		{
 * 			"name": "tickerStream",
 * 			"topic": "${symbol}@ticker",
 * 			"description": "Subscribe to ticker stream",
 * 			"request": {
 * 				"parameters": [
 * 					{"name": "symbol", "type":"STRING", "description":"Symbol to subscribe to ticker stream of", "sampleValue":"BTC_USDT"}
 * 				]
 * 			},
 * 			"topicParametersListSeparator": "|",
 * 			"messageTopicMatcherFields": [
 * 				{"name": "topic",  "value": "ticker"},
 * 				{"name": "symbol",  "value": "${symbol}"}
 * 			],
 * 			"message": { 
 * 				"parameters": [
 * 					{"name":"topic", "msgField":"t", "type": "STRING", "description":"Topic", "sampleValue":"ticker"},
 * 					{"name":"symbol", "msgField":"s", "type": "STRING", "description":"Symbol name", "sampleValue":"BTC_USDT"},
 * 					{"name":"last", "msgField":"p", "type": "BIGDECIMAL", "description":"Last traded price", "sampleValue":"16000.00"}
 * 				]
 * 			}
 * 		}
 * 	]
 * }
 * </pre>
 * 
 * @see ExchangeDescriptor
 * @see RestEndpointDescriptor
 * @see WebsocketEndpointDescriptor
 * @see RateLimitRule
 * @see HttpRequestInterceptorFactory
 * @see HttpRequestExecutorFactory
 * @see WebsocketFactory
 * @see WebsocketHookFactory
 */
public class ExchangeApiDescriptor {
	
	private String name;
	
	private String description;
	
	private List<RestEndpointDescriptor> restEndpoints;
	
	private String httpRequestInterceptorFactory;
	
	private String httpRequestExecutorFactory;
	
	private String websocketFactory;
	
	private String websocketHookFactory;
	
	private String websocketUrl;
	
	private List<WebsocketEndpointDescriptor> websocketEndpoints;
	
	private List<RateLimitRule> rateLimits;

	/**
	 * Retrieves the list of REST endpoints for this API.
	 * @return The list of REST endpoints for this API, may be <code>null</code>.
	 */
	public List<RestEndpointDescriptor> getRestEndpoints() {
		return restEndpoints;
	}

	/**
	 * Sets the list of REST endpoints for this API.
	 * @param restEndpoints The list of REST endpoints for this API.
	 */
	public void setRestEndpoints(List<RestEndpointDescriptor> restEndpoints) {
		this.restEndpoints = restEndpoints;
	}

	/**
	 * @return Unique name of the API among APIs of parent ExchangeDescriptor.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name Unique name of the API among APIs of parent ExchangeDescriptor.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Description of the API.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description Description of the API.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Retrieves the list of Websocket endpoints for this API.
	 * 
	 * @return The list of Websocket endpoints for this API, may be
	 *         <code>null</code>.
	 */
	public List<WebsocketEndpointDescriptor> getWebsocketEndpoints() {
		return websocketEndpoints;
	}

	/**
	 * Sets the list of Websocket endpoints for this API.
	 * 
	 * @param websocketEndpoints The list of Websocket endpoints for this API.
	 */
	public void setWebsocketEndpoints(List<WebsocketEndpointDescriptor> websocketEndpoints) {
		this.websocketEndpoints = websocketEndpoints;
	}

	/**
	 * Retrieves the list of rate limits for this API. These limits are shared among
	 * all REST endpoints of this API.
	 * 
	 * @return The list of rate limits for this API, may be <code>null</code>.
	 */
	public List<RateLimitRule> getRateLimits() {
		return rateLimits;
	}

	/**
	 * Sets the list of rate limits for this API. These limits are shared among all
	 * REST endpoints of this API.
	 * 
	 * @param rateLimits The list of rate limits for this API.
	 */
	public void setRateLimits(List<RateLimitRule> rateLimits) {
		this.rateLimits = rateLimits;
	}

	/**
	 * Retrieves the factory class that creates HttpRequestInterceptor instances for
	 * this API.
	 * 
	 * @return The factory class that creates HttpRequestInterceptor instances for
	 *         this API, may be <code>null</code>, in which case no request
	 *         interceptor is used.
	 */
	public String getHttpRequestInterceptorFactory() {
		return httpRequestInterceptorFactory;
	}

	/**
	 * Sets the factory class that creates HttpRequestInterceptor instances for this
	 * API.
	 * 
	 * @param httpRequestInterceptorFactory The factory class that creates
	 *                                      HttpRequestInterceptor instances for
	 *                                      this API, may be <code>null</code>, in
	 *                                      which case no request interceptor is
	 *                                      used.
	 */
	public void setHttpRequestInterceptorFactory(String httpRequestInterceptorFactory) {
		this.httpRequestInterceptorFactory = httpRequestInterceptorFactory;
	}

	/**
	 * Retrieves the factory class that creates HttpRequestExecutor instances for
	 * this API.
	 * 
	 * @return The factory class that creates HttpRequestExecutor instances for this
	 *         API, may be <code>null</code>, in which case default
	 *         {@link JavaNetHttpRequestExecutor} is used.
	 */
	public String getHttpRequestExecutorFactory() {
		return httpRequestExecutorFactory;
	}

	/**
	 * Sets the factory class that creates HttpRequestExecutor instances for this
	 * API.
	 * 
	 * @param httpRequestExecutorFactory The factory class that creates
	 *                                   HttpRequestExecutor instances for this API,
	 *                                   may be <code>null</code>, in which case
	 *                                   default {@link JavaNetHttpRequestExecutor}
	 *                                   is used.
	 */
	public void setHttpRequestExecutorFactory(String httpRequestExecutorFactory) {
		this.httpRequestExecutorFactory = httpRequestExecutorFactory;
	}

	/**
	 * Retrieves the factory class that creates WebsocketFactory instances for this
	 * API.
	 * 
	 * @return The factory class that creates WebsocketFactory instances for this
	 *         API, may be <code>null</code>, in which case default
	 *         {@link DefaultWebsocketFactory} is used.
	 */
	public String getWebsocketHookFactory() {
		return websocketHookFactory;
	}

	/**
	 * Sets the factory class that creates WebsocketFactory instances for this API.
	 * 
	 * @param websocketHookFactory The factory class that creates WebsocketFactory
	 *                             instances for this API, may be <code>null</code>,
	 *                             in which case default
	 *                             {@link DefaultWebsocketFactory} is used.
	 */
	public void setWebsocketHookFactory(String websocketHookFactory) {
		this.websocketHookFactory = websocketHookFactory;
	}

	/**
	 * Retrieves the factory class that creates WebsocketHook instances for this
	 * API.
	 * 
	 * @return The factory class that creates WebsocketHook instances for this API,
	 *         may be <code>null</code>, in which case no websocket hook is used.
	 */
	public String getWebsocketFactory() {
		return websocketFactory;
	}

	/**
	 * Sets the factory class that creates WebsocketHook instances for this API.
	 * 
	 * @param websocketFactory The factory class that creates WebsocketHook
	 *                         instances for this API, may be <code>null</code>, in
	 *                         which case no websocket hook is used.
	 */
	public void setWebsocketFactory(String websocketFactory) {
		this.websocketFactory = websocketFactory;
	}

	/**
	 * Retrieves the URL of the websocket endpoint for this API.
	 * 
	 * @return The URL of the websocket endpoint for this API, may be
	 *         <code>null</code>, in which case the URL is expected to be set by
	 *         WebsocketHook on WebsocketManager during
	 *         {@link WebsocketHook#init(com.scz.jxapi.netutils.websocket.WebsocketManager)}
	 *         or
	 *         {@link WebsocketHook#beforeConnect(com.scz.jxapi.netutils.websocket.WebsocketManager)}.
	 */
	public String getWebsocketUrl() {
		return websocketUrl;
	}

	/**
	 * Sets the URL of the websocket endpoint for this API.
	 * 
	 * @param websocketUrl The URL of the websocket endpoint for this API, may be
	 *                     <code>null</code>, in which case the URL is expected to
	 *                     be set by WebsocketHook on WebsocketManager during
	 *                     {@link WebsocketHook#init(com.scz.jxapi.netutils.websocket.WebsocketManager)}
	 *                     or
	 *                     {@link WebsocketHook#beforeConnect(com.scz.jxapi.netutils.websocket.WebsocketManager)}.
	 */
	public void setWebsocketUrl(String websocketUrl) {
		this.websocketUrl = websocketUrl;
	}

	@Override
	public String toString() {
		return EncodingUtil.pojoToString(this);
	}

}
