package org.jxapi.generator.java.exchange;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Test;

import org.jxapi.exchange.descriptor.gen.ExchangeDescriptor;
import org.jxapi.exchange.descriptor.parser.ExchangeDescriptorParser;
import org.jxapi.generator.java.JavaCodeGenUtil;

/**
 * Unit test for {@link ExchangeClassesGenerator}
 */
public class ExchangeClassesGeneratorTest {
  
  private Path srcFolder;
  
  @After
  public void tearDown() throws IOException {
    if (srcFolder != null) {
      JavaCodeGenUtil.deletePath(srcFolder);
      srcFolder = null;
    }
  }

  @Test
  public void testGenerateExchangeClasses() throws IOException {
    srcFolder = ClassesGeneratorTestUtil.generateTmpDir();
    ExchangeDescriptor exchange = ExchangeDescriptorParser.fromJson(Paths.get(".", "src", "test", "resources", "testExchangeDescriptor.json"));
    ExchangeClassesGenerator generator = new ExchangeClassesGenerator(exchange);
    generator.generateClasses(srcFolder);
    
    checkJavaFilesCount(Paths.get("."), 5);
    checkSourceFileExists(Paths.get("MyTestExchangeExchange.java"));
    checkSourceFileExists(Paths.get("MyTestExchangeExchangeImpl.java"));
    checkSourceFileExists(Paths.get("MyTestExchangeConstants.java"));
    checkSourceFileExists(Paths.get("MyTestExchangeProperties.java"));
    
    checkJavaFilesCount(Paths.get("marketdata"), 3);
    checkSourceFileExists(Paths.get("marketdata", "MyTestExchangeMarketDataApi.java"));
    checkSourceFileExists(Paths.get("marketdata", "MyTestExchangeMarketDataApiImpl.java"));
    
    checkJavaFilesCount(Paths.get("marketdata", "pojo"), 10);
    checkSourceFileExists(Paths.get("marketdata", "pojo", "MyTestExchangeMarketDataExchangeInfoRequest.java"));
    checkSourceFileExists(Paths.get("marketdata", "pojo", "MyTestExchangeMarketDataExchangeInfoResponse.java"));
    checkSourceFileExists(Paths.get("marketdata", "pojo", "MyTestExchangeMarketDataExchangeInfoResponsePayload.java"));
    checkSourceFileExists(Paths.get("marketdata", "pojo", "MyTestExchangeMarketDataTickersRequest.java"));
    checkSourceFileExists(Paths.get("marketdata", "pojo", "MyTestExchangeMarketDataTickersResponse.java"));
    checkSourceFileExists(Paths.get("marketdata", "pojo", "MyTestExchangeMarketDataTickersResponsePayload.java"));
    checkSourceFileExists(Paths.get("marketdata", "pojo", "MyTestExchangeMarketDataTickerStreamRequest.java"));
    checkSourceFileExists(Paths.get("marketdata", "pojo", "MyTestExchangeMarketDataTickerStreamMessage.java"));
    
    checkJavaFilesCount(Paths.get("marketdata", "pojo", "deserializers"), 8);
    checkSourceFileExists(Paths.get("marketdata", "pojo", "deserializers", "MyTestExchangeMarketDataExchangeInfoRequestDeserializer.java"));
    checkSourceFileExists(Paths.get("marketdata", "pojo", "deserializers", "MyTestExchangeMarketDataExchangeInfoResponseDeserializer.java"));
    checkSourceFileExists(Paths.get("marketdata", "pojo", "deserializers", "MyTestExchangeMarketDataExchangeInfoResponsePayloadDeserializer.java"));
    checkSourceFileExists(Paths.get("marketdata", "pojo", "deserializers", "MyTestExchangeMarketDataTickersRequestDeserializer.java"));
    checkSourceFileExists(Paths.get("marketdata", "pojo", "deserializers", "MyTestExchangeMarketDataTickersResponseDeserializer.java"));
    checkSourceFileExists(Paths.get("marketdata", "pojo", "deserializers", "MyTestExchangeMarketDataTickersResponsePayloadDeserializer.java"));
    checkSourceFileExists(Paths.get("marketdata", "pojo", "deserializers", "MyTestExchangeMarketDataTickerStreamMessageDeserializer.java"));
    checkSourceFileExists(Paths.get("marketdata", "pojo", "deserializers", "MyTestExchangeMarketDataTickerStreamRequestDeserializer.java"));
    
    checkJavaFilesCount(Paths.get("marketdata", "pojo", "serializers"), 8);
    checkSourceFileExists(Paths.get("marketdata", "pojo", "serializers", "MyTestExchangeMarketDataExchangeInfoRequestSerializer.java"));        
    checkSourceFileExists(Paths.get("marketdata", "pojo", "serializers", "MyTestExchangeMarketDataExchangeInfoResponsePayloadSerializer.java"));
    checkSourceFileExists(Paths.get("marketdata", "pojo", "serializers", "MyTestExchangeMarketDataExchangeInfoResponseSerializer.java"));       
    checkSourceFileExists(Paths.get("marketdata", "pojo", "serializers", "MyTestExchangeMarketDataTickersRequestSerializer.java"));             
    checkSourceFileExists(Paths.get("marketdata", "pojo", "serializers", "MyTestExchangeMarketDataTickersResponsePayloadSerializer.java"));     
    checkSourceFileExists(Paths.get("marketdata", "pojo", "serializers", "MyTestExchangeMarketDataTickersResponseSerializer.java"));
    checkSourceFileExists(Paths.get("marketdata", "pojo", "serializers", "MyTestExchangeMarketDataTickerStreamRequestSerializer.java"));
    checkSourceFileExists(Paths.get("marketdata", "pojo", "serializers", "MyTestExchangeMarketDataTickerStreamMessageSerializer.java"));
  }
  
  @Test
  public void testGenerateExchangeClassesNoConstantsNoProperties() throws IOException {
    srcFolder = ClassesGeneratorTestUtil.generateTmpDir();
    ExchangeDescriptor exchange = ExchangeDescriptorParser.fromJson(Paths.get(".", "src", "test", "resources", "testExchangeDescriptor.json"));
    exchange.setProperties(null);
    exchange.setConstants(null);
    ExchangeClassesGenerator generator = new ExchangeClassesGenerator(exchange);
    generator.generateClasses(srcFolder);
    
    checkJavaFilesCount(Paths.get("."), 3);
    checkSourceFileExists(Paths.get("MyTestExchangeExchange.java"));
    checkSourceFileExists(Paths.get("MyTestExchangeExchangeImpl.java"));
  }
  
  private void checkJavaFilesCount(Path relativePkg, int count) {
    ClassesGeneratorTestUtil.checkJavaFilesCount(srcFolder.resolve(Paths.get("com", "foo", "bar", "gen").resolve(relativePkg)), count);
  }
  
  private Path checkSourceFileExists(Path srcFilePath) {
    return ClassesGeneratorTestUtil.checkSourceFileExists(srcFolder.resolve(Paths.get("com", "foo", "bar", "gen")), srcFilePath);
  }

}
