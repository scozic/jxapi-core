package org.jxapi.exchanges.demo.common;

/**
 * Common interface all POJOs generated for Demo exchange REST endpoint response will implement.
 */
public interface DemoExchangeResponse {

  /**
   * @return A response code indicating the status of response
   */
  int getResponseCode();
}
