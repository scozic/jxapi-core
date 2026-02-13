package org.jxapi.netutils.rest.mock;

import org.jxapi.netutils.rest.HttpRequest;
import org.jxapi.netutils.rest.HttpRequestInterceptor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for {@link MockHttpRequestInterceptor}
 */
public class MockHttpRequestInterceptorTest {

  private MockHttpRequestInterceptor interceptor;
  private HttpRequest request;

  @Before
  public void setUp() {
    interceptor = new MockHttpRequestInterceptor();
    request = new HttpRequest();
  }

  @Test
  public void testIntercept_AddsHeaderToRequest() {
    // Arrange
    String headerName = "Authorization";
    String headerValue = "Bearer token";

    // Act
    interceptor.addPreparedInterceptor(new HttpRequestInterceptor() {
      @Override
      public void intercept(HttpRequest request) {
        request.setHeader(headerName, headerValue);
      }
    });
    interceptor.intercept(request);

    // Assert
    Assert.assertEquals(headerValue, request.getHeaders().get(headerName).get(0));
  }

  @Test
  public void testIntercept_NoPreparedInterceptor() {
    interceptor.intercept(request);
    Assert.assertNull(request.getHeaders());
  }

  @Test
  public void testIntercept_MultiplePreparedInterceptors_AllExecutedInOrderAdded() {
    // Arrange
    String headerName1 = "Header1";
    String headerValue1 = "Value1";
    String headerName2 = "Header2";
    String headerValue2 = "Value2";

    // Act
    interceptor.addPreparedInterceptor(new HttpRequestInterceptor() {
      @Override
      public void intercept(HttpRequest request) {
        request.setHeader(headerName1, headerValue1);
      }
    });
    interceptor.addPreparedInterceptor(new HttpRequestInterceptor() {
      @Override
      public void intercept(HttpRequest request) {
        request.setHeader(headerName2, request.getHeaders().get(headerName1).get(0) + headerValue2);
      }
    });
    interceptor.intercept(request);

    // Assert
    Assert.assertEquals(headerValue1, request.getHeaders().get(headerName1).get(0));
    Assert.assertEquals("Value1Value2", request.getHeaders().get(headerName2).get(0));
  }

  @Test(expected = RuntimeException.class)
  public void testAddPreparedThrow_AddsExceptionToPreparedInterceptors() {
    // Arrange
    RuntimeException exception = new RuntimeException("Test exception");

    // Act
    interceptor.addPreparedThrow(exception);

    interceptor.intercept(request);
  }

  @Test
  public void testClearPreparedInterceptors_ClearsPreparedInterceptorsList() {
    // Arrange
    HttpRequestInterceptor preparedInterceptor = new HttpRequestInterceptor() {
      @Override
      public void intercept(HttpRequest request) {
        // Do nothing
      }
    };
    interceptor.addPreparedInterceptor(preparedInterceptor);
    interceptor.clearPreparedInterceptors();
    Assert.assertTrue(interceptor.getPreparedInterceptors().isEmpty());
  }

  @Test
  public void testGetPreparedInterceptors() {
    // Arrange
    HttpRequestInterceptor preparedInterceptor = new HttpRequestInterceptor() {
      @Override
      public void intercept(HttpRequest request) {
        // Do nothing
      }
    };
    interceptor.addPreparedInterceptor(preparedInterceptor);

    // Act
    Assert.assertEquals(1, interceptor.getPreparedInterceptors().size());
    Assert.assertEquals(preparedInterceptor, interceptor.getPreparedInterceptors().get(0));
  }
}
