package org.jxapi.exchanges.employee.gen;

import java.util.List;
import java.util.Properties;

import javax.annotation.processing.Generated;
import org.jxapi.exchange.descriptor.Type;
import org.jxapi.util.CollectionUtil;
import org.jxapi.util.ConfigProperty;
import org.jxapi.util.DefaultConfigProperty;
import org.jxapi.util.PropertiesUtil;

/**
 * Configurable properties for <strong>Employee</strong> exchange:<br>
 * <table>
 *   <caption>Employee properties</caption>
 *   <tr>
 *     <th>Name</th>
 *     <th>Type</th>
 *     <th>Description</th>
 *     <th>Default value</th>
 *   </tr>
 *   <tr>
 *     <td>baseHttpUrl</td>
 *     <td>STRING</td>
 *     <td>Base URL for REST endpoints the Employee Exchange API</td>
 *     <td></td>
 *   </tr>
 *   <tr>
 *     <td>baseWebsocketUrl</td>
 *     <td>STRING</td>
 *     <td>Base URL for websocket endpoints of the Employee Exchange API</td>
 *     <td></td>
 *   </tr>
 *   <tr>
 *     <td>demoEmployeeId</td>
 *     <td>STRING</td>
 *     <td>Used in demo snippets to set as value of Employee 'id' property</td>
 *     <td>1</td>
 *   </tr>
 * </table>
 * <br>
 * Exposes helper methods are available to retrieve value of each of these properties with right type, returning default value if not present in properties.
 * @see ConfigProperty
 */
@Generated("org.jxapi.generator.java.exchange.properties.PropertiesClassGenerator")
public class EmployeeProperties {
  
  private EmployeeProperties(){}
  
  /**
   * Base URL for REST endpoints the Employee Exchange API
   */
  public static final ConfigProperty BASE_HTTP_URL = DefaultConfigProperty.create(
    "baseHttpUrl",
    Type.STRING,
    "Base URL for REST endpoints the Employee Exchange API",
    null);
  
  /**
   * Base URL for websocket endpoints of the Employee Exchange API
   */
  public static final ConfigProperty BASE_WEBSOCKET_URL = DefaultConfigProperty.create(
    "baseWebsocketUrl",
    Type.STRING,
    "Base URL for websocket endpoints of the Employee Exchange API",
    null);
  
  /**
   * Used in demo snippets to set as value of Employee 'id' property
   */
  public static final ConfigProperty DEMO_EMPLOYEE_ID = DefaultConfigProperty.create(
    "demoEmployeeId",
    Type.STRING,
    "Used in demo snippets to set as value of Employee 'id' property",
    "1");
  
  /**
   * Retrieves value of 'baseHttpUrl' property.
   * @param properties Properties to look for value of 'baseHttpUrl' property into.
   * @return Value found in properties or <code>null</code> if not found.
   */
  public static String getBaseHttpUrl(Properties properties) {return PropertiesUtil.getString(properties, BASE_HTTP_URL);}
  
  /**
   * Retrieves value of 'baseWebsocketUrl' property.
   * @param properties Properties to look for value of 'baseWebsocketUrl' property into.
   * @return Value found in properties or <code>null</code> if not found.
   */
  public static String getBaseWebsocketUrl(Properties properties) {return PropertiesUtil.getString(properties, BASE_WEBSOCKET_URL);}
  
  /**
   * Retrieves value of 'demoEmployeeId' property.
   * @param properties Properties to look for value of 'demoEmployeeId' property into.
   * @return Value found in properties or default value '1' if not found.
   */
  public static String getDemoEmployeeId(Properties properties) {return PropertiesUtil.getString(properties, DEMO_EMPLOYEE_ID);}
  /**
   * List of all configuration properties defined in this class
   */
  public static final List<ConfigProperty> ALL = List.copyOf(CollectionUtil.mergeLists(List.of(
    List.of(
      BASE_HTTP_URL,
      BASE_WEBSOCKET_URL,
      DEMO_EMPLOYEE_ID))));
}
