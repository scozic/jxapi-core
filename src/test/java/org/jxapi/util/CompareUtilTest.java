package org.jxapi.util;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link CompareUtil}.
 */
public class CompareUtilTest {

  @Test
  public void testCompare() {
    Assert.assertEquals(0, CompareUtil.compare(null, null));
    Assert.assertEquals(-1, CompareUtil.compare(null, "a"));
    Assert.assertEquals(1, CompareUtil.compare("a", null));
    Assert.assertEquals(0, CompareUtil.compare("a", "a"));
    Assert.assertEquals(-1, CompareUtil.compare("a", "b"));
    Assert.assertEquals(1, CompareUtil.compare("b", "a"));
  }

  @Test
  public void testCompareLists() {
    Assert.assertEquals(0, CompareUtil.compareLists(null, null, null));
    Assert.assertEquals(-1, CompareUtil.compareLists(null, List.of("a"), CompareUtil::compare));
    Assert.assertEquals(1, CompareUtil.compareLists(List.of("a"), null, CompareUtil::compare));
    Assert.assertEquals(0, CompareUtil.compareLists(List.of("a"), List.of("a"), CompareUtil::compare));
    Assert.assertEquals(-1, CompareUtil.compareLists(List.of("a"), List.of("b"), CompareUtil::compare));
    Assert.assertEquals(1, CompareUtil.compareLists(List.of("b"), List.of("a"), CompareUtil::compare));
    Assert.assertEquals(1, CompareUtil.compareLists(List.of("a", "b"), List.of("a"), CompareUtil::compare));
    Assert.assertEquals(-1,  CompareUtil.compareLists(List.of("a"), List.of("a", "b"), CompareUtil::compare));
    Assert.assertEquals(0, CompareUtil.compareLists(List.of("a", "b"), List.of("a", "b"), CompareUtil::compare));
    Assert.assertEquals(-1,  CompareUtil.compareLists(List.of("a", "a"), List.of("a", "b"), CompareUtil::compare));
    Assert.assertEquals(1, CompareUtil.compareLists(List.of("a", "b"), List.of("a", "a"), CompareUtil::compare));
  }

  @Test
  public void testCompareMaps() {
    Map<String, String> map1 = new LinkedHashMap<>();
    Map<String, String> map2 = new LinkedHashMap<>();
    Assert.assertEquals(0, CompareUtil.compareMaps(null, null, null));
    Assert.assertEquals(-1, CompareUtil.compareMaps(null, map2, CompareUtil::compare));
    Assert.assertEquals(1, CompareUtil.compareMaps(map1, null, CompareUtil::compare));
    Assert.assertEquals(0, CompareUtil.compareMaps(map1, map2, CompareUtil::compare));
    map1.put("a", "1");
    Assert.assertEquals(1, CompareUtil.compareMaps(map1, map2, CompareUtil::compare));
    map2.put("a", "1");
    Assert.assertEquals(0, CompareUtil.compareMaps(map1, map2, CompareUtil::compare));
    map1.put("b", "2");
    Assert.assertEquals(1, CompareUtil.compareMaps(map1, map2, CompareUtil::compare));
    map2.put("b", "2");
    Assert.assertEquals(0, CompareUtil.compareMaps(map1, map2, CompareUtil::compare));
    map1.put("c", "3");
    map2.put("d", "4");
    Assert.assertEquals(1, CompareUtil.compareMaps(map1, map2, CompareUtil::compare));
  }
  
  @Test
  public void testCompareObjects() {
      // Both null
      Assert.assertEquals(0, CompareUtil.compareObjects(null, null));

      // First null
      Assert.assertEquals(-1, CompareUtil.compareObjects(null, "test"));

      // Second null
      Assert.assertEquals(1, CompareUtil.compareObjects("test", null));

      // Same object reference
      Object obj = new Object();
      Assert.assertEquals(0, CompareUtil.compareObjects(obj, obj));

      // Equal objects
      Assert.assertEquals(0, CompareUtil.compareObjects("test", new String("test")));

      // Different class names
      Assert.assertEquals(10, CompareUtil.compareObjects("test", 123));
      Assert.assertEquals(-10, CompareUtil.compareObjects(123, "test"));

      // Different string representations
      Assert.assertEquals(-23, CompareUtil.compareObjects("abc", "xyz"));
      Assert.assertEquals(23, CompareUtil.compareObjects("xyz", "abc"));
  }

}
