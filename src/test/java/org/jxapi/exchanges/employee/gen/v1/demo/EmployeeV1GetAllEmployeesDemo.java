package org.jxapi.exchanges.employee.gen.v1.demo;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.jxapi.exchange.ExchangeApiObserver;
import org.jxapi.exchanges.employee.gen.EmployeeExchange;
import org.jxapi.exchanges.employee.gen.EmployeeExchangeImpl;
import org.jxapi.exchanges.employee.gen.v1.EmployeeV1Api;
import org.jxapi.exchanges.employee.gen.v1.pojo.Employee;
import org.jxapi.netutils.rest.RestResponse;
import org.jxapi.util.DemoUtil;
import javax.annotation.processing.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snippet to test call to {@link EmployeeV1Api#getAllEmployees()})}<br>
 */
@Generated("org.jxapi.generator.java.exchange.api.demo.RestEndpointDemoGenerator")
public class EmployeeV1GetAllEmployeesDemo {
  private static final Logger log = LoggerFactory.getLogger(EmployeeV1GetAllEmployeesDemo.class);
  
  /**
   * Submits a call to {@link EmployeeV1Api#getAllEmployees()}and waits for response.
   * @param configProperties  The configuration properties to instantiate exchange with
   * @param apiObserver API observer that will notified of events. Is subscribed before REST API call and unsubscribed right after. Ignored if <code>null</code>
   * @return Response data resulting from this API call
   * @throws InterruptedException eventually thrown waiting for response
   * @throws ExecutionException raised if response is not OK, see {@link RestResponse#isOk()}
   */
  public static RestResponse<List<Employee>> execute(Properties configProperties, ExchangeApiObserver apiObserver) throws InterruptedException, ExecutionException {
    EmployeeExchange exchange = new EmployeeExchangeImpl("test-" + EmployeeExchange.ID, configProperties);
    EmployeeV1Api api = exchange.getEmployeeV1Api();
    log.info("Calling org.jxapi.exchanges.employee.gen.v1.EmployeeV1Api.getAllEmployees() API");
    if (apiObserver != null) {
      api.subscribeObserver(apiObserver);
    }
    try {
      return DemoUtil.checkResponse(api.getAllEmployees());
    }
    finally {
      if (apiObserver != null) {
        api.unsubscribeObserver(apiObserver);
      }
      exchange.dispose();
    }
  }
  
  /**
   * Runs REST endpoint demo snippet calling {@link EmployeeV1Api#getAllEmployees()}
   * @param args no argument expected
   */
  public static void main(String[] args) {
    try {
      execute(DemoUtil.loadDemoExchangeProperties(EmployeeExchange.ID),
              DemoUtil::logRestApiEvent);
      System.exit(0);
    }
    catch (Throwable t) {
      log.error("Exception raised from main()", t);
      System.exit(-1);
    }
  }
}
