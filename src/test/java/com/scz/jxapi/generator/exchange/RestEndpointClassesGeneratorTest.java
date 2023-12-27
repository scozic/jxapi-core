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
 * Unit test for {@link RestEndpointClassesGenerator}
 */
public class RestEndpointClassesGeneratorTest {
	
	private Path srcFolder;
	
	@After
	public void tearDown() throws IOException {
		if (srcFolder != null) {
			JavaCodeGenerationUtil.deletePath(srcFolder);
			srcFolder = null;
		}
	}

	@Test
	public void testGenerateRestEndpointClasses() throws IOException {
		srcFolder = Paths.get("tmp" + Math.random());
		ExchangeDescriptor exchange = new ExchangeDescriptorParser().fromJson(Paths.get(".", "src", "test", "resources", "testCEXDescriptor.json"));
		ExchangeApiDescriptor api = exchange.getApis().get(0);
		RestEndpointDescriptor restEndpoint = api.getRestEndpoints().get(0);
		RestEndpointClassesGenerator generator = new RestEndpointClassesGenerator(exchange, api, restEndpoint);
		generator.generateClasses(srcFolder);
		
		checkJavaFilesCount(Paths.get("deserializers"), 2);
		checkSourceFileExists(Paths.get("deserializers", "MyTestCEXMarketDataExchangeInfoResponseDeserializer.java"));
		checkSourceFileExists(Paths.get("deserializers", "MyTestCEXMarketDataExchangeInfoResponsePayloadDeserializer.java"));
		
		checkJavaFilesCount(Paths.get("pojo"), 3);
		checkSourceFileExists(Paths.get("pojo", "MyTestCEXMarketDataExchangeInfoRequest.java"));
		checkSourceFileExists(Paths.get("pojo", "MyTestCEXMarketDataExchangeInfoResponse.java"));
		checkSourceFileExists(Paths.get("pojo", "MyTestCEXMarketDataExchangeInfoResponsePayload.java"));
		
		
		checkJavaFilesCount(Paths.get("serializers"), 6);
		checkSourceFileExists(Paths.get("serializers", "MyTestCEXMarketDataExchangeInfoRequestSerializer.java"));        
		checkSourceFileExists(Paths.get("serializers", "MyTestCEXMarketDataExchangeInfoResponsePayloadSerializer.java"));
		checkSourceFileExists(Paths.get("serializers", "MyTestCEXMarketDataExchangeInfoResponseSerializer.java"));       
		checkSourceFileExists(Paths.get("serializers", "MyTestCEXMarketDataTickersRequestSerializer.java"));             
		checkSourceFileExists(Paths.get("serializers", "MyTestCEXMarketDataTickersResponsePayloadSerializer.java"));     
		checkSourceFileExists(Paths.get("serializers", "MyTestCEXMarketDataTickersResponseSerializer.java"));            
	}
	
	private void checkJavaFilesCount(Path relativePkg, int count) throws IOException {
		Path pkg = srcFolder.resolve(Paths.get("com", "foo", "bar", "gen", "marketdata"));
		File folder = pkg.resolve(relativePkg).toFile(); 
		Assert.assertTrue(folder.exists());
		Assert.assertTrue(folder.isDirectory());
		Assert.assertEquals("Expected " + count + " files, but got:" + Arrays.toString(folder.listFiles()),
							 count,	
							 folder.listFiles().length);
	}
	
	private void checkSourceFileExists(Path srcFilePath) {
		Path pkg = srcFolder.resolve(Paths.get("com", "foo", "bar", "gen", "marketdata"));
		Path fullPath = pkg.resolve(srcFilePath);
		Assert.assertTrue(fullPath.toFile().exists());
	}
}
