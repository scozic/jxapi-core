package org.jxapi.netutils.rest.mock;

import org.jxapi.exchange.ExchangeApi;
import org.jxapi.netutils.rest.HttpRequestExecutorFactory;

/**
 * Factory for creating {@link MockHttpRequestExecutor} instances.
 */
public class MockHttpRequestExecutorFactory implements HttpRequestExecutorFactory {

  @Override
  public MockHttpRequestExecutor createExecutor(ExchangeApi exchangeApi) {
    return new MockHttpRequestExecutor();
  }

}
