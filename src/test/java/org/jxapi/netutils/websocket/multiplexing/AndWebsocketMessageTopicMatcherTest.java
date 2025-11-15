
package org.jxapi.netutils.websocket.multiplexing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * Unit test for {@link AndWebsocketMessageTopicMatcher}
 */
public class AndWebsocketMessageTopicMatcherTest {

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
    AndWebsocketMessageTopicMatcher matcher = new AndWebsocketMessageTopicMatcher(matchers);
    assertSame(matchers, matcher.getMatchers());
  }

  @Test
  public void testCreateAndMatcher_ZeroOperand() {
    AndWebsocketMessageTopicMatcher matcher = new AndWebsocketMessageTopicMatcher(List.of());
    assertEquals(WebsocketMessageTopicMatchStatus.MATCHED, matcher.getStatus());
    matcher.reset();
    assertEquals(WebsocketMessageTopicMatchStatus.MATCHED, matcher.getStatus());
  }

  @Test
  public void testMatches_AllMatchersMatched() {
    List<StubWebsocketMessageTopicMatcher> matchers = List.of(
        new StubWebsocketMessageTopicMatcher(),
        new StubWebsocketMessageTopicMatcher());
    AndWebsocketMessageTopicMatcher matcher = new AndWebsocketMessageTopicMatcher(
        new ArrayList<WebsocketMessageTopicMatcher>(matchers));
    matchers.forEach(m -> m.setStatus(WebsocketMessageTopicMatchStatus.MATCHED));
    assertEquals(WebsocketMessageTopicMatchStatus.MATCHED, matcher.matches("field", "value"));
    assertEquals(WebsocketMessageTopicMatchStatus.MATCHED, matcher.getStatus());
  }

  @Test
  public void testMatches_LastPreviousMatchedNotRechecked() {
    List<StubWebsocketMessageTopicMatcher> matchers = List.of(
        new StubWebsocketMessageTopicMatcher(),
        new StubWebsocketMessageTopicMatcher());
    AndWebsocketMessageTopicMatcher matcher = new AndWebsocketMessageTopicMatcher(
        new ArrayList<WebsocketMessageTopicMatcher>(matchers));
    matchers.get(0).setStatus(WebsocketMessageTopicMatchStatus.MATCHED);
    // No match because second matcher is NO_MATCH. But first matcher is counted as
    // matched and won't be rechecked.
    assertEquals(WebsocketMessageTopicMatchStatus.NO_MATCH, matcher.matches("field", "value"));
    matchers.get(0).setStatus(WebsocketMessageTopicMatchStatus.NO_MATCH);
    matchers.get(1).setStatus(WebsocketMessageTopicMatchStatus.MATCHED);

    // Only second matcher is checked, first matcher is not rechecked.
    assertEquals(WebsocketMessageTopicMatchStatus.MATCHED, matcher.matches("field", "value"));
    assertEquals(WebsocketMessageTopicMatchStatus.MATCHED, matcher.getStatus());
  }

  @Test
  public void testMatches_OneCantMatch() {
    List<StubWebsocketMessageTopicMatcher> matchers = List.of(
        new StubWebsocketMessageTopicMatcher(WebsocketMessageTopicMatchStatus.MATCHED),
        new StubWebsocketMessageTopicMatcher(WebsocketMessageTopicMatchStatus.MATCHED));
    AndWebsocketMessageTopicMatcher matcher = new AndWebsocketMessageTopicMatcher(
        new ArrayList<WebsocketMessageTopicMatcher>(matchers));
    matchers.get(1).setStatus(WebsocketMessageTopicMatchStatus.MATCHED);
    assertEquals(WebsocketMessageTopicMatchStatus.NO_MATCH, matcher.matches("field", "value"));
    assertEquals(WebsocketMessageTopicMatchStatus.NO_MATCH, matcher.getStatus());
    assertEquals(WebsocketMessageTopicMatchStatus.NO_MATCH, matchers.get(0).getStatus());
    assertEquals(WebsocketMessageTopicMatchStatus.MATCHED, matchers.get(1).getStatus());
  }

  @Test
  public void testMatches_CantMatchIsFinalState() {
    List<StubWebsocketMessageTopicMatcher> matchers = List.of(
        new StubWebsocketMessageTopicMatcher(WebsocketMessageTopicMatchStatus.MATCHED),
        new StubWebsocketMessageTopicMatcher(WebsocketMessageTopicMatchStatus.MATCHED));
    AndWebsocketMessageTopicMatcher matcher = new AndWebsocketMessageTopicMatcher(
        new ArrayList<WebsocketMessageTopicMatcher>(matchers));
    matchers.get(0).setStatus(WebsocketMessageTopicMatchStatus.CANT_MATCH);
    assertEquals(WebsocketMessageTopicMatchStatus.CANT_MATCH, matcher.matches("field", "value"));
    assertEquals(WebsocketMessageTopicMatchStatus.CANT_MATCH, matcher.getStatus());
    // Set all matchers to MATCHED
    matchers.forEach(m -> m.setStatus(WebsocketMessageTopicMatchStatus.MATCHED));
    // The matcher remains in CANT_MATCH state
    assertEquals(WebsocketMessageTopicMatchStatus.CANT_MATCH, matcher.matches("field", "value"));
    assertEquals(WebsocketMessageTopicMatchStatus.CANT_MATCH, matcher.getStatus());
  }

  @Test
  public void testMatches_NotYetMatched() {
    List<StubWebsocketMessageTopicMatcher> matchers = List.of(
        new StubWebsocketMessageTopicMatcher(WebsocketMessageTopicMatchStatus.MATCHED),
        new StubWebsocketMessageTopicMatcher(WebsocketMessageTopicMatchStatus.MATCHED));
    AndWebsocketMessageTopicMatcher matcher = new AndWebsocketMessageTopicMatcher(
        new ArrayList<WebsocketMessageTopicMatcher>(matchers));
    assertEquals(WebsocketMessageTopicMatchStatus.NO_MATCH, matcher.getStatus());
    for (StubWebsocketMessageTopicMatcher m : matchers) {
      assertEquals(WebsocketMessageTopicMatchStatus.NO_MATCH, m.getStatus());
    }
    assertEquals(WebsocketMessageTopicMatchStatus.NO_MATCH, matcher.matches("field", "value"));
  }

  @Test
  public void testMatches_TransitionToMatched() {
    StubWebsocketMessageTopicMatcher first = new StubWebsocketMessageTopicMatcher();
    StubWebsocketMessageTopicMatcher second = new StubWebsocketMessageTopicMatcher();
    AndWebsocketMessageTopicMatcher matcher = new AndWebsocketMessageTopicMatcher(List.of(first, second));

    second.setStatus(WebsocketMessageTopicMatchStatus.MATCHED);
    // Initially NO_MATCH, since first matcher is NO_MATCH (reset during init of AND
    // matcher)
    assertEquals(WebsocketMessageTopicMatchStatus.NO_MATCH, matcher.matches("field", "value"));

    // Transition first matcher to MATCHED
    first.setStatus(WebsocketMessageTopicMatchStatus.MATCHED);
    assertEquals(WebsocketMessageTopicMatchStatus.MATCHED, matcher.matches("field", "value"));
    second.setStatus(WebsocketMessageTopicMatchStatus.NO_MATCH);
  }

  @Test
  public void testReset() {
    List<StubWebsocketMessageTopicMatcher> matchers = List.of(new StubWebsocketMessageTopicMatcher(),
        new StubWebsocketMessageTopicMatcher());
    AndWebsocketMessageTopicMatcher matcher = new AndWebsocketMessageTopicMatcher(
        new ArrayList<WebsocketMessageTopicMatcher>(matchers));
    matchers.forEach(m -> m.setStatus(WebsocketMessageTopicMatchStatus.MATCHED));
    assertEquals(WebsocketMessageTopicMatchStatus.MATCHED, matcher.matches("field", "value"));

    // Reset and verify state
    matcher.reset();
    assertEquals(WebsocketMessageTopicMatchStatus.NO_MATCH, matcher.getStatus());
    for (StubWebsocketMessageTopicMatcher m : matchers) {
      assertEquals(WebsocketMessageTopicMatchStatus.NO_MATCH, m.getStatus());
    }
  }
}
