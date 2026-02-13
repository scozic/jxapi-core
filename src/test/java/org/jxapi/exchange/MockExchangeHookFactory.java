package org.jxapi.exchange;

public class MockExchangeHookFactory implements ExchangeHookFactory {

  /**
   * Creates a new instance of {@link MockExchangeHook}.
   * 
   * @return a new MockExchangeHook instance
   */
  @Override
  public ExchangeHook createExchangeHook() {
    return new MockExchangeHook();
  }

}
