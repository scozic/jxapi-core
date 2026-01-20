package org.jxapi.netutils.rest;


import java.util.List;
import java.util.concurrent.ExecutionException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jxapi.exchange.ExchangeEvent;
import org.jxapi.exchange.ExchangeEventType;
import org.jxapi.netutils.deserialization.MessageDeserializer;
import org.jxapi.netutils.rest.mock.MockHttpRequestExecutor;
import org.jxapi.netutils.rest.pagination.NextPageResolver;
import org.jxapi.netutils.rest.ratelimits.RateLimitRule;
import org.jxapi.netutils.serialization.MessageSerializer;
import org.jxapi.observability.MockExchangeApiObserver;

/**
 * Unit test for {@link RestEndpoint}.
 */
public class RestEndpointTest {

    private RestEndpoint<String, String> restEndpoint;
    private MockHttpRequestExecutor mockExecutor;
    private MessageDeserializer<String> mockDeserializer;
    private MessageSerializer<String> mockSerializer;
    private MockExchangeApiObserver eventObserver;

    @Before
    public void setUp() {
        mockExecutor = new MockHttpRequestExecutor();
        mockDeserializer = this::deserialize;
        mockSerializer = this::serialize;

        restEndpoint = new RestEndpoint<>();
        restEndpoint.setHttpRequestExecutor(mockExecutor);
        restEndpoint.setDeserializer(mockDeserializer);
        restEndpoint.setSerializer(mockSerializer);
        restEndpoint.setUrl("http://example.com/api/test");
        restEndpoint.setHttpMethod(HttpMethod.POST);
        eventObserver = new MockExchangeApiObserver();
        restEndpoint.setObserver(eventObserver);
    }
    
    private String serialize(String data) {
      return "Serialized:" + data;
    }
    
    private String deserialize(String data) {
      return "Deserialized:" + data;
    }

    @Test
    public void testCreateHttpRequestWithBody() {
        String requestData = "Test Request Data";

        HttpRequest httpRequest = restEndpoint.createHttpRequest(requestData);

        Assert.assertNotNull(httpRequest);
        Assert.assertEquals("http://example.com/api/test", httpRequest.getUrl());
        Assert.assertEquals(HttpMethod.POST, httpRequest.getHttpMethod());
        Assert.assertEquals("Serialized:" + requestData, httpRequest.getBody());
    }
    
    @Test
    public void testCreateHttpRequestWithUrlParamsAndNoBody() {
        String requestData = "TestData";
        restEndpoint.setHttpMethod(HttpMethod.GET);
        HttpRequestUrlParamsSerializer<String> urlParamsSerializer = (data, url) -> url + "?param=" + data;
        restEndpoint.setUrlParamsSerializer(urlParamsSerializer);
        restEndpoint.setSerializer(null); // No body serialization
        HttpRequest httpRequest = restEndpoint.createHttpRequest(requestData);

        Assert.assertNotNull(httpRequest);
        Assert.assertEquals("http://example.com/api/test?param=TestData", httpRequest.getUrl());
        Assert.assertEquals(HttpMethod.GET, httpRequest.getHttpMethod());
        Assert.assertNull(httpRequest.getBody());
    }
    
    @Test
    public void testCreateHttpRequestWithNullData() {
        restEndpoint.setHttpMethod(HttpMethod.GET);
        restEndpoint.setSerializer(null); // No body serialization
        HttpRequest httpRequest = restEndpoint.createHttpRequest(null);

        Assert.assertNotNull(httpRequest);
        Assert.assertEquals("http://example.com/api/test", httpRequest.getUrl());
        Assert.assertEquals(HttpMethod.GET, httpRequest.getHttpMethod());
        Assert.assertNull(httpRequest.getBody());
    }
    
    @Test
    public void testGetterAndSettersAndToString() {
      restEndpoint.setName("TestEndpoint");
      Assert.assertEquals("TestEndpoint", restEndpoint.getName());

      restEndpoint.setWeight(5);
      Assert.assertEquals(5, restEndpoint.getWeight());
      
      restEndpoint.setUrl("http://newexample.com/api");
      Assert.assertEquals("http://newexample.com/api", restEndpoint.getUrl());
      
      restEndpoint.setHttpRequestExecutor(mockExecutor);
      Assert.assertEquals(mockExecutor, restEndpoint.getHttpRequestExecutor());
      
      restEndpoint.setDeserializer(mockDeserializer);
      Assert.assertEquals(mockDeserializer, restEndpoint.getDeserializer());
      
      restEndpoint.setSerializer(mockSerializer);
      Assert.assertEquals(mockSerializer, restEndpoint.getSerializer());
      
      restEndpoint.setRateLimitRules(null);
      Assert.assertNull(restEndpoint.getRateLimitRules());
      
      List<RateLimitRule> rules = List.of(RateLimitRule.createRule("myRule", 30000L, 15));
      restEndpoint.setRateLimitRules(rules);
      Assert.assertEquals(rules, restEndpoint.getRateLimitRules());
      
      Assert.assertEquals(this.eventObserver, restEndpoint.getObserver());
      
      HttpRequestUrlParamsSerializer<String> urlParamsSerializer = (data, url) -> url + "?data=" + data;
      restEndpoint.setUrlParamsSerializer(urlParamsSerializer);
      Assert.assertEquals(urlParamsSerializer, restEndpoint.getUrlParamsSerializer());
      
      restEndpoint.setHttpMethod(HttpMethod.PUT);
      Assert.assertEquals(HttpMethod.PUT, restEndpoint.getHttpMethod());
      
      Assert.assertFalse(restEndpoint.isPaginated());
      
      Assert.assertEquals("RestEndpoint{\"httpMethod\":\"PUT\",\"name\":\"TestEndpoint\",\"url\":\"http://newexample.com/api\",\"httpRequestExecutor\":{\"disposed\":false,\"requestTimeout\":30000,\"submittedRequests\":[]},\"deserializer\":{},\"serializer\":{},\"rateLimitRules\":[{\"id\":\"myRule\",\"timeFrame\":30000,\"maxRequestCount\":15,\"maxTotalWeight\":-1,\"granularity\":10}],\"weight\":5,\"observer\":{\"defaulTimeout\":2000,\"allEvents\":[]},\"urlParamsSerializer\":{},\"paginated\":false}", restEndpoint.toString());
    }
    
    @Test
    public void testSubmit_SuccessfulResponse() throws InterruptedException, ExecutionException {
        String requestData = "Test Request Data";
        String responseData = "Test Response Data";

        HttpResponse mockResponse = createDummyOkResponse(restEndpoint.createHttpRequest(requestData), responseData);

        FutureRestResponse<String> futureResponse = restEndpoint.submit(requestData, null);
        checkEventReceived(ExchangeEventType.HTTP_REQUEST);
        Assert.assertNotNull(futureResponse);
        Assert.assertFalse(futureResponse.isDone());
        mockExecutor.popRequest().complete(mockResponse);
        checkEventReceived(ExchangeEventType.HTTP_RESPONSE);

        Assert.assertNotNull(futureResponse);
        Assert.assertTrue(futureResponse.isDone());
        
        RestResponse<String> restResponse = futureResponse.get();
        Assert.assertTrue(restResponse.isOk());
        Assert.assertEquals(deserialize(responseData), restResponse.getResponse());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testSubmit_ExceptionForHttpRequestExecutorNotSet() {
        String requestData = "Test Request Data";
        restEndpoint.setHttpRequestExecutor(null);
        restEndpoint.submit(requestData, null);
    }
    
    @Test
    public void testSubmit_FailedResponse() throws InterruptedException, ExecutionException {
        String requestData = "Test Request Data";

        HttpResponse mockResponse  = new HttpResponse();
        mockResponse.setResponseCode(400);
        mockResponse.setRequest(restEndpoint.createHttpRequest(requestData));

        FutureRestResponse<String> futureResponse = restEndpoint.submit(requestData, null);
        checkEventReceived(ExchangeEventType.HTTP_REQUEST);
        Assert.assertNotNull(futureResponse);
        Assert.assertFalse(futureResponse.isDone());
        mockExecutor.popRequest().complete(mockResponse);
        checkEventReceived(ExchangeEventType.HTTP_RESPONSE);
        Assert.assertNotNull(futureResponse);
        Assert.assertTrue(futureResponse.isDone());
        
        RestResponse<String> restResponse = futureResponse.get();
        Assert.assertFalse(restResponse.isOk());
    }

    @Test
    public void testSubmit_DeserializationFailure() throws InterruptedException, ExecutionException {
        String requestData = "Test Request Data";
        String responseData = "Invalid Response Data";
        restEndpoint.setDeserializer(s -> {
          throw new RuntimeException("Deserialization failed");
        });
        HttpResponse mockResponse = createDummyOkResponse(restEndpoint.createHttpRequest(requestData), responseData);
        FutureRestResponse<String> futureResponse = restEndpoint.submit(requestData, null);
        checkEventReceived(ExchangeEventType.HTTP_REQUEST);
        Assert.assertNotNull(futureResponse);
        Assert.assertFalse(futureResponse.isDone());
        mockExecutor.popRequest().complete(mockResponse);
        checkEventReceived(ExchangeEventType.HTTP_RESPONSE);

        Assert.assertNotNull(futureResponse);
        Assert.assertTrue(futureResponse.isDone());
        RestResponse<String> restResponse = futureResponse.get();
        Assert.assertFalse(restResponse.isOk());
        Assert.assertNotNull(restResponse.getException());
    }

    @Test
    public void testSubmit_WithNextPageResolver() throws InterruptedException, ExecutionException {
        String requestData = "Test Request Data";
        String responseData = "Test Response Data";

        HttpResponse mockResponse = createDummyOkResponse(restEndpoint.createHttpRequest(requestData), responseData);
        FutureRestResponse<String> nextPageFutureResponse = new FutureRestResponse<>();

        NextPageResolver<String> nextPageResolver = response -> nextPageFutureResponse;
        
        FutureRestResponse<String> futureResponse = restEndpoint.submit(requestData, nextPageResolver);
        checkEventReceived(ExchangeEventType.HTTP_REQUEST);
        Assert.assertNotNull(futureResponse);
        Assert.assertFalse(futureResponse.isDone());
        mockExecutor.popRequest().complete(mockResponse);
        checkEventReceived(ExchangeEventType.HTTP_RESPONSE);
        Assert.assertNotNull(futureResponse);
        Assert.assertTrue(futureResponse.isDone());
        RestResponse<String> response = futureResponse.get();
        Assert.assertEquals(deserialize(responseData), response.getResponse());
        Assert.assertTrue(response.isPaginated());
        Assert.assertEquals(nextPageFutureResponse, futureResponse.get().getNextPageResolver().nextPage(response));
    }
    
    private HttpResponse createDummyOkResponse(HttpRequest request, String body) {
      HttpResponse response = new HttpResponse();
      response.setResponseCode(200);
      response.setRequest(request);
      response.setBody(body);
      response.setHeader("Content-Type", "text/html");
      return response;
    }
    
    private void checkEventReceived(ExchangeEventType eventType) {
      ExchangeEvent event = eventObserver.pop();
      Assert.assertNotNull(event);
      Assert.assertEquals(eventType, event.getType());
      Assert.assertEquals(restEndpoint.getName(), event.getEndpoint());
    }
}
