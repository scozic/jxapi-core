package com.scz.jxapi.netutils.rest.ratelimits;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * Keeps track of state of a given {@link RateLimitRule} to enforce over time.
 */
public class RateLimitManager {
	
	
	public static final int DEFAULT_GRANULARITY = 10;
	
	/**
	 * Checks if given rate limit is reached for given request count and weight.
	 * 
	 * @param rateLimit the rate limit rule to check against.
	 * @param requestCount is rate limit reached for given request count?
	 * @param weight is rate limit reached for given weight?
	 * @return <code>true</code> If rateLimit is reached for current stat, that is
	 *         if given rateLimit max request count is valid (positive value) and
	 *         given stat request count is sup or equal to max request count, or if
	 *         rateLimit max cumulated weight is valid (positive value) and given
	 *         stat total weight is sup to that value.
	 */
	public static boolean isLimitReached(RateLimitRule rateLimit, int requestCount, int weight) {
		int maxRequestCount = rateLimit.getMaxRequestCount();
		if (maxRequestCount >= 0 && requestCount >=0 && requestCount > maxRequestCount) {
			return true;
		}
		int totalWeight = rateLimit.getMaxTotalWeight();
		return totalWeight >= 0 && weight >=0 && weight > totalWeight;
	}

	private final RateLimitRule rateLimit;
	
	private final TreeMap<Long, TimeStat> timeStats = new TreeMap<>();
	
	private int granularity = DEFAULT_GRANULARITY;
	
	/**
	 * Creates a new rate limit manager for given rate limit rule.
	 * 
	 * @param limit the rate limit rule to enforce.
	 */
	public RateLimitManager(RateLimitRule limit) {
		this.rateLimit = limit;
	}
	
	/**
	 * @return the rate limit rule this manager enforces.
	 */
	public RateLimitRule getRateLimit() {
		return rateLimit;
	}
	
	/**
	 * To be called before issuing a request to an API subject to rate limit. Will
	 * return 0 if request can be sent immediately, in this case state is updated to
	 * count one more call at current timestamp. Otherwise, returns a delay in ms
	 * that corresponds to minimum time to wait for before retrying. Another call to
	 * {@link #requestCall()} should be performed then, until 0 is returned.
	 * 
	 * @return 0 if request can be sent immediately, a delay in ms to wait before retrying otherwise.
	 */
	public long requestCall() {
		return requestCall(0);
	}
	
	/**
	 * Same as {@link #requestCall()} but using weighted request
	 * @param weight the weight of request to send. 0 if request is not a weighted rule.
	 * @return 0 if request can be sent immediately, a delay in ms to wait before retrying otherwise.
	 */
	public long requestCall(int weight) {
		return requestCall(System.currentTimeMillis(), weight);
	}
	
	/**
	 * Same as {@link #requestCall()} but using weighted request and providing
	 * current timestamp (this method is for testing purpose and client
	 * implementation should call {@link #requestCall(int)} or
	 * {@link #requestCall()} that will use current timestamp.
	 * 
	 * @param now    the current time in ms.
	 * @param weight the weight of request to send.
	 * @return 0 if request can be sent immediately, a delay in ms to wait before
	 *         retrying otherwise.
	 */
	public long requestCall(long now, int weight) {
		now = now - now % granularity;
		long minDelayBeforeNextPossibleCall = getMinDelayBeforeNextPossibleCall(now, weight);
		if (minDelayBeforeNextPossibleCall <= 0) {
			TimeStat mss = getTimeStat(now);
			mss.requestCount++;
			mss.totalWeight += weight;
		}
		return minDelayBeforeNextPossibleCall;
	}
	
	/**
	 * @param now the current time in ms.
	 * @param weight the weight of request to send.
	 * @return the minimum delay in ms before next possible call with given weight.
	 */
	public long getMinDelayBeforeNextPossibleCall(long now, int weight) {
		if (rateLimit.getMaxRequestCount() == 0 || rateLimit.getMaxTotalWeight() >= 0 && weight > rateLimit.getMaxTotalWeight()) {
			throw new IllegalArgumentException("Cannot submit call with given weight:" + weight + " that is above threshold of:" + rateLimit);
		}
		// In msStat map we never add stat that would exceed rate limit so current RateLimitManagerStat for now does not exceed the limit.
		// But would it if we submit one more request with given weight?
		RateLimitManagerStat curStat = getCurrentStat(now);
		if (isLimitReached(rateLimit, curStat.getRequestCount() + 1, curStat.getTotalWeight() + weight)) {
			// Limit is reached. Next call will not be possible at least before time of oldest call within rolling time frame becomes out of rolling time frame
			// Remark: We add granularity because actual time of oldest call within rolling time frame is not known more precisely than granularity.
			long oldestCallWithinTimeFrame = timeStats.firstEntry().getKey().longValue() + granularity;
			long timeElapsedSinceOldestCallWithinTimeFrame = now - oldestCallWithinTimeFrame;
			return Math.max(granularity, rateLimit.getTimeFrame()) - timeElapsedSinceOldestCallWithinTimeFrame;
		}
		return 0L;
	}
	
	private TimeStat getTimeStat(long now) {
		return timeStats.computeIfAbsent(now, t -> new TimeStat());
	}
	
	/**
	 * @return the current state of this manager.
	 */
	public RateLimitManagerStat getCurrentStat() {
		return getCurrentStat(System.currentTimeMillis());
	}
	
	private RateLimitManagerStat getCurrentStat(long now) {
		purge(now);
		RateLimitManagerStat stat = new RateLimitManagerStat();
		stat.setTime(now);
		timeStats.forEach((time, mss) -> {
			stat.setRequestCount(stat.getRequestCount() + mss.requestCount);
			stat.setTotalWeight(stat.getTotalWeight() + mss.totalWeight);
		});
		return stat;
	}

	private void purge(long now) {
		long oldestTime = now - rateLimit.getTimeFrame();
		for (Iterator<Entry<Long, TimeStat>> it = timeStats.entrySet().iterator(); it.hasNext();) {
			Entry<Long, TimeStat> entry = it.next();
			if (entry.getKey() < oldestTime) {
				it.remove();
			} else {
				break;
			}
		}
	}

	/**
	 * The granularity in ms of this manager. This is the time unit used to keep
	 * track of rate limit state. <br>
	 * Every time a request is made, a request count is incremented for current time in ms, and 
	 * a total weight is incremented for current time in ms. These values are stored with granularity 
	 * that is if granularity is X ms, the counters are updated at time T are stored for
	 *  grain N*X where N*X &t;= T and (N+1)*X &gtn; <br>
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

	private static class TimeStat {
		int requestCount;
		int totalWeight;
	}
	
}
