package org.jxapi.generator.java.exchange;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.jxapi.exchange.descriptor.Constant;
import org.jxapi.exchange.descriptor.DefaultConfigProperty;
import org.jxapi.exchange.descriptor.ExchangeApiDescriptor;
import org.jxapi.exchange.descriptor.ExchangeDescriptor;
import org.jxapi.exchange.descriptor.Field;
import org.jxapi.exchange.descriptor.Type;
import org.jxapi.generator.java.Imports;
import org.jxapi.generator.java.JavaCodeGenUtil;
import org.jxapi.netutils.deserialization.json.field.BigDecimalJsonFieldDeserializer;
import org.jxapi.netutils.deserialization.json.field.BooleanJsonFieldDeserializer;
import org.jxapi.netutils.deserialization.json.field.IntegerJsonFieldDeserializer;
import org.jxapi.netutils.deserialization.json.field.ListJsonFieldDeserializer;
import org.jxapi.netutils.deserialization.json.field.LongJsonFieldDeserializer;
import org.jxapi.netutils.deserialization.json.field.MapJsonFieldDeserializer;
import org.jxapi.netutils.deserialization.json.field.StringJsonFieldDeserializer;
import org.jxapi.util.EncodingUtil;
import org.jxapi.util.PlaceHolderResolver;
import org.jxapi.util.PropertiesUtil;

/**
 * Unit test for {@link ExchangeGenUtil}
 */
public class ExchangeGenUtilTest {
  
  @Test
  public void testGetApiInterfaceClassName() {
    ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
    exchangeDescriptor.setId("TestExchange");
    exchangeDescriptor.setBasePackage("com.x.y.z");
    ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
    apiDescriptor.setName("Spot");
    Assert.assertEquals("com.x.y.z.spot.TestExchangeSpotApi", 
              ExchangeGenUtil.getApiInterfaceClassName(exchangeDescriptor, apiDescriptor));
  }
  
  @Test
  public void testGetApiInterfaceImplementationClassName() {
    ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
    exchangeDescriptor.setId("TestExchange");
    exchangeDescriptor.setBasePackage("com.x.y.z");
    ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
    apiDescriptor.setName("Spot");
    Assert.assertEquals("com.x.y.z.spot.TestExchangeSpotApiImpl", 
              ExchangeGenUtil.getApiInterfaceImplementationClassName(exchangeDescriptor, apiDescriptor));
  }  
    
  @Test
  public void testGetExchangeInterfaceName() {
    ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
    exchangeDescriptor.setId("TestExchange");
    exchangeDescriptor.setBasePackage("com.x.y.z");
    Assert.assertEquals("com.x.y.z.TestExchangeExchange", 
              ExchangeGenUtil.getExchangeInterfaceName(exchangeDescriptor));
  }

  @Test
  public void testGenerateRateLimitVariableName() {
    Assert.assertEquals("rateLimitTestRateLimit", 
              ExchangeGenUtil.generateRateLimitVariableName("testRateLimit"));
  }

  @Test
  public void testGetJsonMessageDeserializerClassName() {
    Assert.assertEquals("com.x.y.deserializers.MyObjectDeserializer", 
              ExchangeGenUtil.getJsonMessageDeserializerClassName("com.x.y.pojo.MyObject"));
  }

  @Test
  public void testGetClassNameForType_INT() {
    Assert.assertEquals("Integer", ExchangeGenUtil.getClassNameForType(Type.INT, new Imports(), null));
  }

  @Test
  public void testGetClassNameForType_STRING() {
    Assert.assertEquals("String", ExchangeGenUtil.getClassNameForType(Type.STRING, new Imports(), null));
  }

  @Test
  public void testGetClassNameForType_BOOLEAN() {
    Assert.assertEquals("Boolean", ExchangeGenUtil.getClassNameForType(Type.BOOLEAN, new Imports(), null));
  }

  @Test
  public void testGetClassNameForType_BIGDECIMAL() {
    Imports imports = new Imports();
    Assert.assertEquals("BigDecimal", ExchangeGenUtil.getClassNameForType(Type.BIGDECIMAL, imports, null));
    Assert.assertEquals(1, imports.size());
    Assert.assertTrue(imports.contains(BigDecimal.class));
  }

  @Test
  public void testGetClassNameForType_BIGDECIMAL_NullImports() {
    Assert.assertEquals("BigDecimal", ExchangeGenUtil.getClassNameForType(Type.BIGDECIMAL, null, null));
  }

  @Test
  public void testGetClassNameForType_LONG() {
    Assert.assertEquals("Long", ExchangeGenUtil.getClassNameForType(Type.LONG, new Imports(), null));  
  }

  @Test
  public void testGetClassNameForType_STRING_LIST() {
    Imports imports = new Imports();
    Assert.assertEquals("List<String>", ExchangeGenUtil.getClassNameForType(Type.fromTypeName("STRING_LIST"), imports, null));
    Assert.assertEquals(1, imports.size());
    Assert.assertTrue(imports.contains(List.class));
  }

  @Test
  public void testGetClassNameForType_STRING_LIST_NullImports() {
    Assert.assertEquals("List<String>", ExchangeGenUtil.getClassNameForType(Type.fromTypeName("STRING_LIST"), null, null));
  }

  @Test
  public void testGetClassNameForType_INT_MAP() {
    Imports imports = new Imports();
    Assert.assertEquals("Map<String, Integer>", ExchangeGenUtil.getClassNameForType(Type.fromTypeName("INT_MAP"), imports, null));
    Assert.assertEquals(1, imports.size());
    Assert.assertTrue(imports.contains(Map.class));
  }

  @Test
  public void testGetClassNameForType_INT_MAP_NullImports() {
    Assert.assertEquals("Map<String, Integer>", ExchangeGenUtil.getClassNameForType(Type.fromTypeName("INT_MAP"), null, null));
  }

  @Test
  public void testGetClassNameForType_OBJECT() {
    Imports imports = new Imports();
    String objectClassName = "com.x.y.z.MyObject";
    Assert.assertEquals("MyObject", ExchangeGenUtil.getClassNameForType(Type.OBJECT, imports, objectClassName));
    Assert.assertEquals(1, imports.size());
    Assert.assertTrue(imports.contains(objectClassName));
  }
  
  @Test
  public void testGetClassNameForType_OBJECT_NullImports() {
    String objectClassName = "com.x.y.z.MyObject";
    Assert.assertEquals("MyObject", ExchangeGenUtil.getClassNameForType(Type.OBJECT, null, objectClassName));
  }

  @Test
  public void testGetClassNameForType_OBJECT_LIST_MAP() {
    Imports imports = new Imports();
    String objectClassName = "com.x.y.z.MyObject";
    Assert.assertEquals("Map<String, List<MyObject>>", ExchangeGenUtil.getClassNameForType(Type.fromTypeName("OBJECT_LIST_MAP"), imports, objectClassName));
    Assert.assertEquals(3, imports.size());
    Assert.assertTrue(imports.contains(objectClassName));
    Assert.assertTrue(imports.contains(Map.class));
    Assert.assertTrue(imports.contains(List.class));
  }

  @Test
  public void testGetClassNameForType_NullType() {
    Assert.assertNull(ExchangeGenUtil.getClassNameForType(null, null, null));
  }

  @Test
  public void testGetNewJsonFieldDeserializerInstruction_INT() {
    Imports imports = new Imports();
    Assert.assertEquals("IntegerJsonFieldDeserializer.getInstance()", ExchangeGenUtil.getNewJsonFieldDeserializerInstruction(Type.INT, null, imports));
    Assert.assertEquals(1, imports.size());
    Assert.assertTrue(imports.contains(IntegerJsonFieldDeserializer.class));
  }

  @Test
  public void testGetNewJsonFieldDeserializerInstruction_BOOLEAN() {
    Imports imports = new Imports();
    Assert.assertEquals("BooleanJsonFieldDeserializer.getInstance()", ExchangeGenUtil.getNewJsonFieldDeserializerInstruction(Type.BOOLEAN, null, imports));
    Assert.assertEquals(1, imports.size());
    Assert.assertTrue(imports.contains(BooleanJsonFieldDeserializer.class));
  }

  @Test
  public void testGetNewJsonFieldDeserializerInstruction_BIGDECIMAL() {
    Imports imports = new Imports();
    Assert.assertEquals("BigDecimalJsonFieldDeserializer.getInstance()", ExchangeGenUtil.getNewJsonFieldDeserializerInstruction(Type.BIGDECIMAL, null, imports));
    Assert.assertEquals(1, imports.size());
    Assert.assertTrue(imports.contains(BigDecimalJsonFieldDeserializer.class));
  }

  @Test
  public void testGetNewJsonFieldDeserializerInstruction_LONG() {
    Imports imports = new Imports();
    Assert.assertEquals("LongJsonFieldDeserializer.getInstance()", ExchangeGenUtil.getNewJsonFieldDeserializerInstruction(Type.LONG, null, imports));
    Assert.assertEquals(1, imports.size());
    Assert.assertTrue(imports.contains(LongJsonFieldDeserializer.class));
  }

  @Test
  public void testGetNewJsonFieldDeserializerInstruction_STRING() {
    Imports imports = new Imports();
    Assert.assertEquals("StringJsonFieldDeserializer.getInstance()", ExchangeGenUtil.getNewJsonFieldDeserializerInstruction(Type.STRING, null, imports));
    Assert.assertEquals(1, imports.size());
    Assert.assertTrue(imports.contains(StringJsonFieldDeserializer.class));
  }
  
  @Test
  public void testGetNewJsonFieldDeserializerInstruction_STRING_LIST() {
    Imports imports = new Imports();
    Assert.assertEquals("new ListJsonFieldDeserializer<>(StringJsonFieldDeserializer.getInstance())", ExchangeGenUtil.getNewJsonFieldDeserializerInstruction(Type.fromTypeName("STRING_LIST"), null, imports));
    Assert.assertEquals(2, imports.size());
    Assert.assertTrue(imports.contains(ListJsonFieldDeserializer.class));
    Assert.assertTrue(imports.contains(StringJsonFieldDeserializer.class));
  }

  @Test
  public void testGetNewJsonFieldDeserializerInstruction_INT_MAP() {
    Imports imports = new Imports();
    Assert.assertEquals("new MapJsonFieldDeserializer<>(IntegerJsonFieldDeserializer.getInstance())", ExchangeGenUtil.getNewJsonFieldDeserializerInstruction(Type.fromTypeName("INT_MAP"), null, imports));
    Assert.assertEquals(2, imports.size());
    Assert.assertTrue(imports.contains(MapJsonFieldDeserializer.class));
    Assert.assertTrue(imports.contains(IntegerJsonFieldDeserializer.class));
  }

  @Test
  public void testGetNewJsonFieldDeserializerInstruction_OBJECT() {
    Imports imports = new Imports();
    String objectClassName = "com.x.y.z.MyObject";
    Assert.assertEquals("new MyObjectDeserializer()", ExchangeGenUtil.getNewJsonFieldDeserializerInstruction(Type.fromTypeName("OBJECT"), objectClassName, imports));
    Assert.assertEquals(1, imports.size());
    Assert.assertTrue(imports.contains("com.x.y.z.deserializers.MyObjectDeserializer"));
  }

  @Test
  public void testGetNewJsonFieldDeserializerInstruction_NullType() {
    Imports imports = new Imports();
    Assert.assertEquals("StringJsonFieldDeserializer.getInstance()", ExchangeGenUtil.getNewJsonFieldDeserializerInstruction(null, null, imports));
    Assert.assertEquals(1, imports.size());
    Assert.assertTrue(imports.contains(StringJsonFieldDeserializer.class));
  }
  
  @Test 
  public void testGetPrimitiveTypeFieldSampleValueDeclaration_NullSampleValue() {
    Imports imports = new Imports();
    Assert.assertEquals("null", ExchangeGenUtil.getPrimitiveTypeFieldSampleValueDeclaration(Type.INT, null, imports, null));
    Assert.assertEquals(0, imports.size());
  }

  @Test 
  public void testGetPrimitiveTypeFieldSampleValueDeclaration_BigDecimalSampleValue() {
    Imports imports = new Imports();
    Assert.assertEquals("new BigDecimal(\"1.23\")", ExchangeGenUtil.getPrimitiveTypeFieldSampleValueDeclaration(Type.BIGDECIMAL, "1.23", imports, null));
    Assert.assertEquals(1, imports.size());
    Assert.assertTrue(imports.contains(BigDecimal.class));
  }

  @Test
  public void testGetPrimitiveTypeFieldSampleValueDeclaration_LongSampleValue() {
    Imports imports = new Imports();
    Assert.assertEquals("Long.valueOf(\"123\")", ExchangeGenUtil.getPrimitiveTypeFieldSampleValueDeclaration(Type.LONG, "123", imports, null));
    Assert.assertEquals(0, imports.size());
  }

  @Test
  public void testGetPrimitiveTypeFieldSampleValueDeclaration_LongpNowSampleValue() {
    Imports imports = new Imports();
    Assert.assertEquals("Long.valueOf(System.currentTimeMillis())", ExchangeGenUtil.getPrimitiveTypeFieldSampleValueDeclaration(Type.LONG, "now()", imports, null));
    Assert.assertEquals(0, imports.size());
  }

  @Test
  public void testGetPrimitiveTypeFieldSampleValueDeclaration_StringSampleValue() {
    Imports imports = new Imports();
    Assert.assertEquals("\"test\"", ExchangeGenUtil.getPrimitiveTypeFieldSampleValueDeclaration(Type.STRING, "test", imports, null));
    Assert.assertEquals(0, imports.size());
  }

  @Test
  public void testGetPrimitiveTypeFieldSampleValueDeclaration_IntegersSampleValue() {
    Imports imports = new Imports();
    Assert.assertEquals("Integer.valueOf(\"1\")", ExchangeGenUtil.getPrimitiveTypeFieldSampleValueDeclaration(Type.INT, 1, imports, null));
    Assert.assertEquals(0, imports.size());
  }
  
  @Test
  public void testGetPrimitiveTypeFieldSampleValueDeclaration_IntegersSampleValueWithPlaceHolder() {
    Imports imports = new Imports();
    PlaceHolderResolver placeholderResolver = PlaceHolderResolver.create(Map.of("config.myInt1", "1234", "config.myInt2", "5678"));
    Assert.assertEquals("Integer.valueOf(12345678)", ExchangeGenUtil.getPrimitiveTypeFieldSampleValueDeclaration(Type.INT, "${config.myInt1}${config.myInt2}", imports, placeholderResolver));
    Assert.assertEquals(0, imports.size());
  }

  @Test
  public void testGetPrimitiveTypeFieldSampleValueDeclaration_BooleanSampleValue() {
    Imports imports = new Imports();
    Assert.assertEquals("Boolean.valueOf(\"true\")", ExchangeGenUtil.getPrimitiveTypeFieldSampleValueDeclaration(Type.BOOLEAN, "true", imports, null));
    Assert.assertEquals(0, imports.size());
  }

  @Test
  public void testGetPrimitiveTypeFieldSampleValueDeclaration_NonPrimitiveType() {
    Imports imports = new Imports();
    Assert.assertEquals("\"[1, 3, 5]\"", ExchangeGenUtil.getPrimitiveTypeFieldSampleValueDeclaration(Type.fromTypeName("INT_LIST"), "[1, 3, 5]", imports, null));
    Assert.assertEquals(0, imports.size());
  }

  @Test
  public void testGetExchangeConstantsInterfaceName() {
    ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
    exchangeDescriptor.setId("TestExchange");
    exchangeDescriptor.setBasePackage("com.x.y.z");
    Assert.assertEquals("com.x.y.z.TestExchangeConstants", 
              ExchangeGenUtil.getExchangeConstantsInterfaceName(exchangeDescriptor));
  }

  @Test
  public void testGetExchangeApiConstantsInterfaceName() {
    ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
    exchangeDescriptor.setId("TestExchange");
    exchangeDescriptor.setBasePackage("com.x.y.z");
    ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
    apiDescriptor.setName("Spot");
    Assert.assertEquals("com.x.y.z.spot.TestExchangeSpotConstants", 
              ExchangeGenUtil.getExchangeApiConstantsInterfaceName(exchangeDescriptor, apiDescriptor));
  }

  @Test
  public void testGetExchangePropertiesInterfaceName() {
    ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
    exchangeDescriptor.setId("TestExchange");
    exchangeDescriptor.setBasePackage("com.x.y.z");
    Assert.assertEquals("com.x.y.z.TestExchangeProperties", 
              ExchangeGenUtil.getExchangePropertiesInterfaceName(exchangeDescriptor));
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
  public void testIsObjectField_NullField() {
    Assert.assertFalse(ExchangeGenUtil.isObjectField(null));
  }
  
  @Test
  public void testIsObjectField_NullFieldType_ObjectTypeImplictFromProperties() {
    Assert.assertTrue(ExchangeGenUtil.isObjectField(Field.builder().name("test").properties(List.of()).build()));
  }
  
  @Test
  public void testIsObjectField_ObjectType() {
    Assert.assertTrue(ExchangeGenUtil.isObjectField(Field.builder().name("test").type(Type.OBJECT).build()));
  }
  
  @Test
  public void testIsObjectField_StringType() {
    Assert.assertFalse(ExchangeGenUtil.isObjectField(Field.builder().name("test").type(Type.STRING).build()));
  }
  
  @Test
  public void testGetFieldType_NullField() {
    Assert.assertNull(ExchangeGenUtil.getFieldType(null));
  }
  
  @Test
  public void testGetFieldType_ObjecListMapType() {
    Type objectListMapType = Type.fromTypeName("OBJECT_LIST_MAP");
    Assert.assertEquals(objectListMapType, 
        ExchangeGenUtil.getFieldType((Field.builder().name("test").type(objectListMapType).build())));
  }
  
  @Test
  public void testGetFieldType_ObjectType_Explicit() {
    Assert.assertEquals(Type.OBJECT, 
        ExchangeGenUtil.getFieldType(Field.builder().name("test").type(Type.OBJECT).build()));
  }
  
  @Test
  public void testGetFieldType_ObjectType_ImplicitFromObjectName() {
    Assert.assertEquals(Type.OBJECT, 
        ExchangeGenUtil.getFieldType(Field.builder().name("test").objectName("MyObjectName").build()));
  }
  
  @Test
  public void testGetFieldType_ObjectType_ImplicitFromProperties() {
    Assert.assertEquals(Type.OBJECT, 
        ExchangeGenUtil.getFieldType(Field.builder().name("test").properties(List.of()).build()));
  }
  
  @Test
  public void testGetFieldType_String_Implicit() {
    Assert.assertEquals(Type.STRING, 
        ExchangeGenUtil.getFieldType(Field.builder().name("test").build()));
  }
  
  @Test
  public void testGetExchangeInterfaceImplementationNameFromExchangeClassName() {
    Assert.assertEquals("com.x.y.z.TestExchangeImpl",
        ExchangeGenUtil.getExchangeInterfaceImplementationName("com.x.y.z.TestExchange"));
  }
  
  @Test
  public void testGetExchangeInterfaceImplementationNameFromExchangeDescriptor() {
    ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
    exchangeDescriptor.setId("Test");
    exchangeDescriptor.setBasePackage("com.x.y.z");
    Assert.assertEquals("com.x.y.z.TestExchangeImpl",
        ExchangeGenUtil.getExchangeInterfaceImplementationName(exchangeDescriptor));
  }
  
  @Test
  public void testFindPlaceHolders() {
    Assert.assertEquals(0, ExchangeGenUtil.findPlaceHolders(null).size());
    String value = "Hello ${name} you are using exchange ${exchange.name}";
    List<String> placeHolders = ExchangeGenUtil.findPlaceHolders(value);
    Assert.assertEquals(2, placeHolders.size());
    Assert.assertEquals("name", placeHolders.get(0));
    Assert.assertEquals("exchange.name", placeHolders.get(1));
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
  public void testGetClassNameForConstant_NullConstants( ) {
    ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
    exchangeDescriptor.setId("Test");
    exchangeDescriptor.setBasePackage("com.x.y.z");
    ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
    apiDescriptor.setName("Spot");
    Assert.assertNull(ExchangeGenUtil.getClassNameForConstant("foo", exchangeDescriptor, apiDescriptor));
  }
  
  @Test
  public void testGetClassNameForConstant_NotFound( ) {
    ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
    exchangeDescriptor.setId("Test");
    exchangeDescriptor.setBasePackage("com.x.y.z");
    Constant constant = new Constant();
    constant.setName("bar");
    exchangeDescriptor.setConstants(List.of(constant));
    ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
    apiDescriptor.setName("Spot");
    apiDescriptor.setConstants(List.of(constant));
    Assert.assertNull(ExchangeGenUtil.getClassNameForConstant("foo", exchangeDescriptor, apiDescriptor));
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testGetClassNameForConstant_NotFound_NullExchange( ) {
    ExchangeGenUtil.getClassNameForConstant("foo", null, null);
  }
  
  @Test
  public void testGetClassNameForConstant_ConstantFoundInApiGroup( ) {
    ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
    exchangeDescriptor.setId("Test");
    exchangeDescriptor.setBasePackage("com.x.y.z");
    exchangeDescriptor.setConstants(List.of());
    ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
    apiDescriptor.setName("Spot");
    Constant constant = new Constant();
    constant.setName("foo");
    apiDescriptor.setConstants(List.of(constant));
    Assert.assertEquals("com.x.y.z.spot.TestSpotConstants", ExchangeGenUtil.getClassNameForConstant("foo", exchangeDescriptor, apiDescriptor));
  }
  
  @Test
  public void testGetClassNameForConstant_ConstantFoundInExchange( ) {
    ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
    exchangeDescriptor.setId("Test");
    exchangeDescriptor.setBasePackage("com.x.y.z");
    Constant constant = new Constant();
    constant.setName("foo");
    exchangeDescriptor.setConstants(List.of(constant));
    ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
    apiDescriptor.setName("Spot");
    apiDescriptor.setConstants(List.of());
    Assert.assertEquals("com.x.y.z.TestConstants", ExchangeGenUtil.getClassNameForConstant("foo", exchangeDescriptor, apiDescriptor));
  }
  
  @Test
  public void testGetClassNameForConstant_ConstantFoundInExchange_NullApiGroup( ) {
    ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
    exchangeDescriptor.setId("Test");
    exchangeDescriptor.setBasePackage("com.x.y.z");
    Constant constant = new Constant();
    constant.setName("foo");
    exchangeDescriptor.setConstants(List.of(constant));
    Assert.assertEquals("com.x.y.z.TestConstants", ExchangeGenUtil.getClassNameForConstant("foo", exchangeDescriptor, null));
  }
  
  @Test
  public void testGetClassNameForConstant_ConstantFoundInBothExchangeAndApiGroupThenApiGroupConstantClassNameIsReturned( ) {
    ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
    exchangeDescriptor.setId("Test");
    exchangeDescriptor.setBasePackage("com.x.y.z");
    Constant constant = new Constant();
    constant.setName("foo");
    exchangeDescriptor.setConstants(List.of(constant));
    ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
    apiDescriptor.setName("Spot");
    apiDescriptor.setConstants(List.of(constant));
    Assert.assertEquals("com.x.y.z.spot.TestSpotConstants", ExchangeGenUtil.getClassNameForConstant("foo", exchangeDescriptor, apiDescriptor));
  }
  
  @Test
  public void testGetClassNameForConfigProperty_NotFound() {
    ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
    exchangeDescriptor.setId("Test");
    exchangeDescriptor.setBasePackage("com.x.y.z");
    exchangeDescriptor.setProperties(List.of());
    Assert.assertNull(ExchangeGenUtil.getClassNameForConfigProperty("foo", exchangeDescriptor));
  }
  
  @Test
  public void testGetClassNameForConfigProperty() {
    ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
    exchangeDescriptor.setId("Test");
    exchangeDescriptor.setBasePackage("com.x.y.z");
    DefaultConfigProperty configProperty = new DefaultConfigProperty();
    configProperty.setName("myProp");
    exchangeDescriptor.setProperties(List.of(configProperty));
    Assert.assertNull(ExchangeGenUtil.getClassNameForConfigProperty("foo", exchangeDescriptor));
    Assert.assertEquals("com.x.y.z.TestProperties", ExchangeGenUtil.getClassNameForConfigProperty("myProp", exchangeDescriptor));
  }
  
  @Test
  public void testGetValueDeclarationForConstant_ConstantNotFound() {
    ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
    exchangeDescriptor.setId("Test");
    exchangeDescriptor.setBasePackage("com.x.y.z");
    exchangeDescriptor.setConstants(List.of());
    Assert.assertNull(ExchangeGenUtil.getValueDeclarationForConstant("foo", exchangeDescriptor, null, null));
  }
  
  @Test
  public void testGetValueDeclarationForConstant_ExchangeLevelConstant() {
    ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
    exchangeDescriptor.setId("MyExchange");
    exchangeDescriptor.setBasePackage("com.x.y.z");
    
    Constant constant = new Constant();
    constant.setName("foo");
    constant.setValue("bar");
    constant.setType(Type.STRING);
    exchangeDescriptor.setConstants(List.of(constant));
    Imports imports = new Imports();
    Assert.assertEquals("MyExchangeConstants.FOO", ExchangeGenUtil.getValueDeclarationForConstant("foo", exchangeDescriptor, null, imports));
    Assert.assertEquals(1, imports.size());
    Assert.assertTrue(imports.contains("com.x.y.z.MyExchangeConstants"));
  }
  
  @Test
  public void testGetValueDeclarationForConstant_ApiGroupLevelConstant() {
    ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
    exchangeDescriptor.setId("MyExchange");
    exchangeDescriptor.setBasePackage("com.x.y.z");
    
    Constant constant = new Constant();
    constant.setName("foo");
    constant.setValue("bar");
    constant.setType(Type.STRING);
    exchangeDescriptor.setConstants(List.of(constant));
    
    ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
    apiDescriptor.setName("MyApiGroup");
    apiDescriptor.setConstants(List.of(constant));
    
    Imports imports = new Imports();
    Assert.assertEquals("MyExchangeMyApiGroupConstants.FOO", ExchangeGenUtil.getValueDeclarationForConstant("foo", exchangeDescriptor, apiDescriptor, imports));
    Assert.assertEquals(1, imports.size());
    Assert.assertTrue(imports.contains("com.x.y.z.myapigroup.MyExchangeMyApiGroupConstants"));
  }
  
  @Test
  public void testGetValueDeclarationForConfigProperty_PropertyNotFound() {
    ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
    exchangeDescriptor.setId("MyExchange");
    exchangeDescriptor.setBasePackage("com.x.y.z");
    Assert.assertNull(ExchangeGenUtil.getValueDeclarationForConfigProperty("foo", exchangeDescriptor, null, null));
  }
  
  @Test
  public void testGetValueDeclarationForConfigProperty() {
    ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
    exchangeDescriptor.setId("MyExchange");
    exchangeDescriptor.setBasePackage("com.x.y.z");
    DefaultConfigProperty configProperty = new DefaultConfigProperty();
    configProperty.setName("foo");
    exchangeDescriptor.setProperties(List.of(configProperty));
    Imports imports = new Imports();
    Assert.assertEquals("PropertiesUtil.getString(myProps, MyExchangeProperties.FOO)", ExchangeGenUtil.getValueDeclarationForConfigProperty("foo", exchangeDescriptor, "myProps", imports));
    Assert.assertEquals(2, imports.size());
    Assert.assertTrue(imports.contains(PropertiesUtil.class.getName()));
    Assert.assertTrue(imports.contains("com.x.y.z.MyExchangeProperties"));
  }
  
  @Test
  public void testGetDescriptionReplacements_NullExchangeDescriptor() {
    Assert.assertTrue(ExchangeGenUtil.getDescriptionReplacements(null, null).isEmpty());
  }
  
  @Test
  public void testGetDescriptionReplacements_ExchangeContext() {
    ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
    exchangeDescriptor.setId("TestExchange");
    exchangeDescriptor.setBasePackage("com.x.y.z");
    Constant exConstant1 = new Constant();
    exConstant1.setName("exchangeConstant1");
    Constant exConstant2 = new Constant();
    exConstant2.setName("exchangeConstant2");
    exchangeDescriptor.setConstants(List.of(exConstant1, exConstant2));
    
    DefaultConfigProperty exConfigProp1 = new DefaultConfigProperty();
    exConfigProp1.setName("configProp1");
    DefaultConfigProperty exConfigProp2 = new DefaultConfigProperty();
    exConfigProp2.setName("configProp2");
    
    exchangeDescriptor.setProperties(List.of(exConfigProp1, exConfigProp2));
    
    Map<String, Object> replacements = ExchangeGenUtil.getDescriptionReplacements(exchangeDescriptor, null, null);
    Assert.assertEquals(4, replacements.size());
    Assert.assertEquals("{@link com.x.y.z.TestExchangeConstants#EXCHANGE_CONSTANT1}", replacements.get("constants.exchangeConstant1"));
    Assert.assertEquals("{@link com.x.y.z.TestExchangeConstants#EXCHANGE_CONSTANT2}", replacements.get("constants.exchangeConstant2"));
    Assert.assertEquals("{@link com.x.y.z.TestExchangeProperties#CONFIG_PROP1}", replacements.get("config.configProp1"));
    Assert.assertEquals("{@link com.x.y.z.TestExchangeProperties#CONFIG_PROP2}", replacements.get("config.configProp2"));
  }
  
  @Test
  public void testGetDescriptionReplacements_ApiGroupContext() {
    ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
    exchangeDescriptor.setId("TestExchange");
    exchangeDescriptor.setBasePackage("com.x.y.z");
    Constant exConstant1 = new Constant();
    exConstant1.setName("exchangeConstant1");
    exchangeDescriptor.setConstants(List.of(exConstant1));
    
    DefaultConfigProperty exConfigProp1 = new DefaultConfigProperty();
    exConfigProp1.setName("configProp1");
    
    exchangeDescriptor.setProperties(List.of(exConfigProp1));
    
    ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
    apiDescriptor.setName("Spot");
    Constant apiConstant1 = new Constant();
    apiConstant1.setName("apiConstant1");
    Constant apiConstant2 = new Constant();
    apiConstant2.setName("apiConstant2");
    apiDescriptor.setConstants(List.of(apiConstant1, apiConstant2));
    exchangeDescriptor.setApis(List.of(apiDescriptor));
    
    Map<String, Object> replacements = ExchangeGenUtil.getDescriptionReplacements(exchangeDescriptor, "Spot", null);
    Assert.assertEquals(4, replacements.size());
    Assert.assertEquals("{@link com.x.y.z.TestExchangeConstants#EXCHANGE_CONSTANT1}", replacements.get("constants.exchangeConstant1"));
    Assert.assertEquals("{@link com.x.y.z.TestExchangeProperties#CONFIG_PROP1}", replacements.get("config.configProp1"));
    Assert.assertEquals("{@link com.x.y.z.spot.TestExchangeSpotConstants#API_CONSTANT1}", replacements.get("constants.apiConstant1"));
    Assert.assertEquals("{@link com.x.y.z.spot.TestExchangeSpotConstants#API_CONSTANT2}", replacements.get("constants.apiConstant2"));
  }
  
  @Test
  public void testGetDescriptionReplacements_ApiGroupContext_HtmlLinks() {
    ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
    exchangeDescriptor.setId("TestExchange");
    exchangeDescriptor.setBasePackage("com.x.y.z");
    Constant exConstant1 = new Constant();
    exConstant1.setName("exchangeConstant1");
    exchangeDescriptor.setConstants(List.of(exConstant1));
    
    DefaultConfigProperty exConfigProp1 = new DefaultConfigProperty();
    exConfigProp1.setName("configProp1");
    
    exchangeDescriptor.setProperties(List.of(exConfigProp1));
    
    ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
    apiDescriptor.setName("Spot");
    Constant apiConstant1 = new Constant();
    apiConstant1.setName("apiConstant1");
    Constant apiConstant2 = new Constant();
    apiConstant2.setName("apiConstant2");
    apiDescriptor.setConstants(List.of(apiConstant1, apiConstant2));
    exchangeDescriptor.setApis(List.of(apiDescriptor));
    
    Map<String, Object> replacements = ExchangeGenUtil.getDescriptionReplacements(exchangeDescriptor, "Spot", "http://example.com/javadoc/");
    Assert.assertEquals(4, replacements.size());
    Assert.assertEquals("<a href=\"http://example.com/javadoc/com/x/y/z/TestExchangeConstants.html#EXCHANGE_CONSTANT1\">exchangeConstant1</a>", 
                        replacements.get("constants.exchangeConstant1"));
    Assert.assertEquals("<a href=\"http://example.com/javadoc/com/x/y/z/TestExchangeProperties.html#CONFIG_PROP1\">configProp1</a>", 
                        replacements.get("config.configProp1"));
    Assert.assertEquals("<a href=\"http://example.com/javadoc/com/x/y/z/spot/TestExchangeSpotConstants.html#API_CONSTANT1\">apiConstant1</a>", 
                        replacements.get("constants.apiConstant1"));
    Assert.assertEquals("<a href=\"http://example.com/javadoc/com/x/y/z/spot/TestExchangeSpotConstants.html#API_CONSTANT2\">apiConstant2</a>", 
                        replacements.get("constants.apiConstant2"));
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testGetDescriptionReplacements_ApiGroupContext_InvalidApiGroupName() {
    ExchangeGenUtil.getDescriptionReplacements(new ExchangeDescriptor(), "Foo");
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
  public void generateSubstitutionInstructionDeclaration() {
    ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
    exchangeDescriptor.setProperties(List.of(DefaultConfigProperty.create("stranger", Type.STRING, "Your name", "Bob")));
    exchangeDescriptor.setId("MyExchange");
    exchangeDescriptor.setBasePackage("com.x.gen");
    Constant ownName = new Constant();
    ownName.setName("ownName");
    ownName.setType(Type.STRING);
    ownName.setDescription("Narratory name");
    ownName.setValue("John Doe");
    exchangeDescriptor.setConstants(List.of(ownName));
    
    ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
    apiDescriptor.setName("MyApi");
    Constant birthYear = new Constant();
    birthYear.setName("birthYear");
    birthYear.setType(Type.INT);
    birthYear.setDescription("Narratory year of birth");
    birthYear.setValue(1983);
    apiDescriptor.setConstants(List.of(birthYear));
    Imports imports = new Imports();
    Assert.assertEquals(
        "EncodingUtil.substituteArguments(\"Hello ${config.stranger}, I am ${constants.ownName}, born in ${constants.birthYear}\", \"config.stranger\", PropertiesUtil.getString(myProps, MyExchangeProperties.STRANGER), \"constants.ownName\", MyExchangeConstants.OWN_NAME, \"constants.birthYear\", MyExchangeMyApiConstants.BIRTH_YEAR)", 
        ExchangeGenUtil.generateSubstitutionInstructionDeclaration(
            "Hello ${config.stranger}, I am ${constants.ownName}, born in ${constants.birthYear}", 
            exchangeDescriptor, 
            apiDescriptor, 
            "myProps", imports));
     Assert.assertEquals(5, imports.size());
     Iterator<String> it = imports.iterator();
     Assert.assertEquals("com.x.gen.MyExchangeConstants", it.next());
     Assert.assertEquals("com.x.gen.MyExchangeProperties", it.next());
     Assert.assertEquals("com.x.gen.myapi.MyExchangeMyApiConstants", it.next());
     Assert.assertEquals(EncodingUtil.class.getName(), it.next());
     Assert.assertEquals(PropertiesUtil.class.getName(), it.next());
  }
}
