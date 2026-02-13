package org.jxapi.generator.java.exchange;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.jxapi.exchange.descriptor.Constant;
import org.jxapi.exchange.descriptor.gen.ConfigPropertyDescriptor;
import org.jxapi.exchange.descriptor.gen.ConstantDescriptor;
import org.jxapi.exchange.descriptor.gen.ExchangeApiDescriptor;
import org.jxapi.exchange.descriptor.gen.ExchangeDescriptor;
import org.jxapi.exchange.descriptor.gen.HttpClientDescriptor;
import org.jxapi.exchange.descriptor.gen.NetworkDescriptor;
import org.jxapi.exchange.descriptor.gen.WebsocketClientDescriptor;
import org.jxapi.generator.java.Imports;
import org.jxapi.generator.java.JavaCodeGenUtil;
import org.jxapi.generator.java.exchange.api.ExchangeApiGenUtil;
import org.jxapi.generator.java.pojo.PojoGenUtil;
import org.jxapi.pojo.descriptor.Type;
import org.jxapi.util.EncodingUtil;

/**
 * Unit test for {@link ExchangeGenUtil}
 */
public class ExchangeGenUtilTest {
  
  @Test
  public void testGetApiInterfaceClassName() {
    ExchangeDescriptor exchangeDescriptor = new TestExchangeDescriptor();
    ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
    apiDescriptor.setName("Spot");
    exchangeDescriptor.setApis(List.of(apiDescriptor));
    Assert.assertEquals("com.x.gen.spot.MyExchangeSpotApi", 
              ExchangeGenUtil.getApiInterfaceClassName(exchangeDescriptor, apiDescriptor));
  }
  
  @Test
  public void testGetApiInterfaceImplementationClassName() {
    ExchangeDescriptor exchangeDescriptor = new TestExchangeDescriptor();
    ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
    apiDescriptor.setName("Spot");
    exchangeDescriptor.setApis(List.of(apiDescriptor));
    Assert.assertEquals("com.x.gen.spot.MyExchangeSpotApiImpl", 
              ExchangeGenUtil.getApiInterfaceImplementationClassName(exchangeDescriptor, apiDescriptor));
  }  
    
  @Test
  public void testGetExchangeInterfaceName() {
    ExchangeDescriptor exchangeDescriptor = new TestExchangeDescriptor();
    Assert.assertEquals("com.x.gen.MyExchangeExchange", 
              ExchangeGenUtil.getExchangeInterfaceName(exchangeDescriptor));
  }

  @Test
  public void testGenerateRateLimitVariableName() {
    Assert.assertEquals("rateLimitTestRateLimit", 
              ExchangeGenUtil.generateRateLimitVariableName("testRateLimit"));
  }

  @Test
  public void testGetJsonMessageDeserializerClassName() {
    Assert.assertEquals("com.x.y.pojo.deserializers.MyObjectDeserializer", 
              PojoGenUtil.getJsonMessageDeserializerClassName("com.x.y.pojo.MyObject"));
  }

  @Test
  public void testGetExchangeConstantsInterfaceName() {
    ExchangeDescriptor exchangeDescriptor = new TestExchangeDescriptor();
    Assert.assertEquals("com.x.gen.MyExchangeConstants", 
              ExchangeGenUtil.getExchangeConstantsClassName(exchangeDescriptor));
  }

  @Test
  public void testGetExchangePropertiesClassName() {
    ExchangeDescriptor exchangeDescriptor = new TestExchangeDescriptor();
    Assert.assertEquals("com.x.gen.MyExchangeProperties", 
              ExchangeGenUtil.getExchangePropertiesClassName(exchangeDescriptor));
  }
  
  @Test
  public void testGetExchangeDemoPropertiesInterfaceName() {
    ExchangeDescriptor exchangeDescriptor = new TestExchangeDescriptor();
    Assert.assertEquals("com.x.gen.MyExchangeDemoProperties", 
              ExchangeGenUtil.getExchangeDemoPropertiesClassName(exchangeDescriptor));
  }
  
  @Test
  public void testGenerateRateLimitGetterImplementationMethodDeclaration() {
    Assert.assertEquals("@Override\n"
        + "public RateLimitRule getTestRateLimitRateLimit() {\n"
        + "  return this.rateLimitTestRateLimit;\n"
        + "}\n",
        ExchangeGenUtil.generateRateLimitGetterImplementationMethodDeclaration("testRateLimit"));
  }
  
  @Test
  public void testGenerateRateLimitGetterMethodName() {
    Assert.assertEquals("getTestRateLimitRateLimit",
        ExchangeGenUtil.generateRateLimitGetterMethodName("testRateLimit"));
  }
  
  @Test
  public void testGenerateRateLimitRuleInterfaceMethodDeclaration() {
    Assert.assertEquals("/**\n"
        + " * @return 'testRateLimit' rate limit rule.\n"
        + " */\n"
        + "public RateLimitRule getTestRateLimitRateLimit();\n",
        ExchangeGenUtil.generateRateLimitRuleInterfaceMethodDeclaration("testRateLimit"));
  }
  
  @Test
  public void testgenerateRateLimitGetterImplementationMethodDeclaration() {
    Assert.assertEquals("@Override\n"
        + "public RateLimitRule getTestRateLimitRateLimit() {\n"
        + "  return this.rateLimitTestRateLimit;\n"
        + "}\n",
        ExchangeGenUtil.generateRateLimitGetterImplementationMethodDeclaration("testRateLimit"));
  }
  
  @Test
  public void testGetExchangeInterfaceImplementationNameFromExchangeClassName() {
    Assert.assertEquals("com.x.gen.MyExchangeImpl",
        ExchangeGenUtil.getExchangeInterfaceImplementationName("com.x.gen.MyExchange"));
  }
  
  @Test
  public void testGetExchangeInterfaceImplementationNameFromExchangeDescriptor() {
    ExchangeDescriptor exchangeDescriptor = new TestExchangeDescriptor();
    Assert.assertEquals("com.x.gen.MyExchangeExchangeImpl",
        ExchangeGenUtil.getExchangeInterfaceImplementationName(exchangeDescriptor));
  }
  
  @Test
  public void testGetConstantPlaceHolder() {
    Assert.assertNull(ExchangeGenUtil.getConstantPlaceHolder(null));
    Assert.assertNull(ExchangeGenUtil.getConstantPlaceHolder(""));
    Assert.assertNull(ExchangeGenUtil.getConstantPlaceHolder("foo"));
    Assert.assertEquals("foo", ExchangeGenUtil.getConstantPlaceHolder(ExchangeGenUtil.CONSTANT_PLACEHOLDER_PREFIX + "foo"));
  }
  
  @Test
  public void testGetConfigPropertyPlaceHolder() {
    Assert.assertNull(ExchangeGenUtil.getConfigPropertyPlaceHolder(null));
    Assert.assertNull(ExchangeGenUtil.getConfigPropertyPlaceHolder(""));
    Assert.assertNull(ExchangeGenUtil.getConfigPropertyPlaceHolder("foo"));
    Assert.assertEquals("foo", ExchangeGenUtil
        .getConfigPropertyPlaceHolder(ExchangeGenUtil.CONFIG_PLACEHOLDER_PREFIX + "foo"));
  }
  
  @Test
  public void testGetDemoConfigPropertyPlaceHolder() {
    Assert.assertNull(ExchangeGenUtil.getConfigPropertyPlaceHolder(null));
    Assert.assertNull(ExchangeGenUtil.getConfigPropertyPlaceHolder(""));
    Assert.assertNull(ExchangeGenUtil.getConfigPropertyPlaceHolder("foo"));
    Assert.assertEquals("foo", ExchangeGenUtil
        .getDemoConfigPropertyPlaceHolder(ExchangeGenUtil.DEMO_CONFIG_PLACEHOLDER_PREFIX + "foo"));
  }
  
  @Test
  public void testGetClassNameForConstant_NullConstants() {
    ExchangeDescriptor exchangeDescriptor = new TestExchangeDescriptor();
    Assert.assertNull(ExchangeGenUtil.getClassNameForConstant("foo", exchangeDescriptor));
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testGetClassNameForConstant_NullConstantName() {
    ExchangeDescriptor exchangeDescriptor = new TestExchangeDescriptor();
    ExchangeGenUtil.getClassNameForConstant(null, exchangeDescriptor);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testGetClassNameForConstant_NullExchangeDescriptor() {
    ExchangeGenUtil.getClassNameForConstant("foo", null);
  }
  
  @Test
  public void testGetClassNameForConstant_NotFound() {
    ExchangeDescriptor exchangeDescriptor = new TestExchangeDescriptor();
    ConstantDescriptor groupConstant = new ConstantDescriptor();
    groupConstant.setName("myGroup");
    groupConstant.setConstants(List.of(ConstantDescriptor.builder()
        .name("foo")
        .type(Type.STRING.toString())
        .description("Foo constant")
        .value("fooVal").build()));
    exchangeDescriptor.setConstants(List.of(groupConstant));
    Assert.assertNull(ExchangeGenUtil.getClassNameForConstant("bar", exchangeDescriptor));
    Assert.assertNull(ExchangeGenUtil.getClassNameForConstant("myGroup.foo.toto", exchangeDescriptor));
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testGetClassNameForConstant_NotFound_NullExchange() {
    ExchangeGenUtil.getClassNameForConstant("foo", null);
  }
  
  @Test
  public void testGetClassNameForConstant_ConstantFound() {
    ExchangeDescriptor exchangeDescriptor = new TestExchangeDescriptor();
    ConstantDescriptor constant = new ConstantDescriptor();
    constant.setName("foo");
    exchangeDescriptor.setConstants(List.of(constant));
    Assert.assertEquals("com.x.gen.MyExchangeConstants", ExchangeGenUtil.getClassNameForConstant("foo", exchangeDescriptor));
  }
  
  @Test
  public void testGetClassNameForConstant_GroupConstantFound() {
    ExchangeDescriptor exchangeDescriptor = new TestExchangeDescriptor();
    ConstantDescriptor constant = new ConstantDescriptor();
    constant.setName("myGroup");
    constant.setConstants(List.of(ConstantDescriptor.builder()
        .name("foo")
        .type(Type.STRING.toString())
        .description("Foo constant")
        .value("bar").build()));
    exchangeDescriptor.setConstants(List.of(constant));
    Assert.assertEquals("com.x.gen.MyExchangeConstants.MyGroup", ExchangeGenUtil.getClassNameForConstant("myGroup", exchangeDescriptor));
  }
  
  @Test
  public void testGetClassNameForConstant_ConstantFoundInGroup() {
    ExchangeDescriptor exchangeDescriptor = new TestExchangeDescriptor();
    exchangeDescriptor.setId("Test");
    ConstantDescriptor constant = ConstantDescriptor.builder()
        .name("myGroup")
        .addToConstants(ConstantDescriptor.builder()
            .name("foo")
            .type(Type.STRING.toString())
            .description("Foo constant")
            .value("bar").build())
        .build();
    exchangeDescriptor.setConstants(List.of(constant));
    Assert.assertEquals("com.x.gen.TestConstants.MyGroup", ExchangeGenUtil.getClassNameForConstant("myGroup.foo", exchangeDescriptor));
  }
  
  @Test
  public void testGetClassNameForConfigProperty_NotFound() {
    ExchangeDescriptor exchangeDescriptor = new TestExchangeDescriptor();
    exchangeDescriptor.setProperties(List.of());
    Assert.assertNull(ExchangeGenUtil.getClassNameForConfigProperty("foo", exchangeDescriptor));
  }
  
  @Test
  public void testGetClassNameForConfigProperty() {
    ExchangeDescriptor exchangeDescriptor = new TestExchangeDescriptor();
    ConfigPropertyDescriptor configProperty = ConfigPropertyDescriptor.builder()
        .name("myProp")
        .type(Type.STRING.toString())
        .description("A demo property")
        .defaultValue("defaultValue").build();
    exchangeDescriptor.setProperties(List.of(configProperty));
    Assert.assertNull(ExchangeGenUtil.getClassNameForConfigProperty("foo", exchangeDescriptor));
    Assert.assertEquals("com.x.gen.MyExchangeProperties", ExchangeGenUtil.getClassNameForConfigProperty("myProp", exchangeDescriptor));
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testGetClassNameForConfigProperty_NullProp() {
    ExchangeDescriptor exchangeDescriptor = new TestExchangeDescriptor();
    ExchangeGenUtil.getClassNameForConfigProperty(null, exchangeDescriptor);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testGetClassNameForConfigProperty_NullExchange() {
    ExchangeGenUtil.getClassNameForConfigProperty("foo", null);
  }
  
  @Test
  public void testGetClassNameForConfigProperty_GroupProperty() {
    ExchangeDescriptor exchangeDescriptor = new TestExchangeDescriptor();
    
    ConfigPropertyDescriptor configProperty = ConfigPropertyDescriptor.builder()
        .name("myProp")
        .type(Type.STRING.toString())
        .description("A demo property")
        .defaultValue("defaultValue")
        .build();
    
    ConfigPropertyDescriptor configProperty2 = ConfigPropertyDescriptor.builder()
        .name("myProp2")
        .type(Type.STRING.toString())
        .description("A second demo property")
        .defaultValue("defaultValue2")
        .build();
    
    ConfigPropertyDescriptor nestedGroupProp = ConfigPropertyDescriptor.builder()
        .name("myNestedGroup")
        .description("A property group")
        .addToProperties(configProperty)
        .build();
    
    ConfigPropertyDescriptor groupProp = ConfigPropertyDescriptor.builder().name("myGroup")
        .description("A property group")
        .addToProperties(nestedGroupProp)
        .addToProperties(configProperty2)
        .build();
    
    exchangeDescriptor.setProperties(List.of(groupProp));
    Assert.assertNull(ExchangeGenUtil.getClassNameForConfigProperty("foo", exchangeDescriptor));
    Assert.assertEquals("com.x.gen.MyExchangeProperties.MyGroup.MyNestedGroup", ExchangeGenUtil.getClassNameForConfigProperty("myGroup.myNestedGroup.myProp", exchangeDescriptor));
    Assert.assertNull(ExchangeGenUtil.getClassNameForConfigProperty("myGroup.myProp2.myProp", exchangeDescriptor));
  }
  
  @Test
  public void testGetValueDeclarationForConstant_ConstantNotFound() {
    ExchangeDescriptor exchangeDescriptor = new TestExchangeDescriptor();
    exchangeDescriptor.setConstants(List.of());
    Assert.assertNull(ExchangeGenUtil.getValueDeclarationForConstant("foo", exchangeDescriptor, null));
  }
  
  @Test
  public void testGetValueDeclarationForConstant_ExchangeLevelConstant() {
    ExchangeDescriptor exchangeDescriptor = new TestExchangeDescriptor();
    ConstantDescriptor constant = ConstantDescriptor.builder()
        .name("foo")
        .type(Type.STRING.toString())
        .description("Foo constant")
        .value("bar")
        .build();
    exchangeDescriptor.setConstants(List.of(constant));
    Imports imports = new Imports();
    Assert.assertEquals(
        "MyExchangeConstants.FOO", 
        ExchangeGenUtil.getValueDeclarationForConstant("foo", exchangeDescriptor, imports));
    Assert.assertEquals(1, imports.size());
    Assert.assertTrue(imports.contains("com.x.gen.MyExchangeConstants"));
  }
  
  @Test
  public void testGetValueDeclarationForConstant_GroupConstant() {
    ExchangeDescriptor exchangeDescriptor = new TestExchangeDescriptor();
    
    ConstantDescriptor constant = ConstantDescriptor.builder()
        .name("foo")
        .type(Type.STRING.toString())
        .description("Foo constant")
        .value("bar")
        .build();
    
    // Create a constant group
    ConstantDescriptor constantGroup = ConstantDescriptor.builder()
        .name("myGroup")
        .description("A constant group")
        .addToConstants(constant)
        .build();
    
    exchangeDescriptor.setConstants(List.of(constantGroup));
    Imports imports = new Imports();
    Assert.assertEquals("MyExchangeConstants.MyGroup", ExchangeGenUtil.getValueDeclarationForConstant("myGroup", exchangeDescriptor, imports));
    Assert.assertEquals(1, imports.size());
    Assert.assertTrue(imports.contains("com.x.gen.MyExchangeConstants"));
  }
  
  @Test
  public void testGetValueDeclarationForConstant_ConstantNestedInGroup() {
    ExchangeDescriptor exchangeDescriptor = new TestExchangeDescriptor();
    
    ConstantDescriptor constant = ConstantDescriptor.builder()
        .name("foo")
        .type(Type.STRING.toString())
        .description("Foo constant")
        .value("bar")
        .build();
    
    // Create a constant group
    ConstantDescriptor constantGroup = ConstantDescriptor.builder()
        .name("myGroup")
        .description("A constant group")
        .addToConstants(constant)
        .build();
    
    exchangeDescriptor.setConstants(List.of(constantGroup));
    Imports imports = new Imports();
    Assert.assertEquals("MyExchangeConstants.MyGroup.FOO", ExchangeGenUtil.getValueDeclarationForConstant("myGroup.foo", exchangeDescriptor, imports));
    Assert.assertEquals(1, imports.size());
    Assert.assertTrue(imports.contains("com.x.gen.MyExchangeConstants"));
  }
  
  @Test
  public void testGetValueDeclarationForConstant_ConstantNestedInGroupWithConflictingStaticVariableName() {
    ExchangeDescriptor exchangeDescriptor = new TestExchangeDescriptor();
    
    ConstantDescriptor constant = ConstantDescriptor.builder()
        .name("foo")
        .type(Type.STRING.toString())
        .description("Foo constant")
        .value("bar")
        .build();
    
    ConstantDescriptor constant2 = ConstantDescriptor.builder()
        .name("Foo")
        .type(Type.STRING.toString())
        .description("Foo constant with different casing")
        .value("Bar")
        .build();
    
    // Create a constant group
    ConstantDescriptor constantGroup = ConstantDescriptor.builder()
        .name("myGroup")
        .description("A constant group")
        .addToConstants(constant)
        .addToConstants(constant2)
        .build();
    
    ConstantDescriptor constant3 = ConstantDescriptor.builder()
        .name("hello")
        .type(Type.STRING.toString())
        .description("Hello constant")
        .value("world")
        .build();
    
    ConstantDescriptor group2 = ConstantDescriptor.builder()
        .name("MyGroup")
        .description("Another constant group to test case sensitivity")
        .addToConstants(constant3)
        .build();
    
    exchangeDescriptor.setConstants(List.of(constantGroup, group2));
    Imports imports = new Imports();
    Assert.assertEquals("MyExchangeConstants.MyGroup.FOO", ExchangeGenUtil.getValueDeclarationForConstant("myGroup.foo", exchangeDescriptor, imports));
    Assert.assertEquals("MyExchangeConstants.MyGroup.FOO_", ExchangeGenUtil.getValueDeclarationForConstant("myGroup.Foo", exchangeDescriptor, imports));
    Assert.assertEquals("MyExchangeConstants.MyGroup_.HELLO", ExchangeGenUtil.getValueDeclarationForConstant("MyGroup.hello", exchangeDescriptor, imports));
    Assert.assertEquals(1, imports.size());
    Assert.assertTrue(imports.contains("com.x.gen.MyExchangeConstants"));
  }
  
  @Test
  public void testGetValueDeclarationForConfigProperty_PropertyNotFound() {
    ExchangeDescriptor exchangeDescriptor = new TestExchangeDescriptor();
    Assert.assertNull(ExchangeGenUtil.getValueDeclarationForConfigProperty(
      "foo", 
      exchangeDescriptor, 
      null, 
      null, 
      null));
  }
  
  @Test
  public void testGetValueDeclarationForConfigProperty_ExchangeConfigProperty() {
    ConfigPropertyDescriptor configProperty = new ConfigPropertyDescriptor();
    configProperty.setName("foo");
    List<ConfigPropertyDescriptor> allProps = List.of(configProperty);
    doTestGetValueDeclarationForConfigProperty("MyExchangeProperties.getFoo(myProps)", "foo", allProps, false);
  }
  
  @Test
  public void testGetValueDeclarationForConfigProperty_ConflictingPropertyNames() {
    ConfigPropertyDescriptor p1 = ConfigPropertyDescriptor.builder().name("p1").type(Type.STRING.toString()).build();
    ConfigPropertyDescriptor p1Up = ConfigPropertyDescriptor.builder().name("P1").type(Type.STRING.toString()).build();
    ConfigPropertyDescriptor p1Underscore = ConfigPropertyDescriptor.builder().name("p1_").type(Type.STRING.toString()).build();
    ConfigPropertyDescriptor subProp = ConfigPropertyDescriptor.builder().name("sub").type(Type.STRING.toString()).build();
    ConfigPropertyDescriptor p1UpUnderscore = ConfigPropertyDescriptor.builder().name("P1_").addToProperties(subProp).build();
    List<ConfigPropertyDescriptor> allProps = List.of(p1, p1Up, p1Underscore, p1UpUnderscore);
    
    doTestGetValueDeclarationForConfigProperty("MyExchangeProperties.getp1(myProps)", "p1", allProps, false);
    doTestGetValueDeclarationForConfigProperty("MyExchangeProperties.getP1(myProps)", "P1", allProps, false);
    doTestGetValueDeclarationForConfigProperty("MyExchangeProperties.getp1_(myProps)", "p1_", allProps, false);
    doTestGetValueDeclarationForConfigProperty("MyExchangeProperties.getP1_(myProps)", "P1_", allProps, false);
    doTestGetValueDeclarationForConfigProperty("MyExchangeProperties.P1___.getSub(myProps)", "P1_.sub", allProps, false);
  }
  
  @Test
  public void testGetValueDeclarationForConfigProperty_DemoPropertyNestedInGroup() {
    ConfigPropertyDescriptor configProperty = ConfigPropertyDescriptor.builder()
        .name("foo")
        .type(Type.STRING.toString())
        .description("A demo property")
        .defaultValue("defaultValue")
        .build();
    
    ConfigPropertyDescriptor groupProp = ConfigPropertyDescriptor.builder()
        .name("myGroup")
        .description("A property group")
        .addToProperties(configProperty)
        .build();
    
    List<ConfigPropertyDescriptor> demoProperties = List.of(groupProp);
    doTestGetValueDeclarationForConfigProperty("MyExchangeDemoProperties.MyGroup.getFoo(myProps)", "myGroup.foo", demoProperties, true);
  }
  
  private void doTestGetValueDeclarationForConfigProperty(
      String expected, 
      String propName,
      List<ConfigPropertyDescriptor> exchangeProperties, 
      boolean demoProps) {
    ExchangeDescriptor exchangeDescriptor = new TestExchangeDescriptor();
    List<ConfigPropertyDescriptor> demoProperties = null;
    if (demoProps) {
      demoProperties = exchangeProperties;
    }  else {
      exchangeDescriptor.setProperties(exchangeProperties); 
    }
    
    Imports imports = new Imports();
    Assert.assertEquals(expected, ExchangeGenUtil.getValueDeclarationForConfigProperty(propName, exchangeDescriptor,
        demoProperties, "myProps", imports));
    Assert.assertEquals(1, imports.size());
    if (demoProps) {
      Assert.assertTrue(imports.contains("com.x.gen.MyExchangeDemoProperties"));
    } else {
      Assert.assertTrue(imports.contains("com.x.gen.MyExchangeProperties"));
    }
  }
  
  @Test
  public void testGetDescriptionReplacements_NullExchangeDescriptor() {
    Assert.assertTrue(ExchangeGenUtil.getDescriptionReplacements(null, null).isEmpty());
  }
  
  @Test
  public void testGetDescriptionReplacements() {
    ExchangeDescriptor exchangeDescriptor = new TestExchangeDescriptor();
    
    ConstantDescriptor exConstant1 = ConstantDescriptor.builder()
        .name("exchangeConstant1")
        .type(Type.STRING.toString())
        .description("Exchange constant 1")
        .value("exConstant1Value")
        .build();
    
    ConstantDescriptor exConstant2 = ConstantDescriptor.builder()
        .name("exchangeConstant2")
        .type(Type.STRING.toString())
        .description("Exchange constant 2")
        .value("exConstant2Value")
        .build();
    
    ConstantDescriptor nestedConstant1 = ConstantDescriptor.builder()
        .name("nc1")
        .type(Type.STRING.toString())
        .description("Nested constant 1")
        .value("nc1Value")
        .build();
    
    ConstantDescriptor nestedConstant2 = ConstantDescriptor.builder()
        .name("nc2")
        .type(Type.STRING.toString())
        .description("Nested constant 2")
        .value("nc2Value")
        .build();
    
    ConstantDescriptor nestedGroup = ConstantDescriptor.builder()
        .name("nestedGroup2")
        .description("Nested nested group")
        .addToConstants(nestedConstant1)
        .addToConstants(nestedConstant2)
        .build();
    
    ConstantDescriptor group = ConstantDescriptor.builder()
        .name("myGroup")
        .description("A constant group")
        .addToConstants(nestedGroup)
        .build();
    
    exchangeDescriptor.setConstants(List.of(exConstant1, group, exConstant2));
    
    ConfigPropertyDescriptor exConfigProp1 = ConfigPropertyDescriptor.builder()
        .name("configProp1")
        .type(Type.STRING.toString())
        .description("Config property 1")
        .defaultValue("configProp1Value")
        .build();
    
    ConfigPropertyDescriptor exConfigProp2 = ConfigPropertyDescriptor.builder()
        .name("configProp2")
        .type(Type.STRING.toString())
        .description("Config property 2")
        .defaultValue("configProp2Value")
        .build();
    
    ConfigPropertyDescriptor nestedConfigProp1 = ConfigPropertyDescriptor.builder()
        .name("np1")
        .type(Type.STRING.toString())
        .description("Nested config prop 1")
        .defaultValue("np1Value")
        .build();
    
    ConfigPropertyDescriptor nestedConfigProp2 = ConfigPropertyDescriptor.builder()
        .name("np2")
        .type(Type.STRING.toString())
        .description("Nested config prop 2")
        .defaultValue("np2Value")
        .build();
    
    ConfigPropertyDescriptor nestedConfigGroupNestedProp1 = ConfigPropertyDescriptor.builder()
        .name("nnp1")
        .type(Type.STRING.toString())
        .description("Nested config group nested prop")
        .defaultValue("nnp1Value")
        .build();
    
    ConfigPropertyDescriptor nestedGroupConfigProp = ConfigPropertyDescriptor.builder()
        .name("mySubGroup")
        .description("A sub group property")
        .addToProperties(nestedConfigGroupNestedProp1)
        .build();
    
    ConfigPropertyDescriptor grouupConfigProp = ConfigPropertyDescriptor.builder()
        .name("myGroup")
        .description("A property group")
        .addToProperties(nestedConfigProp1)
        .addToProperties(nestedConfigProp2)
        .addToProperties(nestedGroupConfigProp)
        .build();
    
    exchangeDescriptor.setProperties(List.of(exConfigProp1, exConfigProp2, grouupConfigProp));
    
    Map<String, Object> replacements = ExchangeGenUtil.getDescriptionReplacements(exchangeDescriptor);
    Assert.assertEquals(13, replacements.size());
    Assert.assertEquals("{@link com.x.gen.MyExchangeConstants#EXCHANGE_CONSTANT1}", replacements.get("constants.exchangeConstant1"));
    Assert.assertEquals("{@link com.x.gen.MyExchangeConstants#EXCHANGE_CONSTANT2}", replacements.get("constants.exchangeConstant2"));
    Assert.assertEquals("{@link com.x.gen.MyExchangeConstants.MyGroup}", replacements.get("constants.myGroup"));
    Assert.assertEquals("{@link com.x.gen.MyExchangeConstants.MyGroup.NestedGroup2}", replacements.get("constants.myGroup.nestedGroup2"));
    Assert.assertEquals("{@link com.x.gen.MyExchangeConstants.MyGroup.NestedGroup2#NC1}", replacements.get("constants.myGroup.nestedGroup2.nc1"));
    Assert.assertEquals("{@link com.x.gen.MyExchangeConstants.MyGroup.NestedGroup2#NC2}", replacements.get("constants.myGroup.nestedGroup2.nc2"));
    Assert.assertEquals("{@link com.x.gen.MyExchangeProperties#CONFIG_PROP1}", replacements.get("config.configProp1"));
    Assert.assertEquals("{@link com.x.gen.MyExchangeProperties#CONFIG_PROP2}", replacements.get("config.configProp2"));
    Assert.assertEquals("{@link com.x.gen.MyExchangeProperties.MyGroup}", replacements.get("config.myGroup"));
    Assert.assertEquals("{@link com.x.gen.MyExchangeProperties.MyGroup#NP1}", replacements.get("config.myGroup.np1"));
    Assert.assertEquals("{@link com.x.gen.MyExchangeProperties.MyGroup#NP2}", replacements.get("config.myGroup.np2"));
    Assert.assertEquals("{@link com.x.gen.MyExchangeProperties.MyGroup.MySubGroup}", replacements.get("config.myGroup.mySubGroup"));
    Assert.assertEquals("{@link com.x.gen.MyExchangeProperties.MyGroup.MySubGroup#NNP1}", replacements.get("config.myGroup.mySubGroup.nnp1"));
    
    
  }
  
  @Test
  public void testGetDescriptionReplacements_HtmlLinks() {
    ExchangeDescriptor exchangeDescriptor = new TestExchangeDescriptor();
    
    ConstantDescriptor exConstant1 = ConstantDescriptor.builder()
        .name("exchangeConstant1")
        .type(Type.STRING.toString())
        .description("Exchange constant 1")
        .value("exConstant1Value")
        .build();
    
    ConstantDescriptor exConstant2 = ConstantDescriptor.builder()
        .name("exchangeConstant2")
        .type(Type.STRING.toString())
        .description("Exchange constant 2")
        .value("exConstant2Value")
        .build();
    
    ConstantDescriptor nestedConstant1 = ConstantDescriptor.builder()
        .name("nc1")
        .type(Type.STRING.toString())
        .description("Nested constant 1")
        .value("nc1Value")
        .build();
    
    ConstantDescriptor nestedConstant2 = ConstantDescriptor.builder()
        .name("nc2")
        .type(Type.STRING.toString())
        .description("Nested constant 2")
        .value("nc2Value")
        .build();
    
    ConstantDescriptor nestedGroup = ConstantDescriptor.builder()
        .name("nestedGroup2")
        .description("Nested nested group")
        .addToConstants(nestedConstant1)
        .addToConstants(nestedConstant2)
        .build();
    
    ConstantDescriptor group = ConstantDescriptor.builder()
        .name("myGroup")
        .description("A constant group")
        .addToConstants(nestedGroup)
        .build();
    
    exchangeDescriptor.setConstants(List.of(exConstant1, group, exConstant2));
    
    ConfigPropertyDescriptor exConfigProp1 = new ConfigPropertyDescriptor();
    exConfigProp1.setName("configProp1");
    ConfigPropertyDescriptor exConfigProp2 = new ConfigPropertyDescriptor();
    exConfigProp2.setName("configProp2");
    
    exchangeDescriptor.setProperties(List.of(exConfigProp1, exConfigProp2));
    
    Map<String, Object> replacements = ExchangeGenUtil.getDescriptionReplacements(exchangeDescriptor, "http://example.com/javadoc/");
    Assert.assertEquals(8, replacements.size());
    Assert.assertEquals("<a href=\"http://example.com/javadoc/com/x/gen/MyExchangeConstants.html#EXCHANGE_CONSTANT1\">exchangeConstant1</a>", replacements.get("constants.exchangeConstant1"));
    Assert.assertEquals("<a href=\"http://example.com/javadoc/com/x/gen/MyExchangeConstants.html#EXCHANGE_CONSTANT2\">exchangeConstant2</a>", replacements.get("constants.exchangeConstant2"));
    Assert.assertEquals("<a href=\"http://example.com/javadoc/com/x/gen/MyExchangeConstants.MyGroup.html\">myGroup</a>", replacements.get("constants.myGroup"));
    Assert.assertEquals("<a href=\"http://example.com/javadoc/com/x/gen/MyExchangeConstants.MyGroup.NestedGroup2.html\">nestedGroup2</a>", replacements.get("constants.myGroup.nestedGroup2"));
    Assert.assertEquals("<a href=\"http://example.com/javadoc/com/x/gen/MyExchangeConstants.MyGroup.NestedGroup2.html#NC1\">nc1</a>", replacements.get("constants.myGroup.nestedGroup2.nc1"));
    Assert.assertEquals("<a href=\"http://example.com/javadoc/com/x/gen/MyExchangeConstants.MyGroup.NestedGroup2.html#NC2\">nc2</a>", replacements.get("constants.myGroup.nestedGroup2.nc2"));
    Assert.assertEquals("<a href=\"http://example.com/javadoc/com/x/gen/MyExchangeProperties.html#CONFIG_PROP1\">configProp1</a>", replacements.get("config.configProp1"));
    Assert.assertEquals("<a href=\"http://example.com/javadoc/com/x/gen/MyExchangeProperties.html#CONFIG_PROP2\">configProp2</a>", replacements.get("config.configProp2"));
  }
  
  @Test
  public void testGetValuesReplacements() {
    Assert.assertEquals(0, ExchangeGenUtil.getValuesReplacements(null).size());
    ExchangeDescriptor exchangeDescriptor = new TestExchangeDescriptor();
    
    ConstantDescriptor exConstant1 = ConstantDescriptor.builder()
        .name("exchangeConstant1")
        .type(Type.STRING.toString())
        .description("Exchange constant 1")
        .value("exConst1Value")
        .build();
    
    ConstantDescriptor exConstant2 = ConstantDescriptor.builder()
        .name("exchangeConstant2")
        .type(Type.INT.toString())
        .description("Exchange constant 2")
        .value(123)
        .build();
    
    ConstantDescriptor nestedConstant1 = ConstantDescriptor.builder()
        .name("nc1")
        .type(Type.STRING.toString())
        .description("Nested constant 1")
        .value("nc1Value")
        .build();
    
    ConstantDescriptor nestedConstant2 = ConstantDescriptor.builder()
        .name("nc2")
        .type(Type.STRING.toString())
        .description("Nested constant 2")
        .value("nc2Value")
        .build();
    
    ConstantDescriptor nestedGroup = ConstantDescriptor.builder()
        .name("nestedGroup2")
        .description("Nested nested group")
        .addToConstants(nestedConstant1)
        .addToConstants(nestedConstant2)
        .build();
    
    ConstantDescriptor group = ConstantDescriptor.builder()
        .name("myGroup")
        .description("A constant group")
        .addToConstants(nestedGroup)
        .build();
    
    exchangeDescriptor.setConstants(List.of(exConstant1, group, exConstant2));
    
    Map<String, Object> replacements = ExchangeGenUtil.getValuesReplacements(exchangeDescriptor);
    Assert.assertEquals(4, replacements.size());
    Assert.assertEquals("exConst1Value", replacements.get("constants.exchangeConstant1"));
    Assert.assertEquals(123, replacements.get("constants.exchangeConstant2"));
    Assert.assertEquals("nc1Value", replacements.get("constants.myGroup.nestedGroup2.nc1"));
    Assert.assertEquals("nc2Value", replacements.get("constants.myGroup.nestedGroup2.nc2"));
  }
  
  @Test
  public void generateSubstitutionInstructionDeclaration_NoPlaceholder() {
      Assert.assertEquals("\"foo\"", ExchangeGenUtil.generateSubstitutionInstructionDeclaration("foo", null, null, null, null));
  }
  
  @Test
  public void generateSubstitutionInstructionDeclaration_NullTemplate() {
    Assert.assertEquals(JavaCodeGenUtil.NULL, ExchangeGenUtil.generateSubstitutionInstructionDeclaration(null, null, null, null, null));
  }
  
  @Test
  public void generateSubstitutionInstructionDeclaration_NoConfigProperties() {
    ExchangeDescriptor exchangeDescriptor = new TestExchangeDescriptor();
    ConstantDescriptor ownName = ConstantDescriptor.builder()
        .name("ownName")
        .type(Type.STRING.toString())
        .description("Narratory name")
        .value("John Doe")
        .build();
    exchangeDescriptor.setConstants(List.of(ownName));
    Imports imports = new Imports();
    Assert.assertEquals(
        "EncodingUtil.substituteArguments(\"Hello I am ${constants.ownName}, this placeholder is not resolved: ${config.city}, nor this one: ${foo}.\", \"constants.ownName\", MyExchangeConstants.OWN_NAME)", 
        ExchangeGenUtil.generateSubstitutionInstructionDeclaration(
            "Hello I am ${constants.ownName}, this placeholder is not resolved: ${config.city}, nor this one: ${foo}.", 
            exchangeDescriptor, 
            null,
            null,
            imports));
    Assert.assertEquals(2, imports.size());
    Iterator<String> it = imports.iterator();
    Assert.assertEquals("com.x.gen.MyExchangeConstants", it.next());
    Assert.assertEquals(EncodingUtil.class.getName(), it.next());
  }
  
  @Test
  public void generateSubstitutionInstructionDeclaration() {
    ExchangeDescriptor exchangeDescriptor = new TestExchangeDescriptor();
    ConfigPropertyDescriptor strangerProp = ConfigPropertyDescriptor.builder()
        .name("stranger")
        .type(Type.STRING.toString())
        .description("Your name")
        .defaultValue("Bob")
        .build();
    exchangeDescriptor.setProperties(List.of(strangerProp));
    ConfigPropertyDescriptor demoCityProp = ConfigPropertyDescriptor.builder()
        .name("city")
        .type(Type.STRING.toString())
        .description("Your city")
        .defaultValue("London")
        .build();
    
    ConfigPropertyDescriptor demoGroupProp = ConfigPropertyDescriptor.builder()
        .name("address")
        .description("Demo properties group")
        .addToProperties(demoCityProp)
        .build();

    List<ConfigPropertyDescriptor> demoProperties = List.of(demoGroupProp);
    
    ConstantDescriptor ownName = ConstantDescriptor.builder()
        .name("ownName")
        .type(Type.STRING.toString())
        .description("Narratory name")
        .value("John Doe")
        .build();
    
    ConstantDescriptor airports = ConstantDescriptor.builder()
        .name("airports")
        .description("Common airports")
        .addToConstants(ConstantDescriptor.builder()
            .name("london")
            .type(Type.STRING.toString())
            .description("Main airport of London")
            .value("LHR").build())
        .addToConstants(ConstantDescriptor.builder()
            .name("paris")
            .type(Type.STRING.toString())
            .description("Main airport of Paris")
            .value("CDG").build())
        .addToConstants(ConstantDescriptor.builder()
            .name("ny")
            .type(Type.STRING.toString())
            .description("Main airport of New York")
            .value("JFK")
            .build())
        .build();
    
    exchangeDescriptor.setConstants(List.of(ownName, airports));
    
    Constant birthYear = new Constant();
    birthYear.setName("birthYear");
    birthYear.setType(Type.INT);
    birthYear.setDescription("Narratory year of birth");
    birthYear.setValue(1983);
    
    Imports imports = new Imports();
    Assert.assertEquals(
        "EncodingUtil.substituteArguments("
          + "\"Hello ${config.stranger}, I am ${constants.ownName}, " 
          + "born in ${constants.birthYear} in the city of ${demo.config.address.city}. " 
          + "The code of the main airport in London is ${constants.airports.london}." 
          + " These placeholders are not resolved: ${constants.notFoundConstant}, ${config.notFoundProp}, ${foo}.\", " 
          + "\"config.stranger\", MyExchangeProperties.getStranger(myProps), " 
          + "\"constants.ownName\", MyExchangeConstants.OWN_NAME, " 
          + "\"demo.config.address.city\", MyExchangeDemoProperties.Address.getCity(myProps), " 
          + "\"constants.airports.london\", MyExchangeConstants.Airports.LONDON)", 
        ExchangeGenUtil.generateSubstitutionInstructionDeclaration(
         "Hello ${config.stranger}, I am ${constants.ownName}, " 
          + "born in ${constants.birthYear} in the city of ${demo.config.address.city}. " 
          + "The code of the main airport in London is ${constants.airports.london}. " 
          + "These placeholders are not resolved: ${constants.notFoundConstant}, ${config.notFoundProp}, ${foo}.", 
            exchangeDescriptor, 
            demoProperties,
            "myProps", 
            imports));
     Assert.assertEquals(4, imports.size());
     Iterator<String> it = imports.iterator();
     Assert.assertEquals("com.x.gen.MyExchangeConstants", it.next());
     Assert.assertEquals("com.x.gen.MyExchangeDemoProperties", it.next());
     Assert.assertEquals("com.x.gen.MyExchangeProperties", it.next());
     Assert.assertEquals(EncodingUtil.class.getName(), it.next());
  }
  
  @Test
  public void testGetApiGroupGetterMethodNames() {
    ExchangeApiDescriptor api1 = new ExchangeApiDescriptor();
    api1.setName("group1");
    ExchangeApiDescriptor api2 = new ExchangeApiDescriptor();
    api2.setName("a");
    ExchangeApiDescriptor api3 = new ExchangeApiDescriptor();
    api3.setName("A");
    List<ExchangeApiDescriptor> apis = List.of(api1, api2, api3);
    Map<String, String> methodNames = ExchangeGenUtil.getApiGroupGetterMethodNames(apis);
    Assert.assertEquals(3, methodNames.size());
    Assert.assertEquals("getGroup1Api", methodNames.get("group1"));
    Assert.assertEquals("getaApi", methodNames.get("a"));
    Assert.assertEquals("getAApi", methodNames.get("A"));
  }
  
  @Test
  public void testGetApiGroupGetterMethodName() {
    ExchangeApiDescriptor api1 = new ExchangeApiDescriptor();
    api1.setName("group1");
    ExchangeApiDescriptor api2 = new ExchangeApiDescriptor();
    api2.setName("a");
    ExchangeApiDescriptor api3 = new ExchangeApiDescriptor();
    api3.setName("A");
    ExchangeDescriptor exchange = new TestExchangeDescriptor();
    exchange.setApis(List.of(api1, api2, api3));
    Assert.assertEquals("getGroup1Api", ExchangeGenUtil.getApiGroupGetterMethodName(exchange,api1));
    Assert.assertEquals("getaApi", ExchangeGenUtil.getApiGroupGetterMethodName(exchange,api2));
    Assert.assertEquals("getAApi", ExchangeGenUtil.getApiGroupGetterMethodName(exchange,api3));
  }
  
  @Test
  public void testGenerateNamesStaticVariablesDeclarations() {
    List<String> endpointNames = List.of("getAccount", "a", "A");
    String suffix = ExchangeApiGenUtil.REST_ENDPOINT_NAME_SUFFIX;
    StringBuilder classBody = new StringBuilder();
    Map<String, String> variables = ExchangeGenUtil.generateNamesStaticVariablesDeclarations(
        endpointNames,
        suffix, 
        classBody,
        "public static final");
    Assert.assertEquals(3, variables.size());
    Assert.assertEquals("GET_ACCOUNT_REST_API", variables.get("getAccount"));
    Assert.assertEquals("A_REST_API", variables.get("a"));
    Assert.assertEquals("A_REST_API_", variables.get("A"));
    Assert.assertEquals("/**\n"
        + " * Name of <code>getAccount</code> RestApi.\n"
        + " */\n"
        + "public static final String GET_ACCOUNT_REST_API = \"getAccount\";\n"
        + "\n"
        + "/**\n"
        + " * Name of <code>a</code> RestApi.\n"
        + " */\n"
        + "public static final String A_REST_API = \"a\";\n"
        + "\n"
        + "/**\n"
        + " * Name of <code>A</code> RestApi.\n"
        + " */\n"
        + "public static final String A_REST_API_ = \"A\";\n"
        + "", 
        classBody.toString());
  }
  
  @Test
  public void testGenerateNamesStaticVariablesDeclarations_NullClassBody() {
    List<String> endpointNames = List.of("getAccount", "a", "A");
    String suffix = ExchangeApiGenUtil.REST_ENDPOINT_NAME_SUFFIX;
    Map<String, String> variables = ExchangeGenUtil.generateNamesStaticVariablesDeclarations(
        endpointNames,
        suffix, 
        null,
        "public static final");
    Assert.assertEquals(3, variables.size());
    Assert.assertEquals("GET_ACCOUNT_REST_API", variables.get("getAccount"));
    Assert.assertEquals("A_REST_API", variables.get("a"));
    Assert.assertEquals("A_REST_API_", variables.get("A"));
  }
  
  @Test 
  public void testGenerateHttpClientNamesStaticVariablesDeclarations() {
    NetworkDescriptor net = NetworkDescriptor.builder()
        .addToHttpClients(HttpClientDescriptor.builder().name("client1").build())
        .addToHttpClients(HttpClientDescriptor.builder().name("a").build())
        .addToHttpClients(HttpClientDescriptor.builder().name("A").build())
        .build();
    StringBuilder body = new StringBuilder();
    Map<String, String> variables = ExchangeGenUtil.generateHttpClientNamesStaticVariablesDeclarations(net, body);
    Assert.assertEquals(3, variables.size());
    Assert.assertEquals("CLIENT1_HTTP_CLIENT", variables.get("client1"));
    Assert.assertEquals("A_HTTP_CLIENT", variables.get("a"));
    Assert.assertEquals("A_HTTP_CLIENT_", variables.get("A"));
    Assert.assertEquals("/**\n"
        + " * Name of <code>client1</code> HttpClient.\n"
        + " */\n"
        + " String CLIENT1_HTTP_CLIENT = \"client1\";\n"
        + "\n"
        + "/**\n"
        + " * Name of <code>a</code> HttpClient.\n"
        + " */\n"
        + " String A_HTTP_CLIENT = \"a\";\n"
        + "\n"
        + "/**\n"
        + " * Name of <code>A</code> HttpClient.\n"
        + " */\n"
        + " String A_HTTP_CLIENT_ = \"A\";\n"
        + "", body.toString());
  }
  
  @Test 
  public void testGenerateHttpClientNamesStaticVariablesDeclarations_NullNetwork() {
    StringBuilder body = new StringBuilder();
    Map<String, String> variables = ExchangeGenUtil.generateHttpClientNamesStaticVariablesDeclarations(null, body);
    Assert.assertEquals(0, variables.size());
    Assert.assertEquals("", body.toString());
  }
  
  @Test 
  public void testGenerateWebsocketClientNamesStaticVariablesDeclarations() {
    NetworkDescriptor net = NetworkDescriptor.builder()
        .addToWebsocketClients(WebsocketClientDescriptor.builder().name("client1").build())
        .addToWebsocketClients(WebsocketClientDescriptor.builder().name("a").build())
        .addToWebsocketClients(WebsocketClientDescriptor.builder().name("A").build())
        .build();
    StringBuilder body = new StringBuilder();
    Map<String, String> variables = ExchangeGenUtil.generateWebsocketClientNamesStaticVariablesDeclarations(net, body);
    Assert.assertEquals(3, variables.size());
    Assert.assertEquals("CLIENT1_WEBSOCKET_CLIENT", variables.get("client1"));
    Assert.assertEquals("A_WEBSOCKET_CLIENT", variables.get("a"));
    Assert.assertEquals("A_WEBSOCKET_CLIENT_", variables.get("A"));
    Assert.assertEquals("/**\n"
        + " * Name of <code>client1</code> WebsocketClient.\n"
        + " */\n"
        + " String CLIENT1_WEBSOCKET_CLIENT = \"client1\";\n"
        + "\n"
        + "/**\n"
        + " * Name of <code>a</code> WebsocketClient.\n"
        + " */\n"
        + " String A_WEBSOCKET_CLIENT = \"a\";\n"
        + "\n"
        + "/**\n"
        + " * Name of <code>A</code> WebsocketClient.\n"
        + " */\n"
        + " String A_WEBSOCKET_CLIENT_ = \"A\";\n"
        + "", body.toString());
  }
  
  @Test 
  public void testGenerateWebsocketClientNamesStaticVariablesDeclarations_NullNetwork() {
    StringBuilder body = new StringBuilder();
    Map<String, String> variables = ExchangeGenUtil.generateWebsocketClientNamesStaticVariablesDeclarations(null, body);
    Assert.assertEquals(0, variables.size());
    Assert.assertEquals("", body.toString());
  }
  
  private static class TestExchangeDescriptor extends ExchangeDescriptor {
    private static final long serialVersionUID = -5540852152797264769L;

	public TestExchangeDescriptor() {
      setId("MyExchange");
      setBasePackage("com.x.gen");
    }
  }
  
}
