package org.jxapi.exchange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jxapi.exchanges.employee.gen.v1.deserializers.EmployeeV1GetAllEmployeesResponseDeserializer;
import org.jxapi.exchanges.employee.gen.v1.pojo.EmployeeV1GetAllEmployeesRequest;
import org.jxapi.exchanges.employee.gen.v1.pojo.EmployeeV1GetAllEmployeesResponse;
import org.jxapi.exchanges.employee.gen.v1.serializers.EmployeeV1GetAllEmployeesResponseSerializer;
import org.jxapi.netutils.deserialization.MessageDeserializer;
import org.jxapi.netutils.deserialization.RawStringMessageDeserializer;
import org.jxapi.netutils.rest.FutureRestResponse;
import org.jxapi.netutils.rest.HttpMethod;
import org.jxapi.netutils.rest.HttpRequest;
import org.jxapi.netutils.rest.HttpRequestExecutor;
import org.jxapi.netutils.rest.HttpResponse;
import org.jxapi.netutils.rest.RestResponse;
import org.jxapi.netutils.rest.javanet.JavaNetHttpRequestExecutor;
import org.jxapi.netutils.rest.mock.MockFutureHttpResponse;
import org.jxapi.netutils.rest.mock.MockHttpRequestExecutor;
import org.jxapi.netutils.rest.mock.MockHttpRequestExecutorFactory;
import org.jxapi.netutils.rest.mock.MockHttpRequestInterceptor;
import org.jxapi.netutils.rest.mock.MockHttpRequestInterceptorFactory;
import org.jxapi.netutils.rest.ratelimits.RequestThrottler;
import org.jxapi.netutils.rest.ratelimits.RequestThrottlingMode;
import org.jxapi.netutils.websocket.DefaultWebsocketEndpoint;
import org.jxapi.netutils.websocket.DefaultWebsocketManager;
import org.jxapi.netutils.websocket.WebsocketException;
import org.jxapi.netutils.websocket.WebsocketManager;
import org.jxapi.netutils.websocket.WebsocketSubscribeRequest;
import org.jxapi.netutils.websocket.mock.MockWebsocket;
import org.jxapi.netutils.websocket.mock.MockWebsocketFactory;
import org.jxapi.netutils.websocket.mock.MockWebsocketHook;
import org.jxapi.netutils.websocket.mock.MockWebsocketHookFactory;
import org.jxapi.netutils.websocket.mock.MockWebsocketListener;
import org.jxapi.netutils.websocket.multiplexing.WebsocketMessageTopicMatcherFactory;
import org.jxapi.netutils.websocket.spring.SpringWebsocket;
import org.jxapi.observability.MockExchangeApiObserver;
import org.jxapi.observability.Observable;
import org.jxapi.util.JsonUtil;

/**
 * Unit test for {@link AbstractExchangeApi}
 */
public class AbstractExchangeApiTest {

  private TestExchangeApi exchangeApi;

  @Before
  public void setUp() {
    // Initialize the exchangeApi object with test data
    exchangeApi = new TestExchangeApi("TestApi");
  }

  @Test
  public void testGetName() {
    assertEquals("TestApi", exchangeApi.getName());
  }

  @Test
  public void testGetExchange() {
    Assert.assertSame(ExchangeStub.INSTANCE, exchangeApi.getExchange());
  }
  
  @Test
  public void testGetProperties() {
    Assert.assertSame(ExchangeStub.INSTANCE.getProperties(), exchangeApi.getProperties());
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
    request.setHttpMethod(HttpMethod.GET);
    MessageDeserializer<String> deserializer = RawStringMessageDeserializer.getInstance();
    exchangeApi.submit(request, deserializer);
  }

  @Test
  public void testSubmitAndExecuteHttpRequestOk() throws Exception {
    MockExchangeApiObserver observer = new MockExchangeApiObserver();
    exchangeApi.subscribeObserver(observer);
    MockHttpRequestExecutor executor = createMockHttpRequestExecutor();
    HttpRequest request = createDummyRequest();
    MessageDeserializer<String> deserializer = RawStringMessageDeserializer.getInstance();
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
  public void testSubmitAndExecutePaginatedRequest() throws Exception {
    MockExchangeApiObserver observer = new MockExchangeApiObserver();
    exchangeApi.subscribeObserver(observer);
    MockHttpRequestExecutor executor = createMockHttpRequestExecutor();
    
    EmployeeV1GetAllEmployeesRequest pageRequest = new EmployeeV1GetAllEmployeesRequest();
    pageRequest.setSize(10);
    pageRequest.setPage(1);
    EmployeeV1GetAllEmployeesResponse pageResponse = new EmployeeV1GetAllEmployeesResponse();
    pageResponse.setTotalPages(25);
    pageResponse.setPage(1);
    
    Function<EmployeeV1GetAllEmployeesRequest, FutureRestResponse<EmployeeV1GetAllEmployeesResponse>> paginatedRestEndpoint = r -> { 
      // Not called in this test, just a dummy function
      return new FutureRestResponse<>();
    };
    
    HttpRequest request = createDummyRequest();
    request.setRequest(pageRequest);
    MessageDeserializer<EmployeeV1GetAllEmployeesResponse> deserializer = new EmployeeV1GetAllEmployeesResponseDeserializer();
    FutureRestResponse<EmployeeV1GetAllEmployeesResponse> response = exchangeApi.submitPaginated(request, deserializer, paginatedRestEndpoint);
    ExchangeApiEvent event = observer.pop();
    Assert.assertEquals(ExchangeApiEventType.HTTP_REQUEST, event.getType());
    Assert.assertEquals(request, event.getHttpRequest());
    MockFutureHttpResponse mockResponse = executor.popRequest();
    HttpResponse okResponse = createDummyOkResponse(request);
    RestResponse<EmployeeV1GetAllEmployeesResponse> restResponse = new RestResponse<>(okResponse);
    restResponse.setResponse(pageResponse);
    okResponse.setBody(JsonUtil.pojoToJsonString(pageResponse));
    mockResponse.complete(okResponse);
    Assert.assertEquals(request, mockResponse.getRequest());
    
    RestResponse<EmployeeV1GetAllEmployeesResponse> actualResponse = response.get(1, TimeUnit.MILLISECONDS);
    Assert.assertTrue(actualResponse.isOk());
    Assert.assertEquals(pageResponse, actualResponse.getResponse());
    Assert.assertEquals(200, actualResponse.getHttpStatus());
    Assert.assertNotNull(actualResponse.getNextPageResolver());
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
    MessageDeserializer<String> deserializer = RawStringMessageDeserializer.getInstance();
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
  public void testSubmitAndExecuteHttpRequestWithDummyInterceptor() {
    MockExchangeApiObserver observer = new MockExchangeApiObserver();
    exchangeApi.subscribeObserver(observer);
    createMockHttpRequestExecutor();
    exchangeApi.createHttpRequestInterceptor(MockHttpRequestInterceptorFactory.class.getName());
    AtomicReference<HttpRequest> interceptedRequest = new AtomicReference<>();
    ((MockHttpRequestInterceptor) exchangeApi.getRequestInterceptor()).addPreparedInterceptor(r -> interceptedRequest.set(r));
    HttpRequest request = createDummyRequest();
    MessageDeserializer<String> deserializer = RawStringMessageDeserializer.getInstance();
    exchangeApi.submit(request, deserializer);
    Assert.assertEquals(request, interceptedRequest.get());  
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testCreateHttpRequestInterceptorUnresolvedInterceptorFactory() {
    MockExchangeApiObserver observer = new MockExchangeApiObserver();
    exchangeApi.subscribeObserver(observer);
    MockHttpRequestExecutor executor = new MockHttpRequestExecutor();
    exchangeApi.setHttpRequestExecutor(executor);
    exchangeApi.createHttpRequestInterceptor("");  
  }
  
  @Test
  public void testSubmitAndExecuteHttpRequestOkUsingRequestThrottler() throws Exception {
    exchangeApi = new TestExchangeApi("TestApi", "test-MyExchange", "MyExchange", new Properties(), new RequestThrottler("TestApi"));
    MockExchangeApiObserver observer = new MockExchangeApiObserver();
    exchangeApi.subscribeObserver(observer);
    MockHttpRequestExecutor executor = createMockHttpRequestExecutor();
    HttpRequest request = createDummyRequest();
    MessageDeserializer<String> deserializer = RawStringMessageDeserializer.getInstance();
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
    exchangeApi = new TestExchangeApi("TestApi", "test-MyExchange", "MyExchange", new Properties(), new RequestThrottler("TestApi"));
    MockExchangeApiObserver observer = new MockExchangeApiObserver();
    exchangeApi.subscribeObserver(observer);
    HttpRequest request = createDummyRequest();
    MessageDeserializer<String> deserializer = RawStringMessageDeserializer.getInstance();
    FutureRestResponse<String> response = exchangeApi.submit(request, deserializer);
    RestResponse<String> actualResponse = response.get(1, TimeUnit.MILLISECONDS);
    Assert.assertFalse(actualResponse.isOk());
    Assert.assertEquals("No HttpRequestExecutor set", actualResponse.getException().getMessage());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testCreateHttpRequestExecutorInvalidFactoryClass() {
    exchangeApi.createHttpRequestExecutor("", -1L);
  }
  
  @Test
  public void testCreateDefaultHttpRequestExecutor() {
    exchangeApi.createHttpRequestExecutor(null, 500L);
    HttpRequestExecutor executor = exchangeApi.getHttpRequestExecutor();
    Assert.assertTrue(executor instanceof JavaNetHttpRequestExecutor);
    Assert.assertEquals(500L, executor.getRequestTimeout());
  }
  
  @Test
  public void testCreateWebsocketanagerWithDefaultWebsocketFactoryAndWithoutHookOk() {
    DefaultWebsocketManager websocketManager= createWebsocketManager(null, null, null);
    Assert.assertNotNull(websocketManager);
    Assert.assertTrue(websocketManager.getWebsocket() instanceof SpringWebsocket);
  }
  
  @Test
  public void testCreateWebsocketanagerWithoutHookOk() {
    DefaultWebsocketManager websocketManager= createWebsocketManager("wss://myexchange.com/ws", 
                                     MockWebsocketFactory.class.getName(), 
                                     null);
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
    DefaultWebsocketManager websocketManager = createWebsocketManager("wss://myexchange.com/ws", 
                                    MockWebsocketFactory.class.getName(), 
                                    MockWebsocketHookFactory.class.getName());
    Assert.assertNotNull(websocketManager);
    Assert.assertTrue(websocketManager.getWebsocket() instanceof MockWebsocket);
    Assert.assertTrue(websocketManager.getWebsocketHook() instanceof MockWebsocketHook);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testCreateWebsocketanagerWithHooInvalidWebsocketHookFactoryClass() {
    createWebsocketManager("wss://myexchange.com/ws", 
                    MockWebsocketFactory.class.getName(), 
                 "");
  }
  
  @Test(expected = IllegalStateException.class)
  public void testCreateWebsocketEndpointNullWebsocketManager() {
    exchangeApi.createWebsocketEndpoint("myWsApi", RawStringMessageDeserializer.getInstance());
  }
  
  @Test
  public void testCreateWebsocketEndpointOk() { 
    exchangeApi.createWebsocketManager("wss://myexchange.com/ws", MockWebsocketFactory.class.getName(), null);
    DefaultWebsocketEndpoint<String> wsEndpoint = (DefaultWebsocketEndpoint<String>) exchangeApi.createWebsocketEndpoint("myWsApi", RawStringMessageDeserializer.getInstance());
    Assert.assertNotNull(wsEndpoint);
  }
  
  @Test
  public void testDispatchWsApiEvents() { 
    MockExchangeApiObserver observer = new MockExchangeApiObserver();
    exchangeApi.subscribeObserver(observer);
    exchangeApi.createWebsocketManager("wss://myexchange.com/ws", MockWebsocketFactory.class.getName(), null);
    DefaultWebsocketEndpoint<String> wsEndpoint = (DefaultWebsocketEndpoint<String>) exchangeApi.createWebsocketEndpoint("myWsApi", RawStringMessageDeserializer.getInstance());
    Assert.assertNotNull(wsEndpoint);
    wsEndpoint.subscribe(WebsocketSubscribeRequest.create(null, "mytopic", WebsocketMessageTopicMatcherFactory.ANY_MATCHER_FACTORY), new MockWebsocketListener<>());
    ExchangeApiEvent event = observer.pop();
    Assert.assertEquals(ExchangeApiEventType.WEBSOCKET_SUBSCRIBE, event.getType());
  }
  
  @Test
  public void testReceiveWebsocketErrorOnAsyncSend() throws Exception {
    MockExchangeApiObserver observer = new MockExchangeApiObserver();
    exchangeApi.subscribeObserver(observer);
    exchangeApi.createWebsocketManager("wss://myexchange.com/ws", MockWebsocketFactory.class.getName(), null);
    MockWebsocket ws = (MockWebsocket) ((DefaultWebsocketManager) exchangeApi.getWebsocketManager()).getWebsocket();
    ws.addExceptionToThrowOnSend("Error sending message");
    Assert.assertNotNull(exchangeApi.getWebsocketManager().sendAsync("foo").get());
    
    ExchangeApiEvent event = observer.await(2000);
    Assert.assertEquals(ExchangeApiEventType.WEBSOCKET_ERROR, event.getType());
    WebsocketException ex = event.getWebsocketError();
    Assert.assertNotNull(ex);
    Assert.assertEquals("Error while sending message:foo", ex.getMessage());
  }
  
  @Test
  public void setThrottlingModeAndMaxThrottleDelayNoRequestThrottler() {
    exchangeApi = new TestExchangeApi("TestApi");
    exchangeApi.setRequestThrottlingMode(RequestThrottlingMode.BLOCK);
    exchangeApi.setMaxRequestThrottleDelay(1000L);
    Assert.assertEquals(RequestThrottlingMode.NONE, exchangeApi.getRequestThrottlingMode());
    Assert.assertEquals(-1L, exchangeApi.getMaxRequestThrottleDelay());
  }
  
  @Test
  public void setThrottlingModeAndMaxThrottleDelayWithRequestThrottler() {
    Properties props = new Properties();
    props.setProperty(CommonConfigProperties.REQUEST_THROTTLING_MODE_PROPERTY.getName(), RequestThrottlingMode.NONE.name());
    props.setProperty(CommonConfigProperties.MAX_REQUEST_THROTTLE_DELAY_PROPERTY.getName(), "30000");
    exchangeApi = new TestExchangeApi("TestApi", "test-MyExchange", "MyExchange", props, new RequestThrottler("TestApi"));
    Assert.assertEquals(RequestThrottlingMode.NONE, exchangeApi.getRequestThrottlingMode());
    Assert.assertEquals(30000L, exchangeApi.getMaxRequestThrottleDelay());
    exchangeApi.setRequestThrottlingMode(RequestThrottlingMode.BLOCK);
    exchangeApi.setMaxRequestThrottleDelay(1000L);
    Assert.assertEquals(RequestThrottlingMode.BLOCK, exchangeApi.getRequestThrottlingMode());
    Assert.assertEquals(1000L, exchangeApi.getMaxRequestThrottleDelay());
  }
  
  @Test
  public void testDispose_BothRequestThrottlerHttpRequestExecutorWebsocketManagerNull() {
    exchangeApi = new TestExchangeApi("TestApi");
    exchangeApi.dispose();
    Assert.assertTrue(exchangeApi.isDisposed());
  }
  
  @Test
  public void testSetHttpRequesTimeout_NoHttpRequestExecutor() {
    exchangeApi = new TestExchangeApi("TestApi");
    exchangeApi.setHttpRequestTimeout(500L);
    Assert.assertEquals(-1L, exchangeApi.getHttpRequestTimeout());
  }
  
  @Test
  public void testSetHttpRequesTimeout_WithHttpRequestExecutor() {
    exchangeApi = new TestExchangeApi("TestApi");
    exchangeApi.createHttpRequestExecutor(null, 200L);
    Assert.assertEquals(200L, exchangeApi.getHttpRequestTimeout());
    exchangeApi.setHttpRequestTimeout(500L);
    Assert.assertEquals(500L, exchangeApi.getHttpRequestTimeout());
  }
  
  @Test
  public void testDispose_BothRequestThrottlerHttpRequestExecutorWebsocketManagerNotNull() {
    exchangeApi = new TestExchangeApi("TestApi", "test-MyExchange", "MyExchange", new Properties(), new RequestThrottler("TestApi"));
    exchangeApi.createHttpRequestExecutor(null, 500L);
    createWebsocketManager("wss://myexchange.com/ws", 
          MockWebsocketFactory.class.getName(), 
          MockWebsocketHookFactory.class.getName());
    exchangeApi.dispose();
    Assert.assertTrue(exchangeApi.isDisposed());
  }
  
  @Test
  public void testSerializeRequestBody() {
    exchangeApi = new TestExchangeApi("TestApi");
    HttpRequest request = createDummyRequest();
    exchangeApi.serializeRequestBody(request);
    Assert.assertEquals("\"ping\"", request.getBody());
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
    exchangeApi.createHttpRequestExecutor(MockHttpRequestExecutorFactory.class.getName(), -1L);
    return (MockHttpRequestExecutor) exchangeApi.getHttpRequestExecutor();
  }
  
  private DefaultWebsocketManager createWebsocketManager(String url, 
          String websocketFactoryClassName, 
          String websocketHookFactoryClassName) {
    exchangeApi.createWebsocketManager(url, websocketFactoryClassName, websocketHookFactoryClassName);
    return (DefaultWebsocketManager) exchangeApi.getWebsocketManager();
  }

  private class TestExchangeApi extends AbstractExchangeApi {
    
    public TestExchangeApi(String apiName) {
      super(apiName, ExchangeStub.INSTANCE);
    }
    
    public TestExchangeApi(String apiName, String exchangeName, String exchangeId, Properties properties, RequestThrottler requestThrottler) {
      super(apiName, new AbstractExchange("myExchangeId", "1.0.0", "myExchange", properties, null, null) {}, requestThrottler, null, null);
    }
    
    public Observable<ExchangeApiObserver, ExchangeApiEvent> getObservable() {
      return observable;
    }
    
    public MockHttpRequestInterceptor getRequestInterceptor() {
      return (MockHttpRequestInterceptor) this.httpRequestInterceptor;
    }
    
    @Override
    public void createHttpRequestInterceptor(String factoryClassName) {
       super.createHttpRequestInterceptor(factoryClassName);
    }
    
    @Override
    public void createHttpRequestExecutor(String factoryClassName, long timeout) {
      super.createHttpRequestExecutor(factoryClassName, timeout);
    }
    
    @Override
    protected void createWebsocketManager(String url, 
          String websocketFactoryClassName, 
          String websocketHookFactoryClassName) {
      super.createWebsocketManager(url, websocketFactoryClassName, websocketHookFactoryClassName);
    }
    
    public WebsocketManager getWebsocketManager() {
      return this.websocketManager;
    }
    
  }
}
