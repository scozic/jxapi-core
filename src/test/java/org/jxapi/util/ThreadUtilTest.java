package org.jxapi.util;

import java.util.concurrent.ThreadFactory;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link ThreadUtil}
 */
public class ThreadUtilTest {

  @Test
  public void testCreateNamePrefixThreadFactory() {
    ThreadFactory fac = ThreadUtil.createNamePrefixThreadFactory("MYPOOL");
    Assert.assertNotNull(fac);
    Thread t = fac.newThread(() -> {});
    Assert.assertNotNull(t);
    Assert.assertTrue(t.getName().startsWith("MYPOOL"));
  }
}
