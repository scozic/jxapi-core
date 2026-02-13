package org.jxapi.util;

import java.util.Properties;

/**
 * Interface for objects that have properties. Proxies the {@link Properties}
 */
public interface HasProperties {

  /**
   * Returns the properties of the object.
   * 
   * @return the properties
   */
  Properties getProperties();
}
