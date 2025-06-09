package org.jxapi.exchange;

import org.jxapi.observability.GenericObserver;

/**
 * A mock implementation of {@link ExchangeHook} for testing purposes. This
 * class extends {@link GenericObserver} to handle calls to {@link #afterInit(Exchange)} as events.
 * <p>
 * In order to retrieve the instance of this hook, you can use the property {@link #MOCK_EXCHANGE_HOOK_PROPERTY} on the {@link Exchange} instance.
 * This property is set by the {@link #afterInit(Exchange)} method with instance of this hook as value.
 * 
 * @see ExchangeHook
 * @see GenericObserver
 */
public class MockExchangeHook extends GenericObserver<Exchange> implements ExchangeHook {
  
  /**
   * The property key under which the {@link MockExchangeHook} instance is stored
   * in the {@link Exchange} properties.
   */
  public static final String MOCK_EXCHANGE_HOOK_PROPERTY = "mockExchangeHook";

  @Override
  public void afterInit(Exchange exchange) {
    exchange.getProperties().put("mockExchangeHook", this);
    super.handleEvent(exchange);
  }

}
