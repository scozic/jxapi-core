package org.jxapi.netutils.websocket.multiplexing;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/*
 * Test class for WSMTMFUtil
 */
public class WSMTMFUtilTest {

  @Test
  public void testValueFactory() {
    WebsocketMessageTopicMatcherFactory factory = WSMTMFUtil.value("field1", "value1");
    WebsocketMessageTopicMatcher matcher = factory.createWebsocketMessageTopicMatcher();
    Assert.assertTrue(matcher instanceof FieldValueWebsocketMessageTopicMatcher);
  }
  
  @Test
  public void testRegexpFactory() {
    WebsocketMessageTopicMatcherFactory factory = WSMTMFUtil.regexp("field1", "value.*");
    WebsocketMessageTopicMatcher matcher = factory.createWebsocketMessageTopicMatcher();
    Assert.assertTrue(matcher instanceof FieldRegexpWebsocketMessageTopicMatcher);
  }
  
  @Test
  public void testAndFactory() {
    WebsocketMessageTopicMatcherFactory fac1 = WSMTMFUtil.value("f1", "value1");
    WebsocketMessageTopicMatcherFactory fac2 = WSMTMFUtil.regexp("f2", "value.*");
    WebsocketMessageTopicMatcherFactory andFac = WSMTMFUtil.and(List.of(fac1, fac2));
    WebsocketMessageTopicMatcher matcher = andFac.createWebsocketMessageTopicMatcher();
    Assert.assertTrue(matcher instanceof AndWebsocketMessageTopicMatcher);
  }
  
  @Test
  public void testOrFactory() {
    WebsocketMessageTopicMatcherFactory fac1 = WSMTMFUtil.value("f1", "value1");
    WebsocketMessageTopicMatcherFactory fac2 = WSMTMFUtil.regexp("f2", "value.*");
    WebsocketMessageTopicMatcherFactory orFac = WSMTMFUtil.or(List.of(fac1, fac2));
    WebsocketMessageTopicMatcher matcher = orFac.createWebsocketMessageTopicMatcher();
    Assert.assertTrue(matcher instanceof OrWebsocketMessageTopicMatcher);
  }

}
