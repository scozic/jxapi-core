package com.scz.jxapi.generator.exchange;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import com.scz.jxapi.generator.JavaCodeGenerationUtil;

/**
 * Unit test for {@link RestEndpointClassesGenerator}
 */
public class RestEndpointClassesGeneratorTest {
	
	private static final Path BASE_PKG = Paths.get("com", "foo", "bar", "gen", "marketdata");
	
	private Path srcFolder;
	
	@After
	public void tearDown() throws IOException {
		if (srcFolder != null) {
			JavaCodeGenerationUtil.deletePath(srcFolder);
			srcFolder = null;
		}
	}
	
	private RestEndpointDescriptor findRestEndpointByName(String name, ExchangeApiDescriptor exchangeDescriptor) {
		for (RestEndpointDescriptor api: exchangeDescriptor.getRestEndpoints()) {
			if (api.getName().equals(name)) {
				return api;
			}
		}
		throw new AssertionError("No such API:" + name + " in:" + exchangeDescriptor);
	}
	
	private void checkJavaFilesCount(Path relativePkg, int count) throws IOException {
		ClassesGeneratorTestUtil.checkJavaFilesCount(srcFolder.resolve(BASE_PKG).resolve(relativePkg), count);
	}
	
	private Path checkSourceFileExists(Path srcFilePath) {
		return ClassesGeneratorTestUtil.checkSourceFileExists(srcFolder.resolve(BASE_PKG), srcFilePath);
	}

	@Test
	public void testGenerateRestEndpointClasses() throws IOException {
		srcFolder = ClassesGeneratorTestUtil.generateTmpDir();
		ExchangeDescriptor exchange = new ExchangeDescriptorParser().fromJson(Paths.get(".", "src", "test", "resources", "testCEXDescriptor.json"));
		ExchangeApiDescriptor api = exchange.getApis().get(0);
		RestEndpointDescriptor restEndpoint = api.getRestEndpoints().get(0);
		RestEndpointClassesGenerator generator = new RestEndpointClassesGenerator(exchange, api, restEndpoint);
		generator.generateClasses(srcFolder);
		
		checkJavaFilesCount(Paths.get("deserializers"), 2);
		checkSourceFileExists(Paths.get("deserializers", "MyTestCEXMarketDataExchangeInfoResponseDeserializer.java"));
		checkSourceFileExists(Paths.get("deserializers", "MyTestCEXMarketDataExchangeInfoResponsePayloadDeserializer.java"));
		
		checkJavaFilesCount(Paths.get("pojo"), 3);
		
		Assert.assertEquals("package com.foo.bar.gen.marketdata.pojo;\n"
				+ "\n"
				+ "import com.fasterxml.jackson.databind.annotation.JsonSerialize;\n"
				+ "import com.foo.bar.gen.marketdata.serializers.MyTestCEXMarketDataExchangeInfoRequestSerializer;\n"
				+ "import com.scz.jxapi.util.EncodingUtil;\n"
				+ "import java.util.List;\n"
				+ "import java.util.Objects;\n"
				+ "\n"
				+ "/**\n"
				+ " * Request for MyTestCEX MarketData API exchangeInfo REST endpoint<br/>\n"
				+ " * Fetch market information of symbols that can be traded\n"
				+ " * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>\n"
				+ " */\n"
				+ "@JsonSerialize(using = MyTestCEXMarketDataExchangeInfoRequestSerializer.class)\n"
				+ "public class MyTestCEXMarketDataExchangeInfoRequest {\n"
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
				+ "  public boolean equals(Object other) {\n"
				+ "    if (other == null)\n"
				+ "      return false;\n"
				+ "    if (!getClass().equals(other.getClass()))\n"
				+ "      return false;\n"
				+ "    MyTestCEXMarketDataExchangeInfoRequest o = (MyTestCEXMarketDataExchangeInfoRequest) other;\n"
				+ "    return Objects.equals(symbols, o.symbols);\n"
				+ "  }\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public int hashCode() {\n"
				+ "    return Objects.hash(symbols);\n"
				+ "  }\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public String toString() {\n"
				+ "    return EncodingUtil.pojoToString(this);\n"
				+ "  }\n"
				+ "}\n", 
				Files.readString(checkSourceFileExists(Paths.get("pojo", "MyTestCEXMarketDataExchangeInfoRequest.java"))));
		
		Assert.assertEquals("package com.foo.bar.gen.marketdata.pojo;\n"
				+ "\n"
				+ "import com.fasterxml.jackson.databind.annotation.JsonSerialize;\n"
				+ "import com.foo.bar.gen.marketdata.serializers.MyTestCEXMarketDataExchangeInfoResponseSerializer;\n"
				+ "import com.scz.jxapi.util.EncodingUtil;\n"
				+ "import java.util.List;\n"
				+ "import java.util.Objects;\n"
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
				+ "  public boolean equals(Object other) {\n"
				+ "    if (other == null)\n"
				+ "      return false;\n"
				+ "    if (!getClass().equals(other.getClass()))\n"
				+ "      return false;\n"
				+ "    MyTestCEXMarketDataExchangeInfoResponse o = (MyTestCEXMarketDataExchangeInfoResponse) other;\n"
				+ "    return Objects.equals(payload, o.payload)\n"
				+ "            && Objects.equals(responseCode, o.responseCode);\n"
				+ "  }\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public int hashCode() {\n"
				+ "    return Objects.hash(payload, responseCode);\n"
				+ "  }\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public String toString() {\n"
				+ "    return EncodingUtil.pojoToString(this);\n"
				+ "  }\n"
				+ "}\n", 
				Files.readString(checkSourceFileExists(Paths.get("pojo", "MyTestCEXMarketDataExchangeInfoResponse.java"))));
		
		checkSourceFileExists(Paths.get("pojo", "MyTestCEXMarketDataExchangeInfoResponsePayload.java"));
		
		checkJavaFilesCount(Paths.get("serializers"), 3);
		checkSourceFileExists(Paths.get("serializers", "MyTestCEXMarketDataExchangeInfoRequestSerializer.java"));        
		checkSourceFileExists(Paths.get("serializers", "MyTestCEXMarketDataExchangeInfoResponsePayloadSerializer.java"));
		checkSourceFileExists(Paths.get("serializers", "MyTestCEXMarketDataExchangeInfoResponseSerializer.java"));               
	}
	
	@Test
	public void testGenerateRestEndpointClassesWithRequestAndResponseImplmentingCustomInterfaces() throws IOException {
		srcFolder = ClassesGeneratorTestUtil.generateTmpDir();
		ExchangeDescriptor exchange = new ExchangeDescriptorParser().fromJson(Paths.get(".", "src", "test", "resources", "testExchangeDescriptorWithRequestAndResponseImplementingCustomInterfaces.json"));
		ExchangeApiDescriptor api = exchange.getApis().get(0);
		RestEndpointDescriptor restEndpoint = findRestEndpointByName("exchangeInfo", api);
		RestEndpointClassesGenerator generator = new RestEndpointClassesGenerator(exchange, api, restEndpoint);
		generator.generateClasses(srcFolder);
		
		checkJavaFilesCount(Paths.get("deserializers"), 2);
		checkSourceFileExists(Paths.get("deserializers", "MyTestCEXMarketDataExchangeInfoResponseDeserializer.java"));
		checkSourceFileExists(Paths.get("deserializers", "MyTestCEXMarketDataExchangeInfoResponsePayloadDeserializer.java"));
		
		checkJavaFilesCount(Paths.get("pojo"), 3);
		
		Assert.assertEquals("package com.foo.bar.gen.marketdata.pojo;\n"
				+ "\n"
				+ "import com.fasterxml.jackson.databind.annotation.JsonSerialize;\n"
				+ "import com.foo.bar.ExchangeInfoRequestInterface1;\n"
				+ "import com.foo.bar.ExchangeInfoRequestInterface2;\n"
				+ "import com.foo.bar.gen.marketdata.serializers.MyTestCEXMarketDataExchangeInfoRequestSerializer;\n"
				+ "import com.scz.jxapi.util.EncodingUtil;\n"
				+ "import java.util.List;\n"
				+ "import java.util.Objects;\n"
				+ "\n"
				+ "/**\n"
				+ " * Request for MyTestCEX MarketData API exchangeInfo REST endpoint<br/>\n"
				+ " * Fetch market information of symbols that can be traded\n"
				+ " * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>\n"
				+ " */\n"
				+ "@JsonSerialize(using = MyTestCEXMarketDataExchangeInfoRequestSerializer.class)\n"
				+ "public class MyTestCEXMarketDataExchangeInfoRequest implements ExchangeInfoRequestInterface1, ExchangeInfoRequestInterface2 {\n"
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
				+ "  public boolean equals(Object other) {\n"
				+ "    if (other == null)\n"
				+ "      return false;\n"
				+ "    if (!getClass().equals(other.getClass()))\n"
				+ "      return false;\n"
				+ "    MyTestCEXMarketDataExchangeInfoRequest o = (MyTestCEXMarketDataExchangeInfoRequest) other;\n"
				+ "    return Objects.equals(symbols, o.symbols);\n"
				+ "  }\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public int hashCode() {\n"
				+ "    return Objects.hash(symbols);\n"
				+ "  }\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public String toString() {\n"
				+ "    return EncodingUtil.pojoToString(this);\n"
				+ "  }\n"
				+ "}\n", 
				Files.readString(checkSourceFileExists(Paths.get("pojo", "MyTestCEXMarketDataExchangeInfoRequest.java"))));
		
		Assert.assertEquals("package com.foo.bar.gen.marketdata.pojo;\n"
				+ "\n"
				+ "import com.fasterxml.jackson.databind.annotation.JsonSerialize;\n"
				+ "import com.foo.bar.ExchangeInfoResponseInterface1;\n"
				+ "import com.foo.bar.ExchangeInfoResponseInterface2;\n"
				+ "import com.foo.bar.gen.marketdata.serializers.MyTestCEXMarketDataExchangeInfoResponseSerializer;\n"
				+ "import com.scz.jxapi.util.EncodingUtil;\n"
				+ "import java.util.List;\n"
				+ "import java.util.Objects;\n"
				+ "\n"
				+ "/**\n"
				+ " * Response to MyTestCEX MarketData API <br/>\n"
				+ " * exchangeInfo REST endpoint request<br/>\n"
				+ " * Fetch market information of symbols that can be traded\n"
				+ " * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>\n"
				+ " */\n"
				+ "@JsonSerialize(using = MyTestCEXMarketDataExchangeInfoResponseSerializer.class)\n"
				+ "public class MyTestCEXMarketDataExchangeInfoResponse implements ExchangeInfoResponseInterface1, ExchangeInfoResponseInterface2 {\n"
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
				+ "  public boolean equals(Object other) {\n"
				+ "    if (other == null)\n"
				+ "      return false;\n"
				+ "    if (!getClass().equals(other.getClass()))\n"
				+ "      return false;\n"
				+ "    MyTestCEXMarketDataExchangeInfoResponse o = (MyTestCEXMarketDataExchangeInfoResponse) other;\n"
				+ "    return Objects.equals(payload, o.payload)\n"
				+ "            && Objects.equals(responseCode, o.responseCode);\n"
				+ "  }\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public int hashCode() {\n"
				+ "    return Objects.hash(payload, responseCode);\n"
				+ "  }\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public String toString() {\n"
				+ "    return EncodingUtil.pojoToString(this);\n"
				+ "  }\n"
				+ "}\n", 
				Files.readString(checkSourceFileExists(Paths.get("pojo", "MyTestCEXMarketDataExchangeInfoResponse.java"))));
		
		checkSourceFileExists(Paths.get("pojo", "MyTestCEXMarketDataExchangeInfoResponsePayload.java"));
		
		checkJavaFilesCount(Paths.get("serializers"), 3);
		checkSourceFileExists(Paths.get("serializers", "MyTestCEXMarketDataExchangeInfoRequestSerializer.java"));        
		checkSourceFileExists(Paths.get("serializers", "MyTestCEXMarketDataExchangeInfoResponsePayloadSerializer.java"));
		checkSourceFileExists(Paths.get("serializers", "MyTestCEXMarketDataExchangeInfoResponseSerializer.java"));               
	}

	@Test
	public void testGenerateClassesSpecificApiRequestTypesPrimitiveType() throws Exception {
		srcFolder = ClassesGeneratorTestUtil.generateTmpDir();
		ExchangeDescriptor exchange = new ExchangeDescriptorParser().fromJson(Paths.get(".", "src", "test", "resources", "testExchangeDescriptorWithAllRestRequestDataTypes.json"));
		ExchangeApiDescriptor api = exchange.getApis().get(0);
		RestEndpointDescriptor restEndpoint = findRestEndpointByName("postRestRequestDataTypeInt", api);
		RestEndpointClassesGenerator generator = new RestEndpointClassesGenerator(exchange, api, restEndpoint);
		generator.generateClasses(srcFolder);
		
		checkJavaFilesCount(Paths.get("deserializers"), 1);
		checkSourceFileExists(Paths.get("deserializers", "GenericResponseDeserializer.java"));
		
		checkJavaFilesCount(Paths.get("pojo"), 1);
		checkSourceFileExists(Paths.get("pojo", "GenericResponse.java"));
		
		checkJavaFilesCount(Paths.get("serializers"), 1);
		checkSourceFileExists(Paths.get("serializers", "GenericResponseSerializer.java"));           
	}
	
	@Test
	public void testGenerateClassesSpecificApiRequestTypesPrimitiveListType() throws Exception {
		srcFolder = ClassesGeneratorTestUtil.generateTmpDir();
		ExchangeDescriptor exchange = new ExchangeDescriptorParser().fromJson(Paths.get(".", "src", "test", "resources", "testExchangeDescriptorWithAllRestRequestDataTypes.json"));
		ExchangeApiDescriptor api = exchange.getApis().get(0);
		RestEndpointDescriptor restEndpoint = findRestEndpointByName("postRestRequestDataTypeIntList", api);
		RestEndpointClassesGenerator generator = new RestEndpointClassesGenerator(exchange, api, restEndpoint);
		generator.generateClasses(srcFolder);
		checkJavaFilesCount(srcFolder, 0);        
	}
	
	@Test
	public void testGenerateClassesSpecificApiRequestTypesNoParametersRequest() throws Exception {
		srcFolder = ClassesGeneratorTestUtil.generateTmpDir();
		ExchangeDescriptor exchange = new ExchangeDescriptorParser().fromJson(Paths.get(".", "src", "test", "resources", "testExchangeDescriptorWithAllRestRequestDataTypes.json"));
		ExchangeApiDescriptor api = exchange.getApis().get(0);
		RestEndpointDescriptor restEndpoint = findRestEndpointByName("postRestRequestDataTypeObjectNoParameters", api);
		RestEndpointClassesGenerator generator = new RestEndpointClassesGenerator(exchange, api, restEndpoint);
		generator.generateClasses(srcFolder);
		checkJavaFilesCount(srcFolder, 0);        
	}
	
	@Test
	public void testGenerateClassesSpecificApiResponseTypesPrimitiveType() throws Exception {
		srcFolder = ClassesGeneratorTestUtil.generateTmpDir();
		ExchangeDescriptor exchange = new ExchangeDescriptorParser().fromJson(Paths.get(".", "src", "test", "resources", "testExchangeDescriptorWithAllRestResponseDataTypes.json"));
		ExchangeApiDescriptor api = exchange.getApis().get(0);
		RestEndpointDescriptor restEndpoint = findRestEndpointByName("getRestResponseDataTypeInt", api);
		RestEndpointClassesGenerator generator = new RestEndpointClassesGenerator(exchange, api, restEndpoint);
		generator.generateClasses(srcFolder);
		checkJavaFilesCount(srcFolder, 0); 
	}
	
	@Test
	public void testGenerateClassesSpecificApiResponseTypesPrimitiveListType() throws Exception {
		srcFolder = ClassesGeneratorTestUtil.generateTmpDir();
		ExchangeDescriptor exchange = new ExchangeDescriptorParser().fromJson(Paths.get(".", "src", "test", "resources", "testExchangeDescriptorWithAllRestResponseDataTypes.json"));
		ExchangeApiDescriptor api = exchange.getApis().get(0);
		RestEndpointDescriptor restEndpoint = findRestEndpointByName("getRestResponseDataTypeIntList", api);
		RestEndpointClassesGenerator generator = new RestEndpointClassesGenerator(exchange, api, restEndpoint);
		generator.generateClasses(srcFolder);
		checkJavaFilesCount(srcFolder, 0); 
	}
	
	@Test
	public void testGenerateClassesSpecificApiResponseTypesEmptyResponse() throws Exception {
		srcFolder = ClassesGeneratorTestUtil.generateTmpDir();
		ExchangeDescriptor exchange = new ExchangeDescriptorParser().fromJson(Paths.get(".", "src", "test", "resources", "testExchangeDescriptorWithAllRestResponseDataTypes.json"));
		ExchangeApiDescriptor api = exchange.getApis().get(0);
		RestEndpointDescriptor restEndpoint = findRestEndpointByName("getRestEmptyResponseDataType", api);
		RestEndpointClassesGenerator generator = new RestEndpointClassesGenerator(exchange, api, restEndpoint);
		generator.generateClasses(srcFolder);
		checkJavaFilesCount(srcFolder, 0); 
	}
}
