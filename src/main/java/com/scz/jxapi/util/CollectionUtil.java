package com.scz.jxapi.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
	 * Return a copy of the list with the same items.
	 * @param <T>
	 * @param list the list to clone
	 * @return a copy of the list with the same items
	 */
	public static <T> List<T> cloneList(List<T> list) {
		if (list == null) {
			return null;
		}
		return new ArrayList<>(list);
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
			clonedList = new ArrayList<>(list.size());
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
		Map<String, T> m = new HashMap<>();
		m.putAll(map);
		return m;
	}
	
	/**
	 * Return a deep copy of the map with items from input map deep cloned using the provided function.
	 * @param <T> The type of values in the map
	 * @param map the map to deep clone
	 * @param itemDeepCloneFunction the function to deep clone the values
	 * @return
	 */
	public static <T> Map<String, T> deepCloneMap(Map<String, T> map, UnaryOperator<T> itemDeepCloneFunction) {
		Map<String, T> clonedMap = null;
		if (map != null) {
			clonedMap = new HashMap<>(map.size());
			for (Entry<String, T> entry : map.entrySet()) {
				clonedMap.put(entry.getKey(), itemDeepCloneFunction.apply(entry.getValue()));
			}
		}
		return clonedMap;
	}
	
	public static <T> List<T> mergeLists(List<T> list1, List<T> list2) {
		List<T> merged = new ArrayList<>();
		if (list1 != null) {
			merged.addAll(list1);
		}
		if (list2 != null) {
			merged.addAll(list2);
		}
		return merged;
	}
}
