package com.scz.jxapi.exchanges.employee.gen;

import java.util.Properties;

import com.scz.jxapi.exchange.AbstractExchange;
import com.scz.jxapi.exchanges.employee.gen.v1.EmployeeV1Api;
import com.scz.jxapi.exchanges.employee.gen.v1.EmployeeV1ApiImpl;
import javax.annotation.processing.Generated;

/**
 * Actual implementation of {@link EmployeeExchange}<br>
 */
@Generated("com.scz.jxapi.generator.java.exchange.ExchangeInterfaceImplementationGenerator")
public class EmployeeExchangeImpl extends AbstractExchange implements EmployeeExchange {
  
  /**
   * Base REST API URL
   */
  public static final String HTTP_URL = "BASEURL";
  
  private final EmployeeV1Api employeeV1Api;
  
  public EmployeeExchangeImpl(String exchangeName, Properties properties) {
    super(ID, exchangeName, properties);
    this.employeeV1Api = addApi(new EmployeeV1ApiImpl(getName(), properties));
  }
  
  @Override
  public EmployeeV1Api getEmployeeV1Api() {
    return this.employeeV1Api;
  }
  
}
