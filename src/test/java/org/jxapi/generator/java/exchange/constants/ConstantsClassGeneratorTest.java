package org.jxapi.generator.java.exchange.constants;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import org.jxapi.exchange.descriptor.Constant;
import org.jxapi.exchange.descriptor.gen.ExchangeDescriptor;
import org.jxapi.exchange.descriptor.parser.ExchangeDescriptorParser;
import org.jxapi.generator.java.JavaCodeGenUtil;
import org.jxapi.generator.java.exchange.ExchangeGenUtil;
import org.jxapi.pojo.descriptor.Type;
import org.jxapi.util.EncodingUtil;
import org.jxapi.util.PlaceHolderResolver;

/**
 * Unit test for {@link ConstantsClassGenerator}
 */
public class ConstantsClassGeneratorTest {
  
  @Test
  public void testGettersAndSetters() {
    ConstantsClassGenerator gen = new ConstantsClassGenerator("com.x.y.MyConstants", List.of(), null);
    PlaceHolderResolver valuesPlaceHolderResolver = s -> "foo";
    gen.setConstantValuePlaceHolderResolver(valuesPlaceHolderResolver);
    Assert.assertSame(valuesPlaceHolderResolver, gen.getConstantValuePlaceHolderResolver());
  }

  @Test
  public void testGenerateClassWitheNoConstants() {
    ConstantsClassGenerator gen = new ConstantsClassGenerator("com.x.y.MyConstants", List.of(), null);
    Assert.assertEquals("package com.x.y;\n"
        + "\n"
        + "import javax.annotation.processing.Generated;\n"
        + "\n"
        + "\n"
        + "@Generated(\"org.jxapi.generator.java.exchange.constants.ConstantsClassGenerator\")\n"
        + "public class MyConstants {\n"
        + "  \n"
        + "  private MyConstants(){}\n"
        + "}\n", gen.generate());
  }
  
  @Test
  public void testGenerateClassWithMultipleConstants() {
    Constant c01 = Constant.create("myString", Type.STRING, null, "foo");
    Constant c02 = Constant.create("myInt", Type.INT, "A test Integer constant, for instance '${testMyIntValue}'", "${testMyIntValue}");
    Constant c03 = Constant.create("myIntWithValueAsString", Type.INT, "A test Integer constant with value specified as String", "123");
    Constant c04 = Constant.create("myBool", Type.BOOLEAN, "A test Boolean constant", true);
    Constant c05 = Constant.create("myBoolWithValueAsString", Type.INT, "A test Boolean constant with value specified as String", "true");
    Constant c06 = Constant.create("myLong", Type.LONG, "A test Long constant", 1234567890123456L);
    Constant c07 = Constant.create("myLongWithValueAsString", Type.LONG, "A test Long constant with value specified as String", "12345678901234567");
    Constant c08 = Constant.create("myTimestamp", Type.LONG, "A test Timestamp constant using 'now()' placeholder", "now()");
    Constant c09 = Constant.create("myBigDecimal", Type.BIGDECIMAL, "A test BigDecimal constant", 1.2345);
    Constant c10 = Constant.create("myBigDecimalWithValueAsString", Type.BIGDECIMAL, "A test BigDecimal constant with value specified as String", "5.4321");
    Constant c11 = Constant.create("myStringListWithValueAsObject", Type.fromTypeName("STRING_LIST"), "A test String list constant with value specified as list object", List.of("a", "b", "${testMyIntValue}"));
    Constant c12 = Constant.create("myIntMapWithValueAsObject", Type.fromTypeName("INT_MAP"), "A test String list constant with value specified as list object", Map.of("a", "${testMyIntValue}", "b", 456));
    
    PlaceHolderResolver docPlaceholderResolver = PlaceHolderResolver.create(Map.of("testMyIntValue", "123"));
    PlaceHolderResolver valuesPlaceHolderResolver = s -> {
      s = EncodingUtil.substituteArguments(s, Map.of("testMyIntValue", "1234"));
      return JavaCodeGenUtil.getQuotedString(s);
    };
    
    List<Constant> allConstants = List.of(c01, c02, c03, c04, c05, c06, c07, c08, c09, c10, c11, c12);
    ConstantsClassGenerator gen = new ConstantsClassGenerator("com.x.y.MyConstants", allConstants, docPlaceholderResolver);
    gen.setConstantValuePlaceHolderResolver(valuesPlaceHolderResolver);
    Assert.assertEquals("package com.x.y;\n"
        + "\n"
        + "import java.math.BigDecimal;\n"
        + "import java.util.List;\n"
        + "import java.util.Map;\n"
        + "\n"
        + "import javax.annotation.processing.Generated;\n"
        + "import org.jxapi.netutils.deserialization.json.field.IntegerJsonFieldDeserializer;\n"
        + "import org.jxapi.netutils.deserialization.json.field.ListJsonFieldDeserializer;\n"
        + "import org.jxapi.netutils.deserialization.json.field.MapJsonFieldDeserializer;\n"
        + "import org.jxapi.netutils.deserialization.json.field.StringJsonFieldDeserializer;\n"
        + "\n"
        + "\n"
        + "@Generated(\"org.jxapi.generator.java.exchange.constants.ConstantsClassGenerator\")\n"
        + "public class MyConstants {\n"
        + "  \n"
        + "  private MyConstants(){}\n"
        + "  \n"
        + "  public static final String MY_STRING = \"foo\";\n"
        + "  \n"
        + "  /**\n"
        + "   * A test Integer constant, for instance '123'\n"
        + "   */\n"
        + "  public static final Integer MY_INT = Integer.valueOf(\"1234\");\n"
        + "  \n"
        + "  /**\n"
        + "   * A test Integer constant with value specified as String\n"
        + "   */\n"
        + "  public static final Integer MY_INT_WITH_VALUE_AS_STRING = Integer.valueOf(\"123\");\n"
        + "  \n"
        + "  /**\n"
        + "   * A test Boolean constant\n"
        + "   */\n"
        + "  public static final Boolean MY_BOOL = Boolean.valueOf(\"true\");\n"
        + "  \n"
        + "  /**\n"
        + "   * A test Boolean constant with value specified as String\n"
        + "   */\n"
        + "  public static final Integer MY_BOOL_WITH_VALUE_AS_STRING = Integer.valueOf(\"true\");\n"
        + "  \n"
        + "  /**\n"
        + "   * A test Long constant\n"
        + "   */\n"
        + "  public static final Long MY_LONG = Long.valueOf(\"1234567890123456\");\n"
        + "  \n"
        + "  /**\n"
        + "   * A test Long constant with value specified as String\n"
        + "   */\n"
        + "  public static final Long MY_LONG_WITH_VALUE_AS_STRING = Long.valueOf(\"12345678901234567\");\n"
        + "  \n"
        + "  /**\n"
        + "   * A test Timestamp constant using 'now()' placeholder\n"
        + "   */\n"
        + "  public static final Long MY_TIMESTAMP = Long.valueOf(System.currentTimeMillis());\n"
        + "  \n"
        + "  /**\n"
        + "   * A test BigDecimal constant\n"
        + "   */\n"
        + "  public static final BigDecimal MY_BIG_DECIMAL = new BigDecimal(\"1.2345\");\n"
        + "  \n"
        + "  /**\n"
        + "   * A test BigDecimal constant with value specified as String\n"
        + "   */\n"
        + "  public static final BigDecimal MY_BIG_DECIMAL_WITH_VALUE_AS_STRING = new BigDecimal(\"5.4321\");\n"
        + "  \n"
        + "  /**\n"
        + "   * A test String list constant with value specified as list object\n"
        + "   */\n"
        + "  public static final List<String> MY_STRING_LIST_WITH_VALUE_AS_OBJECT = new ListJsonFieldDeserializer<>(StringJsonFieldDeserializer.getInstance()).deserialize(\"[\\\"a\\\",\\\"b\\\",\\\"1234\\\"]\");\n"
        + "  \n"
        + "  /**\n"
        + "   * A test String list constant with value specified as list object\n"
        + "   */\n"
        + "  public static final Map<String, Integer> MY_INT_MAP_WITH_VALUE_AS_OBJECT = new MapJsonFieldDeserializer<>(IntegerJsonFieldDeserializer.getInstance()).deserialize(\"{\\\"a\\\":\\\"1234\\\",\\\"b\\\":456}\");\n"
        + "}\n"
        + "", gen.generate());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidConstantNotAPrimitiveType() {
    new ConstantsClassGenerator("com.x.y.MyConstants", List.of(Constant.create("myConst", Type.fromTypeName("OBJECT_MAP"), "My description", "[1, 2, 3]")), null).generate();
  }
  
  @Test
  public void testGenerateClassWithConstantGroups() {
    Constant c01 = Constant.create("myString", Type.STRING, null, "foo");
    
    Constant c0201 = Constant.create("myInt", Type.INT, "A test Integer constant, for instance '${testMyIntValue}'", "${testMyIntValue}");
    Constant c0202 = Constant.create("myBigDecimal", Type.BIGDECIMAL, "A test BigDecimal constant", 1.2345);
    
    Constant c020301 = Constant.create("myLong", Type.LONG, "A test Long constant, for instance ${testMyLongValue}", "${testMyLongValue}");
    Constant g0203 = Constant.createGroup("myNestedGroup", "A group of constants nested in a parent group", List.of(c020301)); 
    
    Constant g02 = Constant.createGroup("myGroup", "A ${bar} related group of constants", List.of(c0201, c0202, g0203));
    
    PlaceHolderResolver docPlaceholderResolver = PlaceHolderResolver.create(Map.of("testMyIntValue", "123", "testMyLongValue", "1234567890123459L", "foo", "Foo", "bar", "Bar"));
    PlaceHolderResolver valuesPlaceHolderResolver = s -> {
      s = EncodingUtil.substituteArguments(s, Map.of("testMyIntValue", 1234, "testMyLongValue", 1234567890123456L));
      return JavaCodeGenUtil.getQuotedString(s);
    };
    
    ConstantsClassGenerator gen = new ConstantsClassGenerator("com.x.y.MyConstants", List.of(c01, g02), docPlaceholderResolver);
    gen.setDescription("Constants for ${foo}");
    gen.setConstantValuePlaceHolderResolver(valuesPlaceHolderResolver);
    
    Assert.assertEquals("package com.x.y;\n"
        + "\n"
        + "import java.math.BigDecimal;\n"
        + "\n"
        + "import javax.annotation.processing.Generated;\n"
        + "\n"
        + "/**\n"
        + " * Constants for Foo\n"
        + " */\n"
        + "@Generated(\"org.jxapi.generator.java.exchange.constants.ConstantsClassGenerator\")\n"
        + "public class MyConstants {\n"
        + "  \n"
        + "  private MyConstants(){}\n"
        + "  \n"
        + "  public static final String MY_STRING = \"foo\";\n"
        + "  \n"
        + "  /**\n"
        + "   * A Bar related group of constants\n"
        + "   */\n"
        + "  @Generated(\"org.jxapi.generator.java.exchange.constants.ConstantsClassGenerator\")\n"
        + "  public static class MyGroup {\n"
        + "    \n"
        + "    private MyGroup(){}\n"
        + "    \n"
        + "    /**\n"
        + "     * A test Integer constant, for instance '123'\n"
        + "     */\n"
        + "    public static final Integer MY_INT = Integer.valueOf(\"1234\");\n"
        + "    \n"
        + "    /**\n"
        + "     * A test BigDecimal constant\n"
        + "     */\n"
        + "    public static final BigDecimal MY_BIG_DECIMAL = new BigDecimal(\"1.2345\");\n"
        + "    \n"
        + "    /**\n"
        + "     * A group of constants nested in a parent group\n"
        + "     */\n"
        + "    @Generated(\"org.jxapi.generator.java.exchange.constants.ConstantsClassGenerator\")\n"
        + "    public static class MyNestedGroup {\n"
        + "      \n"
        + "      private MyNestedGroup(){}\n"
        + "      \n"
        + "      /**\n"
        + "       * A test Long constant, for instance 1234567890123459L\n"
        + "       */\n"
        + "      public static final Long MY_LONG = Long.valueOf(\"1234567890123456\");\n"
        + "    }\n"
        + "  }\n"
        + "}\n"
        + "", gen.generate());
  }
  
  @Test
  public void testGenerateClassForExchangeWithConflictingConstantNames() throws IOException {
    ExchangeDescriptor exchangeDescriptor = ExchangeDescriptorParser.fromYaml(Paths.get(".", "src", "test", "resources", "exchangeWithEndpointWithConflictingPropertyNames.yaml"));
    PlaceHolderResolver docPlaceHolderResolver = 
        PlaceHolderResolver.create(ExchangeGenUtil.getDescriptionReplacements(exchangeDescriptor, null));
    ConstantsClassGenerator gen = new ConstantsClassGenerator(
        ExchangeGenUtil.getExchangeConstantsClassName(exchangeDescriptor), 
        Constant.fromDescriptors(exchangeDescriptor.getConstants()),
        docPlaceHolderResolver);
    gen.setConstantValuePlaceHolderResolver(s -> ExchangeGenUtil.generateSubstitutionInstructionDeclaration(
        s, 
        exchangeDescriptor, 
        null,
        null,
        gen.getImports()));
    gen.setDescription("Constants used in {@link " + ExchangeGenUtil.getExchangeInterfaceName(exchangeDescriptor) + "} API wrapper");
    Assert.assertEquals("package org.jxapi.exchanges.conflict.gen;\n"
        + "\n"
        + "import javax.annotation.processing.Generated;\n"
        + "\n"
        + "/**\n"
        + " * Constants used in {@link org.jxapi.exchanges.conflict.gen.ConflictExchange} API wrapper\n"
        + " */\n"
        + "@Generated(\"org.jxapi.generator.java.exchange.constants.ConstantsClassGenerator\")\n"
        + "public class ConflictConstants {\n"
        + "  \n"
        + "  private ConflictConstants(){}\n"
        + "  \n"
        + "  /**\n"
        + "   * 'c1' constant\n"
        + "   */\n"
        + "  public static final Integer C1 = Integer.valueOf(\"12340\");\n"
        + "  \n"
        + "  /**\n"
        + "   * 'C1' constant (note the uppercase 'C')\n"
        + "   */\n"
        + "  public static final String C1_ = \"10000\";\n"
        + "  \n"
        + "  /**\n"
        + "   * 'c1_' constant (note the underscore)\n"
        + "   */\n"
        + "  public static final Long C1__ = Long.valueOf(\"1234567890\");\n"
        + "  \n"
        + "  /**\n"
        + "   * 'C1_' constant group (note the uppercase 'C' and underscore)\n"
        + "   */\n"
        + "  @Generated(\"org.jxapi.generator.java.exchange.constants.ConstantsClassGenerator\")\n"
        + "  public static class C1___ {\n"
        + "    \n"
        + "    private C1___(){}\n"
        + "    \n"
        + "    /**\n"
        + "     * Foo constant of C1 group. Value is default websocket URL\n"
        + "     */\n"
        + "    public static final String FOO = \"wss://ws.api.example.com/conflict\";\n"
        + "    \n"
        + "    /**\n"
        + "     * Bar constant of C1 group\n"
        + "     */\n"
        + "    public static final String BAR = \"barVal\";\n"
        + "  }\n"
        + "}\n"
        + "", 
        gen.generate());
  }
}
