package com.scz.jxapi.netutils.rest.ratelimits;

import com.scz.jxapi.util.JsonUtil;

/**
 * Describes status of a {@link RateLimitManager} at a given time: number of requests and total weight counted over rolling time frame.
 */
public class RateLimitManagerStat {

	private long time;
	private int requestCount;
	private int totalWeight;
	
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public int getRequestCount() {
		return requestCount;
	}
	public void setRequestCount(int requestCount) {
		this.requestCount = requestCount;
	}
	public int getTotalWeight() {
		return totalWeight;
	}
	public void setTotalWeight(int totalWeight) {
		this.totalWeight = totalWeight;
	}
	
	public String toString() {
		return JsonUtil.pojoToJsonString(this);
	}
	
}
