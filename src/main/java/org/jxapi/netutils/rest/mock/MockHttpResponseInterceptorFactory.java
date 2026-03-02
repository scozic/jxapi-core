package org.jxapi.netutils.rest.mock;

import org.jxapi.exchange.Exchange;
import org.jxapi.netutils.rest.HttpResponseInterceptorFactory;

/**
 * Factory of {@link MockHttpResponseInterceptor} that can be used in tests to mock response body deserialization.
 */
public class MockHttpResponseInterceptorFactory implements HttpResponseInterceptorFactory {

  @Override
  public MockHttpResponseInterceptor createInterceptor(Exchange exchange) {
    return new MockHttpResponseInterceptor();
  }

}
