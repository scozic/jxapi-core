package org.jxapi.generator.java.exchange.api;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Test;

import org.jxapi.exchange.descriptor.gen.ExchangeApiDescriptor;
import org.jxapi.exchange.descriptor.gen.ExchangeDescriptor;
import org.jxapi.exchange.descriptor.parser.ExchangeDescriptorParser;
import org.jxapi.generator.java.JavaCodeGenUtil;
import org.jxapi.generator.java.exchange.ClassesGeneratorTestUtil;

/**
 * Unit test for {@link ExchangeApiClassesGenerator}
 */
public class ExchangeApiClassesGeneratorTest {
  
  private static final Path BASE_PKG = Paths.get("com", "foo", "bar", "gen", "marketdata");
  
  private Path srcFolder;
  
  @After
  public void tearDown() throws IOException {
    if (srcFolder != null) {
      JavaCodeGenUtil.deletePath(srcFolder);
      srcFolder = null;
    }
  }

  @Test
  public void testGenerateExchangeApiClasses() throws IOException {
    srcFolder = ClassesGeneratorTestUtil.generateTmpDir();
    ExchangeDescriptor exchange = ExchangeDescriptorParser.fromJson(Paths.get(".", "src", "test", "resources", "testExchangeDescriptor.json"));
    ExchangeApiDescriptor api = exchange.getApis().get(0);
    ExchangeApiClassesGenerator generator = new ExchangeApiClassesGenerator(exchange, api, null);
    generator.generateClasses(srcFolder);
    
    checkJavaFilesCount(Paths.get("."), 3);
    checkSourceFileExists(Paths.get("MyTestExchangeMarketDataApi.java"));
    checkSourceFileExists(Paths.get("MyTestExchangeMarketDataApiImpl.java"));
    
    checkJavaFilesCount(Paths.get("pojo"), 10);
    checkSourceFileExists(Paths.get("pojo", "MyTestExchangeMarketDataExchangeInfoRequest.java"));
    checkSourceFileExists(Paths.get("pojo", "MyTestExchangeMarketDataExchangeInfoResponse.java"));
    checkSourceFileExists(Paths.get("pojo", "MyTestExchangeMarketDataExchangeInfoResponsePayload.java"));
    checkSourceFileExists(Paths.get("pojo", "MyTestExchangeMarketDataTickersRequest.java"));
    checkSourceFileExists(Paths.get("pojo", "MyTestExchangeMarketDataTickersResponse.java"));
    checkSourceFileExists(Paths.get("pojo", "MyTestExchangeMarketDataTickersResponsePayload.java"));
    checkSourceFileExists(Paths.get("pojo", "MyTestExchangeMarketDataTickerStreamRequest.java"));
    checkSourceFileExists(Paths.get("pojo", "MyTestExchangeMarketDataTickerStreamMessage.java"));
    
    checkJavaFilesCount(Paths.get("pojo/deserializers"), 8);
    checkSourceFileExists(Paths.get("pojo/deserializers", "MyTestExchangeMarketDataExchangeInfoRequestDeserializer.java"));
    checkSourceFileExists(Paths.get("pojo/deserializers", "MyTestExchangeMarketDataExchangeInfoResponseDeserializer.java"));
    checkSourceFileExists(Paths.get("pojo/deserializers", "MyTestExchangeMarketDataExchangeInfoResponsePayloadDeserializer.java"));
    checkSourceFileExists(Paths.get("pojo/deserializers", "MyTestExchangeMarketDataTickersRequestDeserializer.java"));
    checkSourceFileExists(Paths.get("pojo/deserializers", "MyTestExchangeMarketDataTickersResponseDeserializer.java"));
    checkSourceFileExists(Paths.get("pojo/deserializers", "MyTestExchangeMarketDataTickersResponsePayloadDeserializer.java"));
    checkSourceFileExists(Paths.get("pojo/deserializers", "MyTestExchangeMarketDataTickerStreamMessageDeserializer.java"));
    checkSourceFileExists(Paths.get("pojo/deserializers", "MyTestExchangeMarketDataTickerStreamRequestDeserializer.java"));
    
    checkJavaFilesCount(Paths.get("pojo/serializers"), 8);
    checkSourceFileExists(Paths.get("pojo/serializers", "MyTestExchangeMarketDataExchangeInfoRequestSerializer.java"));        
    checkSourceFileExists(Paths.get("pojo/serializers", "MyTestExchangeMarketDataExchangeInfoResponsePayloadSerializer.java"));
    checkSourceFileExists(Paths.get("pojo/serializers", "MyTestExchangeMarketDataExchangeInfoResponseSerializer.java"));       
    checkSourceFileExists(Paths.get("pojo/serializers", "MyTestExchangeMarketDataTickersRequestSerializer.java"));             
    checkSourceFileExists(Paths.get("pojo/serializers", "MyTestExchangeMarketDataTickersResponsePayloadSerializer.java"));     
    checkSourceFileExists(Paths.get("pojo/serializers", "MyTestExchangeMarketDataTickersResponseSerializer.java"));
    checkSourceFileExists(Paths.get("pojo/serializers", "MyTestExchangeMarketDataTickerStreamRequestSerializer.java"));
    checkSourceFileExists(Paths.get("pojo/serializers", "MyTestExchangeMarketDataTickerStreamMessageSerializer.java"));
  }
  
  @Test
  public void testGenerateExchangeApiClassesNoRestEndpoints() throws IOException {
    srcFolder = ClassesGeneratorTestUtil.generateTmpDir();
    ExchangeDescriptor exchange = ExchangeDescriptorParser.fromJson(Paths.get(".", "src", "test", "resources", "testExchangeDescriptor.json"));
    ExchangeApiDescriptor api = exchange.getApis().get(0);
    api.setRestEndpoints(null);
    ExchangeApiClassesGenerator generator = new ExchangeApiClassesGenerator(exchange, api, null);
    generator.generateClasses(srcFolder);
    
    checkJavaFilesCount(Paths.get("."), 3);
    checkSourceFileExists(Paths.get("MyTestExchangeMarketDataApi.java"));
    checkSourceFileExists(Paths.get("MyTestExchangeMarketDataApiImpl.java"));
    
    checkJavaFilesCount(Paths.get("pojo"), 4);
    checkSourceFileExists(Paths.get("pojo", "MyTestExchangeMarketDataTickerStreamRequest.java"));
    checkSourceFileExists(Paths.get("pojo", "MyTestExchangeMarketDataTickerStreamMessage.java"));
    
    checkJavaFilesCount(Paths.get("pojo/deserializers"), 2);
    checkSourceFileExists(Paths.get("pojo/deserializers", "MyTestExchangeMarketDataTickerStreamMessageDeserializer.java"));
    checkSourceFileExists(Paths.get("pojo/deserializers", "MyTestExchangeMarketDataTickerStreamRequestDeserializer.java"));
    
    checkJavaFilesCount(Paths.get("pojo/serializers"), 2);
    checkSourceFileExists(Paths.get("pojo/serializers", "MyTestExchangeMarketDataTickerStreamRequestSerializer.java"));
    checkSourceFileExists(Paths.get("pojo/serializers", "MyTestExchangeMarketDataTickerStreamMessageSerializer.java"));
  }
  
  @Test
  public void testGenerateExchangeApiClassesNoWebsocketEndpoint() throws IOException {
    srcFolder = ClassesGeneratorTestUtil.generateTmpDir();
    ExchangeDescriptor exchange = ExchangeDescriptorParser.fromJson(Paths.get(".", "src", "test", "resources", "testExchangeDescriptor.json"));
    ExchangeApiDescriptor api = exchange.getApis().get(0);
    api.setWebsocketEndpoints(null);
    ExchangeApiClassesGenerator generator = new ExchangeApiClassesGenerator(exchange, api, null);
    generator.generateClasses(srcFolder);
    
    checkJavaFilesCount(Paths.get("."), 3);
    checkSourceFileExists(Paths.get("MyTestExchangeMarketDataApi.java"));
    checkSourceFileExists(Paths.get("MyTestExchangeMarketDataApiImpl.java"));
    
    checkJavaFilesCount(Paths.get("pojo"), 8);
    checkSourceFileExists(Paths.get("pojo", "MyTestExchangeMarketDataExchangeInfoRequest.java"));
    checkSourceFileExists(Paths.get("pojo", "MyTestExchangeMarketDataExchangeInfoResponse.java"));
    checkSourceFileExists(Paths.get("pojo", "MyTestExchangeMarketDataExchangeInfoResponsePayload.java"));
    checkSourceFileExists(Paths.get("pojo", "MyTestExchangeMarketDataTickersRequest.java"));
    checkSourceFileExists(Paths.get("pojo", "MyTestExchangeMarketDataTickersResponse.java"));
    checkSourceFileExists(Paths.get("pojo", "MyTestExchangeMarketDataTickersResponsePayload.java"));
    
    checkJavaFilesCount(Paths.get("pojo/deserializers"), 6);
    checkSourceFileExists(Paths.get("pojo/deserializers", "MyTestExchangeMarketDataExchangeInfoRequestDeserializer.java"));
    checkSourceFileExists(Paths.get("pojo/deserializers", "MyTestExchangeMarketDataExchangeInfoResponseDeserializer.java"));
    checkSourceFileExists(Paths.get("pojo/deserializers", "MyTestExchangeMarketDataExchangeInfoResponsePayloadDeserializer.java"));
    checkSourceFileExists(Paths.get("pojo/deserializers", "MyTestExchangeMarketDataTickersRequestDeserializer.java"));
    checkSourceFileExists(Paths.get("pojo/deserializers", "MyTestExchangeMarketDataTickersResponseDeserializer.java"));
    checkSourceFileExists(Paths.get("pojo/deserializers", "MyTestExchangeMarketDataTickersResponsePayloadDeserializer.java"));
    
    checkJavaFilesCount(Paths.get("pojo/serializers"), 6);
    checkSourceFileExists(Paths.get("pojo/serializers", "MyTestExchangeMarketDataExchangeInfoRequestSerializer.java"));        
    checkSourceFileExists(Paths.get("pojo/serializers", "MyTestExchangeMarketDataExchangeInfoResponsePayloadSerializer.java"));
    checkSourceFileExists(Paths.get("pojo/serializers", "MyTestExchangeMarketDataExchangeInfoResponseSerializer.java"));       
    checkSourceFileExists(Paths.get("pojo/serializers", "MyTestExchangeMarketDataTickersRequestSerializer.java"));             
    checkSourceFileExists(Paths.get("pojo/serializers", "MyTestExchangeMarketDataTickersResponsePayloadSerializer.java"));     
    checkSourceFileExists(Paths.get("pojo/serializers", "MyTestExchangeMarketDataTickersResponseSerializer.java"));
  }
  
  private void checkJavaFilesCount(Path relativePkg, int count) {
    ClassesGeneratorTestUtil.checkJavaFilesCount(srcFolder.resolve(BASE_PKG).resolve(relativePkg), count);
  }
  
  private Path checkSourceFileExists(Path srcFilePath) {
    return ClassesGeneratorTestUtil.checkSourceFileExists(srcFolder.resolve(BASE_PKG), srcFilePath);
  }

}
