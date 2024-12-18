
package com.scz.jxapi.exchange;

import java.net.http.HttpClient;
import java.util.Properties;

import com.scz.jxapi.netutils.deserialization.MessageDeserializer;
import com.scz.jxapi.netutils.rest.FutureRestResponse;
import com.scz.jxapi.netutils.rest.HttpMethod;
import com.scz.jxapi.netutils.rest.HttpRequest;
import com.scz.jxapi.netutils.rest.HttpRequestExecutor;
import com.scz.jxapi.netutils.rest.HttpRequestExecutorFactory;
import com.scz.jxapi.netutils.rest.HttpRequestInterceptor;
import com.scz.jxapi.netutils.rest.HttpRequestInterceptorFactory;
import com.scz.jxapi.netutils.rest.RestResponse;
import com.scz.jxapi.netutils.rest.javanet.JavaNetHttpRequestExecutor;
import com.scz.jxapi.netutils.rest.ratelimits.RequestThrottler;
import com.scz.jxapi.netutils.websocket.DefaultWebsocketEndpoint;
import com.scz.jxapi.netutils.websocket.DefaultWebsocketFactory;
import com.scz.jxapi.netutils.websocket.DefaultWebsocketManager;
import com.scz.jxapi.netutils.websocket.Websocket;
import com.scz.jxapi.netutils.websocket.WebsocketEndpoint;
import com.scz.jxapi.netutils.websocket.WebsocketFactory;
import com.scz.jxapi.netutils.websocket.WebsocketHook;
import com.scz.jxapi.netutils.websocket.WebsocketHookFactory;
import com.scz.jxapi.netutils.websocket.WebsocketManager;
import com.scz.jxapi.observability.Observable;
import com.scz.jxapi.observability.SynchronizedObservable;
import com.scz.jxapi.util.DefaultDisposable;
import com.scz.jxapi.util.FactoryUtil;
import com.scz.jxapi.util.JsonUtil;

/**
 * The AbstractExchangeApi class is an abstract base class that provides common
 * functionality and structure for exchange APIs.
 * It implements the ExchangeApi interface and provides default implementations
 * for some of its methods.
 * It is designed to be used as a base class by ExchangeApiGenerator to generate
 * concrete exchange API classes.
 * <br>
 * This class contains properties and methods that are shared by all exchange
 * APIs, such as the exchange name, exchange ID,
 * properties, request throttler, HTTP request executor, and observable for
 * handling exchange API events.
 * <br>
 * Subclasses of AbstractExchangeApi are expected to provide concrete
 * implementations for the remaining methods defined in the
 * ExchangeApi interface.
 * <br>
 * This class also provides helper methods for creating HTTP request
 * interceptors, HTTP request executors, and websocket managers.
 * 
 * @see ExchangeApi
 * @see HttpRequestInterceptor
 * @see HttpRequestExecutor
 * @see WebsocketManager
 * @see ExchangeApiObserver
 * @see ExchangeApiEvent
 * @see Observable
 */
public abstract class AbstractExchangeApi extends DefaultDisposable implements ExchangeApi {

	protected final String name;
	protected final String exchangeName;
	protected final String exchangeId;
	protected final Properties properties;
	protected final RequestThrottler requestThrottler;
	protected HttpRequestExecutor httpRequestExecutor = null;
	protected HttpRequestInterceptor httpRequestInterceptor = null;
	protected WebsocketManager websocketManager = null;
	protected final Observable<ExchangeApiObserver, ExchangeApiEvent> observable 
						= new SynchronizedObservable<>(ExchangeApiObserver::handleEvent);
	
	/**
	 * Creates a new AbstractExchangeApi instance with the specified API name,
	 * exchange name, exchange ID, and properties.
	 * 
	 * @param apiName      The name of the API.
	 * @param exchangeName The name of the exchange instance.
	 * @param exchangeId   The ID of the exchange.
	 * @param properties   The properties associated with the exchange instance.
	 */
	protected AbstractExchangeApi(String apiName, 
							   String exchangeName, 
							   String exchangeId, 
							   Properties properties) {
		this(apiName, exchangeName, exchangeId, properties, null);
	}  

	/**
	 * Creates a new AbstractExchangeApi instance with the specified API name,
	 * exchange name, exchange ID, properties, and request throttler.
	 * 
	 * @param apiName          The name of the API.
	 * @param exchangeName     The name of the exchange instance.
	 * @param exchangeId       The ID of the exchange.
	 * @param properties       The properties associated with the exchange instance.
	 * @param requestThrottler The request throttler to use for rate limiting.
	 */
	protected AbstractExchangeApi(String apiName, 
							   String exchangeName, 
							   String exchangeId, 
							   Properties properties, 
							   RequestThrottler requestThrottler) {
		this.name = apiName;
		this.exchangeName = exchangeName;
		this.exchangeId = exchangeId;
		this.properties = properties;
		this.requestThrottler = requestThrottler;
	}

	/**
	 * Returns the name of the exchange instance associated with this API.
	 * @return The name of the exchange instance.
	 */
	@Override
	public String getExchangeName() {
		return exchangeName;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public String getExchangeId() {
		return exchangeId;
	}
	
	@Override
	public Properties getProperties() {
		return properties;
	}
	
	/**
	 * @return the request executor used by this exchange API group.
	 */
	public HttpRequestExecutor getHttpRequestExecutor() {
		return httpRequestExecutor;
	}

	/**
	 * @param httpRequestExecutor the request executor used by this exchange API group.
	 */
	public void setHttpRequestExecutor(HttpRequestExecutor httpRequestExecutor) {
		this.httpRequestExecutor = httpRequestExecutor;
	}
	
	@Override
	public void subscribeObserver(ExchangeApiObserver exchangeApiObserver) {
		this.observable.subscribe(exchangeApiObserver);
	}

	@Override
	public boolean unsubscribeObserver(ExchangeApiObserver exchangeApiObserver) {
		return this.observable.unsubscribe(exchangeApiObserver);
	}
	
	/**
	 * Instantiates HTTP request interceptor using the specified factory class name.
	 * Should be called by subclasses to create the HTTP request interceptor if there is at least one REST API and a {@link HttpRequestInterceptorFactory} class name is specified.
	 * 
	 * @param factoryClassName The fully qualified class name of the factory class
	 *                         that creates the HTTP request interceptor.
	 */
	protected void createHttpRequestInterceptor(String factoryClassName) {
		httpRequestInterceptor = ((HttpRequestInterceptorFactory) FactoryUtil.fromClassName(factoryClassName)).createInterceptor(this);
	}
	
	/**
	 * Instantiates HTTP request executor using the specified factory class name, or
	 * the default executor factory which is usually sufficient. Should be called by
	 * subclasses to create the HTTP request executor if there is at least one REST
	 * API.
	 * 
	 * @param factoryClassName The fully qualified class name of the factory class
	 *                         that creates the HTTP request executor, or null if
	 *                         the default executor factory
	 *                         {@link JavaNetHttpRequestExecutor} should be used.
	 * @param requestTimeout   the request timeout used on executor, see
	 *                         {@link HttpRequestExecutor#setRequestTimeout(long)}.
	 *                         If value is &lt; 0, the default
	 *                         {@link HttpRequestExecutor#DEFAULT_REQUEST_TIMEOUT}
	 *                         is used.
	 */
	protected void createHttpRequestExecutor(String factoryClassName, long requestTimeout) {
		if  (factoryClassName != null) {
			httpRequestExecutor = ((HttpRequestExecutorFactory)  FactoryUtil.fromClassName(factoryClassName)).createExecutor(this);
		} else {
			httpRequestExecutor = new JavaNetHttpRequestExecutor(HttpClient.newHttpClient());
		}
		if (requestTimeout >= 0) {
			httpRequestExecutor.setRequestTimeout(requestTimeout);
		}
	}
	
	/**
	 * Submits a REST request asynchronously using the specified request and message deserializer to deserialize response.
	 * If a request throttler is set, the request is submitted through the throttler.
	 * <br>
	 * This method should used by subclasses to submit REST requests.
	 * @param <A>
	 * @param request
	 * @param deserializer
	 * @return
	 */
	protected <A> FutureRestResponse<A> submit(HttpRequest request, MessageDeserializer<A> deserializer) {
		if (request.getHttpMethod().requestHasBody && request.getRequest() != null) {
			request.setBody(JsonUtil.pojoToJsonString(request.getRequest()));
		}
		if (requestThrottler != null) {
			return requestThrottler.submit(request, r -> { 
				try {
					return submitNow(r, deserializer);
				} catch (Exception ex) {
					FutureRestResponse<A> callback = new FutureRestResponse<>();
					RestResponse<A> response = new RestResponse<>();
					response.setException(ex);
					callback.complete(response);
					return callback;
				}
			});
		} else {
			return submitNow(request, deserializer);
		}
	}

	private <A> FutureRestResponse<A> submitNow(HttpRequest request, MessageDeserializer<A> deserializer) {
		if (httpRequestExecutor == null) {
			throw new IllegalStateException("No " + HttpRequestExecutor.class.getSimpleName() + " set");
		}
		if (httpRequestInterceptor != null) {
			httpRequestInterceptor.intercept(request);
		}
		dispatchApiEvent(ExchangeApiEvent.createHttpRequestEvent(request));
		FutureRestResponse<A> callback = new FutureRestResponse<>();
		httpRequestExecutor.execute(request).thenAccept(httpResponse -> {
			RestResponse<A> response = new RestResponse<>(httpResponse);
			response.setHttpStatus(httpResponse.getResponseCode());
			if(response.isOk()) {
				try {
					response.setResponse(deserializer.deserialize(httpResponse.getBody()));
				} catch (Exception ex) {
					response.setException(ex);
				}
			}
			dispatchApiEvent(ExchangeApiEvent.createRestResponseEvent(response));
			callback.complete(response);
		});
		return callback;
	}
	
	/**
	 * Creates a websocket manager using the specified URL, websocket factory class name, and websocket hook factory class name.
	 * Should be called by subclasses to create the websocket manager if there is at least one websocket endpoint.
	 * 
	 * @param url The URL of the websocket server.
	 * @param websocketFactoryClassName The fully qualified class name of the websocket factory class that creates the websocket.
	 * @param websocketHookFactoryClassName The fully qualified class name of the websocket hook factory class that creates the websocket hook.
	 */
	protected void createWebsocketManager(String url, 
										  String websocketFactoryClassName, 
										  String websocketHookFactoryClassName) {
		WebsocketFactory websocketFactory = websocketFactoryClassName == null? 
												new DefaultWebsocketFactory(): 
												WebsocketFactory.fromClassName(websocketFactoryClassName);
		Websocket websocket = websocketFactory.createWebsocket(this);
		if (url != null) {
			websocket.setUrl(url);
		}
		WebsocketHook websocketHook = websocketHookFactoryClassName == null? 
										null: 
										WebsocketHookFactory.fromClassName(websocketHookFactoryClassName).createWebsocketHook(this);
		websocketManager = new DefaultWebsocketManager(this, websocket, websocketHook);
		websocketManager.subscribeErrorHandler(error -> dispatchApiEvent(ExchangeApiEvent.createWebsocketErrorEvent(error)));
	}
	
	/**
	 * Creates a websocket endpoint with the specified name and message deserializer.
	 * Should be called by subclasses to create websocket endpoints.
	 * 
	 * @param <M> The type of the message deserialized for this endpoint.
	 * @param endpointName The name of the websocket endpoint.
	 * @param messageDeserializer The message deserializer to use for deserializing messages received by the endpoint.
	 * @return The created websocket endpoint.
	 */
	protected <M> WebsocketEndpoint<M> createWebsocketEndpoint(String endpointName, 
															   MessageDeserializer<M> messageDeserializer) {
		if (websocketManager == null) {
			throw new IllegalStateException("Cannot create websocket endpoint as no websocket manager is set");
		}
		return new DefaultWebsocketEndpoint<>(endpointName, 
				   							  websocketManager, 
											  messageDeserializer,
											  this::dispatchApiEvent);
	}
	
	/**
	 * Dispatches the specified exchange API event to all observers.
	 * <br>
	 * Needs usually not be called by subclasses, as it is called for every call to
	 * {@link #submit(HttpRequest, MessageDeserializer)} and
	 * {@link #createWebsocketEndpoint(String, MessageDeserializer)}.
	 * 
	 * @param event The exchange API event to dispatch.
	 */
	protected void dispatchApiEvent(ExchangeApiEvent event) {
		event.setExchangeName(exchangeName);
		event.setExchangeApiName(name);
		event.setExchangeId(exchangeId);
		observable.dispatch(event);
	}
	
	/**
	 * When submitting a REST request call, outgoing HTT request may carry a body, this is usually the case for {@link HttpMethod#POST} method, see {@link HttpMethod#requestHasBody}.
	 * This method will serialize POJO or plain value type to body, by default as JSON 
	 * @param request Request data to serialize as HTTP request body
	 * @return The serialized request data to use as HTTP request body.
	 * @see JsonUtil#pojoToJsonString(Object)
	 */
	protected String serializeRequestBody(Object request) {
		return JsonUtil.pojoToJsonString(request);
	}
	
	@Override
	protected void doDispose() {
		if (requestThrottler != null) {
			requestThrottler.dispose();
		}
		if (httpRequestExecutor != null) {
			httpRequestExecutor.dispose();
		}
		if (websocketManager != null) {
			websocketManager.dispose();
		}
	}
}
