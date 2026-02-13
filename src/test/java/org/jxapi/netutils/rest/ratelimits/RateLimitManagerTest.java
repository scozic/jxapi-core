package org.jxapi.netutils.rest.ratelimits;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link RateLimitManager}
 *
 */
public class RateLimitManagerTest {
  
  @Test
  public void testRequestCallCanExecuteNowWhenThresholdNotReachedThenDelayedOnceThresholdReached() {
    RateLimitManager manager = new RateLimitManager(RateLimitRule.createRule("MYRULE", 100L, 2));
    manager.getRule().setGranularity(1);
    Assert.assertEquals(0L, manager.requestCall(0L, 0));
    Assert.assertEquals(0L, manager.requestCall(0L, 0));
    // Threshold reached. Remaining delay is 100ms + 1ms (granularity)
    Assert.assertEquals(101L, manager.requestCall(0L, 0));
  }
  
  @Test
  public void testRequestCallLimitReachedAndRuleDelayHalfElapsed() {
    RateLimitManager manager = new RateLimitManager(RateLimitRule.createRule("MYRULE", 100L, 2));
    manager.getRule().setGranularity(1);
    Assert.assertEquals(0L, manager.requestCall(0L, 0));
    Assert.assertEquals(0L, manager.requestCall(50L, 0));
    Assert.assertEquals(50L, manager.requestCall(51L, 0));
  }
  
  @Test
  public void testRequestCallCanExecuteAgainAfterDelayElapsed() {
    RateLimitManager manager = new RateLimitManager(RateLimitRule.createRule("MYRULE", 100L, 2));
    manager.getRule().setGranularity(1);
    Assert.assertEquals(0L, manager.requestCall(0L, 0));
    Assert.assertEquals(0L, manager.requestCall(0L, 0));
    // To check additional request call in while delay has not elapsed is not counted
    Assert.assertEquals(50L, manager.requestCall(51L, 0));
    Assert.assertEquals(0L, manager.requestCall(101L, 0));
  }
  
  @Test
  public void testRequestCallRollingTimeframeGranularityGreaterThanRuleTimeFrame() {
    RateLimitManager manager = new RateLimitManager(RateLimitRule.createRule("MYRULE", 100L, 2));
    manager.getRule().setGranularity(1000);
    Assert.assertEquals(0L, manager.requestCall(0L, 0));
    Assert.assertEquals(0L, manager.requestCall(50L, 0));
    Assert.assertEquals(2000L, manager.requestCall(50L, 0));
    Assert.assertEquals(2000L, manager.requestCall(101L, 0));
  }
  
  @Test
  public void testRequestCallRollingTimeframe() {
    RateLimitManager manager = new RateLimitManager(RateLimitRule.createRule("MYRULE", 100L, 2));
    manager.getRule().setGranularity(1);
    Assert.assertEquals(0L, manager.requestCall(0L, 0));
    Assert.assertEquals(0L, manager.requestCall(50L, 0));
    Assert.assertEquals(50L, manager.requestCall(51L, 0));
    Assert.assertEquals(0L, manager.requestCall(101L, 0));
    Assert.assertEquals(50L, manager.requestCall(101L, 0));
  }
  
  @Test
  public void testWeightedRequestCallLimitReachedAndRuleDelayHalfElapsed() {
    RateLimitManager manager = new RateLimitManager(RateLimitRule.createWeightedRule("MYRULE", 100L, 100));
    manager.getRule().setGranularity(1);
    Assert.assertEquals(0L, manager.requestCall(0L, 20));
    Assert.assertEquals(0L, manager.requestCall(0L, 80));
    Assert.assertEquals(50L, manager.requestCall(51L, 1));
    Assert.assertEquals(0L, manager.requestCall(101L, 100));
  }
  
  @Test
  public void testWeightedRequestCallRollingTimeframe() {
    RateLimitManager manager = new RateLimitManager(RateLimitRule.createWeightedRule("MYRULE", 100L, 100));
    manager.getRule().setGranularity(1);
    Assert.assertEquals(0L, manager.requestCall(0L, 30));
    Assert.assertEquals(0L, manager.requestCall(50L, 70));
    Assert.assertEquals(50L, manager.requestCall(51L, 1));
    Assert.assertEquals(50L, manager.requestCall(101L, 31));
    Assert.assertEquals(0L, manager.requestCall(101L, 30));
  }
  
  @Test
  public void testIsLimitReached_MaxRequestCountNotReached() {
    RateLimitRule rule = RateLimitRule.createRule("MYRULE", 100L, 2);
    Assert.assertFalse(RateLimitManager.isLimitReached(rule, -1, 0));
  }
  
  @Test
  public void testIsLimitReached_MaxRequestCountReached() {
    RateLimitRule rule = RateLimitRule.createRule("MYRULE", 100L, 2);
    Assert.assertTrue(RateLimitManager.isLimitReached(rule, 3, 0));
  }
  
  @Test
  public void testIsLimitReached_VoidRule() {
    RateLimitRule rule = RateLimitRule.createRule("MYRULE", 0L, -1);
    Assert.assertFalse(RateLimitManager.isLimitReached(rule, 0, 0));
  }
  
  @Test
  public void testIsLimitReached_WeightedRuleMaxWeightNotReached() {
    RateLimitRule rule = RateLimitRule.createWeightedRule("MYRULE", 100L, 100);
    Assert.assertFalse(RateLimitManager.isLimitReached(rule, 0, -1));
  }
  
  @Test
  public void testIsLimitReached_WeightedRuleMaxWeightReached() {
    RateLimitRule rule = RateLimitRule.createWeightedRule("MYRULE", 100L, 100);
    Assert.assertTrue(RateLimitManager.isLimitReached(rule, 0, 101));
  }
 
  @Test
  public void testGetRateLimit() {
    RateLimitRule rule = RateLimitRule.createRule("MYRULE", 100L, 2);
    RateLimitManager manager = new RateLimitManager(rule);
    Assert.assertEquals(rule, manager.getRule());
  }
  
  @Test
  public void testRequestCall() {
    RateLimitManager manager = new RateLimitManager(RateLimitRule.createWeightedRule("MYRULE", 100L, 100));
    Assert.assertEquals(0L, manager.requestCall());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testRequestCallThrowsWhenMaxRequestCountZero() {
    RateLimitManager manager = new RateLimitManager(RateLimitRule.createRule("MYRULE", 100L, 0));
    manager.requestCall();
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testRequestCallThrowsWhenMaxWeightZero() {
    RateLimitManager manager = new RateLimitManager(RateLimitRule.createWeightedRule("MYRULE", 100L, 0));
    manager.requestCall(1);
  }
  
  @Test
  public void testGetCurrentStat() {
    RateLimitManager manager = new RateLimitManager(RateLimitRule.createWeightedRule("MYRULE", 100L, 100));
    RateLimitManagerStat stat = manager.getCurrentStat();
    Assert.assertNotNull(stat);
    long delta = 20;
    long now = System.currentTimeMillis();
    long min = now - delta;
    long max = now + delta;
    long time = stat.getTime();
    Assert.assertTrue("Unexpected time:" + time + " should be around:" + now, 
              time >= min && time < max);
  }
}
