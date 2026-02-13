package org.jxapi.netutils.rest.javanet;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.glassfish.grizzly.http.util.HttpStatus;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jxapi.netutils.rest.FutureHttpResponse;
import org.jxapi.netutils.rest.HttpMethod;
import org.jxapi.netutils.rest.HttpRequest;
import org.jxapi.netutils.rest.HttpResponse;


/**
 * Unit test for {@link JavaNetHttpRequestExecutor}
 */
public class JavaNetHttpRequestExecutorTest {
	
  private static final AtomicInteger THREAD_COUNTER = new AtomicInteger(1);
  
  private static final long TIMEOUT = 2000L;

  private MockHttpServer mockHttpServer;
  private JavaNetHttpRequestExecutor executor;
  private int serverPort;
  
  @Before
  public void setUp() throws IOException {
    mockHttpServer = new MockHttpServer();
    mockHttpServer.start();
    this.serverPort = mockHttpServer.getPort();
    executor = new JavaNetHttpRequestExecutor("JavaNetHttpRequestExecutorTest" + THREAD_COUNTER.getAndIncrement());
  }
  
  @After
  public void tearDown() {
    if (mockHttpServer != null) {
      mockHttpServer.stop();
    }
  }
  
  private String getServerUrl() {
    return "http://localhost:" + serverPort;
  }
  
  private void checkSameRequests(HttpRequest expected, HttpRequest actual) {
    Assert.assertEquals("Unexpected method in " + actual, expected.getHttpMethod(), actual.getHttpMethod());
    Assert.assertEquals("Unexpected URL in " + actual, expected.getUrl(), actual.getUrl());
    Assert.assertEquals("Unexpected body in " + actual, 
                        Optional.ofNullable(expected.getBody()).orElse(""), 
                        Optional.ofNullable(actual.getBody()).orElse(""));
    Map<String, List<String>> expectedHeaders = expected.getHeaders();
    Map<String, List<String>> actualHeaders = actual.getHeaders();
    if (expectedHeaders != null) {
      expectedHeaders.entrySet().forEach(e -> {
        Assert.assertEquals("Unexpected header values for header:" + e.getKey() + " in " + actual, 
                  e.getValue(), actualHeaders.get(e.getKey()));
      });
    }
  }
  
  private void checkSameResponses(HttpResponse expected, HttpResponse actual) {
    checkSameRequests(expected.getRequest(), actual.getRequest());
    Assert.assertEquals(expected.getResponseCode(), actual.getResponseCode());
    if (expected.getException() != null) {
      Assert.assertNotNull(actual.getException());
    } else {
      Assert.assertEquals("Unexpected body in " + actual, expected.getBody(), actual.getBody());
      Map<String, List<String>> expectedHeaders = expected.getHeaders();
      Map<String, List<String>> actualHeaders = actual.getHeaders();
      Assert.assertEquals("Unexpected header count in " + actual, expectedHeaders.size(), actualHeaders.size());
      expectedHeaders.entrySet().forEach(e -> {
        Assert.assertEquals("Unexpected header values for header:" + e.getKey() + " in " + actual, 
                  e.getValue(), actualHeaders.get(e.getKey()));
      });
    }
    
  }
  
  private void testSubmitRequest(HttpRequest request, HttpResponse response) throws Exception {
    response.setRequest(request);
    FutureHttpResponse futureResponse = executor.execute(request);
    MockHttpRequest mockRequest = mockHttpServer.popRequest(TIMEOUT);
    HttpRequest receivedRequest = mockRequest.getHttpRequest();
    checkSameRequests(request, receivedRequest);
    mockRequest.complete(response);
    HttpResponse actualResponse = futureResponse.get(TIMEOUT, TimeUnit.MILLISECONDS);
    checkSameResponses(response, actualResponse);
  }
  
  @Test
  public void testSubmitGetRequest() throws Exception {
    HttpRequest request = new HttpRequest();
    request.setHttpMethod(HttpMethod.GET);
    request.setUrl(getServerUrl() + "/hello");
    request.setHeader("cache-control", List.of("max-age=31536000", "public"));
    HttpResponse response = new HttpResponse();
    response.setResponseCode(HttpStatus.OK_200.getStatusCode());
    String responseBody = "Hello, world!";
    response.setBody(responseBody);
    response.setHeader("content-length", String.valueOf(responseBody.length()));
    testSubmitRequest(request, response);
  }
  
  @Test
  public void testSubmitGetRequest_NullHeaders() throws Exception {
    HttpRequest request = new HttpRequest();
    request.setHttpMethod(HttpMethod.GET);
    request.setUrl(getServerUrl() + "/hello");
    request.setHeaders(null);
    HttpResponse response = new HttpResponse();
    response.setResponseCode(HttpStatus.OK_200.getStatusCode());
    String responseBody = "Hello, world!";
    response.setBody(responseBody);
    response.setHeader("content-length", String.valueOf(responseBody.length()));
    testSubmitRequest(request, response);
  }
  
  @Test
  public void testSubmitPostRequest() throws Exception {
    HttpRequest request = new HttpRequest();
    request.setHttpMethod(HttpMethod.POST);
    request.setUrl(getServerUrl() + "/helloName");
    request.setBody("Bob");
    request.setHeader("cache-control", List.of("max-age=31536000", "public"));
    HttpResponse response = new HttpResponse();
    response.setResponseCode(HttpStatus.OK_200.getStatusCode());
    String responseBody = "Hello, Bob!";
    response.setBody(responseBody);
    response.setHeader("content-length", String.valueOf(responseBody.length()));
    testSubmitRequest(request, response);
  }
  
  @Test
  public void testSubmitDeleteRequest() throws Exception {
    HttpRequest request = new HttpRequest();
    request.setHttpMethod(HttpMethod.DELETE);
    request.setUrl(getServerUrl() + "/deleteName&name=Bob");
    request.setHeader("cache-control", List.of("max-age=31536000", "public"));
    HttpResponse response = new HttpResponse();
    response.setResponseCode(HttpStatus.OK_200.getStatusCode());
    String responseBody = "OK";
    response.setBody(responseBody);
    response.setHeader("content-length", String.valueOf(responseBody.length()));
    testSubmitRequest(request, response);
  }
  
  @Test
  public void testSubmitPutRequest() throws Exception {
    HttpRequest request = new HttpRequest();
    request.setHttpMethod(HttpMethod.PUT);
    request.setUrl(getServerUrl() + "/putName");
    request.setBody("Bob");
    request.setHeader("cache-control", List.of("max-age=31536000", "public"));
    HttpResponse response = new HttpResponse();
    response.setResponseCode(HttpStatus.OK_200.getStatusCode());
    String responseBody = "Successfully added Bob";
    response.setBody(responseBody);
    response.setHeader("content-length", String.valueOf(responseBody.length()));
    testSubmitRequest(request, response);
  }
  
  @Test
  public void testSubmitGetRequest_MalformedUrl() throws Exception {
    HttpRequest request = new HttpRequest();
    request.setHttpMethod(HttpMethod.GET);
    request.setUrl("foo");
    FutureHttpResponse futureResponse = executor.execute(request);
    HttpResponse actualResponse = futureResponse.get(TIMEOUT, TimeUnit.MILLISECONDS);
    Assert.assertNotNull(actualResponse.getException());
  }
  
  @Test
  public void testSubmitGetRequest_UnsupportedMEthod() throws Exception {
    HttpRequest request = new HttpRequest();
    request.setHttpMethod(HttpMethod.PATCH);
    request.setUrl(getServerUrl() + "/hello");
    FutureHttpResponse futureResponse = executor.execute(request);
    HttpResponse actualResponse = futureResponse.get(TIMEOUT, TimeUnit.MILLISECONDS);
    Assert.assertNotNull(actualResponse.getException());
  }
  
  @Test
  public void testSubmitGetRequest_ErrorInResponseInvalidStatusCode() throws Exception {
    HttpRequest request = new HttpRequest();
    request.setHttpMethod(HttpMethod.GET);
    request.setUrl(getServerUrl() + "/hello");
    request.setHeader("cache-control", List.of("max-age=31536000", "public"));
    HttpResponse response = new HttpResponse();
    response.setResponseCode(0);
    response.setHeader("content-length", String.valueOf(0));
    // Remark: actual exception may be a 'protocol exception', turns out behavior
    // between ide and tests run from maven cycle on command line produce different
    // behavior... disabled innser exception content check
    response.setException(new Exception("java.util.concurrent.CompletionException: java.lang.NumberFormatException: For input string: \"0 C\""));
    testSubmitRequest(request, response);
  }
  
  @Test(expected = IllegalStateException.class)
  public void testSumbitWhenDisposedThrows() {
    HttpRequest request = new HttpRequest();
    request.setHttpMethod(HttpMethod.GET);
    request.setUrl(getServerUrl() + "/hello");
    HttpResponse response = new HttpResponse();
    response.setResponseCode(HttpStatus.OK_200.getStatusCode());
    response.setRequest(request);
    executor.dispose();
    executor.execute(request);
  }
  
  @Test
  public void testCreateJavaNetHttpClientExecutorService() throws Exception {
    String threadNamePrefix = "TestThread-";
    ExecutorService executorService = JavaNetHttpRequestExecutor.createJavaNetHttpClientExecutorService(threadNamePrefix);
    try {
      Future<String> future = executorService.submit(() -> Thread.currentThread().getName());
      String threadName = future.get();
      Assert.assertTrue(threadName.startsWith(threadNamePrefix));
    } finally {
      executorService.shutdownNow();
    }
  }
  
  @Test
  public void testDisposeDoesNotShutdownExternalExecutorService() {
      // Arrange
      ExecutorService externalExecutor = Executors.newCachedThreadPool();
      HttpClient httpClient = HttpClient.newHttpClient();
      JavaNetHttpRequestExecutor exec = new JavaNetHttpRequestExecutor(httpClient, externalExecutor);
      try {
        exec.dispose();
        Assert.assertFalse(externalExecutor.isShutdown());
      } finally {
        externalExecutor.shutdownNow();
      }
      // Act
      exec.dispose();
  }
}
