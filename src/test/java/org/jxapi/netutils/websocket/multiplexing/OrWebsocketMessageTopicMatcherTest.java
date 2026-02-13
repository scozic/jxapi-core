package org.jxapi.netutils.websocket.multiplexing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * Unit test for {@link OrWebsocketMessageTopicMatcher}
 */
public class OrWebsocketMessageTopicMatcherTest {

  @Test
  public void testCreateAndMatcher_MultipleOperands() {
    List<WebsocketMessageTopicMatcher> matchers = List.of(
        new StubWebsocketMessageTopicMatcher(WebsocketMessageTopicMatchStatus.MATCHED),
        new StubWebsocketMessageTopicMatcher(WebsocketMessageTopicMatchStatus.MATCHED));
    AndWebsocketMessageTopicMatcher matcher = new AndWebsocketMessageTopicMatcher(matchers);
    // Initial status is NO_MATCH and all operands have been reset
    assertEquals(WebsocketMessageTopicMatchStatus.NO_MATCH, matcher.getStatus());
    assertEquals(WebsocketMessageTopicMatchStatus.NO_MATCH, matchers.get(0).getStatus());
    assertEquals(WebsocketMessageTopicMatchStatus.NO_MATCH, matchers.get(1).getStatus());
  }

  @Test
  public void testGetMatchers() {
    List<WebsocketMessageTopicMatcher> matchers = List.of(
        new StubWebsocketMessageTopicMatcher(),
        new StubWebsocketMessageTopicMatcher());
    OrWebsocketMessageTopicMatcher matcher = new OrWebsocketMessageTopicMatcher(matchers);
    assertSame(matchers, matcher.getMatchers());
  }

  @Test
  public void testCreateAndMatcher_ZeroOperand() {
    OrWebsocketMessageTopicMatcher matcher = new OrWebsocketMessageTopicMatcher(List.of());
    assertEquals(WebsocketMessageTopicMatchStatus.CANT_MATCH, matcher.getStatus());
    matcher.reset();
    assertEquals(WebsocketMessageTopicMatchStatus.CANT_MATCH, matcher.getStatus());
  }

  @Test
  public void testMatches_OneMatchTriggersMatch() {
    List<StubWebsocketMessageTopicMatcher> matchers = List.of(
        new StubWebsocketMessageTopicMatcher(),
        new StubWebsocketMessageTopicMatcher());
    OrWebsocketMessageTopicMatcher matcher = new OrWebsocketMessageTopicMatcher(
        new ArrayList<WebsocketMessageTopicMatcher>(matchers));
    matchers.get(0).setStatus(WebsocketMessageTopicMatchStatus.MATCHED);
    assertEquals(WebsocketMessageTopicMatchStatus.MATCHED, matcher.matches("field", "value"));
    assertEquals(WebsocketMessageTopicMatchStatus.MATCHED, matcher.getStatus());
    matchers.get(0).setStatus(WebsocketMessageTopicMatchStatus.CANT_MATCH);
    // MATCHED status is terminal
    assertEquals(WebsocketMessageTopicMatchStatus.MATCHED, matcher.matches("field", "value"));
    assertEquals(WebsocketMessageTopicMatchStatus.MATCHED, matcher.getStatus());
  }
  
  @Test
  public void testLastCantMatchMatcherIsNotRechecked() {
    List<StubWebsocketMessageTopicMatcher> matchers = List.of(
        new StubWebsocketMessageTopicMatcher(),
        new StubWebsocketMessageTopicMatcher());
    OrWebsocketMessageTopicMatcher matcher = new OrWebsocketMessageTopicMatcher(
        new ArrayList<WebsocketMessageTopicMatcher>(matchers));
    matchers.get(0).setStatus(WebsocketMessageTopicMatchStatus.CANT_MATCH);
    assertEquals(WebsocketMessageTopicMatchStatus.NO_MATCH, matcher.matches("field", "value"));
    assertEquals(WebsocketMessageTopicMatchStatus.NO_MATCH, matcher.getStatus());
    matchers.get(0).setStatus(WebsocketMessageTopicMatchStatus.MATCHED);
    // Last CANT_MATCH status matcher is not rechecked
    assertEquals(WebsocketMessageTopicMatchStatus.NO_MATCH, matcher.matches("field", "value"));
    assertEquals(WebsocketMessageTopicMatchStatus.NO_MATCH, matcher.getStatus());
  }
  
  @Test
  public void testBothCantMatchTriggersTerminalCantMatchThenReset() {
    List<StubWebsocketMessageTopicMatcher> matchers = List.of(
        new StubWebsocketMessageTopicMatcher(),
        new StubWebsocketMessageTopicMatcher());
    OrWebsocketMessageTopicMatcher matcher = new OrWebsocketMessageTopicMatcher(
        new ArrayList<WebsocketMessageTopicMatcher>(matchers));
    matchers.get(0).setStatus(WebsocketMessageTopicMatchStatus.CANT_MATCH);
    matchers.get(1).setStatus(WebsocketMessageTopicMatchStatus.CANT_MATCH);
    assertEquals(WebsocketMessageTopicMatchStatus.CANT_MATCH, matcher.matches("field", "value"));
    assertEquals(WebsocketMessageTopicMatchStatus.CANT_MATCH, matcher.getStatus());
    matchers.get(0).setStatus(WebsocketMessageTopicMatchStatus.CANT_MATCH);
    
    matcher.reset();
    assertEquals(WebsocketMessageTopicMatchStatus.NO_MATCH, matcher.getStatus());
    assertEquals(WebsocketMessageTopicMatchStatus.NO_MATCH, matchers.get(0).getStatus());
    assertEquals(WebsocketMessageTopicMatchStatus.NO_MATCH, matchers.get(1).getStatus());
  }
  
  @Test
  public void testCantMatchMatcherAfterUnmatchedIsRechecked() {
    List<StubWebsocketMessageTopicMatcher> matchers = List.of(
        new StubWebsocketMessageTopicMatcher(),
        new StubWebsocketMessageTopicMatcher());
    OrWebsocketMessageTopicMatcher matcher = new OrWebsocketMessageTopicMatcher(
        new ArrayList<WebsocketMessageTopicMatcher>(matchers));
    matchers.get(1).setStatus(WebsocketMessageTopicMatchStatus.CANT_MATCH);
    assertEquals(WebsocketMessageTopicMatchStatus.NO_MATCH, matcher.matches("field", "value"));
    assertEquals(WebsocketMessageTopicMatchStatus.NO_MATCH, matcher.getStatus());
    matchers.get(1).setStatus(WebsocketMessageTopicMatchStatus.MATCHED);
    // Last CANT_MATCH status matcher is not rechecked
    assertEquals(WebsocketMessageTopicMatchStatus.MATCHED, matcher.matches("field", "value"));
    assertEquals(WebsocketMessageTopicMatchStatus.MATCHED, matcher.getStatus());
  }

}
