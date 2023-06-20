package com.scz.jxapi.netutils.rest.ratelimits;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * Keeps track of state of a given {@link RateLimitRule} to enforce over time.
 */
public class RateLimitManager {
	
	/**
	 * @param stat
	 * @param rateLimit
	 * @return <code>true</code> If rateLimit is reached for current stat, that is
	 *         if given rateLimit max request count is valid (positive value) and
	 *         given stat request count is sup or equal to max request count, or if
	 *         rateLimit max cumulated weight is valid (positive value) and given
	 *         stat total weight is sup to that value.
	 */
	public static boolean isLimitReached(RateLimitRule rateLimit, int requestCount, int weight) {
		int maxRequestCount = rateLimit.getMaxRequestCount();
		if (maxRequestCount >= 0 && requestCount >=0 && requestCount >= maxRequestCount) {
			return true;
		}
		int totalWeight = rateLimit.getMaxTotalWeight();
		return totalWeight >= 0 && weight >=0 && weight > totalWeight;
	}

	private final RateLimitRule rateLimit;
	
	private final TreeMap<Long, MilliSecondStat> msStats = new TreeMap<>();
	
	public RateLimitManager(RateLimitRule limit) {
		this.rateLimit = limit;
	}
	
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
	 * @param weight the weight of request to send.
	 * @return
	 */
	public long requestCall(int weight) {
		return requestCall(System.currentTimeMillis(), weight);
	}
	
	public long requestCall(long now, int weight) {
		long minDelayBeforeNextPossibleCall = getMinDelayBeforeNextPossibleCall(now, weight);
		if (minDelayBeforeNextPossibleCall <= 0) {
			MilliSecondStat mss = getMsStat(now);
			mss.requestCount++;
			mss.totalWeight += weight;
		}
		return minDelayBeforeNextPossibleCall;
	}
	
	public long getMinDelayBeforeNextPossibleCall(long now, int weight) {
		if (rateLimit.getMaxRequestCount() == 0 || rateLimit.getMaxTotalWeight() >= 0 && weight > rateLimit.getMaxTotalWeight()) {
			throw new IllegalArgumentException("Cannot submit call with given weight:" + weight + " that is above threshold of:" + rateLimit);
		}
		// In msStat map we never add stat that would exceed rate limit so current RateLimitManagerStat for now does not exceed the limit.
		// But would it if we submit one more request with given weight?
		RateLimitManagerStat curStat = getCurrentStat(now);
		if (isLimitReached(rateLimit, curStat.getRequestCount() + 1, curStat.getTotalWeight() + weight)) {
			// Limit is reached. Next call will not be possible at least before time of oldest call within rolling time frame becomes out of rolling time frame
			long oldestCallWithinTimeFrame = msStats.firstEntry().getKey().longValue();
			long timeElapsedSinceOldestCallWithinTimeFrame = now - oldestCallWithinTimeFrame;
			return rateLimit.getTimeFrame() - timeElapsedSinceOldestCallWithinTimeFrame;
		}
		return 0L;
	}
	
	private MilliSecondStat getMsStat(long now) {
		MilliSecondStat mss = msStats.get(now);
		if (mss == null) {
			mss = new MilliSecondStat();
			mss.time = now;
			msStats.put(now, mss);
		}
		return mss;
	}
	public RateLimitManagerStat getCurrentStat() {
		return getCurrentStat(System.currentTimeMillis());
	}
	
	private RateLimitManagerStat getCurrentStat(long now) {
		purge(now);
		RateLimitManagerStat stat = new RateLimitManagerStat();
		stat.setTime(now);
		msStats.forEach((time, mss) -> {
			stat.setRequestCount(stat.getRequestCount() + mss.requestCount);
			stat.setTotalWeight(stat.getTotalWeight() + mss.totalWeight);
		});
		return stat;
	}

	private void purge(long now) {
		long oldestTime = now - rateLimit.getTimeFrame();
		for (Iterator<Entry<Long, MilliSecondStat>> it = msStats.entrySet().iterator(); it.hasNext();) {
			Entry<Long, MilliSecondStat> entry = it.next();
			if (entry.getKey() < oldestTime) {
				it.remove();
			} else {
				break;
			}
		}
	}

	private class MilliSecondStat implements Comparable<MilliSecondStat> {
		long time;
		int requestCount;
		int totalWeight;
		
		@Override
		public int compareTo(MilliSecondStat o) {
			if (o == null) {
				return 1;
			}
			if (time > o.time) {
				return 1;
			} else if (time < o.time) {
				return -1;
			}
			return 0;
		}
		
	}
	
}
