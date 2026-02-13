package org.jxapi.exchanges.demo.gen;

import java.util.List;
import java.util.Properties;

import javax.annotation.processing.Generated;
import org.jxapi.pojo.descriptor.Type;
import org.jxapi.util.CollectionUtil;
import org.jxapi.util.ConfigProperty;
import org.jxapi.util.DefaultConfigProperty;
import org.jxapi.util.EncodingUtil;
import org.jxapi.util.PropertiesUtil;

/**
 * Configurable demo properties for <strong>DemoExchange</strong> exchange:<br>
 * <table>
 *   <caption>DemoExchange properties</caption>
 *   <tr>
 *     <th>Name</th>
 *     <th>Type</th>
 *     <th>Description</th>
 *     <th>Default value</th>
 *   </tr>
 *   <tr>
 *     <td>MarketData.rest.exchangeInfo.request</td>
 *     <td>STRING</td>
 *     <td>Demo configuration property for exchangeInfo.request field as raw JSON string value.</td>
 *     <td></td>
 *   </tr>
 *   <tr>
 *     <td>MarketData.rest.exchangeInfo.request.symbols</td>
 *     <td>STRING_LIST</td>
 *     <td>Demo configuration property for request.symbols field.<p>
 *     The list of symbol to fetch market information for. Leave empty to fetch all markets</td>
 *     <td>[{@link org.jxapi.exchanges.demo.gen.DemoExchangeConstants#DEFAULT_SYMBOL}, BNB_USDT]</td>
 *   </tr>
 *   <tr>
 *     <td>MarketData.rest.postRestRequestDataTypeInt.request</td>
 *     <td>INT</td>
 *     <td>Demo configuration property for postRestRequestDataTypeInt.request field.</td>
 *     <td>{@link org.jxapi.exchanges.demo.gen.DemoExchangeConstants.User#AGE}</td>
 *   </tr>
 *   <tr>
 *     <td>MarketData.rest.getRestRequestDataTypePrimitiveWithMsgField.age</td>
 *     <td>INT</td>
 *     <td>Demo configuration property for getRestRequestDataTypePrimitiveWithMsgField.age field.</td>
 *     <td>18</td>
 *   </tr>
 *   <tr>
 *     <td>MarketData.rest.postRestRequestDataTypeIntList.request</td>
 *     <td>INT_LIST</td>
 *     <td>Demo configuration property for postRestRequestDataTypeIntList.request field.</td>
 *     <td>[1, 3, 5]</td>
 *   </tr>
 *   <tr>
 *     <td>MarketData.rest.postRestRequestDataTypeObjectListMap.request</td>
 *     <td>STRING</td>
 *     <td>Demo configuration property for postRestRequestDataTypeObjectListMap.request field as raw JSON string value.</td>
 *     <td></td>
 *   </tr>
 *   <tr>
 *     <td>MarketData.rest.postRestRequestDataTypeObjectListMap.request.symbol</td>
 *     <td>STRING</td>
 *     <td>Demo configuration property for request.symbol field.<p>
 *     Symbol name</td>
 *     <td>BTC_USDT</td>
 *   </tr>
 *   <tr>
 *     <td>MarketData.ws.tickerStream.request</td>
 *     <td>STRING</td>
 *     <td>Demo configuration property for tickerStream.request field as raw JSON string value.</td>
 *     <td></td>
 *   </tr>
 *   <tr>
 *     <td>MarketData.ws.tickerStream.request.symbol</td>
 *     <td>STRING</td>
 *     <td>Demo configuration property for request.symbol field.<p>
 *     Symbol to subscribe to ticker stream of</td>
 *     <td>ETH_USDT</td>
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
   * Configuration properties for MarketData API group endpoints demo snippets
   */
  @Generated("org.jxapi.generator.java.exchange.properties.PropertiesClassGenerator")
  public static class MarketData {
    
    private MarketData(){}
    
    /**
     * Configuration properties for REST endpoints demo snippets of MarketData API group
     */
    @Generated("org.jxapi.generator.java.exchange.properties.PropertiesClassGenerator")
    public static class Rest {
      
      private Rest(){}
      
      /**
       * Configuration properties for REST exchangeInfo endpoint of MarketData API group
       */
      @Generated("org.jxapi.generator.java.exchange.properties.PropertiesClassGenerator")
      public static class ExchangeInfo {
        
        private ExchangeInfo(){}
        
        /**
         * Demo configuration property for exchangeInfo.request field as raw JSON string value.
         */
        public static final ConfigProperty REQUEST = DefaultConfigProperty.create(
          "demo.MarketData.rest.exchangeInfo.request",
          Type.STRING,
          "Demo configuration property for exchangeInfo.request field as raw JSON string value.",
          null);
        
        /**
         * Demo configuration properties for exchangeInfo.request field object instance.
         */
        @Generated("org.jxapi.generator.java.exchange.properties.PropertiesClassGenerator")
        public static class Request {
          
          private Request(){}
          
          /**
           * Demo configuration property for request.symbols field.<p>
           * The list of symbol to fetch market information for. Leave empty to fetch all markets
           */
          public static final ConfigProperty SYMBOLS = DefaultConfigProperty.create(
            "demo.MarketData.rest.exchangeInfo.request.symbols",
            Type.STRING,
            "Demo configuration property for request.symbols field.<p>\nThe list of symbol to fetch market information for. Leave empty to fetch all markets",
            EncodingUtil.substituteArguments("[\"${constants.defaultSymbol}\",\"BNB_USDT\"]", "constants.defaultSymbol", DemoExchangeConstants.DEFAULT_SYMBOL));
          
          /**
           * Retrieves value of 'symbols' property.
           * @param properties Properties to look for value of 'symbols' property into.
           * @return Value found in properties or default value <i>["{@link org.jxapi.exchanges.demo.gen.DemoExchangeConstants#DEFAULT_SYMBOL}","BNB_USDT"]</i> if not found.
           */
          public static String getSymbols(Properties properties) {return PropertiesUtil.getString(properties, SYMBOLS);}
          /**
           * List of all configuration properties defined in this class
           */
          public static final List<ConfigProperty> ALL = List.copyOf(CollectionUtil.mergeLists(List.of(
            List.of(
              SYMBOLS))));
        }
        
        /**
         * Retrieves value of 'request' property.
         * @param properties Properties to look for value of 'request' property into.
         * @return Value found in properties or <code>null</code> if not found.
         */
        public static String getRequest(Properties properties) {return PropertiesUtil.getString(properties, REQUEST);}
        /**
         * List of all configuration properties defined in this class
         */
        public static final List<ConfigProperty> ALL = List.copyOf(CollectionUtil.mergeLists(List.of(
          List.of(
            REQUEST
          ),
          Request.ALL)));
      }
      
      /**
       * Configuration properties for REST postRestRequestDataTypeInt endpoint of MarketData API group
       */
      @Generated("org.jxapi.generator.java.exchange.properties.PropertiesClassGenerator")
      public static class PostRestRequestDataTypeInt {
        
        private PostRestRequestDataTypeInt(){}
        
        /**
         * Demo configuration property for postRestRequestDataTypeInt.request field.
         */
        public static final ConfigProperty REQUEST = DefaultConfigProperty.create(
          "demo.MarketData.rest.postRestRequestDataTypeInt.request",
          Type.INT,
          "Demo configuration property for postRestRequestDataTypeInt.request field.",
          EncodingUtil.substituteArguments("${constants.user.age}", "constants.user.age", DemoExchangeConstants.User.AGE));
        
        /**
         * Retrieves value of 'request' property.
         * @param properties Properties to look for value of 'request' property into.
         * @return Value found in properties or default value <i>{@link org.jxapi.exchanges.demo.gen.DemoExchangeConstants.User#AGE}</i> if not found.
         */
        public static Integer getRequest(Properties properties) {return PropertiesUtil.getInt(properties, REQUEST);}
        /**
         * List of all configuration properties defined in this class
         */
        public static final List<ConfigProperty> ALL = List.copyOf(CollectionUtil.mergeLists(List.of(
          List.of(
            REQUEST))));
      }
      
      /**
       * Configuration properties for REST getRestRequestDataTypePrimitiveWithMsgField endpoint of MarketData API group
       */
      @Generated("org.jxapi.generator.java.exchange.properties.PropertiesClassGenerator")
      public static class GetRestRequestDataTypePrimitiveWithMsgField {
        
        private GetRestRequestDataTypePrimitiveWithMsgField(){}
        
        /**
         * Demo configuration property for getRestRequestDataTypePrimitiveWithMsgField.age field.
         */
        public static final ConfigProperty AGE = DefaultConfigProperty.create(
          "demo.MarketData.rest.getRestRequestDataTypePrimitiveWithMsgField.age",
          Type.INT,
          "Demo configuration property for getRestRequestDataTypePrimitiveWithMsgField.age field.",
          "18");
        
        /**
         * Retrieves value of 'age' property.
         * @param properties Properties to look for value of 'age' property into.
         * @return Value found in properties or default value <i>18</i> if not found.
         */
        public static Integer getAge(Properties properties) {return PropertiesUtil.getInt(properties, AGE);}
        /**
         * List of all configuration properties defined in this class
         */
        public static final List<ConfigProperty> ALL = List.copyOf(CollectionUtil.mergeLists(List.of(
          List.of(
            AGE))));
      }
      
      /**
       * Configuration properties for REST postRestRequestDataTypeIntList endpoint of MarketData API group
       */
      @Generated("org.jxapi.generator.java.exchange.properties.PropertiesClassGenerator")
      public static class PostRestRequestDataTypeIntList {
        
        private PostRestRequestDataTypeIntList(){}
        
        /**
         * Demo configuration property for postRestRequestDataTypeIntList.request field.
         */
        public static final ConfigProperty REQUEST = DefaultConfigProperty.create(
          "demo.MarketData.rest.postRestRequestDataTypeIntList.request",
          Type.STRING,
          "Demo configuration property for postRestRequestDataTypeIntList.request field.",
          "[1,3,5]");
        
        /**
         * Retrieves value of 'request' property.
         * @param properties Properties to look for value of 'request' property into.
         * @return Value found in properties or default value <i>[1,3,5]</i> if not found.
         */
        public static String getRequest(Properties properties) {return PropertiesUtil.getString(properties, REQUEST);}
        /**
         * List of all configuration properties defined in this class
         */
        public static final List<ConfigProperty> ALL = List.copyOf(CollectionUtil.mergeLists(List.of(
          List.of(
            REQUEST))));
      }
      
      /**
       * Configuration properties for REST postRestRequestDataTypeObjectListMap endpoint of MarketData API group
       */
      @Generated("org.jxapi.generator.java.exchange.properties.PropertiesClassGenerator")
      public static class PostRestRequestDataTypeObjectListMap {
        
        private PostRestRequestDataTypeObjectListMap(){}
        
        /**
         * Demo configuration property for postRestRequestDataTypeObjectListMap.request field as raw JSON string value.
         */
        public static final ConfigProperty REQUEST = DefaultConfigProperty.create(
          "demo.MarketData.rest.postRestRequestDataTypeObjectListMap.request",
          Type.STRING,
          "Demo configuration property for postRestRequestDataTypeObjectListMap.request field as raw JSON string value.",
          null);
        
        /**
         * Demo configuration properties for postRestRequestDataTypeObjectListMap.request field object instance.
         */
        @Generated("org.jxapi.generator.java.exchange.properties.PropertiesClassGenerator")
        public static class Request {
          
          private Request(){}
          
          /**
           * Demo configuration property for request.symbol field.<p>
           * Symbol name
           */
          public static final ConfigProperty SYMBOL = DefaultConfigProperty.create(
            "demo.MarketData.rest.postRestRequestDataTypeObjectListMap.request.symbol",
            Type.STRING,
            "Demo configuration property for request.symbol field.<p>\nSymbol name",
            "BTC_USDT");
          
          /**
           * Retrieves value of 'symbol' property.
           * @param properties Properties to look for value of 'symbol' property into.
           * @return Value found in properties or default value <i>BTC_USDT</i> if not found.
           */
          public static String getSymbol(Properties properties) {return PropertiesUtil.getString(properties, SYMBOL);}
          /**
           * List of all configuration properties defined in this class
           */
          public static final List<ConfigProperty> ALL = List.copyOf(CollectionUtil.mergeLists(List.of(
            List.of(
              SYMBOL))));
        }
        
        /**
         * Retrieves value of 'request' property.
         * @param properties Properties to look for value of 'request' property into.
         * @return Value found in properties or <code>null</code> if not found.
         */
        public static String getRequest(Properties properties) {return PropertiesUtil.getString(properties, REQUEST);}
        /**
         * List of all configuration properties defined in this class
         */
        public static final List<ConfigProperty> ALL = List.copyOf(CollectionUtil.mergeLists(List.of(
          List.of(
            REQUEST
          ),
          Request.ALL)));
      }
      /**
       * List of all configuration properties defined in this class
       */
      public static final List<ConfigProperty> ALL = List.copyOf(CollectionUtil.mergeLists(List.of(
        ExchangeInfo.ALL,
        PostRestRequestDataTypeInt.ALL,
        GetRestRequestDataTypePrimitiveWithMsgField.ALL,
        PostRestRequestDataTypeIntList.ALL,
        PostRestRequestDataTypeObjectListMap.ALL)));
    }
    
    /**
     * Configuration properties for websocket endpoints demo snippets of MarketData API group
     */
    @Generated("org.jxapi.generator.java.exchange.properties.PropertiesClassGenerator")
    public static class Ws {
      
      private Ws(){}
      
      /**
       * Configuration properties for websocket tickerStream endpoint of MarketData API group
       */
      @Generated("org.jxapi.generator.java.exchange.properties.PropertiesClassGenerator")
      public static class TickerStream {
        
        private TickerStream(){}
        
        /**
         * Demo configuration property for tickerStream.request field as raw JSON string value.
         */
        public static final ConfigProperty REQUEST = DefaultConfigProperty.create(
          "demo.MarketData.ws.tickerStream.request",
          Type.STRING,
          "Demo configuration property for tickerStream.request field as raw JSON string value.",
          null);
        
        /**
         * Demo configuration properties for tickerStream.request field object instance.
         */
        @Generated("org.jxapi.generator.java.exchange.properties.PropertiesClassGenerator")
        public static class Request {
          
          private Request(){}
          
          /**
           * Demo configuration property for request.symbol field.<p>
           * Symbol to subscribe to ticker stream of
           */
          public static final ConfigProperty SYMBOL = DefaultConfigProperty.create(
            "demo.MarketData.ws.tickerStream.request.symbol",
            Type.STRING,
            "Demo configuration property for request.symbol field.<p>\nSymbol to subscribe to ticker stream of",
            "ETH_USDT");
          
          /**
           * Retrieves value of 'symbol' property.
           * @param properties Properties to look for value of 'symbol' property into.
           * @return Value found in properties or default value <i>ETH_USDT</i> if not found.
           */
          public static String getSymbol(Properties properties) {return PropertiesUtil.getString(properties, SYMBOL);}
          /**
           * List of all configuration properties defined in this class
           */
          public static final List<ConfigProperty> ALL = List.copyOf(CollectionUtil.mergeLists(List.of(
            List.of(
              SYMBOL))));
        }
        
        /**
         * Retrieves value of 'request' property.
         * @param properties Properties to look for value of 'request' property into.
         * @return Value found in properties or <code>null</code> if not found.
         */
        public static String getRequest(Properties properties) {return PropertiesUtil.getString(properties, REQUEST);}
        /**
         * List of all configuration properties defined in this class
         */
        public static final List<ConfigProperty> ALL = List.copyOf(CollectionUtil.mergeLists(List.of(
          List.of(
            REQUEST
          ),
          Request.ALL)));
      }
      /**
       * List of all configuration properties defined in this class
       */
      public static final List<ConfigProperty> ALL = List.copyOf(CollectionUtil.mergeLists(List.of(
        TickerStream.ALL)));
    }
    /**
     * List of all configuration properties defined in this class
     */
    public static final List<ConfigProperty> ALL = List.copyOf(CollectionUtil.mergeLists(List.of(
      Rest.ALL,
      Ws.ALL)));
  }
  /**
   * List of all configuration properties defined in this class
   */
  public static final List<ConfigProperty> ALL = List.copyOf(CollectionUtil.mergeLists(List.of(
    MarketData.ALL)));
}
