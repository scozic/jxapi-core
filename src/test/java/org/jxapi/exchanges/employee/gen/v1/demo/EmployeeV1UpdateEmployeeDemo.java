package org.jxapi.exchanges.employee.gen.v1.demo;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import javax.annotation.processing.Generated;
import org.jxapi.exchange.ExchangeApiObserver;
import org.jxapi.exchanges.employee.gen.EmployeeConstants;
import org.jxapi.exchanges.employee.gen.EmployeeExchange;
import org.jxapi.exchanges.employee.gen.EmployeeExchangeImpl;
import org.jxapi.exchanges.employee.gen.EmployeeProperties;
import org.jxapi.exchanges.employee.gen.v1.EmployeeV1Api;
import org.jxapi.exchanges.employee.gen.v1.pojo.Employee;
import org.jxapi.netutils.rest.RestResponse;
import org.jxapi.util.DemoUtil;
import org.jxapi.util.EncodingUtil;
import org.jxapi.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snippet to test call to {@link EmployeeV1Api#updateEmployee(org.jxapi.exchanges.employee.gen.v1.pojo.Employee)})}<br>
 */
@Generated("org.jxapi.generator.java.exchange.api.demo.RestEndpointDemoGenerator")
public class EmployeeV1UpdateEmployeeDemo {
  private static final Logger log = LoggerFactory.getLogger(EmployeeV1UpdateEmployeeDemo.class);
  
  /**
   * Creates a sample value for the request field of type Employee using sample value(s) defined in the field descriptor.
   * 
   * @param properties the configuration properties to use for the sample value generation.
   */
  public static Employee createRequest(Properties properties) {
    Employee request = new Employee();
    request.setId(Integer.valueOf(EncodingUtil.substituteArguments("${config.demoEmployeeId}", "config.demoEmployeeId", PropertiesUtil.getString(properties, EmployeeProperties.DEMO_EMPLOYEE_ID))));
    request.setFirstName("John");
    request.setLastName("Doe");
    request.setProfile(EncodingUtil.substituteArguments("${constants.profile.regular}", "constants.profile.regular", EmployeeConstants.Profile.REGULAR));
    return request;
  }
  
  /**
   * Submits a call to {@link EmployeeV1Api#updateEmployee(org.jxapi.exchanges.employee.gen.v1.pojo.Employee)}and waits for response.
   * @param request     The request to submit
   * @param configProperties  The configuration properties to instantiate exchange with
   * @param apiObserver API observer that will notified of events. Is subscribed before REST API call and unsubscribed right after. Ignored if <code>null</code>
   * @return Response data resulting from this API call
   * @throws InterruptedException eventually thrown waiting for response
   * @throws ExecutionException raised if response is not OK, see {@link RestResponse#isOk()}
   */
  public static RestResponse<String> execute(Employee request, Properties configProperties, ExchangeApiObserver apiObserver) throws InterruptedException, ExecutionException {
    EmployeeExchange exchange = new EmployeeExchangeImpl("test-" + EmployeeExchange.ID, configProperties);
    EmployeeV1Api api = exchange.getEmployeeV1Api();
    log.info("Calling org.jxapi.exchanges.employee.gen.v1.EmployeeV1Api.updateEmployee() API with request:{}", request);
    if (apiObserver != null) {
      api.subscribeObserver(apiObserver);
    }
    try {
      return DemoUtil.checkResponse(api.updateEmployee(request));
    }
    finally {
      if (apiObserver != null) {
        api.unsubscribeObserver(apiObserver);
      }
      exchange.dispose();
    }
  }
  
  /**
   * Runs REST endpoint demo snippet calling {@link EmployeeV1Api#updateEmployee(org.jxapi.exchanges.employee.gen.v1.pojo.Employee)}
   * @param args no argument expected
   */
  public static void main(String[] args) {
    try {
      Properties properties = DemoUtil.loadDemoExchangeProperties(EmployeeExchange.ID);
      execute(createRequest(properties),
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
