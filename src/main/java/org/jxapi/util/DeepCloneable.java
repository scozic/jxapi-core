package org.jxapi.util;

/**
 * Interface for objects that can be deep cloned.
 * <p>
 * This is interface is implemented in particular by generated POJOs.
 *
 * @param <T> The type of the object to deep clone
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
