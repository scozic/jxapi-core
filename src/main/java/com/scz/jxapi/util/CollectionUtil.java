package com.scz.jxapi.util;

import java.util.Collection;

/**
 * Helper static methods around {@link Collection}
 */
public class CollectionUtil {

	private CollectionUtil() {}
	
	/**
	 * @param collection
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
