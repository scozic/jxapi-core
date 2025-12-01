package org.jxapi.util;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

/**
 * Helper methods for object comparison.
 */
public class CompareUtil {

  private CompareUtil() {}
  
  /**
   * Compares two comparable objects that may be null. <code>null</code> is
   * considered less than any other value.
   * 
   * @param o1 First object
   * @param o2 Second object
   * @return 0 if both are <code>null</code>, -1 if o1 is <code>null</code>, 1 if
   *         o2 is <code>null</code>, otherwise the result of
   *         <code>o1.compareTo(o2)</code>
   * @param <T> Type of objects
   */
  public static <T extends Comparable<T>> int compare(T o1, T o2) {
    if (o1 == null) {
            if (o2 != null) {
                return -1;
            }
            return 0;
        } else if (o2 == null) {
            return 1;
        }
        return o1.compareTo(o2);
  }
  
  /**
   * Compares two lists that may be <code>null</code> of comparable objects that
   * may be <code>null</code>.
   * <ul>
   * <li><code>null</code> is considered less than any other value.
   * <li>A list with more elements is considered greater than a list with fewer
   * elements.
   * <li>Elements are compared in order. The first pair of elements that are not
   * equal determines the result.
   * <li>If all elements are equal, the lists are considered equal
   * </ul>
   * 
   * @param <T>        Type of elements in the lists
   * @param list1      First list
   * @param list2      Second list
   * @param comparator Comparator for elements in the lists
   * @return 0 if both are <code>null</code>, -1 if list1 is <code>null</code>, 1
   *         if list2 is <code>null</code>, otherwise the result of comparison.
   */
  public static <T> int compareLists(List<T> list1, List<T> list2, Comparator<T> comparator) {
        if (list1 == null) {
            if (list2 != null) {
                return -1;
            }
            return 0;
        } else if (list2 == null) {
            return 1;
        }
        if (list1.size() != list2.size()) {
            return list1.size() - list2.size();
        }
        for (int i = 0; i < list1.size(); i++) {
            int cmp = comparator.compare(list1.get(i), list2.get(i));
            if (cmp != 0) {
                return cmp;
            }
        }
        return 0;
    }
  
  /**
   * Compares two maps that may be <code>null</code> of comparable objects that
   * may be <code>null></code>.
   * <ul>
   * <li><code>null</code> is considered less than any other value.
   * <li>A map with more entries is considered greater than a map with fewer
   * entries.
   * <li>Entries are compared in order of iteration of the first map. The first
   * value pair of elements that are not equal determines the result.
   * <li>If all elements are equal, the maps are considered equal
   * </ul>
   * This method assumes that the maps are ordered, i.e. the iteration order is
   * consistent.
   * In regular POJOs, this is usually the case as {@link LinkedHashMap} is used
   * to create the maps
   * using JSON deserialization, or nested Builder classes are used to create the
   * maps.
   * 
   * @param <T>        Type of elements in the maps
   * @param map1       First map
   * @param map2       Second map
   * @param comparator Comparator for elements in the maps
   * @return 0 if both are <code>null</code>, -1 if map1 is <code>null</code>, 1
   *         if map2 is <code>null</code>, otherwise the result of comparison.
   */
  public static <T> int compareMaps(Map<String, T> map1, Map<String, T> map2, Comparator<T> comparator) {
    if (map1 == null) {
      if (map2 != null) {
        return -1;
      }
      return 0;
    } else if (map2 == null) {
      return 1;
    }
    if (map1.size() != map2.size()) {
      return map1.size() - map2.size();
    }

    // Remark: LinkedHashMap is used to ensure iteration order is consistent
    Set<Entry<String, T>> entrySet1 = map1.entrySet();
    for (Entry<String, T> entry : entrySet1) {
      int cmp = comparator.compare(entry.getValue(), map2.get(entry.getKey()));
      if (cmp != 0) {
        return cmp;
      }
    }
    return 0;
  }
  
  /**
   * Compares two objects that may be null. Since Objects do not implement
   * {@link Comparable}, the comparison is done:
   * <ul>
   * <li>by checking if both are the same object reference
   * <li>by checking if both are equal using {@link Objects#equals(Object, Object)}
   * <li>by comparing their class names
   * <li>by comparing their string representations using {@link Object#toString()}
   * </ul>
   * 
   * @param o1 The first object
   * @param o2 The second object
   * @return 0 if both are <code>null</code>, -1 if o1 is <code>null</code>, 1 if 
   *         o2 is <code>null</code>, otherwise the result of comparing their string representations.
   */
  public static int compareObjects(Object o1, Object o2) {
    if (o1 == null) {
      return (o2 == null) ? 0 : -1;
    } else if (o2 == null) {
      return 1;
    }
    if (o1 == o2) {
      return 0;
    }
    if (Objects.equals(o1, o2)) {
      return 0;
    }
    int res = o1.getClass().getName().compareTo(o2.getClass().getName());
    if (res != 0) {
      return res;
    }
    return o1.toString().compareTo(o2.toString());
  }

}
