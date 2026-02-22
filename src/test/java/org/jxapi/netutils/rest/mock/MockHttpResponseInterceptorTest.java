package org.jxapi.netutils.rest.mock;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jxapi.netutils.rest.HttpResponse;
import org.jxapi.netutils.rest.HttpResponseInterceptor;

/**
 * Test class for {@link MockHttpResponseInterceptor}
 */
public class MockHttpResponseInterceptorTest {

  private MockHttpResponseInterceptor interceptor;
  private HttpResponse Response;

  @Before
  public void setUp() {
    interceptor = new MockHttpResponseInterceptor();
    Response = new HttpResponse();
  }

  @Test
  public void testIntercept_AddsHeaderToResponse() {
    // Arrange
    String headerName = "Authorization";
    String headerValue = "Bearer token";

    // Act
    interceptor.addPreparedInterceptor(new HttpResponseInterceptor() {
      @Override
      public void intercept(HttpResponse Response) {
        Response.setHeader(headerName, headerValue);
      }
    });
    interceptor.intercept(Response);

    // Assert
    Assert.assertEquals(headerValue, Response.getHeaders().get(headerName).get(0));
  }

  @Test
  public void testIntercept_NoPreparedInterceptor() {
    interceptor.intercept(Response);
    Assert.assertNull(Response.getHeaders());
  }

  @Test
  public void testIntercept_MultiplePreparedInterceptors_AllExecutedInOrderAdded() {
    // Arrange
    String headerName1 = "Header1";
    String headerValue1 = "Value1";
    String headerName2 = "Header2";
    String headerValue2 = "Value2";

    // Act
    interceptor.addPreparedInterceptor(new HttpResponseInterceptor() {
      @Override
      public void intercept(HttpResponse Response) {
        Response.setHeader(headerName1, headerValue1);
      }
    });
    interceptor.addPreparedInterceptor(new HttpResponseInterceptor() {
      @Override
      public void intercept(HttpResponse Response) {
        Response.setHeader(headerName2, Response.getHeaders().get(headerName1).get(0) + headerValue2);
      }
    });
    interceptor.intercept(Response);

    // Assert
    Assert.assertEquals(headerValue1, Response.getHeaders().get(headerName1).get(0));
    Assert.assertEquals("Value1Value2", Response.getHeaders().get(headerName2).get(0));
  }

  @Test(expected = RuntimeException.class)
  public void testAddPreparedThrow_AddsExceptionToPreparedInterceptors() {
    // Arrange
    RuntimeException exception = new RuntimeException("Test exception");

    // Act
    interceptor.addPreparedThrow(exception);

    interceptor.intercept(Response);
  }

  @Test
  public void testClearPreparedInterceptors_ClearsPreparedInterceptorsList() {
    // Arrange
    HttpResponseInterceptor preparedInterceptor = new HttpResponseInterceptor() {
      @Override
      public void intercept(HttpResponse Response) {
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
    HttpResponseInterceptor preparedInterceptor = new HttpResponseInterceptor() {
      @Override
      public void intercept(HttpResponse Response) {
        // Do nothing
      }
    };
    interceptor.addPreparedInterceptor(preparedInterceptor);

    // Act
    Assert.assertEquals(1, interceptor.getPreparedInterceptors().size());
    Assert.assertEquals(preparedInterceptor, interceptor.getPreparedInterceptors().get(0));
  }
}
