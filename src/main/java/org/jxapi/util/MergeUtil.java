package org.jxapi.util;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Utility class for merging values.
 */
public class MergeUtil {

  private MergeUtil() {}

  /**
   * Merges two lists of values of the same type. Values are assumed not to be 'mergeable'.
   * The result is a new list
   * containing all the values of the two input lists. If an item with the same
   * identifier (name) is present in both lists, an exception is thrown.
   * 
   * @param <T>         Type of the values
   * @param context     Context of the values (used in exception message)
   * @param e1          First list of values
   * @param e2          Second list of values
   * @param idExtractor Function to extract the identifier (name) of the values
   * @return A new list containing all the values of the two input lists
   * @throws IllegalArgumentException if an item with the same identifier (name)
   *                                  is present in both lists
   */
  public static <T> List<T> mergeLists(String context, List<T> e1, List<T> e2, Function<T, String> idExtractor) {
    return mergeLists(context, e1, e2, idExtractor, (item1, item2) -> {
		  throw new IllegalArgumentException(String.format("Conflict: Found duplicate item for %s with id:%s", context, idExtractor.apply(item1)));
		});
  }
  
  /**
   * Merges two lists of values of the same type. Values are assumed to be 'mergeable'.
   * The result is a new list
   * containing all the values of the two input lists. If an item with the same
   * identifier (name) is present in both lists, the merger function is used to
   * merge the two items.
   * 
   * @param <T>         Type of the values
   * @param context     Context of the values (used in exception message)
   * @param e1          First list of values
   * @param e2          Second list of values
   * @param idExtractor Function to extract the identifier (name) of the values
   * @param merger      Function to merge two items with the same identifier
   * @return A new unmodifiable list containing all the values of the two input lists
   */
  public static <T> List<T> mergeLists(
      String context,
      List<T> e1,
      List<T> e2,
      Function<T, String> idExtractor,
      BinaryOperator<T> merger) {

  if (e1 == null && e2 == null) {
      return List.of();
  }
  if (e1 == null) {
      return List.copyOf(e2);
  }
  if (e2 == null) {
      return List.copyOf(e1);
  }

  Map<String, T> merged = new LinkedHashMap<>();

  for (T item : Stream.concat(e1.stream(), e2.stream()).toList()) {
    String id = idExtractor.apply(item);
      
      T existing = merged.get(id);

      if (existing == null) {
          merged.put(id, item);
      } else {
        try {
          merged.put(id, merger.apply(existing, item));
        }  catch (IllegalArgumentException ex) {
          throw new IllegalArgumentException(String.format(
              "Error merging items with id:%s for %s: %s", id, context, ex.getMessage()), 
              ex);
        }
      }
  }

  return List.copyOf(merged.values());
}


  /**
   * Merges values of properties holding positive longs, assuming a negative value
   * means 'unset'. If the values are equal, the result is the value. If the
   * values are different, an exception is thrown. If one of the value is &lt; 0,
   * the other value is returned. If one of the value is negative, the other value
   * is returned.
   * 
   * @param context Context of the values (used in exception message)
   * @param e1      First value
   * @param e2      Second value
   * @return The merged value
   * @throws IllegalArgumentException if the values are not <code>null</code> and
   *                                  different
   */
  public static Long mergePositiveLongs(String context, Long e1, Long e2) {
    if (e1 == null) {
      return e2;
    }
    if (e2 == null) {
      return e1;
    }
    if (e1.equals(e2)) {
      return e1;
    }
    if (e1.longValue() < 0) {
      return e2;
    }
    if (e2.longValue() < 0) {
      return e1;
    }
    throw new IllegalArgumentException(String.format("Conflict: Found distinct values for %s:[%d] and [%d]", context, e1, e2));
  }

  /**
   * Merges two values of the same type. If the values are equal, the result is
   * the value. If the values are different, an exception is thrown. If one of the
   * value is <code>null</code>, the other value is returned.
   * 
   * @param <T>     Type of the values
   * @param context Context of the values (used in exception message)
   * @param e1      First value
   * @param e2      Second value
   * @return The merged value
   * @throws IllegalArgumentException if the values are not <code>null</code> and different
   */
  public static <T> T merge(String context, T e1, T e2) {
    if (e1 == null) {
      return e2;
    }
    if (e2 == null) {
      return e1;
    }
    if (e1.equals(e2)) {
      return e1;
    }
    throw new IllegalArgumentException(String.format("Conflict: Found distinct values for %s:[%s] and [%s]", context, e1, e2));
  }

}
