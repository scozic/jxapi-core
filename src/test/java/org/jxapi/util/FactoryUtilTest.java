package org.jxapi.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link FactoryUtil}
 */
public class FactoryUtilTest {

  @Test
  public void testFromClassName() {
    TestFactory factory = FactoryUtil.fromClassName(TestFactory.class.getName());
    Assert.assertNotNull(factory);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFromClassNameInvalidClass() {
    FactoryUtil.fromClassName("org.jxapi.util.FactoryUtilTest$TestInvalidFactory");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFromClassNameInvalidClassWithNoDefaultConstructor() {
    FactoryUtil.fromClassName(TestInvalidFactoryWithNoDefaultConstructor.class.getName());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFromClassNameNull() {
    FactoryUtil.fromClassName(null);
  }
  
  public static class TestFactory {
  }
  
  public static class TestInvalidFactoryWithNoDefaultConstructor {
    
    public TestInvalidFactoryWithNoDefaultConstructor(int id) {}
  }
}
