package com.scz.netutis.rest.ratelimits;

import org.junit.Assert;
import org.junit.Test;

import com.scz.jxapi.netutils.rest.ratelimits.RateLimitManager;
import com.scz.jxapi.netutils.rest.ratelimits.RateLimitRule;

/**
 * Unit test for {@link RateLimitManager}
 *
 */
public class RateLimitManagerTest {
	
	@Test
	public void testRequestCallCanExecuteNowWhenThresholdNotReachedThenDelayedOnceThresholdReached() {
		RateLimitManager manager = new RateLimitManager(RateLimitRule.createRule("MYRULE", 100L, 2));
		Assert.assertEquals(0L, manager.requestCall(0L, 0));
		Assert.assertEquals(0L, manager.requestCall(0L, 0));
		Assert.assertEquals(100L, manager.requestCall(0L, 0));
	}
	
	@Test
	public void testRequestCallLimitReachedAndRuleDelayHalfElapsed() throws InterruptedException {
		RateLimitManager manager = new RateLimitManager(RateLimitRule.createRule("MYRULE", 100L, 2));
		Assert.assertEquals(0L, manager.requestCall(0L, 0));
		Assert.assertEquals(0L, manager.requestCall(50L, 0));
		Assert.assertEquals(50L, manager.requestCall(50L, 0));
	}
	
	@Test
	public void testRequestCallCanExecuteAgainAfterDelayElapsed() throws InterruptedException {
		RateLimitManager manager = new RateLimitManager(RateLimitRule.createRule("MYRULE", 100L, 2));
		Assert.assertEquals(0L, manager.requestCall(0L, 0));
		Assert.assertEquals(0L, manager.requestCall(0L, 0));
		Thread.sleep(50L);
		// To check additional request call in while delay has not elapsed is not counted
		Assert.assertEquals(50L, manager.requestCall(50L, 0));
		Assert.assertEquals(0L, manager.requestCall(101L, 0));
	}
	
	@Test
	public void testRequestCallRollingTimeframe() throws InterruptedException {
		RateLimitManager manager = new RateLimitManager(RateLimitRule.createRule("MYRULE", 100L, 2));
		Assert.assertEquals(0L, manager.requestCall(0L, 0));
		Assert.assertEquals(0L, manager.requestCall(50L, 0));
		Assert.assertEquals(50L, manager.requestCall(50L, 0));
		Assert.assertEquals(0L, manager.requestCall(101L, 0));
		Assert.assertEquals(49L, manager.requestCall(101L, 0));
	}
	
	@Test
	public void testWeightedRequestCallLimitReachedAndRuleDelayHalfElapsed() throws InterruptedException {
		RateLimitManager manager = new RateLimitManager(RateLimitRule.createWeightedRule("MYRULE", 100L, 100));
		Assert.assertEquals(0L, manager.requestCall(0L, 20));
		Assert.assertEquals(0L, manager.requestCall(0L, 80));
		Assert.assertEquals(50L, manager.requestCall(50L, 1));
		Assert.assertEquals(0L, manager.requestCall(101L, 100));
	}
	
	@Test
	public void testWeightedRequestCallRollingTimeframe() throws InterruptedException {
		RateLimitManager manager = new RateLimitManager(RateLimitRule.createWeightedRule("MYRULE", 100L, 100));
		Assert.assertEquals(0L, manager.requestCall(0L, 30));
		Assert.assertEquals(0L, manager.requestCall(50L, 70));
		Assert.assertEquals(50L, manager.requestCall(50L, 1));
		Assert.assertEquals(49L, manager.requestCall(101L, 31));
		Assert.assertEquals(0L, manager.requestCall(101L, 30));
	}
 
}
