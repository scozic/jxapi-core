package org.jxapi.generator.java.exchange;

import java.math.BigDecimal;
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
import org.jxapi.netutils.deserialization.json.field.BigDecimalJsonFieldDeserializer;
import org.jxapi.netutils.deserialization.json.field.BooleanJsonFieldDeserializer;
import org.jxapi.netutils.deserialization.json.field.IntegerJsonFieldDeserializer;
import org.jxapi.netutils.deserialization.json.field.ListJsonFieldDeserializer;
import org.jxapi.netutils.deserialization.json.field.LongJsonFieldDeserializer;
import org.jxapi.netutils.deserialization.json.field.MapJsonFieldDeserializer;
import org.jxapi.netutils.deserialization.json.field.StringJsonFieldDeserializer;
import org.jxapi.util.PropertiesUtil;

/**
 * Unit test for {@link ExchangeJavaGenUtil}
 */
public class ExchangeJavaGenUtilTest {
  
  @Test
  public void testGetApiInterfaceClassName() {
    ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
    exchangeDescriptor.setId("TestExchange");
    exchangeDescriptor.setBasePackage("com.x.y.z");
    ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
    apiDescriptor.setName("Spot");
    Assert.assertEquals("com.x.y.z.spot.TestExchangeSpotApi", 
              ExchangeJavaGenUtil.getApiInterfaceClassName(exchangeDescriptor, apiDescriptor));
  }
  
  @Test
  public void testGetApiInterfaceImplementationClassName() {
    ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
    exchangeDescriptor.setId("TestExchange");
    exchangeDescriptor.setBasePackage("com.x.y.z");
    ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
    apiDescriptor.setName("Spot");
    Assert.assertEquals("com.x.y.z.spot.TestExchangeSpotApiImpl", 
              ExchangeJavaGenUtil.getApiInterfaceImplementationClassName(exchangeDescriptor, apiDescriptor));
  }  
    
  @Test
  public void testGetExchangeInterfaceName() {
    ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
    exchangeDescriptor.setId("TestExchange");
    exchangeDescriptor.setBasePackage("com.x.y.z");
    Assert.assertEquals("com.x.y.z.TestExchangeExchange", 
              ExchangeJavaGenUtil.getExchangeInterfaceName(exchangeDescriptor));
  }

  @Test
  public void testGenerateRateLimitVariableName() {
    Assert.assertEquals("rateLimitTestRateLimit", 
              ExchangeJavaGenUtil.generateRateLimitVariableName("testRateLimit"));
  }

  @Test
  public void testGetJsonMessageDeserializerClassName() {
    Assert.assertEquals("com.x.y.deserializers.MyObjectDeserializer", 
              ExchangeJavaGenUtil.getJsonMessageDeserializerClassName("com.x.y.pojo.MyObject"));
  }

  @Test
  public void testGetClassNameForType_INT() {
    Assert.assertEquals("Integer", ExchangeJavaGenUtil.getClassNameForType(Type.INT, new Imports(), null));
  }

  @Test
  public void testGetClassNameForType_STRING() {
    Assert.assertEquals("String", ExchangeJavaGenUtil.getClassNameForType(Type.STRING, new Imports(), null));
  }

  @Test
  public void testGetClassNameForType_BOOLEAN() {
    Assert.assertEquals("Boolean", ExchangeJavaGenUtil.getClassNameForType(Type.BOOLEAN, new Imports(), null));
  }

  @Test
  public void testGetClassNameForType_BIGDECIMAL() {
    Imports imports = new Imports();
    Assert.assertEquals("BigDecimal", ExchangeJavaGenUtil.getClassNameForType(Type.BIGDECIMAL, imports, null));
    Assert.assertEquals(1, imports.size());
    Assert.assertTrue(imports.contains(BigDecimal.class));
  }

  @Test
  public void testGetClassNameForType_BIGDECIMAL_NullImports() {
    Assert.assertEquals("BigDecimal", ExchangeJavaGenUtil.getClassNameForType(Type.BIGDECIMAL, null, null));
  }

  @Test
  public void testGetClassNameForType_LONG() {
    Assert.assertEquals("Long", ExchangeJavaGenUtil.getClassNameForType(Type.LONG, new Imports(), null));  
  }

  @Test
  public void testGetClassNameForType_STRING_LIST() {
    Imports imports = new Imports();
    Assert.assertEquals("List<String>", ExchangeJavaGenUtil.getClassNameForType(Type.fromTypeName("STRING_LIST"), imports, null));
    Assert.assertEquals(1, imports.size());
    Assert.assertTrue(imports.contains(List.class));
  }

  @Test
  public void testGetClassNameForType_STRING_LIST_NullImports() {
    Assert.assertEquals("List<String>", ExchangeJavaGenUtil.getClassNameForType(Type.fromTypeName("STRING_LIST"), null, null));
  }

  @Test
  public void testGetClassNameForType_INT_MAP() {
    Imports imports = new Imports();
    Assert.assertEquals("Map<String, Integer>", ExchangeJavaGenUtil.getClassNameForType(Type.fromTypeName("INT_MAP"), imports, null));
    Assert.assertEquals(1, imports.size());
    Assert.assertTrue(imports.contains(Map.class));
  }

  @Test
  public void testGetClassNameForType_INT_MAP_NullImports() {
    Assert.assertEquals("Map<String, Integer>", ExchangeJavaGenUtil.getClassNameForType(Type.fromTypeName("INT_MAP"), null, null));
  }

  @Test
  public void testGetClassNameForType_OBJECT() {
    Imports imports = new Imports();
    String objectClassName = "com.x.y.z.MyObject";
    Assert.assertEquals("MyObject", ExchangeJavaGenUtil.getClassNameForType(Type.OBJECT, imports, objectClassName));
    Assert.assertEquals(1, imports.size());
    Assert.assertTrue(imports.contains(objectClassName));
  }
  
  @Test
  public void testGetClassNameForType_OBJECT_NullImports() {
    String objectClassName = "com.x.y.z.MyObject";
    Assert.assertEquals("MyObject", ExchangeJavaGenUtil.getClassNameForType(Type.OBJECT, null, objectClassName));
  }

  @Test
  public void testGetClassNameForType_OBJECT_LIST_MAP() {
    Imports imports = new Imports();
    String objectClassName = "com.x.y.z.MyObject";
    Assert.assertEquals("Map<String, List<MyObject>>", ExchangeJavaGenUtil.getClassNameForType(Type.fromTypeName("OBJECT_LIST_MAP"), imports, objectClassName));
    Assert.assertEquals(3, imports.size());
    Assert.assertTrue(imports.contains(objectClassName));
    Assert.assertTrue(imports.contains(Map.class));
    Assert.assertTrue(imports.contains(List.class));
  }

  @Test
  public void testGetClassNameForType_NullType() {
    Assert.assertNull(ExchangeJavaGenUtil.getClassNameForType(null, null, null));
  }

  @Test
  public void testGetNewJsonFieldDeserializerInstruction_INT() {
    Imports imports = new Imports();
    Assert.assertEquals("IntegerJsonFieldDeserializer.getInstance()", ExchangeJavaGenUtil.getNewJsonFieldDeserializerInstruction(Type.INT, null, imports));
    Assert.assertEquals(1, imports.size());
    Assert.assertTrue(imports.contains(IntegerJsonFieldDeserializer.class));
  }

  @Test
  public void testGetNewJsonFieldDeserializerInstruction_BOOLEAN() {
    Imports imports = new Imports();
    Assert.assertEquals("BooleanJsonFieldDeserializer.getInstance()", ExchangeJavaGenUtil.getNewJsonFieldDeserializerInstruction(Type.BOOLEAN, null, imports));
    Assert.assertEquals(1, imports.size());
    Assert.assertTrue(imports.contains(BooleanJsonFieldDeserializer.class));
  }

  @Test
  public void testGetNewJsonFieldDeserializerInstruction_BIGDECIMAL() {
    Imports imports = new Imports();
    Assert.assertEquals("BigDecimalJsonFieldDeserializer.getInstance()", ExchangeJavaGenUtil.getNewJsonFieldDeserializerInstruction(Type.BIGDECIMAL, null, imports));
    Assert.assertEquals(1, imports.size());
    Assert.assertTrue(imports.contains(BigDecimalJsonFieldDeserializer.class));
  }

  @Test
  public void testGetNewJsonFieldDeserializerInstruction_LONG() {
    Imports imports = new Imports();
    Assert.assertEquals("LongJsonFieldDeserializer.getInstance()", ExchangeJavaGenUtil.getNewJsonFieldDeserializerInstruction(Type.LONG, null, imports));
    Assert.assertEquals(1, imports.size());
    Assert.assertTrue(imports.contains(LongJsonFieldDeserializer.class));
  }

  @Test
  public void testGetNewJsonFieldDeserializerInstruction_STRING() {
    Imports imports = new Imports();
    Assert.assertEquals("StringJsonFieldDeserializer.getInstance()", ExchangeJavaGenUtil.getNewJsonFieldDeserializerInstruction(Type.STRING, null, imports));
    Assert.assertEquals(1, imports.size());
    Assert.assertTrue(imports.contains(StringJsonFieldDeserializer.class));
  }
  
  @Test
  public void testGetNewJsonFieldDeserializerInstruction_STRING_LIST() {
    Imports imports = new Imports();
    Assert.assertEquals("new ListJsonFieldDeserializer<>(StringJsonFieldDeserializer.getInstance())", ExchangeJavaGenUtil.getNewJsonFieldDeserializerInstruction(Type.fromTypeName("STRING_LIST"), null, imports));
    Assert.assertEquals(2, imports.size());
    Assert.assertTrue(imports.contains(ListJsonFieldDeserializer.class));
    Assert.assertTrue(imports.contains(StringJsonFieldDeserializer.class));
  }

  @Test
  public void testGetNewJsonFieldDeserializerInstruction_INT_MAP() {
    Imports imports = new Imports();
    Assert.assertEquals("new MapJsonFieldDeserializer<>(IntegerJsonFieldDeserializer.getInstance())", ExchangeJavaGenUtil.getNewJsonFieldDeserializerInstruction(Type.fromTypeName("INT_MAP"), null, imports));
    Assert.assertEquals(2, imports.size());
    Assert.assertTrue(imports.contains(MapJsonFieldDeserializer.class));
    Assert.assertTrue(imports.contains(IntegerJsonFieldDeserializer.class));
  }

  @Test
  public void testGetNewJsonFieldDeserializerInstruction_OBJECT() {
    Imports imports = new Imports();
    String objectClassName = "com.x.y.z.MyObject";
    Assert.assertEquals("new MyObjectDeserializer()", ExchangeJavaGenUtil.getNewJsonFieldDeserializerInstruction(Type.fromTypeName("OBJECT"), objectClassName, imports));
    Assert.assertEquals(1, imports.size());
    Assert.assertTrue(imports.contains("com.x.y.z.deserializers.MyObjectDeserializer"));
  }

  @Test
  public void testGetNewJsonFieldDeserializerInstruction_NullType() {
    Imports imports = new Imports();
    Assert.assertEquals("StringJsonFieldDeserializer.getInstance()", ExchangeJavaGenUtil.getNewJsonFieldDeserializerInstruction(null, null, imports));
    Assert.assertEquals(1, imports.size());
    Assert.assertTrue(imports.contains(StringJsonFieldDeserializer.class));
  }
  
  @Test 
  public void testGetPrimitiveTypeFieldSampleValueDeclaration_NullSampleValue() {
    Imports imports = new Imports();
    Assert.assertEquals(null, ExchangeJavaGenUtil.getPrimitiveTypeFieldSampleValueDeclaration(Type.INT, null, imports));
    Assert.assertEquals(0, imports.size());
  }

  @Test 
  public void testGetPrimitiveTypeFieldSampleValueDeclaration_BigDecimalSampleValue() {
    Imports imports = new Imports();
    Assert.assertEquals("new BigDecimal(\"1.23\")", ExchangeJavaGenUtil.getPrimitiveTypeFieldSampleValueDeclaration(Type.BIGDECIMAL, "1.23", imports));
    Assert.assertEquals(1, imports.size());
    Assert.assertTrue(imports.contains(BigDecimal.class));
  }

  @Test
  public void testGetPrimitiveTypeFieldSampleValueDeclaration_LongSampleValue() {
    Imports imports = new Imports();
    Assert.assertEquals("Long.valueOf(\"123\")", ExchangeJavaGenUtil.getPrimitiveTypeFieldSampleValueDeclaration(Type.LONG, "123", imports));
    Assert.assertEquals(0, imports.size());
  }

  @Test
  public void testGetPrimitiveTypeFieldSampleValueDeclaration_LongpNowSampleValue() {
    Imports imports = new Imports();
    Assert.assertEquals("Long.valueOf(System.currentTimeMillis())", ExchangeJavaGenUtil.getPrimitiveTypeFieldSampleValueDeclaration(Type.LONG, "now()", imports));
    Assert.assertEquals(0, imports.size());
  }

  @Test
  public void testGetPrimitiveTypeFieldSampleValueDeclaration_StringSampleValue() {
    Imports imports = new Imports();
    Assert.assertEquals("\"test\"", ExchangeJavaGenUtil.getPrimitiveTypeFieldSampleValueDeclaration(Type.STRING, "test", imports));
    Assert.assertEquals(0, imports.size());
  }

  @Test
  public void testGetPrimitiveTypeFieldSampleValueDeclaration_IntegersSampleValue() {
    Imports imports = new Imports();
    Assert.assertEquals("Integer.valueOf(1)", ExchangeJavaGenUtil.getPrimitiveTypeFieldSampleValueDeclaration(Type.INT, "1", imports));
    Assert.assertEquals(0, imports.size());
  }

  @Test
  public void testGetPrimitiveTypeFieldSampleValueDeclaration_BooleanSampleValue() {
    Imports imports = new Imports();
    Assert.assertEquals("Boolean.valueOf(true)", ExchangeJavaGenUtil.getPrimitiveTypeFieldSampleValueDeclaration(Type.BOOLEAN, "true", imports));
    Assert.assertEquals(0, imports.size());
  }

  @Test
  public void testGetPrimitiveTypeFieldSampleValueDeclaration_NonPrimitiveType() {
    Imports imports = new Imports();
    Assert.assertEquals("\"[1, 3, 5]\"", ExchangeJavaGenUtil.getPrimitiveTypeFieldSampleValueDeclaration(Type.fromTypeName("INT_LIST"), "[1, 3, 5]", imports));
    Assert.assertEquals(0, imports.size());
  }

  @Test
  public void testGetExchangeConstantsInterfaceName() {
    ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
    exchangeDescriptor.setId("TestExchange");
    exchangeDescriptor.setBasePackage("com.x.y.z");
    Assert.assertEquals("com.x.y.z.TestExchangeConstants", 
              ExchangeJavaGenUtil.getExchangeConstantsInterfaceName(exchangeDescriptor));
  }

  @Test
  public void testGetExchangeApiConstantsInterfaceName() {
    ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
    exchangeDescriptor.setId("TestExchange");
    exchangeDescriptor.setBasePackage("com.x.y.z");
    ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
    apiDescriptor.setName("Spot");
    Assert.assertEquals("com.x.y.z.spot.TestExchangeSpotConstants", 
              ExchangeJavaGenUtil.getExchangeApiConstantsInterfaceName(exchangeDescriptor, apiDescriptor));
  }

  @Test
  public void testGetExchangePropertiesInterfaceName() {
    ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
    exchangeDescriptor.setId("TestExchange");
    exchangeDescriptor.setBasePackage("com.x.y.z");
    Assert.assertEquals("com.x.y.z.TestExchangeProperties", 
              ExchangeJavaGenUtil.getExchangePropertiesInterfaceName(exchangeDescriptor));
  }
  
  @Test
  public void testGenerateRateLimitGetterImplementationMethodDeclaration() {
    Assert.assertEquals("@Override\n"
        + "public RateLimitRule getTestRateLimitRateLimit() {\n"
        + "  return this.rateLimitTestRateLimit;\n"
        + "}\n",
        ExchangeJavaGenUtil.generateRateLimitGetterImplementationMethodDeclaration("testRateLimit"));
  }
  
  @Test
  public void testGenerateRateLimitGetterMethodName() {
    Assert.assertEquals("getTestRateLimitRateLimit",
        ExchangeJavaGenUtil.generateRateLimitGetterMethodName("testRateLimit"));
  }
  
  @Test
  public void testGenerateRateLimitRuleInterfaceMethodDeclaration() {
    Assert.assertEquals("/**\n"
        + " * @return 'testRateLimit' rate limit rule.\n"
        + " */\n"
        + "public RateLimitRule getTestRateLimitRateLimit();\n",
        ExchangeJavaGenUtil.generateRateLimitRuleInterfaceMethodDeclaration("testRateLimit"));
  }
  
  @Test
  public void testgenerateRateLimitGetterImplementationMethodDeclaration() {
    Assert.assertEquals("@Override\n"
        + "public RateLimitRule getTestRateLimitRateLimit() {\n"
        + "  return this.rateLimitTestRateLimit;\n"
        + "}\n",
        ExchangeJavaGenUtil.generateRateLimitGetterImplementationMethodDeclaration("testRateLimit"));
  }
  
  @Test
  public void testIsObjectField_NullField() {
    Assert.assertFalse(ExchangeJavaGenUtil.isObjectField(null));
  }
  
  @Test
  public void testIsObjectField_NullFieldType_ObjectTypeImplictFromProperties() {
    Assert.assertTrue(ExchangeJavaGenUtil.isObjectField(Field.builder().name("test").properties(List.of()).build()));
  }
  
  @Test
  public void testIsObjectField_ObjectType() {
    Assert.assertTrue(ExchangeJavaGenUtil.isObjectField(Field.builder().name("test").type(Type.OBJECT).build()));
  }
  
  @Test
  public void testIsObjectField_StringType() {
    Assert.assertFalse(ExchangeJavaGenUtil.isObjectField(Field.builder().name("test").type(Type.STRING).build()));
  }
  
  @Test
  public void testGetFieldType_NullField() {
    Assert.assertNull(ExchangeJavaGenUtil.getFieldType(null));
  }
  
  @Test
  public void testGetFieldType_ObjecListMapType() {
    Type objectListMapType = Type.fromTypeName("OBJECT_LIST_MAP");
    Assert.assertEquals(objectListMapType, 
        ExchangeJavaGenUtil.getFieldType((Field.builder().name("test").type(objectListMapType).build())));
  }
  
  @Test
  public void testGetFieldType_ObjectType_Explicit() {
    Assert.assertEquals(Type.OBJECT, 
        ExchangeJavaGenUtil.getFieldType(Field.builder().name("test").type(Type.OBJECT).build()));
  }
  
  @Test
  public void testGetFieldType_ObjectType_ImplicitFromObjectName() {
    Assert.assertEquals(Type.OBJECT, 
        ExchangeJavaGenUtil.getFieldType(Field.builder().name("test").objectName("MyObjectName").build()));
  }
  
  @Test
  public void testGetFieldType_ObjectType_ImplicitFromProperties() {
    Assert.assertEquals(Type.OBJECT, 
        ExchangeJavaGenUtil.getFieldType(Field.builder().name("test").properties(List.of()).build()));
  }
  
  @Test
  public void testGetFieldType_String_Implicit() {
    Assert.assertEquals(Type.STRING, 
        ExchangeJavaGenUtil.getFieldType(Field.builder().name("test").build()));
  }
  
  @Test
  public void testGetExchangeInterfaceImplementationNameFromExchangeClassName() {
    Assert.assertEquals("com.x.y.z.TestExchangeImpl",
        ExchangeJavaGenUtil.getExchangeInterfaceImplementationName("com.x.y.z.TestExchange"));
  }
  
  @Test
  public void testGetExchangeInterfaceImplementationNameFromExchangeDescriptor() {
    ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
    exchangeDescriptor.setId("Test");
    exchangeDescriptor.setBasePackage("com.x.y.z");
    Assert.assertEquals("com.x.y.z.TestExchangeImpl",
        ExchangeJavaGenUtil.getExchangeInterfaceImplementationName(exchangeDescriptor));
  }
  
  @Test
  public void testFindPlaceHolders() {
    Assert.assertEquals(0, ExchangeJavaGenUtil.findPlaceHolders(null).size());
    String value = "Hello ${name} you are using exchange ${exchange.name}";
    List<String> placeHolders = ExchangeJavaGenUtil.findPlaceHolders(value);
    Assert.assertEquals(2, placeHolders.size());
    Assert.assertEquals("name", placeHolders.get(0));
    Assert.assertEquals("exchange.name", placeHolders.get(1));
  }
  
  @Test
  public void testGetConstantPlaceHolder() {
    Assert.assertNull(ExchangeJavaGenUtil.getConstantPlaceHolder(null));
    Assert.assertNull(ExchangeJavaGenUtil.getConstantPlaceHolder(""));
    Assert.assertNull(ExchangeJavaGenUtil.getConstantPlaceHolder("foo"));
    Assert.assertEquals("foo", ExchangeJavaGenUtil.getConstantPlaceHolder(ExchangeJavaGenUtil.CONSTANT_PLACEHOLDER_PREFIX + "foo"));
  }
  
  @Test
  public void testGetConfigPropertyPlaceHolder() {
    Assert.assertNull(ExchangeJavaGenUtil.getConfigPropertyPlaceHolder(null));
    Assert.assertNull(ExchangeJavaGenUtil.getConfigPropertyPlaceHolder(""));
    Assert.assertNull(ExchangeJavaGenUtil.getConfigPropertyPlaceHolder("foo"));
    Assert.assertEquals("foo", ExchangeJavaGenUtil
        .getConfigPropertyPlaceHolder(ExchangeJavaGenUtil.CONFIG_PLACEHOLDER_PREFIX + "foo"));
  }
  
  @Test
  public void testGetClassNameForConstant_NullConstants( ) {
    ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
    exchangeDescriptor.setId("Test");
    exchangeDescriptor.setBasePackage("com.x.y.z");
    ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
    apiDescriptor.setName("Spot");
    Assert.assertNull(ExchangeJavaGenUtil.getClassNameForConstant("foo", exchangeDescriptor, apiDescriptor));
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
    Assert.assertNull(ExchangeJavaGenUtil.getClassNameForConstant("foo", exchangeDescriptor, apiDescriptor));
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testGetClassNameForConstant_NotFound_NullExchange( ) {
    ExchangeJavaGenUtil.getClassNameForConstant("foo", null, null);
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
    Assert.assertEquals("com.x.y.z.spot.TestSpotConstants", ExchangeJavaGenUtil.getClassNameForConstant("foo", exchangeDescriptor, apiDescriptor));
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
    Assert.assertEquals("com.x.y.z.TestConstants", ExchangeJavaGenUtil.getClassNameForConstant("foo", exchangeDescriptor, apiDescriptor));
  }
  
  @Test
  public void testGetClassNameForConstant_ConstantFoundInExchange_NullApiGroup( ) {
    ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
    exchangeDescriptor.setId("Test");
    exchangeDescriptor.setBasePackage("com.x.y.z");
    Constant constant = new Constant();
    constant.setName("foo");
    exchangeDescriptor.setConstants(List.of(constant));
    Assert.assertEquals("com.x.y.z.TestConstants", ExchangeJavaGenUtil.getClassNameForConstant("foo", exchangeDescriptor, null));
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
    Assert.assertEquals("com.x.y.z.spot.TestSpotConstants", ExchangeJavaGenUtil.getClassNameForConstant("foo", exchangeDescriptor, apiDescriptor));
  }
  
  @Test
  public void testGetClassNameForConfigProperty_NotFound() {
    ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
    exchangeDescriptor.setId("Test");
    exchangeDescriptor.setBasePackage("com.x.y.z");
    exchangeDescriptor.setProperties(List.of());
    Assert.assertNull(ExchangeJavaGenUtil.getClassNameForConfigProperty("foo", exchangeDescriptor));
  }
  
  @Test
  public void testGetClassNameForConfigProperty() {
    ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
    exchangeDescriptor.setId("Test");
    exchangeDescriptor.setBasePackage("com.x.y.z");
    DefaultConfigProperty configProperty = new DefaultConfigProperty();
    configProperty.setName("myProp");
    exchangeDescriptor.setProperties(List.of(configProperty));
    Assert.assertNull(ExchangeJavaGenUtil.getClassNameForConfigProperty("foo", exchangeDescriptor));
    Assert.assertEquals("com.x.y.z.TestProperties", ExchangeJavaGenUtil.getClassNameForConfigProperty("myProp", exchangeDescriptor));
  }
  
  @Test
  public void testGetValueDeclarationForConstant_ConstantNotFound() {
    ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
    exchangeDescriptor.setId("Test");
    exchangeDescriptor.setBasePackage("com.x.y.z");
    exchangeDescriptor.setConstants(List.of());
    Assert.assertNull(ExchangeJavaGenUtil.getValueDeclarationForConstant("foo", exchangeDescriptor, null, null));
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
    Assert.assertEquals("MyExchangeConstants.FOO", ExchangeJavaGenUtil.getValueDeclarationForConstant("foo", exchangeDescriptor, null, imports));
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
    Assert.assertEquals("MyExchangeMyApiGroupConstants.FOO", ExchangeJavaGenUtil.getValueDeclarationForConstant("foo", exchangeDescriptor, apiDescriptor, imports));
    Assert.assertEquals(1, imports.size());
    Assert.assertTrue(imports.contains("com.x.y.z.myapigroup.MyExchangeMyApiGroupConstants"));
  }
  
  @Test
  public void testGetValueDeclarationForConfigProperty_PropertyNotFound() {
    ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
    exchangeDescriptor.setId("MyExchange");
    exchangeDescriptor.setBasePackage("com.x.y.z");
    Assert.assertNull(ExchangeJavaGenUtil.getValueDeclarationForConfigProperty("foo", exchangeDescriptor, null, null));
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
    Assert.assertEquals("PropertiesUtil.getString(myProps, MyExchangeProperties.FOO)", ExchangeJavaGenUtil.getValueDeclarationForConfigProperty("foo", exchangeDescriptor, "myProps", imports));
    Assert.assertEquals(2, imports.size());
    Assert.assertTrue(imports.contains(PropertiesUtil.class.getName()));
    Assert.assertTrue(imports.contains("com.x.y.z.MyExchangeProperties"));
  }
  
  @Test
  public void testGetDescriptionReplacements_NullExchangeDescriptor() {
    Assert.assertTrue(ExchangeJavaGenUtil.getDescriptionReplacements(null, null).isEmpty());
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
    
    Map<String, Object> replacements = ExchangeJavaGenUtil.getDescriptionReplacements(exchangeDescriptor, null, null);
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
    
    Map<String, Object> replacements = ExchangeJavaGenUtil.getDescriptionReplacements(exchangeDescriptor, "Spot", null);
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
    
    Map<String, Object> replacements = ExchangeJavaGenUtil.getDescriptionReplacements(exchangeDescriptor, "Spot", "http://example.com/javadoc/");
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
    ExchangeJavaGenUtil.getDescriptionReplacements(new ExchangeDescriptor(), "Foo");
  }
}
