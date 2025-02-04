package com.scz.jxapi.netutils.rest;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link HttpRequestUtil}
 */
public class HttpRequestUtilTest {

    @Test
    public void testGetUrlQueryParams() {
        Assert.assertNull(HttpRequestUtil.getUrlQueryParams("http://example.com"));
        Assert.assertEquals("", HttpRequestUtil.getUrlQueryParams("http://example.com?"));
        Assert.assertEquals("name=foo&age=12", HttpRequestUtil.getUrlQueryParams("http://example.com?name=foo&age=12"));
    }

}
