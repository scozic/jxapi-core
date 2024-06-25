package com.scz.jxapi.generator.exchange.api;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Test;

import com.scz.jxapi.exchange.descriptor.ExchangeApiDescriptor;
import com.scz.jxapi.exchange.descriptor.ExchangeDescriptor;
import com.scz.jxapi.exchange.descriptor.ExchangeDescriptorParser;
import com.scz.jxapi.generator.exchange.ClassesGeneratorTestUtil;
import com.scz.jxapi.generator.java.JavaCodeGenerationUtil;

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
		ExchangeDescriptor exchange = new ExchangeDescriptorParser().fromJson(Paths.get(".", "src", "test", "resources", "testExchangeDescriptor.json"));
		ExchangeApiDescriptor api = exchange.getApis().get(0);
		ExchangeApiClassesGenerator generator = new ExchangeApiClassesGenerator(exchange, api);
		generator.generateClasses(srcFolder);
		
		checkJavaFilesCount(Paths.get("."), 5);
		checkSourceFileExists(Paths.get("MyTestExchangeMarketDataApi.java"));
		checkSourceFileExists(Paths.get("MyTestExchangeMarketDataApiImpl.java"));
		
		checkJavaFilesCount(Paths.get("deserializers"), 5);
		checkSourceFileExists(Paths.get("deserializers", "MyTestExchangeMarketDataExchangeInfoResponseDeserializer.java"));
		checkSourceFileExists(Paths.get("deserializers", "MyTestExchangeMarketDataExchangeInfoResponsePayloadDeserializer.java"));
		checkSourceFileExists(Paths.get("deserializers", "MyTestExchangeMarketDataTickersResponseDeserializer.java"));
		checkSourceFileExists(Paths.get("deserializers", "MyTestExchangeMarketDataTickersResponsePayloadDeserializer.java"));
		checkSourceFileExists(Paths.get("deserializers", "MyTestExchangeMarketDataTickerStreamMessageDeserializer.java"));
		
		checkJavaFilesCount(Paths.get("pojo"), 8);
		checkSourceFileExists(Paths.get("pojo", "MyTestExchangeMarketDataExchangeInfoRequest.java"));
		checkSourceFileExists(Paths.get("pojo", "MyTestExchangeMarketDataExchangeInfoResponse.java"));
		checkSourceFileExists(Paths.get("pojo", "MyTestExchangeMarketDataExchangeInfoResponsePayload.java"));
		checkSourceFileExists(Paths.get("pojo", "MyTestExchangeMarketDataTickersRequest.java"));
		checkSourceFileExists(Paths.get("pojo", "MyTestExchangeMarketDataTickersResponse.java"));
		checkSourceFileExists(Paths.get("pojo", "MyTestExchangeMarketDataTickersResponsePayload.java"));
		checkSourceFileExists(Paths.get("pojo", "MyTestExchangeMarketDataTickerStreamRequest.java"));
		checkSourceFileExists(Paths.get("pojo", "MyTestExchangeMarketDataTickerStreamMessage.java"));
		
		checkJavaFilesCount(Paths.get("serializers"), 8);
		checkSourceFileExists(Paths.get("serializers", "MyTestExchangeMarketDataExchangeInfoRequestSerializer.java"));        
		checkSourceFileExists(Paths.get("serializers", "MyTestExchangeMarketDataExchangeInfoResponsePayloadSerializer.java"));
		checkSourceFileExists(Paths.get("serializers", "MyTestExchangeMarketDataExchangeInfoResponseSerializer.java"));       
		checkSourceFileExists(Paths.get("serializers", "MyTestExchangeMarketDataTickersRequestSerializer.java"));             
		checkSourceFileExists(Paths.get("serializers", "MyTestExchangeMarketDataTickersResponsePayloadSerializer.java"));     
		checkSourceFileExists(Paths.get("serializers", "MyTestExchangeMarketDataTickersResponseSerializer.java"));
		checkSourceFileExists(Paths.get("serializers", "MyTestExchangeMarketDataTickerStreamRequestSerializer.java"));
		checkSourceFileExists(Paths.get("serializers", "MyTestExchangeMarketDataTickerStreamMessageSerializer.java"));
	}
	
	@Test
	public void testGenerateExchangeApiClassesNoRestEndpoints() throws IOException {
		srcFolder = ClassesGeneratorTestUtil.generateTmpDir();
		ExchangeDescriptor exchange = new ExchangeDescriptorParser().fromJson(Paths.get(".", "src", "test", "resources", "testExchangeDescriptor.json"));
		ExchangeApiDescriptor api = exchange.getApis().get(0);
		api.setRestEndpoints(null);
		ExchangeApiClassesGenerator generator = new ExchangeApiClassesGenerator(exchange, api);
		generator.generateClasses(srcFolder);
		
		checkJavaFilesCount(Paths.get("."), 5);
		checkSourceFileExists(Paths.get("MyTestExchangeMarketDataApi.java"));
		checkSourceFileExists(Paths.get("MyTestExchangeMarketDataApiImpl.java"));
		
		checkJavaFilesCount(Paths.get("deserializers"), 1);
		checkSourceFileExists(Paths.get("deserializers", "MyTestExchangeMarketDataTickerStreamMessageDeserializer.java"));
		
		checkJavaFilesCount(Paths.get("pojo"), 2);
		checkSourceFileExists(Paths.get("pojo", "MyTestExchangeMarketDataTickerStreamRequest.java"));
		checkSourceFileExists(Paths.get("pojo", "MyTestExchangeMarketDataTickerStreamMessage.java"));
		
		checkJavaFilesCount(Paths.get("serializers"), 2);
		checkSourceFileExists(Paths.get("serializers", "MyTestExchangeMarketDataTickerStreamRequestSerializer.java"));
		checkSourceFileExists(Paths.get("serializers", "MyTestExchangeMarketDataTickerStreamMessageSerializer.java"));
	}
	
	@Test
	public void testGenerateExchangeApiClassesNoWebsocketEndpoint() throws IOException {
		srcFolder = ClassesGeneratorTestUtil.generateTmpDir();
		ExchangeDescriptor exchange = new ExchangeDescriptorParser().fromJson(Paths.get(".", "src", "test", "resources", "testExchangeDescriptor.json"));
		ExchangeApiDescriptor api = exchange.getApis().get(0);
		api.setWebsocketEndpoints(null);
		ExchangeApiClassesGenerator generator = new ExchangeApiClassesGenerator(exchange, api);
		generator.generateClasses(srcFolder);
		
		checkJavaFilesCount(Paths.get("."), 5);
		checkSourceFileExists(Paths.get("MyTestExchangeMarketDataApi.java"));
		checkSourceFileExists(Paths.get("MyTestExchangeMarketDataApiImpl.java"));
		
		checkJavaFilesCount(Paths.get("deserializers"), 4);
		checkSourceFileExists(Paths.get("deserializers", "MyTestExchangeMarketDataExchangeInfoResponseDeserializer.java"));
		checkSourceFileExists(Paths.get("deserializers", "MyTestExchangeMarketDataExchangeInfoResponsePayloadDeserializer.java"));
		checkSourceFileExists(Paths.get("deserializers", "MyTestExchangeMarketDataTickersResponseDeserializer.java"));
		checkSourceFileExists(Paths.get("deserializers", "MyTestExchangeMarketDataTickersResponsePayloadDeserializer.java"));
		
		checkJavaFilesCount(Paths.get("pojo"), 6);
		checkSourceFileExists(Paths.get("pojo", "MyTestExchangeMarketDataExchangeInfoRequest.java"));
		checkSourceFileExists(Paths.get("pojo", "MyTestExchangeMarketDataExchangeInfoResponse.java"));
		checkSourceFileExists(Paths.get("pojo", "MyTestExchangeMarketDataExchangeInfoResponsePayload.java"));
		checkSourceFileExists(Paths.get("pojo", "MyTestExchangeMarketDataTickersRequest.java"));
		checkSourceFileExists(Paths.get("pojo", "MyTestExchangeMarketDataTickersResponse.java"));
		checkSourceFileExists(Paths.get("pojo", "MyTestExchangeMarketDataTickersResponsePayload.java"));
		
		checkJavaFilesCount(Paths.get("serializers"), 6);
		checkSourceFileExists(Paths.get("serializers", "MyTestExchangeMarketDataExchangeInfoRequestSerializer.java"));        
		checkSourceFileExists(Paths.get("serializers", "MyTestExchangeMarketDataExchangeInfoResponsePayloadSerializer.java"));
		checkSourceFileExists(Paths.get("serializers", "MyTestExchangeMarketDataExchangeInfoResponseSerializer.java"));       
		checkSourceFileExists(Paths.get("serializers", "MyTestExchangeMarketDataTickersRequestSerializer.java"));             
		checkSourceFileExists(Paths.get("serializers", "MyTestExchangeMarketDataTickersResponsePayloadSerializer.java"));     
		checkSourceFileExists(Paths.get("serializers", "MyTestExchangeMarketDataTickersResponseSerializer.java"));
	}
	
	private void checkJavaFilesCount(Path relativePkg, int count) throws IOException {
		ClassesGeneratorTestUtil.checkJavaFilesCount(srcFolder.resolve(BASE_PKG).resolve(relativePkg), count);
	}
	
	private Path checkSourceFileExists(Path srcFilePath) {
		return ClassesGeneratorTestUtil.checkSourceFileExists(srcFolder.resolve(BASE_PKG), srcFilePath);
	}

}
