package org.jxapi.generator.java.exchange;

import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Test;
import org.jxapi.exchange.descriptor.gen.ExchangeDescriptor;
import org.jxapi.exchange.descriptor.parser.ExchangeDescriptorParser;
import org.jxapi.generator.java.Imports;
import org.jxapi.util.EncodingUtil;
import org.jxapi.util.PlaceHolderResolver;

/**
 * Unit test for {@link ExchangeConstantValuePlaceholderResolverFactory}
 */
public class ExchangeConstantValuePlaceholderResolverFactoryTest {
  
  @Test
  public void testCreateConstantValuePlaceholderResolver() throws Exception {
    ExchangeDescriptor exchangeDescriptor = ExchangeDescriptorParser.fromJson(Paths.get(".", "src", "test", "resources", "testExchangeDescriptor.json"));
    ExchangeConstantValuePlaceholderResolverFactory factory = new ExchangeConstantValuePlaceholderResolverFactory(exchangeDescriptor);
    Imports imports = new Imports();
    PlaceHolderResolver resolver = factory.createConstantValuePlaceholderResolver(imports);
    String template = "Ping message is ${constants.pingMessage} and pong message is ${constants.pongMessage}";
    String result = resolver.resolve(template);
    Assert.assertEquals("EncodingUtil.substituteArguments(\"Ping message is ${constants.pingMessage} and pong message is ${constants.pongMessage}\", \"constants.pingMessage\", MyTestExchangeConstants.PING_MESSAGE, \"constants.pongMessage\", MyTestExchangeConstants.PONG_MESSAGE)", result);
    Assert.assertEquals(2, imports.size());
    Assert.assertTrue(imports.contains(EncodingUtil.class.getName()));
    Assert.assertTrue(imports.contains("com.foo.bar.gen.MyTestExchangeConstants"));
  }

}
