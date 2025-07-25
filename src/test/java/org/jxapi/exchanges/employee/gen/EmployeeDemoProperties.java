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
public class EmployeeDemoProperties {
  
  private EmployeeDemoProperties(){}
  
  /**
   * Used in demo snippets to set as value of Employee 'id' property
   */
  public static final ConfigProperty DEMO_EMPLOYEE_ID = DefaultConfigProperty.create(
    "demoEmployeeId",
    Type.STRING,
    "Used in demo snippets to set as value of Employee 'id' property",
    "1");
  
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
      DEMO_EMPLOYEE_ID))));
}
