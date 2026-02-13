package org.jxapi.exchange;

/**
 * Factory interface for creating {@link ExchangeHook} instances.
 * <p>
 * Actual implementations of this interface should expose a public no-arg constructor.
 * 
 * @see ExchangeHook
 */
public interface ExchangeHookFactory {

  /**
   * Creates a new instance of {@link ExchangeHook}.
   * 
   * @return a new ExchangeHook instance
   */
  ExchangeHook createExchangeHook();
  
  /**
   * A no-op implementation of {@link ExchangeHookFactory} that returns a no-op
   * {@link ExchangeHook}.
   * <p>
   * This can be used when no custom hooks are needed.
   * @see ExchangeHook#NO_OP
   */
  static final ExchangeHookFactory NO_OP = () -> ExchangeHook.NO_OP;
}
