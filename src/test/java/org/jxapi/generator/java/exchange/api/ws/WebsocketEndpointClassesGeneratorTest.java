package org.jxapi.generator.java.exchange.api.ws;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import org.jxapi.exchange.descriptor.ExchangeApiDescriptor;
import org.jxapi.exchange.descriptor.ExchangeDescriptor;
import org.jxapi.exchange.descriptor.WebsocketEndpointDescriptor;
import org.jxapi.exchange.descriptor.parser.ExchangeDescriptorParser;
import org.jxapi.generator.java.JavaCodeGenUtil;
import org.jxapi.generator.java.exchange.ClassesGeneratorTestUtil;

/**
 * Unit test for {@link WebsocketEndpointClassesGenerator}
 */
public class WebsocketEndpointClassesGeneratorTest {
  
  private static final Path BASE_PKG = Paths.get("com", "foo", "bar", "gen", "marketdata");
  
  private Path srcFolder;
  
  @After
  public void tearDown() throws IOException {
    if (srcFolder != null) {
      JavaCodeGenUtil.deletePath(srcFolder);
      srcFolder = null;
    }
  }
  
  private void checkJavaFilesCount(Path relativePkg, int count) {
    ClassesGeneratorTestUtil.checkJavaFilesCount(srcFolder.resolve(BASE_PKG).resolve(relativePkg), count);
  }
  
  private Path checkSourceFileExists(Path srcFilePath) {
    return ClassesGeneratorTestUtil.checkSourceFileExists(srcFolder.resolve(BASE_PKG), srcFilePath);
  }

  @Test
  public void testGenerateWebsocketEndpointClasses() throws IOException {
    srcFolder = ClassesGeneratorTestUtil.generateTmpDir();
    ExchangeDescriptor exchange = ExchangeDescriptorParser.fromJson(Paths.get(".", "src", "test", "resources", "testExchangeDescriptor.json"));
    ExchangeApiDescriptor api = exchange.getApis().get(0);
    WebsocketEndpointDescriptor wsEndpoint = api.getWebsocketEndpoints().get(0);
    WebsocketEndpointClassesGenerator generator = new WebsocketEndpointClassesGenerator(exchange, api, wsEndpoint);
    generator.generateClasses(srcFolder);
    
    checkJavaFilesCount(Paths.get("deserializers"), 1);
    checkSourceFileExists(Paths.get("deserializers", "MyTestExchangeMarketDataTickerStreamMessageDeserializer.java"));
    
    checkJavaFilesCount(Paths.get("pojo"), 2);
    
    Assert.assertEquals("package com.foo.bar.gen.marketdata.pojo;\n"
        + "\n"
        + "import java.util.Objects;\n"
        + "\n"
        + "import com.fasterxml.jackson.databind.annotation.JsonSerialize;\n"
        + "import com.foo.bar.gen.marketdata.serializers.MyTestExchangeMarketDataTickerStreamRequestSerializer;\n"
        + "import javax.annotation.processing.Generated;\n"
        + "import org.jxapi.util.CompareUtil;\n"
        + "import org.jxapi.util.EncodingUtil;\n"
        + "import org.jxapi.util.Pojo;\n"
        + "\n"
        + "/**\n"
        + " * Subscription request toMyTestExchange MarketData API tickerStream websocket endpoint<br>\n"
        + " * Subscribe to ticker stream\n"
        + " */\n"
        + "@Generated(\"org.jxapi.generator.java.exchange.api.pojo.PojoGenerator\")\n"
        + "@JsonSerialize(using = MyTestExchangeMarketDataTickerStreamRequestSerializer.class)\n"
        + "public class MyTestExchangeMarketDataTickerStreamRequest implements Pojo<MyTestExchangeMarketDataTickerStreamRequest> {\n"
        + "  \n"
        + "  private static final long serialVersionUID = -6050628374331236559L;\n"
        + "  \n"
        + "  /**\n"
        + "   * @return A new builder to build {@link MyTestExchangeMarketDataTickerStreamRequest} objects\n"
        + "   */\n"
        + "  public static Builder builder() {\n"
        + "    return new Builder();\n"
        + "  }\n"
        + "  \n"
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
        + "    MyTestExchangeMarketDataTickerStreamRequest o = (MyTestExchangeMarketDataTickerStreamRequest) other;\n"
        + "    return Objects.equals(symbol, o.symbol);\n"
        + "  }\n"
        + "  \n"
        + "  @Override\n"
        + "  public int compareTo(MyTestExchangeMarketDataTickerStreamRequest other) {\n"
        + "    if (other == null) {\n"
        + "      return 1;\n"
        + "    }\n"
        + "    int res = 0;\n"
        + "    res = CompareUtil.compare(this.symbol, other.symbol);\n"
        + "    return res;\n"
        + "  }\n"
        + "  \n"
        + "  @Override\n"
        + "  public int hashCode() {\n"
        + "    return Objects.hash(symbol);\n"
        + "  }\n"
        + "  \n"
        + "  @Override\n"
        + "  public MyTestExchangeMarketDataTickerStreamRequest deepClone() {\n"
        + "    MyTestExchangeMarketDataTickerStreamRequest clone = new MyTestExchangeMarketDataTickerStreamRequest();\n"
        + "    clone.symbol = this.symbol;\n"
        + "    return clone;\n"
        + "  }\n"
        + "  \n"
        + "  @Override\n"
        + "  public String toString() {\n"
        + "    return EncodingUtil.pojoToString(this);\n"
        + "  }\n"
        + "  \n"
        + "  /**\n"
        + "   * Builder for {@link MyTestExchangeMarketDataTickerStreamRequest}\n"
        + "   */\n"
        + "  @Generated(\"org.jxapi.generator.java.JavaTypeGenerator\")\n"
        + "  public static class Builder {\n"
        + "    \n"
        + "    private String symbol;\n"
        + "    \n"
        + "    /**\n"
        + "     * Will set the value of <code>symbol</code> field in the builder\n"
        + "     * @param symbol Symbol to subscribe to ticker stream of\n"
        + "     * @return Builder instance\n"
        + "     * @see #setSymbol(String)\n"
        + "     */\n"
        + "    public Builder symbol(String symbol)  {\n"
        + "      this.symbol = symbol;\n"
        + "      return this;\n"
        + "    }\n"
        + "    \n"
        + "    /**\n"
        + "     * @return a new instance of MyTestExchangeMarketDataTickerStreamRequest using the values set in this builder\n"
        + "     */\n"
        + "    public MyTestExchangeMarketDataTickerStreamRequest build() {\n"
        + "      MyTestExchangeMarketDataTickerStreamRequest res = new MyTestExchangeMarketDataTickerStreamRequest();\n"
        + "      res.symbol = this.symbol;\n"
        + "      return res;\n"
        + "    }\n"
        + "  }\n"
        + "}\n", 
        Files.readString(checkSourceFileExists(Paths.get("pojo", "MyTestExchangeMarketDataTickerStreamRequest.java"))));
    
    Assert.assertEquals("package com.foo.bar.gen.marketdata.pojo;\n"
        + "\n"
        + "import java.math.BigDecimal;\n"
        + "import java.util.Objects;\n"
        + "\n"
        + "import com.fasterxml.jackson.databind.annotation.JsonSerialize;\n"
        + "import com.foo.bar.gen.marketdata.serializers.MyTestExchangeMarketDataTickerStreamMessageSerializer;\n"
        + "import javax.annotation.processing.Generated;\n"
        + "import org.jxapi.util.CompareUtil;\n"
        + "import org.jxapi.util.EncodingUtil;\n"
        + "import org.jxapi.util.Pojo;\n"
        + "\n"
        + "/**\n"
        + " * Message disseminated upon subscription to MyTestExchange MarketData API tickerStream websocket endpoint request<br>\n"
        + " * Subscribe to ticker stream\n"
        + " */\n"
        + "@Generated(\"org.jxapi.generator.java.exchange.api.pojo.PojoGenerator\")\n"
        + "@JsonSerialize(using = MyTestExchangeMarketDataTickerStreamMessageSerializer.class)\n"
        + "public class MyTestExchangeMarketDataTickerStreamMessage implements Pojo<MyTestExchangeMarketDataTickerStreamMessage> {\n"
        + "  \n"
        + "  private static final long serialVersionUID = -4262053138471100088L;\n"
        + "  \n"
        + "  /**\n"
        + "   * @return A new builder to build {@link MyTestExchangeMarketDataTickerStreamMessage} objects\n"
        + "   */\n"
        + "  public static Builder builder() {\n"
        + "    return new Builder();\n"
        + "  }\n"
        + "  \n"
        + "  private String topic;\n"
        + "  private String symbol;\n"
        + "  private BigDecimal last;\n"
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
        + "  @Override\n"
        + "  public boolean equals(Object other) {\n"
        + "    if (other == null)\n"
        + "      return false;\n"
        + "    if (!getClass().equals(other.getClass()))\n"
        + "      return false;\n"
        + "    MyTestExchangeMarketDataTickerStreamMessage o = (MyTestExchangeMarketDataTickerStreamMessage) other;\n"
        + "    return Objects.equals(topic, o.topic)\n"
        + "            && Objects.equals(symbol, o.symbol)\n"
        + "            && Objects.equals(last, o.last);\n"
        + "  }\n"
        + "  \n"
        + "  @Override\n"
        + "  public int compareTo(MyTestExchangeMarketDataTickerStreamMessage other) {\n"
        + "    if (other == null) {\n"
        + "      return 1;\n"
        + "    }\n"
        + "    int res = 0;\n"
        + "    res = CompareUtil.compare(this.topic, other.topic);\n"
        + "    if (res != 0) {\n"
        + "      return res;\n"
        + "    }\n"
        + "    res = CompareUtil.compare(this.symbol, other.symbol);\n"
        + "    if (res != 0) {\n"
        + "      return res;\n"
        + "    }\n"
        + "    res = CompareUtil.compare(this.last, other.last);\n"
        + "    return res;\n"
        + "  }\n"
        + "  \n"
        + "  @Override\n"
        + "  public int hashCode() {\n"
        + "    return Objects.hash(topic, symbol, last);\n"
        + "  }\n"
        + "  \n"
        + "  @Override\n"
        + "  public MyTestExchangeMarketDataTickerStreamMessage deepClone() {\n"
        + "    MyTestExchangeMarketDataTickerStreamMessage clone = new MyTestExchangeMarketDataTickerStreamMessage();\n"
        + "    clone.topic = this.topic;\n"
        + "    clone.symbol = this.symbol;\n"
        + "    clone.last = this.last;\n"
        + "    return clone;\n"
        + "  }\n"
        + "  \n"
        + "  @Override\n"
        + "  public String toString() {\n"
        + "    return EncodingUtil.pojoToString(this);\n"
        + "  }\n"
        + "  \n"
        + "  /**\n"
        + "   * Builder for {@link MyTestExchangeMarketDataTickerStreamMessage}\n"
        + "   */\n"
        + "  @Generated(\"org.jxapi.generator.java.JavaTypeGenerator\")\n"
        + "  public static class Builder {\n"
        + "    \n"
        + "    private String topic;\n"
        + "    private String symbol;\n"
        + "    private BigDecimal last;\n"
        + "    \n"
        + "    /**\n"
        + "     * Will set the value of <code>topic</code> field in the builder\n"
        + "     * @param topic Topic Message field <strong>t</strong>\n"
        + "     * @return Builder instance\n"
        + "     * @see #setTopic(String)\n"
        + "     */\n"
        + "    public Builder topic(String topic)  {\n"
        + "      this.topic = topic;\n"
        + "      return this;\n"
        + "    }\n"
        + "    \n"
        + "    /**\n"
        + "     * Will set the value of <code>symbol</code> field in the builder\n"
        + "     * @param symbol Symbol name Message field <strong>s</strong>\n"
        + "     * @return Builder instance\n"
        + "     * @see #setSymbol(String)\n"
        + "     */\n"
        + "    public Builder symbol(String symbol)  {\n"
        + "      this.symbol = symbol;\n"
        + "      return this;\n"
        + "    }\n"
        + "    \n"
        + "    /**\n"
        + "     * Will set the value of <code>last</code> field in the builder\n"
        + "     * @param last Last traded price Message field <strong>p</strong>\n"
        + "     * @return Builder instance\n"
        + "     * @see #setLast(BigDecimal)\n"
        + "     */\n"
        + "    public Builder last(BigDecimal last)  {\n"
        + "      this.last = last;\n"
        + "      return this;\n"
        + "    }\n"
        + "    \n"
        + "    /**\n"
        + "     * @return a new instance of MyTestExchangeMarketDataTickerStreamMessage using the values set in this builder\n"
        + "     */\n"
        + "    public MyTestExchangeMarketDataTickerStreamMessage build() {\n"
        + "      MyTestExchangeMarketDataTickerStreamMessage res = new MyTestExchangeMarketDataTickerStreamMessage();\n"
        + "      res.topic = this.topic;\n"
        + "      res.symbol = this.symbol;\n"
        + "      res.last = this.last;\n"
        + "      return res;\n"
        + "    }\n"
        + "  }\n"
        + "}\n", 
        Files.readString(checkSourceFileExists(Paths.get("pojo", "MyTestExchangeMarketDataTickerStreamMessage.java"))));
    
    checkJavaFilesCount(Paths.get("serializers"), 2);
    checkSourceFileExists(Paths.get("serializers", "MyTestExchangeMarketDataTickerStreamRequestSerializer.java"));
    checkSourceFileExists(Paths.get("serializers", "MyTestExchangeMarketDataTickerStreamMessageSerializer.java"));
  }
  
  @Test
  public void testGenerateWebsocketEndpointWithRequestAndMessageImplementingCustomInterfaces() throws Exception {
    srcFolder = ClassesGeneratorTestUtil.generateTmpDir();
    ExchangeDescriptor exchange = ExchangeDescriptorParser.fromJson(Paths.get(".", "src", "test", "resources", "testExchangeDescriptorWithRequestAndResponseImplementingCustomInterfaces.json"));
    ExchangeApiDescriptor api = exchange.getApis().get(0);
    WebsocketEndpointDescriptor wsEndpoint = api.getWebsocketEndpoints().get(0);
    WebsocketEndpointClassesGenerator generator = new WebsocketEndpointClassesGenerator(exchange, api, wsEndpoint);
    generator.generateClasses(srcFolder);
    checkJavaFilesCount(Paths.get("deserializers"), 1);
    checkSourceFileExists(Paths.get("deserializers", "MyTestExchangeMarketDataTickerStreamMessageDeserializer.java"));
    
    checkJavaFilesCount(Paths.get("pojo"), 2);
    
    Assert.assertEquals("package com.foo.bar.gen.marketdata.pojo;\n"
        + "\n"
        + "import java.util.Objects;\n"
        + "\n"
        + "import com.fasterxml.jackson.databind.annotation.JsonSerialize;\n"
        + "import com.foo.bar.TickerStreamRequestInterface1;\n"
        + "import com.foo.bar.TickerStreamRequestInterface2;\n"
        + "import com.foo.bar.gen.marketdata.serializers.MyTestExchangeMarketDataTickerStreamRequestSerializer;\n"
        + "import javax.annotation.processing.Generated;\n"
        + "import org.jxapi.util.CompareUtil;\n"
        + "import org.jxapi.util.EncodingUtil;\n"
        + "import org.jxapi.util.Pojo;\n"
        + "\n"
        + "/**\n"
        + " * Subscription request toMyTestExchange MarketData API tickerStream websocket endpoint<br>\n"
        + " * Subscribe to ticker stream\n"
        + " */\n"
        + "@Generated(\"org.jxapi.generator.java.exchange.api.pojo.PojoGenerator\")\n"
        + "@JsonSerialize(using = MyTestExchangeMarketDataTickerStreamRequestSerializer.class)\n"
        + "public class MyTestExchangeMarketDataTickerStreamRequest implements Pojo<MyTestExchangeMarketDataTickerStreamRequest>, TickerStreamRequestInterface1, TickerStreamRequestInterface2 {\n"
        + "  \n"
        + "  private static final long serialVersionUID = 8012256550868394115L;\n"
        + "  \n"
        + "  /**\n"
        + "   * @return A new builder to build {@link MyTestExchangeMarketDataTickerStreamRequest} objects\n"
        + "   */\n"
        + "  public static Builder builder() {\n"
        + "    return new Builder();\n"
        + "  }\n"
        + "  \n"
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
        + "    MyTestExchangeMarketDataTickerStreamRequest o = (MyTestExchangeMarketDataTickerStreamRequest) other;\n"
        + "    return Objects.equals(symbol, o.symbol);\n"
        + "  }\n"
        + "  \n"
        + "  @Override\n"
        + "  public int compareTo(MyTestExchangeMarketDataTickerStreamRequest other) {\n"
        + "    if (other == null) {\n"
        + "      return 1;\n"
        + "    }\n"
        + "    int res = 0;\n"
        + "    res = CompareUtil.compare(this.symbol, other.symbol);\n"
        + "    return res;\n"
        + "  }\n"
        + "  \n"
        + "  @Override\n"
        + "  public int hashCode() {\n"
        + "    return Objects.hash(symbol);\n"
        + "  }\n"
        + "  \n"
        + "  @Override\n"
        + "  public MyTestExchangeMarketDataTickerStreamRequest deepClone() {\n"
        + "    MyTestExchangeMarketDataTickerStreamRequest clone = new MyTestExchangeMarketDataTickerStreamRequest();\n"
        + "    clone.symbol = this.symbol;\n"
        + "    return clone;\n"
        + "  }\n"
        + "  \n"
        + "  @Override\n"
        + "  public String toString() {\n"
        + "    return EncodingUtil.pojoToString(this);\n"
        + "  }\n"
        + "  \n"
        + "  /**\n"
        + "   * Builder for {@link MyTestExchangeMarketDataTickerStreamRequest}\n"
        + "   */\n"
        + "  @Generated(\"org.jxapi.generator.java.JavaTypeGenerator\")\n"
        + "  public static class Builder {\n"
        + "    \n"
        + "    private String symbol;\n"
        + "    \n"
        + "    /**\n"
        + "     * Will set the value of <code>symbol</code> field in the builder\n"
        + "     * @param symbol Symbol to subscribe to ticker stream of\n"
        + "     * @return Builder instance\n"
        + "     * @see #setSymbol(String)\n"
        + "     */\n"
        + "    public Builder symbol(String symbol)  {\n"
        + "      this.symbol = symbol;\n"
        + "      return this;\n"
        + "    }\n"
        + "    \n"
        + "    /**\n"
        + "     * @return a new instance of MyTestExchangeMarketDataTickerStreamRequest using the values set in this builder\n"
        + "     */\n"
        + "    public MyTestExchangeMarketDataTickerStreamRequest build() {\n"
        + "      MyTestExchangeMarketDataTickerStreamRequest res = new MyTestExchangeMarketDataTickerStreamRequest();\n"
        + "      res.symbol = this.symbol;\n"
        + "      return res;\n"
        + "    }\n"
        + "  }\n"
        + "}\n", 
        Files.readString(checkSourceFileExists(Paths.get("pojo", "MyTestExchangeMarketDataTickerStreamRequest.java"))));
    
    Assert.assertEquals("package com.foo.bar.gen.marketdata.pojo;\n"
        + "\n"
        + "import java.math.BigDecimal;\n"
        + "import java.util.Objects;\n"
        + "\n"
        + "import com.fasterxml.jackson.databind.annotation.JsonSerialize;\n"
        + "import com.foo.bar.TickerStreamResponseInterface1;\n"
        + "import com.foo.bar.TickerStreamResponseInterface2;\n"
        + "import com.foo.bar.gen.marketdata.serializers.MyTestExchangeMarketDataTickerStreamMessageSerializer;\n"
        + "import javax.annotation.processing.Generated;\n"
        + "import org.jxapi.util.CompareUtil;\n"
        + "import org.jxapi.util.EncodingUtil;\n"
        + "import org.jxapi.util.Pojo;\n"
        + "\n"
        + "/**\n"
        + " * Message disseminated upon subscription to MyTestExchange MarketData API tickerStream websocket endpoint request<br>\n"
        + " * Subscribe to ticker stream\n"
        + " */\n"
        + "@Generated(\"org.jxapi.generator.java.exchange.api.pojo.PojoGenerator\")\n"
        + "@JsonSerialize(using = MyTestExchangeMarketDataTickerStreamMessageSerializer.class)\n"
        + "public class MyTestExchangeMarketDataTickerStreamMessage implements Pojo<MyTestExchangeMarketDataTickerStreamMessage>, TickerStreamResponseInterface1, TickerStreamResponseInterface2 {\n"
        + "  \n"
        + "  private static final long serialVersionUID = 1671784664167570937L;\n"
        + "  \n"
        + "  /**\n"
        + "   * @return A new builder to build {@link MyTestExchangeMarketDataTickerStreamMessage} objects\n"
        + "   */\n"
        + "  public static Builder builder() {\n"
        + "    return new Builder();\n"
        + "  }\n"
        + "  \n"
        + "  private String topic;\n"
        + "  private String symbol;\n"
        + "  private BigDecimal last;\n"
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
        + "  @Override\n"
        + "  public boolean equals(Object other) {\n"
        + "    if (other == null)\n"
        + "      return false;\n"
        + "    if (!getClass().equals(other.getClass()))\n"
        + "      return false;\n"
        + "    MyTestExchangeMarketDataTickerStreamMessage o = (MyTestExchangeMarketDataTickerStreamMessage) other;\n"
        + "    return Objects.equals(topic, o.topic)\n"
        + "            && Objects.equals(symbol, o.symbol)\n"
        + "            && Objects.equals(last, o.last);\n"
        + "  }\n"
        + "  \n"
        + "  @Override\n"
        + "  public int compareTo(MyTestExchangeMarketDataTickerStreamMessage other) {\n"
        + "    if (other == null) {\n"
        + "      return 1;\n"
        + "    }\n"
        + "    int res = 0;\n"
        + "    res = CompareUtil.compare(this.topic, other.topic);\n"
        + "    if (res != 0) {\n"
        + "      return res;\n"
        + "    }\n"
        + "    res = CompareUtil.compare(this.symbol, other.symbol);\n"
        + "    if (res != 0) {\n"
        + "      return res;\n"
        + "    }\n"
        + "    res = CompareUtil.compare(this.last, other.last);\n"
        + "    return res;\n"
        + "  }\n"
        + "  \n"
        + "  @Override\n"
        + "  public int hashCode() {\n"
        + "    return Objects.hash(topic, symbol, last);\n"
        + "  }\n"
        + "  \n"
        + "  @Override\n"
        + "  public MyTestExchangeMarketDataTickerStreamMessage deepClone() {\n"
        + "    MyTestExchangeMarketDataTickerStreamMessage clone = new MyTestExchangeMarketDataTickerStreamMessage();\n"
        + "    clone.topic = this.topic;\n"
        + "    clone.symbol = this.symbol;\n"
        + "    clone.last = this.last;\n"
        + "    return clone;\n"
        + "  }\n"
        + "  \n"
        + "  @Override\n"
        + "  public String toString() {\n"
        + "    return EncodingUtil.pojoToString(this);\n"
        + "  }\n"
        + "  \n"
        + "  /**\n"
        + "   * Builder for {@link MyTestExchangeMarketDataTickerStreamMessage}\n"
        + "   */\n"
        + "  @Generated(\"org.jxapi.generator.java.JavaTypeGenerator\")\n"
        + "  public static class Builder {\n"
        + "    \n"
        + "    private String topic;\n"
        + "    private String symbol;\n"
        + "    private BigDecimal last;\n"
        + "    \n"
        + "    /**\n"
        + "     * Will set the value of <code>topic</code> field in the builder\n"
        + "     * @param topic Topic Message field <strong>t</strong>\n"
        + "     * @return Builder instance\n"
        + "     * @see #setTopic(String)\n"
        + "     */\n"
        + "    public Builder topic(String topic)  {\n"
        + "      this.topic = topic;\n"
        + "      return this;\n"
        + "    }\n"
        + "    \n"
        + "    /**\n"
        + "     * Will set the value of <code>symbol</code> field in the builder\n"
        + "     * @param symbol Symbol name Message field <strong>s</strong>\n"
        + "     * @return Builder instance\n"
        + "     * @see #setSymbol(String)\n"
        + "     */\n"
        + "    public Builder symbol(String symbol)  {\n"
        + "      this.symbol = symbol;\n"
        + "      return this;\n"
        + "    }\n"
        + "    \n"
        + "    /**\n"
        + "     * Will set the value of <code>last</code> field in the builder\n"
        + "     * @param last Last traded price Message field <strong>p</strong>\n"
        + "     * @return Builder instance\n"
        + "     * @see #setLast(BigDecimal)\n"
        + "     */\n"
        + "    public Builder last(BigDecimal last)  {\n"
        + "      this.last = last;\n"
        + "      return this;\n"
        + "    }\n"
        + "    \n"
        + "    /**\n"
        + "     * @return a new instance of MyTestExchangeMarketDataTickerStreamMessage using the values set in this builder\n"
        + "     */\n"
        + "    public MyTestExchangeMarketDataTickerStreamMessage build() {\n"
        + "      MyTestExchangeMarketDataTickerStreamMessage res = new MyTestExchangeMarketDataTickerStreamMessage();\n"
        + "      res.topic = this.topic;\n"
        + "      res.symbol = this.symbol;\n"
        + "      res.last = this.last;\n"
        + "      return res;\n"
        + "    }\n"
        + "  }\n"
        + "}\n", 
        Files.readString(checkSourceFileExists(Paths.get("pojo", "MyTestExchangeMarketDataTickerStreamMessage.java"))));
    
    checkJavaFilesCount(Paths.get("serializers"), 2);
    checkSourceFileExists(Paths.get("serializers", "MyTestExchangeMarketDataTickerStreamRequestSerializer.java"));
    checkSourceFileExists(Paths.get("serializers", "MyTestExchangeMarketDataTickerStreamMessageSerializer.java"));
    
  }
  
  @Test
  public void testGenerateClassesSpecificApiRequestTypesPrimitiveType() throws Exception {
    srcFolder = ClassesGeneratorTestUtil.generateTmpDir();
    ExchangeDescriptor exchange = ExchangeDescriptorParser.fromJson(Paths.get(".", "src", "test", "resources", "testExchangeDescriptorWithAllRestRequestDataTypes.json"));
    ExchangeApiDescriptor api = exchange.getApis().get(0);
    WebsocketEndpointDescriptor websocketEndpoint = ClassesGeneratorTestUtil.findWebsocketEndpointByName("streamWithIntRequestDataType", api);
    WebsocketEndpointClassesGenerator generator = new WebsocketEndpointClassesGenerator(exchange, api, websocketEndpoint);
    generator.generateClasses(srcFolder);
    checkJavaFilesCount(Paths.get("."), 0);       
  }
  
  @Test
  public void testGenerateClassesSpecificApiRequestTypesEmptyRequestAndDefinedResponse() throws Exception {
    srcFolder = ClassesGeneratorTestUtil.generateTmpDir();
    ExchangeDescriptor exchange = ExchangeDescriptorParser.fromJson(Paths.get(".", "src", "test", "resources", "testExchangeDescriptorWithAllRestRequestDataTypes.json"));
    ExchangeApiDescriptor api = exchange.getApis().get(0);
    WebsocketEndpointDescriptor websocketEndpoint = ClassesGeneratorTestUtil.findWebsocketEndpointByName("streamWithObjectRequestDataTypeZeroParameters", api);
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
  public void testGenerateClassesSpecificApiRequestTypesObjectListMapRequestReferencedObject() throws Exception {
    srcFolder = ClassesGeneratorTestUtil.generateTmpDir();
    ExchangeDescriptor exchange = ExchangeDescriptorParser.fromJson(Paths.get(".", "src", "test", "resources", "testExchangeDescriptorWithAllRestRequestDataTypes.json"));
    ExchangeApiDescriptor api = exchange.getApis().get(0);
    WebsocketEndpointDescriptor websocketEndpoint = ClassesGeneratorTestUtil.findWebsocketEndpointByName("streamWithObjectListMapRequestDataType", api);
    WebsocketEndpointClassesGenerator generator = new WebsocketEndpointClassesGenerator(exchange, api, websocketEndpoint);
    generator.generateClasses(srcFolder);
    checkJavaFilesCount(Paths.get("."), 0);  
  }
  
  @Test
  public void testGenerateClassesSpecificApiRequestTypeNullRequest() throws Exception {
    srcFolder = ClassesGeneratorTestUtil.generateTmpDir();
    ExchangeDescriptor exchange = ExchangeDescriptorParser.fromJson(Paths.get(".", "src", "test", "resources", "testExchangeDescriptorWithAllRestRequestDataTypes.json"));
    ExchangeApiDescriptor api = exchange.getApis().get(0);
    WebsocketEndpointDescriptor websocketEndpoint = ClassesGeneratorTestUtil.findWebsocketEndpointByName("streamWithIntRequestDataType", api);
    websocketEndpoint.setRequest(null);
    WebsocketEndpointClassesGenerator generator = new WebsocketEndpointClassesGenerator(exchange, api, websocketEndpoint);
    generator.generateClasses(srcFolder);
    checkJavaFilesCount(Paths.get("."), 0);       
  }

}
