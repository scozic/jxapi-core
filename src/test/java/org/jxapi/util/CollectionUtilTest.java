package org.jxapi.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link CollectionUtil}
 */
public class CollectionUtilTest {

  @Test
  public void testIsEmpty() {
      Assert.assertTrue(CollectionUtil.isEmpty(null));
      Assert.assertTrue(CollectionUtil.isEmpty(Collections.emptyList()));
      Assert.assertFalse(CollectionUtil.isEmpty(Collections.singletonList("a")));
  }
  
  @Test
  public void testIsEmptyMap() {
    Assert.assertTrue(CollectionUtil.isEmptyMap(null));
    Assert.assertTrue(CollectionUtil.isEmptyMap(Collections.emptyMap()));
    Assert.assertFalse(CollectionUtil.isEmptyMap(Map.of("a", "b")));
  }
  
  @Test
  public void testEmptyIfNull() {
    Assert.assertTrue(CollectionUtil.emptyIfNull(null).isEmpty());
    List<String> l = List.of("a");
    Assert.assertSame(l, CollectionUtil.emptyIfNull(l));
  }
  
  @Test
  public void testCloneList_NullList() {
    Assert.assertNull(CollectionUtil.cloneList(null));
  }
  
  @Test
  public void testCloneList() {
    Assert.assertEquals(Collections.emptyList(), CollectionUtil.cloneList(Collections.emptyList()));
    Assert.assertEquals(Collections.singletonList("a"), CollectionUtil.cloneList(Collections.singletonList("a")));
  }
  
  @Test
  public void testDeepCloneList_NullList() {
    Assert.assertNull(CollectionUtil.deepCloneList(null, null));
  }
  
  @Test
  public void testDeepCloneList() {
    Assert.assertEquals(Collections.emptyList(), CollectionUtil.deepCloneList(Collections.emptyList(), null));
    List<Item> toClone = List.of(new Item("a"), new Item("b"));
    List<Item> cloned = CollectionUtil.deepCloneList(toClone, item -> new Item(item.value));
    Assert.assertEquals(toClone.size(), cloned.size());
    for (int i = 0; i < toClone.size(); i++) {
      Assert.assertNotSame(toClone.get(i), cloned.get(i));
      Assert.assertEquals(toClone.get(i), cloned.get(i));
    }
  }
  
  @Test
  public void testClone_NullMap() {
    Assert.assertNull(CollectionUtil.cloneMap(null));
  }
  
  @Test
  public void testCloneMap() {
    Assert.assertNull(CollectionUtil.deepCloneMap(null, null));
    Map<String, String> toClone = Map.of("a", "aval", "b", "bval");
    Map<String, String> cloned = CollectionUtil.cloneMap(toClone);
    Assert.assertEquals(toClone.size(), cloned.size());
    for (Map.Entry<String, String> entry : toClone.entrySet()) {
      Assert.assertEquals(entry.getValue(), cloned.get(entry.getKey()));
    }
  }
  
  @Test
  public void testDeepCloneMap_NullMap() {
    Assert.assertNull(CollectionUtil.deepCloneMap(null, null));
  }
  
  @Test
  public void testDeepCloneMap() {
    Assert.assertEquals(Collections.emptyMap(), CollectionUtil.deepCloneMap(Collections.emptyMap(), null));
        Map<String, Item> toClone = Map.of("a", new Item("a"), "b", new Item("b"));
        Map<String, Item> cloned = CollectionUtil.deepCloneMap(toClone, item -> new Item(item.value));
        Assert.assertEquals(toClone.size(), cloned.size());
    for (Map.Entry<String, Item> entry : toClone.entrySet()) {
      Assert.assertNotSame(entry.getValue(), cloned.get(entry.getKey()));
      Assert.assertEquals(entry.getValue(), cloned.get(entry.getKey()));
    }
  }
  
  @Test
  public void testMergeLists() {
    Assert.assertTrue(CollectionUtil.mergeLists(null, null).isEmpty());
    List<String> list1 = List.of("a", "b");
    Assert.assertEquals(list1, CollectionUtil.mergeLists(list1, null));
    Assert.assertEquals(list1, CollectionUtil.mergeLists(null, list1));
    List<String> list2 = List.of("c", "d");
    Assert.assertEquals(List.of("a", "b", "c", "d"), CollectionUtil.mergeLists(list1, list2));
    
    Assert.assertTrue(CollectionUtil.mergeLists(List.of()).isEmpty());
    List<List<String>> lists = new ArrayList<>();
    lists.add(null);
    lists.add(list1);
    lists.add(list2);
    Assert.assertEquals(List.of("a", "b", "c", "d"), CollectionUtil.mergeLists(lists));
  }
  
  @Test
  public void testCreateList() {
    List<String> list = CollectionUtil.createList();
    Assert.assertTrue(list.isEmpty());
    Assert.assertTrue(list.isEmpty());
    Assert.assertTrue(list instanceof ArrayList);
  }
  
  @Test
  public void testCreateListWithInitialCapacity() {
    List<String> list = CollectionUtil.createList(5);
    Assert.assertTrue(list.isEmpty());
    Assert.assertTrue(list instanceof ArrayList);
  }
  
  @Test
  public void testCreateMap() {
    Assert.assertTrue(CollectionUtil.createMap(0).isEmpty());
    Map<String, String> map = CollectionUtil.createMap(5);
    Assert.assertTrue(map.isEmpty());
    Assert.assertTrue(map instanceof LinkedHashMap);
  }
  
  @Test
  public void testCreateMapWithInitialCapacity() {
    Map<String, String> map = CollectionUtil.createMap(5);
    Assert.assertTrue(map.isEmpty());
    Assert.assertTrue(map instanceof LinkedHashMap);
  }
  
  @Test
  public void testCreateMapWithKeysAndValues() {
    Map<String, Object> map = CollectionUtil.createMap("a", 1, "b", 2);
    Assert.assertEquals(2, map.size());
    Assert.assertEquals(1, map.get("a"));
    Assert.assertEquals(2, map.get("b"));
  }
  
  @Test
  public void testCreateMapWithKeysAndValues_EmptyArgs() {
    Object[] args = {};
    Map<String, Object> map = CollectionUtil.createMap(args);
    Assert.assertTrue(map.isEmpty());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testCreateMapWithKeysAndValues_OddArguments() {
    CollectionUtil.createMap("a");
  }
  
  
  private static class Item {
    String value;
    
    public Item(String value) {
      this.value = value;
    }
    
    public boolean equals(Object other) {
      if (other != null && other instanceof Item) {
        return value.equals(((Item) other).value);
      }
      return false;
    }
  }
}
