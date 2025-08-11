package org.jxapi.generator.java.exchange.api.demo;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;
import org.jxapi.exchange.descriptor.ConfigPropertyDescriptor;
import org.jxapi.exchange.descriptor.ExchangeApiDescriptor;
import org.jxapi.exchange.descriptor.ExchangeDescriptor;
import org.jxapi.exchange.descriptor.Field;
import org.jxapi.exchange.descriptor.RestEndpointDescriptor;
import org.jxapi.exchange.descriptor.Type;
import org.jxapi.exchange.descriptor.WebsocketEndpointDescriptor;
import org.jxapi.generator.java.Imports;
import org.jxapi.generator.java.JavaCodeGenUtil;
import org.jxapi.generator.java.exchange.api.ExchangeApiGenUtil;
import org.jxapi.netutils.deserialization.json.field.BigDecimalJsonFieldDeserializer;
import org.jxapi.netutils.deserialization.json.field.BooleanJsonFieldDeserializer;
import org.jxapi.netutils.deserialization.json.field.IntegerJsonFieldDeserializer;
import org.jxapi.netutils.deserialization.json.field.ListJsonFieldDeserializer;
import org.jxapi.netutils.deserialization.json.field.LongJsonFieldDeserializer;
import org.jxapi.netutils.deserialization.json.field.MapJsonFieldDeserializer;
import org.jxapi.netutils.deserialization.json.field.StringJsonFieldDeserializer;
import org.jxapi.util.PlaceHolderResolver;

/**
 * Unit test for {@link EndpointDemoGenUtil}
 */
public class EndpointDemoGenUtilTest {

  @Test
  public void testGenerateEndpointParameterCreationMethodPrimitiveStringParameter() {
    Field param = Field.builder().type(Type.STRING).name("myStringParam").sampleValue("Hello World").build();
    Imports imports = new Imports();
    String method = EndpointDemoGenUtil.generateFieldCreationMethod(param, null, ExchangeApiGenUtil.DEFAULT_REQUEST_ARG_NAME, imports, null);
    Assert.assertEquals(
        "/**\n"
        + " * Creates a sample value for the myStringParam field of type String using sample value(s) defined in the field descriptor.\n"
        + " * \n"
        + " * @param properties the configuration properties to use for the sample value generation.\n"
        + " */\n"
        + "public static String createMyStringParam(Properties properties) {\n"
        + "  return \"Hello World\";\n"
        + "}\n", method);
    checkImports(imports, Properties.class);
  }
  
  @Test
  public void testGenerateEndpointParameterCreationMethodPrimitiveStringParameterNullSampleValue() {
    Field param = Field.builder().type(Type.STRING).name("myStringParam").build();
    Imports imports = new Imports();
    String method = EndpointDemoGenUtil.generateFieldCreationMethod(param, null, ExchangeApiGenUtil.DEFAULT_REQUEST_ARG_NAME, imports, null);
    Assert.assertEquals(
        "/**\n"
        + " * Creates a sample value for the myStringParam field of type String using sample value(s) defined in the field descriptor.\n"
        + " * \n"
        + " * @param properties the configuration properties to use for the sample value generation.\n"
        + " */\n"
        + "public static String createMyStringParam(Properties properties) {\n"
        + "  return null;\n"
        + "}\n", method);
    checkImports(imports, Properties.class);
  }
  
  @Test
  public void testGenerateEndpointParameterCreationMethodPrimitiveIntegerParameter() {
    Field param = Field.builder().type(Type.INT).name("myIntParam").sampleValue(123).build();
    Imports imports = new Imports();
    String method = EndpointDemoGenUtil.generateFieldCreationMethod(param, null, ExchangeApiGenUtil.DEFAULT_REQUEST_ARG_NAME, imports, null);
    Assert.assertEquals(
        "/**\n"
        + " * Creates a sample value for the myIntParam field of type Integer using sample value(s) defined in the field descriptor.\n"
        + " * \n"
        + " * @param properties the configuration properties to use for the sample value generation.\n"
        + " */\n"
        + "public static Integer createMyIntParam(Properties properties) {\n"
        + "  return Integer.valueOf(\"123\");\n"
        + "}\n"
        + "", method);
  }
  
  @Test
  public void  testGenerateEndpointParameterCreationMethodPrimitiveLongParameter() {
    Field param = Field.builder().type(Type.LONG).name("myLongParam").sampleValue(123L).build();
    Imports imports = new Imports();
    String method = EndpointDemoGenUtil.generateFieldCreationMethod(param, null, ExchangeApiGenUtil.DEFAULT_REQUEST_ARG_NAME, imports, null);
    Assert.assertEquals(
        "/**\n"
        + " * Creates a sample value for the myLongParam field of type Long using sample value(s) defined in the field descriptor.\n"
        + " * \n"
        + " * @param properties the configuration properties to use for the sample value generation.\n"
        + " */\n"
        + "public static Long createMyLongParam(Properties properties) {\n"
        + "  return Long.valueOf(\"123\");\n"
        + "}\n", method);
    checkImports(imports, Properties.class);
  }
  
  @Test
  public void  testGenerateEndpointParameterCreationMethodPrimitiveTimestampParameterSpecialValueNow() {
    Field param = Field.builder().type(Type.LONG).name("myLongParam").sampleValue("now()").build();
    Imports imports = new Imports();
    String method = EndpointDemoGenUtil.generateFieldCreationMethod(param, null, ExchangeApiGenUtil.DEFAULT_REQUEST_ARG_NAME, imports, null);
    Assert.assertEquals(
        "/**\n"
        + " * Creates a sample value for the myLongParam field of type Long using sample value(s) defined in the field descriptor.\n"
        + " * \n"
        + " * @param properties the configuration properties to use for the sample value generation.\n"
        + " */\n"
        + "public static Long createMyLongParam(Properties properties) {\n"
        + "  return Long.valueOf(System.currentTimeMillis());\n"
        + "}\n"
        + "", method);
    checkImports(imports, Properties.class);
  }
  
  @Test
  public void  testGenerateEndpointParameterCreationMethodPrimitiveBigDecimalParameter() {
    Field param = Field.builder().type(Type.BIGDECIMAL).name("myBigDecimalParam").sampleValue("123.45").build();
    Imports imports = new Imports();
    String method = EndpointDemoGenUtil.generateFieldCreationMethod(param, null, ExchangeApiGenUtil.DEFAULT_REQUEST_ARG_NAME, imports, null);
    Assert.assertEquals(
        "/**\n"
        + " * Creates a sample value for the myBigDecimalParam field of type BigDecimal using sample value(s) defined in the field descriptor.\n"
        + " * \n"
        + " * @param properties the configuration properties to use for the sample value generation.\n"
        + " */\n"
        + "public static BigDecimal createMyBigDecimalParam(Properties properties) {\n"
        + "  return new BigDecimal(\"123.45\");\n"
        + "}\n", method);
    checkImports(imports, BigDecimal.class, Properties.class);
  }
  
  @Test
  public void  testGenerateEndpointParameterCreationMethodPrimitiveBooleanParameter() {
    Field param = Field.builder().type(Type.BOOLEAN).name("myBooleanParam").sampleValue(true).build();
    Imports imports = new Imports();
    String method = EndpointDemoGenUtil.generateFieldCreationMethod(param, null, ExchangeApiGenUtil.DEFAULT_REQUEST_ARG_NAME, imports, null);
    Assert.assertEquals(
        "/**\n"
        + " * Creates a sample value for the myBooleanParam field of type Boolean using sample value(s) defined in the field descriptor.\n"
        + " * \n"
        + " * @param properties the configuration properties to use for the sample value generation.\n"
        + " */\n"
        + "public static Boolean createMyBooleanParam(Properties properties) {\n"
        + "  return Boolean.valueOf(\"true\");\n"
        + "}\n", method);
    checkImports(imports, Properties.class);
  }

  @Test
  public void testGenerateEndpointParameterCreationMethodSimpleObjectParameter() {
    Field param = Field.builder()
               .name("myObjParam")
               .property(Field.builder().type(Type.INT).name("foo").sampleValue(123).build())
               .property(Field.builder().type(Type.STRING).name("hello").sampleValue("Hello World").build())
               .property(Field.builder().type(Type.LONG).name("aLong").sampleValue(9876543210L).build())
               .property(Field.builder().type(Type.BIGDECIMAL).name("bDecimal").sampleValue("123.45").build())
               .property(Field.builder().type(Type.BOOLEAN).name("cBool").sampleValue(true).build())
               .property(Field.builder().type(Type.STRING).name("theVoidStr").build())
               .property(Field.builder().type("STRING_LIST").name("theVoidList").build())
               .property(Field.builder().type("STRING_MAP").name("theVoidMap").build())
               .build();
               
    Imports imports = new Imports();
    String method = EndpointDemoGenUtil.generateFieldCreationMethod(param, "MyRequest", ExchangeApiGenUtil.DEFAULT_REQUEST_ARG_NAME, imports, null);
    Assert.assertEquals(
        "/**\n"
        + " * Creates a sample value for the myObjParam field of type MyRequest using sample value(s) defined in the field descriptor.\n"
        + " * \n"
        + " * @param properties the configuration properties to use for the sample value generation.\n"
        + " */\n"
        + "public static MyRequest createMyObjParam(Properties properties) {\n"
        + "  MyRequest request = new MyRequest();\n"
        + "  request.setFoo(Integer.valueOf(\"123\"));\n"
        + "  request.setHello(\"Hello World\");\n"
        + "  request.setALong(Long.valueOf(\"9876543210\"));\n"
        + "  request.setBDecimal(new BigDecimal(\"123.45\"));\n"
        + "  request.setCBool(Boolean.valueOf(\"true\"));\n"
        + "  return request;\n"
        + "}\n", method);
    checkImports(imports, BigDecimal.class, Properties.class);
  }
  
  @Test
  public void testGenerateEndpointParameterCreationMethodComplexObjectParameter() {
    Field param = Field.builder()
                  .name("myObjParam")
                  .property(Field.builder().type(Type.INT).name("foo").sampleValue(123).build())
                  .property(Field.builder().type(Type.STRING).name("hello").sampleValue("${helloMessage}").build())
                  .property(Field.builder().type("OBJECT_LIST").name("bar")
                            .property(Field.builder().type(Type.STRING).name("id").sampleValue("id#0").build())
                            .property(Field.builder().type(Type.BOOLEAN).name("enabled").sampleValue(true).build())
                            .property(Field.builder().type(Type.LONG).name("time").build())
                            .property(Field.builder().type("BIGDECIMAL_LIST_MAP").name("bestBids")
                                     .sampleValue("{\"BTC_USDT\": [\"69268.61\", \"69268.62\"], \"ETH_USDT\":[\"3427.98\", \"3427.90\"]}")
                                     .build())
                              .build())
                  .build();
    
    Imports imports = new Imports();
    
    PlaceHolderResolver resolver = s -> {
      if ("${helloMessage}".equals(s)) {
        s = "Hello World!";
      }
      return JavaCodeGenUtil.getQuotedString(s);
    };
    String method = EndpointDemoGenUtil.generateFieldCreationMethod(param, "MyRequest", ExchangeApiGenUtil.DEFAULT_REQUEST_ARG_NAME, imports, resolver);
    Assert.assertEquals(
        "/**\n"
        + " * Creates a sample value for the myObjParam field of type MyRequest using sample value(s) defined in the field descriptor.\n"
        + " * \n"
        + " * @param properties the configuration properties to use for the sample value generation.\n"
        + " */\n"
        + "public static MyRequest createMyObjParam(Properties properties) {\n"
        + "  MyRequest request = new MyRequest();\n"
        + "  request.setFoo(Integer.valueOf(\"123\"));\n"
        + "  request.setHello(\"Hello World!\");\n"
        + "  MyRequestBar request_barItem = new MyRequestBar();\n"
        + "  request_barItem.setId(\"id#0\");\n"
        + "  request_barItem.setEnabled(Boolean.valueOf(\"true\"));\n"
        + "  request_barItem.setBestBids(new MapJsonFieldDeserializer<>(new ListJsonFieldDeserializer<>(BigDecimalJsonFieldDeserializer.getInstance())).deserialize(\"{\\\"BTC_USDT\\\": [\\\"69268.61\\\", \\\"69268.62\\\"], \\\"ETH_USDT\\\":[\\\"3427.98\\\", \\\"3427.90\\\"]}\"));\n"
        + "  request.setBar(List.of(request_barItem));\n"
        + "  return request;\n"
        + "}\n", method);
    
    checkImports(imports, 
         List.class,
         Properties.class,
         BigDecimalJsonFieldDeserializer.class,
         ListJsonFieldDeserializer.class,
         MapJsonFieldDeserializer.class );
  }
  
  @Test
  public void testGenerateEndpointParameterCreationMethodPrimitiveIntegerList() {
    Field param = Field.builder()
               .type("INT_LIST")
               .name("myIntListParam")
               .sampleValue(List.of(1,3,5,7))
               .build();
    Imports imports = new Imports();
    String method = EndpointDemoGenUtil.generateFieldCreationMethod(param, null, ExchangeApiGenUtil.DEFAULT_REQUEST_ARG_NAME, imports, null);
    Assert.assertEquals(
        "/**\n"
        + " * Creates a sample value for the myIntListParam field of type List<Integer> using sample value(s) defined in the field descriptor.\n"
        + " * \n"
        + " * @param properties the configuration properties to use for the sample value generation.\n"
        + " */\n"
        + "public static List<Integer> createMyIntListParam(Properties properties) {\n"
        + "  return new ListJsonFieldDeserializer<>(IntegerJsonFieldDeserializer.getInstance()).deserialize(\"[1, 3, 5, 7]\");\n"
        + "}\n"
        + "", method);
    checkImports(imports,
           List.class,
           Properties.class,
           IntegerJsonFieldDeserializer.class, 
           ListJsonFieldDeserializer.class);
  }
  
  @Test
  public void testGenerateEndpointParameterCreationMethodPrimitiveStringList() {
    Field param = Field.builder()
               .type("STRING_LIST")
               .name("myStringListParam")
               .sampleValue("[\"BTC\",\"ETH\"]")
               .build();
    Imports imports = new Imports();
    String method = EndpointDemoGenUtil.generateFieldCreationMethod(param, null, ExchangeApiGenUtil.DEFAULT_REQUEST_ARG_NAME, imports, null);
    Assert.assertEquals(
        "/**\n"
        + " * Creates a sample value for the myStringListParam field of type List<String> using sample value(s) defined in the field descriptor.\n"
        + " * \n"
        + " * @param properties the configuration properties to use for the sample value generation.\n"
        + " */\n"
        + "public static List<String> createMyStringListParam(Properties properties) {\n"
        + "  return new ListJsonFieldDeserializer<>(StringJsonFieldDeserializer.getInstance()).deserialize(\"[\\\"BTC\\\",\\\"ETH\\\"]\");\n"
        + "}\n", method);
    checkImports(imports, 
           List.class, 
           Properties.class,
           ListJsonFieldDeserializer.class, 
           StringJsonFieldDeserializer.class);
  }
  
  @Test
  public void testGenerateEndpointParameterCreationMethodPrimitiveLongList() {
    Field param = Field.builder()
               .type("LONG_LIST")
               .name("myLongListParam")
               .sampleValue("[1234567890,9876543210]")
               .build();
    Imports imports = new Imports();
    String method = EndpointDemoGenUtil.generateFieldCreationMethod(param, null, ExchangeApiGenUtil.DEFAULT_REQUEST_ARG_NAME, imports, null);
    Assert.assertEquals(
        "/**\n"
        + " * Creates a sample value for the myLongListParam field of type List<Long> using sample value(s) defined in the field descriptor.\n"
        + " * \n"
        + " * @param properties the configuration properties to use for the sample value generation.\n"
        + " */\n"
        + "public static List<Long> createMyLongListParam(Properties properties) {\n"
        + "  return new ListJsonFieldDeserializer<>(LongJsonFieldDeserializer.getInstance()).deserialize(\"[1234567890,9876543210]\");\n"
        + "}\n", method);
    checkImports(imports, 
            List.class, 
            Properties.class,
            ListJsonFieldDeserializer.class, 
            LongJsonFieldDeserializer.class);
  }
  
  @Test
  public void testGenerateEndpointParameterCreationMethodPrimitiveBigDecimalList() {
    Field param = Field.builder()
               .type("BIGDECIMAL_LIST")
               .name("myBigDecimalListParam")
               .sampleValue("[1234.56789,9876.54321]")
               .build();
    Imports imports = new Imports();
    String method = EndpointDemoGenUtil.generateFieldCreationMethod(param, null, ExchangeApiGenUtil.DEFAULT_REQUEST_ARG_NAME, imports, null);
    Assert.assertEquals(
        "/**\n"
        + " * Creates a sample value for the myBigDecimalListParam field of type List<BigDecimal> using sample value(s) defined in the field descriptor.\n"
        + " * \n"
        + " * @param properties the configuration properties to use for the sample value generation.\n"
        + " */\n"
        + "public static List<BigDecimal> createMyBigDecimalListParam(Properties properties) {\n"
        + "  return new ListJsonFieldDeserializer<>(BigDecimalJsonFieldDeserializer.getInstance()).deserialize(\"[1234.56789,9876.54321]\");\n"
        + "}\n", method);
    checkImports(imports,
           BigDecimal.class,
           List.class,
           Properties.class,
           BigDecimalJsonFieldDeserializer.class,
           ListJsonFieldDeserializer.class);
  }
  
  @Test
  public void testGenerateEndpointParameterCreationMethodPrimitiveBooleanList() {
    Field param = Field.builder()
           .type("BOOLEAN_LIST")
           .name("myBooleanListParam")
           .sampleValue("[true, false]")
           .build();
    Imports imports = new Imports();
    String method = EndpointDemoGenUtil.generateFieldCreationMethod(param, null, ExchangeApiGenUtil.DEFAULT_REQUEST_ARG_NAME, imports, null);
    Assert.assertEquals(
        "/**\n"
        + " * Creates a sample value for the myBooleanListParam field of type List<Boolean> using sample value(s) defined in the field descriptor.\n"
        + " * \n"
        + " * @param properties the configuration properties to use for the sample value generation.\n"
        + " */\n"
        + "public static List<Boolean> createMyBooleanListParam(Properties properties) {\n"
        + "  return new ListJsonFieldDeserializer<>(BooleanJsonFieldDeserializer.getInstance()).deserialize(\"[true, false]\");\n"
        + "}\n", method);
    checkImports(imports, 
           List.class,
           Properties.class,
           BooleanJsonFieldDeserializer.class, 
           ListJsonFieldDeserializer.class);
  }
  
  @Test
  public void testGenerateEndpointParameterCreationMethodObjectList() {  
    Field param = Field.builder()
               .type("OBJECT_LIST")
               .name("myObjListParam")
               .property(Field.builder()
                         .type("INT")
                         .name("foo")
                         .sampleValue(123)
                         .build())
               .property(Field.builder()
                           .type("STRING")
                           .name("hello")
                           .sampleValue("Hello World")
                           .build())
               .build();
    Imports imports = new Imports();
    String method = EndpointDemoGenUtil.generateFieldCreationMethod(param, "MyRequest", ExchangeApiGenUtil.DEFAULT_REQUEST_ARG_NAME, imports, null);
    Assert.assertEquals(
        "/**\n"
        + " * Creates a sample value for the myObjListParam field of type List<MyRequest> using sample value(s) defined in the field descriptor.\n"
        + " * \n"
        + " * @param properties the configuration properties to use for the sample value generation.\n"
        + " */\n"
        + "public static List<MyRequest> createMyObjListParam(Properties properties) {\n"
        + "  MyRequest requestItem = new MyRequest();\n"
        + "  requestItem.setFoo(Integer.valueOf(\"123\"));\n"
        + "  requestItem.setHello(\"Hello World\");\n"
        + "  return List.of(requestItem);\n"
        + "}\n", method);
    checkImports(imports, List.class, Properties.class);
  }
  
  @Test
  public void testGenerateEndpointParameterCreationMethodObjectListWithFullListSampleValue() {  
    Field param = Field.builder()
               .type("OBJECT_LIST")
               .name("myObjListParam")
               .property(Field.builder()
                         .type("INT")
                         .name("foo")
                         .sampleValue(123)
                         .build())
               .property(Field.builder()
                           .type("STRING")
                           .name("hello")
                           .sampleValue("Hello World")
                           .build())
               .sampleValue("[{\"foo\": 1230, \"hello!\": \"Hello World!\"}, {\"foo\": 4560, \"Hello hello\": \"Goodbye World\"}]")
               .build();
    Imports imports = new Imports();
    String method = EndpointDemoGenUtil.generateFieldCreationMethod(param, "com.x.gen.api.pojo.MyRequest", ExchangeApiGenUtil.DEFAULT_REQUEST_ARG_NAME, imports, null);
    Assert.assertEquals(
        "/**\n"
        + " * Creates a sample value for the myObjListParam field of type List<MyRequest> using sample value(s) defined in the field descriptor.\n"
        + " * \n"
        + " * @param properties the configuration properties to use for the sample value generation.\n"
        + " */\n"
        + "public static List<MyRequest> createMyObjListParam(Properties properties) {\n"
        + "  return new ListJsonFieldDeserializer<>(new MyRequestDeserializer()).deserialize(\"[{\\\"foo\\\": 1230, \\\"hello!\\\": \\\"Hello World!\\\"}, {\\\"foo\\\": 4560, \\\"Hello hello\\\": \\\"Goodbye World\\\"}]\");\n"
        + "}\n"
        + "", method);
    checkImports(imports, 
        List.class.getName(), 
        Properties.class.getName(), 
        "com.x.gen.api.deserializers.MyRequestDeserializer", 
        "com.x.gen.api.pojo.MyRequest",
        ListJsonFieldDeserializer.class.getName());
  }
  
  @Test
  public void testGenerateEndpointParameterCreationMethodObjectMap() {
    Field param = Field.builder()
               .type("OBJECT_MAP")
               .name("myObjMapParam")
               .property(Field.builder()
                         .type("INT")
                         .name("foo")
                         .sampleValue(123)
                         .build())
               .property(Field.builder()
                           .type("STRING")
                           .name("hello")
                           .sampleValue("Hello World")
                           .build())
               .sampleMapKeyValues(List.of("myKey0"))
               .build();
    
    Imports imports = new Imports();
    String method = EndpointDemoGenUtil.generateFieldCreationMethod(param, "MyRequest", ExchangeApiGenUtil.DEFAULT_REQUEST_ARG_NAME, imports, null);
    Assert.assertEquals(
        "/**\n"
        + " * Creates a sample value for the myObjMapParam field of type Map<String, MyRequest> using sample value(s) defined in the field descriptor.\n"
        + " * \n"
        + " * @param properties the configuration properties to use for the sample value generation.\n"
        + " */\n"
        + "public static Map<String, MyRequest> createMyObjMapParam(Properties properties) {\n"
        + "  MyRequest requestItem = new MyRequest();\n"
        + "  requestItem.setFoo(Integer.valueOf(\"123\"));\n"
        + "  requestItem.setHello(\"Hello World\");\n"
        + "  return Map.of(\"myKey0\", requestItem);\n"
        + "}\n", method);
    checkImports(imports, Map.class, Properties.class);
  }
  
  @Test
  public void testGenerateEndpointParameterCreationMethodObjectMapNullSampleMapKey() {
    Field param = Field.builder()
               .type("OBJECT_MAP")
               .name("myObjMapParam")
               .property(Field.builder()
                         .type("INT")
                         .name("foo")
                         .sampleValue(123)
                         .build())
               .property(Field.builder()
                           .type("STRING")
                           .name("hello")
                           .sampleValue("Hello World")
                           .build())
               .sampleMapKeyValues(null)
               .build();
    param.setSampleMapKeyValue(null);
    Imports imports = new Imports();
    String method = EndpointDemoGenUtil.generateFieldCreationMethod(param, "MyRequest", ExchangeApiGenUtil.DEFAULT_REQUEST_ARG_NAME, imports, null);
    Assert.assertEquals(
        "/**\n"
        + " * Creates a sample value for the myObjMapParam field of type Map<String, MyRequest> using sample value(s) defined in the field descriptor.\n"
        + " * \n"
        + " * @param properties the configuration properties to use for the sample value generation.\n"
        + " */\n"
        + "public static Map<String, MyRequest> createMyObjMapParam(Properties properties) {\n"
        + "  return null;\n"
        + "}\n", method);
    checkImports(imports, Map.class, Properties.class);
  }
  
  @Test
  public void testGenerateEndpointParameterCreationMethodObjectMapEmptySampleMapKey() {
    Field param = Field.builder()
               .type("OBJECT_MAP")
               .name("myObjMapParam")
               .property(Field.builder()
                         .type("INT")
                         .name("foo")
                         .sampleValue(123)
                         .build())
               .property(Field.builder()
                           .type("STRING")
                           .name("hello")
                           .sampleValue("Hello World")
                           .build())
               .sampleMapKeyValues(List.of())
               .build();
    Imports imports = new Imports();
    String method = EndpointDemoGenUtil.generateFieldCreationMethod(param, "MyRequest", ExchangeApiGenUtil.DEFAULT_REQUEST_ARG_NAME, imports, null);
    Assert.assertEquals(
        "/**\n"
        + " * Creates a sample value for the myObjMapParam field of type Map<String, MyRequest> using sample value(s) defined in the field descriptor.\n"
        + " * \n"
        + " * @param properties the configuration properties to use for the sample value generation.\n"
        + " */\n"
        + "public static Map<String, MyRequest> createMyObjMapParam(Properties properties) {\n"
        + "  return null;\n"
        + "}\n", method);
    checkImports(imports, Map.class, Properties.class);
  }
  
  @Test
  public void testGenerateEndpointParameterCreationMethodObjectMapMap() {
    Field param = Field.builder()
               .type("OBJECT_MAP_LIST_MAP")
               .name("myObjMapListMapParam")
               .property(Field.builder()
                         .type("INT")
                         .name("foo")
                         .sampleValue(123)
                         .build())
               .property(Field.builder()
                           .type("STRING")
                           .name("hello")
                           .sampleValue("Hello World")
                           .build())
               .sampleMapKeyValues(List.of("m1Key", "m2Key"))
               .build();
    
    Imports imports = new Imports();
    String method = EndpointDemoGenUtil.generateFieldCreationMethod(param, "MyRequest", ExchangeApiGenUtil.DEFAULT_REQUEST_ARG_NAME, imports, null);
    Assert.assertEquals(
        "/**\n"
        + " * Creates a sample value for the myObjMapListMapParam field of type Map<String, List<Map<String, MyRequest>>> using sample value(s) defined in the field descriptor.\n"
        + " * \n"
        + " * @param properties the configuration properties to use for the sample value generation.\n"
        + " */\n"
        + "public static Map<String, List<Map<String, MyRequest>>> createMyObjMapListMapParam(Properties properties) {\n"
        + "  MyRequest requestItem = new MyRequest();\n"
        + "  requestItem.setFoo(Integer.valueOf(\"123\"));\n"
        + "  requestItem.setHello(\"Hello World\");\n"
        + "  return Map.of(\"m1Key\", List.of(Map.of(\"m2Key\", requestItem)));\n"
        + "}\n", method);
    checkImports(imports, List.class, Map.class, Properties.class);
  }
  
  @Test
  public void testGenerateEndpointParameterCreationMethodStringValuesMap() {
    Field param = Field.builder().type("STRING_MAP").name("adresses").sampleValue("{\"Jamie\": \"London\", \"Amina\": \"Djibouti\"}").build();
    Imports imports = new Imports();
    String method = EndpointDemoGenUtil.generateFieldCreationMethod(param, "MyRequest", ExchangeApiGenUtil.DEFAULT_REQUEST_ARG_NAME, imports, null);
    Assert.assertEquals(
        "/**\n"
        + " * Creates a sample value for the adresses field of type Map<String, String> using sample value(s) defined in the field descriptor.\n"
        + " * \n"
        + " * @param properties the configuration properties to use for the sample value generation.\n"
        + " */\n"
        + "public static Map<String, String> createAdresses(Properties properties) {\n"
        + "  return new MapJsonFieldDeserializer<>(StringJsonFieldDeserializer.getInstance()).deserialize(\"{\\\"Jamie\\\": \\\"London\\\", \\\"Amina\\\": \\\"Djibouti\\\"}\");\n"
        + "}\n", method);
    checkImports(imports, 
           Map.class, 
           Properties.class,
           MapJsonFieldDeserializer.class, 
           StringJsonFieldDeserializer.class);
  }
  
  private void checkImports(Imports actualImports, Class<?>...expectedImports) {
    checkImports(
        actualImports, 
        Arrays.stream(expectedImports)
                  .map(i -> i.getName())
                  .collect(Collectors.toList())
                  .toArray(new String[expectedImports.length]));
  }
  
  private void checkImports(Imports actualImports, String...expectedImports) {
    List<String> actual = actualImports.getAllImports().stream().filter(s -> !s.startsWith("java.lang") && s.contains(".")).collect(Collectors.toList());
    BinaryOperator<String> concat = (s1, s2) -> s1 + "\n" + s2;
    String errMsg = "Unexpected imports, expected:\n" 
            + Arrays.stream(expectedImports)
                .reduce(concat).orElse(null) 
            + "\n actual:\n" 
            + actual.stream().reduce(concat).orElse(null);
    Assert.assertEquals(errMsg, expectedImports.length, actual.size());
    for (int i = 0; i < expectedImports.length; i++) {
      Assert.assertEquals(errMsg, expectedImports[i], actual.get(i));
    }
  }
  
  @Test
  public void testGetRestApiDemoClassName() {
      ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
      exchangeDescriptor.setId("MyExchange");
      exchangeDescriptor.setBasePackage("com.x.y.z.gen");
      ExchangeApiDescriptor exchangeApiDescriptor = new ExchangeApiDescriptor();
      exchangeApiDescriptor.setName("MyApi");
      RestEndpointDescriptor restEndpointDescriptor = new RestEndpointDescriptor();
      restEndpointDescriptor.setName("MyRestEndpoint");
      String className = EndpointDemoGenUtil.getRestApiDemoClassName(exchangeDescriptor, exchangeApiDescriptor, restEndpointDescriptor);
      Assert.assertEquals("com.x.y.z.gen.myapi.demo.MyExchangeMyApiMyRestEndpointDemo", className);
  }
  
  @Test
  public void testGetWebsocketApiDemoClassName() {
    ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
    exchangeDescriptor.setId("MyExchange");
    exchangeDescriptor.setBasePackage("com.x.y.z.gen");
    ExchangeApiDescriptor exchangeApiDescriptor = new ExchangeApiDescriptor();
    exchangeApiDescriptor.setName("MyApi");
    WebsocketEndpointDescriptor websocketEndpointDescriptor = new WebsocketEndpointDescriptor();
    websocketEndpointDescriptor.setName("MyWebsocketEndpoint");
    String className = EndpointDemoGenUtil.getWebsocketApiDemoClassName(exchangeDescriptor, exchangeApiDescriptor, websocketEndpointDescriptor);
    Assert.assertEquals("com.x.y.z.gen.myapi.demo.MyExchangeMyApiMyWebsocketEndpointDemo", className);
  }
  
  @Test
  public void testGetNewTestApiInstruction() {
    String simpleApiClassName  = "MyApi";
    Assert.assertEquals("MyApi api = exchange.getMyApi();", 
        EndpointDemoGenUtil.getNewTestApiInstruction("exchange", "api", simpleApiClassName));
  }
  
  @Test
  public void testGetNewTestExchangeInstruction() {
    String exchangeClassName = "com.x.y.z.gen.MyExchange";
    Assert.assertEquals("MyExchange exchange = new MyExchangeImpl(\"test-\" + MyExchange.ID, props);",
        EndpointDemoGenUtil.getNewTestExchangeInstruction(exchangeClassName, "exchange", "props"));
  }
  
  @Test
  public void testCollectDemoConfigProperties_ExchangeWithoutApiGroup() {
    ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
    exchangeDescriptor.setId("MyExchange");
    exchangeDescriptor.setBasePackage("com.x.y.z.gen");
    List<ConfigPropertyDescriptor> demoGroupProp = EndpointDemoGenUtil.collectDemoConfigProperties(exchangeDescriptor);
    Assert.assertEquals(0, demoGroupProp.size());
  }
  
  @Test
  public void testGetPropertiesInstruction() {
    Imports imports = new Imports();
    Assert.assertEquals(
        "DemoUtil.loadDemoExchangeProperties(properties.ID)",
        EndpointDemoGenUtil.getTestPropertiesInstruction("properties", imports));
  }
  
  @Test
  public void testCollectDemoConfigProperties_ExchangeWithOneApiGroupWithoutEndpoint() {
    ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
    exchangeDescriptor.setId("MyExchange");
    exchangeDescriptor.setBasePackage("com.x.y.z.gen");
    ExchangeApiDescriptor exchangeApiDescriptor = new ExchangeApiDescriptor();
    exchangeApiDescriptor.setName("MyApi");
    exchangeDescriptor.setApis(List.of(exchangeApiDescriptor));
    List<ConfigPropertyDescriptor> demoGroupProp = EndpointDemoGenUtil.collectDemoConfigProperties(exchangeDescriptor);
    Assert.assertEquals(0, demoGroupProp.size());
  }
  
  @Test
  public void testCollectDemoConfigProperties_ExchangeWithOneApiWithRestAndWsEnpointsWithoutRequest() {
    ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
    exchangeDescriptor.setId("MyExchange");
    exchangeDescriptor.setBasePackage("com.x.y.z.gen");
    ExchangeApiDescriptor exchangeApiDescriptor = new ExchangeApiDescriptor();
    exchangeApiDescriptor.setName("MyApi");
    RestEndpointDescriptor restEndpointDescriptor = new RestEndpointDescriptor();
    restEndpointDescriptor.setName("MyRestEndpoint");
    exchangeApiDescriptor.setRestEndpoints(List.of(restEndpointDescriptor));
    WebsocketEndpointDescriptor websocketEndpointDescriptor = new WebsocketEndpointDescriptor();
    websocketEndpointDescriptor.setName("MyWebsocketEndpoint");
    exchangeDescriptor.setApis(List.of(exchangeApiDescriptor));
    exchangeApiDescriptor.setWebsocketEndpoints(List.of(websocketEndpointDescriptor));
    List<ConfigPropertyDescriptor> demoGroupProp = EndpointDemoGenUtil.collectDemoConfigProperties(exchangeDescriptor);
    Assert.assertEquals(0, demoGroupProp.size());
  }
  
  @Test
  public void testCollectDemoConfigProperties_RestEndpointWithPrimitiveTypeRequest() {
    ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
    exchangeDescriptor.setId("myExchange");
    exchangeDescriptor.setBasePackage("com.x.y.z.gen");
    ExchangeApiDescriptor exchangeApiDescriptor = new ExchangeApiDescriptor();
    exchangeApiDescriptor.setName("myApi");
    exchangeDescriptor.setApis(List.of(exchangeApiDescriptor));
    RestEndpointDescriptor restEndpointDescriptor = new RestEndpointDescriptor();
    restEndpointDescriptor.setName("myRestEndpoint");
    Field request = Field.builder()
        .type(Type.INT)
        .description("This is a test parameter")
        .sampleValue(123).build();
    restEndpointDescriptor.setRequest(request);
    exchangeApiDescriptor.setRestEndpoints(List.of(restEndpointDescriptor));
    List<ConfigPropertyDescriptor> demoGroupProp = EndpointDemoGenUtil.collectDemoConfigProperties(exchangeDescriptor);
    Assert.assertEquals(1, demoGroupProp.size());
    ConfigPropertyDescriptor apiDemoGroupProp = demoGroupProp.get(0);
    checkDemoProperty(apiDemoGroupProp, "myApi", "Configuration properties for myApi API group endpoints demo snippets", Type.OBJECT, null);
    Assert.assertEquals(1, apiDemoGroupProp.getProperties().size());
    ConfigPropertyDescriptor restEndpointsGroup = apiDemoGroupProp.getProperties().get(0);
    checkDemoProperty(restEndpointsGroup, "myApi.rest", "Configuration properties for REST endpoints demo snippets of myApi API group", Type.OBJECT, null);
    Assert.assertEquals(1, restEndpointsGroup.getProperties().size());
    ConfigPropertyDescriptor endpointGroup = restEndpointsGroup.getProperties().get(0);
    checkDemoProperty(endpointGroup, 
        "myApi.rest.myRestEndpoint", 
        "Configuration properties for myApi.rest myRestEndpoint endpoint of myApi API group", 
        Type.OBJECT, 
        null);
    Assert.assertEquals(1, endpointGroup.getProperties().size());
    ConfigPropertyDescriptor endpointRequestProp = endpointGroup.getProperties().get(0);
    checkDemoProperty(endpointRequestProp, "myApi.rest.myRestEndpoint.request", 
        "Demo configuration property for myRestEndpoint.request field<p>\n"
        + "This is a test parameter", 
        Type.INT, 
        123);
  }
  
  @Test
  public void testCollectDemoConfigProperties_WsEndpointWithPrimitiveTypeRequest() {
    ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
    exchangeDescriptor.setId("myExchange");
    exchangeDescriptor.setBasePackage("com.x.y.z.gen");
    ExchangeApiDescriptor exchangeApiDescriptor = new ExchangeApiDescriptor();
    exchangeApiDescriptor.setName("myApi");
    exchangeDescriptor.setApis(List.of(exchangeApiDescriptor));
    WebsocketEndpointDescriptor wsEndpointDescriptor = new WebsocketEndpointDescriptor();
    wsEndpointDescriptor.setName("myRestEndpoint");
    Field request = Field.builder()
        .name("myStringParam")
        .description("This is a test parameter")
        .sampleValue(123).build();
    wsEndpointDescriptor.setRequest(request);
    exchangeApiDescriptor.setWebsocketEndpoints(List.of(wsEndpointDescriptor));
    List<ConfigPropertyDescriptor> demoGroupProp = EndpointDemoGenUtil.collectDemoConfigProperties(exchangeDescriptor);
    Assert.assertEquals(1, demoGroupProp.size());
    ConfigPropertyDescriptor apiDemoGroupProp = demoGroupProp.get(0);
    checkDemoProperty(apiDemoGroupProp, 
        "myApi", 
        "Configuration properties for myApi API group endpoints demo snippets", 
        Type.OBJECT, 
        null);
    Assert.assertEquals(1, apiDemoGroupProp.getProperties().size());
    ConfigPropertyDescriptor restEndpointsGroup = apiDemoGroupProp.getProperties().get(0);
    checkDemoProperty(restEndpointsGroup, 
        "myApi.ws", 
        "Configuration properties for websocket endpoints demo snippets of myApi API group", 
        Type.OBJECT, 
        null);
    Assert.assertEquals(1, restEndpointsGroup.getProperties().size());
    ConfigPropertyDescriptor endpointGroup = restEndpointsGroup.getProperties().get(0);
    checkDemoProperty(endpointGroup, 
        "myApi.ws.myRestEndpoint", 
        "Configuration properties for myApi.ws myRestEndpoint endpoint of myApi API group", 
        Type.OBJECT, 
        null);
    Assert.assertEquals(1, endpointGroup.getProperties().size());
    ConfigPropertyDescriptor endpointRequestProp = endpointGroup.getProperties().get(0);
    checkDemoProperty(endpointRequestProp, 
        "myApi.ws.myRestEndpoint.myStringParam", 
        "Demo configuration property for myRestEndpoint.myStringParam field<p>\n"
        + "This is a test parameter", 
        Type.STRING, 
        123);
  }
  
  @Test
  public void testCollectDemoConfigProperties_RestEndpointWithObjectTypeRequestAndWsEndpointWithReferenceToSameObject() {
    ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
    exchangeDescriptor.setId("myExchange");
    exchangeDescriptor.setBasePackage("com.x.y.z.gen");
    ExchangeApiDescriptor exchangeApiDescriptor = new ExchangeApiDescriptor();
    exchangeApiDescriptor.setName("myApi");
    exchangeDescriptor.setApis(List.of(exchangeApiDescriptor));
    RestEndpointDescriptor restEndpointDescriptor = new RestEndpointDescriptor();
    restEndpointDescriptor.setName("myRestEndpoint");
    Field restRequest = Field.builder()
        .description("This is a test parameter")
        .objectName("MyRequest")
        .properties(List.of(
            Field.builder()
                .type(Type.INT)
                .name("myIntParam")
                .sampleValue(123)
                .build(),
            Field.builder()
                .name("mySubParam")
                .property(Field.builder()
                    .name("hello")
                    .sampleValue("Hello World")
                    .build())
                .build()
         ))
        .build();
    restEndpointDescriptor.setRequest(restRequest);
    exchangeApiDescriptor.setRestEndpoints(List.of(restEndpointDescriptor));
    
    WebsocketEndpointDescriptor wsEndpointDescriptor = new WebsocketEndpointDescriptor();
    wsEndpointDescriptor.setName("myWsEndpoint");
    Field wsRequest = Field.builder()
        .name("mySubscribeRequest")
        .objectName("MyRequest")
        .description("This is a test parameter for the mySubscribeRequest field")
        .build();
    wsEndpointDescriptor.setRequest(wsRequest);
    exchangeApiDescriptor.setWebsocketEndpoints(List.of(wsEndpointDescriptor));
    
    List<ConfigPropertyDescriptor> demoGroupProp = EndpointDemoGenUtil.collectDemoConfigProperties(exchangeDescriptor);
    Assert.assertEquals(1, demoGroupProp.size());
    ConfigPropertyDescriptor apiDemoGroupProp = demoGroupProp.get(0);
    checkDemoProperty(apiDemoGroupProp, 
        "myApi", 
        "Configuration properties for myApi API group endpoints demo snippets", 
        Type.OBJECT, 
        null);
    Assert.assertEquals(2, apiDemoGroupProp.getProperties().size());
    
    // REST endpoints group
    ConfigPropertyDescriptor restEndpointsGroup = apiDemoGroupProp.getProperties().get(0);
    checkDemoProperty(restEndpointsGroup, 
        "myApi.rest", 
        "Configuration properties for REST endpoints demo snippets of myApi API group", 
        Type.OBJECT, 
        null);
    Assert.assertEquals(1, restEndpointsGroup.getProperties().size());
    ConfigPropertyDescriptor restEndpointGroup = restEndpointsGroup.getProperties().get(0);
    checkDemoProperty(restEndpointGroup, 
        "myApi.rest.myRestEndpoint", "Configuration properties for myApi.rest myRestEndpoint endpoint of myApi API group", 
        Type.OBJECT, 
        null);
    Assert.assertEquals(1, restEndpointGroup.getProperties().size());
    ConfigPropertyDescriptor requestProp = restEndpointGroup.getProperties().get(0);
    checkDemoProperty(requestProp, 
        "myApi.rest.myRestEndpoint.request", 
        "Demo configuration property for myRestEndpoint.request field<p>\n"
        + "This is a test parameter", 
        Type.OBJECT, 
        null);
    Assert.assertEquals(2, requestProp.getProperties().size());
    checkDemoProperty(requestProp.getProperties().get(0), 
        "myApi.rest.myRestEndpoint.request.myIntParam",
        "Demo configuration property for request.myIntParam field", 
        Type.INT, 
        123);
    
    ConfigPropertyDescriptor mySubParam = requestProp.getProperties().get(1);
    checkDemoProperty(mySubParam, 
        "myApi.rest.myRestEndpoint.request.mySubParam",
        "Demo configuration property for request.mySubParam field", 
        Type.OBJECT, 
        null);
    Assert.assertEquals(1, mySubParam.getProperties().size());
    ConfigPropertyDescriptor helloProp = mySubParam.getProperties().get(0);
    checkDemoProperty(helloProp, 
        "myApi.rest.myRestEndpoint.request.mySubParam.hello",
        "Demo configuration property for mySubParam.hello field", 
        Type.STRING, 
        "Hello World");
    
    // Websocket endpoints group
    ConfigPropertyDescriptor wsEndpointsGroup = apiDemoGroupProp.getProperties().get(1);
    checkDemoProperty(wsEndpointsGroup, 
        "myApi.ws", 
        "Configuration properties for websocket endpoints demo snippets of myApi API group",
        Type.OBJECT, 
        null);
    Assert.assertEquals(1, restEndpointsGroup.getProperties().size());
    ConfigPropertyDescriptor wsEndpointGroup = wsEndpointsGroup.getProperties().get(0);
    checkDemoProperty(wsEndpointGroup, 
        "myApi.ws.myWsEndpoint", 
        "Configuration properties for myApi.ws myWsEndpoint endpoint of myApi API group", 
        Type.OBJECT, 
        null);
    Assert.assertEquals(1, wsEndpointGroup.getProperties().size());
    ConfigPropertyDescriptor wsRequestProp = wsEndpointGroup.getProperties().get(0);
    checkDemoProperty(wsRequestProp, 
        "myApi.ws.myWsEndpoint.mySubscribeRequest", 
        "Demo configuration property for myWsEndpoint.mySubscribeRequest field<p>\n"
        + "This is a test parameter for the mySubscribeRequest field", 
        Type.OBJECT, 
        null);
    Assert.assertEquals(2, wsRequestProp.getProperties().size());
    checkDemoProperty(wsRequestProp.getProperties().get(0), 
        "myApi.ws.myWsEndpoint.mySubscribeRequest.myIntParam",
        "Demo configuration property for mySubscribeRequest.myIntParam field", 
        Type.INT, 
        123);
    
    ConfigPropertyDescriptor wsMySubParam = wsRequestProp.getProperties().get(1);
    checkDemoProperty(wsMySubParam, 
        "myApi.ws.myWsEndpoint.mySubscribeRequest.mySubParam",
        "Demo configuration property for mySubscribeRequest.mySubParam field", 
        Type.OBJECT, 
        null);
    Assert.assertEquals(1, wsMySubParam.getProperties().size());
    checkDemoProperty(wsMySubParam.getProperties().get(0), 
        "myApi.ws.myWsEndpoint.mySubscribeRequest.mySubParam.hello",
        "Demo configuration property for mySubParam.hello field", 
        Type.STRING, 
        "Hello World");
  }
  
  private void checkDemoProperty(ConfigPropertyDescriptor prop, 
                                 String name, 
                                 String description, 
                                 Type type, 
                                 Object sampleValue) {
    Assert.assertEquals(name, prop.getName());
    Assert.assertEquals("Invalid description for prop:" + name, description, prop.getDescription());
    Assert.assertEquals(type, prop.getType());
    Assert.assertEquals("Invalid default value for prop:" + name, sampleValue, prop.getDefaultValue());
  }
}
