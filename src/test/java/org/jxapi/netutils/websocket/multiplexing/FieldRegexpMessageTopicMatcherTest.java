package org.jxapi.netutils.websocket.multiplexing;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Unit test for {@link FieldRegexpWebsocketMessageTopicMatcher}
 */
public class FieldRegexpMessageTopicMatcherTest {

  @Test
  public void testMatchSuccess() {
      FieldRegexpWebsocketMessageTopicMatcher matcher = 
          new FieldRegexpWebsocketMessageTopicMatcher("fieldName", "value.*");
      WebsocketMessageTopicMatchStatus status = matcher.matches("fieldName", "value123");
      assertEquals(WebsocketMessageTopicMatchStatus.MATCHED, status);
  }

  @Test
  public void testMatchFailure() {
      FieldRegexpWebsocketMessageTopicMatcher matcher = 
          new FieldRegexpWebsocketMessageTopicMatcher("fieldName", "value.*");
      WebsocketMessageTopicMatchStatus status = matcher.matches("fieldName", "otherValue");
      assertEquals(WebsocketMessageTopicMatchStatus.CANT_MATCH, status);
  }

  @Test
  public void testNoMatchForDifferentFieldName() {
      FieldRegexpWebsocketMessageTopicMatcher matcher = 
          new FieldRegexpWebsocketMessageTopicMatcher("fieldName", "value.*");
      WebsocketMessageTopicMatchStatus status = matcher.matches("otherField", "value123");
      assertEquals(WebsocketMessageTopicMatchStatus.NO_MATCH, status);
  }

  @Test
  public void testNullValue() {
      FieldRegexpWebsocketMessageTopicMatcher matcher = 
          new FieldRegexpWebsocketMessageTopicMatcher("fieldName", "value.*");
      WebsocketMessageTopicMatchStatus status = matcher.matches("fieldName", null);
      assertEquals(WebsocketMessageTopicMatchStatus.CANT_MATCH, status);
  }

  @Test
  public void testTerminalStatus() {
      FieldRegexpWebsocketMessageTopicMatcher matcher = 
          new FieldRegexpWebsocketMessageTopicMatcher("fieldName", "value.*");
      matcher.matches("fieldName", "value123"); // First match sets status to MATCHED
      WebsocketMessageTopicMatchStatus status = matcher.matches("fieldName", "value456");
      assertEquals(WebsocketMessageTopicMatchStatus.MATCHED, status); // Status remains terminal
  }

}
