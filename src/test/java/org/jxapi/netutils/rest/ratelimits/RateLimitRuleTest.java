package org.jxapi.netutils.rest.ratelimits;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.jxapi.exchange.descriptor.gen.RateLimitRuleDescriptor;

/**
 * Unit test for {@link RateLimitRule}
 */
public class RateLimitRuleTest {

    @Test
    public void testGettersAndSetters() {
        RateLimitRule rule = new RateLimitRule();
        Assert.assertEquals(RateLimitRule.DEFAULT_GRANULARITY, rule.getGranularity());
        rule.setId("id");
        rule.setTimeFrame(1000);
        rule.setMaxRequestCount(100);
        rule.setMaxTotalWeight(1000);
        rule.setGranularity(50);
        Assert.assertEquals("id", rule.getId());
        Assert.assertEquals(1000, rule.getTimeFrame());
        Assert.assertEquals(100, rule.getMaxRequestCount());
        Assert.assertEquals(1000, rule.getMaxTotalWeight());
        Assert.assertEquals(50, rule.getGranularity());
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
    public void testFromDescriptor() {
      RateLimitRuleDescriptor descriptor = new RateLimitRuleDescriptor();
      RateLimitRule rule = RateLimitRule.fromDescriptor(descriptor);
      Assert.assertNull(rule.getId());
      Assert.assertEquals(0, rule.getTimeFrame());
      Assert.assertEquals(-1, rule.getMaxRequestCount());
      Assert.assertEquals(-1, rule.getMaxTotalWeight());
      Assert.assertEquals(RateLimitRule.DEFAULT_GRANULARITY, rule.getGranularity());
      
      descriptor = RateLimitRuleDescriptor.builder()
          .id("id")
          .timeFrame(1000L)
          .maxRequestCount(100)
          .maxTotalWeight(1000)
          .granularity(20)
          .build();

      rule = RateLimitRule.fromDescriptor(descriptor);
      Assert.assertEquals("id", rule.getId());
      Assert.assertEquals(1000, rule.getTimeFrame());
      Assert.assertEquals(100, rule.getMaxRequestCount());
      Assert.assertEquals(1000, rule.getMaxTotalWeight());
      Assert.assertEquals(20, rule.getGranularity());
    }
    
    @Test
    public void testFromDescriptors() {
      RateLimitRuleDescriptor descriptor1 = RateLimitRuleDescriptor.builder().id("id1").timeFrame(1000L)
          .maxRequestCount(100).maxTotalWeight(1000).granularity(20).build();

      RateLimitRuleDescriptor descriptor2 = RateLimitRuleDescriptor.builder().id("id2").timeFrame(2000L)
          .maxRequestCount(200).maxTotalWeight(2000).granularity(30).build();
      
      Assert.assertEquals(0, RateLimitRule.fromDescriptors(null).size());
      List<RateLimitRule> rules = RateLimitRule.fromDescriptors(List.of(descriptor1, descriptor2));
      Assert.assertEquals(2, rules.size());

      RateLimitRule rule1 = rules.get(0);
      Assert.assertEquals("id1", rule1.getId());
      Assert.assertEquals(1000, rule1.getTimeFrame());
      Assert.assertEquals(100, rule1.getMaxRequestCount());
      Assert.assertEquals(1000, rule1.getMaxTotalWeight());
      Assert.assertEquals(20, rule1.getGranularity());

      RateLimitRule rule2 = rules.get(1);
      Assert.assertEquals("id2", rule2.getId());
      Assert.assertEquals(2000, rule2.getTimeFrame());
      Assert.assertEquals(200, rule2.getMaxRequestCount());
      Assert.assertEquals(2000, rule2.getMaxTotalWeight());
      Assert.assertEquals(30, rule2.getGranularity());
    }

    @Test
    public void testToString() {
        RateLimitRule rule = new RateLimitRule();
        rule.setId("id");
        rule.setTimeFrame(1000);
        rule.setMaxRequestCount(100);
        rule.setMaxTotalWeight(1000);
        Assert.assertEquals("RateLimitRule{\"id\":\"id\",\"timeFrame\":1000,\"maxRequestCount\":100,\"maxTotalWeight\":1000,\"granularity\":10}", rule.toString());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetGranularityNegativeThrows() {
      RateLimitRule rule = new RateLimitRule();
      rule.setGranularity(-10);
    }

}
