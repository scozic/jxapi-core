package org.jxapi.exchanges.employee;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.jxapi.exchange.descriptor.gen.ExchangeDescriptor;
import org.jxapi.exchange.descriptor.parser.ExchangeDescriptorParser;
import org.jxapi.exchanges.demo.DemoExchangeGeneratorMain;
import org.jxapi.generator.java.JavaCodeGenUtil;
import org.jxapi.generator.java.exchange.ExchangeGeneratorMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmployeeExchangeGeneratorMain {

  private static Logger log = LoggerFactory.getLogger(DemoExchangeGeneratorMain.class);
  
  public static void generateDemoExchange() throws IOException {
    generateDemoExchange(Paths.get(".", "src", "test"));
  }

  public static void generateDemoExchange(Path srcTestFolder) throws IOException {
    log.info("Generating Employee exchange wrapper in current project");
    Path srcTestResourcesFolder = srcTestFolder.resolve(Paths.get("resources"));
    Path exchangeDescriptorFile = srcTestResourcesFolder.resolve(Paths.get("employeeExchange.yaml"));
    ExchangeDescriptor exchangeDescriptor = ExchangeDescriptorParser.fromYaml(exchangeDescriptorFile);
    Path srcTestJavaFolder = srcTestFolder.resolve(Paths.get("java"));
    Path mainPackagePath = Paths.get(exchangeDescriptor.getBasePackage().replace('.', '/'));
    Path genMainPackagesFolder = srcTestJavaFolder.resolve(mainPackagePath);
    JavaCodeGenUtil.deletePath(genMainPackagesFolder);
    ExchangeGeneratorMain.generateExchangeWrapper(exchangeDescriptor, srcTestJavaFolder);
    ExchangeGeneratorMain.generateExchangeWrapperDemos(exchangeDescriptor, srcTestJavaFolder);
    ExchangeGeneratorMain.generateDemoPropertiesFileTemplate(exchangeDescriptor, srcTestResourcesFolder);
    ExchangeGeneratorMain.generateExchangeWrapperReadme(exchangeDescriptor, Paths.get("."), "./doc/javadoc/", ".");
    log.info("Generating Employee exchange wrapper in current project:DONE");
  }

  public static void main(String[] args) {
    try {
      generateDemoExchange();
      System.exit(0);
    } catch (Throwable t) {
      log.error("Error in " + DemoExchangeGeneratorMain.class.getName() + " main", t);
      System.exit(-1);
    }
  }

}
