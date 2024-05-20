package com.scz.jxapi.netutils.rest.ratelimits;

import com.scz.jxapi.util.JsonUtil;

/**
 * Encapsulates a rate limit definition for an API endpoint, an API group (see
 * ExchangeApiDescriptor), or globally for an exchange. Such limit is expressed
 * for a given timeframe maximum request count or maximum total weight.<br/>
 * 
 * Usually, such limitation is defined in cumulated number of calls to an
 * endpoint or any endpoint of API group or exchange.<br/>
 * 
 * Some APIs like Binance trading API apply limitation in weight, that is every
 * endpoint is assigned a 'weight' that adds at every call and total sum of
 * weights of requests sent within timeframe must not exceed a given limit.<br/>
 */
public class RateLimitRule {
	
	public static RateLimitRule createRule(String id, long timeFrame, int maxRequestCount) {
		RateLimitRule rule = new RateLimitRule();
		rule.setId(id);
		rule.setTimeFrame(timeFrame);
		rule.setMaxRequestCount(maxRequestCount);
		return rule;
	}
	
	public static RateLimitRule createWeightedRule(String id, long timeFrame, int maxWeight) {
		RateLimitRule rule = new RateLimitRule();
		rule.setId(id);
		rule.setTimeFrame(timeFrame);
		rule.setMaxTotalWeight(maxWeight);
		return rule;
	}
	
	private String id;
	
	private long timeFrame;
	
	private int maxRequestCount = -1;
	
	private int maxTotalWeight = -1;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return time frame in ms for which request count or cumulated weight should not exceed limit.
	 */
	public long getTimeFrame() {
		return timeFrame;
	}

	/**
	 * @see #getTimeFrame()
	 */
	public void setTimeFrame(long timeframe) {
		this.timeFrame = timeframe;
	}

	/**
	 * @return The maximum number of requests that can be attempted within rolling time frame. A negative value means it should not be taken into account and this rate limit is expressed in cumulated weight.
	 * @see #getTimeFrame()
	 * @see #getMaxTotalWeight()
	 */
	public int getMaxRequestCount() {
		return maxRequestCount;
	}

	/**
	 * @see #getMaxRequestCount()
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
	 * @see #getMaxTotalWeight()
	 */
	public void setMaxTotalWeight(int maxTotalWeight) {
		this.maxTotalWeight = maxTotalWeight;
	}
	
	public String toString() {
		return getClass().getSimpleName() + JsonUtil.pojoToJsonString(this);
	}

}
