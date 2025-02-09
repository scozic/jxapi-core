package com.scz.jxapi.generator.java.exchange;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Test;

import com.scz.jxapi.exchange.descriptor.ExchangeDescriptor;
import com.scz.jxapi.exchange.descriptor.parser.ExchangeDescriptorParser;
import com.scz.jxapi.generator.java.JavaCodeGenerationUtil;

/**
 * Unit test for {@link ExchangeClassesGenerator}
 */
public class ExchangeClassesGeneratorTest {
	
	private Path srcFolder;
	
	@After
	public void tearDown() throws IOException {
		if (srcFolder != null) {
			JavaCodeGenerationUtil.deletePath(srcFolder);
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
		
		checkJavaFilesCount(Paths.get("marketData"), 6);
		checkSourceFileExists(Paths.get("marketData", "MyTestExchangeMarketDataApi.java"));
		checkSourceFileExists(Paths.get("marketData", "MyTestExchangeMarketDataApiImpl.java"));
		checkSourceFileExists(Paths.get("marketData", "MyTestExchangeMarketDataConstants.java"));
		
		checkJavaFilesCount(Paths.get("marketData", "deserializers"), 5);
		checkSourceFileExists(Paths.get("marketData", "deserializers", "MyTestExchangeMarketDataExchangeInfoResponseDeserializer.java"));
		checkSourceFileExists(Paths.get("marketData", "deserializers", "MyTestExchangeMarketDataExchangeInfoResponsePayloadDeserializer.java"));
		checkSourceFileExists(Paths.get("marketData", "deserializers", "MyTestExchangeMarketDataTickerStreamMessageDeserializer.java"));
		checkSourceFileExists(Paths.get("marketData", "deserializers", "MyTestExchangeMarketDataTickersResponseDeserializer.java"));
		checkSourceFileExists(Paths.get("marketData", "deserializers", "MyTestExchangeMarketDataTickersResponsePayloadDeserializer.java"));
		checkSourceFileExists(Paths.get("marketData", "deserializers", "MyTestExchangeMarketDataTickerStreamMessageDeserializer.java"));
		
		checkJavaFilesCount(Paths.get("marketData", "pojo"), 8);
		checkSourceFileExists(Paths.get("marketData", "pojo", "MyTestExchangeMarketDataExchangeInfoRequest.java"));
		checkSourceFileExists(Paths.get("marketData", "pojo", "MyTestExchangeMarketDataExchangeInfoResponse.java"));
		checkSourceFileExists(Paths.get("marketData", "pojo", "MyTestExchangeMarketDataExchangeInfoResponsePayload.java"));
		checkSourceFileExists(Paths.get("marketData", "pojo", "MyTestExchangeMarketDataTickersRequest.java"));
		checkSourceFileExists(Paths.get("marketData", "pojo", "MyTestExchangeMarketDataTickersResponse.java"));
		checkSourceFileExists(Paths.get("marketData", "pojo", "MyTestExchangeMarketDataTickersResponsePayload.java"));
		checkSourceFileExists(Paths.get("marketData", "pojo", "MyTestExchangeMarketDataTickerStreamRequest.java"));
		checkSourceFileExists(Paths.get("marketData", "pojo", "MyTestExchangeMarketDataTickerStreamMessage.java"));
		
		checkJavaFilesCount(Paths.get("marketData", "serializers"), 8);
		checkSourceFileExists(Paths.get("marketData", "serializers", "MyTestExchangeMarketDataExchangeInfoRequestSerializer.java"));        
		checkSourceFileExists(Paths.get("marketData", "serializers", "MyTestExchangeMarketDataExchangeInfoResponsePayloadSerializer.java"));
		checkSourceFileExists(Paths.get("marketData", "serializers", "MyTestExchangeMarketDataExchangeInfoResponseSerializer.java"));       
		checkSourceFileExists(Paths.get("marketData", "serializers", "MyTestExchangeMarketDataTickersRequestSerializer.java"));             
		checkSourceFileExists(Paths.get("marketData", "serializers", "MyTestExchangeMarketDataTickersResponsePayloadSerializer.java"));     
		checkSourceFileExists(Paths.get("marketData", "serializers", "MyTestExchangeMarketDataTickersResponseSerializer.java"));
		checkSourceFileExists(Paths.get("marketData", "serializers", "MyTestExchangeMarketDataTickerStreamRequestSerializer.java"));
		checkSourceFileExists(Paths.get("marketData", "serializers", "MyTestExchangeMarketDataTickerStreamMessageSerializer.java"));
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
