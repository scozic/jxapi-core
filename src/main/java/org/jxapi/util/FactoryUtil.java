package org.jxapi.util;

import java.lang.reflect.InvocationTargetException;

/**
 * Helper methods for factory classes.
 */
public class FactoryUtil {
  
  private FactoryUtil() {}

  /**
   * Factory method to instantiate a given class from its
   * class.
   * 
   * @param cls Full name of class to instantiate. Should have a public constructor with no argument.
   * @param <T> Type of class to instantiate
   * @return New instance of that class
   * @throws IllegalArgumentException If provided class cannot be instantiated by
   *                                  reflection or does not provide a default
   *                                  constructor.
   */
  @SuppressWarnings("unchecked")
  public static <T> T fromClassName(String cls) {
    try {
      return (T) Class.forName(cls).getConstructor().newInstance();
    } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
        | NoSuchMethodException | SecurityException | ClassNotFoundException  |NullPointerException e) {
      throw new IllegalArgumentException("Failed to instantiate " 
                        + cls + 
                        " implementation '" + cls + "'.",
                        e);
    }
  }

}
