package com.scz.jxapi.generator.java.exchange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.scz.jxapi.exchange.AbstractExchangeApi;
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
import com.scz.jxapi.netutils.rest.ratelimits.RequestThrottler;
import com.scz.jxapi.observability.ExchangeApiEvent;
import com.scz.jxapi.observability.ExchangeApiEventType;
import com.scz.jxapi.observability.ExchangeApiObserver;
import com.scz.jxapi.observability.MockExchangeApiObserver;
import com.scz.jxapi.observability.Observable;
import com.scz.netutis.rest.MockFutureHttpResponse;
import com.scz.netutis.rest.MockHttpRequestExecutor;
import com.scz.netutis.rest.MockHttpRequestExecutorFactory;
import com.scz.netutis.rest.MockHttpRequestInterceptor;
import com.scz.netutis.rest.MockHttpRequestInterceptorFactory;

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
		exchangeApi.setHttpRequestExecutor(executor);
		HttpRequest request = createDummyRequest();
		MessageDeserializer<String> deserializer = new RawStringMessageDeserializer();
		FutureRestResponse<String> response = exchangeApi.submit(request, deserializer);
		ExchangeApiEvent event = observer.popEvent();
		Assert.assertEquals(ExchangeApiEventType.HTTP_REQUEST, event.getType());
		Assert.assertEquals(request, event.getHttpRequest());
		MockFutureHttpResponse mockResponse = executor.popRequest();
		Assert.assertEquals(request, mockResponse.getRequest());
		mockResponse.complete(createDummyOkResponse(request));
		RestResponse<String> actualResponse = response.get(1, TimeUnit.MILLISECONDS);
		Assert.assertTrue(actualResponse.isOk());
		Assert.assertEquals("pong", actualResponse.getResponse());
		Assert.assertEquals(200, actualResponse.getHttpStatus());
		event = observer.popEvent();
		Assert.assertEquals(ExchangeApiEventType.HTTP_RESPONSE, event.getType());
		Assert.assertTrue(event.getHttpResponse().isOk());
	}
	
	@Test
	public void testSubmitAndExecuteHttpRequestServerKo() throws Exception {
		MockExchangeApiObserver observer = new MockExchangeApiObserver();
		exchangeApi.subscribeObserver(observer);
		MockHttpRequestExecutor executor = createMockHttpRequestExecutor();
		exchangeApi.setHttpRequestExecutor(executor);
		HttpRequest request = createDummyRequest();
		MessageDeserializer<String> deserializer = new RawStringMessageDeserializer();
		FutureRestResponse<String> response = exchangeApi.submit(request, deserializer);
		ExchangeApiEvent event = observer.popEvent();
		Assert.assertEquals(ExchangeApiEventType.HTTP_REQUEST, event.getType());
		Assert.assertEquals(request, event.getHttpRequest());
		MockFutureHttpResponse mockResponse = executor.popRequest();
		Assert.assertEquals(request, mockResponse.getRequest());
		mockResponse.complete(createDummyErrorResponse(request));
		RestResponse<String> actualResponse = response.get(1, TimeUnit.MILLISECONDS);
		Assert.assertFalse(actualResponse.isOk());
		Assert.assertEquals(null, actualResponse.getResponse());
		Assert.assertEquals(500, actualResponse.getHttpStatus());
		event = observer.popEvent();
		Assert.assertEquals(ExchangeApiEventType.HTTP_RESPONSE, event.getType());
		Assert.assertFalse(event.getHttpResponse().isOk());
	}
	
	@Test
	public void testSubmitAndExecuteHttpRequestDeserializationError() throws Exception {
		MockExchangeApiObserver observer = new MockExchangeApiObserver();
		exchangeApi.subscribeObserver(observer);
		MockHttpRequestExecutor executor = createMockHttpRequestExecutor();
		exchangeApi.setHttpRequestExecutor(executor);
		HttpRequest request = createDummyRequest();
		MessageDeserializer<String> deserializer = msg -> {throw new RuntimeException("Deserialization error");};
		FutureRestResponse<String> response = exchangeApi.submit(request, deserializer);
		ExchangeApiEvent event = observer.popEvent();
		Assert.assertEquals(ExchangeApiEventType.HTTP_REQUEST, event.getType());
		Assert.assertEquals(request, event.getHttpRequest());
		MockFutureHttpResponse mockResponse = executor.popRequest();
		Assert.assertEquals(request, mockResponse.getRequest());
		mockResponse.complete(createDummyOkResponse(request));
		RestResponse<String> actualResponse = response.get(1, TimeUnit.MILLISECONDS);
		Assert.assertFalse(actualResponse.isOk());
		Assert.assertEquals(null, actualResponse.getResponse());
		Assert.assertEquals(200, actualResponse.getHttpStatus());
		event = observer.popEvent();
		Assert.assertEquals(ExchangeApiEventType.HTTP_RESPONSE, event.getType());
		Assert.assertFalse(event.getHttpResponse().isOk());
	}
	
	@Test
	public void testSubmitAndExecuteHttpRequestWithDummyInterceptor() throws Exception {
		MockExchangeApiObserver observer = new MockExchangeApiObserver();
		exchangeApi.subscribeObserver(observer);
		MockHttpRequestExecutor executor = createMockHttpRequestExecutor();
		exchangeApi.setHttpRequestExecutor(executor);
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
		exchangeApi.setHttpRequestExecutor(executor);
		HttpRequest request = createDummyRequest();
		MessageDeserializer<String> deserializer = new RawStringMessageDeserializer();
		FutureRestResponse<String> response = exchangeApi.submit(request, deserializer);
		ExchangeApiEvent event = observer.popEvent();
		Assert.assertEquals(ExchangeApiEventType.HTTP_REQUEST, event.getType());
		Assert.assertEquals(request, event.getHttpRequest());
		MockFutureHttpResponse mockResponse = executor.popRequest();
		Assert.assertEquals(request, mockResponse.getRequest());
		mockResponse.complete(createDummyOkResponse(request));
		RestResponse<String> actualResponse = response.get(1, TimeUnit.MILLISECONDS);
		Assert.assertTrue(actualResponse.isOk());
		Assert.assertEquals("pong", actualResponse.getResponse());
		Assert.assertEquals(200, actualResponse.getHttpStatus());
		event = observer.popEvent();
		Assert.assertEquals(ExchangeApiEventType.HTTP_RESPONSE, event.getType());
		Assert.assertTrue(event.getHttpResponse().isOk());
	}
	
	@Test
	public void testSubmitAndExecuteHttpRequestMissingRequestExxecutorUsingRequestThrottler() throws Exception {
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
	public void testCreateDefaultWebsocketanagerWithoutHook() {
		
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
		
		public <A> FutureRestResponse<A> submit(HttpRequest request, MessageDeserializer<A> deserializer) {
			return super.submit(request, deserializer);
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
			return super.createHttpRequestExecutor(factoryClassName);
		}
	}
}
