package org.jxapi.netutils.rest;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link FutureRestResponse}
 */
public class FutureRestResponseTest {

  @Test
  public void testNew() {
    Assert.assertNotNull(new FutureRestResponse<>());
  }
}
