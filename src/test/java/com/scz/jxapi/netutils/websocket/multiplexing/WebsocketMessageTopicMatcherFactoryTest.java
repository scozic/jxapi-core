package com.scz.jxapi.netutils.websocket.multiplexing;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link WebsocketMessageTopicMatcherFactory}
 */
public class WebsocketMessageTopicMatcherFactoryTest {

	@Test
	public void testAnyMatcherFactory() {
		Assert.assertEquals(WebsocketMessageTopicMatcher.ANY_MATCHER, 
							WebsocketMessageTopicMatcherFactory.ANY_MATCHER_FACTORY.createWebsocketMessageTopicMatcher());
	}
	
	@Test
	public void testCreateMessageTopicMatcherFactory_EmptyFieldListReturnsAnyMatcherFactory() {
		Assert.assertEquals(WebsocketMessageTopicMatcherFactory.ANY_MATCHER_FACTORY, 
							WebsocketMessageTopicMatcherFactory.createFactory());
	}
	
	@Test
	public void testCreateMessageTopicMatcherFactory_NotEmptyFieldList() {
		WebsocketMessageTopicMatcherFactory fac = WebsocketMessageTopicMatcherFactory.createFactory("f1", "value1", "f2", "value2");
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
