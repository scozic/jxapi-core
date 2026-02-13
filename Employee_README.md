# Employee API Java wrapper

Employee exchange is a demo exchange REST APIs to get, add, delete and update employees and a websocket endpoint to get notified of updates from an employee database.<br> A server can be started using <code>org.jxapi.exchanges.employee.EmployeeExchangeServer</code> class to serve these APIs.<br> The URL of the HTTP server and Websocket server must be set using the ${config.baseHttpUrl} and ${config.baseWebsocketUrl} properties.<br> Notice how the 'employee' object present in APIs request and responses is used in multiple endpoints and its properties defined only once.

<!-- BEGIN TABLE OF CONTENTS -->
  - [Employee API Java wrapper](#employee-api-java-wrapper)
      - [Quick start](#quick-start)
      - [Properties](#properties)
      - [Constants](#constants)
    - [API Groups](#api-groups)
      - [v1](#v1)
        - [REST endpoints](#rest-endpoints)
        - [Websocket endpoints](#websocket-endpoints)
    - [Demo snippets](#demo-snippets)
      - [Endpoint demo snippets](#endpoint-demo-snippets)
        - [v1 REST endpoints demo snippets:](#v1-rest-endpoints-demo-snippets)
        - [v1 Websocket endpoints demo snippets](#v1-websocket-endpoints-demo-snippets)

<!-- END TABLE OF CONTENTS -->
See <a href="https://www.example.com/docs/employee">reference documentation</a>
### Quick start

This wrapper offers Java interfaces for using Employee API
Add maven dependency to your project, then you can use the wrapper by instantiating <a href="./doc/javadoc/org/jxapi/exchanges/employee/gen/EmployeeExchange.html">EmployeeExchange</a> in your code:
``` java
import org.jxapi.exchanges.employee.gen.EmployeeExchange;
import org.jxapi.exchanges.employee.gen.EmployeeExchangeImpl;

public void call() {
  Properties properties = new Properties();
  // Set relevant configuration properties (see documentation) in 'props'
  EmployeeExchange   exchange = new EmployeeExchangeImpl(properties);
  // Access API groups and their endpoints through 'exchange' methods.
}
```
You may have a look at <a href="./src/test/java/org/jxapi/exchanges/employee/gen/v1/demo/EmployeeV1GetEmployeeDemo.java">EmployeeV1GetEmployeeDemo</a> class for full usage example

### Properties

<table>
  <caption>Configuration properties</caption>
  <tr>
    <th>Name</th>
    <th>Type</th>
    <th>description</th>
    <th>Default value</th>
  </tr>
  <tr>
    <td>server</td>
    <td>group</td>
    <td colspan="2">Server related properties</td>
  </tr>
  <tr>
    <td>server.baseHttpUrl</td>
    <td>STRING</td>
    <td>Base URL for REST endpoints the Employee Exchange API</td>
    <td></td>
  </tr>
  <tr>
    <td>server.baseWebsocketUrl</td>
    <td>STRING</td>
    <td>Base URL for websocket endpoints of the Employee Exchange API</td>
    <td></td>
  </tr>
</table>
Some demo configuration properties are available to tune common request parameters used in demo snippets, as <a href="./src/test/java/org/jxapi/exchanges/employee/gen/EmployeeDemoProperties.java">EmployeeDemoProperties</a> class.
 These properties are used to configure default values for request parameters used in demo snippets.

In order to run demo snippets, you can uncomment and set properties values in __demo-Employee.properties__ properties file you create from .dist template in src/test/resources folder.


### Constants

Some useful constants are defined in <a href="./doc/javadoc/org/jxapi/exchanges/employee/gen/EmployeeConstants.html">EmployeeConstants</a>

## API Groups
APIs are available using the following interfaces accessible from <a href="./doc/javadoc/org/jxapi/exchanges/employee/gen/EmployeeExchange.html">EmployeeExchange</a> interface

### v1
Methods exposed in <a href="./doc/javadoc/org/jxapi/exchanges/employee/gen/v1/EmployeeV1Api.html">EmployeeV1Api</a> accessible from <a href="./doc/javadoc/org/jxapi/exchanges/employee/gen/EmployeeExchange#getEmployeeV1Api().html">EmployeeExchange#getEmployeeV1Api()</a>

Version 1 of the Employee API

#### REST endpoints

<table>
  <caption>Employee v1 REST endpoints</caption>
  <tr>
    <th>Endpoint</th>
    <th>Description</th>
    <th>API Reference</th>
  </tr>
  <tr>
    <td><a href="./doc/javadoc/org/jxapi/exchanges/employee/gen/v1/EmployeeV1Api.html#getEmployee(java.lang.Integer)">getEmployee</a></td>
    <td>Get employee details by ID</td>
    <td><a href="https://www.example.com/docs/employee/get">link</a></td>
  </tr>
  <tr>
    <td><a href="./doc/javadoc/org/jxapi/exchanges/employee/gen/v1/EmployeeV1Api.html#getAllEmployees(org.jxapi.exchanges.employee.gen.v1.pojo.EmployeeV1GetAllEmployeesRequest)">getAllEmployees</a></td>
    <td>Get all employees</td>
    <td><a href="https://www.example.com/docs/employee/getAll">link</a></td>
  </tr>
  <tr>
    <td><a href="./doc/javadoc/org/jxapi/exchanges/employee/gen/v1/EmployeeV1Api.html#addEmployee(org.jxapi.exchanges.employee.gen.v1.pojo.Employee)">addEmployee</a></td>
    <td>Add a new employee</td>
    <td><a href="https://www.example.com/docs/employee/add">link</a></td>
  </tr>
  <tr>
    <td><a href="./doc/javadoc/org/jxapi/exchanges/employee/gen/v1/EmployeeV1Api.html#updateEmployee(org.jxapi.exchanges.employee.gen.v1.pojo.Employee)">updateEmployee</a></td>
    <td>Update an existing employee</td>
    <td><a href="https://www.example.com/docs/employee/add">link</a></td>
  </tr>
  <tr>
    <td><a href="./doc/javadoc/org/jxapi/exchanges/employee/gen/v1/EmployeeV1Api.html#deleteEmployee(java.lang.Integer)">deleteEmployee</a></td>
    <td>Delete an employee</td>
    <td><a href="https://www.example.com/docs/employee/delete">link</a></td>
  </tr>
</table>

#### Websocket endpoints

<table>
  <caption>Employee v1 websocket endpoints</caption>
  <tr>
    <th>Subscription method</th>
    <th>Description</th>
    <th>API Reference</th>
  </tr>
  <tr>
    <td><a href="./doc/javadoc/org/jxapi/exchanges/employee/gen/v1/EmployeeV1Api.html#subscribeEmployeeUpdates()">subscribeEmployeeUpdates</a></td>
    <td>Employee updates websocket</td>
    <td><a href="https://www.example.com/docs/employee/updates">link</a></td>
  </tr>
</table>

## Demo snippets

This wrapper contains demo snippets for the most important endpoints. These snippets are generated in _src/test/java/_ source folder.

Some demo configuration properties are available to tune common request parameters used in snippets, as <a href="./src/test/java/org/jxapi/exchanges/employee/gen/EmployeeDemoProperties.java">EmployeeDemoProperties</a> class.
 These properties are used to configure default values for request parameters used in demo snippets.

In order to run demo snippets, you can set properties values in __demo-Employee.properties__ properties file in src/test/resources folder.


### Endpoint demo snippets


#### v1 REST endpoints demo snippets:

 - __getEmployee__: <a href="./src/test/java/org/jxapi/exchanges/employee/gen/v1/demo/EmployeeV1GetEmployeeDemo.java">EmployeeV1GetEmployeeDemo</a>
 - __getAllEmployees__: <a href="./src/test/java/org/jxapi/exchanges/employee/gen/v1/demo/EmployeeV1GetAllEmployeesDemo.java">EmployeeV1GetAllEmployeesDemo</a>
 - __addEmployee__: <a href="./src/test/java/org/jxapi/exchanges/employee/gen/v1/demo/EmployeeV1AddEmployeeDemo.java">EmployeeV1AddEmployeeDemo</a>
 - __updateEmployee__: <a href="./src/test/java/org/jxapi/exchanges/employee/gen/v1/demo/EmployeeV1UpdateEmployeeDemo.java">EmployeeV1UpdateEmployeeDemo</a>
 - __deleteEmployee__: <a href="./src/test/java/org/jxapi/exchanges/employee/gen/v1/demo/EmployeeV1DeleteEmployeeDemo.java">EmployeeV1DeleteEmployeeDemo</a>

#### v1 Websocket endpoints demo snippets

 - __employeeUpdates__: <a href="./src/test/java/org/jxapi/exchanges/employee/gen/v1/demo/EmployeeV1EmployeeUpdatesDemo.java">EmployeeV1EmployeeUpdatesDemo</a>
