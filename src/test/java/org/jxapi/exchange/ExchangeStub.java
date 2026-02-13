package org.jxapi.exchange;

import java.util.Properties;

/**
 * A stub implementation of {@link AbstractExchange} for testing purposes. This
 * class is used to create a mock exchange with a predefined ID and version, and empty properties.
 */
public class ExchangeStub extends AbstractExchange {

  public static final String EXCHANGE_ID = "myExchangeID";
  
  public static final ExchangeStub INSTANCE = new ExchangeStub("myExchange");
  
  public ExchangeStub(String name) {
    super(EXCHANGE_ID, "1.0.0", name, new Properties(), "http://localhost:8080/api", false);
  }

}
