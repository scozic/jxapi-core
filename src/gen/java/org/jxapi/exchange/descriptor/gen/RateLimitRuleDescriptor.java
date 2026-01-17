package org.jxapi.exchange.descriptor.gen;

import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import javax.annotation.processing.Generated;
import org.jxapi.exchange.descriptor.gen.deserializers.RateLimitRuleDescriptorDeserializer;
import org.jxapi.exchange.descriptor.gen.serializers.RateLimitRuleDescriptorSerializer;
import org.jxapi.util.CompareUtil;
import org.jxapi.util.EncodingUtil;
import org.jxapi.util.Pojo;

/**
 * Represents a rate limit rule that applies to all API groups of the exchange.<br>
 * Exchange descriptor may contain a list of such rate limit rules as value of
 * 'rateLimits' property of exchange.<br>
 * Such rate limit rules will be applied to all endpoints of each API group of
 * the exchange.<br>
 * See {@link org.jxapi.netutils.rest.ratelimits.RateLimitRule} for details.
 * 
 */
@Generated("org.jxapi.generator.java.pojo.PojoGenerator")
@JsonSerialize(using = RateLimitRuleDescriptorSerializer.class)
@JsonDeserialize(using = RateLimitRuleDescriptorDeserializer.class)
public class RateLimitRuleDescriptor implements Pojo<RateLimitRuleDescriptor> {
  
  private static final long serialVersionUID = 222675654459556526L;
  
  /**
   * @return A new builder to build {@link RateLimitRuleDescriptor} objects
   */
  public static Builder builder() {
    return new Builder();
  }
  
  /**
   * Default value for <code>granularity</code>
   */
  public static final Integer GRANULARITY_DEFAULT_VALUE = Integer.valueOf("10");
  
  private String id;
  private Long timeFrame;
  private Integer maxTotalWeight;
  private Integer maxRequestCount;
  private Integer granularity = GRANULARITY_DEFAULT_VALUE;
  
  /**
   * @return Unique identifier of the rate limit rule
   */
  public String getId() {
    return id;
  }
  
  /**
   * @param id Unique identifier of the rate limit rule
   */
  public void setId(String id) {
    this.id = id;
  }
  
  /**
   * @return Time frame in ms for which request count should not exceed limit.
   */
  public Long getTimeFrame() {
    return timeFrame;
  }
  
  /**
   * @param timeFrame Time frame in ms for which request count should not exceed limit.
   */
  public void setTimeFrame(Long timeFrame) {
    this.timeFrame = timeFrame;
  }
  
  /**
   * @return The maximum cumulated weight of calls within time frame limitation.
   */
  public Integer getMaxTotalWeight() {
    return maxTotalWeight;
  }
  
  /**
   * @param maxTotalWeight The maximum cumulated weight of calls within time frame limitation.
   */
  public void setMaxTotalWeight(Integer maxTotalWeight) {
    this.maxTotalWeight = maxTotalWeight;
  }
  
  /**
   * @return The maximum number of requests that can be attempted
   * within rolling time frame. A negative value means it
   * should not be taken into account and this rate limit
   * is expressed in cumulated weight.
   * 
   */
  public Integer getMaxRequestCount() {
    return maxRequestCount;
  }
  
  /**
   * @param maxRequestCount The maximum number of requests that can be attempted
   * within rolling time frame. A negative value means it
   * should not be taken into account and this rate limit
   * is expressed in cumulated weight.
   * 
   */
  public void setMaxRequestCount(Integer maxRequestCount) {
    this.maxRequestCount = maxRequestCount;
  }
  
  /**
   * @return The suggested granularity in ms to use when enforcing. This is the time unit used to keep
   * track of rate limit state. <br>
   * Every time a request is made, a request count is incremented for current time in ms, and 
   * a total weight is incremented for current time in ms. These values are stored with granularity 
   * that is if granularity is X ms, the counters are updated at time T are stored for
   *  grain N*X where N*X &lt;= T and (N+1)*X &gt; n <br>
   * The grains for time older than rule's timeframe are purged before counting or updating.<br>
   * The number/total weight of calls that occurred on rolling time frame is then the sum of 
   * request count or weight for all grain.<br>
   * Default is 10ms. The finer the granularity, the more precise
   * the rate limit enforcement will be. However, the more memory will be used to
   * keep track of state, and the more CPU will be used to update or evaluate it.<br>
   * 
   */
  public Integer getGranularity() {
    return granularity;
  }
  
  /**
   * @param granularity The suggested granularity in ms to use when enforcing. This is the time unit used to keep
   * track of rate limit state. <br>
   * Every time a request is made, a request count is incremented for current time in ms, and 
   * a total weight is incremented for current time in ms. These values are stored with granularity 
   * that is if granularity is X ms, the counters are updated at time T are stored for
   *  grain N*X where N*X &lt;= T and (N+1)*X &gt; n <br>
   * The grains for time older than rule's timeframe are purged before counting or updating.<br>
   * The number/total weight of calls that occurred on rolling time frame is then the sum of 
   * request count or weight for all grain.<br>
   * Default is 10ms. The finer the granularity, the more precise
   * the rate limit enforcement will be. However, the more memory will be used to
   * keep track of state, and the more CPU will be used to update or evaluate it.<br>
   * 
   */
  public void setGranularity(Integer granularity) {
    this.granularity = granularity;
  }
  
  @Override
  public boolean equals(Object other) {
    if (other == null) {
      return false;
    }
    if (this == other) {
      return true;
    }
    if (!getClass().equals(other.getClass()))
      return false;
    RateLimitRuleDescriptor o = (RateLimitRuleDescriptor) other;
    return Objects.equals(this.id, o.id)
        && Objects.equals(this.timeFrame, o.timeFrame)
        && Objects.equals(this.maxTotalWeight, o.maxTotalWeight)
        && Objects.equals(this.maxRequestCount, o.maxRequestCount)
        && Objects.equals(this.granularity, o.granularity);
  }
  
  @Override
  public int compareTo(RateLimitRuleDescriptor other) {
    if (other == null) {
      return 1;
    }
    if (this == other) {
      return 0;
    }
    int res = 0;
    res = CompareUtil.compare(this.id, other.id);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compare(this.timeFrame, other.timeFrame);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compare(this.maxTotalWeight, other.maxTotalWeight);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compare(this.maxRequestCount, other.maxRequestCount);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compare(this.granularity, other.granularity);
    return res;
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(id, timeFrame, maxTotalWeight, maxRequestCount, granularity);
  }
  
  @Override
  public RateLimitRuleDescriptor deepClone() {
    RateLimitRuleDescriptor clone = new RateLimitRuleDescriptor();
    clone.id = this.id;
    clone.timeFrame = this.timeFrame;
    clone.maxTotalWeight = this.maxTotalWeight;
    clone.maxRequestCount = this.maxRequestCount;
    clone.granularity = this.granularity;
    return clone;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
  
  /**
   * Builder for {@link RateLimitRuleDescriptor}
   */
  @Generated("org.jxapi.generator.java.JavaTypeGenerator")
  public static class Builder {
    
    private String id;
    private Long timeFrame;
    private Integer maxTotalWeight;
    private Integer maxRequestCount;
    private Integer granularity = GRANULARITY_DEFAULT_VALUE;
    
    /**
     * Will set the value of <code>id</code> field in the builder
     * @param id Unique identifier of the rate limit rule
     * @return Builder instance
     * @see #setId(String)
     */
    public Builder id(String id)  {
      this.id = id;
      return this;
    }
    
    /**
     * Will set the value of <code>timeFrame</code> field in the builder
     * @param timeFrame Time frame in ms for which request count should not exceed limit.
     * @return Builder instance
     * @see #setTimeFrame(Long)
     */
    public Builder timeFrame(Long timeFrame)  {
      this.timeFrame = timeFrame;
      return this;
    }
    
    /**
     * Will set the value of <code>maxTotalWeight</code> field in the builder
     * @param maxTotalWeight The maximum cumulated weight of calls within time frame limitation.
     * @return Builder instance
     * @see #setMaxTotalWeight(Integer)
     */
    public Builder maxTotalWeight(Integer maxTotalWeight)  {
      this.maxTotalWeight = maxTotalWeight;
      return this;
    }
    
    /**
     * Will set the value of <code>maxRequestCount</code> field in the builder
     * @param maxRequestCount The maximum number of requests that can be attempted
     * within rolling time frame. A negative value means it
     * should not be taken into account and this rate limit
     * is expressed in cumulated weight.
     * 
     * @return Builder instance
     * @see #setMaxRequestCount(Integer)
     */
    public Builder maxRequestCount(Integer maxRequestCount)  {
      this.maxRequestCount = maxRequestCount;
      return this;
    }
    
    /**
     * Will set the value of <code>granularity</code> field in the builder
     * @param granularity The suggested granularity in ms to use when enforcing. This is the time unit used to keep
     * track of rate limit state. <br>
     * Every time a request is made, a request count is incremented for current time in ms, and 
     * a total weight is incremented for current time in ms. These values are stored with granularity 
     * that is if granularity is X ms, the counters are updated at time T are stored for
     *  grain N*X where N*X &lt;= T and (N+1)*X &gt; n <br>
     * The grains for time older than rule's timeframe are purged before counting or updating.<br>
     * The number/total weight of calls that occurred on rolling time frame is then the sum of 
     * request count or weight for all grain.<br>
     * Default is 10ms. The finer the granularity, the more precise
     * the rate limit enforcement will be. However, the more memory will be used to
     * keep track of state, and the more CPU will be used to update or evaluate it.<br>
     * 
     * @return Builder instance
     * @see #setGranularity(Integer)
     */
    public Builder granularity(Integer granularity)  {
      this.granularity = granularity;
      return this;
    }
    
    /**
     * @return a new instance of RateLimitRuleDescriptor using the values set in this builder
     */
    public RateLimitRuleDescriptor build() {
      RateLimitRuleDescriptor res = new RateLimitRuleDescriptor();
      res.id = this.id;
      res.timeFrame = this.timeFrame;
      res.maxTotalWeight = this.maxTotalWeight;
      res.maxRequestCount = this.maxRequestCount;
      res.granularity = this.granularity;
      return res;
    }
  }
}
