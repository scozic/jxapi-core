package org.jxapi.util;

import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Function;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link MergeUtil}
 */
public class MergeUtilTest {


  @Test
  public void testMerge() {
    Assert.assertEquals("foo", MergeUtil.merge(null, "foo", null));
    Assert.assertEquals("foo", MergeUtil.merge(null,  null, "foo"));
    Assert.assertNull(MergeUtil.merge(null, null, null));
    Assert.assertEquals("foo", MergeUtil.merge(null,  "foo", "foo"));
    try {
      MergeUtil.merge("property 'hello'", "foo", "bar");
      Assert.fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // expected
      Assert.assertEquals("Conflict: Found distinct values for property 'hello':[foo] and [bar]", e.getMessage());
    }
  }
  
  @Test
  public void testMergePositiveLongs() {
    Assert.assertEquals(Long.valueOf(1L), MergeUtil.mergePositiveLongs(null, 1L, -1L));
    Assert.assertEquals(Long.valueOf(1L), MergeUtil.mergePositiveLongs(null, -1L, 1L));
    Assert.assertEquals(Long.valueOf(1L), MergeUtil.mergePositiveLongs(null, 1L, 1L));
    Assert.assertEquals(Long.valueOf(-1L), MergeUtil.mergePositiveLongs(null, -1L, -1L));
    Assert.assertEquals(Long.valueOf(-2L), MergeUtil.mergePositiveLongs(null, null, -2L));
    Assert.assertEquals(Long.valueOf(-2L), MergeUtil.mergePositiveLongs(null, -2L, null));
    try {
      MergeUtil.mergePositiveLongs("property 'hello'", 1L, 2L);
      Assert.fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // expected
      Assert.assertEquals("Conflict: Found distinct values for property 'hello':[1] and [2]", e.getMessage());
    }
  }
  
  @Test
  public void testMergeLists_NoItemMerger() {
    List<MyType> list1 = List.of(new MyType("foo"), new MyType("bar"));
    List<MyType> list2 = List.of(new MyType("hello"), new MyType("world"));
    List<MyType> list3 = List.of(new MyType("bye"), new MyType("foo"));
    Function<MyType, String> idExtractor = MyType::getValue;
    
    Assert.assertEquals(list1, MergeUtil.mergeLists(null, list1, null, idExtractor));
    Assert.assertEquals(list1, MergeUtil.mergeLists(null, null, list1, idExtractor));
    Assert.assertEquals(List.of(), MergeUtil.mergeLists(null, null, null, null));
    List<MyType> mergedL1L2 = MergeUtil.mergeLists(null, list1, list2, idExtractor);
    Assert.assertEquals(4, mergedL1L2.size());
    Assert.assertEquals("foo", mergedL1L2.get(0).getValue());
    Assert.assertEquals("bar", mergedL1L2.get(1).getValue());
    Assert.assertEquals("hello", mergedL1L2.get(2).getValue());
    Assert.assertEquals("world", mergedL1L2.get(3).getValue());
    
    try {
      MergeUtil.mergeLists("list property 'hello'", list1, list3, idExtractor);
      Assert.fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // expected
      Assert.assertEquals("Error merging items with id:foo for list property 'hello': Conflict: Found duplicate item for list property 'hello' with id:foo", e.getMessage());
    }
    
  }
  
  @Test
  public void testMergeLists_WithItemMerger() {
	    List<MyType> list1 = List.of(new MyType("foo"), new MyType("bar"));
	    List<MyType> list2 = List.of(new MyType("hello"), new MyType("world"));
	    List<MyType> list3 = List.of(new MyType("bye"), new MyType("foo"));
	    Function<MyType, String> idExtractor = MyType::getValue;
	    BinaryOperator<MyType> itemMerger = (item1, item2) -> new MyType(item1 + "_" + item2); // keep first
	    
	    Assert.assertEquals(list1, MergeUtil.mergeLists(null, list1, null, idExtractor, itemMerger));
	    Assert.assertEquals(list1, MergeUtil.mergeLists(null, null, list1, null));
	    Assert.assertEquals(List.of(), MergeUtil.mergeLists(null, null, null, null, null));
	    List<MyType> mergedL1L2 = MergeUtil.mergeLists(null, list1, list2, idExtractor, itemMerger);
	    Assert.assertEquals(4, mergedL1L2.size());
	    Assert.assertEquals("foo", mergedL1L2.get(0).getValue());
	    Assert.assertEquals("bar", mergedL1L2.get(1).getValue());
	    Assert.assertEquals("hello", mergedL1L2.get(2).getValue());
	    Assert.assertEquals("world", mergedL1L2.get(3).getValue());
	    
	    List<MyType> mergedL1L3 = MergeUtil.mergeLists(null, list1, list3, idExtractor, itemMerger);
	    Assert.assertEquals(3, mergedL1L3.size());
	    Assert.assertEquals("'foo'_'foo'", mergedL1L3.get(0).getValue());
	    Assert.assertEquals("bar", mergedL1L3.get(1).getValue());
	    Assert.assertEquals("bye", mergedL1L3.get(2).getValue());
  }
  
  private class MyType {
    String value;

    public MyType(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }
    
    @Override
    public String toString() {
      return "'" + value + "'";
    }
    
  }
}
