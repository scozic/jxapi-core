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
 * Unit test for {@link WebsocketEndpointClassesGenerator}
 */
public class WebsocketEndpointClassesGeneratorTest {
	
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
	
	private WebsocketEndpointDescriptor findWebsocketEndpointByName(String name, ExchangeApiDescriptor exchangeDescriptor) {
		for (WebsocketEndpointDescriptor api: exchangeDescriptor.getWebsocketEndpoints()) {
			if (api.getName().equals(name)) {
				return api;
			}
		}
		throw new AssertionError("No such API:" + name + " in:" + exchangeDescriptor);
	}

	@Test
	public void testGenerateWebsocketEndpointClasses() throws IOException {
		srcFolder = ClassesGeneratorTestUtil.generateTmpDir();
		ExchangeDescriptor exchange = new ExchangeDescriptorParser().fromJson(Paths.get(".", "src", "test", "resources", "testCEXDescriptor.json"));
		ExchangeApiDescriptor api = exchange.getApis().get(0);
		WebsocketEndpointDescriptor wsEndpoint = api.getWebsocketEndpoints().get(0);
		WebsocketEndpointClassesGenerator generator = new WebsocketEndpointClassesGenerator(exchange, api, wsEndpoint);
		generator.generateClasses(srcFolder);
		
		checkJavaFilesCount(Paths.get("deserializers"), 1);
		checkSourceFileExists(Paths.get("deserializers", "MyTestCEXMarketDataTickerStreamMessageDeserializer.java"));
		
		checkJavaFilesCount(Paths.get("pojo"), 2);
		
		Assert.assertEquals("package com.foo.bar.gen.marketdata.pojo;\n"
				+ "\n"
				+ "import com.fasterxml.jackson.databind.annotation.JsonSerialize;\n"
				+ "import com.foo.bar.gen.marketdata.serializers.MyTestCEXMarketDataTickerStreamRequestSerializer;\n"
				+ "import com.scz.jxapi.util.EncodingUtil;\n"
				+ "import java.util.Objects;\n"
				+ "\n"
				+ "/**\n"
				+ " * Subscription request toMyTestCEX MarketData API tickerStream websocket endpoint<br/>\n"
				+ " * Subscribe to ticker stream\n"
				+ " * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>\n"
				+ " */\n"
				+ "@JsonSerialize(using = MyTestCEXMarketDataTickerStreamRequestSerializer.class)\n"
				+ "public class MyTestCEXMarketDataTickerStreamRequest {\n"
				+ "  private String symbol;\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @return Symbol to subscribe to ticker stream of\n"
				+ "   */\n"
				+ "  public String getSymbol() {\n"
				+ "    return symbol;\n"
				+ "  }\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @param symbol Symbol to subscribe to ticker stream of\n"
				+ "   */\n"
				+ "  public void setSymbol(String symbol) {\n"
				+ "    this.symbol = symbol;\n"
				+ "  }\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public boolean equals(Object other) {\n"
				+ "    if (other == null)\n"
				+ "      return false;\n"
				+ "    if (!getClass().equals(other.getClass()))\n"
				+ "      return false;\n"
				+ "    MyTestCEXMarketDataTickerStreamRequest o = (MyTestCEXMarketDataTickerStreamRequest) other;\n"
				+ "    return Objects.equals(symbol, o.symbol);\n"
				+ "  }\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public int hashCode() {\n"
				+ "    return Objects.hash(symbol);\n"
				+ "  }\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public String toString() {\n"
				+ "    return EncodingUtil.pojoToString(this);\n"
				+ "  }\n"
				+ "}\n", 
				Files.readString(checkSourceFileExists(Paths.get("pojo", "MyTestCEXMarketDataTickerStreamRequest.java"))));
		
		Assert.assertEquals("package com.foo.bar.gen.marketdata.pojo;\n"
				+ "\n"
				+ "import com.fasterxml.jackson.databind.annotation.JsonSerialize;\n"
				+ "import com.foo.bar.gen.marketdata.serializers.MyTestCEXMarketDataTickerStreamMessageSerializer;\n"
				+ "import com.scz.jxapi.util.EncodingUtil;\n"
				+ "import java.math.BigDecimal;\n"
				+ "import java.util.Objects;\n"
				+ "\n"
				+ "/**\n"
				+ " * Message disseminated upon subscription to MyTestCEX MarketData API tickerStream websocket endpoint request<br/>\n"
				+ " * Subscribe to ticker stream\n"
				+ " * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>\n"
				+ " */\n"
				+ "@JsonSerialize(using = MyTestCEXMarketDataTickerStreamMessageSerializer.class)\n"
				+ "public class MyTestCEXMarketDataTickerStreamMessage {\n"
				+ "  private BigDecimal last;\n"
				+ "  private String symbol;\n"
				+ "  private String topic;\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @return Last traded price Message field <strong>p</strong>\n"
				+ "   */\n"
				+ "  public BigDecimal getLast() {\n"
				+ "    return last;\n"
				+ "  }\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @param last Last traded price Message field <strong>p</strong>\n"
				+ "   */\n"
				+ "  public void setLast(BigDecimal last) {\n"
				+ "    this.last = last;\n"
				+ "  }\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @return Symbol name Message field <strong>s</strong>\n"
				+ "   */\n"
				+ "  public String getSymbol() {\n"
				+ "    return symbol;\n"
				+ "  }\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @param symbol Symbol name Message field <strong>s</strong>\n"
				+ "   */\n"
				+ "  public void setSymbol(String symbol) {\n"
				+ "    this.symbol = symbol;\n"
				+ "  }\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @return Topic Message field <strong>t</strong>\n"
				+ "   */\n"
				+ "  public String getTopic() {\n"
				+ "    return topic;\n"
				+ "  }\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @param topic Topic Message field <strong>t</strong>\n"
				+ "   */\n"
				+ "  public void setTopic(String topic) {\n"
				+ "    this.topic = topic;\n"
				+ "  }\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public boolean equals(Object other) {\n"
				+ "    if (other == null)\n"
				+ "      return false;\n"
				+ "    if (!getClass().equals(other.getClass()))\n"
				+ "      return false;\n"
				+ "    MyTestCEXMarketDataTickerStreamMessage o = (MyTestCEXMarketDataTickerStreamMessage) other;\n"
				+ "    return Objects.equals(last, o.last)\n"
				+ "            && Objects.equals(symbol, o.symbol)\n"
				+ "            && Objects.equals(topic, o.topic);\n"
				+ "  }\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public int hashCode() {\n"
				+ "    return Objects.hash(last, symbol, topic);\n"
				+ "  }\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public String toString() {\n"
				+ "    return EncodingUtil.pojoToString(this);\n"
				+ "  }\n"
				+ "}\n"
				+ "", 
				Files.readString(checkSourceFileExists(Paths.get("pojo", "MyTestCEXMarketDataTickerStreamMessage.java"))));
		
		checkJavaFilesCount(Paths.get("serializers"), 2);
		checkSourceFileExists(Paths.get("serializers", "MyTestCEXMarketDataTickerStreamRequestSerializer.java"));
		checkSourceFileExists(Paths.get("serializers", "MyTestCEXMarketDataTickerStreamMessageSerializer.java"));
	}
	
	@Test
	public void testGenerateWebsocketEndpointWithRequestAndMessageImplementingCustomInterfaces() throws Exception {
		srcFolder = ClassesGeneratorTestUtil.generateTmpDir();
		ExchangeDescriptor exchange = new ExchangeDescriptorParser().fromJson(Paths.get(".", "src", "test", "resources", "testExchangeDescriptorWithRequestAndResponseImplementingCustomInterfaces.json"));
		ExchangeApiDescriptor api = exchange.getApis().get(0);
		WebsocketEndpointDescriptor wsEndpoint = api.getWebsocketEndpoints().get(0);
		WebsocketEndpointClassesGenerator generator = new WebsocketEndpointClassesGenerator(exchange, api, wsEndpoint);
		generator.generateClasses(srcFolder);
		checkJavaFilesCount(Paths.get("deserializers"), 1);
		checkSourceFileExists(Paths.get("deserializers", "MyTestCEXMarketDataTickerStreamMessageDeserializer.java"));
		
		checkJavaFilesCount(Paths.get("pojo"), 2);
		
		Assert.assertEquals("package com.foo.bar.gen.marketdata.pojo;\n"
				+ "\n"
				+ "import com.fasterxml.jackson.databind.annotation.JsonSerialize;\n"
				+ "import com.foo.bar.TickerStreamRequestInterface1;\n"
				+ "import com.foo.bar.TickerStreamRequestInterface2;\n"
				+ "import com.foo.bar.gen.marketdata.serializers.MyTestCEXMarketDataTickerStreamRequestSerializer;\n"
				+ "import com.scz.jxapi.util.EncodingUtil;\n"
				+ "import java.util.Objects;\n"
				+ "\n"
				+ "/**\n"
				+ " * Subscription request toMyTestCEX MarketData API tickerStream websocket endpoint<br/>\n"
				+ " * Subscribe to ticker stream\n"
				+ " * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>\n"
				+ " */\n"
				+ "@JsonSerialize(using = MyTestCEXMarketDataTickerStreamRequestSerializer.class)\n"
				+ "public class MyTestCEXMarketDataTickerStreamRequest implements TickerStreamRequestInterface1, TickerStreamRequestInterface2 {\n"
				+ "  private String symbol;\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @return Symbol to subscribe to ticker stream of\n"
				+ "   */\n"
				+ "  public String getSymbol() {\n"
				+ "    return symbol;\n"
				+ "  }\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @param symbol Symbol to subscribe to ticker stream of\n"
				+ "   */\n"
				+ "  public void setSymbol(String symbol) {\n"
				+ "    this.symbol = symbol;\n"
				+ "  }\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public boolean equals(Object other) {\n"
				+ "    if (other == null)\n"
				+ "      return false;\n"
				+ "    if (!getClass().equals(other.getClass()))\n"
				+ "      return false;\n"
				+ "    MyTestCEXMarketDataTickerStreamRequest o = (MyTestCEXMarketDataTickerStreamRequest) other;\n"
				+ "    return Objects.equals(symbol, o.symbol);\n"
				+ "  }\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public int hashCode() {\n"
				+ "    return Objects.hash(symbol);\n"
				+ "  }\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public String toString() {\n"
				+ "    return EncodingUtil.pojoToString(this);\n"
				+ "  }\n"
				+ "}\n", 
				Files.readString(checkSourceFileExists(Paths.get("pojo", "MyTestCEXMarketDataTickerStreamRequest.java"))));
		
		Assert.assertEquals("package com.foo.bar.gen.marketdata.pojo;\n"
				+ "\n"
				+ "import com.fasterxml.jackson.databind.annotation.JsonSerialize;\n"
				+ "import com.foo.bar.TickerStreamResponseInterface1;\n"
				+ "import com.foo.bar.TickerStreamResponseInterface2;\n"
				+ "import com.foo.bar.gen.marketdata.serializers.MyTestCEXMarketDataTickerStreamMessageSerializer;\n"
				+ "import com.scz.jxapi.util.EncodingUtil;\n"
				+ "import java.math.BigDecimal;\n"
				+ "import java.util.Objects;\n"
				+ "\n"
				+ "/**\n"
				+ " * Message disseminated upon subscription to MyTestCEX MarketData API tickerStream websocket endpoint request<br/>\n"
				+ " * Subscribe to ticker stream\n"
				+ " * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>\n"
				+ " */\n"
				+ "@JsonSerialize(using = MyTestCEXMarketDataTickerStreamMessageSerializer.class)\n"
				+ "public class MyTestCEXMarketDataTickerStreamMessage implements TickerStreamResponseInterface1, TickerStreamResponseInterface2 {\n"
				+ "  private BigDecimal last;\n"
				+ "  private String symbol;\n"
				+ "  private String topic;\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @return Last traded price Message field <strong>p</strong>\n"
				+ "   */\n"
				+ "  public BigDecimal getLast() {\n"
				+ "    return last;\n"
				+ "  }\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @param last Last traded price Message field <strong>p</strong>\n"
				+ "   */\n"
				+ "  public void setLast(BigDecimal last) {\n"
				+ "    this.last = last;\n"
				+ "  }\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @return Symbol name Message field <strong>s</strong>\n"
				+ "   */\n"
				+ "  public String getSymbol() {\n"
				+ "    return symbol;\n"
				+ "  }\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @param symbol Symbol name Message field <strong>s</strong>\n"
				+ "   */\n"
				+ "  public void setSymbol(String symbol) {\n"
				+ "    this.symbol = symbol;\n"
				+ "  }\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @return Topic Message field <strong>t</strong>\n"
				+ "   */\n"
				+ "  public String getTopic() {\n"
				+ "    return topic;\n"
				+ "  }\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @param topic Topic Message field <strong>t</strong>\n"
				+ "   */\n"
				+ "  public void setTopic(String topic) {\n"
				+ "    this.topic = topic;\n"
				+ "  }\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public boolean equals(Object other) {\n"
				+ "    if (other == null)\n"
				+ "      return false;\n"
				+ "    if (!getClass().equals(other.getClass()))\n"
				+ "      return false;\n"
				+ "    MyTestCEXMarketDataTickerStreamMessage o = (MyTestCEXMarketDataTickerStreamMessage) other;\n"
				+ "    return Objects.equals(last, o.last)\n"
				+ "            && Objects.equals(symbol, o.symbol)\n"
				+ "            && Objects.equals(topic, o.topic);\n"
				+ "  }\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public int hashCode() {\n"
				+ "    return Objects.hash(last, symbol, topic);\n"
				+ "  }\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public String toString() {\n"
				+ "    return EncodingUtil.pojoToString(this);\n"
				+ "  }\n"
				+ "}\n", 
				Files.readString(checkSourceFileExists(Paths.get("pojo", "MyTestCEXMarketDataTickerStreamMessage.java"))));
		
		checkJavaFilesCount(Paths.get("serializers"), 2);
		checkSourceFileExists(Paths.get("serializers", "MyTestCEXMarketDataTickerStreamRequestSerializer.java"));
		checkSourceFileExists(Paths.get("serializers", "MyTestCEXMarketDataTickerStreamMessageSerializer.java"));
		
	}
	
	@Test
	public void testGenerateClassesSpecificApiRequestTypesPrimitiveType() throws Exception {
		srcFolder = ClassesGeneratorTestUtil.generateTmpDir();
		ExchangeDescriptor exchange = new ExchangeDescriptorParser().fromJson(Paths.get(".", "src", "test", "resources", "testExchangeDescriptorWithAllRestRequestDataTypes.json"));
		ExchangeApiDescriptor api = exchange.getApis().get(0);
		WebsocketEndpointDescriptor websocketEndpoint = findWebsocketEndpointByName("streamWithIntRequestDataType", api);
		WebsocketEndpointClassesGenerator generator = new WebsocketEndpointClassesGenerator(exchange, api, websocketEndpoint);
		generator.generateClasses(srcFolder);
		checkJavaFilesCount(Paths.get("."), 0);       
	}
	
	@Test
	public void testGenerateClassesSpecificApiRequestTypesEmptyRequestAndDefinedResponse() throws Exception {
		srcFolder = ClassesGeneratorTestUtil.generateTmpDir();
		ExchangeDescriptor exchange = new ExchangeDescriptorParser().fromJson(Paths.get(".", "src", "test", "resources", "testExchangeDescriptorWithAllRestRequestDataTypes.json"));
		ExchangeApiDescriptor api = exchange.getApis().get(0);
		WebsocketEndpointDescriptor websocketEndpoint = findWebsocketEndpointByName("streamWithObjectRequestDataTypeZeroParameters", api);
		WebsocketEndpointClassesGenerator generator = new WebsocketEndpointClassesGenerator(exchange, api, websocketEndpoint);
		generator.generateClasses(srcFolder);
		checkJavaFilesCount(Paths.get("."), 3);
		checkJavaFilesCount(Paths.get("serializers"), 1);
		checkSourceFileExists(Paths.get("serializers", "TickerSerializer.java"));
		checkJavaFilesCount(Paths.get("deserializers"), 1);
		checkSourceFileExists(Paths.get("deserializers", "TickerDeserializer.java"));
		checkJavaFilesCount(Paths.get("pojo"), 1);
		checkSourceFileExists(Paths.get("pojo", "Ticker.java"));
	}
	
	@Test
	public void testGenerateClassesSpecificApiRequestTypesObjectListMapRequestEmptyObject() throws Exception {
		srcFolder = ClassesGeneratorTestUtil.generateTmpDir();
		ExchangeDescriptor exchange = new ExchangeDescriptorParser().fromJson(Paths.get(".", "src", "test", "resources", "testExchangeDescriptorWithAllRestRequestDataTypes.json"));
		ExchangeApiDescriptor api = exchange.getApis().get(0);
		WebsocketEndpointDescriptor websocketEndpoint = findWebsocketEndpointByName("streamWithObjectListMapRequestDataType", api);
		WebsocketEndpointClassesGenerator generator = new WebsocketEndpointClassesGenerator(exchange, api, websocketEndpoint);
		generator.generateClasses(srcFolder);
		checkJavaFilesCount(Paths.get("."), 2);
		// FIXME
//		Files.walk(srcFolder)
//	      .sorted(Comparator.reverseOrder())
//	      .map(Path::toFile)
//	      .forEach(f -> System.out.println(f.getAbsolutePath()));
		
		checkJavaFilesCount(Paths.get("serializers"), 1);
		checkSourceFileExists(Paths.get("serializers", "MyTestCEXMarketDataStreamWithObjectListMapRequestDataTypeRequestSerializer.java"));
		checkJavaFilesCount(Paths.get("pojo"), 1);
		checkSourceFileExists(Paths.get("pojo", "MyTestCEXMarketDataStreamWithObjectListMapRequestDataTypeRequest.java"));
	}
	
	/*
	 		
		// FIXME
		Files.walk(srcFolder)
	      .sorted(Comparator.reverseOrder())
	      .map(Path::toFile)
	      .forEach(f -> System.out.println(f.getAbsolutePath()));
		
	 */

}
