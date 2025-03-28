package org.jxapi.exchanges.employee.gen;

import java.util.Properties;

import javax.annotation.processing.Generated;
import org.jxapi.exchange.AbstractExchange;
import org.jxapi.exchanges.employee.gen.v1.EmployeeV1Api;
import org.jxapi.exchanges.employee.gen.v1.EmployeeV1ApiImpl;

/**
 * Actual implementation of {@link EmployeeExchange}<br>
 */
@Generated("org.jxapi.generator.java.exchange.ExchangeInterfaceImplementationGenerator")
public class EmployeeExchangeImpl extends AbstractExchange implements EmployeeExchange {
  
  /**
   * Base REST API URL
   */
  public static final String HTTP_URL = "BASEURL";
  
  private final EmployeeV1Api employeeV1Api;
  
  public EmployeeExchangeImpl(String exchangeName, Properties properties) {
    super(ID, VERSION, exchangeName, properties);
    this.employeeV1Api = addApi(new EmployeeV1ApiImpl(getName(), properties));
  }
  
  @Override
  public EmployeeV1Api getEmployeeV1Api() {
    return this.employeeV1Api;
  }
  
}
