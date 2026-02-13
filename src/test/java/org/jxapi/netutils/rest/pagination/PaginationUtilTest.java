package org.jxapi.netutils.rest.pagination;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.junit.Assert;
import org.junit.Test;
import org.jxapi.exchanges.employee.gen.v1.pojo.EmployeeV1GetAllEmployeesRequest;
import org.jxapi.exchanges.employee.gen.v1.pojo.EmployeeV1GetAllEmployeesResponse;
import org.jxapi.netutils.rest.FutureRestResponse;
import org.jxapi.netutils.rest.HttpResponse;
import org.jxapi.netutils.rest.RestResponse;
import org.jxapi.observability.GenericObserver;

/**
 * Unit test for {@link PaginationUtil}.
 */
public class PaginationUtilTest {

  @Test
  public void testHasNextPage() {
    Assert.assertFalse(PaginationUtil.hasNextPage(null));
    RestResponse<EmployeeV1GetAllEmployeesResponse> response = new RestResponse<>();
    // response has invalid http status -> no next page
    Assert.assertFalse(PaginationUtil.hasNextPage(response));
    response.setHttpStatus(200);
    response.setPaginated(false);
    // response is not paginated -> no next page
    Assert.assertFalse(PaginationUtil.hasNextPage(response));
    response.setPaginated(true);
    // response is paginated but has no data -> no next page
    Assert.assertFalse(PaginationUtil.hasNextPage(response));
    EmployeeV1GetAllEmployeesResponse data = new EmployeeV1GetAllEmployeesResponse();
    data.setPage(1);
    data.setTotalPages(1);
    response.setResponse(data);
    // response is paginated and has data, but last page is reached -> no next page
    Assert.assertFalse(PaginationUtil.hasNextPage(response));
    data.setTotalPages(2);
    // response is ok, paginated, and has data, and next page is available
    Assert.assertTrue(PaginationUtil.hasNextPage(response));
  }
  
  @Test
  public void testGetNextPageResolverAndFetchNextPage() throws InterruptedException, ExecutionException {
    MockPaginatedEndpoint endpoint = new MockPaginatedEndpoint();
    
    int size = 10;
    EmployeeV1GetAllEmployeesRequest request1 = new EmployeeV1GetAllEmployeesRequest();
    request1.setPage(1);
    request1.setSize(size);
    
    NextPageResolver<EmployeeV1GetAllEmployeesResponse> resolver1 = PaginationUtil.getNextPageResolver(request1, endpoint::call);
    
    RestResponse<EmployeeV1GetAllEmployeesResponse> response1 = new RestResponse<>();
    response1.setHttpStatus(200);
    response1.setPaginated(true);
    EmployeeV1GetAllEmployeesResponse data1 = new EmployeeV1GetAllEmployeesResponse();
    data1.setPage(1);
    data1.setTotalPages(2);
    response1.setResponse(data1);
    
    // Call to next page with a response that has a next page
    response1.setNextPageResolver(resolver1);
    FutureRestResponse<EmployeeV1GetAllEmployeesResponse> futureResponse1 = PaginationUtil.fetchNextPage(response1);
    // next page should trigger a call to the endpoint, with the request for the next page
    Assert.assertEquals(1, endpoint.size());
    PaginatedEndpointCall call = endpoint.pop();
    
    
    EmployeeV1GetAllEmployeesRequest request2 = request1.deepClone();
    request2.setPage(2);
    Assert.assertEquals(request2, call.request);
    Assert.assertNotNull(call.futureResponse);
    
    RestResponse<EmployeeV1GetAllEmployeesResponse> response2 = new RestResponse<>();
    response2.setHttpStatus(200);
    response2.setPaginated(true);
    EmployeeV1GetAllEmployeesResponse data2 = data1.deepClone();
    data2.setPage(2);
    response2.setResponse(data2);
    call.futureResponse.complete(response2);
    
    Assert.assertTrue(futureResponse1.isDone());
    Assert.assertTrue(futureResponse1.get().isOk());
    Assert.assertEquals(data2, futureResponse1.get().getResponse());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testGetNextPageResolver_NullRestResponse() {
    MockPaginatedEndpoint endpoint = new MockPaginatedEndpoint();
    int size = 10;
    EmployeeV1GetAllEmployeesRequest request1 = new EmployeeV1GetAllEmployeesRequest();
    request1.setPage(1);
    request1.setSize(size);
    NextPageResolver<EmployeeV1GetAllEmployeesResponse> resolver1 = PaginationUtil.getNextPageResolver(request1, endpoint::call);
    resolver1.nextPage(null);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testGetNextPageResolver_NoNextPageForRestResponse() {
    MockPaginatedEndpoint endpoint = new MockPaginatedEndpoint();
    int size = 10;
    EmployeeV1GetAllEmployeesRequest request1 = new EmployeeV1GetAllEmployeesRequest();
    request1.setPage(1);
    request1.setSize(size);
    NextPageResolver<EmployeeV1GetAllEmployeesResponse> resolver = PaginationUtil.getNextPageResolver(request1, endpoint::call);
    
    RestResponse<EmployeeV1GetAllEmployeesResponse> response = new RestResponse<>();
    response.setHttpStatus(200);
    response.setPaginated(true);
    EmployeeV1GetAllEmployeesResponse data2 = new EmployeeV1GetAllEmployeesResponse();
    data2.setPage(2);
    data2.setTotalPages(2);
    response.setResponse(data2);
    resolver.nextPage(response);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testFetchAllPages_NullResponse() {
    PaginationUtil.fetchAllPages(null);
  }
  
  @Test
  public void testFetchAllPages() throws InterruptedException, ExecutionException {
    HttpResponse okResponse = new HttpResponse();
    okResponse.setResponseCode(200);
    MockPaginatedEndpoint endpoint = new MockPaginatedEndpoint();

    int size = 10;
    EmployeeV1GetAllEmployeesRequest request1 = new EmployeeV1GetAllEmployeesRequest();
    request1.setPage(1);
    request1.setSize(size);
    // Initial call to fetch first page
    FutureRestResponse<EmployeeV1GetAllEmployeesResponse> futureResponse1 = endpoint.call(request1);
    PaginatedEndpointCall call1 = endpoint.pop();
    Assert.assertEquals(request1, call1.request);
    RestResponse<EmployeeV1GetAllEmployeesResponse> response1 = new RestResponse<>(okResponse);
    response1.setHttpStatus(200);
    response1.setPaginated(true);
    EmployeeV1GetAllEmployeesResponse data1 = new EmployeeV1GetAllEmployeesResponse();
    data1.setPage(1);
    data1.setTotalPages(3);
    response1.setResponse(data1);
    call1.complete(response1);

    Assert.assertTrue(futureResponse1.isDone());
    Assert.assertTrue(futureResponse1.get().isOk());

    // Fetch all pages from the first page response
    FutureRestResponse<List<EmployeeV1GetAllEmployeesResponse>> allPagesFuture = PaginationUtil.fetchAllPages(futureResponse1);

    // 2nd page page call
    Assert.assertEquals(1, endpoint.size());
    PaginatedEndpointCall call2 = endpoint.pop();
    EmployeeV1GetAllEmployeesRequest request2 = request1.deepClone();
    request2.setPage(2);
    Assert.assertEquals(request2, call2.request);
    Assert.assertFalse(allPagesFuture.isDone());
    RestResponse<EmployeeV1GetAllEmployeesResponse> response2 = new RestResponse<>(okResponse);
    response2.setHttpStatus(200);
    response2.setPaginated(true);
    EmployeeV1GetAllEmployeesResponse data2 = data1.deepClone();
    data2.setPage(2);
    response2.setResponse(data2);
    call2.complete(response2);
    Assert.assertFalse(allPagesFuture.isDone());

    // 3rd page call
    EmployeeV1GetAllEmployeesRequest request3 = request2.deepClone();
    request3.setPage(3);
    RestResponse<EmployeeV1GetAllEmployeesResponse> response3 = new RestResponse<>(okResponse);
    response3.setHttpStatus(200);
    response3.setPaginated(true);
    EmployeeV1GetAllEmployeesResponse data3 = data2.deepClone();
    data3.setPage(3);
    response3.setResponse(data3);
    
    PaginatedEndpointCall call3 = endpoint.pop();
    Assert.assertEquals(request3, call3.request);
    call3.complete(response3);
    
    Assert.assertTrue(allPagesFuture.isDone());
    Assert.assertTrue(allPagesFuture.get().isOk());
    List<EmployeeV1GetAllEmployeesResponse> allPages = allPagesFuture.get().getResponse();
    Assert.assertEquals(3, allPages.size());
    Assert.assertEquals(data1.getEmployees(), allPages.get(0).getEmployees());
    Assert.assertEquals(data2.getEmployees(), allPages.get(1).getEmployees());
    Assert.assertEquals(data3.getEmployees(), allPages.get(2).getEmployees());
  }
  
  @Test
  public void testFetchAllPages_LoadingPage() throws InterruptedException, ExecutionException {
    HttpResponse okResponse = new HttpResponse();
    okResponse.setResponseCode(200);
    MockPaginatedEndpoint endpoint = new MockPaginatedEndpoint();

    int size = 10;
    EmployeeV1GetAllEmployeesRequest request1 = new EmployeeV1GetAllEmployeesRequest();
    request1.setPage(1);
    request1.setSize(size);
    // Initial call to fetch first page
    FutureRestResponse<EmployeeV1GetAllEmployeesResponse> futureResponse1 = endpoint.call(request1);
    PaginatedEndpointCall call1 = endpoint.pop();
    Assert.assertEquals(request1, call1.request);
    RestResponse<EmployeeV1GetAllEmployeesResponse> response1 = new RestResponse<>(okResponse);
    response1.setHttpStatus(200);
    response1.setPaginated(true);
    EmployeeV1GetAllEmployeesResponse data1 = new EmployeeV1GetAllEmployeesResponse();
    data1.setPage(1);
    data1.setTotalPages(3);
    response1.setResponse(data1);
    call1.complete(response1);

    Assert.assertTrue(futureResponse1.isDone());
    Assert.assertTrue(futureResponse1.get().isOk());

    // Fetch all pages from the first page response
    FutureRestResponse<List<EmployeeV1GetAllEmployeesResponse>> allPagesFuture = PaginationUtil.fetchAllPages(futureResponse1);

    // 2nd page page call
    Assert.assertEquals(1, endpoint.size());
    PaginatedEndpointCall call2 = endpoint.pop();
    EmployeeV1GetAllEmployeesRequest request2 = request1.deepClone();
    request2.setPage(2);
    Assert.assertEquals(request2, call2.request);
    Assert.assertFalse(allPagesFuture.isDone());
    RestResponse<EmployeeV1GetAllEmployeesResponse> response2 = new RestResponse<>(okResponse);
    response2.setHttpStatus(500);
    response2.setException(new IOException("Simulated server error"));
    response2.setPaginated(true);
    call2.complete(response2);
    Assert.assertTrue(allPagesFuture.isDone());

    // No 3rd page call
    Assert.assertEquals(0, endpoint.size());
    RestResponse<List<EmployeeV1GetAllEmployeesResponse>> allPagesResponse = allPagesFuture.get();
    Assert.assertFalse(allPagesResponse.isOk());
    Assert.assertEquals(response2.getException(), allPagesResponse.getException());
    // Final response should contain the first page only, since the second page carried null payload
    Assert.assertEquals(1, allPagesResponse.getResponse().size());
    Assert.assertEquals(data1, allPagesResponse.getResponse().get(0));
  }
  
  private static class MockPaginatedEndpoint extends GenericObserver<PaginatedEndpointCall> {
    
    public FutureRestResponse<EmployeeV1GetAllEmployeesResponse> call(EmployeeV1GetAllEmployeesRequest request) {
      PaginatedEndpointCall call = new PaginatedEndpointCall(request, new FutureRestResponse<>(), PaginationUtil.getNextPageResolver(request, this::call));
      handleEvent(call);
      return call.futureResponse;
    }
  }
  
  private static class PaginatedEndpointCall {
    EmployeeV1GetAllEmployeesRequest request;
    FutureRestResponse<EmployeeV1GetAllEmployeesResponse> futureResponse;
    NextPageResolver<EmployeeV1GetAllEmployeesResponse> nextPageResolver;
    
    public PaginatedEndpointCall(EmployeeV1GetAllEmployeesRequest request,
                                 FutureRestResponse<EmployeeV1GetAllEmployeesResponse> futureResponse,
                                 NextPageResolver<EmployeeV1GetAllEmployeesResponse> nextPageResolver) {
      this.request = request;
      this.futureResponse = futureResponse;
      this.nextPageResolver = nextPageResolver;
    }
    
    public void complete(RestResponse<EmployeeV1GetAllEmployeesResponse> response) {
      response.setNextPageResolver(nextPageResolver);
      futureResponse.complete(response);
    }
  }

}
