package org.jxapi.netutils.rest.ratelimits;

import org.jxapi.util.JsonUtil;

/**
 * Describes status of a {@link RateLimitManager} at a given time: number of requests and total weight counted over rolling time frame.
 */
public class RateLimitManagerStat {

  private long time;
  private int requestCount;
  private int totalWeight;
  
  /**
   * @return Timestamp of this status.
   */
  public long getTime() {
    return time;
  }

  /**
   * Sets the timestamp of this status.
   * 
   * @param time Timestamp of this status.
   */
  public void setTime(long time) {
    this.time = time;
  }

  /**
   * @return Number of requests counted at this time.
   */
  public int getRequestCount() {
    return requestCount;
  }

  /**
   * Sets the number of requests counted at this time.
   * 
   * @param requestCount Number of requests counted at this time.
   */
  public void setRequestCount(int requestCount) {
    this.requestCount = requestCount;
  }

  /**
   * @return Total weight of requests counted at this time.
   */
  public int getTotalWeight() {
    return totalWeight;
  }

  /**
   * Sets the total weight of requests counted at this time.
   * 
   * @param totalWeight Total weight of requests counted at this time.
   */
  public void setTotalWeight(int totalWeight) {
    this.totalWeight = totalWeight;
  }
  
  /**
   * @return JSON representation of this object.
   */
  @Override
  public String toString() {
    return JsonUtil.pojoToJsonString(this);
  }
  
}
