package org.jxapi.util;

import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jxapi.netutils.rest.ratelimits.RateLimitManagerStat;

public class RateLimitIterationTest {
  
  private static final Logger log = LoggerFactory.getLogger(RateLimitIterationTest.class);
  
  private static final int NB_SAMPLES = 360_000;
  private static final int ITERATIONS = 5000;
  
  private static class TimeStat {
    int requestCount;
    int totalWeight;
  }

  public long run(int nbSamples) {
    TreeMap<Long, TimeStat> timeStats = prepareSamples(nbSamples);
    long start = System.nanoTime();
    getCurrentStat(timeStats);
    return System.nanoTime() - start;
  }
  
  private TreeMap<Long, TimeStat> prepareSamples(int nbSamples) {
    TreeMap<Long, TimeStat> timeStats = new TreeMap<>();
    for (int i = 0; i < nbSamples; i++) {
      TimeStat timeStat = new TimeStat();
      timeStat.requestCount = 1;
      timeStats.put((long) i, timeStat);
    }
    return timeStats;
  }
  
  private RateLimitManagerStat getCurrentStat(TreeMap<Long, TimeStat> timeStats) {
    RateLimitManagerStat stat = new RateLimitManagerStat();
    stat.setTime(0L);
    timeStats.forEach((time, mss) -> {
      stat.setRequestCount(stat.getRequestCount() + mss.requestCount);
      stat.setTotalWeight(stat.getTotalWeight() + mss.totalWeight);
    });
    return stat;
  }
  
  public static void main(String[] args) {
    try {
      long totalElapsed = 0L;
      RateLimitIterationTest test = new RateLimitIterationTest();
      for (int i = 0; i < ITERATIONS; i++) {
        totalElapsed += test.run(NB_SAMPLES);
      }
      double average = totalElapsed / Double.valueOf("" + ITERATIONS).doubleValue();
      log.info("Average elapsed time nanos:{}, ms:{}", average, average / 1_000_000.0);
      System.exit(0);
    } catch (Throwable t) {
      log.error("Error", t);
    }
  }

}
