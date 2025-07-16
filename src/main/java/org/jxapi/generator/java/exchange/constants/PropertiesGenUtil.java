package org.jxapi.generator.java.exchange.constants;

import org.jxapi.exchange.descriptor.ConfigPropertyDescriptor;
import org.jxapi.util.ConfigProperty;
import org.jxapi.util.DefaultConfigProperty;

/**
 * Helper methods around configuration properties related code generation.
 */
public class PropertiesGenUtil {

  private PropertiesGenUtil() {}
  
  /**
   * Creates a {@link ConfigProperty} from a given
   * {@link ConfigPropertyDescriptor} instance.
   * 
   * @param propertyDescriptor The property descriptor to convert.
   * @return The converted ConfigProperty, or null if the propertyDescriptor is
   *         null.
   * @throws IllegalArgumentException if the propertyDescriptor is a group. A
   *                                  {@link ConfigProperty} represents a single
   *                                  configuration property, not a group.
   */
  public static ConfigProperty asConfigProperty(ConfigPropertyDescriptor propertyDescriptor) {
    if (propertyDescriptor == null) {
      return null;
    }
    if (propertyDescriptor.isGroup()) {
      throw new IllegalArgumentException("Property '" + propertyDescriptor.getName() + "' is a group, not a property");
    }
    return DefaultConfigProperty.create(
        propertyDescriptor.getName(),
        propertyDescriptor.getType(),
        propertyDescriptor.getDescription(),
        propertyDescriptor.getDefaultValue());
  }
  
  /**
   * Creates a {@link ConfigPropertyDescriptor} matching the given
   * {@link ConfigProperty} instance.
   * 
   * @param configProperty The {@link ConfigProperty} to convert.
   * @return a {@link ConfigPropertyDescriptor} matching the given
   *         {@link ConfigProperty} instance or <code>null</code> if input is
   *         <code>null</code>
   */
  public static ConfigPropertyDescriptor asConfigPropertyDescriptor(ConfigProperty configProperty) {
    if (configProperty == null) {
      return null;
    }
    return ConfigPropertyDescriptor.create(
        configProperty.getName(),
        configProperty.getType(),
        configProperty.getDescription(),
        configProperty.getDefaultValue());
  }

}
