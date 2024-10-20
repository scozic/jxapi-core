package com.scz.jxapi.netutils.rest.ratelimits;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link RateLimitRule}
 */
public class RateLimitRuleTest {

    @Test
    public void testGettersAndSetters() {
        RateLimitRule rule = new RateLimitRule();
        rule.setId("id");
        rule.setTimeFrame(1000);
        rule.setMaxRequestCount(100);
        rule.setMaxTotalWeight(1000);
        Assert.assertEquals("id", rule.getId());
        Assert.assertEquals(1000, rule.getTimeFrame());
        Assert.assertEquals(100, rule.getMaxRequestCount());
        Assert.assertEquals(1000, rule.getMaxTotalWeight());
    }

    @Test
    public void testCreateRule() {
        RateLimitRule rule = RateLimitRule.createRule("id", 1000, 100);
        Assert.assertEquals("id", rule.getId());
        Assert.assertEquals(1000, rule.getTimeFrame());
        Assert.assertEquals(100, rule.getMaxRequestCount());
    }

    @Test
    public void testCreateWeightedRule() {
        RateLimitRule rule = RateLimitRule.createWeightedRule("id", 1000, 1000);
        Assert.assertEquals("id", rule.getId());
        Assert.assertEquals(1000, rule.getTimeFrame());
        Assert.assertEquals(1000, rule.getMaxTotalWeight());
    }

    @Test
    public void testToString() {
        RateLimitRule rule = new RateLimitRule();
        rule.setId("id");
        rule.setTimeFrame(1000);
        rule.setMaxRequestCount(100);
        rule.setMaxTotalWeight(1000);
        Assert.assertEquals("RateLimitRule{\"id\":\"id\",\"maxRequestCount\":100,\"maxTotalWeight\":1000,\"timeFrame\":1000}", rule.toString());
    }

}
