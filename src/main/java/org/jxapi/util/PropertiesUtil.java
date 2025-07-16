package org.jxapi.util;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

/**
 * Helper methods around properties management.
 */
public class PropertiesUtil {

  private PropertiesUtil() {}
  
  /**
   * Filters the properties by namespace, for instance "namespace.key=value".
   * 
   * @param source          The source properties
   * @param namespace       The namespace to filter for instance "namespace"
   * @param removeNamespace If true, the namespace is removed from the keys
   * @return new Properties instance containing only the properties with the given
   *         namespace. The namespace can be removed from the keys if
   *         <code>removeNamespace</code> is <code>true</code>.
   */
  public static Properties filterProperties(Properties source, 
                        String namespace, 
                        boolean removeNamespace) {
    if (source == null) {
      return null;
    }
    Properties res = new Properties();
    String prefix = namespace + ".";
    source.forEach((key, value) -> {
      if (key instanceof String) {
        String k = key.toString();
        if (k.startsWith(prefix)) {
          if (removeNamespace) {
            k = StringUtils.substringAfter(k, prefix);
          }
          res.put(k, value);
        }
      }
    });
    return res;
    
  }
  
  /**
   * Retrieves a property as a string from a properties instance.
   * 
   * @param properties   The properties instance
   * @param key          The property key
   * @param defaultValue The default value (can be <code>null</code>)
   * @return The property value as a string or the default value if the property
   *         is not found
   */
  public static String getString(Properties properties, String key, Object defaultValue) {
    Object v = Optional.ofNullable(properties.get(key)).orElse(defaultValue);
    if (v != null) {
      return v.toString();
    }
    return null;
  }
  
  /**
   * Retrieves a property as a string from a properties instance.
   * 
   * @param properties   The properties instance
   * @param property     The property
   * @return The property value as a string or the default value if the property is not found in <code>properties</code>.
   */
  public static String getString(Properties properties, ConfigProperty property) {
    return getString(properties, property.getName(), property.getDefaultValue());
  }
  
  /**
   * Retrieves a property as an integer from a properties instance.
   * 
   * @param properties   The properties instance
   * @param key          The property key
   * @param defaultValue The default value (can be <code>null</code>)
   * @return The property value as an integer or the default value if the property
   *         is not found
   */
  public static Integer getInt(Properties properties, String key, Object defaultValue) {
    Object v = Optional.ofNullable(properties.get(key)).orElse(defaultValue);
    if (v != null) {
      return Integer.valueOf(v.toString());
    }
    return null;
  }
  
  /**
   * Retrieves a property as an integer from a properties instance.
   * 
   * @param properties The properties instance
   * @param property   The property
   * @return The property value as an integer or the default value if the property
   *         is not found in <code>properties</code>.
   */
  public static Integer getInt(Properties properties, ConfigProperty property) {
    return getInt(properties, property.getName(), property.getDefaultValue());
  }
  
  /**
   * Retrieves a property as a long from a properties instance.
   * 
   * @param properties   The properties instance
   * @param key          The property key
   * @param defaultValue The default value (can be <code>null</code>)
   * @return The property value as a long or the default value if the property is
   *         not found
   */
  public static Long getLong(Properties properties, String key, Object defaultValue) {
    Object v = Optional.ofNullable(properties.get(key)).orElse(defaultValue);
    if (v != null) {
      if("now()".equals(v)) {
        return Long.valueOf(System.currentTimeMillis());
      }
      return Long.valueOf(v.toString());
    }
    return null;
  }
  
  /**
   * Retrieves a property as a long from a properties instance.
   * 
   * @param properties The properties instance
   * @param property   The property
   * @return The property value as a long or the default value if the property is
   *         not found in <code>properties</code>.
   */
  public static Long getLong(Properties properties, ConfigProperty property) {
    return getLong(properties, property.getName(), property.getDefaultValue());
  }
  
  /**
   * Retrieves a property as a big decimal from a properties instance.
   * 
   * @param properties   The properties instance
   * @param key          The property key
   * @param defaultValue The default value (can be <code>null</code>)
   * @return The property value as a big decimal or the default value if the
   *         property is not found
   */
  public static BigDecimal getBigDecimal(Properties properties, String key, Object defaultValue) {
    Object v = Optional.ofNullable(properties.get(key)).orElse(defaultValue);
    if (v != null) {
      return new BigDecimal(v.toString());
    }
    return null;
  }
  
  /**
   * Retrieves a property as a big decimal from a properties instance.
   * 
   * @param properties The properties instance
   * @param property   The property
   * @return The property value as a big decimal or the default value if the
   *         property is not found in <code>properties</code>.
   */
  public static BigDecimal getBigDecimal(Properties properties, ConfigProperty property) {
    return getBigDecimal(properties, property.getName(), property.getDefaultValue());
  }
  
  /**
   * Retrieves a property as a boolean from a properties instance.
   * 
   * @param properties   The properties instance
   * @param key          The property key
   * @param defaultValue The default value (can be <code>null</code>)
   * @return The property value as a boolean or the default value if the property
   *         is not found
   */
  public static Boolean getBoolean(Properties properties, String key, Object defaultValue) {
    Object v = Optional.ofNullable(properties.get(key)).orElse(defaultValue);
    if (v != null) {
      return Boolean.valueOf(v.toString());
    }
    return null;
  }
  
  /**
   * Retrieves a property as a boolean from a properties instance.
   * 
   * @param properties The properties instance
   * @param property   The property
   * @return The property value as a boolean or the default value if the property
   *         is not found in <code>properties</code>.
   */
  public static Boolean getBoolean(Properties properties, ConfigProperty property) {
    return getBoolean(properties, property.getName(), property.getDefaultValue());
  }

}
