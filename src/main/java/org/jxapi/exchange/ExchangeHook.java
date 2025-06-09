package org.jxapi.exchange;

/**
 * Interface for hooks that can be used to perform actions after an Exchange is
 * initialized.
 * <p>
 * Implementations of this interface can be registered through an Exchange to
 * execute custom logic that should be performed only once after the Exchange is
 * initialized, like modifying rate limits or adding custom properties.
 */
public interface ExchangeHook {

  /**
   * Called after an Exchange has been initialized. 
   * This method is invoked once per Exchange instance, allowing for custom logic.
   * @param exchange the Exchange instance that has been initialized. May be cast to specific Exchange type.
   */
  void afterInit(Exchange exchange);
  
  /**
   * A no-op implementation of {@link ExchangeHook} that does nothing.
   * <p>
   * This can be used when no custom hooks are needed.
   */
  static final ExchangeHook NO_OP = e -> {};
}
