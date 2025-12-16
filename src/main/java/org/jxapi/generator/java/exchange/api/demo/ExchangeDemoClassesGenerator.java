package org.jxapi.generator.java.exchange.api.demo;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import org.jxapi.exchange.descriptor.gen.ConfigPropertyDescriptor;
import org.jxapi.exchange.descriptor.gen.ExchangeApiDescriptor;
import org.jxapi.exchange.descriptor.gen.ExchangeDescriptor;
import org.jxapi.exchange.descriptor.gen.RestEndpointDescriptor;
import org.jxapi.exchange.descriptor.gen.WebsocketEndpointDescriptor;
import org.jxapi.generator.java.exchange.ClassesGenerator;
import org.jxapi.generator.java.exchange.ExchangeGenUtil;
import org.jxapi.generator.java.exchange.properties.PropertiesClassGenerator;
import org.jxapi.generator.java.exchange.properties.PropertiesGenUtil;
import org.jxapi.util.CollectionUtil;

/**
 * Generates demo classes for an exchange, e.g. one snippet class for each REST
 * and Websocket endpoint of each API of the exchange.
 * 
 * @see ExchangeDescriptor
 */
public class ExchangeDemoClassesGenerator implements ClassesGenerator {
  
  private final ExchangeDescriptor exchangeDescriptor;
  private final List<ConfigPropertyDescriptor> demoProperties;

  /**
   * Constructor.
   * 
   * @param exchangeDescriptor the exchange descriptor
   * @param demoProperties     the demo configuration properties, if null or empty
   *                           then description or sample value replacements will
   *                           be performed using exchange descriptor constants
   *                           and config properties only.
   */
  public ExchangeDemoClassesGenerator(ExchangeDescriptor exchangeDescriptor, List<ConfigPropertyDescriptor> demoProperties) {
    this.exchangeDescriptor = exchangeDescriptor;
    this.demoProperties = Optional
        .ofNullable(demoProperties)
        .orElse(EndpointDemoGenUtil.collectDemoConfigProperties(exchangeDescriptor));
  }

  @Override
  public void generateClasses(Path outputFolder) throws IOException {
    for (ExchangeApiDescriptor api: exchangeDescriptor.getApis()) {  
      if (api.getRestEndpoints() != null) {
        for (RestEndpointDescriptor restApi: api.getRestEndpoints()) {
          RestEndpointDemoGenerator restEndpointDemoGenerator = new RestEndpointDemoGenerator(exchangeDescriptor, api, restApi, demoProperties);
          restEndpointDemoGenerator.writeJavaFile(outputFolder);
        }
      }
      
      if (api.getWebsocketEndpoints() != null) {
        for (WebsocketEndpointDescriptor websocketApi: api.getWebsocketEndpoints()) {
          WebsocketEndpointDemoGenerator websocketEndpointDemoGenerator = new WebsocketEndpointDemoGenerator(exchangeDescriptor, api, websocketApi, demoProperties);
          websocketEndpointDemoGenerator.writeJavaFile(outputFolder);
        }
      }
      
      if (!CollectionUtil.isEmpty(demoProperties)) {
        PropertiesClassGenerator pgen = new PropertiesClassGenerator(
            ExchangeGenUtil.getExchangeDemoPropertiesClassName(exchangeDescriptor), 
            exchangeDescriptor, 
            demoProperties,
            PropertiesGenUtil.DEMO_PREFIX);
        pgen.writeJavaFile(outputFolder);
      }
    }
  }

}
