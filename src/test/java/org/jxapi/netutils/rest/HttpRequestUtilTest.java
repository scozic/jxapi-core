package org.jxapi.netutils.rest;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link HttpRequestUtil}
 */
public class HttpRequestUtilTest {

  @Test
  public void testGetUrlQueryParams() {
    Assert.assertNull(HttpRequestUtil.getUrlQueryParams(null));
    Assert.assertNull(HttpRequestUtil.getUrlQueryParams(""));
    Assert.assertNull(HttpRequestUtil.getUrlQueryParams("http://example.com"));
    Assert.assertEquals("", HttpRequestUtil.getUrlQueryParams("http://example.com?"));
    Assert.assertEquals("name=foo&age=12", HttpRequestUtil.getUrlQueryParams("http://example.com?name=foo&age=12"));
  }

  @Test
  public void testParseUrlQueryParams_EmptyParams() {
    Assert.assertEquals(Map.of(), HttpRequestUtil.parseUrlQueryParams("http://example.com"));
    Assert.assertEquals(Map.of(), HttpRequestUtil.parseUrlQueryParams("http://example.com?"));
  }

  @Test
  public void testParseUrlQueryParams() {
    Map<String, String> params = HttpRequestUtil.parseUrlQueryParams("http://example.com?name=foo&age=12");
    Assert.assertEquals(2, params.size());
    Assert.assertEquals("foo", params.get("name"));
    Assert.assertEquals("12", params.get("age"));
  }
  
  @Test
  public void testParseUrlQueryParams_EmptyParam() {
    Map<String, String> params = HttpRequestUtil.parseUrlQueryParams("http://example.com?name=foo&age=12&empty=&");
    Assert.assertEquals(3, params.size());
    Assert.assertEquals("foo", params.get("name"));
    Assert.assertEquals("12", params.get("age"));
    Assert.assertEquals("", params.get("empty")); // Empty parameter should be included
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testParseUrlQueryParams_InvalidParam() {
    HttpRequestUtil.parseUrlQueryParams("http://example.com?name=foo&age=12=15");
  }

}
