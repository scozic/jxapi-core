package org.jxapi.util;

import org.junit.Assert;
import org.junit.Test;


/**
 * Unit test for {@link DefaultDisposable}
 */
public class DefaultDisposableTest {

  @Test
  public void testDispose() {
    TestDisposable td = new TestDisposable();
    Assert.assertEquals(0, td.doDisposeCallCount);
    Assert.assertFalse(td.isDisposed());
    // Call to checkNotDisposed when not disposed should not trigger exception
    td.checkNotDisposed();
    
    td.dispose();
    Assert.assertEquals(1, td.doDisposeCallCount);
    Assert.assertTrue(td.isDisposed());
    
    // 2nd call to dispose() should not trigger call to doDispose()
    td.dispose();
    Assert.assertEquals(1, td.doDisposeCallCount);
    Assert.assertTrue(td.isDisposed());
  }
  
  @Test(expected = IllegalStateException.class)
  public void testCheckNotDisposedThrows() {
    TestDisposable td = new TestDisposable();
    td.dispose();
    td.checkNotDisposed();
  }
  
  private class TestDisposable extends DefaultDisposable {
    
    private int doDisposeCallCount = 0;
    
    /**
     * 
     */
    @Override
    protected void doDispose() {
      doDisposeCallCount++;
    }
    
    /**
     * 
     */
    @Override
    public void checkNotDisposed() {
      super.checkNotDisposed();
    }
  }
}
