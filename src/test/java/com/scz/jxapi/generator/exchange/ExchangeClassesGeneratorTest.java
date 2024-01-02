package com.scz.jxapi.generator.exchange;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import com.scz.jxapi.generator.JavaCodeGenerationUtil;

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
		srcFolder = Paths.get("tmp" + Math.random());
		ExchangeDescriptor exchange = new ExchangeDescriptorParser().fromJson(Paths.get(".", "src", "test", "resources", "testCEXDescriptor.json"));
		ExchangeClassesGenerator generator = new ExchangeClassesGenerator(exchange);
		generator.generateClasses(srcFolder);
		
		checkJavaFilesCount(Paths.get("."), 3);
		checkSourceFileExists(Paths.get("MyTestCEXExchange.java"));
		checkSourceFileExists(Paths.get("MyTestCEXExchangeImpl.java"));
		
		checkJavaFilesCount(Paths.get("marketData"), 5);
		checkSourceFileExists(Paths.get("marketData", "MyTestCEXMarketDataApi.java"));
		checkSourceFileExists(Paths.get("marketData", "MyTestCEXMarketDataApiImpl.java"));
		
		checkJavaFilesCount(Paths.get("marketData", "deserializers"), 5);
		checkSourceFileExists(Paths.get("marketData", "deserializers", "MyTestCEXMarketDataExchangeInfoResponseDeserializer.java"));
		checkSourceFileExists(Paths.get("marketData", "deserializers", "MyTestCEXMarketDataExchangeInfoResponsePayloadDeserializer.java"));
		checkSourceFileExists(Paths.get("marketData", "deserializers", "MyTestCEXMarketDataTickerStreamMessageDeserializer.java"));
		checkSourceFileExists(Paths.get("marketData", "deserializers", "MyTestCEXMarketDataTickersResponseDeserializer.java"));
		checkSourceFileExists(Paths.get("marketData", "deserializers", "MyTestCEXMarketDataTickersResponsePayloadDeserializer.java"));
		checkSourceFileExists(Paths.get("marketData", "deserializers", "MyTestCEXMarketDataTickerStreamMessageDeserializer.java"));
		
		checkJavaFilesCount(Paths.get("marketData", "pojo"), 8);
		checkSourceFileExists(Paths.get("marketData", "pojo", "MyTestCEXMarketDataExchangeInfoRequest.java"));
		checkSourceFileExists(Paths.get("marketData", "pojo", "MyTestCEXMarketDataExchangeInfoResponse.java"));
		checkSourceFileExists(Paths.get("marketData", "pojo", "MyTestCEXMarketDataExchangeInfoResponsePayload.java"));
		checkSourceFileExists(Paths.get("marketData", "pojo", "MyTestCEXMarketDataTickersRequest.java"));
		checkSourceFileExists(Paths.get("marketData", "pojo", "MyTestCEXMarketDataTickersResponse.java"));
		checkSourceFileExists(Paths.get("marketData", "pojo", "MyTestCEXMarketDataTickersResponsePayload.java"));
		checkSourceFileExists(Paths.get("marketData", "pojo", "MyTestCEXMarketDataTickerStreamRequest.java"));
		checkSourceFileExists(Paths.get("marketData", "pojo", "MyTestCEXMarketDataTickerStreamMessage.java"));
		
		checkJavaFilesCount(Paths.get("marketData", "serializers"), 8);
		checkSourceFileExists(Paths.get("marketData", "serializers", "MyTestCEXMarketDataExchangeInfoRequestSerializer.java"));        
		checkSourceFileExists(Paths.get("marketData", "serializers", "MyTestCEXMarketDataExchangeInfoResponsePayloadSerializer.java"));
		checkSourceFileExists(Paths.get("marketData", "serializers", "MyTestCEXMarketDataExchangeInfoResponseSerializer.java"));       
		checkSourceFileExists(Paths.get("marketData", "serializers", "MyTestCEXMarketDataTickersRequestSerializer.java"));             
		checkSourceFileExists(Paths.get("marketData", "serializers", "MyTestCEXMarketDataTickersResponsePayloadSerializer.java"));     
		checkSourceFileExists(Paths.get("marketData", "serializers", "MyTestCEXMarketDataTickersResponseSerializer.java"));
		checkSourceFileExists(Paths.get("marketData", "serializers", "MyTestCEXMarketDataTickerStreamRequestSerializer.java"));
		checkSourceFileExists(Paths.get("marketData", "serializers", "MyTestCEXMarketDataTickerStreamMessageSerializer.java"));
	}
	
	private void checkJavaFilesCount(Path relativePkg, int count) throws IOException {
		Path pkg = srcFolder.resolve(Paths.get("com", "foo", "bar", "gen"));
		File folder = pkg.resolve(relativePkg).toFile(); 
		Assert.assertTrue(folder.exists());
		Assert.assertTrue(folder.isDirectory());
		Assert.assertEquals("Expected " + count + " files, but got:" + Arrays.toString(folder.listFiles()),
							 count,	
							 folder.listFiles().length);
	}
	
	private Path checkSourceFileExists(Path srcFilePath) {
		Path pkg = srcFolder.resolve(Paths.get("com", "foo", "bar", "gen"));
		Path fullPath = pkg.resolve(srcFilePath);
		Assert.assertTrue(fullPath.toFile().exists());
		return fullPath;
	}

}
