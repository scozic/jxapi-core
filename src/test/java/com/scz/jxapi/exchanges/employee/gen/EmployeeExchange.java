package com.scz.jxapi.exchanges.employee.gen;

import com.scz.jxapi.exchange.Exchange;
import com.scz.jxapi.exchanges.employee.gen.v1.EmployeeV1Api;

/**
 * Employee API<br>
 * Employee exchange is a demo exchange REST APIs to get, add, delete and  update employees and a websocket endpoint to get notified of updates from an employee database.<br> A server can be started using <code>com.scz.jxapi.exchanges.employee.EmployeeExchangeServer</code> class to serve these APIs.<br> The URL of the server must be set using the baseUrl property.<br> Notice how the 'employee' object present in APIs request and responses is used in multiple endpoints and its properties defined only once.
 * 
 * @see <a href="https://www.example.com/docs/employee">Reference documentation</a>
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public interface EmployeeExchange extends Exchange {
  
  String ID = "Employee";
  
  /**
   * @return Version 1 of the Employee API
   */
  EmployeeV1Api getEmployeeV1Api();
}
