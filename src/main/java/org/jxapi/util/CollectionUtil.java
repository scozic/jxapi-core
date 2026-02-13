package org.jxapi.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.UnaryOperator;

/**
 * Helper static methods around {@link Collection}
 */
public class CollectionUtil {

  private CollectionUtil() {}
  
  /**
   * Check if a collection is null or empty.
   * @param collection Collection to check.
   * @return <code>true</code> if collection is null or empty.
   */
  public static boolean isEmpty(Collection<?> collection) {
    if (collection == null) {
      return true;
    }
    return collection.isEmpty();
  }
  
  /**
   * Check if a map is null or empty.
   * 
   * @param map Map to check.
   * @return <code>true</code> if map is null or empty.
   */
  public static boolean isEmptyMap(Map<?, ?> map) {
    if (map == null) {
      return true;
    }
    return map.isEmpty();
  }
  
  /**
   * Returns a non-null list. If the input list is null, an empty non modifiable list is returned.
   * @param list list to check.
   * @param <T> The type of the items in the list
   * @return A non-null list. If the input list is null, an empty non modifiable list is returned. Otherwise, the input list is returned.
   */
  public static <T> List<T> emptyIfNull(List<T> list) {
    if (list == null) {
      return List.of();
    }
    return list;
  }
  
  /**
   * Creates a new modifiable list with default capacity.
   * 
   * @param <T> The type of the items in the list
   * @return A new modifiable list.
   */
  public static <T> List<T> createList() {
    return new ModifiableList<>();
  }
  
  /**
   * Creates a new modifiable list with the given capacity.
   * @param <T> The type of the items in the list
   * @param initialCapacity the initial capacity of the list 
   * @return A new modifiable list with the given capacity.
   */
  public static <T> List<T> createList(int initialCapacity) {
    return new ModifiableList<>(initialCapacity);
  }
  
  /**
   * Creates a new String key modifiable map with default initial capacity.
   * @param <T> The type of the values in the map
   * @return A new modifiable map with the default initial capacity.
   */
  public static <T> Map<String, T> createMap() {
    return new ModifiableMap<>();
  }
  
  
  /**
   * Creates a new modifiable map with given initial capacity.
   * @param <T> The type of the values in the map
   * @param initialCapacity the initial capacity of the map
   * @return A new modifiable map with the given initial capacity.
   */
  public static <T> Map<String, T> createMap(int initialCapacity) {
    return new ModifiableMap<>(initialCapacity);
  }
  
  /**
   * Creates a new modifiable map with the given keys and values.
   * @param keysAndValues alternating keys and values for the map.
   * @return A new modifiable map with the given keys and values.
   * @throws IllegalArgumentException if the keys and values are not in pairs ( <code>keysAndValues.length % 2 != 0</code>).
   */
  public static Map<String, Object> createMap(Object... keysAndValues) {
    Map<String, Object> map = createMap();
    if (keysAndValues.length == 0) {
      return map;
    }
    if (keysAndValues.length % 2 != 0) {
      throw new IllegalArgumentException("Keys and values must be in pairs.");
    }
    for (int i = 0; i < keysAndValues.length; i += 2) {
      map.put(String.valueOf(keysAndValues[i]), keysAndValues[i + 1]);
    }
    return map;
  }
  
  /**
   * Return a copy of the list with the same items.
   * @param <T> The type of the items in the list
   * @param list the list to clone
   * @return a copy of the list with the same items
   */
  public static <T> List<T> cloneList(List<T> list) {
    if (list == null) {
      return null;
    }
    return new ModifiableList<>(list);
  }
  
  /**
   * Return a deep copy of the list with items from input list deep cloned using the provided function.
   * @param <T> The type of the items in the list
   * @param list the list to deep clone
   * @param itemDeepCloneFunction the function to deep clone the items
   * @return A deep copy of the list.
   */
  public static <T> List<T> deepCloneList(List<T> list, UnaryOperator<T> itemDeepCloneFunction) {
    List<T> clonedList = null;
    if (list != null) {
      clonedList = createList(list.size());
      for (T item : list) {
        clonedList.add(itemDeepCloneFunction.apply(item));
      }
    }
    return clonedList;
  }
  
  /**
   * Return a copy of the map with the same items.
   * @param <T> The type of the items in the map
   * @param map the map to clone
   * @return a copy of the map with the same items
   */
  public static <T> Map<String, T> cloneMap(Map<String, T> map) {
    if (map == null) {
      return null;
    }
    Map<String, T> m = createMap(map.size());
    m.putAll(map);
    return m;
  }
  
  /**
   * Return a deep copy of the map with values from input map deep cloned using the provided function.
   * @param <T> The type of values in the map
   * @param map the map to deep clone
   * @param itemDeepCloneFunction the function to deep clone the values
   * @return A deep copy of the map.
   */
  public static <T> Map<String, T> deepCloneMap(Map<String, T> map, UnaryOperator<T> itemDeepCloneFunction) {
    Map<String, T> clonedMap = null;
    if (map != null) {
      clonedMap = createMap(map.size());
      for (Entry<String, T> entry : map.entrySet()) {
        clonedMap.put(entry.getKey(), itemDeepCloneFunction.apply(entry.getValue()));
      }
    }
    return clonedMap;
  }
  
  /**
   * Merge two lists into one.
   * 
   * @param <T>   The type of the items in the lists
   * @param list1 First list
   * @param list2 Second list
   * @return A new list containing all items from both input lists. First list
   *         items are before second list items. <code>null</code> arguments or
   *         list items are ignored.
   */
  public static <T> List<T> mergeLists(List<T> list1, List<T> list2) {
    if (list1 == null && list2 == null) {
      return mergeLists(List.of());
    } else if (list2 == null) {
      return mergeLists(List.of(list1));
    } else if (list1 == null) {
      return mergeLists(List.of(list2));
    }
    return mergeLists(List.of(list1, list2));
  }
  
  /**
   * Merge multiple lists into one.
   * 
   * @param <T>   The type of the items in the lists
   * @param lists A list of lists to merge
   * @return A new unmodifiable list containing all items from all input lists. Items from each
   *         list are added in order.
   */
  public static <T> List<T> mergeLists(List<List<T>> lists) {
    List<T> merged = createList();
    if (isEmpty(lists)) {
      return merged;
    }
    for (List<T> list : lists) {
      if (list != null) {
        merged.addAll(list);
      }
    }
    return merged;
  }
  
  private static class ModifiableMap<K, V> extends LinkedHashMap<K, V> {
    
    private static final long serialVersionUID = -6725437561193347431L;

	public ModifiableMap() {
      super();
    }
    
    public ModifiableMap(int capacity) {
      super(capacity);
    }
    
    @Override
    public String toString() {
      return JsonUtil.pojoToJsonString(this);
    }
  }
  
  private static class ModifiableList<T> extends ArrayList<T> {
    
    private static final long serialVersionUID = 8279767079893684245L;

	public ModifiableList() {
      super();
    }
    
    public ModifiableList(int initialCapacity) {
      super(initialCapacity);
    }
    
    public ModifiableList(List<T> list) {
      super(list);
    }
    
    @Override
    public String toString() {
      return JsonUtil.pojoToJsonString(this);
    }
  }
}
