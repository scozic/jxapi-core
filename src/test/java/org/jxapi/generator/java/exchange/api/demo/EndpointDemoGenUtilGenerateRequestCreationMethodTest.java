package org.jxapi.generator.java.exchange.api.demo;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jxapi.exchange.descriptor.ExchangeApiDescriptor;
import org.jxapi.exchange.descriptor.ExchangeDescriptor;
import org.jxapi.exchange.descriptor.RestEndpointDescriptor;
import org.jxapi.exchange.descriptor.WebsocketEndpointDescriptor;
import org.jxapi.generator.java.Imports;
import org.jxapi.generator.java.exchange.api.ExchangeApiGenUtil;
import org.jxapi.generator.java.pojo.PojoGenUtil;
import org.jxapi.netutils.deserialization.json.field.BigDecimalJsonFieldDeserializer;
import org.jxapi.netutils.deserialization.json.field.BooleanJsonFieldDeserializer;
import org.jxapi.netutils.deserialization.json.field.IntegerJsonFieldDeserializer;
import org.jxapi.netutils.deserialization.json.field.ListJsonFieldDeserializer;
import org.jxapi.netutils.deserialization.json.field.LongJsonFieldDeserializer;
import org.jxapi.netutils.deserialization.json.field.MapJsonFieldDeserializer;
import org.jxapi.netutils.deserialization.json.field.StringJsonFieldDeserializer;
import org.jxapi.pojo.descriptor.Field;
import org.jxapi.pojo.descriptor.Type;

/**
 * Unit tests for {@link EndpointDemoGenUtil#generateFieldCreationMethod(org.jxapi.pojo.descriptor.Field, String, org.jxapi.exchange.descriptor.ExchangeDescriptor, org.jxapi.exchange.descriptor.ExchangeApiDescriptor, String, org.jxapi.generator.java.Imports)}
 */
public class EndpointDemoGenUtilGenerateRequestCreationMethodTest {

  private ExchangeDescriptor exchangeDescriptor;
  private ExchangeApiDescriptor exchangeApiDescriptor;
  private RestEndpointDescriptor restEndpointDescriptor;
  private WebsocketEndpointDescriptor websocketEndpointDescriptor;
  
  @Before
  public void setUp() {
    exchangeDescriptor = new ExchangeDescriptor();
    exchangeDescriptor.setId("myExchange");
    exchangeDescriptor.setBasePackage("com.x.gen");
    exchangeApiDescriptor = new ExchangeApiDescriptor();
    exchangeApiDescriptor.setName("myApi");
    restEndpointDescriptor = new RestEndpointDescriptor();
    restEndpointDescriptor.setName("myRestEndpoint");
    exchangeApiDescriptor.setRestEndpoints(List.of(restEndpointDescriptor));
    websocketEndpointDescriptor = new WebsocketEndpointDescriptor();
    websocketEndpointDescriptor.setName("myWebsocketEndpoint");
    exchangeApiDescriptor.setWebsocketEndpoints(List.of(websocketEndpointDescriptor));
    exchangeDescriptor.setApis(List.of(exchangeApiDescriptor));
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
    
    checkCreateRestRequestMethodGeneration(param,
        "/**\n"
        + " * Creates a sample value for the myObjParam field of type MyExchangeMyApiMyRestEndpointRequest using sample value(s) defined in demo configuration properties.\n"
        + " * \n"
        + " * @param properties the configuration properties to use for the sample value generation.\n"
        + " */\n"
        + "public static MyExchangeMyApiMyRestEndpointRequest createMyObjParam(Properties properties) {\n"
        + "  return Optional\n"
        + "    .ofNullable(new MyExchangeMyApiMyRestEndpointRequestDeserializer().deserialize(MyExchangeDemoProperties.MyApi.Rest.MyRestEndpoint.getMyObjParam(properties)))\n"
        + "    .orElse(MyExchangeMyApiMyRestEndpointRequest.builder()  \n"
        + "      .foo(MyExchangeDemoProperties.MyApi.Rest.MyRestEndpoint.MyObjParam.getFoo(properties))\n"
        + "      .hello(MyExchangeDemoProperties.MyApi.Rest.MyRestEndpoint.MyObjParam.getHello(properties))\n"
        + "      .bar(Optional\n"
        + "        .ofNullable(new ListJsonFieldDeserializer<>(new MyExchangeMyApiMyRestEndpointRequestBarDeserializer()).deserialize(MyExchangeDemoProperties.MyApi.Rest.MyRestEndpoint.MyObjParam.getBar(properties)))\n"
        + "        .orElse(List.of(MyExchangeMyApiMyRestEndpointRequestBar.builder()  \n"
        + "          .id(MyExchangeDemoProperties.MyApi.Rest.MyRestEndpoint.MyObjParam.Bar.getId(properties))\n"
        + "          .enabled(MyExchangeDemoProperties.MyApi.Rest.MyRestEndpoint.MyObjParam.Bar.isEnabled(properties))\n"
        + "          .time(MyExchangeDemoProperties.MyApi.Rest.MyRestEndpoint.MyObjParam.Bar.getTime(properties))\n"
        + "          .bestBids(new MapJsonFieldDeserializer<>(new ListJsonFieldDeserializer<>(BigDecimalJsonFieldDeserializer.getInstance())).deserialize(MyExchangeDemoProperties.MyApi.Rest.MyRestEndpoint.MyObjParam.Bar.getBestBids(properties)))\n"
        + "          .build())))\n"
        + "      .build());\n"
        + "}\n"
        + "",  
         List.class,
         Optional.class,
         Properties.class,
         "com.x.gen.MyExchangeDemoProperties",
         "com.x.gen.myapi.deserializers.MyExchangeMyApiMyRestEndpointRequestBarDeserializer",
         "com.x.gen.myapi.deserializers.MyExchangeMyApiMyRestEndpointRequestDeserializer",
         "com.x.gen.myapi.pojo.MyExchangeMyApiMyRestEndpointRequest",
         "com.x.gen.myapi.pojo.MyExchangeMyApiMyRestEndpointRequestBar",
         BigDecimalJsonFieldDeserializer.class,
         ListJsonFieldDeserializer.class,
         MapJsonFieldDeserializer.class);
  }
  
  @Test
  public void testGenerateEndpointParameterCreationMethodPrimitiveIntegerList() {
    Field param = Field.builder()
               .type("INT_LIST")
               .name("myIntListParam")
               .sampleValue(List.of(1,3,5,7))
               .build();
    checkCreateRestRequestMethodGeneration(param,
        "/**\n"
        + " * Creates a sample value for the myIntListParam field of type List<Integer> using sample value(s) defined in demo configuration properties.\n"
        + " * \n"
        + " * @param properties the configuration properties to use for the sample value generation.\n"
        + " */\n"
        + "public static List<Integer> createMyIntListParam(Properties properties) {\n"
        + "  return new ListJsonFieldDeserializer<>(IntegerJsonFieldDeserializer.getInstance()).deserialize(MyExchangeDemoProperties.MyApi.Rest.MyRestEndpoint.getMyIntListParam(properties));\n"
        + "}\n",
           List.class,
           Properties.class,
           "com.x.gen.MyExchangeDemoProperties",
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
    checkCreateRestRequestMethodGeneration(param,
        "/**\n"
        + " * Creates a sample value for the myStringListParam field of type List<String> using sample value(s) defined in demo configuration properties.\n"
        + " * \n"
        + " * @param properties the configuration properties to use for the sample value generation.\n"
        + " */\n"
        + "public static List<String> createMyStringListParam(Properties properties) {\n"
        + "  return new ListJsonFieldDeserializer<>(StringJsonFieldDeserializer.getInstance()).deserialize(MyExchangeDemoProperties.MyApi.Rest.MyRestEndpoint.getMyStringListParam(properties));\n"
        + "}\n",
           List.class,
           Properties.class,
           "com.x.gen.MyExchangeDemoProperties",
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
    checkCreateRestRequestMethodGeneration(param,
        "/**\n"
        + " * Creates a sample value for the myLongListParam field of type List<Long> using sample value(s) defined in demo configuration properties.\n"
        + " * \n"
        + " * @param properties the configuration properties to use for the sample value generation.\n"
        + " */\n"
        + "public static List<Long> createMyLongListParam(Properties properties) {\n"
        + "  return new ListJsonFieldDeserializer<>(LongJsonFieldDeserializer.getInstance()).deserialize(MyExchangeDemoProperties.MyApi.Rest.MyRestEndpoint.getMyLongListParam(properties));\n"
        + "}\n"
        + "",
        List.class, 
        Properties.class,
        "com.x.gen.MyExchangeDemoProperties",
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
    checkCreateRestRequestMethodGeneration(param,
        "/**\n"
        + " * Creates a sample value for the myBigDecimalListParam field of type List<BigDecimal> using sample value(s) defined in demo configuration properties.\n"
        + " * \n"
        + " * @param properties the configuration properties to use for the sample value generation.\n"
        + " */\n"
        + "public static List<BigDecimal> createMyBigDecimalListParam(Properties properties) {\n"
        + "  return new ListJsonFieldDeserializer<>(BigDecimalJsonFieldDeserializer.getInstance()).deserialize(MyExchangeDemoProperties.MyApi.Rest.MyRestEndpoint.getMyBigDecimalListParam(properties));\n"
        + "}\n"
        + "",
        BigDecimal.class,
        List.class,
        Properties.class,
        "com.x.gen.MyExchangeDemoProperties",
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
    checkCreateRestRequestMethodGeneration(param,
        "/**\n"
        + " * Creates a sample value for the myBooleanListParam field of type List<Boolean> using sample value(s) defined in demo configuration properties.\n"
        + " * \n"
        + " * @param properties the configuration properties to use for the sample value generation.\n"
        + " */\n"
        + "public static List<Boolean> createMyBooleanListParam(Properties properties) {\n"
        + "  return new ListJsonFieldDeserializer<>(BooleanJsonFieldDeserializer.getInstance()).deserialize(MyExchangeDemoProperties.MyApi.Rest.MyRestEndpoint.getMyBooleanListParam(properties));\n"
        + "}\n"
        + "",
        List.class,
        Properties.class,
        "com.x.gen.MyExchangeDemoProperties",
        BooleanJsonFieldDeserializer.class, 
        ListJsonFieldDeserializer.class);
  }
  
  @Test
  public void testGenerateEndpointParameterCreationMethodObjectListList() {  
    Field param = Field.builder()
               .type("OBJECT_LIST_LIST")
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
    checkCreateRestRequestMethodGeneration(param,
        "/**\n"
        + " * Creates a sample value for the myObjListParam field of type List<List<MyExchangeMyApiMyRestEndpointRequest>> using sample value(s) defined in demo configuration properties.\n"
        + " * \n"
        + " * @param properties the configuration properties to use for the sample value generation.\n"
        + " */\n"
        + "public static List<List<MyExchangeMyApiMyRestEndpointRequest>> createMyObjListParam(Properties properties) {\n"
        + "  return Optional\n"
        + "    .ofNullable(new ListJsonFieldDeserializer<>(new ListJsonFieldDeserializer<>(new MyExchangeMyApiMyRestEndpointRequestDeserializer())).deserialize(MyExchangeDemoProperties.MyApi.Rest.MyRestEndpoint.getMyObjListParam(properties)))\n"
        + "    .orElse(List.of(List.of(MyExchangeMyApiMyRestEndpointRequest.builder()  \n"
        + "      .foo(MyExchangeDemoProperties.MyApi.Rest.MyRestEndpoint.MyObjListParam.getFoo(properties))\n"
        + "      .hello(MyExchangeDemoProperties.MyApi.Rest.MyRestEndpoint.MyObjListParam.getHello(properties))\n"
        + "      .build())));\n"
        + "}\n"
        + "",
        List.class, 
        Optional.class,
        Properties.class,
        "com.x.gen.MyExchangeDemoProperties",
        "com.x.gen.myapi.deserializers.MyExchangeMyApiMyRestEndpointRequestDeserializer",
        "com.x.gen.myapi.pojo.MyExchangeMyApiMyRestEndpointRequest",
        ListJsonFieldDeserializer.class);
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
               .build();
    checkCreateRestRequestMethodGeneration(param,
        "/**\n"
        + " * Creates a sample value for the myObjMapParam field of type Map<String, MyExchangeMyApiMyRestEndpointRequest> using sample value(s) defined in demo configuration properties.\n"
        + " * \n"
        + " * @param properties the configuration properties to use for the sample value generation.\n"
        + " */\n"
        + "public static Map<String, MyExchangeMyApiMyRestEndpointRequest> createMyObjMapParam(Properties properties) {\n"
        + "  return new MapJsonFieldDeserializer<>(new MyExchangeMyApiMyRestEndpointRequestDeserializer()).deserialize(MyExchangeDemoProperties.MyApi.Rest.MyRestEndpoint.getMyObjMapParam(properties));\n"
        + "}\n"
        + "",
        Map.class, 
        Properties.class,
        "com.x.gen.MyExchangeDemoProperties",
        "com.x.gen.myapi.deserializers.MyExchangeMyApiMyRestEndpointRequestDeserializer",
        "com.x.gen.myapi.pojo.MyExchangeMyApiMyRestEndpointRequest",
        MapJsonFieldDeserializer.class);
  }
  
  @Test
  public void testGenerateEndpointParameterCreationMethodObjectMapListMap() {
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
               .build();
    
    checkCreateRestRequestMethodGeneration(param,
        "/**\n"
        + " * Creates a sample value for the myObjMapListMapParam field of type Map<String, List<Map<String, MyExchangeMyApiMyRestEndpointRequest>>> using sample value(s) defined in demo configuration properties.\n"
        + " * \n"
        + " * @param properties the configuration properties to use for the sample value generation.\n"
        + " */\n"
        + "public static Map<String, List<Map<String, MyExchangeMyApiMyRestEndpointRequest>>> createMyObjMapListMapParam(Properties properties) {\n"
        + "  return new MapJsonFieldDeserializer<>(new ListJsonFieldDeserializer<>(new MapJsonFieldDeserializer<>(new MyExchangeMyApiMyRestEndpointRequestDeserializer()))).deserialize(MyExchangeDemoProperties.MyApi.Rest.MyRestEndpoint.getMyObjMapListMapParam(properties));\n"
        + "}\n"
        + "",
        List.class,
        Map.class, 
        Properties.class,
        "com.x.gen.MyExchangeDemoProperties",
        "com.x.gen.myapi.deserializers.MyExchangeMyApiMyRestEndpointRequestDeserializer",
        "com.x.gen.myapi.pojo.MyExchangeMyApiMyRestEndpointRequest",
        ListJsonFieldDeserializer.class,
        MapJsonFieldDeserializer.class);
  }
  
  @Test
  public void testGenerateEndpointParameterCreationMethodPrimitiveStringParameter() {
    Field param = Field.builder().type(Type.STRING).name("myStringParam").sampleValue("Hello World").build();
    checkCreateRestRequestMethodGeneration(param,
        "/**\n"
        + " * Creates a sample value for the myStringParam field of type String using sample value(s) defined in demo configuration properties.\n"
        + " * \n"
        + " * @param properties the configuration properties to use for the sample value generation.\n"
        + " */\n"
        + "public static String createMyStringParam(Properties properties) {\n"
        + "  return MyExchangeDemoProperties.MyApi.Rest.MyRestEndpoint.getMyStringParam(properties);\n"
        + "}\n"
        + "",
        Properties.class,
        "com.x.gen.MyExchangeDemoProperties");
  }
  
  @Test
  public void  testGenerateEndpointParameterCreationMethodPrimitiveBigDecimalParameter() {
    Field param = Field.builder().type(Type.BIGDECIMAL).name("myBigDecimalParam").sampleValue("123.45").build();
    checkCreateRestRequestMethodGeneration(param,
        "/**\n"
        + " * Creates a sample value for the myBigDecimalParam field of type BigDecimal using sample value(s) defined in demo configuration properties.\n"
        + " * \n"
        + " * @param properties the configuration properties to use for the sample value generation.\n"
        + " */\n"
        + "public static BigDecimal createMyBigDecimalParam(Properties properties) {\n"
        + "  return MyExchangeDemoProperties.MyApi.Rest.MyRestEndpoint.getMyBigDecimalParam(properties);\n"
        + "}\n"
        + "",
        BigDecimal.class, 
        Properties.class,
        "com.x.gen.MyExchangeDemoProperties");
  }
  
  @Test
  public void testGenerateEndpointParameterCreationMethodPrimitiveIntegerParameter() {
    Field param = Field.builder().type(Type.INT).name("myIntParam").sampleValue(123).build();
    checkCreateRestRequestMethodGeneration(param,
        "/**\n"
        + " * Creates a sample value for the myIntParam field of type Integer using sample value(s) defined in demo configuration properties.\n"
        + " * \n"
        + " * @param properties the configuration properties to use for the sample value generation.\n"
        + " */\n"
        + "public static Integer createMyIntParam(Properties properties) {\n"
        + "  return MyExchangeDemoProperties.MyApi.Rest.MyRestEndpoint.getMyIntParam(properties);\n"
        + "}\n"
        + "",
        Properties.class,
        "com.x.gen.MyExchangeDemoProperties");
  }
  
  @Test
  public void  testGenerateEndpointParameterCreationMethodPrimitiveLongParameter() {
    Field param = Field.builder().type(Type.LONG).name("myLongParam").sampleValue(123L).build();
    checkCreateRestRequestMethodGeneration(param,
        "/**\n"
        + " * Creates a sample value for the myLongParam field of type Long using sample value(s) defined in demo configuration properties.\n"
        + " * \n"
        + " * @param properties the configuration properties to use for the sample value generation.\n"
        + " */\n"
        + "public static Long createMyLongParam(Properties properties) {\n"
        + "  return MyExchangeDemoProperties.MyApi.Rest.MyRestEndpoint.getMyLongParam(properties);\n"
        + "}\n"
        + "",
        Properties.class,
        "com.x.gen.MyExchangeDemoProperties");
  }
  
  @Test
  public void  testGenerateEndpointParameterCreationMethodPrimitiveBooleanParameter() {
    Field param = Field.builder().type(Type.BOOLEAN).name("myBooleanParam").sampleValue(true).build();
    checkCreateRestRequestMethodGeneration(param,
        "/**\n"
        + " * Creates a sample value for the myBooleanParam field of type Boolean using sample value(s) defined in demo configuration properties.\n"
        + " * \n"
        + " * @param properties the configuration properties to use for the sample value generation.\n"
        + " */\n"
        + "public static Boolean createMyBooleanParam(Properties properties) {\n"
        + "  return MyExchangeDemoProperties.MyApi.Rest.MyRestEndpoint.isMyBooleanParam(properties);\n"
        + "}\n"
        + "",
        Properties.class,
        "com.x.gen.MyExchangeDemoProperties");
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
    checkCreateRestRequestMethodGeneration(param,
        "/**\n"
        + " * Creates a sample value for the myObjParam field of type MyExchangeMyApiMyRestEndpointRequest using sample value(s) defined in demo configuration properties.\n"
        + " * \n"
        + " * @param properties the configuration properties to use for the sample value generation.\n"
        + " */\n"
        + "public static MyExchangeMyApiMyRestEndpointRequest createMyObjParam(Properties properties) {\n"
        + "  return Optional\n"
        + "    .ofNullable(new MyExchangeMyApiMyRestEndpointRequestDeserializer().deserialize(MyExchangeDemoProperties.MyApi.Rest.MyRestEndpoint.getMyObjParam(properties)))\n"
        + "    .orElse(MyExchangeMyApiMyRestEndpointRequest.builder()  \n"
        + "      .foo(MyExchangeDemoProperties.MyApi.Rest.MyRestEndpoint.MyObjParam.getFoo(properties))\n"
        + "      .hello(MyExchangeDemoProperties.MyApi.Rest.MyRestEndpoint.MyObjParam.getHello(properties))\n"
        + "      .aLong(MyExchangeDemoProperties.MyApi.Rest.MyRestEndpoint.MyObjParam.getALong(properties))\n"
        + "      .bDecimal(MyExchangeDemoProperties.MyApi.Rest.MyRestEndpoint.MyObjParam.getBDecimal(properties))\n"
        + "      .cBool(MyExchangeDemoProperties.MyApi.Rest.MyRestEndpoint.MyObjParam.isCBool(properties))\n"
        + "      .theVoidStr(MyExchangeDemoProperties.MyApi.Rest.MyRestEndpoint.MyObjParam.getTheVoidStr(properties))\n"
        + "      .theVoidList(new ListJsonFieldDeserializer<>(StringJsonFieldDeserializer.getInstance()).deserialize(MyExchangeDemoProperties.MyApi.Rest.MyRestEndpoint.MyObjParam.getTheVoidList(properties)))\n"
        + "      .theVoidMap(new MapJsonFieldDeserializer<>(StringJsonFieldDeserializer.getInstance()).deserialize(MyExchangeDemoProperties.MyApi.Rest.MyRestEndpoint.MyObjParam.getTheVoidMap(properties)))\n"
        + "      .build());\n"
        + "}\n"
        + "",
        Optional.class,
        Properties.class,
        "com.x.gen.MyExchangeDemoProperties",
        "com.x.gen.myapi.deserializers.MyExchangeMyApiMyRestEndpointRequestDeserializer",
        "com.x.gen.myapi.pojo.MyExchangeMyApiMyRestEndpointRequest",
        ListJsonFieldDeserializer.class,
        MapJsonFieldDeserializer.class,
        StringJsonFieldDeserializer.class);
  }
  
  private void checkCreateRestRequestMethodGeneration(Field request, String expectedCode, Object...expectedImports) {
    Imports imports = new Imports();
    restEndpointDescriptor.setRequest(request);
    boolean hasArguments = ExchangeApiGenUtil.restEndpointHasArguments(restEndpointDescriptor, exchangeApiDescriptor);
    String requestClassName = null;
    Type requestDataType = null;
    if (hasArguments) {
      requestDataType =  PojoGenUtil.getFieldType(request);
      if (requestDataType.getCanonicalType().isPrimitive) {
        requestClassName = requestDataType.getCanonicalType().typeClass.getName();
      } else if (requestDataType.isObject() ){
        requestClassName = ExchangeApiGenUtil.generateRestEnpointRequestPojoClassName(
                    exchangeDescriptor, 
                    exchangeApiDescriptor, 
                    restEndpointDescriptor);
      } else {
        requestClassName = Type.getLeafSubType(requestDataType).getCanonicalType().typeClass.getName();
      }
      
    } else {
      requestClassName = null;
    }
    
    String code = EndpointDemoGenUtil.generateFieldCreationMethod(
        request, 
        requestClassName, 
        exchangeDescriptor, 
        exchangeApiDescriptor, 
        EndpointDemoGenUtil.REST_DEMO_GROUP_NAME,
        restEndpointDescriptor.getName(),
        EndpointDemoGenUtil.collectDemoConfigProperties(exchangeDescriptor),
        imports);
    
    Assert.assertEquals(expectedCode, code);
    checkImports(imports,  expectedImports);
  }

  private void checkImports(Imports actualImports, Object...expectedImports) {
    Function<Object, String> toClassName = s -> (s instanceof Class<?>) ? ((Class<?>) s).getName() : s.toString();
    String[] expectedImportsStr = Arrays
        .stream(expectedImports)
        .map(toClassName)
        .toArray(String[]::new);
    
    List<String> actual = actualImports.getAllImports()
        .stream().filter(s -> !s.startsWith("java.lang") && s.contains("."))
        .map(toClassName)
        .collect(Collectors.toList());
    BinaryOperator<String> concat = (s1, s2) -> s1 + "\n" + s2;
    String errMsg = "Unexpected imports, expected:\n" 
            + Arrays.stream(expectedImportsStr)
                .reduce(concat).orElse(null) 
            + "\n actual:\n" 
            + actual.stream().reduce(concat).orElse(null);
    Assert.assertEquals(errMsg, expectedImports.length, actual.size());
    for (int i = 0; i < expectedImports.length; i++) {
      Assert.assertEquals(errMsg, expectedImportsStr[i], actual.get(i));
    }
  }
}
