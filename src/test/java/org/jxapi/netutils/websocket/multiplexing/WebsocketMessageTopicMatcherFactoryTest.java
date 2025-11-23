package org.jxapi.netutils.websocket.multiplexing;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link WebsocketMessageTopicMatcherFactory}
 */
public class WebsocketMessageTopicMatcherFactoryTest {

  @Test
  public void testAnyMatcherFactory() {
    Assert.assertSame(WebsocketMessageTopicMatcher.ANY_MATCHER, 
              WebsocketMessageTopicMatcherFactory.ANY_MATCHER_FACTORY.createWebsocketMessageTopicMatcher());
  }
  
  @Test
  public void testCreateMessageTopicMatcherFactory_NotEmptyFieldList() {
    WebsocketMessageTopicMatcherFactory fac = WSMTMFUtil.and(List.of(
        WSMTMFUtil.value("f1", "value1"), 
        WSMTMFUtil.value("f2", "value2"))); 
    Assert.assertNotNull(fac);
    WebsocketMessageTopicMatcher matcher = fac.createWebsocketMessageTopicMatcher();
        Assert.assertEquals(WebsocketMessageTopicMatchStatus.NO_MATCH, matcher.getStatus());
        Assert.assertEquals(WebsocketMessageTopicMatchStatus.NO_MATCH, matcher.matches("foo", "bar"));
        Assert.assertEquals(WebsocketMessageTopicMatchStatus.NO_MATCH, matcher.getStatus());
        Assert.assertEquals(WebsocketMessageTopicMatchStatus.NO_MATCH, matcher.matches("f1", "value1"));
        Assert.assertEquals(WebsocketMessageTopicMatchStatus.NO_MATCH, matcher.getStatus());
        Assert.assertEquals(WebsocketMessageTopicMatchStatus.MATCHED, matcher.matches("f2", "value2"));
        Assert.assertEquals(WebsocketMessageTopicMatchStatus.MATCHED, matcher.getStatus());
  }
  
}
