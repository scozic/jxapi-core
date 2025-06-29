package org.jxapi.generator.java.exchange.api.demo;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.jxapi.exchange.descriptor.DefaultConfigProperty;
import org.jxapi.exchange.descriptor.ExchangeApiDescriptor;
import org.jxapi.exchange.descriptor.ExchangeDescriptor;
import org.jxapi.exchange.descriptor.RestEndpointDescriptor;
import org.jxapi.exchange.descriptor.WebsocketEndpointDescriptor;
import org.jxapi.generator.java.exchange.ClassesGenerator;
import org.jxapi.generator.java.exchange.ExchangeGenUtil;
import org.jxapi.generator.java.exchange.constants.PropertiesClassGenerator;
import org.jxapi.util.PlaceHolderResolver;

/**
 * Generates demo classes for an exchange, e.g. one snippet class for each REST
 * and Websocket endpoint of each API of the exchange.
 * 
 * @see ExchangeDescriptor
 */
public class ExchangeDemoClassesGenerator implements ClassesGenerator {
  
  private ExchangeDescriptor exchangeDescriptor;

  /**
   * Constructor.
   * 
   * @param exchangeDescriptor the exchange descriptor
   */
  public ExchangeDemoClassesGenerator(ExchangeDescriptor exchangeDescriptor) {
    this.exchangeDescriptor = exchangeDescriptor;
  }

  @Override
  public void generateClasses(Path outputFolder) throws IOException {
    for (ExchangeApiDescriptor api: exchangeDescriptor.getApis()) {  
      if (api.getRestEndpoints() != null) {
        for (RestEndpointDescriptor restApi: api.getRestEndpoints()) {
          RestEndpointDemoGenerator restEndpointDemoGenerator = new RestEndpointDemoGenerator(exchangeDescriptor, api, restApi);
          restEndpointDemoGenerator.writeJavaFile(outputFolder);
        }
      }
      
      if (api.getWebsocketEndpoints() != null) {
        for (WebsocketEndpointDescriptor websocketApi: api.getWebsocketEndpoints()) {
          WebsocketEndpointDemoGenerator websocketEndpointDemoGenerator = new WebsocketEndpointDemoGenerator(exchangeDescriptor, api, websocketApi);
          websocketEndpointDemoGenerator.writeJavaFile(outputFolder);
        }
      }
      
      PlaceHolderResolver docPlaceHolderResolver = 
          PlaceHolderResolver.create(ExchangeGenUtil.getDescriptionReplacements(exchangeDescriptor, null));
      // Generate properties interface
      List<DefaultConfigProperty> properties = exchangeDescriptor.getDemoProperties();
      if (properties != null) {
        PropertiesClassGenerator pgen = new PropertiesClassGenerator(
            ExchangeGenUtil.getExchangeDemoPropertiesInterfaceName(exchangeDescriptor), 
            exchangeDescriptor.getId(), 
            properties,
            docPlaceHolderResolver);
        pgen.writeJavaFile(outputFolder);
      }
    }
  }

}
