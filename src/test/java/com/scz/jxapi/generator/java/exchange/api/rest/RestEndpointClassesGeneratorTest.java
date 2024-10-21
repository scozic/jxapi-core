package com.scz.jxapi.generator.java.exchange.api.rest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import com.scz.jxapi.exchange.descriptor.ExchangeApiDescriptor;
import com.scz.jxapi.exchange.descriptor.ExchangeDescriptor;
import com.scz.jxapi.exchange.descriptor.RestEndpointDescriptor;
import com.scz.jxapi.exchange.descriptor.parser.ExchangeDescriptorParser;
import com.scz.jxapi.generator.java.JavaCodeGenerationUtil;
import com.scz.jxapi.generator.java.exchange.ClassesGeneratorTestUtil;

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
	
	private void checkJavaFilesCount(Path relativePkg, int count) throws IOException {
		ClassesGeneratorTestUtil.checkJavaFilesCount(srcFolder.resolve(BASE_PKG).resolve(relativePkg), count);
	}
	
	private Path checkSourceFileExists(Path srcFilePath) {
		return ClassesGeneratorTestUtil.checkSourceFileExists(srcFolder.resolve(BASE_PKG), srcFilePath);
	}

	@Test
	public void testGenerateRestEndpointClasses() throws IOException {
		srcFolder = ClassesGeneratorTestUtil.generateTmpDir();
		ExchangeDescriptor exchange = new ExchangeDescriptorParser().fromJson(Paths.get(".", "src", "test", "resources", "testExchangeDescriptor.json"));
		ExchangeApiDescriptor api = exchange.getApis().get(0);
		RestEndpointDescriptor restEndpoint = api.getRestEndpoints().get(0);
		RestEndpointClassesGenerator generator = new RestEndpointClassesGenerator(exchange, api, restEndpoint);
		generator.generateClasses(srcFolder);
		
		checkJavaFilesCount(Paths.get("deserializers"), 2);
		checkSourceFileExists(Paths.get("deserializers", "MyTestExchangeMarketDataExchangeInfoResponseDeserializer.java"));
		checkSourceFileExists(Paths.get("deserializers", "MyTestExchangeMarketDataExchangeInfoResponsePayloadDeserializer.java"));
		
		checkJavaFilesCount(Paths.get("pojo"), 3);
		
		Assert.assertEquals("package com.foo.bar.gen.marketdata.pojo;\n"
				+ "\n"
				+ "import com.fasterxml.jackson.databind.annotation.JsonSerialize;\n"
				+ "import com.foo.bar.gen.marketdata.serializers.MyTestExchangeMarketDataExchangeInfoRequestSerializer;\n"
				+ "import com.scz.jxapi.util.EncodingUtil;\n"
				+ "import java.util.List;\n"
				+ "import java.util.Objects;\n"
				+ "\n"
				+ "/**\n"
				+ " * Request for MyTestExchange MarketData API exchangeInfo REST endpoint<br/>\n"
				+ " * Fetch market information of symbols that can be traded\n"
				+ " * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>\n"
				+ " */\n"
				+ "@JsonSerialize(using = MyTestExchangeMarketDataExchangeInfoRequestSerializer.class)\n"
				+ "public class MyTestExchangeMarketDataExchangeInfoRequest {\n"
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
				+ "    MyTestExchangeMarketDataExchangeInfoRequest o = (MyTestExchangeMarketDataExchangeInfoRequest) other;\n"
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
				Files.readString(checkSourceFileExists(Paths.get("pojo", "MyTestExchangeMarketDataExchangeInfoRequest.java"))));
		
		Assert.assertEquals("package com.foo.bar.gen.marketdata.pojo;\n"
				+ "\n"
				+ "import com.fasterxml.jackson.databind.annotation.JsonSerialize;\n"
				+ "import com.foo.bar.gen.marketdata.serializers.MyTestExchangeMarketDataExchangeInfoResponseSerializer;\n"
				+ "import com.scz.jxapi.util.EncodingUtil;\n"
				+ "import java.util.List;\n"
				+ "import java.util.Objects;\n"
				+ "\n"
				+ "/**\n"
				+ " * Response to MyTestExchange MarketData API <br/>\n"
				+ " * exchangeInfo REST endpoint request<br/>\n"
				+ " * Fetch market information of symbols that can be traded\n"
				+ " * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>\n"
				+ " */\n"
				+ "@JsonSerialize(using = MyTestExchangeMarketDataExchangeInfoResponseSerializer.class)\n"
				+ "public class MyTestExchangeMarketDataExchangeInfoResponse {\n"
				+ "  private List<MyTestExchangeMarketDataExchangeInfoResponsePayload> payload;\n"
				+ "  private Integer responseCode;\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @return List of market information for each requested symbol\n"
				+ "   */\n"
				+ "  public List<MyTestExchangeMarketDataExchangeInfoResponsePayload> getPayload() {\n"
				+ "    return payload;\n"
				+ "  }\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @param payload List of market information for each requested symbol\n"
				+ "   */\n"
				+ "  public void setPayload(List<MyTestExchangeMarketDataExchangeInfoResponsePayload> payload) {\n"
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
				+ "    MyTestExchangeMarketDataExchangeInfoResponse o = (MyTestExchangeMarketDataExchangeInfoResponse) other;\n"
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
				Files.readString(checkSourceFileExists(Paths.get("pojo", "MyTestExchangeMarketDataExchangeInfoResponse.java"))));
		
		checkSourceFileExists(Paths.get("pojo", "MyTestExchangeMarketDataExchangeInfoResponsePayload.java"));
		
		checkJavaFilesCount(Paths.get("serializers"), 3);
		checkSourceFileExists(Paths.get("serializers", "MyTestExchangeMarketDataExchangeInfoRequestSerializer.java"));        
		checkSourceFileExists(Paths.get("serializers", "MyTestExchangeMarketDataExchangeInfoResponsePayloadSerializer.java"));
		checkSourceFileExists(Paths.get("serializers", "MyTestExchangeMarketDataExchangeInfoResponseSerializer.java"));               
	}
	
	@Test
	public void testGenerateRestEndpointClassesWithRequestAndResponseImplementingCustomInterfaces() throws IOException {
		srcFolder = ClassesGeneratorTestUtil.generateTmpDir();
		ExchangeDescriptor exchange = new ExchangeDescriptorParser().fromJson(Paths.get(".", "src", "test", "resources", "testExchangeDescriptorWithRequestAndResponseImplementingCustomInterfaces.json"));
		ExchangeApiDescriptor api = exchange.getApis().get(0);
		RestEndpointDescriptor restEndpoint = ClassesGeneratorTestUtil.findRestEndpointByName("exchangeInfo", api);
		RestEndpointClassesGenerator generator = new RestEndpointClassesGenerator(exchange, api, restEndpoint);
		generator.generateClasses(srcFolder);
		
		checkJavaFilesCount(Paths.get("deserializers"), 2);
		checkSourceFileExists(Paths.get("deserializers", "MyTestExchangeMarketDataExchangeInfoResponseDeserializer.java"));
		checkSourceFileExists(Paths.get("deserializers", "MyTestExchangeMarketDataExchangeInfoResponsePayloadDeserializer.java"));
		
		checkJavaFilesCount(Paths.get("pojo"), 3);
		
		Assert.assertEquals("package com.foo.bar.gen.marketdata.pojo;\n"
				+ "\n"
				+ "import com.fasterxml.jackson.databind.annotation.JsonSerialize;\n"
				+ "import com.foo.bar.ExchangeInfoRequestInterface1;\n"
				+ "import com.foo.bar.ExchangeInfoRequestInterface2;\n"
				+ "import com.foo.bar.gen.marketdata.serializers.MyTestExchangeMarketDataExchangeInfoRequestSerializer;\n"
				+ "import com.scz.jxapi.util.EncodingUtil;\n"
				+ "import java.util.List;\n"
				+ "import java.util.Objects;\n"
				+ "\n"
				+ "/**\n"
				+ " * Request for MyTestExchange MarketData API exchangeInfo REST endpoint<br/>\n"
				+ " * Fetch market information of symbols that can be traded\n"
				+ " * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>\n"
				+ " */\n"
				+ "@JsonSerialize(using = MyTestExchangeMarketDataExchangeInfoRequestSerializer.class)\n"
				+ "public class MyTestExchangeMarketDataExchangeInfoRequest implements ExchangeInfoRequestInterface1, ExchangeInfoRequestInterface2 {\n"
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
				+ "    MyTestExchangeMarketDataExchangeInfoRequest o = (MyTestExchangeMarketDataExchangeInfoRequest) other;\n"
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
				Files.readString(checkSourceFileExists(Paths.get("pojo", "MyTestExchangeMarketDataExchangeInfoRequest.java"))));
		
		Assert.assertEquals("package com.foo.bar.gen.marketdata.pojo;\n"
				+ "\n"
				+ "import com.fasterxml.jackson.databind.annotation.JsonSerialize;\n"
				+ "import com.foo.bar.ExchangeInfoResponseInterface1;\n"
				+ "import com.foo.bar.ExchangeInfoResponseInterface2;\n"
				+ "import com.foo.bar.gen.marketdata.serializers.MyTestExchangeMarketDataExchangeInfoResponseSerializer;\n"
				+ "import com.scz.jxapi.util.EncodingUtil;\n"
				+ "import java.util.List;\n"
				+ "import java.util.Objects;\n"
				+ "\n"
				+ "/**\n"
				+ " * Response to MyTestExchange MarketData API <br/>\n"
				+ " * exchangeInfo REST endpoint request<br/>\n"
				+ " * Fetch market information of symbols that can be traded\n"
				+ " * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>\n"
				+ " */\n"
				+ "@JsonSerialize(using = MyTestExchangeMarketDataExchangeInfoResponseSerializer.class)\n"
				+ "public class MyTestExchangeMarketDataExchangeInfoResponse implements ExchangeInfoResponseInterface1, ExchangeInfoResponseInterface2 {\n"
				+ "  private List<MyTestExchangeMarketDataExchangeInfoResponsePayload> payload;\n"
				+ "  private Integer responseCode;\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @return List of market information for each requested symbol\n"
				+ "   */\n"
				+ "  public List<MyTestExchangeMarketDataExchangeInfoResponsePayload> getPayload() {\n"
				+ "    return payload;\n"
				+ "  }\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @param payload List of market information for each requested symbol\n"
				+ "   */\n"
				+ "  public void setPayload(List<MyTestExchangeMarketDataExchangeInfoResponsePayload> payload) {\n"
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
				+ "    MyTestExchangeMarketDataExchangeInfoResponse o = (MyTestExchangeMarketDataExchangeInfoResponse) other;\n"
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
				Files.readString(checkSourceFileExists(Paths.get("pojo", "MyTestExchangeMarketDataExchangeInfoResponse.java"))));
		
		checkSourceFileExists(Paths.get("pojo", "MyTestExchangeMarketDataExchangeInfoResponsePayload.java"));
		
		checkJavaFilesCount(Paths.get("serializers"), 3);
		checkSourceFileExists(Paths.get("serializers", "MyTestExchangeMarketDataExchangeInfoRequestSerializer.java"));        
		checkSourceFileExists(Paths.get("serializers", "MyTestExchangeMarketDataExchangeInfoResponsePayloadSerializer.java"));
		checkSourceFileExists(Paths.get("serializers", "MyTestExchangeMarketDataExchangeInfoResponseSerializer.java"));               
	}

	@Test
	public void testGenerateClassesSpecificApiRequestTypesPrimitiveType() throws Exception {
		srcFolder = ClassesGeneratorTestUtil.generateTmpDir();
		ExchangeDescriptor exchange = new ExchangeDescriptorParser().fromJson(Paths.get(".", "src", "test", "resources", "testExchangeDescriptorWithAllRestRequestDataTypes.json"));
		ExchangeApiDescriptor api = exchange.getApis().get(0);
		RestEndpointDescriptor restEndpoint = ClassesGeneratorTestUtil.findRestEndpointByName("postRestRequestDataTypeInt", api);
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
		RestEndpointDescriptor restEndpoint = ClassesGeneratorTestUtil.findRestEndpointByName("postRestRequestDataTypeIntList", api);
		RestEndpointClassesGenerator generator = new RestEndpointClassesGenerator(exchange, api, restEndpoint);
		generator.generateClasses(srcFolder);
		checkJavaFilesCount(srcFolder, 0);        
	}
	
	@Test
	public void testGenerateClassesSpecificApiRequestTypesNoParametersRequest() throws Exception {
		srcFolder = ClassesGeneratorTestUtil.generateTmpDir();
		ExchangeDescriptor exchange = new ExchangeDescriptorParser().fromJson(Paths.get(".", "src", "test", "resources", "testExchangeDescriptorWithAllRestRequestDataTypes.json"));
		ExchangeApiDescriptor api = exchange.getApis().get(0);
		RestEndpointDescriptor restEndpoint = ClassesGeneratorTestUtil.findRestEndpointByName("postRestRequestDataTypeObjectNoParameters", api);
		RestEndpointClassesGenerator generator = new RestEndpointClassesGenerator(exchange, api, restEndpoint);
		generator.generateClasses(srcFolder);
		checkJavaFilesCount(srcFolder, 0);        
	}
	
	@Test
	public void testGenerateClassesSpecificApiResponseTypesPrimitiveType() throws Exception {
		srcFolder = ClassesGeneratorTestUtil.generateTmpDir();
		ExchangeDescriptor exchange = new ExchangeDescriptorParser().fromJson(Paths.get(".", "src", "test", "resources", "testExchangeDescriptorWithAllRestResponseDataTypes.json"));
		ExchangeApiDescriptor api = exchange.getApis().get(0);
		RestEndpointDescriptor restEndpoint = ClassesGeneratorTestUtil.findRestEndpointByName("getRestResponseDataTypeInt", api);
		RestEndpointClassesGenerator generator = new RestEndpointClassesGenerator(exchange, api, restEndpoint);
		generator.generateClasses(srcFolder);
		checkJavaFilesCount(srcFolder, 0); 
	}
	
	@Test
	public void testGenerateClassesSpecificApiResponseTypesPrimitiveListType() throws Exception {
		srcFolder = ClassesGeneratorTestUtil.generateTmpDir();
		ExchangeDescriptor exchange = new ExchangeDescriptorParser().fromJson(Paths.get(".", "src", "test", "resources", "testExchangeDescriptorWithAllRestResponseDataTypes.json"));
		ExchangeApiDescriptor api = exchange.getApis().get(0);
		RestEndpointDescriptor restEndpoint = ClassesGeneratorTestUtil.findRestEndpointByName("getRestResponseDataTypeIntList", api);
		RestEndpointClassesGenerator generator = new RestEndpointClassesGenerator(exchange, api, restEndpoint);
		generator.generateClasses(srcFolder);
		checkJavaFilesCount(srcFolder, 0); 
	}
	
	@Test
	public void testGenerateClassesSpecificApiResponseTypesEmptyResponse() throws Exception {
		srcFolder = ClassesGeneratorTestUtil.generateTmpDir();
		ExchangeDescriptor exchange = new ExchangeDescriptorParser().fromJson(Paths.get(".", "src", "test", "resources", "testExchangeDescriptorWithAllRestResponseDataTypes.json"));
		ExchangeApiDescriptor api = exchange.getApis().get(0);
		RestEndpointDescriptor restEndpoint = ClassesGeneratorTestUtil.findRestEndpointByName("getRestEmptyResponseDataType", api);
		RestEndpointClassesGenerator generator = new RestEndpointClassesGenerator(exchange, api, restEndpoint);
		generator.generateClasses(srcFolder);
		checkJavaFilesCount(srcFolder, 0); 
	}
}
