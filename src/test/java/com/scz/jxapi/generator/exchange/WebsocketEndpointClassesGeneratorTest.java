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
 * Unit test for {@link WebsocketEndpointClassesGenerator}
 */
public class WebsocketEndpointClassesGeneratorTest {
	
	private Path srcFolder;
	
	@After
	public void tearDown() throws IOException {
		if (srcFolder != null) {
			JavaCodeGenerationUtil.deletePath(srcFolder);
			srcFolder = null;
		}
	}

	@Test
	public void testGenerateWebsocketEndpointClasses() throws IOException {
		srcFolder = Paths.get("tmp" + Math.random());
		
		srcFolder = Paths.get("tmp" + Math.random());
		ExchangeDescriptor exchange = new ExchangeDescriptorParser().fromJson(Paths.get(".", "src", "test", "resources", "testCEXDescriptor.json"));
		ExchangeApiDescriptor api = exchange.getApis().get(0);
		WebsocketEndpointDescriptor wsEndpoint = api.getWebsocketEndpoints().get(0);
		WebsocketEndpointClassesGenerator generator = new WebsocketEndpointClassesGenerator(exchange, api, wsEndpoint);
		generator.generateClasses(srcFolder);
		
		checkJavaFilesCount(Paths.get("deserializers"), 1);
		checkSourceFileExists(Paths.get("deserializers", "MyTestCEXMarketDataTickerStreamMessageDeserializer.java"));
		
		checkJavaFilesCount(Paths.get("pojo"), 2);
		checkResponsePojoContent(checkSourceFileExists(Paths.get("pojo", "MyTestCEXMarketDataTickerStreamRequest.java")));
		checkRequestPojoContent(checkSourceFileExists(Paths.get("pojo", "MyTestCEXMarketDataTickerStreamMessage.java")));
		
		checkJavaFilesCount(Paths.get("serializers"), 2);
		checkSourceFileExists(Paths.get("serializers", "MyTestCEXMarketDataTickerStreamRequestSerializer.java"));
		checkSourceFileExists(Paths.get("serializers", "MyTestCEXMarketDataTickerStreamMessageSerializer.java"));
	}
	
	private void checkRequestPojoContent(Path requestPojoFile) throws IOException {
		Assert.assertEquals("package com.foo.bar.gen.marketdata.pojo;\n"
				+ "\n"
				+ "import com.fasterxml.jackson.databind.annotation.JsonSerialize;\n"
				+ "import com.foo.bar.gen.marketdata.serializers.MyTestCEXMarketDataTickerStreamMessageSerializer;\n"
				+ "import com.scz.jxapi.util.EncodingUtil;\n"
				+ "import java.math.BigDecimal;\n"
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
				+ "  public String toString() {\n"
				+ "    return EncodingUtil.pojoToString(this);\n"
				+ "  }\n"
				+ "}\n", 
				Files.readString(requestPojoFile));
	}
	
	private void checkResponsePojoContent(Path requestPojoFile) throws IOException {
		Assert.assertEquals("package com.foo.bar.gen.marketdata.pojo;\n"
				+ "\n"
				+ "import com.fasterxml.jackson.databind.annotation.JsonSerialize;\n"
				+ "import com.foo.bar.gen.marketdata.serializers.MyTestCEXMarketDataTickerStreamRequestSerializer;\n"
				+ "import com.scz.jxapi.netutils.websocket.WebsocketSubscribeParameters;\n"
				+ "import com.scz.jxapi.util.EncodingUtil;\n"
				+ "\n"
				+ "/**\n"
				+ " * Subscription request toMyTestCEX MarketData API tickerStream websocket endpoint<br/>\n"
				+ " * Subscribe to ticker stream\n"
				+ " * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>\n"
				+ " */\n"
				+ "@JsonSerialize(using = MyTestCEXMarketDataTickerStreamRequestSerializer.class)\n"
				+ "public class MyTestCEXMarketDataTickerStreamRequest implements WebsocketSubscribeParameters {\n"
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
				+ "  \n"
				+ "  @Override\n"
				+ "  public String getTopic() {\n"
				+ "    return EncodingUtil.substituteArguments(\"${symbol}@ticker\", \"symbol\", symbol);\n"
				+ "  }\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public String toString() {\n"
				+ "    return EncodingUtil.pojoToString(this);\n"
				+ "  }\n"
				+ "}\n", 
				Files.readString(requestPojoFile));
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

}
