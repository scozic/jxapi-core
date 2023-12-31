package com.scz.jxapi.generator.exchange;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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
		checkRequestPojoContent(checkSourceFileExists(Paths.get("pojo", "MyTestCEXMarketDataExchangeInfoRequest.java")));
		checkResponsePojoContent(checkSourceFileExists(Paths.get("pojo", "MyTestCEXMarketDataExchangeInfoResponse.java")));
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
	
	private Path checkSourceFileExists(Path srcFilePath) {
		Path pkg = srcFolder.resolve(Paths.get("com", "foo", "bar", "gen", "marketdata"));
		Path fullPath = pkg.resolve(srcFilePath);
		Assert.assertTrue(fullPath.toFile().exists());
		return fullPath;
	}
	
	private void checkRequestPojoContent(Path requestPojoFile) throws IOException {
		Assert.assertEquals("package com.foo.bar.gen.marketdata.pojo;\n"
				+ "\n"
				+ "import com.fasterxml.jackson.databind.annotation.JsonSerialize;\n"
				+ "import com.foo.bar.gen.marketdata.serializers.MyTestCEXMarketDataExchangeInfoRequestSerializer;\n"
				+ "import com.scz.jxapi.netutils.rest.RestEndpointUrlParameters;\n"
				+ "import com.scz.jxapi.util.EncodingUtil;\n"
				+ "import java.util.List;\n"
				+ "\n"
				+ "/**\n"
				+ " * Request for MyTestCEX MarketData API exchangeInfo REST endpoint<br/>\n"
				+ " * Fetch market information of symbols that can be traded\n"
				+ " * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>\n"
				+ " */\n"
				+ "@JsonSerialize(using = MyTestCEXMarketDataExchangeInfoRequestSerializer.class)\n"
				+ "public class MyTestCEXMarketDataExchangeInfoRequest implements RestEndpointUrlParameters {\n"
				+ "  private List<String> symbols;\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @return The list of symbol to fetch market information for. Leave empty to fetch all markets\n"
				+ "   */\n"
				+ "  public List<String> getSymbols() {\n"
				+ "    return symbols;\n"
				+ "  }\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @param symbols The list of symbol to fetch market information for. Leave empty to fetch all markets\n"
				+ "   */\n"
				+ "  public void setSymbols(List<String> symbols) {\n"
				+ "    this.symbols = symbols;\n"
				+ "  }\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public String getUrlParameters() {\n"
				+ "    return EncodingUtil.createUrlQueryParameters(\"symbols\", EncodingUtil.listToUrlParamString(symbols));\n"
				+ "  }\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public String toString() {\n"
				+ "    return EncodingUtil.pojoToString(this);\n"
				+ "  }\n"
				+ "}\n", 
				Files.readString(requestPojoFile));
	}
	
	private void checkResponsePojoContent(Path responsePojoFile) throws IOException {
		Assert.assertEquals("package com.foo.bar.gen.marketdata.pojo;\n"
				+ "\n"
				+ "import com.fasterxml.jackson.databind.annotation.JsonSerialize;\n"
				+ "import com.foo.bar.gen.marketdata.serializers.MyTestCEXMarketDataExchangeInfoResponseSerializer;\n"
				+ "import com.scz.jxapi.util.EncodingUtil;\n"
				+ "import java.util.List;\n"
				+ "\n"
				+ "/**\n"
				+ " * Response to MyTestCEX MarketData API <br/>\n"
				+ " * exchangeInfo REST endpoint request<br/>\n"
				+ " * Fetch market information of symbols that can be traded\n"
				+ " * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>\n"
				+ " */\n"
				+ "@JsonSerialize(using = MyTestCEXMarketDataExchangeInfoResponseSerializer.class)\n"
				+ "public class MyTestCEXMarketDataExchangeInfoResponse {\n"
				+ "  private List<MyTestCEXMarketDataExchangeInfoResponsePayload> payload;\n"
				+ "  private Integer responseCode;\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @return List of market information for each requested symbol\n"
				+ "   */\n"
				+ "  public List<MyTestCEXMarketDataExchangeInfoResponsePayload> getPayload() {\n"
				+ "    return payload;\n"
				+ "  }\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @param payload List of market information for each requested symbol\n"
				+ "   */\n"
				+ "  public void setPayload(List<MyTestCEXMarketDataExchangeInfoResponsePayload> payload) {\n"
				+ "    this.payload = payload;\n"
				+ "  }\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @return Request response code\n"
				+ "   */\n"
				+ "  public Integer getResponseCode() {\n"
				+ "    return responseCode;\n"
				+ "  }\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @param responseCode Request response code\n"
				+ "   */\n"
				+ "  public void setResponseCode(Integer responseCode) {\n"
				+ "    this.responseCode = responseCode;\n"
				+ "  }\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public String toString() {\n"
				+ "    return EncodingUtil.pojoToString(this);\n"
				+ "  }\n"
				+ "}\n", 
				Files.readString(responsePojoFile));
	}
}
