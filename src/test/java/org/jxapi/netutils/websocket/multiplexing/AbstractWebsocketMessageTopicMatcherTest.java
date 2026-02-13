package org.jxapi.netutils.websocket.multiplexing;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link AbstractWebsocketMessageTopicMatcher}
 */
public class AbstractWebsocketMessageTopicMatcherTest {
  
  @Test
  public void testGetStatus() {
    StubWebsocketMessageTopicMatcher matcher = new StubWebsocketMessageTopicMatcher();
    Assert.assertEquals(WebsocketMessageTopicMatchStatus.NO_MATCH, matcher.getStatus());
    matcher.setStatus(WebsocketMessageTopicMatchStatus.MATCHED);
    Assert.assertEquals(WebsocketMessageTopicMatchStatus.MATCHED, matcher.getStatus());
  }
  
  @Test
  public void testToString() {
    StubWebsocketMessageTopicMatcher matcher = new StubWebsocketMessageTopicMatcher();
    Assert.assertEquals("StubWebsocketMessageTopicMatcher{\"status\":\"NO_MATCH\"}", matcher.toString());
  }
  
  @Test
  public void testReset() {
    StubWebsocketMessageTopicMatcher matcher = new StubWebsocketMessageTopicMatcher();
    matcher.setStatus(WebsocketMessageTopicMatchStatus.MATCHED);
    matcher.reset();
    Assert.assertEquals(WebsocketMessageTopicMatchStatus.NO_MATCH, matcher.getStatus());
  }
  

}
