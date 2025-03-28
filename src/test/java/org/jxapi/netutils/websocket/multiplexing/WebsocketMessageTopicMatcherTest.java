package org.jxapi.netutils.websocket.multiplexing;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link WebsocketMessageTopicMatcher}
 */
public class WebsocketMessageTopicMatcherTest {


  @Test
  public void testAnyMatcher() {
    WebsocketMessageTopicMatcher anyMatcher = WebsocketMessageTopicMatcher.ANY_MATCHER;
    Assert.assertNotNull(anyMatcher);
    Assert.assertEquals(WebsocketMessageTopicMatchStatus.MATCHED, anyMatcher.getStatus());
    Assert.assertEquals(WebsocketMessageTopicMatchStatus.MATCHED, anyMatcher.matches("foo", "bar"));
  }
}
