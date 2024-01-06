package com.scz.jxapi.generator.exchange;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Test;

import com.scz.jxapi.generator.JavaCodeGenerationUtil;

/**
 * Unit test for {@link ExchangeApiClassesGenerator}
 */
public class ExchangeApiClassesGeneratorTest {
	
	private static final Path BASE_PKG = Paths.get("com", "foo", "bar", "gen", "marketdata");
	
	private Path srcFolder;
	
	@After
	public void tearDown() throws IOException {
		if (srcFolder != null) {
			JavaCodeGenerationUtil.deletePath(srcFolder);
			srcFolder = null;
		}
	}

	@Test
	public void testGenerateExchangeApiClasses() throws IOException {
		srcFolder = ClassesGeneratorTestUtil.generateTmpDir();
		ExchangeDescriptor exchange = new ExchangeDescriptorParser().fromJson(Paths.get(".", "src", "test", "resources", "testCEXDescriptor.json"));
		ExchangeApiDescriptor api = exchange.getApis().get(0);
		ExchangeApiClassesGenerator generator = new ExchangeApiClassesGenerator(exchange, api);
		generator.generateClasses(srcFolder);
		
		checkJavaFilesCount(Paths.get("."), 5);
		checkSourceFileExists(Paths.get("MyTestCEXMarketDataApi.java"));
		checkSourceFileExists(Paths.get("MyTestCEXMarketDataApiImpl.java"));
		
		checkJavaFilesCount(Paths.get("deserializers"), 5);
		checkSourceFileExists(Paths.get("deserializers", "MyTestCEXMarketDataExchangeInfoResponseDeserializer.java"));
		checkSourceFileExists(Paths.get("deserializers", "MyTestCEXMarketDataExchangeInfoResponsePayloadDeserializer.java"));
		checkSourceFileExists(Paths.get("deserializers", "MyTestCEXMarketDataTickersResponseDeserializer.java"));
		checkSourceFileExists(Paths.get("deserializers", "MyTestCEXMarketDataTickersResponsePayloadDeserializer.java"));
		checkSourceFileExists(Paths.get("deserializers", "MyTestCEXMarketDataTickerStreamMessageDeserializer.java"));
		
		checkJavaFilesCount(Paths.get("pojo"), 8);
		checkSourceFileExists(Paths.get("pojo", "MyTestCEXMarketDataExchangeInfoRequest.java"));
		checkSourceFileExists(Paths.get("pojo", "MyTestCEXMarketDataExchangeInfoResponse.java"));
		checkSourceFileExists(Paths.get("pojo", "MyTestCEXMarketDataExchangeInfoResponsePayload.java"));
		checkSourceFileExists(Paths.get("pojo", "MyTestCEXMarketDataTickersRequest.java"));
		checkSourceFileExists(Paths.get("pojo", "MyTestCEXMarketDataTickersResponse.java"));
		checkSourceFileExists(Paths.get("pojo", "MyTestCEXMarketDataTickersResponsePayload.java"));
		checkSourceFileExists(Paths.get("pojo", "MyTestCEXMarketDataTickerStreamRequest.java"));
		checkSourceFileExists(Paths.get("pojo", "MyTestCEXMarketDataTickerStreamMessage.java"));
		
		checkJavaFilesCount(Paths.get("serializers"), 8);
		checkSourceFileExists(Paths.get("serializers", "MyTestCEXMarketDataExchangeInfoRequestSerializer.java"));        
		checkSourceFileExists(Paths.get("serializers", "MyTestCEXMarketDataExchangeInfoResponsePayloadSerializer.java"));
		checkSourceFileExists(Paths.get("serializers", "MyTestCEXMarketDataExchangeInfoResponseSerializer.java"));       
		checkSourceFileExists(Paths.get("serializers", "MyTestCEXMarketDataTickersRequestSerializer.java"));             
		checkSourceFileExists(Paths.get("serializers", "MyTestCEXMarketDataTickersResponsePayloadSerializer.java"));     
		checkSourceFileExists(Paths.get("serializers", "MyTestCEXMarketDataTickersResponseSerializer.java"));
		checkSourceFileExists(Paths.get("serializers", "MyTestCEXMarketDataTickerStreamRequestSerializer.java"));
		checkSourceFileExists(Paths.get("serializers", "MyTestCEXMarketDataTickerStreamMessageSerializer.java"));
	}
	
	@Test
	public void testGenerateExchangeApiClassesNoRestEndpoints() throws IOException {
		srcFolder = ClassesGeneratorTestUtil.generateTmpDir();
		ExchangeDescriptor exchange = new ExchangeDescriptorParser().fromJson(Paths.get(".", "src", "test", "resources", "testCEXDescriptor.json"));
		ExchangeApiDescriptor api = exchange.getApis().get(0);
		api.setRestEndpoints(null);
		ExchangeApiClassesGenerator generator = new ExchangeApiClassesGenerator(exchange, api);
		generator.generateClasses(srcFolder);
		
		checkJavaFilesCount(Paths.get("."), 5);
		checkSourceFileExists(Paths.get("MyTestCEXMarketDataApi.java"));
		checkSourceFileExists(Paths.get("MyTestCEXMarketDataApiImpl.java"));
		
		checkJavaFilesCount(Paths.get("deserializers"), 1);
		checkSourceFileExists(Paths.get("deserializers", "MyTestCEXMarketDataTickerStreamMessageDeserializer.java"));
		
		checkJavaFilesCount(Paths.get("pojo"), 2);
		checkSourceFileExists(Paths.get("pojo", "MyTestCEXMarketDataTickerStreamRequest.java"));
		checkSourceFileExists(Paths.get("pojo", "MyTestCEXMarketDataTickerStreamMessage.java"));
		
		checkJavaFilesCount(Paths.get("serializers"), 2);
		checkSourceFileExists(Paths.get("serializers", "MyTestCEXMarketDataTickerStreamRequestSerializer.java"));
		checkSourceFileExists(Paths.get("serializers", "MyTestCEXMarketDataTickerStreamMessageSerializer.java"));
	}
	
	@Test
	public void testGenerateExchangeApiClassesNoWebsocketEndpoint() throws IOException {
		srcFolder = ClassesGeneratorTestUtil.generateTmpDir();
		ExchangeDescriptor exchange = new ExchangeDescriptorParser().fromJson(Paths.get(".", "src", "test", "resources", "testCEXDescriptor.json"));
		ExchangeApiDescriptor api = exchange.getApis().get(0);
		api.setWebsocketEndpoints(null);
		ExchangeApiClassesGenerator generator = new ExchangeApiClassesGenerator(exchange, api);
		generator.generateClasses(srcFolder);
		
		checkJavaFilesCount(Paths.get("."), 5);
		checkSourceFileExists(Paths.get("MyTestCEXMarketDataApi.java"));
		checkSourceFileExists(Paths.get("MyTestCEXMarketDataApiImpl.java"));
		
		checkJavaFilesCount(Paths.get("deserializers"), 4);
		checkSourceFileExists(Paths.get("deserializers", "MyTestCEXMarketDataExchangeInfoResponseDeserializer.java"));
		checkSourceFileExists(Paths.get("deserializers", "MyTestCEXMarketDataExchangeInfoResponsePayloadDeserializer.java"));
		checkSourceFileExists(Paths.get("deserializers", "MyTestCEXMarketDataTickersResponseDeserializer.java"));
		checkSourceFileExists(Paths.get("deserializers", "MyTestCEXMarketDataTickersResponsePayloadDeserializer.java"));
		
		checkJavaFilesCount(Paths.get("pojo"), 6);
		checkSourceFileExists(Paths.get("pojo", "MyTestCEXMarketDataExchangeInfoRequest.java"));
		checkSourceFileExists(Paths.get("pojo", "MyTestCEXMarketDataExchangeInfoResponse.java"));
		checkSourceFileExists(Paths.get("pojo", "MyTestCEXMarketDataExchangeInfoResponsePayload.java"));
		checkSourceFileExists(Paths.get("pojo", "MyTestCEXMarketDataTickersRequest.java"));
		checkSourceFileExists(Paths.get("pojo", "MyTestCEXMarketDataTickersResponse.java"));
		checkSourceFileExists(Paths.get("pojo", "MyTestCEXMarketDataTickersResponsePayload.java"));
		
		checkJavaFilesCount(Paths.get("serializers"), 6);
		checkSourceFileExists(Paths.get("serializers", "MyTestCEXMarketDataExchangeInfoRequestSerializer.java"));        
		checkSourceFileExists(Paths.get("serializers", "MyTestCEXMarketDataExchangeInfoResponsePayloadSerializer.java"));
		checkSourceFileExists(Paths.get("serializers", "MyTestCEXMarketDataExchangeInfoResponseSerializer.java"));       
		checkSourceFileExists(Paths.get("serializers", "MyTestCEXMarketDataTickersRequestSerializer.java"));             
		checkSourceFileExists(Paths.get("serializers", "MyTestCEXMarketDataTickersResponsePayloadSerializer.java"));     
		checkSourceFileExists(Paths.get("serializers", "MyTestCEXMarketDataTickersResponseSerializer.java"));
	}
	
	private void checkJavaFilesCount(Path relativePkg, int count) throws IOException {
		ClassesGeneratorTestUtil.checkJavaFilesCount(srcFolder.resolve(BASE_PKG).resolve(relativePkg), count);
	}
	
	private Path checkSourceFileExists(Path srcFilePath) {
		return ClassesGeneratorTestUtil.checkSourceFileExists(srcFolder.resolve(BASE_PKG), srcFilePath);
	}

}
