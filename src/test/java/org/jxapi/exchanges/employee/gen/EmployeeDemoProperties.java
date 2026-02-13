package org.jxapi.exchanges.employee.gen;

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
 * Configurable demo properties for <strong>Employee</strong> exchange:<br>
 * <table>
 *   <caption>Employee properties</caption>
 *   <tr>
 *     <th>Name</th>
 *     <th>Type</th>
 *     <th>Description</th>
 *     <th>Default value</th>
 *   </tr>
 *   <tr>
 *     <td>v1.rest.getEmployee.id</td>
 *     <td>INT</td>
 *     <td>Demo configuration property for getEmployee.id field.<p>
 *     Employee ID</td>
 *     <td>1</td>
 *   </tr>
 *   <tr>
 *     <td>v1.rest.getAllEmployees.request</td>
 *     <td>STRING</td>
 *     <td>Demo configuration property for getAllEmployees.request field as raw JSON string value.<p>
 *     Page request parameters for 'getAllEmployees' rest endpoint paginated requests.</td>
 *     <td></td>
 *   </tr>
 *   <tr>
 *     <td>v1.rest.getAllEmployees.request.page</td>
 *     <td>INT</td>
 *     <td>Demo configuration property for request.page field.<p>
 *     Page number to return, defaults to 1.</td>
 *     <td>1</td>
 *   </tr>
 *   <tr>
 *     <td>v1.rest.getAllEmployees.request.size</td>
 *     <td>INT</td>
 *     <td>Demo configuration property for request.size field.<p>
 *     Number of employees to return per page.<br> Defaults to {@link org.jxapi.exchanges.employee.gen.EmployeeConstants#DEFAULT_PAGE_SIZE}.<br> Maximum is {@link org.jxapi.exchanges.employee.gen.EmployeeConstants#MAX_PAGE_SIZE}.
 *     </td>
 *     <td>10</td>
 *   </tr>
 *   <tr>
 *     <td>v1.rest.addEmployee.request</td>
 *     <td>STRING</td>
 *     <td>Demo configuration property for addEmployee.request field as raw JSON string value.<p>
 *     Employee to add</td>
 *     <td></td>
 *   </tr>
 *   <tr>
 *     <td>v1.rest.addEmployee.request.id</td>
 *     <td>INT</td>
 *     <td>Demo configuration property for request.id field.<p>
 *     Employee ID</td>
 *     <td>1</td>
 *   </tr>
 *   <tr>
 *     <td>v1.rest.addEmployee.request.firstName</td>
 *     <td>STRING</td>
 *     <td>Demo configuration property for request.firstName field.<p>
 *     Employee first name</td>
 *     <td>John</td>
 *   </tr>
 *   <tr>
 *     <td>v1.rest.addEmployee.request.lastName</td>
 *     <td>STRING</td>
 *     <td>Demo configuration property for request.lastName field.<p>
 *     Employee last name</td>
 *     <td>Doe</td>
 *   </tr>
 *   <tr>
 *     <td>v1.rest.addEmployee.request.profile</td>
 *     <td>STRING</td>
 *     <td>Demo configuration property for request.profile field.<p>
 *     Employee profile. See {@link org.jxapi.exchanges.employee.gen.EmployeeConstants.Profile}</td>
 *     <td>{@link org.jxapi.exchanges.employee.gen.EmployeeConstants.Profile#REGULAR}</td>
 *   </tr>
 *   <tr>
 *     <td>v1.rest.updateEmployee.request</td>
 *     <td>STRING</td>
 *     <td>Demo configuration property for updateEmployee.request field as raw JSON string value.<p>
 *     Employee to update</td>
 *     <td></td>
 *   </tr>
 *   <tr>
 *     <td>v1.rest.updateEmployee.request.id</td>
 *     <td>INT</td>
 *     <td>Demo configuration property for request.id field.<p>
 *     Employee ID</td>
 *     <td>1</td>
 *   </tr>
 *   <tr>
 *     <td>v1.rest.updateEmployee.request.firstName</td>
 *     <td>STRING</td>
 *     <td>Demo configuration property for request.firstName field.<p>
 *     Employee first name</td>
 *     <td>John</td>
 *   </tr>
 *   <tr>
 *     <td>v1.rest.updateEmployee.request.lastName</td>
 *     <td>STRING</td>
 *     <td>Demo configuration property for request.lastName field.<p>
 *     Employee last name</td>
 *     <td>Doe</td>
 *   </tr>
 *   <tr>
 *     <td>v1.rest.updateEmployee.request.profile</td>
 *     <td>STRING</td>
 *     <td>Demo configuration property for request.profile field.<p>
 *     Employee profile. See {@link org.jxapi.exchanges.employee.gen.EmployeeConstants.Profile}</td>
 *     <td>{@link org.jxapi.exchanges.employee.gen.EmployeeConstants.Profile#REGULAR}</td>
 *   </tr>
 *   <tr>
 *     <td>v1.rest.deleteEmployee.id</td>
 *     <td>INT</td>
 *     <td>Demo configuration property for deleteEmployee.id field.<p>
 *     Employee ID</td>
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
   * Configuration properties for v1 API group endpoints demo snippets
   */
  @Generated("org.jxapi.generator.java.exchange.properties.PropertiesClassGenerator")
  public static class V1 {
    
    private V1(){}
    
    /**
     * Configuration properties for REST endpoints demo snippets of v1 API group
     */
    @Generated("org.jxapi.generator.java.exchange.properties.PropertiesClassGenerator")
    public static class Rest {
      
      private Rest(){}
      
      /**
       * Configuration properties for REST getEmployee endpoint of v1 API group
       */
      @Generated("org.jxapi.generator.java.exchange.properties.PropertiesClassGenerator")
      public static class GetEmployee {
        
        private GetEmployee(){}
        
        /**
         * Demo configuration property for getEmployee.id field.<p>
         * Employee ID
         */
        public static final ConfigProperty ID = DefaultConfigProperty.create(
          "demo.v1.rest.getEmployee.id",
          Type.INT,
          "Demo configuration property for getEmployee.id field.<p>\nEmployee ID",
          "1");
        
        /**
         * Retrieves value of 'id' property.
         * @param properties Properties to look for value of 'id' property into.
         * @return Value found in properties or default value <i>1</i> if not found.
         */
        public static Integer getId(Properties properties) {return PropertiesUtil.getInt(properties, ID);}
        /**
         * List of all configuration properties defined in this class
         */
        public static final List<ConfigProperty> ALL = List.copyOf(CollectionUtil.mergeLists(List.of(
          List.of(
            ID))));
      }
      
      /**
       * Configuration properties for REST getAllEmployees endpoint of v1 API group
       */
      @Generated("org.jxapi.generator.java.exchange.properties.PropertiesClassGenerator")
      public static class GetAllEmployees {
        
        private GetAllEmployees(){}
        
        /**
         * Demo configuration property for getAllEmployees.request field as raw JSON string value.<p>
         * Page request parameters for 'getAllEmployees' rest endpoint paginated requests.
         */
        public static final ConfigProperty REQUEST = DefaultConfigProperty.create(
          "demo.v1.rest.getAllEmployees.request",
          Type.STRING,
          "Demo configuration property for getAllEmployees.request field as raw JSON string value.<p>\nPage request parameters for 'getAllEmployees' rest endpoint paginated requests.",
          null);
        
        /**
         * Demo configuration properties for getAllEmployees.request field object instance.<p>
         * Page request parameters for 'getAllEmployees' rest endpoint paginated requests.
         */
        @Generated("org.jxapi.generator.java.exchange.properties.PropertiesClassGenerator")
        public static class Request {
          
          private Request(){}
          
          /**
           * Demo configuration property for request.page field.<p>
           * Page number to return, defaults to 1.
           */
          public static final ConfigProperty PAGE = DefaultConfigProperty.create(
            "demo.v1.rest.getAllEmployees.request.page",
            Type.INT,
            "Demo configuration property for request.page field.<p>\nPage number to return, defaults to 1.",
            "1");
          
          /**
           * Demo configuration property for request.size field.<p>
           * Number of employees to return per page.<br> Defaults to {@link org.jxapi.exchanges.employee.gen.EmployeeConstants#DEFAULT_PAGE_SIZE}.<br> Maximum is {@link org.jxapi.exchanges.employee.gen.EmployeeConstants#MAX_PAGE_SIZE}.
           * 
           */
          public static final ConfigProperty SIZE = DefaultConfigProperty.create(
            "demo.v1.rest.getAllEmployees.request.size",
            Type.INT,
            "Demo configuration property for request.size field.<p>\nNumber of employees to return per page.<br> Defaults to {@link org.jxapi.exchanges.employee.gen.EmployeeConstants#DEFAULT_PAGE_SIZE}.<br> Maximum is {@link org.jxapi.exchanges.employee.gen.EmployeeConstants#MAX_PAGE_SIZE}.\n",
            "10");
          
          /**
           * Retrieves value of 'page' property.
           * @param properties Properties to look for value of 'page' property into.
           * @return Value found in properties or default value <i>1</i> if not found.
           */
          public static Integer getPage(Properties properties) {return PropertiesUtil.getInt(properties, PAGE);}
          
          /**
           * Retrieves value of 'size' property.
           * @param properties Properties to look for value of 'size' property into.
           * @return Value found in properties or default value <i>10</i> if not found.
           */
          public static Integer getSize(Properties properties) {return PropertiesUtil.getInt(properties, SIZE);}
          /**
           * List of all configuration properties defined in this class
           */
          public static final List<ConfigProperty> ALL = List.copyOf(CollectionUtil.mergeLists(List.of(
            List.of(
              PAGE,
              SIZE))));
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
       * Configuration properties for REST addEmployee endpoint of v1 API group
       */
      @Generated("org.jxapi.generator.java.exchange.properties.PropertiesClassGenerator")
      public static class AddEmployee {
        
        private AddEmployee(){}
        
        /**
         * Demo configuration property for addEmployee.request field as raw JSON string value.<p>
         * Employee to add
         */
        public static final ConfigProperty REQUEST = DefaultConfigProperty.create(
          "demo.v1.rest.addEmployee.request",
          Type.STRING,
          "Demo configuration property for addEmployee.request field as raw JSON string value.<p>\nEmployee to add",
          null);
        
        /**
         * Demo configuration properties for addEmployee.request field object instance.<p>
         * Employee to add
         */
        @Generated("org.jxapi.generator.java.exchange.properties.PropertiesClassGenerator")
        public static class Request {
          
          private Request(){}
          
          /**
           * Demo configuration property for request.id field.<p>
           * Employee ID
           */
          public static final ConfigProperty ID = DefaultConfigProperty.create(
            "demo.v1.rest.addEmployee.request.id",
            Type.INT,
            "Demo configuration property for request.id field.<p>\nEmployee ID",
            "1");
          
          /**
           * Demo configuration property for request.firstName field.<p>
           * Employee first name
           */
          public static final ConfigProperty FIRST_NAME = DefaultConfigProperty.create(
            "demo.v1.rest.addEmployee.request.firstName",
            Type.STRING,
            "Demo configuration property for request.firstName field.<p>\nEmployee first name",
            "John");
          
          /**
           * Demo configuration property for request.lastName field.<p>
           * Employee last name
           */
          public static final ConfigProperty LAST_NAME = DefaultConfigProperty.create(
            "demo.v1.rest.addEmployee.request.lastName",
            Type.STRING,
            "Demo configuration property for request.lastName field.<p>\nEmployee last name",
            "Doe");
          
          /**
           * Demo configuration property for request.profile field.<p>
           * Employee profile. See {@link org.jxapi.exchanges.employee.gen.EmployeeConstants.Profile}
           */
          public static final ConfigProperty PROFILE = DefaultConfigProperty.create(
            "demo.v1.rest.addEmployee.request.profile",
            Type.STRING,
            "Demo configuration property for request.profile field.<p>\nEmployee profile. See {@link org.jxapi.exchanges.employee.gen.EmployeeConstants.Profile}",
            EncodingUtil.substituteArguments("${constants.profile.regular}", "constants.profile.regular", EmployeeConstants.Profile.REGULAR));
          
          /**
           * Retrieves value of 'id' property.
           * @param properties Properties to look for value of 'id' property into.
           * @return Value found in properties or default value <i>1</i> if not found.
           */
          public static Integer getId(Properties properties) {return PropertiesUtil.getInt(properties, ID);}
          
          /**
           * Retrieves value of 'firstName' property.
           * @param properties Properties to look for value of 'firstName' property into.
           * @return Value found in properties or default value <i>John</i> if not found.
           */
          public static String getFirstName(Properties properties) {return PropertiesUtil.getString(properties, FIRST_NAME);}
          
          /**
           * Retrieves value of 'lastName' property.
           * @param properties Properties to look for value of 'lastName' property into.
           * @return Value found in properties or default value <i>Doe</i> if not found.
           */
          public static String getLastName(Properties properties) {return PropertiesUtil.getString(properties, LAST_NAME);}
          
          /**
           * Retrieves value of 'profile' property.
           * @param properties Properties to look for value of 'profile' property into.
           * @return Value found in properties or default value <i>{@link org.jxapi.exchanges.employee.gen.EmployeeConstants.Profile#REGULAR}</i> if not found.
           */
          public static String getProfile(Properties properties) {return PropertiesUtil.getString(properties, PROFILE);}
          /**
           * List of all configuration properties defined in this class
           */
          public static final List<ConfigProperty> ALL = List.copyOf(CollectionUtil.mergeLists(List.of(
            List.of(
              ID,
              FIRST_NAME,
              LAST_NAME,
              PROFILE))));
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
       * Configuration properties for REST updateEmployee endpoint of v1 API group
       */
      @Generated("org.jxapi.generator.java.exchange.properties.PropertiesClassGenerator")
      public static class UpdateEmployee {
        
        private UpdateEmployee(){}
        
        /**
         * Demo configuration property for updateEmployee.request field as raw JSON string value.<p>
         * Employee to update
         */
        public static final ConfigProperty REQUEST = DefaultConfigProperty.create(
          "demo.v1.rest.updateEmployee.request",
          Type.STRING,
          "Demo configuration property for updateEmployee.request field as raw JSON string value.<p>\nEmployee to update",
          null);
        
        /**
         * Demo configuration properties for updateEmployee.request field object instance.<p>
         * Employee to update
         */
        @Generated("org.jxapi.generator.java.exchange.properties.PropertiesClassGenerator")
        public static class Request {
          
          private Request(){}
          
          /**
           * Demo configuration property for request.id field.<p>
           * Employee ID
           */
          public static final ConfigProperty ID = DefaultConfigProperty.create(
            "demo.v1.rest.updateEmployee.request.id",
            Type.INT,
            "Demo configuration property for request.id field.<p>\nEmployee ID",
            "1");
          
          /**
           * Demo configuration property for request.firstName field.<p>
           * Employee first name
           */
          public static final ConfigProperty FIRST_NAME = DefaultConfigProperty.create(
            "demo.v1.rest.updateEmployee.request.firstName",
            Type.STRING,
            "Demo configuration property for request.firstName field.<p>\nEmployee first name",
            "John");
          
          /**
           * Demo configuration property for request.lastName field.<p>
           * Employee last name
           */
          public static final ConfigProperty LAST_NAME = DefaultConfigProperty.create(
            "demo.v1.rest.updateEmployee.request.lastName",
            Type.STRING,
            "Demo configuration property for request.lastName field.<p>\nEmployee last name",
            "Doe");
          
          /**
           * Demo configuration property for request.profile field.<p>
           * Employee profile. See {@link org.jxapi.exchanges.employee.gen.EmployeeConstants.Profile}
           */
          public static final ConfigProperty PROFILE = DefaultConfigProperty.create(
            "demo.v1.rest.updateEmployee.request.profile",
            Type.STRING,
            "Demo configuration property for request.profile field.<p>\nEmployee profile. See {@link org.jxapi.exchanges.employee.gen.EmployeeConstants.Profile}",
            EncodingUtil.substituteArguments("${constants.profile.regular}", "constants.profile.regular", EmployeeConstants.Profile.REGULAR));
          
          /**
           * Retrieves value of 'id' property.
           * @param properties Properties to look for value of 'id' property into.
           * @return Value found in properties or default value <i>1</i> if not found.
           */
          public static Integer getId(Properties properties) {return PropertiesUtil.getInt(properties, ID);}
          
          /**
           * Retrieves value of 'firstName' property.
           * @param properties Properties to look for value of 'firstName' property into.
           * @return Value found in properties or default value <i>John</i> if not found.
           */
          public static String getFirstName(Properties properties) {return PropertiesUtil.getString(properties, FIRST_NAME);}
          
          /**
           * Retrieves value of 'lastName' property.
           * @param properties Properties to look for value of 'lastName' property into.
           * @return Value found in properties or default value <i>Doe</i> if not found.
           */
          public static String getLastName(Properties properties) {return PropertiesUtil.getString(properties, LAST_NAME);}
          
          /**
           * Retrieves value of 'profile' property.
           * @param properties Properties to look for value of 'profile' property into.
           * @return Value found in properties or default value <i>{@link org.jxapi.exchanges.employee.gen.EmployeeConstants.Profile#REGULAR}</i> if not found.
           */
          public static String getProfile(Properties properties) {return PropertiesUtil.getString(properties, PROFILE);}
          /**
           * List of all configuration properties defined in this class
           */
          public static final List<ConfigProperty> ALL = List.copyOf(CollectionUtil.mergeLists(List.of(
            List.of(
              ID,
              FIRST_NAME,
              LAST_NAME,
              PROFILE))));
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
       * Configuration properties for REST deleteEmployee endpoint of v1 API group
       */
      @Generated("org.jxapi.generator.java.exchange.properties.PropertiesClassGenerator")
      public static class DeleteEmployee {
        
        private DeleteEmployee(){}
        
        /**
         * Demo configuration property for deleteEmployee.id field.<p>
         * Employee ID
         */
        public static final ConfigProperty ID = DefaultConfigProperty.create(
          "demo.v1.rest.deleteEmployee.id",
          Type.INT,
          "Demo configuration property for deleteEmployee.id field.<p>\nEmployee ID",
          "1");
        
        /**
         * Retrieves value of 'id' property.
         * @param properties Properties to look for value of 'id' property into.
         * @return Value found in properties or default value <i>1</i> if not found.
         */
        public static Integer getId(Properties properties) {return PropertiesUtil.getInt(properties, ID);}
        /**
         * List of all configuration properties defined in this class
         */
        public static final List<ConfigProperty> ALL = List.copyOf(CollectionUtil.mergeLists(List.of(
          List.of(
            ID))));
      }
      /**
       * List of all configuration properties defined in this class
       */
      public static final List<ConfigProperty> ALL = List.copyOf(CollectionUtil.mergeLists(List.of(
        GetEmployee.ALL,
        GetAllEmployees.ALL,
        AddEmployee.ALL,
        UpdateEmployee.ALL,
        DeleteEmployee.ALL)));
    }
    /**
     * List of all configuration properties defined in this class
     */
    public static final List<ConfigProperty> ALL = List.copyOf(CollectionUtil.mergeLists(List.of(
      Rest.ALL)));
  }
  /**
   * List of all configuration properties defined in this class
   */
  public static final List<ConfigProperty> ALL = List.copyOf(CollectionUtil.mergeLists(List.of(
    V1.ALL)));
}
