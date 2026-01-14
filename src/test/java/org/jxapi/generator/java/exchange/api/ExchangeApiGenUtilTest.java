package org.jxapi.generator.java.exchange.api;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.jxapi.exchange.descriptor.gen.ConfigPropertyDescriptor;
import org.jxapi.exchange.descriptor.gen.ConstantDescriptor;
import org.jxapi.exchange.descriptor.gen.ExchangeApiDescriptor;
import org.jxapi.exchange.descriptor.gen.ExchangeDescriptor;
import org.jxapi.exchange.descriptor.gen.RestEndpointDescriptor;
import org.jxapi.exchange.descriptor.gen.WebsocketEndpointDescriptor;
import org.jxapi.exchange.descriptor.gen.WebsocketTopicMatcherDescriptor;
import org.jxapi.generator.java.Imports;
import org.jxapi.generator.java.JavaCodeGenUtil;
import org.jxapi.netutils.rest.HttpMethod;
import org.jxapi.netutils.websocket.multiplexing.WSMTMFUtil;
import org.jxapi.netutils.websocket.multiplexing.WebsocketMessageTopicMatcherFactory;
import org.jxapi.pojo.descriptor.Field;
import org.jxapi.pojo.descriptor.Type;
import org.jxapi.util.EncodingUtil;
import org.jxapi.util.JsonUtil;
import org.jxapi.util.PlaceHolderResolver;

/**
 * Unit test for {@link ExchangeApiGenUtil}
 */
public class ExchangeApiGenUtilTest {

    @Test
    public void testGenerateEndpointRequestPojoClassName() {
        ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
        exchangeDescriptor.setId("TestExchange");
        exchangeDescriptor.setBasePackage("com.test.exchange");
        ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
        apiDescriptor.setName("MyApi");
        exchangeDescriptor.setApis(List.of(apiDescriptor));
        RestEndpointDescriptor endpointDescriptor = new RestEndpointDescriptor();
        endpointDescriptor.setName("GetAccount");
        Field request = new Field();
        request.setType(Type.OBJECT);
        endpointDescriptor.setRequest(request);
        apiDescriptor.setRestEndpoints(List.of(endpointDescriptor));
        Assert.assertEquals("com.test.exchange.myapi.pojo.TestExchangeMyApiGetAccountRequest", 
                  ExchangeApiGenUtil.generateRestEnpointRequestPojoClassName(
                      exchangeDescriptor, 
                      apiDescriptor, 
                      endpointDescriptor));
    }

    @Test
    public void testGenerateEndpointRequesPojotClassName_RequestWithObjectName() {
        ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
        exchangeDescriptor.setId("TestExchange");
        exchangeDescriptor.setBasePackage("com.test.exchange");
        ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
        apiDescriptor.setName("MyApi");
        exchangeDescriptor.setApis(List.of(apiDescriptor));
        RestEndpointDescriptor endpointDescriptor = new RestEndpointDescriptor();
        endpointDescriptor.setName("GetAccount");
        Field request = new Field();
        request.setObjectName("MyRequest");
        request.setType(Type.OBJECT);
        endpointDescriptor.setRequest(request);
        apiDescriptor.setRestEndpoints(List.of(endpointDescriptor));
        Assert.assertEquals("com.test.exchange.myapi.pojo.MyRequest", 
                  ExchangeApiGenUtil.generateRestEnpointRequestPojoClassName(
                      exchangeDescriptor, 
                      apiDescriptor, 
                      endpointDescriptor));
    }

    @Test
    public void testGenerateRestEndpointRequestPojoClassName_NullRequestType() {
        ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
        exchangeDescriptor.setId("TestExchange");
        exchangeDescriptor.setBasePackage("com.test.exchange");
        ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
        apiDescriptor.setName("MyApi");
        exchangeDescriptor.setApis(List.of(apiDescriptor));
        RestEndpointDescriptor endpointDescriptor = new RestEndpointDescriptor();
        endpointDescriptor.setName("GetAccount");
        Field request = new Field();
        endpointDescriptor.setRequest(request);
        apiDescriptor.setRestEndpoints(List.of(endpointDescriptor));
        Assert.assertEquals("com.test.exchange.myapi.pojo.TestExchangeMyApiGetAccountRequest", 
                  ExchangeApiGenUtil.generateRestEnpointRequestPojoClassName(
                      exchangeDescriptor, 
                      apiDescriptor, 
                      endpointDescriptor));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGenerateRestEndpointRequestPojoClassName_RequestNotObjectType() {
        ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
        exchangeDescriptor.setId("TestExchange");
        exchangeDescriptor.setBasePackage("com.test.exchange");
        ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
        apiDescriptor.setName("MyApi");
        exchangeDescriptor.setApis(List.of(apiDescriptor));
        RestEndpointDescriptor endpointDescriptor = new RestEndpointDescriptor();
        endpointDescriptor.setName("GetAccount");
        Field request = new Field();
        request.setObjectName("MyRequest");
        request.setType(Type.STRING);
        endpointDescriptor.setRequest(request);
        apiDescriptor.setRestEndpoints(List.of(endpointDescriptor));
        ExchangeApiGenUtil.generateRestEnpointRequestPojoClassName(
            exchangeDescriptor, 
            apiDescriptor, 
            endpointDescriptor);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGenerateRestEndpointRequestPojoClassName_NullRequest() {
        ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
        exchangeDescriptor.setId("TestExchange");
        exchangeDescriptor.setBasePackage("com.test.exchange");
        ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
        apiDescriptor.setName("MyApi");
        exchangeDescriptor.setApis(List.of(apiDescriptor));
        RestEndpointDescriptor endpointDescriptor = new RestEndpointDescriptor();
        endpointDescriptor.setName("GetAccount");
        ExchangeApiGenUtil.generateRestEnpointRequestPojoClassName(
            exchangeDescriptor, 
            apiDescriptor, 
            endpointDescriptor);
    }

    @Test
    public void testGenerateRestEndpointResponsePojoClassName_RawObjectResponseType() {
        ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
        exchangeDescriptor.setId("TestExchange");
        exchangeDescriptor.setBasePackage("com.test.exchange");
        ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
        apiDescriptor.setName("MyApi");
        exchangeDescriptor.setApis(List.of(apiDescriptor));
        RestEndpointDescriptor endpointDescriptor = new RestEndpointDescriptor();
        endpointDescriptor.setName("GetAccount");
        Field response = new Field();
        response.setObjectName(Object.class.getName());
        endpointDescriptor.setResponse(response);
        apiDescriptor.setRestEndpoints(List.of(endpointDescriptor));
        Assert.assertEquals(Object.class.getName(),
                  ExchangeApiGenUtil.generateRestEnpointResponsePojoClassName(
                      exchangeDescriptor, 
                      apiDescriptor, 
                      endpointDescriptor));
    }
    
    @Test
    public void testGenerateRestEndpointResponsePojoClassName() {
        ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
        exchangeDescriptor.setId("TestExchange");
        exchangeDescriptor.setBasePackage("com.test.exchange");
        ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
        apiDescriptor.setName("MyApi");
        exchangeDescriptor.setApis(List.of(apiDescriptor));
        RestEndpointDescriptor endpointDescriptor = new RestEndpointDescriptor();
        endpointDescriptor.setName("GetAccount");
        Field response = new Field();
        response.setType(Type.OBJECT);
        endpointDescriptor.setResponse(response);
        apiDescriptor.setRestEndpoints(List.of(endpointDescriptor));
        Assert.assertEquals("com.test.exchange.myapi.pojo.TestExchangeMyApiGetAccountResponse", 
                  ExchangeApiGenUtil.generateRestEnpointResponsePojoClassName(
                      exchangeDescriptor, 
                      apiDescriptor, 
                      endpointDescriptor));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGenerateRestEndpointResponsePojoClassName_NullResponse() {
        ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
        exchangeDescriptor.setId("TestExchange");
        exchangeDescriptor.setBasePackage("com.test.exchange");
        ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
        apiDescriptor.setName("MyApi");
        exchangeDescriptor.setApis(List.of(apiDescriptor));
        RestEndpointDescriptor endpointDescriptor = new RestEndpointDescriptor();
        endpointDescriptor.setName("GetAccount");
        ExchangeApiGenUtil.generateRestEnpointResponsePojoClassName(
                  exchangeDescriptor, 
                  apiDescriptor, 
                  endpointDescriptor);
    }

    @Test
    public void testGenerateWebsocketEndpointRequestPojoClassName() {
        ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
        exchangeDescriptor.setId("TestExchange");
        exchangeDescriptor.setBasePackage("com.test.exchange");
        ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
        apiDescriptor.setName("MyApi");
        exchangeDescriptor.setApis(List.of(apiDescriptor));
        WebsocketEndpointDescriptor endpointDescriptor = new WebsocketEndpointDescriptor();
        endpointDescriptor.setName("accountWs");
        Field request = new Field();
        request.setType(Type.OBJECT);
        endpointDescriptor.setRequest(request);
        apiDescriptor.setWebsocketEndpoints(List.of(endpointDescriptor));
        Assert.assertEquals("com.test.exchange.myapi.pojo.TestExchangeMyApiAccountWsRequest", 
                  ExchangeApiGenUtil.generateWebsocketEndpointRequestPojoClassName(
                      exchangeDescriptor, 
                      apiDescriptor, 
                      endpointDescriptor));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGenerateWebsocketEndpointRequestPojoClassName_NullRequest() {
        ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
        exchangeDescriptor.setId("TestExchange");
        exchangeDescriptor.setBasePackage("com.test.exchange");
        ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
        apiDescriptor.setName("MyApi");
        exchangeDescriptor.setApis(List.of(apiDescriptor));
        WebsocketEndpointDescriptor endpointDescriptor = new WebsocketEndpointDescriptor();
        endpointDescriptor.setName("accountWs");
        ExchangeApiGenUtil.generateWebsocketEndpointRequestPojoClassName(
        exchangeDescriptor, 
        apiDescriptor, 
        endpointDescriptor);
    }

    @Test
    public void testGenerateWebsocketEndpointMessageClassName() {
        ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
        exchangeDescriptor.setId("TestExchange");
        exchangeDescriptor.setBasePackage("com.test.exchange");
        ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
        apiDescriptor.setName("MyApi");
        exchangeDescriptor.setApis(List.of(apiDescriptor));
        WebsocketEndpointDescriptor endpointDescriptor = new WebsocketEndpointDescriptor();
        endpointDescriptor.setName("accountWs");
        Field message = new Field();
        message.setType(Type.OBJECT);
        endpointDescriptor.setMessage(message);
        apiDescriptor.setWebsocketEndpoints(List.of(endpointDescriptor));
        Assert.assertEquals("com.test.exchange.myapi.pojo.TestExchangeMyApiAccountWsMessage", 
                  ExchangeApiGenUtil.generateWebsocketEndpointMessagePojoClassName(
                      exchangeDescriptor, 
                      apiDescriptor, 
                      endpointDescriptor));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGenerateWebsocketEndpointMessageClassName_NullResponse() {
        ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
        exchangeDescriptor.setId("TestExchange");
        exchangeDescriptor.setBasePackage("com.test.exchange");
        ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
        apiDescriptor.setName("MyApi");
        exchangeDescriptor.setApis(List.of(apiDescriptor));
        WebsocketEndpointDescriptor endpointDescriptor = new WebsocketEndpointDescriptor();
        endpointDescriptor.setName("accountWs");
        ExchangeApiGenUtil.generateWebsocketEndpointMessagePojoClassName(
        exchangeDescriptor, 
        apiDescriptor, 
        endpointDescriptor);
    }

    @Test
    public void testGetWebsocketSubscribeMethodName() {
        WebsocketEndpointDescriptor endpointDescriptor = new WebsocketEndpointDescriptor();
        endpointDescriptor.setName("accountWs");
        Assert.assertEquals("subscribeAccountWs", 
                  ExchangeApiGenUtil.getWebsocketSubscribeMethodName(endpointDescriptor, List.of(endpointDescriptor)));
        WebsocketEndpointDescriptor endpointDescriptorSameNameButFirstLetterUpper = new WebsocketEndpointDescriptor();
        endpointDescriptorSameNameButFirstLetterUpper.setName("AccountWs");
        Assert.assertEquals("subscribeaccountWs", 
            ExchangeApiGenUtil.getWebsocketSubscribeMethodName(endpointDescriptor, List.of(endpointDescriptor, endpointDescriptorSameNameButFirstLetterUpper)));
    }

    @Test
    public void testGetWebsocketUnsubscribeMethodName() {
        WebsocketEndpointDescriptor endpointDescriptor = new WebsocketEndpointDescriptor();
        endpointDescriptor.setName("accountWs");
        Assert.assertEquals("unsubscribeAccountWs", 
                  ExchangeApiGenUtil.getWebsocketUnsubscribeMethodName(endpointDescriptor, List.of(endpointDescriptor)));
        WebsocketEndpointDescriptor endpointDescriptorSameNameButFirstLetterUpper = new WebsocketEndpointDescriptor();
        endpointDescriptorSameNameButFirstLetterUpper.setName("AccountWs");
        Assert.assertEquals("unsubscribeaccountWs", ExchangeApiGenUtil.getWebsocketUnsubscribeMethodName(
            endpointDescriptor, List.of(endpointDescriptor, endpointDescriptorSameNameButFirstLetterUpper)));
    }
    
    @Test
    public void testRestEndpointHasResponse_NullResponse() {
        ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
        apiDescriptor.setName("MyApi");
        RestEndpointDescriptor endpointDescriptor = new RestEndpointDescriptor();
        endpointDescriptor.setName("GetAccount");
        Assert.assertFalse(ExchangeApiGenUtil.restEndpointHasResponse(endpointDescriptor, apiDescriptor));
    }
    
    @Test
    public void testRestEndpointHasResponse_PrimitiveTypeResponse() {
        ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
        apiDescriptor.setName("MyApi");
        RestEndpointDescriptor endpointDescriptor = new RestEndpointDescriptor();
        endpointDescriptor.setName("GetAccount");
        Field response = new Field();
        response.setType(Type.INT);
        endpointDescriptor.setResponse(response);
        Assert.assertTrue(ExchangeApiGenUtil.restEndpointHasResponse(endpointDescriptor, apiDescriptor));
    }
    
    @Test
    public void testRestEndpointHasResponse_ObjectResponseTypeWithNoParameters() {
        ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
        apiDescriptor.setName("MyApi");
        RestEndpointDescriptor endpointDescriptor = new RestEndpointDescriptor();
        endpointDescriptor.setName("GetAccount");
        Field response = new Field();
        response.setType(Type.OBJECT);
        endpointDescriptor.setResponse(response);
        Assert.assertFalse(ExchangeApiGenUtil.restEndpointHasResponse(endpointDescriptor, apiDescriptor));
    }
    
    @Test
    public void testRestEndpointHasResponse_ObjectResponseTypeWithParameters() {
        ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
        apiDescriptor.setName("MyApi");
        RestEndpointDescriptor endpointDescriptor = new RestEndpointDescriptor();
        endpointDescriptor.setName("GetAccount");
        Field response = new Field();
        Field objectParam = new Field();
        response.setProperties(List.of(objectParam));
        endpointDescriptor.setResponse(response);
        Assert.assertTrue(ExchangeApiGenUtil.restEndpointHasResponse(endpointDescriptor, apiDescriptor));
    }
    
    @Test
    public void testRestEndpointHasResponse_RawObjectResponseType() {
        ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
        apiDescriptor.setName("MyApi");
        RestEndpointDescriptor endpointDescriptor = new RestEndpointDescriptor();
        endpointDescriptor.setName("GetAccount");
        Field response = new Field();
        response.setType(Type.OBJECT);
        response.setObjectName(Object.class.getName());
        endpointDescriptor.setResponse(response);
        Assert.assertTrue(ExchangeApiGenUtil.restEndpointHasResponse(endpointDescriptor, apiDescriptor));
    }
    
    @Test
    public void testRestEndpointHasResponse_ExternalObjectResponseType() {
        ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
        apiDescriptor.setName("MyApi");
        RestEndpointDescriptor endpointDescriptor = new RestEndpointDescriptor();
        endpointDescriptor.setName("GetAccount");
        Field response = new Field();
        response.setType(Type.OBJECT);
        response.setObjectName("com.external.package.ExternalPojo");
        endpointDescriptor.setResponse(response);
        Assert.assertTrue(ExchangeApiGenUtil.restEndpointHasResponse(endpointDescriptor, apiDescriptor));
    }
    
    @Test
    public void testRestEnpointHasArguments_NullRequest() {
        ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
        exchangeDescriptor.setId("TestExchange");
        exchangeDescriptor.setBasePackage("com.test.exchange");
        ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
        apiDescriptor.setName("MyApi");
        exchangeDescriptor.setApis(List.of(apiDescriptor));
        RestEndpointDescriptor endpointDescriptor = new RestEndpointDescriptor();
        endpointDescriptor.setName("GetAccount");
        Assert.assertFalse(ExchangeApiGenUtil.restEndpointHasArguments(endpointDescriptor, apiDescriptor));
    }
    
    @Test
    public void testRestEnpointHasArguments_HasRequestWithArguments() {
        ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
        exchangeDescriptor.setId("TestExchange");
        exchangeDescriptor.setBasePackage("com.test.exchange");
        ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
        apiDescriptor.setName("MyApi");
        exchangeDescriptor.setApis(List.of(apiDescriptor));
        RestEndpointDescriptor endpointDescriptor = new RestEndpointDescriptor();
        endpointDescriptor.setName("GetAccount");
        Field request = new Field();
        request.setType(Type.INT);
        endpointDescriptor.setRequest(request);
        Assert.assertTrue(ExchangeApiGenUtil.restEndpointHasArguments(endpointDescriptor, apiDescriptor));
    }
    
    @Test
    public void testWebsocketEnpointHasArguments_NullRequest() {
        ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
        exchangeDescriptor.setId("TestExchange");
        exchangeDescriptor.setBasePackage("com.test.exchange");
        ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
        apiDescriptor.setName("MyApi");
        exchangeDescriptor.setApis(List.of(apiDescriptor));
        WebsocketEndpointDescriptor endpointDescriptor = new WebsocketEndpointDescriptor();
        endpointDescriptor.setName("GetAccount");
        Assert.assertFalse(ExchangeApiGenUtil.websocketEndpointHasArguments(endpointDescriptor, apiDescriptor));
    }
    
  @Test
  public void testWebsocketEnpointHasArguments_HasRequestWithArgument() {
    ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
    apiDescriptor.setName("MyApi");
    WebsocketEndpointDescriptor endpointDescriptor = new WebsocketEndpointDescriptor();
    endpointDescriptor.setName("GetAccount");
    Field request = new Field();
    request.setType(Type.STRING);
    endpointDescriptor.setRequest(request);
    Assert.assertTrue(ExchangeApiGenUtil.websocketEndpointHasArguments(endpointDescriptor, apiDescriptor));
  }
    
    @Test
    public void testEnpointHasArguments_NullType() {
        Assert.assertTrue(ExchangeApiGenUtil.endpointHasArguments(new Field(), null));
    }
    
    @Test
    public void testEnpointHasArguments_NullField() {
        Assert.assertFalse(ExchangeApiGenUtil.endpointHasArguments(null, null));
    }
    
    @Test
    public void testEnpointHasArguments_PrimitiveType() {
      Field f = new Field();
      f.setType(Type.INT);
        Assert.assertTrue(ExchangeApiGenUtil.endpointHasArguments(f, null));
    }
    
    @Test
    public void testEnpointHasArguments_ObjectTypeWithArgs() {
        ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
        apiDescriptor.setName("MyApi");
        Field f = Field.builder()
                 .type(Type.OBJECT)
                 .name("foo")
                 .properties(List.of(new Field()))
                 .build();
        Assert.assertTrue(ExchangeApiGenUtil.endpointHasArguments(f, apiDescriptor));
    }
    
    @Test
    public void testEnpointHasArguments_ObjectTypeWithZeroArgs() {
        ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
        apiDescriptor.setName("MyApi");
        Field f = Field.builder()
                 .type(Type.OBJECT)
                 .name("foo")
                 .properties(List.of())
                 .build();
        Assert.assertFalse(ExchangeApiGenUtil.endpointHasArguments(f, apiDescriptor));
    }
    
    @Test
    public void testGetRequestArgName_Null() {
      Assert.assertEquals(ExchangeApiGenUtil.DEFAULT_REQUEST_ARG_NAME,  ExchangeApiGenUtil.getRequestArgName(null));
    }
    
    @Test
    public void testGetRequestArgName_NotNull() {
      String argName = "myRequestArg";
      Assert.assertEquals(argName,  ExchangeApiGenUtil.getRequestArgName(argName));
    }
    
    @Test
    public void testResolveFieldProperties_NullField() {
      Assert.assertNull(ExchangeApiGenUtil.resolveFieldProperties(null, null));
    }
    
    @Test
    public void testResolveFieldProperties_NullFieldType() {
      Field f = new Field();
      f.setName("foo");
      Assert.assertEquals(f, ExchangeApiGenUtil.resolveFieldProperties(null, f));
    }
    
    @Test
    public void testResolveFieldProperties_PrimitiveFieldType() {
      Field f = new Field();
      f.setName("foo");
      f.setType(Type.INT);
      Assert.assertEquals(f, ExchangeApiGenUtil.resolveFieldProperties(null, f));
    }
    
    @Test
    public void testResolveFieldProperties_ExternalClassObjectNameFieldType() {
      Field f = new Field();
      f.setName("foo");
      f.setObjectName("com.external.package.ExternalPojo");
      Assert.assertEquals(f, ExchangeApiGenUtil.resolveFieldProperties(null, f));
    }
    
    @Test
    public void testResolveFieldProperties_NullFieldTypeAndNullParametersButObjectNameResolvedInAnotherApi() {
    ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
    apiDescriptor.setName("MyApi");
    String objectName = "MyPojo";
      Field f = new Field();
      f.setName("foo");
      f.setObjectName(objectName);
        RestEndpointDescriptor endpointDescriptor = new RestEndpointDescriptor();
        endpointDescriptor.setName("GetAccount");
        endpointDescriptor.setRequest(f);
      
        RestEndpointDescriptor otherEndpointDescriptor = new RestEndpointDescriptor();
        otherEndpointDescriptor.setName("GetAccountV2");
        Field otherRequest = new Field();
        otherRequest.setName("bar");
        otherRequest.setType(Type.OBJECT);
        otherRequest.setObjectName(objectName);
        Field otherRequestProperty = new Field();
        otherRequestProperty.setName("toto");
        otherRequestProperty.setType(Type.STRING);
        otherEndpointDescriptor.setRequest(otherRequest);
        otherRequest.setProperties(List.of(otherRequestProperty));
        apiDescriptor.setRestEndpoints(List.of(endpointDescriptor, otherEndpointDescriptor));
        
        Field expected = f.deepClone();
        expected.setProperties(otherRequest.getProperties());
        expected.setType(Type.OBJECT);
        Assert.assertEquals(expected, ExchangeApiGenUtil.resolveFieldProperties(apiDescriptor, f));
    }
    
    @Test
    public void testResolveFieldProperties_ObjectFieldTypeWithNullObjectNameAndNullParameters() {
    ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
    apiDescriptor.setName("MyApi");
      Field f = new Field();
      f.setName("foo");
      f.setType(Type.OBJECT);
      Assert.assertEquals(f, ExchangeApiGenUtil.resolveFieldProperties(apiDescriptor, f));
    }
    
    @Test
    public void testResolveFieldProperties_ObjectFieldTypeObjectNameAndDefinedProperties() {
    ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
    apiDescriptor.setName("MyApi");
      Field f = new Field();
      f.setName("foo");
      f.setType(Type.OBJECT);
      Field prop = new Field();
      prop.setName("bar");
      prop.setType(Type.STRING);
      f.setObjectName("MyPojo");
      f.setProperties(List.of(prop));
      Assert.assertEquals(f, ExchangeApiGenUtil.resolveFieldProperties(apiDescriptor, f));
    }
    
    @Test
    public void testGetFieldPropertiesCount_NullField() {
      Assert.assertEquals(0, ExchangeApiGenUtil.getFieldPropertiesCount(null, null));
    }
    
    @Test
    public void testGetFieldPropertiesCount_PrimitiveField() {
      Field f = new Field();
      f.setType(Type.INT);
      Assert.assertEquals(0, ExchangeApiGenUtil.getFieldPropertiesCount(f, null));
    }
    
    @Test
    public void testGetFieldPropertiesCountNullParametersObjectField() {
      Field f = new Field();
      f.setType(Type.OBJECT);
      Assert.assertEquals(0, ExchangeApiGenUtil.getFieldPropertiesCount(f, null));
    }
    
    @Test
    public void testGetFieldPropertiesCount_2ParametersObjectField() {
      Field f = new Field();
      f.setName("foo");
      f.setType(Type.OBJECT);
      Field prop1 = new Field();
      prop1.setName("bar");
      prop1.setType(Type.STRING);
      Field prop2 = new Field();
      prop2.setName("hello");
      prop2.setType(Type.STRING);
      f.setProperties(List.of(prop1, prop2));
      Assert.assertEquals(2, ExchangeApiGenUtil.getFieldPropertiesCount(f, null));
    }
    
    @Test
    public void testGetFieldPropertiesCount_NullFieldTypeAndNullParametersButObjectNameResolvedInAnotherApi() {
    ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
    apiDescriptor.setName("MyApi");
    String objectName = "MyPojo";
      Field f = new Field();
      f.setName("foo");
      f.setObjectName(objectName);
        RestEndpointDescriptor endpointDescriptor = new RestEndpointDescriptor();
        endpointDescriptor.setName("GetAccount");
        endpointDescriptor.setRequest(f);
      
        RestEndpointDescriptor otherEndpointDescriptor = new RestEndpointDescriptor();
        otherEndpointDescriptor.setName("GetAccountV2");
        Field otherRequest = new Field();
        otherRequest.setName("bar");
        otherRequest.setType(Type.OBJECT);
        otherRequest.setObjectName(objectName);
        Field otherRequestProperty = new Field();
        otherRequestProperty.setName("toto");
        otherRequestProperty.setType(Type.STRING);
        otherEndpointDescriptor.setRequest(otherRequest);
        otherRequest.setProperties(List.of(otherRequestProperty));
        apiDescriptor.setRestEndpoints(List.of(endpointDescriptor, otherEndpointDescriptor));
      Assert.assertEquals(1, ExchangeApiGenUtil.getFieldPropertiesCount(f, apiDescriptor));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testFindPropertiesForObjectNameInApi_NullObjectName() {
    ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
    apiDescriptor.setName("MyApi");
      ExchangeApiGenUtil.findPropertiesForObjectNameInApi(null, apiDescriptor);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testFindPropertiesForObjectNameInApi_NullApiDescriptor() {
      ExchangeApiGenUtil.findPropertiesForObjectNameInApi("MyPojo", null);
    }
    
    @Test
    public void testFindPropertiesForObjectNameInApi_ObjectNameFoundInRestEndpointRequest() {
    ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
    apiDescriptor.setName("MyApi");
    String objectName = "MyPojo";
      Field f = new Field();
      f.setName("foo");
      f.setObjectName(objectName);
        RestEndpointDescriptor endpointDescriptor = new RestEndpointDescriptor();
        endpointDescriptor.setName("GetAccount");
        endpointDescriptor.setRequest(f);
      
        RestEndpointDescriptor otherEndpointDescriptor = new RestEndpointDescriptor();
        otherEndpointDescriptor.setName("GetAccountV2");
        Field otherRequest = new Field();
        otherRequest.setName("bar");
        otherRequest.setType(Type.OBJECT);
        otherRequest.setObjectName(objectName);
        Field otherRequestProperty = new Field();
        otherRequestProperty.setName("toto");
        otherRequestProperty.setType(Type.STRING);
        otherEndpointDescriptor.setRequest(otherRequest);
        otherRequest.setProperties(List.of(otherRequestProperty));
        apiDescriptor.setRestEndpoints(List.of(endpointDescriptor, otherEndpointDescriptor));
      
      List<Field> actual = ExchangeApiGenUtil.findPropertiesForObjectNameInApi(objectName, apiDescriptor);
      Assert.assertEquals(1, actual.size());
      Assert.assertEquals(otherRequestProperty, actual.get(0));
    }
    
    @Test
    public void testFindPropertiesForObjectNameInApi_ObjectNameFoundInRestEndpointResponse() {
    ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
    apiDescriptor.setName("MyApi");
    String objectName = "MyPojo";
      Field f = new Field();
      f.setName("foo");
      f.setObjectName(objectName);
        RestEndpointDescriptor endpointDescriptor = new RestEndpointDescriptor();
        endpointDescriptor.setName("GetAccount");
        endpointDescriptor.setRequest(f);
      
        RestEndpointDescriptor otherEndpointDescriptor = new RestEndpointDescriptor();
        otherEndpointDescriptor.setName("GetAccountV2");
        Field otherResponse = new Field();
        otherResponse.setName("bar");
        otherResponse.setType(Type.OBJECT);
        otherResponse.setObjectName(objectName);
        Field otherResponseProperty = new Field();
        otherResponseProperty.setName("toto");
        otherResponseProperty.setType(Type.STRING);
        otherEndpointDescriptor.setResponse(otherResponse);
        otherResponse.setProperties(List.of(otherResponseProperty));
        apiDescriptor.setRestEndpoints(List.of(endpointDescriptor, otherEndpointDescriptor));
      
      List<Field> actual = ExchangeApiGenUtil.findPropertiesForObjectNameInApi(objectName, apiDescriptor);
      Assert.assertEquals(1, actual.size());
      Assert.assertEquals(otherResponseProperty, actual.get(0));
    }
    
    @Test
    public void testFindPropertiesForObjectNameInApi_ObjectNameFoundInWebsocketEndpointRequest() {
    ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
    apiDescriptor.setName("MyApi");
    String objectName = "MyPojo";
      
        WebsocketEndpointDescriptor endpointDescriptor = new WebsocketEndpointDescriptor();
        endpointDescriptor.setName("GetAccountV2");
        Field request = new Field();
        request.setName("bar");
        request.setType(Type.OBJECT);
        request.setObjectName(objectName);
        Field otherRequestProperty = new Field();
        otherRequestProperty.setName("toto");
        otherRequestProperty.setType(Type.STRING);
        endpointDescriptor.setRequest(request);
        request.setProperties(List.of(otherRequestProperty));
        apiDescriptor.setWebsocketEndpoints(List.of(endpointDescriptor));
      
      List<Field> actual = ExchangeApiGenUtil.findPropertiesForObjectNameInApi(objectName, apiDescriptor);
      Assert.assertEquals(1, actual.size());
      Assert.assertEquals(otherRequestProperty, actual.get(0));
    }
    
    @Test
    public void testFindPropertiesForObjectNameInApi_ObjectNameFoundInWebsocketEndpointMessage() {
    ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
    apiDescriptor.setName("MyApi");
    String objectName = "MyPojo";
      
        WebsocketEndpointDescriptor endpointDescriptor = new WebsocketEndpointDescriptor();
        endpointDescriptor.setName("GetAccountV2");
        Field message = new Field();
        message.setName("bar");
        message.setType(Type.OBJECT);
        message.setObjectName(objectName);
        Field messageProperty = new Field();
        messageProperty.setName("toto");
        messageProperty.setType(Type.STRING);
        endpointDescriptor.setMessage(message);
        message.setProperties(List.of(messageProperty));
        apiDescriptor.setWebsocketEndpoints(List.of(endpointDescriptor));
      
      List<Field> actual = ExchangeApiGenUtil.findPropertiesForObjectNameInApi(objectName, apiDescriptor);
      Assert.assertEquals(1, actual.size());
      Assert.assertEquals(messageProperty, actual.get(0));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testFindPropertiesForObjectNameInApi_ObjectNameNotFound_NullApiRestAndWebsocketEndpoints() {
      ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
      ExchangeApiGenUtil.findPropertiesForObjectNameInApi("MyPojo", apiDescriptor);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testFindPropertiesForObjectNameInApi_ObjectNameNotFound_EmptyApiRestAndWebsocketEndpoints() {
      ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
      apiDescriptor.setRestEndpoints(List.of());
      apiDescriptor.setWebsocketEndpoints(List.of());
      ExchangeApiGenUtil.findPropertiesForObjectNameInApi("MyPojo", apiDescriptor);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testFindPropertiesForObjectNameInApi_ObjectNameNotFoundInAnyRestOrWebsocketEndpoint() {
      ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
      RestEndpointDescriptor restEndpointDescriptor = new RestEndpointDescriptor();
      apiDescriptor.setRestEndpoints(List.of(restEndpointDescriptor));
      WebsocketEndpointDescriptor websocketEndpointDescriptor = new WebsocketEndpointDescriptor();
      apiDescriptor.setWebsocketEndpoints(List.of(websocketEndpointDescriptor));
      ExchangeApiGenUtil.findPropertiesForObjectNameInApi("MyPojo", apiDescriptor);
    }

    @Test
    public void testResolveAllFieldProperties_NullFieldTypeAndNullParametersButObjectNameResolvedInAnotherApi() {
    ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
    apiDescriptor.setName("MyApi");
    String objectName = "MyPojo";
      Field f = new Field();
      f.setName("foo");
      f.setObjectName(objectName);
        RestEndpointDescriptor endpointDescriptor = new RestEndpointDescriptor();
        endpointDescriptor.setName("GetAccount");
        endpointDescriptor.setRequest(f);
      
        RestEndpointDescriptor otherEndpointDescriptor = new RestEndpointDescriptor();
        otherEndpointDescriptor.setName("GetAccountV2");
        Field otherRequest = new Field();
        otherRequest.setName("bar");
        otherRequest.setType(Type.OBJECT);
        otherRequest.setObjectName(objectName);
        Field otherRequestProperty = new Field();
        otherRequestProperty.setName("toto");
        otherRequestProperty.setType(Type.STRING);
        otherEndpointDescriptor.setRequest(otherRequest);
        otherRequest.setProperties(List.of(otherRequestProperty));
        apiDescriptor.setRestEndpoints(List.of(endpointDescriptor, otherEndpointDescriptor));
        
        Field expected = f.deepClone();
        expected.setProperties(otherRequest.getProperties());
        expected.setType(Type.OBJECT);
        Assert.assertEquals(List.of(expected), ExchangeApiGenUtil.resolveAllFieldProperties(apiDescriptor, List.of(f)));
    }
    
    @Test 
    public void testGetRestApiMethodName() {
      RestEndpointDescriptor restEndpointDescriptor = new RestEndpointDescriptor();
      restEndpointDescriptor.setName("myRestApi");
      RestEndpointDescriptor restEndpointDescriptorSameNameButFirstLetterUpper = new RestEndpointDescriptor();
      restEndpointDescriptorSameNameButFirstLetterUpper.setName("MyRestApi");
      List<RestEndpointDescriptor> restEndpoints = List.of(restEndpointDescriptor, restEndpointDescriptorSameNameButFirstLetterUpper);
      Assert.assertEquals("myRestApi", ExchangeApiGenUtil.getRestApiMethodName(restEndpointDescriptor, restEndpoints));
      Assert.assertEquals("myRestApi", ExchangeApiGenUtil.getRestApiMethodName(restEndpointDescriptorSameNameButFirstLetterUpper, List.of(restEndpointDescriptorSameNameButFirstLetterUpper)));
      Assert.assertEquals("MyRestApi", ExchangeApiGenUtil.getRestApiMethodName(restEndpointDescriptorSameNameButFirstLetterUpper, restEndpoints)); 
    }
    
    @Test
    public void testGetRestEndpointRequestDefaultValueVariableName() {
      RestEndpointDescriptor endpointDescriptor = new RestEndpointDescriptor();
      endpointDescriptor.setName("getAccount");
      Assert.assertEquals("getAccountRestRequest", 
          ExchangeApiGenUtil.getRestEndpointRequestDefaultValueVariableName(endpointDescriptor));
    }
    
    @Test
    public void testGetWebsocketEndpointRequestDefaultValueVariableName() {
      WebsocketEndpointDescriptor endpointDescriptor = new WebsocketEndpointDescriptor();
      endpointDescriptor.setName("getAccount");
      Assert.assertEquals("getAccountWsRequest", 
          ExchangeApiGenUtil.getWebsocketEndpointRequestDefaultValueVariableName(endpointDescriptor));
    }
    
    @Test
    public void generateValueDeclarationForRequestPlaceholder_validPlaceholders() {
      // Initialize imports
      Imports imports = new Imports();

      // Create a mock request field with nested properties
      Field subField1 = Field.builder().name("subField1").type(Type.BOOLEAN).build();
      Field subFielda = Field.builder().name("a").build();
      Field subFieldA = Field.builder().name("A").build();
      Field nestedField = Field.builder().name("nestedField").type(Type.OBJECT)
          .property(subField1)
          .property(subFielda)
          .property(subFieldA).build();

      Field requestField = Field.builder().name("request").type(Type.OBJECT).property(nestedField).build();
      Field requestFieldListType = Field.builder().name("request").type(Type.fromTypeName("INT_LIST")).build();
      Field requestFieldMapType = Field.builder().name("request").type(Type.fromTypeName("OBJECT_MAP"))
          .property(nestedField)
          .build();
      Field primitiveTypeRequestField = Field.builder().name("request").type(Type.LONG).build();
        
      // Test valid placeholder for nested field 'subField1'
      Assert.assertEquals("request.getNestedField().isSubField1()", 
          ExchangeApiGenUtil.generateValueDeclarationForRequestPlaceholder(
            "request.nestedField.subField1", 
            requestField, 
            imports));
      
      // Test conflict between 'a' and 'A' field names
      Assert.assertEquals("request.getNestedField().geta()", 
          ExchangeApiGenUtil.generateValueDeclarationForRequestPlaceholder(
            "request.nestedField.a", 
            requestField, 
            imports));
      Assert.assertEquals("request.getNestedField().getA()", 
          ExchangeApiGenUtil.generateValueDeclarationForRequestPlaceholder(
            "request.nestedField.A", 
            requestField, 
            imports));
      
      // Test using 'request' as the placeholder, request field is of primitive type
      Assert.assertEquals("request",
          ExchangeApiGenUtil.generateValueDeclarationForRequestPlaceholder(
              "request", 
              primitiveTypeRequestField, 
              imports));
      Assert.assertEquals(0, imports.size());
      
      // Test using 'object', 'map' or 'list' target field type: should be converted to JSON string
      Assert.assertEquals("JsonUtil.pojoToJsonString(request)",
          ExchangeApiGenUtil.generateValueDeclarationForRequestPlaceholder(
              "request", 
              requestField, 
              imports));
      Assert.assertEquals(1, imports.size());
      Assert.assertTrue(imports.contains(JsonUtil.class.getName()));
      Assert.assertEquals("JsonUtil.pojoToJsonString(request)",
          ExchangeApiGenUtil.generateValueDeclarationForRequestPlaceholder(
              "request", 
              requestFieldListType, 
              imports));
      Assert.assertEquals("JsonUtil.pojoToJsonString(request)",
          ExchangeApiGenUtil.generateValueDeclarationForRequestPlaceholder(
              "request", 
              requestFieldMapType, 
              imports));
    }
    
    @Test( expected = IllegalArgumentException.class)
    public void generateValueDeclarationForRequestPlaceholder_NotFoundPlaceHolder() {
      Field subField1 = Field.builder().name("subField1").type(Type.BOOLEAN).build();
      Field requestField = Field.builder().name("request").type(Type.OBJECT).property(subField1).build();
      ExchangeApiGenUtil.generateValueDeclarationForRequestPlaceholder(
          "request.foo", 
          requestField, 
          new Imports());
    }
    
    @Test
    public void testGenerateTopicValueSubstitutionInstructionDeclaration() {
      ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
      exchangeDescriptor.setId("TestExchange");
      exchangeDescriptor.setBasePackage("com.test.exchange");
      exchangeDescriptor.setProperties(List.of(
          ConfigPropertyDescriptor.builder().name("p1").type(Type.STRING.toString()).build(),
          ConfigPropertyDescriptor.builder().name("P1").type(Type.BOOLEAN.toString()).build()
      ));
      exchangeDescriptor.setConstants(List.of(
          ConstantDescriptor.builder().name("c1").type(Type.STRING.toString()).value("c1Value").build()
      ));
      Imports imports = new Imports();      
      Field field1 = Field.builder().name("field1").type(Type.BOOLEAN).build();
      Field field2 = Field.builder().name("a").type(Type.STRING).msgField("f2").build();
      Field field3 = Field.builder().name("A").type(Type.INT).msgField("f2").build();
      Field request = Field.builder()
          .name("objField")
          .type(Type.OBJECT)
          .property(field1)
          .property(field2)
          .property(field3)
          .build();
      String template = "${constants.c1}_${config.p1}_${config.P1}_${request.field1}_${request.a}_${request.A}_${request}_${unresolved}";
      Assert.assertNull(ExchangeApiGenUtil.generateTopicValueSubstitutionInstructionDeclaration(null, exchangeDescriptor, request, imports));
      Assert.assertEquals("\"static/topic/value\"", 
          ExchangeApiGenUtil.generateTopicValueSubstitutionInstructionDeclaration(
              "static/topic/value", 
              exchangeDescriptor, 
              request, 
              imports));
      Assert.assertEquals(0, imports.size());
      Assert.assertEquals("EncodingUtil.substituteArguments(\"" + template + "\"," 
              + " \"constants.c1\", TestExchangeConstants.C1," 
              + " \"config.p1\", TestExchangeProperties.getp1(getProperties())," 
              + " \"config.P1\", TestExchangeProperties.isP1(getProperties())," 
              + " \"request.field1\", request.isField1()," 
              + " \"request.a\", request.geta()," 
              + " \"request.A\", request.getA()," 
              + " \"request\", JsonUtil.pojoToJsonString(request))", 
          ExchangeApiGenUtil.generateTopicValueSubstitutionInstructionDeclaration(
              template, 
              exchangeDescriptor, 
              request, 
              imports));
      Assert.assertEquals(4, imports.size());
      Assert.assertTrue(imports.contains("com.test.exchange.TestExchangeConstants"));
      Assert.assertTrue(imports.contains("com.test.exchange.TestExchangeProperties"));
      Assert.assertTrue(imports.contains(EncodingUtil.class.getName()));
      Assert.assertTrue(imports.contains(JsonUtil.class.getName()));
    }
    
    @Test( expected = IllegalArgumentException.class)
    public void testGenerateTopicValueSubstitutionInstructionDeclaration_InvalidTopicPlaceholder() {
      ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
      ExchangeApiGenUtil.generateTopicValueSubstitutionInstructionDeclaration(
          "${topic}", 
          exchangeDescriptor, 
          Field.builder().name("request").build(), 
          new Imports());
    }
    
    @Test
    public void testGenerateTopicMatcherValueSubstitutionInstructionDeclaration_NonStringValue() {
      ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
      Field request = Field.builder().name("field1").type(Type.INT).build();
      Imports imports = new Imports();
      Assert.assertNull(ExchangeApiGenUtil.generateTopicMatcherValueSubstitutionInstructionDeclaration(
          null, 
          exchangeDescriptor, 
          request, 
          imports));
      Assert.assertEquals("123", 
        ExchangeApiGenUtil.generateTopicMatcherValueSubstitutionInstructionDeclaration(
            123, 
            exchangeDescriptor, 
            request, 
            imports));
    }
    
    @Test
    public void testGenerateTopicMatcherValueSubstitutionInstructionDeclaration_templateValue() {
      ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
      exchangeDescriptor.setId("TestExchange");
      exchangeDescriptor.setBasePackage("com.test.exchange");
      exchangeDescriptor.setProperties(List.of(
          ConfigPropertyDescriptor.builder().name("p1").type(Type.STRING.toString()).build(),
          ConfigPropertyDescriptor.builder().name("P1").type(Type.BOOLEAN.toString()).build()
      ));
      exchangeDescriptor.setConstants(List.of(
          ConstantDescriptor.builder().name("c1").type(Type.STRING.toString()).value("c1Value").build()
      ));
      Imports imports = new Imports();      
      Field field1 = Field.builder().name("field1").type(Type.BOOLEAN).build();
      Field field2 = Field.builder().name("a").type(Type.STRING).msgField("f2").build();
      Field field3 = Field.builder().name("A").type(Type.INT).msgField("f2").build();
      Field request = Field.builder()
          .name("objField")
          .type(Type.OBJECT)
          .property(field1)
          .property(field2)
          .property(field3)
          .build();
      String template = "${topic}_${constants.c1}_${config.p1}_${config.P1}_${request.field1}_${request.a}_${request.A}_${request}_${unresolved}";
      Assert.assertNull(ExchangeApiGenUtil.generateTopicMatcherValueSubstitutionInstructionDeclaration(null, exchangeDescriptor, request, imports));
      Assert.assertEquals("\"static/topic/value\"", 
          ExchangeApiGenUtil.generateTopicMatcherValueSubstitutionInstructionDeclaration(
              "static/topic/value", 
              exchangeDescriptor, 
              request, 
              imports));
      Assert.assertEquals(0, imports.size());
      Assert.assertEquals("EncodingUtil.substituteArguments(\"" + template + "\"," 
              + " \"topic\", topic," 
              + " \"constants.c1\", TestExchangeConstants.C1," 
              + " \"config.p1\", TestExchangeProperties.getp1(getProperties())," 
              + " \"config.P1\", TestExchangeProperties.isP1(getProperties())," 
              + " \"request.field1\", request.isField1()," 
              + " \"request.a\", request.geta()," 
              + " \"request.A\", request.getA()," 
              + " \"request\", JsonUtil.pojoToJsonString(request))", 
          ExchangeApiGenUtil.generateTopicMatcherValueSubstitutionInstructionDeclaration(
              template, 
              exchangeDescriptor, 
              request, 
              imports));
      Assert.assertEquals(4, imports.size());
      Assert.assertTrue(imports.contains("com.test.exchange.TestExchangeConstants"));
      Assert.assertTrue(imports.contains("com.test.exchange.TestExchangeProperties"));
      Assert.assertTrue(imports.contains(EncodingUtil.class.getName()));
      Assert.assertTrue(imports.contains(JsonUtil.class.getName()));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGenerateTopicMatcherValueSubstitutionInstructionDeclaration_templateWithFieldNotFound() {
      ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
      exchangeDescriptor.setId("TestExchange");
      exchangeDescriptor.setBasePackage("com.test.exchange");
      Imports imports = new Imports();      
      Field field1 = Field.builder().name("field1").type(Type.BOOLEAN).build();
      Field request = Field.builder()
          .name("objField")
          .type(Type.OBJECT)
          .property(field1)
          .build();
      String template = "${request.foo}";
      ExchangeApiGenUtil.generateTopicMatcherValueSubstitutionInstructionDeclaration(
          template, 
          exchangeDescriptor, 
          request, 
          imports);
    }
    
    @Test
    public void testCheckValidWebsocketTopicMatcherDescriptor_ValidFieldValueDescriptor() {
      String exchangeId = "MyExchange";
      String apiName = "MyApi";
      String endpointName = "MyWsEndpoint";
      WebsocketTopicMatcherDescriptor topicMatcherDescriptor = new WebsocketTopicMatcherDescriptor();
      topicMatcherDescriptor.setFieldName("field1");
      topicMatcherDescriptor.setFieldValue("value1");
      ExchangeApiGenUtil.checkValidWebsocketTopicMatcherDescriptor(exchangeId, apiName, endpointName,
          topicMatcherDescriptor);
    }
    
    @Test
    public void testCheckValidWebsocketTopicMatcherDescriptor_ValidFieldRegexpDescriptor() {
      String exchangeId = "MyExchange";
      String apiName = "MyApi";
      String endpointName = "MyWsEndpoint";
      WebsocketTopicMatcherDescriptor topicMatcherDescriptor = new WebsocketTopicMatcherDescriptor();
      topicMatcherDescriptor.setFieldName("field1");
      topicMatcherDescriptor.setFieldRegexp(".*value1");
      ExchangeApiGenUtil.checkValidWebsocketTopicMatcherDescriptor(exchangeId, apiName, endpointName,
          topicMatcherDescriptor);
    }
    
    @Test
    public void testCheckValidWebsocketTopicMatcherDescriptor_ValidOrDescriptor() {
      String exchangeId = "MyExchange";
      String apiName = "MyApi";
      String endpointName = "MyWsEndpoint";
      WebsocketTopicMatcherDescriptor topicMatcherDescriptor = new WebsocketTopicMatcherDescriptor();
      topicMatcherDescriptor.setOr(List.of());
      ExchangeApiGenUtil.checkValidWebsocketTopicMatcherDescriptor(exchangeId, apiName, endpointName,
          topicMatcherDescriptor);
    }
    
    @Test
    public void testCheckValidWebsocketTopicMatcherDescriptor_ValidDescriptor() {
      String exchangeId = "MyExchange";
      String apiName = "MyApi";
      String endpointName = "MyWsEndpoint";
      WebsocketTopicMatcherDescriptor topicMatcherDescriptor = new WebsocketTopicMatcherDescriptor();
      topicMatcherDescriptor.setAnd(List.of());
      ExchangeApiGenUtil.checkValidWebsocketTopicMatcherDescriptor(exchangeId, apiName, endpointName,
          topicMatcherDescriptor);
      ExchangeApiGenUtil.checkValidWebsocketTopicMatcherDescriptor(exchangeId, apiName, endpointName,
          null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckValidWebsocketTopicMatcherDescriptor_InvalidDescriptor_NoFieldNameNorOrNorAndSet() {
      String exchangeId = "MyExchange";
      String apiName = "MyApi";
      String endpointName = "MyWsEndpoint";
      WebsocketTopicMatcherDescriptor topicMatcherDescriptor = new WebsocketTopicMatcherDescriptor();
      ExchangeApiGenUtil.checkValidWebsocketTopicMatcherDescriptor(exchangeId, apiName, endpointName,
          topicMatcherDescriptor);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckValidWebsocketTopicMatcherDescriptor_InvalidDescriptor_FieldNameAndNoFieldValueNorRegexp() {
      String exchangeId = "MyExchange";
      String apiName = "MyApi";
      String endpointName = "MyWsEndpoint";
      WebsocketTopicMatcherDescriptor topicMatcherDescriptor = new WebsocketTopicMatcherDescriptor();
      topicMatcherDescriptor.setFieldName("field1");
      ExchangeApiGenUtil.checkValidWebsocketTopicMatcherDescriptor(exchangeId, apiName, endpointName,
          topicMatcherDescriptor);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckValidWebsocketTopicMatcherDescriptor_InvalidDescriptor_FieldNameAndOrSet() {
      String exchangeId = "MyExchange";
      String apiName = "MyApi";
      String endpointName = "MyWsEndpoint";
      WebsocketTopicMatcherDescriptor topicMatcherDescriptor = new WebsocketTopicMatcherDescriptor();
      topicMatcherDescriptor.setFieldName("field1");
      topicMatcherDescriptor.setOr(List.of());
      ExchangeApiGenUtil.checkValidWebsocketTopicMatcherDescriptor(exchangeId, apiName, endpointName,
          topicMatcherDescriptor);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckValidWebsocketTopicMatcherDescriptor_InvalidDescriptor_FieldNameAndAndSet() {
      String exchangeId = "MyExchange";
      String apiName = "MyApi";
      String endpointName = "MyWsEndpoint";
      WebsocketTopicMatcherDescriptor topicMatcherDescriptor = new WebsocketTopicMatcherDescriptor();
      topicMatcherDescriptor.setFieldName("field1");
      topicMatcherDescriptor.setAnd(List.of());
      ExchangeApiGenUtil.checkValidWebsocketTopicMatcherDescriptor(exchangeId, apiName, endpointName,
          topicMatcherDescriptor);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckValidWebsocketTopicMatcherDescriptor_InvalidDescriptor_AndAndOrSet() {
      String exchangeId = "MyExchange";
      String apiName = "MyApi";
      String endpointName = "MyWsEndpoint";
      WebsocketTopicMatcherDescriptor topicMatcherDescriptor = new WebsocketTopicMatcherDescriptor();
      topicMatcherDescriptor.setAnd(List.of());
      topicMatcherDescriptor.setOr(List.of());
      ExchangeApiGenUtil.checkValidWebsocketTopicMatcherDescriptor(exchangeId, apiName, endpointName,
          topicMatcherDescriptor);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckValidWebsocketTopicMatcherDescriptor_InvalidDescriptor_AndAndFieldValueSet() {
      String exchangeId = "MyExchange";
      String apiName = "MyApi";
      String endpointName = "MyWsEndpoint";
      WebsocketTopicMatcherDescriptor topicMatcherDescriptor = new WebsocketTopicMatcherDescriptor();
      topicMatcherDescriptor.setAnd(List.of());
      topicMatcherDescriptor.setFieldValue("value1");
      ExchangeApiGenUtil.checkValidWebsocketTopicMatcherDescriptor(exchangeId, apiName, endpointName,
          topicMatcherDescriptor);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckValidWebsocketTopicMatcherDescriptor_InvalidDescriptor_AndAndFieldRegexpSet() {
      String exchangeId = "MyExchange";
      String apiName = "MyApi";
      String endpointName = "MyWsEndpoint";
      WebsocketTopicMatcherDescriptor topicMatcherDescriptor = new WebsocketTopicMatcherDescriptor();
      topicMatcherDescriptor.setAnd(List.of());
      topicMatcherDescriptor.setFieldRegexp("value1");
      ExchangeApiGenUtil.checkValidWebsocketTopicMatcherDescriptor(exchangeId, apiName, endpointName,
          topicMatcherDescriptor);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckValidWebsocketTopicMatcherDescriptor_InvalidDescriptor_OrAndFieldValueSet() {
      String exchangeId = "MyExchange";
      String apiName = "MyApi";
      String endpointName = "MyWsEndpoint";
      WebsocketTopicMatcherDescriptor topicMatcherDescriptor = new WebsocketTopicMatcherDescriptor();
      topicMatcherDescriptor.setOr(List.of());
      topicMatcherDescriptor.setFieldValue("value1");
      ExchangeApiGenUtil.checkValidWebsocketTopicMatcherDescriptor(exchangeId, apiName, endpointName,
          topicMatcherDescriptor);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckValidWebsocketTopicMatcherDescriptor_InvalidDescriptor_OrAndFieldRegexpSet() {
      String exchangeId = "MyExchange";
      String apiName = "MyApi";
      String endpointName = "MyWsEndpoint";
      WebsocketTopicMatcherDescriptor topicMatcherDescriptor = new WebsocketTopicMatcherDescriptor();
      topicMatcherDescriptor.setOr(List.of());
      topicMatcherDescriptor.setFieldRegexp("value1.*");
      ExchangeApiGenUtil.checkValidWebsocketTopicMatcherDescriptor(exchangeId, apiName, endpointName,
          topicMatcherDescriptor);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckValidWebsocketTopicMatcherDescriptor_InvalidDescriptor_OrWithInvalidSubMatcher() {
      String exchangeId = "MyExchange";
      String apiName = "MyApi";
      String endpointName = "MyWsEndpoint";
      WebsocketTopicMatcherDescriptor subMatcherDescriptor = new WebsocketTopicMatcherDescriptor();
      WebsocketTopicMatcherDescriptor topicMatcherDescriptor = new WebsocketTopicMatcherDescriptor();
      topicMatcherDescriptor.setOr(List.of(subMatcherDescriptor));
      ExchangeApiGenUtil.checkValidWebsocketTopicMatcherDescriptor(exchangeId, apiName, endpointName,
          topicMatcherDescriptor);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckValidWebsocketTopicMatcherDescriptor_InvalidDescriptor_AndWithInvalidSubMatcher() {
      String exchangeId = "MyExchange";
      String apiName = "MyApi";
      String endpointName = "MyWsEndpoint";
      WebsocketTopicMatcherDescriptor subMatcherDescriptor = new WebsocketTopicMatcherDescriptor();
      WebsocketTopicMatcherDescriptor topicMatcherDescriptor = new WebsocketTopicMatcherDescriptor();
      topicMatcherDescriptor.setAnd(List.of(subMatcherDescriptor));
      ExchangeApiGenUtil.checkValidWebsocketTopicMatcherDescriptor(exchangeId, apiName, endpointName,
          topicMatcherDescriptor);
    }
    
    @Test
    public void testGenerateWebsocketTopicMatcherFactoryDeclaration() {
      ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
      exchangeDescriptor.setProperties(List.of(
          ConfigPropertyDescriptor.builder().name("p1").type(Type.STRING.toString()).build(),
          ConfigPropertyDescriptor.builder().name("P1").type(Type.BOOLEAN.toString()).build()
      ));
      exchangeDescriptor.setConstants(List.of(
          ConstantDescriptor.builder().name("c1").type(Type.STRING.toString()).value("c1Value").build()
      ));
      exchangeDescriptor.setId("MyExchange");
      exchangeDescriptor.setBasePackage("com.myexchange");
      Imports imports = new Imports();
      ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
      apiDescriptor.setName("MyApi");
      exchangeDescriptor.setApis(List.of(apiDescriptor));
      WebsocketEndpointDescriptor endpointDescriptor = new WebsocketEndpointDescriptor();
      endpointDescriptor.setName("MyWsEndpoint");
      apiDescriptor.setWebsocketEndpoints(List.of(endpointDescriptor));
      WebsocketTopicMatcherDescriptor fieldValueMatcherDescriptor = new WebsocketTopicMatcherDescriptor();
      fieldValueMatcherDescriptor.setFieldName("field1");
      fieldValueMatcherDescriptor.setFieldValue(1);
      WebsocketTopicMatcherDescriptor fieldRegexpMatcherDescriptor = new WebsocketTopicMatcherDescriptor();
      fieldRegexpMatcherDescriptor.setFieldName("field2");
      fieldRegexpMatcherDescriptor.setFieldRegexp("value.*");
      WebsocketTopicMatcherDescriptor aFieldValueMatcherDescriptor = new WebsocketTopicMatcherDescriptor();
      aFieldValueMatcherDescriptor.setFieldName("a");
      aFieldValueMatcherDescriptor.setFieldValue("valueA_${request}");
      WebsocketTopicMatcherDescriptor aUpperFieldValueMatcherDescriptor = new WebsocketTopicMatcherDescriptor();
      aUpperFieldValueMatcherDescriptor.setFieldName("A");
      aUpperFieldValueMatcherDescriptor.setFieldValue("valueAUpper_${config.p1}_${constants.c1}");
      WebsocketTopicMatcherDescriptor orMatcherDescriptor = new WebsocketTopicMatcherDescriptor();
      orMatcherDescriptor.setOr(List.of(aFieldValueMatcherDescriptor, aUpperFieldValueMatcherDescriptor));
      WebsocketTopicMatcherDescriptor andMatcherDescriptor = new WebsocketTopicMatcherDescriptor();
      andMatcherDescriptor.setAnd(List.of(fieldValueMatcherDescriptor, fieldRegexpMatcherDescriptor, orMatcherDescriptor));
      endpointDescriptor.setTopicMatcher(andMatcherDescriptor);
      
      Field request = Field.builder().name("field1").type(Type.INT).build();
      
      Field msgField1 = Field.builder().name("field1").type(Type.INT).build();
      Field msgField2 = Field.builder().name("field2").type(Type.STRING).build();
      Field msgFielda = Field.builder().name("a").build();
      Field msgFieldA = Field.builder().name("A").build();
      Field msgField = Field.builder()
          .name("msg")
          .type(Type.OBJECT)
          .property(msgField1)
          .property(msgField2)
          .property(msgFielda)
          .property(msgFieldA)
          .build();
      
      endpointDescriptor.setMessage(msgField);
      endpointDescriptor.setRequest(request);
      
      String factoryDeclaration = ExchangeApiGenUtil.generateWebsocketTopicMatcherFactoryDeclaration(
          exchangeDescriptor,
          apiDescriptor, 
          endpointDescriptor, 
          imports);
      Assert.assertEquals("WebsocketMessageTopicMatcherFactory topicMatcherFactory = WSMTMFUtil.and(List.of(\n"
          + "  WSMTMFUtil.value(\"field1\", 1),\n"
          + "  WSMTMFUtil.regexp(\"field2\", \"value.*\"),\n"
          + "  WSMTMFUtil.or(List.of(\n"
          + "    WSMTMFUtil.value(\"a\", EncodingUtil.substituteArguments(\"valueA_${request}\", \"request\", request)),\n"
          + "    WSMTMFUtil.value(\"A\", EncodingUtil.substituteArguments(\"valueAUpper_${config.p1}_${constants.c1}\", \"config.p1\", MyExchangeProperties.getp1(getProperties()), \"constants.c1\", MyExchangeConstants.C1))))));", factoryDeclaration);
      Assert.assertEquals(6, imports.size());
      Assert.assertTrue(imports.contains("com.myexchange.MyExchangeConstants"));
      Assert.assertTrue(imports.contains("com.myexchange.MyExchangeProperties"));
      Assert.assertTrue(imports.contains(EncodingUtil.class.getName()));
      Assert.assertTrue(imports.contains(WSMTMFUtil.class.getName()));
      Assert.assertTrue(imports.contains(List.class.getName()));
      Assert.assertTrue(imports.contains(WebsocketMessageTopicMatcherFactory.class.getName()));
    }
    
    @Test
    public void testGenerateWebsocketTopicMatcherFactoryDeclaration_NullRequest() {
      ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
      exchangeDescriptor.setProperties(List.of(
          ConfigPropertyDescriptor.builder().name("p1").type(Type.STRING.toString()).build(),
          ConfigPropertyDescriptor.builder().name("P1").type(Type.BOOLEAN.toString()).build()
      ));
      exchangeDescriptor.setConstants(List.of(
          ConstantDescriptor.builder().name("c1").type(Type.STRING.toString()).value("c1Value").build()
      ));
      exchangeDescriptor.setId("MyExchange");
      exchangeDescriptor.setBasePackage("com.myexchange");
      Imports imports = new Imports();
      ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
      apiDescriptor.setName("MyApi");
      exchangeDescriptor.setApis(List.of(apiDescriptor));
      WebsocketEndpointDescriptor endpointDescriptor = new WebsocketEndpointDescriptor();
      endpointDescriptor.setName("MyWsEndpoint");
      apiDescriptor.setWebsocketEndpoints(List.of(endpointDescriptor));
      WebsocketTopicMatcherDescriptor fieldValueMatcherDescriptor = new WebsocketTopicMatcherDescriptor();
      fieldValueMatcherDescriptor.setFieldName("field1");
      fieldValueMatcherDescriptor.setFieldValue("${config.p1}_${constants.c1}");
      endpointDescriptor.setTopicMatcher(fieldValueMatcherDescriptor);
      
      Field msgField1 = Field.builder().name("field1").type(Type.STRING).build();
      Field msgField = Field.builder()
          .name("msg")
          .type(Type.OBJECT)
          .property(msgField1)
          .build();
      
      endpointDescriptor.setMessage(msgField);
      
      String factoryDeclaration = ExchangeApiGenUtil.generateWebsocketTopicMatcherFactoryDeclaration(
          exchangeDescriptor,
          apiDescriptor, 
          endpointDescriptor, 
          imports);
      Assert.assertEquals("WebsocketMessageTopicMatcherFactory topicMatcherFactory = WSMTMFUtil.value(\"field1\", EncodingUtil.substituteArguments(\"${config.p1}_${constants.c1}\", \"config.p1\", MyExchangeProperties.getp1(getProperties()), \"constants.c1\", MyExchangeConstants.C1));", 
          factoryDeclaration);
    }
    
    @Test
    public void testGenerateWebsocketTopicMatcherFactoryDeclaration_NullRequestAndNullMatcherFactory() {
      ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
      exchangeDescriptor.setProperties(List.of(
          ConfigPropertyDescriptor.builder().name("p1").type(Type.STRING.toString()).build(),
          ConfigPropertyDescriptor.builder().name("P1").type(Type.BOOLEAN.toString()).build()
      ));
      exchangeDescriptor.setConstants(List.of(
          ConstantDescriptor.builder().name("c1").type(Type.STRING.toString()).value("c1Value").build()
      ));
      exchangeDescriptor.setId("MyExchange");
      exchangeDescriptor.setBasePackage("com.myexchange");
      Imports imports = new Imports();
      ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
      apiDescriptor.setName("MyApi");
      exchangeDescriptor.setApis(List.of(apiDescriptor));
      WebsocketEndpointDescriptor endpointDescriptor = new WebsocketEndpointDescriptor();
      endpointDescriptor.setName("MyWsEndpoint");
      apiDescriptor.setWebsocketEndpoints(List.of(endpointDescriptor));
      
      Field msgField1 = Field.builder().name("field1").type(Type.STRING).build();
      Field msgField = Field.builder()
          .name("msg")
          .type(Type.OBJECT)
          .property(msgField1)
          .build();
      
      endpointDescriptor.setMessage(msgField);
      
      String factoryDeclaration = ExchangeApiGenUtil.generateWebsocketTopicMatcherFactoryDeclaration(
          exchangeDescriptor,
          apiDescriptor, 
          endpointDescriptor, 
          imports);
      Assert.assertEquals("WebsocketMessageTopicMatcherFactory topicMatcherFactory = WebsocketMessageTopicMatcherFactory.ANY_MATCHER_FACTORY;", 
          factoryDeclaration);
      Assert.assertEquals(1, imports.size());
      Assert.assertTrue(imports.contains(WebsocketMessageTopicMatcherFactory.class.getName()));
    }
    
    @Test
    public void testGenerateWebsocketEndpointRequestDefaultValuesStaticFieldDeclarations() {
      WebsocketEndpointDescriptor ws1 = new WebsocketEndpointDescriptor();
      ws1.setName("getAccount");
      ws1.setRequest(Field.builder()
          .name("request")
          .description("'getAccount' WS endpoint request description. My ${placeHolder} here.")
          .defaultValue(1.23)
          .type(Type.BIGDECIMAL)
          .build());
      WebsocketEndpointDescriptor ws2 = new WebsocketEndpointDescriptor();
      ws2.setName("getSymbol");
      ws2.setRequest(Field.builder()
          .name("request")
          .description("'getSymbol' WS endpoint request description without default value.")
          .type(Type.INT)
          .build());
      WebsocketEndpointDescriptor ws3 = new WebsocketEndpointDescriptor();
      WebsocketEndpointDescriptor wsa = new WebsocketEndpointDescriptor();
      wsa.setName("a");
      wsa.setRequest(Field.builder()
          .name("request")
          .description("'a' WS endpoint request description")
          .defaultValue("${default_a}")
          .type(Type.STRING)
          .build());
      WebsocketEndpointDescriptor wsA = new WebsocketEndpointDescriptor();
      wsA.setName("A");
      wsA.setRequest(Field.builder()
          .name("request")
          .description("'A' WS endpoint request description")
          .defaultValue("${default_A}")
          .type(Type.STRING)
          .build());
      PlaceHolderResolver descriptionResolver = s -> s.replace("${placeHolder}", "resolved placeholder");
      PlaceHolderResolver defaultValueResolver = s -> JavaCodeGenUtil.getQuotedString(
          s.replace("${default_a}", "aaa")
           .replace("${default_A}", "AAA"));
      Imports imports = new Imports();
      StringBuilder classBody = new StringBuilder();
      Map<String, String> variables = ExchangeApiGenUtil
          .generateWebsocketEndpointRequestDefaultValuesStaticFieldDeclarations(
              List.of(ws1, ws2, ws3, wsa, wsA),
              imports,
              descriptionResolver,
              defaultValueResolver,
              classBody);
      Assert.assertEquals(3, variables.size());
      Assert.assertEquals("GET_ACCOUNT_WS_REQUEST_DEFAULT_VALUE", variables.get("getAccountWsRequest"));
      Assert.assertEquals("A_WS_REQUEST_DEFAULT_VALUE", variables.get("aWsRequest"));
      Assert.assertEquals("A_WS_REQUEST_DEFAULT_VALUE_", variables.get("AWsRequest"));
      Assert.assertEquals(
          "\n"
          + "/**\n"
          + " * Default value for <code>getAccountWsRequest</code>\n"
          + " */\n"
          + "public static final BigDecimal GET_ACCOUNT_WS_REQUEST_DEFAULT_VALUE = new BigDecimal(\"1.23\");\n"
          + "\n"
          + "/**\n"
          + " * Default value for <code>aWsRequest</code>\n"
          + " */\n"
          + "public static final String A_WS_REQUEST_DEFAULT_VALUE = \"aaa\";\n"
          + "\n"
          + "/**\n"
          + " * Default value for <code>AWsRequest</code>\n"
          + " */\n"
          + "public static final String A_WS_REQUEST_DEFAULT_VALUE_ = \"AAA\";\n"
          + "",
          classBody.toString());
      Assert.assertEquals(1, imports.size());
      Assert.assertTrue(imports.contains(BigDecimal.class.getName()));
    }
    
    @Test
    public void testGenerateRestEndpointRequestDefaultValuesStaticFieldDeclarations() {
      RestEndpointDescriptor r1 = new RestEndpointDescriptor();
      r1.setName("getAccount");
      r1.setRequest(Field.builder()
          .name("request")
          .description("'getAccount' REST endpoint request description. My ${placeHolder} here.")
          .defaultValue(1.23)
          .type(Type.BIGDECIMAL)
          .build());
      RestEndpointDescriptor r2 = new RestEndpointDescriptor();
      r2.setName("getSymbol");
      r2.setRequest(Field.builder()
          .name("request")
          .description("'getAccount' REST endpoint request description without default value.")
          .type(Type.INT)
          .build());
      RestEndpointDescriptor r3 = new RestEndpointDescriptor();
      RestEndpointDescriptor ra = new RestEndpointDescriptor();
      ra.setName("a");
      ra.setRequest(Field.builder()
          .name("request")
          .description("'a' REST endpoint request description")
          .defaultValue("${default_a}")
          .type(Type.STRING)
          .build());
      RestEndpointDescriptor rA = new RestEndpointDescriptor();
      rA.setName("A");
      rA.setRequest(Field.builder()
          .name("request")
          .description("'A' REST endpoint request description")
          .defaultValue("${default_A}")
          .type(Type.STRING)
          .build());
      PlaceHolderResolver descriptionResolver = s -> s.replace("${placeHolder}", "resolved placeholder");
      PlaceHolderResolver defaultValueResolver = s -> JavaCodeGenUtil.getQuotedString(
          s.replace("${default_a}", "aaa")
           .replace("${default_A}", "AAA"));
      Imports imports = new Imports();
      StringBuilder classBody = new StringBuilder();
      Map<String, String> variables = ExchangeApiGenUtil
          .generateRestEndpointRequestDefaultValuesStaticFieldDeclarations(
              List.of(r1, r2, r3, ra, rA),
              imports,
              descriptionResolver,
              defaultValueResolver,
              classBody);
      Assert.assertEquals(3, variables.size());
      Assert.assertEquals("GET_ACCOUNT_REST_REQUEST_DEFAULT_VALUE", variables.get("getAccountRestRequest"));
      Assert.assertEquals("A_REST_REQUEST_DEFAULT_VALUE", variables.get("aRestRequest"));
      Assert.assertEquals("A_REST_REQUEST_DEFAULT_VALUE_", variables.get("ARestRequest"));
      Assert.assertEquals(
          "\n"
          + "/**\n"
          + " * Default value for <code>getAccountRestRequest</code>\n"
          + " */\n"
          + "public static final BigDecimal GET_ACCOUNT_REST_REQUEST_DEFAULT_VALUE = new BigDecimal(\"1.23\");\n"
          + "\n"
          + "/**\n"
          + " * Default value for <code>aRestRequest</code>\n"
          + " */\n"
          + "public static final String A_REST_REQUEST_DEFAULT_VALUE = \"aaa\";\n"
          + "\n"
          + "/**\n"
          + " * Default value for <code>ARestRequest</code>\n"
          + " */\n"
          + "public static final String A_REST_REQUEST_DEFAULT_VALUE_ = \"AAA\";\n"
          + "",
          classBody.toString());
      Assert.assertEquals(1, imports.size());
      Assert.assertTrue(imports.contains(BigDecimal.class.getName()));
    }
    
    @Test
    public void testCreateDefaultRawBodyRequest() {
      Field expected = Field.builder()
          .name("body")
          .type(Type.STRING)
          .description("Raw body request field.")
          .build();
      Field actual = ExchangeApiGenUtil.createDefaultRawBodyRequest();
      Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testRestEndpointRequestHasBody() {
      RestEndpointDescriptor getEndpointWithRequestHasBodySet = RestEndpointDescriptor.builder()
          .requestHasBody(true)
          .httpMethod(HttpMethod.GET.name())
          .build();
      Assert.assertTrue(ExchangeApiGenUtil.restEndpointRequestHasBody(getEndpointWithRequestHasBodySet));
      
      RestEndpointDescriptor postEndpointWithRequestHasBodySet = RestEndpointDescriptor.builder()
          .requestHasBody(false)
          .httpMethod(HttpMethod.POST.name())
          .build();
      Assert.assertFalse(ExchangeApiGenUtil.restEndpointRequestHasBody(postEndpointWithRequestHasBodySet));
      
      RestEndpointDescriptor getEndpointWithRequestHasBodyUnset = RestEndpointDescriptor.builder()
          .httpMethod(HttpMethod.GET.name())
          .build();
      Assert.assertFalse(ExchangeApiGenUtil.restEndpointRequestHasBody(getEndpointWithRequestHasBodyUnset));
      
      RestEndpointDescriptor postEndpointWithRequestHasBodyUnset = RestEndpointDescriptor.builder()
          .httpMethod(HttpMethod.POST.name())
          .build();
      Assert.assertTrue(ExchangeApiGenUtil.restEndpointRequestHasBody(postEndpointWithRequestHasBodyUnset));
    }
}
