package org.jxapi.netutils.rest.ratelimits;

import java.util.List;
import java.util.Optional;

import org.jxapi.exchange.descriptor.gen.RateLimitRuleDescriptor;
import org.jxapi.util.CollectionUtil;
import org.jxapi.util.EncodingUtil;

/**
 * Encapsulates a rate limit definition for an API endpoint, an API group (see
 * ExchangeApiDescriptor), or globally for an exchange. Such limit is expressed
 * for a given timeframe maximum request count or maximum total weight.<br>
 * 
 * Usually, such limitation is defined in cumulated number of calls to an
 * endpoint or any endpoint of API group or exchange.<br>
 * 
 * Some APIs like Binance trading API apply limitation in weight, that is every
 * endpoint is assigned a 'weight' that adds at every call and total sum of
 * weights of requests sent within timeframe must not exceed a given limit.<br>
 */
public class RateLimitRule {
  
  /**
   * Default granularity in ms to use when enforcing rate limit. 
   * @see #getGranularity()
   */
  public static final int DEFAULT_GRANULARITY = 10;
  
  /**
   * Creates a new rate limit rule with given id, time frame and maximum request count.
   * 
   * @param id Unique identifier of this rule.
   * @param timeFrame Time frame in ms for which request count should not exceed limit.
   * @param maxRequestCount Maximum number of requests that can be attempted within rolling time frame.
   * @return A new rate limit rule.
   */
  public static RateLimitRule createRule(String id, long timeFrame, int maxRequestCount) {
    RateLimitRule rule = new RateLimitRule();
    rule.setId(id);
    rule.setTimeFrame(timeFrame);
    rule.setMaxRequestCount(maxRequestCount);
    return rule;
  }
  
  /**
   * Creates a new rate limit rule with given id, time frame and maximum total weight.
   * 
   * @param id Unique identifier of this rule.
   * @param timeFrame Time frame in ms for which cumulated weight should not exceed limit.
   * @param maxWeight Maximum cumulated weight of calls within time frame limitation.
   * @return A new rate limit rule.
   */
  public static RateLimitRule createWeightedRule(String id, long timeFrame, int maxWeight) {
    RateLimitRule rule = new RateLimitRule();
    rule.setId(id);
    rule.setTimeFrame(timeFrame);
    rule.setMaxTotalWeight(maxWeight);
    return rule;
  }
  
  /**
   * Creates a new rate limit rule from given descriptor.
   * 
   * @param descriptor the rate limit rule descriptor.
   * @return A new rate limit rule.
   */
  public static RateLimitRule fromDescriptor(RateLimitRuleDescriptor descriptor) {
    RateLimitRule rule = new RateLimitRule();
    rule.setId(descriptor.getId());
    rule.setTimeFrame(Optional.ofNullable(descriptor.getTimeFrame()).orElse(0L));
    rule.setMaxRequestCount(Optional.ofNullable(descriptor.getMaxRequestCount()).orElse(-1));
    rule.setMaxTotalWeight(Optional.ofNullable(descriptor.getMaxTotalWeight()).orElse(-1));
    rule.setGranularity(Optional.ofNullable(descriptor.getGranularity()).orElse(DEFAULT_GRANULARITY));
    return rule;
  }
  
  /**
   * Converts all rate limit rules defined at exchange level from
   * {@link RateLimitRuleDescriptor} to {@link RateLimitRule}.
   * 
   * @param descriptors The list of rate limit rule descriptors defined at exchange level.
   * @return The list of rate limit rules defined at exchange level, never
   *         <code>null</code> but may be empty.
   */
  public static List<RateLimitRule> fromDescriptors(List<RateLimitRuleDescriptor> descriptors) {
    return CollectionUtil.emptyIfNull(descriptors).stream()
        .map(RateLimitRule::fromDescriptor)
        .toList();
  }
  
  private String id;
  
  private long timeFrame;
  
  private int maxRequestCount = -1;
  
  private int maxTotalWeight = -1;
  
  private int granularity = DEFAULT_GRANULARITY;
  
  /**
   * @return Unique identifier of this rule.
   */
  public String getId() {
    return id;
  }

  /**
   * @param id Unique identifier of this rule.
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * @return time frame in ms for which request count or cumulated weight should
   *         not exceed limit.
   */
  public long getTimeFrame() {
    return timeFrame;
  }

  /**
   * @param timeFrame time frame in ms for which request count or cumulated weight should
   *               not exceed limit.
   */
  public void setTimeFrame(long timeFrame) {
    this.timeFrame = timeFrame;
  }

  /**
   * @return The maximum number of requests that can be attempted within rolling
   *         time frame. A negative value means it should not be taken into
   *         account and this rate limit is expressed in cumulated weight.
   * @see #getTimeFrame()
   * @see #getMaxTotalWeight()
   */
  public int getMaxRequestCount() {
    return maxRequestCount;
  }

  /**
   * @param maxRequestCount The maximum number of requests that can be attempted
   *                        within rolling time frame. A negative value means it
   *                        should not be taken into account and this rate limit
   *                        is expressed in cumulated weight.
   */
  public void setMaxRequestCount(int maxRequestCount) {
    this.maxRequestCount = maxRequestCount;
  }

  /**
   * @return The maximum cumulated weight of calls within time frame limitation. 
   */
  public int getMaxTotalWeight() {
    return maxTotalWeight;
  }

  /** 
   * @param maxTotalWeight The maximum cumulated weight of calls within time frame limitation.
   */
  public void setMaxTotalWeight(int maxTotalWeight) {
    this.maxTotalWeight = maxTotalWeight;
  }
  
  /**
   * The suggested granularity in ms to use when enforcing. This is the time unit used to keep
   * track of rate limit state. <br>
   * Every time a request is made, a request count is incremented for current time in ms, and 
   * a total weight is incremented for current time in ms. These values are stored with granularity 
   * that is if granularity is X ms, the counters are updated at time T are stored for
   *  grain N*X where N*X &lt;= T and (N+1)*X &gt; n <br>
   * The grains for time older than rule's timeframe are purged before counting or updating.<br>
   * The number/total weight of calls that occurred on rolling time frame is then the sum of 
   * request count or weight for all grain.<br>
   * Default is {@link #DEFAULT_GRANULARITY}. The finer the granularity, the more precise
   * the rate limit enforcement will be. However, the more memory will be used to
   * keep track of state, and the more CPU will be used to update or evaluate it.<br>
   * 
   * @return the granularity in ms of this manager.
   */
  public int getGranularity() {
    return granularity;
  }

  /**
   * Sets the granularity in ms of this manager.
   * 
   * @param granularity the granularity in ms of this manager.
   * @see #getGranularity()
     */
  public void setGranularity(int granularity) {
    if (granularity < 1) {
            throw new IllegalArgumentException("Granularity must be strictly positive");
    }
    this.granularity = granularity;
  }
  
  /**
   * @return A string representation of this rate limit rule. See
   *         {@link EncodingUtil#pojoToString(Object)}.
   */
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }

}
