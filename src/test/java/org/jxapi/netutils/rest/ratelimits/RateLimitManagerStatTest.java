package org.jxapi.netutils.rest.ratelimits;

import org.junit.Test;

import org.junit.Assert;

/**
 * Unit test for {@link RateLimitManagerStat}
 */
public class RateLimitManagerStatTest {

    @Test
    public void testToString() {
        RateLimitManagerStat stat = new RateLimitManagerStat();
        stat.setTime(1234567890);
        stat.setRequestCount(100);
        stat.setTotalWeight(1000);
        Assert.assertEquals("{\"requestCount\":100,\"time\":1234567890,\"totalWeight\":1000}", stat.toString());
    }


    @Test
    public void testGettersAndSetters() {
        RateLimitManagerStat stat = new RateLimitManagerStat();
        stat.setTime(1234567890);
        stat.setRequestCount(100);
        stat.setTotalWeight(1000);
        Assert.assertEquals(1234567890, stat.getTime());
        Assert.assertEquals(100, stat.getRequestCount());
        Assert.assertEquals(1000, stat.getTotalWeight());
    }

}
