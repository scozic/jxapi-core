package org.jxapi.exchanges.employee.gen.v1.demo;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import javax.annotation.processing.Generated;
import org.jxapi.exchange.ExchangeObserver;
import org.jxapi.exchanges.employee.gen.EmployeeDemoProperties;
import org.jxapi.exchanges.employee.gen.EmployeeExchange;
import org.jxapi.exchanges.employee.gen.EmployeeExchangeImpl;
import org.jxapi.exchanges.employee.gen.v1.EmployeeV1Api;
import org.jxapi.exchanges.employee.gen.v1.pojo.Employee;
import org.jxapi.netutils.rest.RestResponse;
import org.jxapi.util.DemoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snippet to test call to {@link EmployeeV1Api#getEmployee(java.lang.Integer)})}<br>
 */
@Generated("org.jxapi.generator.java.exchange.api.demo.RestEndpointDemoGenerator")
public class EmployeeV1GetEmployeeDemo {
  private static final Logger log = LoggerFactory.getLogger(EmployeeV1GetEmployeeDemo.class);
  
  /**
   * Creates a sample value for the id field of type Integer using sample value(s) defined in demo configuration properties.
   * 
   * @param properties the configuration properties to use for the sample value generation.
   */
  public static Integer createId(Properties properties) {
    return EmployeeDemoProperties.V1.Rest.GetEmployee.getId(properties);
  }
  
  /**
   * Submits a call to {@link EmployeeV1Api#getEmployee(java.lang.Integer)}and waits for response.
   * @param request     The request to submit
   * @param configProperties  The configuration properties to instantiate exchange with
   * @param observer API observer that will notified of events. Is subscribed before REST API call and unsubscribed right after. Ignored if <code>null</code>
   * @return Response data resulting from this API call
   * @throws InterruptedException eventually thrown waiting for response
   * @throws ExecutionException raised if response is not OK, see {@link RestResponse#isOk()}
   */
  public static RestResponse<Employee> execute(Integer request, Properties configProperties, ExchangeObserver observer) throws InterruptedException, ExecutionException {
    EmployeeExchange exchange = new EmployeeExchangeImpl("test-" + EmployeeExchange.ID, configProperties);
    EmployeeV1Api api = exchange.getV1Api();
    log.info("Calling org.jxapi.exchanges.employee.gen.v1.EmployeeV1Api.getEmployee() API with request:{}", request);
    if (observer != null) {
      exchange.subscribeObserver(observer);
    }
    try {
      return DemoUtil.checkResponse(api.getEmployee(request));
    }
    finally {
      if (observer != null) {
        exchange.unsubscribeObserver(observer);
      }
      exchange.dispose();
    }
  }
  
  /**
   * Runs REST endpoint demo snippet calling {@link EmployeeV1Api#getEmployee(java.lang.Integer)}
   * @param args no argument expected
   */
  public static void main(String[] args) {
    try {
      Properties properties = DemoUtil.loadDemoExchangeProperties(EmployeeExchange.ID);
      execute(createId(properties),
              properties,
              DemoUtil::logRestApiEvent);
      System.exit(0);
    }
    catch (Throwable t) {
      log.error("Exception raised from main()", t);
      System.exit(-1);
    }
  }
}
