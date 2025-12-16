package org.jxapi.generator.java.exchange.api.demo;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.jxapi.exchange.descriptor.gen.ConfigPropertyDescriptor;
import org.jxapi.exchange.descriptor.gen.ExchangeApiDescriptor;
import org.jxapi.exchange.descriptor.gen.ExchangeDescriptor;
import org.jxapi.exchange.descriptor.gen.RestEndpointDescriptor;
import org.jxapi.exchange.descriptor.gen.WebsocketEndpointDescriptor;
import org.jxapi.generator.java.Imports;
import org.jxapi.generator.java.exchange.api.ExchangeApiGenUtil;
import org.jxapi.netutils.rest.HttpMethod;
import org.jxapi.pojo.descriptor.Field;
import org.jxapi.pojo.descriptor.Type;

/**
 * Unit test for {@link EndpointDemoGenUtil}, excepts tests for
 * {@link EndpointDemoGenUtil#generateFieldCreationMethod(org.jxapi.pojo.descriptor.Field, String, org.jxapi.exchange.descriptor.ExchangeDescriptor, org.jxapi.exchange.descriptor.ExchangeApiDescriptor, String, org.jxapi.generator.java.Imports)}
 *  that are tested in {@link EndpointDemoGenUtilGenerateRequestCreationMethodTest}.
 */
public class EndpointDemoGenUtilTest {
  
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
        EndpointDemoGenUtil.getNewTestApiInstruction("exchange", "api", simpleApiClassName, "getMyApi"));
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
    restEndpointDescriptor.setHttpMethod(HttpMethod.GET.toString());
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
    restEndpointDescriptor.setHttpMethod(HttpMethod.GET.toString());
    exchangeApiDescriptor.setRestEndpoints(List.of(restEndpointDescriptor));
    List<ConfigPropertyDescriptor> demoGroupProp = EndpointDemoGenUtil.collectDemoConfigProperties(exchangeDescriptor);
    Assert.assertEquals(1, demoGroupProp.size());
    ConfigPropertyDescriptor apiDemoGroupProp = demoGroupProp.get(0);
    checkDemoProperty(apiDemoGroupProp, "myApi", "Configuration properties for myApi API group endpoints demo snippets", null, null);
    Assert.assertEquals(1, apiDemoGroupProp.getProperties().size());
    ConfigPropertyDescriptor restEndpointsGroup = apiDemoGroupProp.getProperties().get(0);
    checkDemoProperty(restEndpointsGroup, "rest", "Configuration properties for REST endpoints demo snippets of myApi API group", null, null);
    Assert.assertEquals(1, restEndpointsGroup.getProperties().size());
    ConfigPropertyDescriptor endpointGroup = restEndpointsGroup.getProperties().get(0);
    checkDemoProperty(endpointGroup, 
        "myRestEndpoint", 
        "Configuration properties for REST myRestEndpoint endpoint of myApi API group", 
        null, 
        null);
    Assert.assertEquals(1, endpointGroup.getProperties().size());
    ConfigPropertyDescriptor endpointRequestProp = endpointGroup.getProperties().get(0);
    checkDemoProperty(endpointRequestProp, "request", 
        "Demo configuration property for myRestEndpoint.request field.<p>\n"
        + "This is a test parameter", 
        Type.INT, 
        123);
  }
  
  @Test
  public void testCollectDemoConfigProperties_RestEndpointWithBodyAndNoRequest() {
    ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
    exchangeDescriptor.setId("myExchange");
    exchangeDescriptor.setBasePackage("com.x.y.z.gen");
    ExchangeApiDescriptor exchangeApiDescriptor = new ExchangeApiDescriptor();
    exchangeApiDescriptor.setName("myApi");
    exchangeDescriptor.setApis(List.of(exchangeApiDescriptor));
    RestEndpointDescriptor restEndpointDescriptor = new RestEndpointDescriptor();
    restEndpointDescriptor.setName("myRestEndpoint");
    restEndpointDescriptor.setHttpMethod(HttpMethod.PUT.toString());
    exchangeApiDescriptor.setRestEndpoints(List.of(restEndpointDescriptor));
    List<ConfigPropertyDescriptor> demoGroupProp = EndpointDemoGenUtil.collectDemoConfigProperties(exchangeDescriptor);
    Assert.assertEquals(1, demoGroupProp.size());
    ConfigPropertyDescriptor apiDemoGroupProp = demoGroupProp.get(0);
    checkDemoProperty(apiDemoGroupProp, "myApi", "Configuration properties for myApi API group endpoints demo snippets", null, null);
    Assert.assertEquals(1, apiDemoGroupProp.getProperties().size());
    ConfigPropertyDescriptor restEndpointsGroup = apiDemoGroupProp.getProperties().get(0);
    checkDemoProperty(restEndpointsGroup, "rest", "Configuration properties for REST endpoints demo snippets of myApi API group", null, null);
    Assert.assertEquals(1, restEndpointsGroup.getProperties().size());
    ConfigPropertyDescriptor endpointGroup = restEndpointsGroup.getProperties().get(0);
    checkDemoProperty(endpointGroup, 
        "myRestEndpoint", 
        "Configuration properties for REST myRestEndpoint endpoint of myApi API group", 
        null, 
        null);
    Assert.assertEquals(1, endpointGroup.getProperties().size());
    ConfigPropertyDescriptor endpointRequestProp = endpointGroup.getProperties().get(0);
    checkDemoProperty(endpointRequestProp, ExchangeApiGenUtil.RAW_BODY_REST_REQUEST_ARG_NAME, 
        "Demo configuration property for myRestEndpoint.body field.<p>\n"
        + "Raw body request field.", 
        Type.STRING, 
        null);
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
        null, 
        null);
    Assert.assertEquals(1, apiDemoGroupProp.getProperties().size());
    ConfigPropertyDescriptor restEndpointsGroup = apiDemoGroupProp.getProperties().get(0);
    checkDemoProperty(restEndpointsGroup, 
        "ws", 
        "Configuration properties for websocket endpoints demo snippets of myApi API group", 
        null, 
        null);
    Assert.assertEquals(1, restEndpointsGroup.getProperties().size());
    ConfigPropertyDescriptor endpointGroup = restEndpointsGroup.getProperties().get(0);
    checkDemoProperty(endpointGroup, 
        "myRestEndpoint", 
        "Configuration properties for websocket myRestEndpoint endpoint of myApi API group", 
        null, 
        null);
    Assert.assertEquals(1, endpointGroup.getProperties().size());
    ConfigPropertyDescriptor endpointRequestProp = endpointGroup.getProperties().get(0);
    checkDemoProperty(endpointRequestProp, 
        "myStringParam", 
        "Demo configuration property for myRestEndpoint.myStringParam field.<p>\n"
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
    restEndpointDescriptor.setHttpMethod(HttpMethod.POST.toString());
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
        null, 
        null);
    Assert.assertEquals(2, apiDemoGroupProp.getProperties().size());
    
    // REST endpoints group
    checkRestEndpointGroup(apiDemoGroupProp.getProperties().get(0));
    
    // Websocket endpoints group
    checkWsEndpointGroup(apiDemoGroupProp.getProperties().get(1));
  }
  
  private void checkRestEndpointGroup(ConfigPropertyDescriptor restEndpointsGroup) {
    Assert.assertEquals(1, restEndpointsGroup.getProperties().size());
    checkDemoProperty(restEndpointsGroup, 
        "rest", 
        "Configuration properties for REST endpoints demo snippets of myApi API group", 
        null, 
        null);
    Assert.assertEquals(1, restEndpointsGroup.getProperties().size());
    ConfigPropertyDescriptor restEndpointGroup = restEndpointsGroup.getProperties().get(0);
    checkDemoProperty(restEndpointGroup, 
        "myRestEndpoint", "Configuration properties for REST myRestEndpoint endpoint of myApi API group", 
        null, 
        null);
    Assert.assertEquals(2, restEndpointGroup.getProperties().size());
    ConfigPropertyDescriptor rawRequestProp = restEndpointGroup.getProperties().get(0);
    checkDemoProperty(rawRequestProp, 
        "request", 
        "Demo configuration property for myRestEndpoint.request field as raw JSON string value.<p>\n"
        + "This is a test parameter", 
        Type.STRING, 
        null);
    
    ConfigPropertyDescriptor requestGroupProp = restEndpointGroup.getProperties().get(1);
    checkDemoProperty(requestGroupProp, 
        "request", 
        "Demo configuration properties for myRestEndpoint.request field object instance.<p>\n"
        + "This is a test parameter", 
        null, 
        null);
    Assert.assertEquals(3, requestGroupProp.getProperties().size());
    checkDemoProperty(requestGroupProp.getProperties().get(0), 
        "myIntParam",
        "Demo configuration property for request.myIntParam field.", 
        Type.INT, 
        123);
    
    ConfigPropertyDescriptor rawMySubParamProp = requestGroupProp.getProperties().get(1);
    checkDemoProperty(rawMySubParamProp, 
        "mySubParam", 
        "Demo configuration property for request.mySubParam field as raw JSON string value.", 
        Type.STRING, 
        null);
    
    ConfigPropertyDescriptor mySubParam = requestGroupProp.getProperties().get(2);
    checkDemoProperty(mySubParam, 
        "mySubParam",
        "Demo configuration properties for request.mySubParam field object instance.", 
        null, 
        null);
    Assert.assertEquals(1, mySubParam.getProperties().size());
    ConfigPropertyDescriptor helloProp = mySubParam.getProperties().get(0);
    checkDemoProperty(helloProp, 
        "hello",
        "Demo configuration property for mySubParam.hello field.", 
        Type.STRING, 
        "Hello World");
  }
  
  private void checkWsEndpointGroup(ConfigPropertyDescriptor wsEndpointsGroup) {
    checkDemoProperty(wsEndpointsGroup, 
        "ws", 
        "Configuration properties for websocket endpoints demo snippets of myApi API group",
        null, 
        null);
    
    ConfigPropertyDescriptor wsEndpointGroup = wsEndpointsGroup.getProperties().get(0);
    checkDemoProperty(wsEndpointGroup, 
        "myWsEndpoint", 
        "Configuration properties for websocket myWsEndpoint endpoint of myApi API group", 
        null, 
        null);
    Assert.assertEquals(2, wsEndpointGroup.getProperties().size());
    
    ConfigPropertyDescriptor rawWsRequestProp = wsEndpointGroup.getProperties().get(0);
    checkDemoProperty(rawWsRequestProp, 
        "mySubscribeRequest",
        "Demo configuration property for myWsEndpoint.mySubscribeRequest field as raw JSON string value.<p>\n"
            + "This is a test parameter for the mySubscribeRequest field",
        Type.STRING, 
        null);
    
    ConfigPropertyDescriptor wsRequestProp = wsEndpointGroup.getProperties().get(1);
    checkDemoProperty(wsRequestProp, 
        "mySubscribeRequest", 
        "Demo configuration properties for myWsEndpoint.mySubscribeRequest field object instance.<p>\n"
        + "This is a test parameter for the mySubscribeRequest field", 
        null, 
        null);
    Assert.assertEquals(3, wsRequestProp.getProperties().size());
    checkDemoProperty(wsRequestProp.getProperties().get(0), 
        "myIntParam",
        "Demo configuration property for mySubscribeRequest.myIntParam field.", 
        Type.INT, 
        123);
    
    ConfigPropertyDescriptor rawWsMySubParam = wsRequestProp.getProperties().get(1);
    checkDemoProperty(rawWsMySubParam, 
        "mySubParam",
        "Demo configuration property for mySubscribeRequest.mySubParam field as raw JSON string value.", 
        Type.STRING,
        null);
    
    ConfigPropertyDescriptor wsMySubParam = wsRequestProp.getProperties().get(2);
    checkDemoProperty(wsMySubParam, 
        "mySubParam",
        "Demo configuration properties for mySubscribeRequest.mySubParam field object instance.", 
        null, 
        null);
    Assert.assertEquals(1, wsMySubParam.getProperties().size());
    checkDemoProperty(wsMySubParam.getProperties().get(0), 
        "hello",
        "Demo configuration property for mySubParam.hello field.", 
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
    if (type == null) {
      Assert.assertNull("Expected null type for prop:" + name, prop.getType());
    } else {
      Assert.assertEquals("Invalid type for prop:" + name, type.toString(), prop.getType());
    }
    Assert.assertEquals("Invalid default value for prop:" + name, sampleValue, prop.getDefaultValue());
  }
}
