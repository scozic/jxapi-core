package com.scz.jxapi.netutils.rest.ratelimits;

import com.scz.jxapi.util.EncodingUtil;

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
	
	private String id;
	
	private long timeFrame;
	
	private int maxRequestCount = -1;
	
	private int maxTotalWeight = -1;
	
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
	 *         			not exceed limit.
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
	 * @return A string representation of this rate limit rule. See
	 *         {@link EncodingUtil#pojoToString(Object)}.
	 */
	@Override
	public String toString() {
		return EncodingUtil.pojoToString(this);
	}

}
