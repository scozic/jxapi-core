package org.jxapi.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link PlaceHolderResolver}.
 */
public class PlaceHolderResolverTest {

  @Test
  public void testNoOpResolver() {
    PlaceHolderResolver resolver = PlaceHolderResolver.NO_OP;
    String input = "This is a test string with a ${placeHolder}.";
    String result = resolver.resolve(input);
    Assert.assertEquals(input, result);
  }
  
  @Test
  public void testCreateResolverWithNoArgumentsReturnsNoOp() {
   Assert.assertSame(PlaceHolderResolver.NO_OP, PlaceHolderResolver.create());
  }
  
  @Test
  public void testCreateResolver() {
    Object[] keysAndValues = {"key1", "value1", "key2", "value2"};
    PlaceHolderResolver resolver = PlaceHolderResolver.create(keysAndValues);
    String input = "This is a test string with ${key1} and ${key2}.";
    String expected = "This is a test string with value1 and value2.";
    String result = resolver.resolve(input);
    Assert.assertEquals(expected, result);
  }
}
