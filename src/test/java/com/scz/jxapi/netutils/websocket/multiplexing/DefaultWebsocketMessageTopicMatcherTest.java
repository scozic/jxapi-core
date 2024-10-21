package com.scz.jxapi.netutils.websocket.multiplexing;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link DefaultWebsocketMessageTopicMatcher}
 */
public class DefaultWebsocketMessageTopicMatcherTest {

    @Test
    public void testCreateMatcherEmptyFieldsMatchesAny() {
        DefaultWebsocketMessageTopicMatcher matcher = new DefaultWebsocketMessageTopicMatcher(List.of());
        Assert.assertNotNull(matcher);
        Assert.assertEquals(WebsocketMessageTopicMatchStatus.MATCHED, matcher.getStatus());
		Assert.assertEquals(WebsocketMessageTopicMatchStatus.MATCHED, matcher.matches("foo", "bar"));
    }

    @Test
    public void testCreateMatcherWithOneFieldToMatch_Matches() {
        DefaultWebsocketMessageTopicMatcher matcher = new DefaultWebsocketMessageTopicMatcher(WebsocketMessageTopicMatcherField.createList("f1","value1"));
        Assert.assertNotNull(matcher);
        Assert.assertEquals(WebsocketMessageTopicMatchStatus.NO_MATCH, matcher.getStatus());
        Assert.assertEquals(WebsocketMessageTopicMatchStatus.NO_MATCH, matcher.matches("foo", "bar"));
        Assert.assertEquals(WebsocketMessageTopicMatchStatus.NO_MATCH, matcher.getStatus());
        Assert.assertEquals(WebsocketMessageTopicMatchStatus.MATCHED, matcher.matches("f1", "value1"));
        Assert.assertEquals(WebsocketMessageTopicMatchStatus.MATCHED, matcher.getStatus());
    }

    @Test
    public void testCreateMatcherWithOneFieldToMatch_CantMatch() {
        DefaultWebsocketMessageTopicMatcher matcher = new DefaultWebsocketMessageTopicMatcher(WebsocketMessageTopicMatcherField.createList("f1","value1"));
        Assert.assertNotNull(matcher);
        Assert.assertEquals(WebsocketMessageTopicMatchStatus.NO_MATCH, matcher.getStatus());
        Assert.assertEquals(WebsocketMessageTopicMatchStatus.CANT_MATCH, matcher.matches("f1", "value2"));
        Assert.assertEquals(WebsocketMessageTopicMatchStatus.CANT_MATCH, matcher.getStatus());
        // The matcher is in a CANT_MATCH state, which is a final state.
        Assert.assertEquals(WebsocketMessageTopicMatchStatus.CANT_MATCH, matcher.matches("foo", "bar"));
        Assert.assertEquals(WebsocketMessageTopicMatchStatus.CANT_MATCH, matcher.getStatus());
    }

    @Test
    public void testCreateMatcherWithTwoFieldsToMatch_Matches() {
        DefaultWebsocketMessageTopicMatcher matcher = new DefaultWebsocketMessageTopicMatcher(WebsocketMessageTopicMatcherField.createList("f1","value1", "f2","value2"));
        Assert.assertNotNull(matcher);
        Assert.assertEquals(WebsocketMessageTopicMatchStatus.NO_MATCH, matcher.getStatus());
        Assert.assertEquals(WebsocketMessageTopicMatchStatus.NO_MATCH, matcher.matches("foo", "bar"));
        Assert.assertEquals(WebsocketMessageTopicMatchStatus.NO_MATCH, matcher.getStatus());
        Assert.assertEquals(WebsocketMessageTopicMatchStatus.NO_MATCH, matcher.matches("f1", "value1"));
        Assert.assertEquals(WebsocketMessageTopicMatchStatus.NO_MATCH, matcher.getStatus());
        Assert.assertEquals(WebsocketMessageTopicMatchStatus.MATCHED, matcher.matches("f2", "value2"));
        Assert.assertEquals(WebsocketMessageTopicMatchStatus.MATCHED, matcher.getStatus());
    }

    @Test
    public void testCreateMatcherWithTwoFieldsToMatch_CantMatch() {
        DefaultWebsocketMessageTopicMatcher matcher = new DefaultWebsocketMessageTopicMatcher(WebsocketMessageTopicMatcherField.createList("f1","value1", "f2","value2"));
        Assert.assertNotNull(matcher);
        Assert.assertEquals(WebsocketMessageTopicMatchStatus.NO_MATCH, matcher.getStatus());
        Assert.assertEquals(WebsocketMessageTopicMatchStatus.CANT_MATCH, matcher.matches("f1", "value2"));
        Assert.assertEquals(WebsocketMessageTopicMatchStatus.CANT_MATCH, matcher.getStatus());
        // The matcher is in a CANT_MATCH state, which is a final state.
        Assert.assertEquals(WebsocketMessageTopicMatchStatus.CANT_MATCH, matcher.matches("f2", "value2"));
        Assert.assertEquals(WebsocketMessageTopicMatchStatus.CANT_MATCH, matcher.getStatus());
    }
}
