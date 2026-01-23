package org.jxapi.generator.java.exchange.api.demo;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Test;

import org.jxapi.exchange.descriptor.gen.ExchangeDescriptor;
import org.jxapi.exchange.descriptor.parser.ExchangeDescriptorParser;
import org.jxapi.generator.java.JavaCodeGenUtil;
import org.jxapi.generator.java.exchange.ClassesGeneratorTestUtil;

/**
 * Unit test for {@link ExchangeDemoClassesGenerator}
 */
public class ExchangeDemoClassesGeneratorTest {
  
  private static final Path BASE_PKG = Paths.get("com", "foo", "bar", "gen");
  
  private Path srcFolder;
  
  @After
  public void tearDown() throws IOException {
    if (srcFolder != null) {
      JavaCodeGenUtil.deletePath(srcFolder);
      srcFolder = null;
    }
  }

  @Test
  public void testGenerateExchangeDemoClasses() throws IOException {
    srcFolder = ClassesGeneratorTestUtil.generateTmpDir();
    ExchangeDescriptor exchange = ExchangeDescriptorParser.fromJson(Paths.get(".", "src", "test", "resources", "testExchangeDescriptor.json"));
    new ExchangeDemoClassesGenerator(exchange, EndpointDemoGenUtil.collectDemoConfigProperties(exchange)).generateClasses(srcFolder);
    Path pkgPath = Paths.get(".");
    checkJavaFilesCount(pkgPath, 2);
    ClassesGeneratorTestUtil.checkSourceFileExists(srcFolder.resolve(BASE_PKG), Paths.get("MyTestExchangeDemoProperties.java"));
    pkgPath = pkgPath.resolve("marketdata");
    checkJavaFilesCount(pkgPath, 1);
    pkgPath = pkgPath.resolve("demo");
    checkJavaFilesCount(pkgPath, 3);
    checkSourceFileExists(Paths.get("MyTestExchangeMarketDataExchangeInfoDemo.java"));
    checkSourceFileExists(Paths.get("MyTestExchangeMarketDataTickersDemo.java"));
    checkSourceFileExists(Paths.get("MyTestExchangeMarketDataTickerStreamDemo.java"));
  }
  
  @Test
  public void testGenerateExchangeDemoClasses_NullRestEndpoints() throws IOException {
    srcFolder = ClassesGeneratorTestUtil.generateTmpDir();
    ExchangeDescriptor exchange = ExchangeDescriptorParser.fromJson(Paths.get(".", "src", "test", "resources", "testExchangeDescriptor.json"));
    exchange.getApis().get(0).setRestEndpoints(null);
    new ExchangeDemoClassesGenerator(exchange, null).generateClasses(srcFolder);
    Path pkgPath = Paths.get(".");
    checkJavaFilesCount(pkgPath, 2);
    pkgPath = pkgPath.resolve("marketdata");
    checkJavaFilesCount(pkgPath, 1);
    pkgPath = pkgPath.resolve("demo");
    checkJavaFilesCount(pkgPath, 1);
    checkSourceFileExists(Paths.get("MyTestExchangeMarketDataTickerStreamDemo.java"));
  }
  
  @Test
  public void testGenerateExchangeDemoClasses_NullWsEndpoints() throws IOException {
    srcFolder = ClassesGeneratorTestUtil.generateTmpDir();
    ExchangeDescriptor exchange = ExchangeDescriptorParser.fromJson(Paths.get(".", "src", "test", "resources", "testExchangeDescriptor.json"));
    exchange.getApis().get(0).setWebsocketEndpoints(null);
    new ExchangeDemoClassesGenerator(exchange, null).generateClasses(srcFolder);
    Path pkgPath = Paths.get(".");
    checkJavaFilesCount(pkgPath, 2);
    ClassesGeneratorTestUtil.checkSourceFileExists(srcFolder.
        resolve(BASE_PKG),
        Paths.get("MyTestExchangeDemoProperties.java"));
    pkgPath = pkgPath.resolve("marketdata");
    checkJavaFilesCount(pkgPath, 1);
    pkgPath = pkgPath.resolve("demo");
    checkJavaFilesCount(pkgPath, 2);
    checkSourceFileExists(Paths.get("MyTestExchangeMarketDataExchangeInfoDemo.java"));
    checkSourceFileExists(Paths.get("MyTestExchangeMarketDataTickersDemo.java"));
  }
  
  private void checkJavaFilesCount(Path relativePkg, int count) {
    ClassesGeneratorTestUtil.checkJavaFilesCount(srcFolder.resolve(BASE_PKG).resolve(relativePkg), count);
  }
  
  private Path checkSourceFileExists(Path srcFilePath) {
    return ClassesGeneratorTestUtil.checkSourceFileExists(srcFolder.
                                resolve(BASE_PKG)
                                .resolve("marketdata")
                                .resolve("demo"), 
                                srcFilePath);
  }

}
