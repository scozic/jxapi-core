package com.scz.jxapi.util;

import java.util.Collection;

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
		if (collection.isEmpty()) {
			return true;
		}
		return false;
	}
}
