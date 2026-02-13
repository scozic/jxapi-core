package org.jxapi.exchanges.employee.gen;

import javax.annotation.processing.Generated;
import org.jxapi.exchange.Exchange;
import org.jxapi.exchanges.employee.gen.v1.EmployeeV1Api;

/**
 * Employee API<br>
 * Employee exchange is a demo exchange REST APIs to get, add, delete and update employees and a websocket endpoint to get notified of updates from an employee database.<br> A server can be started using <code>org.jxapi.exchanges.employee.EmployeeExchangeServer</code> class to serve these APIs.<br> The URL of the HTTP server and Websocket server must be set using the ${config.baseHttpUrl} and ${config.baseWebsocketUrl} properties.<br> Notice how the 'employee' object present in APIs request and responses is used in multiple endpoints and its properties defined only once.
 * 
 * @see <a href="https://www.example.com/docs/employee">Reference documentation</a>
 */
@Generated("org.jxapi.generator.java.exchange.ExchangeInterfaceGenerator")
public interface EmployeeExchange extends Exchange {
  
  /**
   * ID of the 'Employee' exchange
   */
  String ID = "Employee";
  
  /**
   * Version of the 'Employee' exchange
   */
  String VERSION = "1.0.0";
  /**
   * Name of <code>httpDefault</code> HttpClient.
   */
   String HTTP_DEFAULT_HTTP_CLIENT = "httpDefault";
  /**
   * Name of <code>wsDefault</code> WebsocketClient.
   */
   String WS_DEFAULT_WEBSOCKET_CLIENT = "wsDefault";
  
  // API groups
  
  /**
   * @return Version 1 of the Employee API
   */
  EmployeeV1Api getV1Api();
}
