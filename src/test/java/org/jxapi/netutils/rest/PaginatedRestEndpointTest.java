package org.jxapi.netutils.rest;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jxapi.netutils.deserialization.MessageDeserializer;
import org.jxapi.netutils.rest.mock.MockFutureHttpResponse;
import org.jxapi.netutils.rest.mock.MockHttpRequestExecutor;
import org.jxapi.netutils.rest.pagination.PaginatedRestRequest;
import org.jxapi.netutils.rest.pagination.PaginatedRestResponse;
import org.jxapi.netutils.rest.ratelimits.RateLimitRule;
import org.jxapi.netutils.serialization.MessageSerializer;
import org.jxapi.observability.MockExchangeApiObserver;
import org.jxapi.util.JsonUtil;
import org.jxapi.util.Pojo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * Unit tests for {@link PaginatedRestEndpoint}.
 */
public class PaginatedRestEndpointTest {
  
  private PaginatedRestEndpoint<TestPaginatedRestRequest, TestPaginatedRestResponse> restEndpoint;
  private MockHttpRequestExecutor mockExecutor;
  private MessageDeserializer<TestPaginatedRestResponse> mockDeserializer;
  private MessageSerializer<TestPaginatedRestRequest> mockSerializer;
  private MockExchangeApiObserver eventObserver;

  @Before
  public void setUp() {
      mockExecutor = new MockHttpRequestExecutor();
      restEndpoint = new PaginatedRestEndpoint<>();
      restEndpoint.setHttpRequestExecutor(mockExecutor);
      restEndpoint.setUrl("http://example.com/api/test");
      mockDeserializer = data -> {
        try {
          return JsonUtil.DEFAULT_OBJECT_MAPPER.readValue(data, TestPaginatedRestResponse.class);
        } catch (JsonMappingException e) {
          throw new RuntimeException("Deserialization error", e);
        } catch (JsonProcessingException e) {
          throw new RuntimeException("Deserialization error", e);
        }
      };
      mockSerializer = data -> {
        try {
          return JsonUtil.DEFAULT_OBJECT_MAPPER.writeValueAsString(data);
        } catch (JsonProcessingException e) {
          throw new RuntimeException("Serialization error", e);
        }
      };
      restEndpoint.setDeserializer(mockDeserializer);
      restEndpoint.setSerializer(mockSerializer);
      restEndpoint.setHttpMethod(HttpMethod.POST);
      eventObserver = new MockExchangeApiObserver();
      restEndpoint.setObserver(eventObserver);
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
      
      restEndpoint.setRateLimitRules(null);
      Assert.assertNull(restEndpoint.getRateLimitRules());
      
      List<RateLimitRule> rules = List.of(RateLimitRule.createRule("myRule", 30000L, 15));
      restEndpoint.setRateLimitRules(rules);
      Assert.assertEquals(rules, restEndpoint.getRateLimitRules());
      
      Assert.assertEquals(this.eventObserver, restEndpoint.getObserver());
      
      HttpRequestUrlParamsSerializer<TestPaginatedRestRequest> urlParamsSerializer = (data, url) -> url + "?data=" + data;
      restEndpoint.setUrlParamsSerializer(urlParamsSerializer);
      Assert.assertEquals(urlParamsSerializer, restEndpoint.getUrlParamsSerializer());
      
      restEndpoint.setHttpMethod(HttpMethod.PUT);
      Assert.assertEquals(HttpMethod.PUT, restEndpoint.getHttpMethod());
      
      Assert.assertTrue(restEndpoint.isPaginated());
      
      Assert.assertEquals("PaginatedRestEndpoint{\"httpMethod\":\"PUT\",\"name\":\"TestEndpoint\",\"url\":\"http://newexample.com/api\",\"httpRequestExecutor\":{\"disposed\":false,\"requestTimeout\":30000,\"submittedRequests\":[]},\"deserializer\":{},\"serializer\":{},\"rateLimitRules\":[{\"id\":\"myRule\",\"timeFrame\":30000,\"maxRequestCount\":15,\"maxTotalWeight\":-1,\"granularity\":10}],\"weight\":5,\"observer\":{\"defaulTimeout\":2000,\"allEvents\":[]},\"urlParamsSerializer\":{},\"paginated\":true}", restEndpoint.toString());
    }
    
    @Test
    public void testSubmitPaginated() throws InterruptedException, ExecutionException {
      // Prepare mock responses
      TestPaginatedRestResponse page1 = new TestPaginatedRestResponse();
      page1.setPage(1);
      page1.setHasNext(true);
      TestPaginatedRestResponse page2 = new TestPaginatedRestResponse();
      page2.setPage(2);
      page2.setHasNext(false);
      
      // Create request
      TestPaginatedRestRequest request1 = new TestPaginatedRestRequest();
      request1.setCurrentPage(1);
      
      // Submit paginated request
      FutureRestResponse<TestPaginatedRestResponse> futureResponse1 = restEndpoint.submit(request1);
      Assert.assertFalse(futureResponse1.isDone());
      MockFutureHttpResponse mockResponse1 = mockExecutor.popRequest();
      mockResponse1.complete(createOkResponse(restEndpoint.createHttpRequest(request1), page1));
      Assert.assertTrue(futureResponse1.isDone());
      RestResponse<TestPaginatedRestResponse> restResponse1 = futureResponse1.get();
      TestPaginatedRestResponse response1 = restResponse1.getResponse();
      Assert.assertEquals(1, response1.getPage());
      Assert.assertTrue(response1.hasNextPage());
      FutureRestResponse<TestPaginatedRestResponse> futuresResponse2 = restResponse1.getNextPageResolver().nextPage(restResponse1);
      Assert.assertFalse(futuresResponse2.isDone());
      MockFutureHttpResponse mockResponse2 = mockExecutor.popRequest();
      mockResponse2.complete(createOkResponse(restEndpoint.createHttpRequest(request1), page2));
      Assert.assertTrue(futuresResponse2.isDone());
      RestResponse<TestPaginatedRestResponse> restResponse2 = futuresResponse2.get();
      TestPaginatedRestResponse response2 = restResponse2.getResponse();
      Assert.assertEquals(2, response2.getPage());
      Assert.assertFalse(response2.hasNextPage());
    }
    
    private HttpResponse createOkResponse(HttpRequest request, TestPaginatedRestResponse resp) {
      HttpResponse response = new HttpResponse();
      response.setResponseCode(200);
      response.setRequest(request);
      response.setBody(JsonUtil.pojoToJsonString(resp));
      response.setHeader("Content-Type", "text/html");
      return response;
    }
    
    private static class TestPaginatedRestRequest implements PaginatedRestRequest, Pojo<TestPaginatedRestRequest> {
      
      private static final long serialVersionUID = 1598818953015225869L;
	  private int currentPage = 1;

      @Override
      public void setNextPage(PaginatedRestResponse lastPage) {
        TestPaginatedRestResponse resp = (TestPaginatedRestResponse) lastPage;
        setCurrentPage(resp.getPage() + 1);
        
      }

      @SuppressWarnings("unused")
      public int getCurrentPage() {
        return currentPage;
      }

      public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
      }

      @Override
      public TestPaginatedRestRequest deepClone() {
        TestPaginatedRestRequest clone = new TestPaginatedRestRequest();
        clone.setCurrentPage(this.currentPage);
        return clone;
      }

      @Override
      public int compareTo(TestPaginatedRestRequest o) {
        if (o == null) {
          return 1;
        }
        if (this == o) {
          return 0;
        }
        return Integer.compare(this.currentPage, o.currentPage);
      }
      
    }
    
    private static class TestPaginatedRestResponse implements PaginatedRestResponse, Pojo<TestPaginatedRestResponse> {
      
      private static final long serialVersionUID = 4204875578130864590L;
	  private boolean hasNext = false;
      private int page;

      @Override
      public boolean hasNextPage() {
        return isHasNext();
      }

      public boolean isHasNext() {
        return hasNext;
      }

      public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
      }

      public int getPage() {
        return page;
      }

      public void setPage(int page) {
        this.page = page;
      }

      @Override
      public TestPaginatedRestResponse deepClone() {
        TestPaginatedRestResponse clone = new TestPaginatedRestResponse();
        clone.setHasNext(this.hasNext);
        clone.setPage(this.page);
        return null;
      }

      @Override
      public int compareTo(TestPaginatedRestResponse o) {
        if (o == null) {
          return 1;
        }
        if (this == o) {
          return 0;
        }
        return Integer.compare(this.page, o.getPage());
      }

    }

}
