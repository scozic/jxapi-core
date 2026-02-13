package org.jxapi.netutils.rest;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link FutureHttpResponse}
 */
public class FutureHttpResponseTest {
  
  @Test
  public void testNew() {
    Assert.assertNotNull(new FutureHttpResponse());
  }

}
