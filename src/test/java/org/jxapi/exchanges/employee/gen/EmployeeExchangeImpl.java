package org.jxapi.exchanges.employee.gen;

import java.util.Properties;

import javax.annotation.processing.Generated;
import org.jxapi.exchange.AbstractExchange;
import org.jxapi.exchanges.employee.gen.v1.EmployeeV1Api;
import org.jxapi.exchanges.employee.gen.v1.EmployeeV1ApiImpl;
import org.jxapi.util.EncodingUtil;

/**
 * Actual implementation of {@link EmployeeExchange}<br>
 */
@Generated("org.jxapi.generator.java.exchange.ExchangeInterfaceImplementationGenerator")
public class EmployeeExchangeImpl extends AbstractExchange implements EmployeeExchange {
  
  private final EmployeeV1Api v1Api;
  
  public EmployeeExchangeImpl(String exchangeName, Properties properties) {
    super(ID,
          VERSION,
          exchangeName,
          properties,
          EncodingUtil.substituteArguments("${config.server.baseHttpUrl}", "config.server.baseHttpUrl", EmployeeProperties.Server.getBaseHttpUrl(properties)),
          false);
    // Network
    createHttpClient("httpDefault",
      "org.jxapi.exchanges.demo.net.DemoExchangeHttpRequestInterceptorFactory",
      null,
      null);
    createWebsocketClient("wsDefault",
      EncodingUtil.substituteArguments("${config.server.baseWebsocketUrl}", "config.server.baseWebsocketUrl", EmployeeProperties.Server.getBaseWebsocketUrl(properties)),
      null,
      "org.jxapi.exchanges.demo.net.DemoExchangeWebsocketHookFactory");
    
     // APIs
    this.v1Api = addApi(EmployeeV1Api.ID, new EmployeeV1ApiImpl(this, exchangeObserver));
  }
  
  @Override
  public EmployeeV1Api getV1Api() {
    return this.v1Api;
  }
  
}
