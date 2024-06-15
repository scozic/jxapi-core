package com.scz.jxapi.exchange;

import java.lang.reflect.InvocationTargetException;
import java.net.http.HttpClient;
import java.util.Properties;

import com.scz.jxapi.netutils.deserialization.MessageDeserializer;
import com.scz.jxapi.netutils.rest.FutureRestResponse;
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
import com.scz.jxapi.netutils.websocket.WebsocketHookFactory;
import com.scz.jxapi.netutils.websocket.WebsocketManager;

public abstract class AbstractExchangeApi implements ExchangeApi {
	
	private final Properties properties;
	private final RequestThrottler requestThrottler;
	protected HttpRequestExecutor httpRequestExecutor = null;
	protected HttpRequestInterceptor httpRequestInterceptor = null;
	protected WebsocketManager websocketManager = null;
	  
	public AbstractExchangeApi(Properties properties) {
		this(properties, null);
	}  

	public AbstractExchangeApi(Properties properties, RequestThrottler requestThrottler) {
		this.properties = properties;
		this.requestThrottler = requestThrottler;
	}

	@Override
	public Properties getProperties() {
		return properties;
	}
	
	protected HttpRequestInterceptor createHttpRequestInterceptor(String factoryClassName) {
		try {
			return ((HttpRequestInterceptorFactory) Class.forName(factoryClassName).getConstructor().newInstance()).createInterceptor(this);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException | ClassNotFoundException e) {
			throw new IllegalArgumentException("Failed to instantiate " 
												+ HttpRequestInterceptorFactory.class.getName() + 
												" implementation '" + factoryClassName + "'.",
												e);
		}
	}
	
	protected HttpRequestExecutor createHttpRequestExecutor(String factoryClassName) {
		if  (factoryClassName != null) {
			try {
				return ((HttpRequestExecutorFactory) Class.forName(factoryClassName).getConstructor().newInstance()).createExecutor(this);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException | ClassNotFoundException e) {
				throw new IllegalArgumentException("Failed to instantiate " 
													+ HttpRequestInterceptorFactory.class.getName() + 
													" implementation '" + factoryClassName + "'.",
													e);
			}
		}
		return new JavaNetHttpRequestExecutor(HttpClient.newHttpClient());
	}
	
	protected <A> FutureRestResponse<A> submit(HttpRequest request, MessageDeserializer<A> deserializer) {
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
			callback.complete(response);
		});
		return callback;
	}

	public HttpRequestExecutor getHttpRequestExecutor() {
		return httpRequestExecutor;
	}

	public void setHttpRequestExecutor(HttpRequestExecutor httpRequestExecutor) {
		this.httpRequestExecutor = httpRequestExecutor;
	}
	
	protected WebsocketManager createWebsocketManager(String url, String websocketFactoryClassName, String websocketHookFactoryClassName) {
		WebsocketFactory websocketFactory = websocketFactoryClassName == null? new DefaultWebsocketFactory(): WebsocketFactory.fromClassName(websocketFactoryClassName);
		Websocket websocket = websocketFactory.createWebsocket(this);
		if (url != null) {
			websocket.setUrl(url);
		}
		DefaultWebsocketManager wsManager = new DefaultWebsocketManager(websocket);
		if (websocketHookFactoryClassName != null) {
			wsManager.setWebsocketHook(WebsocketHookFactory.fromClassName(websocketHookFactoryClassName).createWebsocketHook(this));
		}
		return wsManager;
	}
	
	protected <M> WebsocketEndpoint<M> createWebsocketEndpoint(MessageDeserializer<M> messageDeserializer) {
		return new DefaultWebsocketEndpoint<>(websocketManager, messageDeserializer);
	}

}
