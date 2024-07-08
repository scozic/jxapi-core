package com.scz.jxapi.exchange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.scz.jxapi.netutils.deserialization.MessageDeserializer;
import com.scz.jxapi.netutils.deserialization.RawStringMessageDeserializer;
import com.scz.jxapi.netutils.rest.FutureRestResponse;
import com.scz.jxapi.netutils.rest.HttpMethod;
import com.scz.jxapi.netutils.rest.HttpRequest;
import com.scz.jxapi.netutils.rest.HttpRequestExecutor;
import com.scz.jxapi.netutils.rest.HttpRequestInterceptor;
import com.scz.jxapi.netutils.rest.HttpResponse;
import com.scz.jxapi.netutils.rest.RestResponse;
import com.scz.jxapi.netutils.rest.javanet.JavaNetHttpRequestExecutor;
import com.scz.jxapi.netutils.rest.mock.MockFutureHttpResponse;
import com.scz.jxapi.netutils.rest.mock.MockHttpRequestExecutor;
import com.scz.jxapi.netutils.rest.mock.MockHttpRequestExecutorFactory;
import com.scz.jxapi.netutils.rest.mock.MockHttpRequestInterceptor;
import com.scz.jxapi.netutils.rest.mock.MockHttpRequestInterceptorFactory;
import com.scz.jxapi.netutils.rest.ratelimits.RequestThrottler;
import com.scz.jxapi.netutils.websocket.DefaultWebsocketEndpoint;
import com.scz.jxapi.netutils.websocket.DefaultWebsocketManager;
import com.scz.jxapi.netutils.websocket.DefaultWebsocketMessageTopicMatcher;
import com.scz.jxapi.netutils.websocket.WebsocketManager;
import com.scz.jxapi.netutils.websocket.WebsocketSubscribeRequest;
import com.scz.jxapi.netutils.websocket.mock.MockWebsocket;
import com.scz.jxapi.netutils.websocket.mock.MockWebsocketFactory;
import com.scz.jxapi.netutils.websocket.mock.MockWebsocketHook;
import com.scz.jxapi.netutils.websocket.mock.MockWebsocketHookFactory;
import com.scz.jxapi.netutils.websocket.mock.MockWebsocketListener;
import com.scz.jxapi.netutils.websocket.spring.SpringWebsocket;
import com.scz.jxapi.observability.ExchangeApiEvent;
import com.scz.jxapi.observability.ExchangeApiEventType;
import com.scz.jxapi.observability.ExchangeApiObserver;
import com.scz.jxapi.observability.MockExchangeApiObserver;
import com.scz.jxapi.observability.Observable;

/**
 * Unit test for {@link AbstractExchangeApi}
 */
public class AbstractExchangeApiTest {

	private TestExchangeApi exchangeApi;

	@Before
	public void setUp() {
		// Initialize the exchangeApi object with test data
		exchangeApi = new TestExchangeApi("TestApi", "test-MyExchange", "MyExchange", new Properties());
	}

	@Test
	public void testGetExchangeName() {
		assertEquals("test-MyExchange", exchangeApi.getExchangeName());
	}

	@Test
	public void testGetName() {
		assertEquals("TestApi", exchangeApi.getName());
	}

	@Test
	public void testGetExchangeId() {
		assertEquals("MyExchange", exchangeApi.getExchangeId());
	}

	@Test
	public void testGetProperties() {
		assertEquals(new Properties(), exchangeApi.getProperties());
	}

	@Test
	public void testSetHttpRequestExecutor() {
		MockHttpRequestExecutor executor = new MockHttpRequestExecutor();
		exchangeApi.setHttpRequestExecutor(executor);
		assertEquals(executor, exchangeApi.getHttpRequestExecutor());
	}

	@Test
	public void testSubscribeObserver() {
		MockExchangeApiObserver observer = new MockExchangeApiObserver();
		exchangeApi.subscribeObserver(observer);
		assertTrue(exchangeApi.getObservable().hasListener(observer));
	}

	@Test
	public void testUnsubscribeObserver() {
		ExchangeApiObserver observer = new MockExchangeApiObserver();
		exchangeApi.subscribeObserver(observer);
		exchangeApi.unsubscribeObserver(observer);
		assertFalse(exchangeApi.getObservable().hasListener(observer));
	}

	@Test(expected = IllegalStateException.class)
	public void testSubmitWithoutHttpRequestExecutor() {
		HttpRequest request = new HttpRequest();
		MessageDeserializer<String> deserializer = new RawStringMessageDeserializer();
		exchangeApi.submit(request, deserializer);
	}

	@Test
	public void testSubmitAndExecuteHttpRequestOk() throws Exception {
		MockExchangeApiObserver observer = new MockExchangeApiObserver();
		exchangeApi.subscribeObserver(observer);
		MockHttpRequestExecutor executor = createMockHttpRequestExecutor();
		HttpRequest request = createDummyRequest();
		MessageDeserializer<String> deserializer = new RawStringMessageDeserializer();
		FutureRestResponse<String> response = exchangeApi.submit(request, deserializer);
		ExchangeApiEvent event = observer.pop();
		Assert.assertEquals(ExchangeApiEventType.HTTP_REQUEST, event.getType());
		Assert.assertEquals(request, event.getHttpRequest());
		MockFutureHttpResponse mockResponse = executor.popRequest();
		Assert.assertEquals(request, mockResponse.getRequest());
		mockResponse.complete(createDummyOkResponse(request));
		RestResponse<String> actualResponse = response.get(1, TimeUnit.MILLISECONDS);
		Assert.assertTrue(actualResponse.isOk());
		Assert.assertEquals("pong", actualResponse.getResponse());
		Assert.assertEquals(200, actualResponse.getHttpStatus());
		event = observer.pop();
		Assert.assertEquals(ExchangeApiEventType.HTTP_RESPONSE, event.getType());
		Assert.assertTrue(event.getHttpResponse().isOk());
	}
	
	@Test
	public void testSubmitAndExecuteHttpRequestServerKo() throws Exception {
		MockExchangeApiObserver observer = new MockExchangeApiObserver();
		exchangeApi.subscribeObserver(observer);
		MockHttpRequestExecutor executor = createMockHttpRequestExecutor();
		HttpRequest request = createDummyRequest();
		MessageDeserializer<String> deserializer = new RawStringMessageDeserializer();
		FutureRestResponse<String> response = exchangeApi.submit(request, deserializer);
		ExchangeApiEvent event = observer.pop();
		Assert.assertEquals(ExchangeApiEventType.HTTP_REQUEST, event.getType());
		Assert.assertEquals(request, event.getHttpRequest());
		MockFutureHttpResponse mockResponse = executor.popRequest();
		Assert.assertEquals(request, mockResponse.getRequest());
		mockResponse.complete(createDummyErrorResponse(request));
		RestResponse<String> actualResponse = response.get(1, TimeUnit.MILLISECONDS);
		Assert.assertFalse(actualResponse.isOk());
		Assert.assertEquals(null, actualResponse.getResponse());
		Assert.assertEquals(500, actualResponse.getHttpStatus());
		event = observer.pop();
		Assert.assertEquals(ExchangeApiEventType.HTTP_RESPONSE, event.getType());
		Assert.assertFalse(event.getHttpResponse().isOk());
	}
	
	@Test
	public void testSubmitAndExecuteHttpRequestDeserializationError() throws Exception {
		MockExchangeApiObserver observer = new MockExchangeApiObserver();
		exchangeApi.subscribeObserver(observer);
		MockHttpRequestExecutor executor = createMockHttpRequestExecutor();
		HttpRequest request = createDummyRequest();
		MessageDeserializer<String> deserializer = msg -> {throw new RuntimeException("Deserialization error");};
		FutureRestResponse<String> response = exchangeApi.submit(request, deserializer);
		ExchangeApiEvent event = observer.pop();
		Assert.assertEquals(ExchangeApiEventType.HTTP_REQUEST, event.getType());
		Assert.assertEquals(request, event.getHttpRequest());
		MockFutureHttpResponse mockResponse = executor.popRequest();
		Assert.assertEquals(request, mockResponse.getRequest());
		mockResponse.complete(createDummyOkResponse(request));
		RestResponse<String> actualResponse = response.get(1, TimeUnit.MILLISECONDS);
		Assert.assertFalse(actualResponse.isOk());
		Assert.assertEquals(null, actualResponse.getResponse());
		Assert.assertEquals(200, actualResponse.getHttpStatus());
		event = observer.pop();
		Assert.assertEquals(ExchangeApiEventType.HTTP_RESPONSE, event.getType());
		Assert.assertFalse(event.getHttpResponse().isOk());
	}
	
	@Test
	public void testSubmitAndExecuteHttpRequestWithDummyInterceptor() throws Exception {
		MockExchangeApiObserver observer = new MockExchangeApiObserver();
		exchangeApi.subscribeObserver(observer);
		createMockHttpRequestExecutor();
		exchangeApi.createHttpRequestInterceptor(MockHttpRequestInterceptorFactory.class.getName());
		AtomicReference<HttpRequest> interceptedRequest = new AtomicReference<>();
		((MockHttpRequestInterceptor) exchangeApi.getRequestInterceptor()).addPreparedInterceptor(r -> interceptedRequest.set(r));
		HttpRequest request = createDummyRequest();
		MessageDeserializer<String> deserializer = new RawStringMessageDeserializer();
		exchangeApi.submit(request, deserializer);
		Assert.assertEquals(request, interceptedRequest.get());	
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCreateHttpRequestInterceptorUnresolvedInterceptorFactory() throws Exception {
		MockExchangeApiObserver observer = new MockExchangeApiObserver();
		exchangeApi.subscribeObserver(observer);
		MockHttpRequestExecutor executor = new MockHttpRequestExecutor();
		exchangeApi.setHttpRequestExecutor(executor);
		exchangeApi.createHttpRequestInterceptor("");	
	}
	
	@Test
	public void testSubmitAndExecuteHttpRequestOkUsingRequestThrottler() throws Exception {
		exchangeApi = new TestExchangeApi("TestApi", "test-MyExchange", "MyExchange", new Properties(), new RequestThrottler("TeetApi"));
		MockExchangeApiObserver observer = new MockExchangeApiObserver();
		exchangeApi.subscribeObserver(observer);
		MockHttpRequestExecutor executor = createMockHttpRequestExecutor();
		HttpRequest request = createDummyRequest();
		MessageDeserializer<String> deserializer = new RawStringMessageDeserializer();
		FutureRestResponse<String> response = exchangeApi.submit(request, deserializer);
		ExchangeApiEvent event = observer.pop();
		Assert.assertEquals(ExchangeApiEventType.HTTP_REQUEST, event.getType());
		Assert.assertEquals(request, event.getHttpRequest());
		MockFutureHttpResponse mockResponse = executor.popRequest();
		Assert.assertEquals(request, mockResponse.getRequest());
		mockResponse.complete(createDummyOkResponse(request));
		RestResponse<String> actualResponse = response.get(1, TimeUnit.MILLISECONDS);
		Assert.assertTrue(actualResponse.isOk());
		Assert.assertEquals("pong", actualResponse.getResponse());
		Assert.assertEquals(200, actualResponse.getHttpStatus());
		event = observer.pop();
		Assert.assertEquals(ExchangeApiEventType.HTTP_RESPONSE, event.getType());
		Assert.assertTrue(event.getHttpResponse().isOk());
	}
	
	@Test
	public void testSubmitAndExecuteHttpRequestMissingRequestExecutorUsingRequestThrottler() throws Exception {
		exchangeApi = new TestExchangeApi("TestApi", "test-MyExchange", "MyExchange", new Properties(), new RequestThrottler("TeetApi"));
		MockExchangeApiObserver observer = new MockExchangeApiObserver();
		exchangeApi.subscribeObserver(observer);
		HttpRequest request = createDummyRequest();
		MessageDeserializer<String> deserializer = new RawStringMessageDeserializer();
		FutureRestResponse<String> response = exchangeApi.submit(request, deserializer);
		RestResponse<String> actualResponse = response.get(1, TimeUnit.MILLISECONDS);
		Assert.assertFalse(actualResponse.isOk());
		Assert.assertEquals("No HttpRequestExecutor set", actualResponse.getException().getMessage());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCreateHttpRequestExecutorInvalidFactoryClass() {
		exchangeApi.createHttpRequestExecutor("");
	}
	
	@Test
	public void testCreateDefaultHttpRequestExecutor() {
		Assert.assertTrue(exchangeApi.createHttpRequestExecutor(null) instanceof JavaNetHttpRequestExecutor);
	}
	
	@Test
	public void testCreateWebsocketanagerWithDefaultWebsocketFactoryAndWithoutHookOk() {
		DefaultWebsocketManager websocketManager= (DefaultWebsocketManager) exchangeApi
				.createWebsocketManager(null, null, null);
		Assert.assertNotNull(websocketManager);
		Assert.assertTrue(websocketManager.getWebsocket() instanceof SpringWebsocket);
	}
	
	@Test
	public void testCreateWebsocketanagerWithoutHookOk() {
		DefaultWebsocketManager websocketManager= (DefaultWebsocketManager) exchangeApi
				.createWebsocketManager("wss://myexchange.com/ws", MockWebsocketFactory.class.getName(), null);
		Assert.assertNotNull(websocketManager);
		Assert.assertEquals("wss://myexchange.com/ws", websocketManager.getUrl());
		Assert.assertTrue(websocketManager.getWebsocket() instanceof MockWebsocket);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCreateWebsocketanagerWithoutHookInvalidWebsocketFactoryClass() {
		exchangeApi.createWebsocketManager("wss://myexchange.com/ws", "", null);
	}
	
	@Test
	public void testCreateWebsocketanagerWithHookOk() {
		DefaultWebsocketManager websocketManager= (DefaultWebsocketManager) exchangeApi
				.createWebsocketManager("wss://myexchange.com/ws", MockWebsocketFactory.class.getName(), MockWebsocketHookFactory.class.getName());
		Assert.assertNotNull(websocketManager);
		Assert.assertTrue(websocketManager.getWebsocket() instanceof MockWebsocket);
		Assert.assertTrue(websocketManager.getWebsocketHook() instanceof MockWebsocketHook);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCreateWebsocketanagerWithHooInvalidWebsocketHookFactoryClass() {
		DefaultWebsocketManager websocketManager= (DefaultWebsocketManager) exchangeApi
				.createWebsocketManager("wss://myexchange.com/ws", MockWebsocketFactory.class.getName(), "");
		Assert.assertNotNull(websocketManager);
		Assert.assertTrue(websocketManager.getWebsocket() instanceof MockWebsocket);
		Assert.assertTrue(websocketManager.getWebsocketHook() instanceof MockWebsocketHook);
	}
	
	@Test(expected = IllegalStateException.class)
	public void testCreateWebsocketEndpointNullWebsocketManager() {
		DefaultWebsocketEndpoint<String> wsEndpoint = (DefaultWebsocketEndpoint<String>) exchangeApi.createWebsocketEndpoint("myWsApi", RawStringMessageDeserializer.INSTANCE);
		Assert.assertNotNull(wsEndpoint);
	}
	
	@Test
	public void testCreateWebsocketEndpointOk() { 
		exchangeApi.createWebsocketManager("wss://myexchange.com/ws", MockWebsocketFactory.class.getName(), null);
		DefaultWebsocketEndpoint<String> wsEndpoint = (DefaultWebsocketEndpoint<String>) exchangeApi.createWebsocketEndpoint("myWsApi", RawStringMessageDeserializer.INSTANCE);
		Assert.assertNotNull(wsEndpoint);
	}
	
	@Test
	public void testDispatchWsApiEvents() { 
		MockExchangeApiObserver observer = new MockExchangeApiObserver();
		exchangeApi.subscribeObserver(observer);
		exchangeApi.createWebsocketManager("wss://myexchange.com/ws", MockWebsocketFactory.class.getName(), null);
		DefaultWebsocketEndpoint<String> wsEndpoint = (DefaultWebsocketEndpoint<String>) exchangeApi.createWebsocketEndpoint("myWsApi", RawStringMessageDeserializer.INSTANCE);
		Assert.assertNotNull(wsEndpoint);
		wsEndpoint.subscribe(WebsocketSubscribeRequest.create(null, "mytopic", DefaultWebsocketMessageTopicMatcher.create()), new MockWebsocketListener<>());
		ExchangeApiEvent event = observer.pop();
		Assert.assertEquals(ExchangeApiEventType.WEBSOCKET_SUBSCRIBE, event.getType());
	}
	
	private HttpRequest createDummyRequest() {
		HttpRequest request = new HttpRequest();
		String reqData = "ping";
		request.setBody(reqData);
		request.setRequest(reqData);
		request.setHttpMethod(HttpMethod.POST);
		request.setHeader("Content-Type", "text/html");
		return request;
	}

	private HttpResponse createDummyOkResponse(HttpRequest request) {
		HttpResponse response = new HttpResponse();
		response.setResponseCode(200);
		response.setRequest(request);
		response.setBody("pong");
		response.setHeader("Content-Type", "text/html");
		return response;
	}
	
	private HttpResponse createDummyErrorResponse(HttpRequest request) {
		HttpResponse response = new HttpResponse();
		response.setResponseCode(500);
		// Remark: Setting a body to ko response, we want to check it is null in
		// returned response even if set because should not try to deserialize body when
		// http status is wrong.
		response.setBody("wrong");
		response.setRequest(request);
		return response;
	}
	
	private MockHttpRequestExecutor createMockHttpRequestExecutor() {
		return (MockHttpRequestExecutor) exchangeApi.createHttpRequestExecutor(MockHttpRequestExecutorFactory.class.getName());
	}

	private class TestExchangeApi extends AbstractExchangeApi {
		
		public TestExchangeApi(String apiName, String exchangeName, String exchangeId, Properties properties) {
			super(apiName, exchangeName, exchangeId, properties);
		}
		
		public TestExchangeApi(String apiName, String exchangeName, String exchangeId, Properties properties, RequestThrottler requestThrottler) {
			super(apiName, exchangeName, exchangeId, properties, requestThrottler);
		}
		
		public Observable<ExchangeApiObserver, ExchangeApiEvent> getObservable() {
			return observable;
		}
		
		public MockHttpRequestInterceptor getRequestInterceptor() {
			return (MockHttpRequestInterceptor) this.httpRequestInterceptor;
		}
		
		public HttpRequestInterceptor createHttpRequestInterceptor(String factoryClassName) {
			 return this.httpRequestInterceptor = super.createHttpRequestInterceptor(factoryClassName);
		}
		
		public HttpRequestExecutor createHttpRequestExecutor(String factoryClassName) {
			return this.httpRequestExecutor = super.createHttpRequestExecutor(factoryClassName);
		}
		
		protected WebsocketManager createWebsocketManager(String url, 
				  String websocketFactoryClassName, 
				  String websocketHookFactoryClassName) {
			return this.websocketManager = super.createWebsocketManager(url, websocketFactoryClassName, websocketHookFactoryClassName);
		}
	}
}
