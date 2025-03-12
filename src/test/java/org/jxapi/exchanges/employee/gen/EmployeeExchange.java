package org.jxapi.exchanges.employee.gen;

import org.jxapi.exchange.Exchange;
import org.jxapi.exchanges.employee.gen.v1.EmployeeV1Api;
import javax.annotation.processing.Generated;

/**
 * Employee API<br>
 * Employee exchange is a demo exchange REST APIs to get, add, delete and  update employees and a websocket endpoint to get notified of updates from an employee database.<br> A server can be started using <code>org.jxapi.exchanges.employee.EmployeeExchangeServer</code> class to serve these APIs.<br> The URL of the server must be set using the baseUrl property.<br> Notice how the 'employee' object present in APIs request and responses is used in multiple endpoints and its properties defined only once.
 * 
 * @see <a href="https://www.example.com/docs/employee">Reference documentation</a>
 */
@Generated("org.jxapi.generator.java.exchange.ExchangeInterfaceGenerator")
public interface EmployeeExchange extends Exchange {
  
  String ID = "Employee";
  
  /**
   * @return Version 1 of the Employee API
   */
  EmployeeV1Api getEmployeeV1Api();
}
