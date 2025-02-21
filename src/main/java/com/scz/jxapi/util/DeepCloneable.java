package com.scz.jxapi.util;

/**
 * Interface for objects that can be deep cloned.
 * <p>
 * This is interface is implemented in particular by generated POJOs.
 *
 * @param <T>
 */
public interface DeepCloneable<T> {
	
	/**
	 * Create a deep clone of this object.
	 * This means that a distinct instance with all fields of this object deep cloned.
	 * 
	 * @return A deep clone of this object.
	 */
	T deepClone();

}
