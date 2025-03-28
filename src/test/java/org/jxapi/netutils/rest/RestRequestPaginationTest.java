package org.jxapi.netutils.rest;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link RestRequestPagination}.
 */
public class RestRequestPaginationTest {

  private static TestRequest createRequest(int index) {
    TestRequest request = new TestRequest();
    request.setIndex("" + index);
    return request;
  }
  
  private static TestResponse createResponse(int index) {
    TestResponse response = new TestResponse();
    if (index >= 0) {
      response.setIndex("" + index);
    }
    List<String> items = List.of("item#" + index);
    response.setItems(items);
    return response;
  }
  
  private static TestResponse mergeResponses(TestResponse r1, TestResponse r2) {
    List<String> l = new ArrayList<>();
    l.addAll(r1.getItems());
    l.addAll(r2.getItems());
    TestResponse r = new TestResponse();
    r.setItems(l);
    r.setIndex(r1.getIndex());
    return r;
  }
  
  private static void checkResponseContent(RestResponse<TestResponse> response, String... items) {
    Assert.assertEquals(items.length, response.getResponse().getItems().size());
    for (int i = 0; i < items.length; i++) {
      Assert.assertEquals("Unexpected item at index #" + i, items[i], response.getResponse().getItems().get(i));
    }
  }
  
  @Test
  public void testFetchAllPagesOnlyOneResponsePage() throws Exception {
    TestEndpoint endpoint = new TestEndpoint();
    endpoint.addPreparedResponses(-1);
    RestResponse<TestResponse> response = RestRequestPagination.fetchAllPages(
        createRequest(0), 
        endpoint::call,
        (index, r) -> r.setIndex(index), 
        TestResponse::getIndex, 
        RestRequestPaginationTest::mergeResponses).get();
    Assert.assertTrue(response.isOk());
    checkResponseContent(response, "item#-1");
  }
  
  @Test
  public void testFetchAllPages3ResponsePages() throws Exception {
    TestEndpoint endpoint = new TestEndpoint();
    endpoint.addPreparedResponses(1, 2, -1);
    RestResponse<TestResponse> response = RestRequestPagination.fetchAllPages(
        createRequest(0), 
        endpoint::call,
        (index, r) -> r.setIndex(index), 
        TestResponse::getIndex, 
        RestRequestPaginationTest::mergeResponses).get();
    Assert.assertTrue(response.isOk());
    checkResponseContent(response,"item#1","item#2", "item#-1");
  }
  
  @Test
  public void testFetchAllPagesErrorOnSecondPage() throws Exception {
    TestEndpoint endpoint = new TestEndpoint();
    endpoint.addPreparedResponses(1);
    RestResponse<TestResponse> errorResponse = new RestResponse<>();
    int errorCode = 500;
    errorResponse.setHttpStatus(errorCode);
    String exceptionMsg = "Internal server error";
    errorResponse.setException(new Exception(exceptionMsg));
    endpoint.addPreparedResponses(errorResponse);
    RestResponse<TestResponse> response = RestRequestPagination.fetchAllPages(
        createRequest(0), 
        endpoint::call, 
        (index, r) -> r.setIndex(index), 
        TestResponse::getIndex, 
        RestRequestPaginationTest::mergeResponses).get();
    Assert.assertFalse(response.isOk());
    Assert.assertEquals(errorCode, response.getHttpStatus());
    Assert.assertEquals(exceptionMsg, response.getException().getMessage());
  }
  
  @Test
  public void testExceptionProcessingResponse() throws Exception {
    TestEndpoint endpoint = new TestEndpoint();
    endpoint.addPreparedResponses(-1);
    RestResponse<TestResponse> response = RestRequestPagination.fetchAllPages(
        createRequest(0), 
        endpoint::call,
        (index, r) -> r.setIndex(index), 
        a -> {throw new RuntimeException("Dummy");}, 
        RestRequestPaginationTest::mergeResponses).get();
    Assert.assertFalse(response.isOk());
    Assert.assertNotNull(response.getException());
  }
  
  private static class TestRequest {
    
    private String index;

    public void setIndex(String index) {
      this.index = index;
    }
    
    public String toString() {
      return "TestRequest#" + index;
    }
  }
  
  private static class TestResponse {
    private String index;
    private List<String> items;

    public String getIndex() {
      return index;
    }

    public void setIndex(String index) {
      this.index = index;
    }

    public List<String> getItems() {
      return items;
    }

    public void setItems(List<String> items) {
      this.items = items;
    }

  }
  
  private static class TestEndpoint  {

    List<RestResponse<TestResponse>> preparedResponses = new ArrayList<>();
    
    public FutureRestResponse<TestResponse> call(TestRequest request) {
      FutureRestResponse<TestResponse> response = new FutureRestResponse<>();
      response.complete(preparedResponses.remove(0));
      return response;
    }
    
    public void addPreparedResponses(RestResponse<TestResponse> preparedResponse) {
      preparedResponses.add(preparedResponse);
    }
    
    public void addPreparedResponses(int... responseIndexes) {
      for (int index: responseIndexes) {
        RestResponse<TestResponse> response = new RestResponse<>();
        response.setHttpStatus(200);
        response.setResponse(createResponse(index));
        addPreparedResponses(response);
      }
    }
    
  }
  
}
