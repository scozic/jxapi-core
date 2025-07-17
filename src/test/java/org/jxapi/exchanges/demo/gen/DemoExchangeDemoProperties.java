package org.jxapi.exchanges.demo.gen;

import java.util.List;
import java.util.Properties;

import javax.annotation.processing.Generated;
import org.jxapi.exchange.descriptor.Type;
import org.jxapi.util.CollectionUtil;
import org.jxapi.util.ConfigProperty;
import org.jxapi.util.DefaultConfigProperty;
import org.jxapi.util.PropertiesUtil;

/**
 * Configurable properties for <strong>DemoExchange</strong> exchange:<br>
 * <table>
 *   <caption>DemoExchange properties</caption>
 *   <tr>
 *     <th>Name</th>
 *     <th>Type</th>
 *     <th>Description</th>
 *     <th>Default value</th>
 *   </tr>
 *   <tr>
 *     <td>demoSymbol</td>
 *     <td>STRING</td>
 *     <td>Default value to use for market 'symbol' parameter in demo snippets</td>
 *     <td>BTC_USDT</td>
 *   </tr>
 * </table>
 * <br>
 * Exposes helper methods are available to retrieve value of each of these properties with right type, returning default value if not present in properties.
 * @see ConfigProperty
 */
@Generated("org.jxapi.generator.java.exchange.properties.PropertiesClassGenerator")
public class DemoExchangeDemoProperties {
  
  private DemoExchangeDemoProperties(){}
  
  /**
   * Default value to use for market 'symbol' parameter in demo snippets
   */
  public static final ConfigProperty DEMO_SYMBOL = DefaultConfigProperty.create(
    "demoSymbol",
    Type.STRING,
    "Default value to use for market 'symbol' parameter in demo snippets",
    "BTC_USDT");
  
  /**
   * Retrieves value of 'demoSymbol' property.
   * @param properties Properties to look for value of 'demoSymbol' property into.
   * @return Value found in properties or default value 'BTC_USDT' if not found.
   */
  public static String getDemoSymbol(Properties properties) {return PropertiesUtil.getString(properties, DEMO_SYMBOL);}
  /**
   * List of all configuration properties defined in this class
   */
  public static final List<ConfigProperty> ALL = List.copyOf(CollectionUtil.mergeLists(List.of(
    List.of(
      DEMO_SYMBOL))));
}
