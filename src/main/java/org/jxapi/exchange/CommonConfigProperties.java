package org.jxapi.exchange;

import java.util.List;

import org.jxapi.netutils.rest.ratelimits.RequestThrottlingMode;
import org.jxapi.pojo.descriptor.Type;
import org.jxapi.util.ConfigProperty;
import org.jxapi.util.DefaultConfigProperty;

/**
 * Lists every {@link ConfigProperty} that is relevant for any {@link Exchange}
 * generated using JXAPI Java API wrapper. These properties may be relevant only
 * when REST APIs are exposed (for instance
 * {@link #HTTP_REQUEST_TIMEOUT_PROPERTY}).
 */
public class CommonConfigProperties {
  
  private CommonConfigProperties() {}
  
  /**
   * Request timeout for calls to REST endpoints of every API. A negative value means no timeout (discouraged)
   */
  public static final ConfigProperty  HTTP_REQUEST_TIMEOUT_PROPERTY = DefaultConfigProperty.create(
      "jxapi.httpRequestTimeout", 
      Type.LONG, 
      "The request timeout for calls to REST endpoints of every API. A negative value means no timeout (discouraged)",
      null);
  
  /**
   * The HTTP request throttling policy in case a rate limit rule is breached, for every exposed ExchangeApi, see enum {@link RequestThrottlingMode}.
   */
  public static final ConfigProperty  REQUEST_THROTTLING_MODE_PROPERTY = DefaultConfigProperty.create(
      "jxapi.requestThrottlingMode", 
      Type.STRING, 
      "Sets the HTTP request throttling policy in case a rate limit rule is breached, for every exposed ExchangeApi, see enum " + RequestThrottlingMode.class.getName(),
      null);
  
  /**
   * The max HTTP request throttle delay for rate limit rule enforcement, for every exposed ExchangeApi.
   */
  public static final ConfigProperty  MAX_REQUEST_THROTTLE_DELAY_PROPERTY = DefaultConfigProperty.create(
      "jxapi.maxRequestThrottleDelay", 
      Type.LONG, 
      "Set the max HTTP request throttle delay for rate limit rule enforcement, for every exposed ExchangeApi.",
      null);
  
  /**
   * List containing all the {@link DefaultConfigProperty} properties of this interface.
   */
  public static final List<ConfigProperty> ALL = List.of(
      HTTP_REQUEST_TIMEOUT_PROPERTY, 
      REQUEST_THROTTLING_MODE_PROPERTY, 
      MAX_REQUEST_THROTTLE_DELAY_PROPERTY);
  
}
