package com.scz.jxapi.generator.java.exchange.api;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.scz.jxapi.exchange.descriptor.ExchangeApiDescriptor;
import com.scz.jxapi.exchange.descriptor.ExchangeDescriptor;
import com.scz.jxapi.exchange.descriptor.Field;
import com.scz.jxapi.exchange.descriptor.RestEndpointDescriptor;
import com.scz.jxapi.exchange.descriptor.Type;
import com.scz.jxapi.exchange.descriptor.WebsocketEndpointDescriptor;

/**
 * Unit test for {@link ExchangeApiGeneratorUtil}
 */
public class ExchangeApiGeneratorUtilTest {

    @Test
    public void testGenerateRestEndpointRequestClassName() {
        ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
        exchangeDescriptor.setName("TestExchange");
        exchangeDescriptor.setBasePackage("com.test.exchange");
        ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
        apiDescriptor.setName("MyApi");
        exchangeDescriptor.setApis(List.of(apiDescriptor));
        RestEndpointDescriptor endpointDescriptor = new RestEndpointDescriptor();
        endpointDescriptor.setName("GetAccount");
        Field request = new Field();
        request.setType(Type.OBJECT);
        endpointDescriptor.setRequest(request);
        Assert.assertEquals("com.test.exchange.myapi.pojo.TestExchangeMyApiGetAccountRequest", 
        					ExchangeApiGeneratorUtil.generateRestEnpointRequestClassName(exchangeDescriptor, 
        																				 apiDescriptor, 
        																				 endpointDescriptor));
    }

    @Test
    public void testGenerateRestEndpointRequestClassName_RequestWithObjectName() {
        ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
        exchangeDescriptor.setName("TestExchange");
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
        Assert.assertEquals("com.test.exchange.myapi.pojo.MyRequest", 
        					ExchangeApiGeneratorUtil.generateRestEnpointRequestClassName(exchangeDescriptor, 
        																				 apiDescriptor, 
        																				 endpointDescriptor));
    }

    @Test
    public void testGenerateRestEndpointRequestClassName_NullRequestType() {
        ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
        exchangeDescriptor.setName("TestExchange");
        exchangeDescriptor.setBasePackage("com.test.exchange");
        ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
        apiDescriptor.setName("MyApi");
        exchangeDescriptor.setApis(List.of(apiDescriptor));
        RestEndpointDescriptor endpointDescriptor = new RestEndpointDescriptor();
        endpointDescriptor.setName("GetAccount");
        Field request = new Field();
        endpointDescriptor.setRequest(request);
        Assert.assertEquals("com.test.exchange.myapi.pojo.TestExchangeMyApiGetAccountRequest", 
        					ExchangeApiGeneratorUtil.generateRestEnpointRequestClassName(exchangeDescriptor, 
        																				 apiDescriptor, 
        																				 endpointDescriptor));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGenerateRestEndpointRequestClassName_RequestWithObjectNameButNotObjectType() {
        ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
        exchangeDescriptor.setName("TestExchange");
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
        ExchangeApiGeneratorUtil.generateRestEnpointRequestClassName(exchangeDescriptor, 
        															 apiDescriptor, 
        															 endpointDescriptor);
    }

    @Test
    public void testGenerateRestEndpointResponseClassName() {
        ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
        exchangeDescriptor.setName("TestExchange");
        exchangeDescriptor.setBasePackage("com.test.exchange");
        ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
        apiDescriptor.setName("MyApi");
        exchangeDescriptor.setApis(List.of(apiDescriptor));
        RestEndpointDescriptor endpointDescriptor = new RestEndpointDescriptor();
        endpointDescriptor.setName("GetAccount");
        Field response = new Field();
        response.setType(Type.OBJECT);
        endpointDescriptor.setResponse(response);
        Assert.assertEquals("com.test.exchange.myapi.pojo.TestExchangeMyApiGetAccountResponse", 
        					ExchangeApiGeneratorUtil.generateRestEnpointResponseClassName(exchangeDescriptor, 
        																				  apiDescriptor, 
        																				  endpointDescriptor));
    }

    @Test
    public void testGenerateWebsocketEndpointRequestClassName() {
        ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
        exchangeDescriptor.setName("TestExchange");
        exchangeDescriptor.setBasePackage("com.test.exchange");
        ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
        apiDescriptor.setName("MyApi");
        exchangeDescriptor.setApis(List.of(apiDescriptor));
        WebsocketEndpointDescriptor endpointDescriptor = new WebsocketEndpointDescriptor();
        endpointDescriptor.setName("accountWs");
        Field request = new Field();
        request.setType(Type.OBJECT);
        endpointDescriptor.setRequest(request);
        Assert.assertEquals("com.test.exchange.myapi.pojo.TestExchangeMyApiAccountWsRequest", 
        					ExchangeApiGeneratorUtil.generateWebsocketEndpointRequestClassName(exchangeDescriptor, 
        																				  apiDescriptor, 
        																				  endpointDescriptor));
    }

    @Test
    public void testGenerateWebsocketEndpointMessageClassName() {
        ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
        exchangeDescriptor.setName("TestExchange");
        exchangeDescriptor.setBasePackage("com.test.exchange");
        ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
        apiDescriptor.setName("MyApi");
        exchangeDescriptor.setApis(List.of(apiDescriptor));
        WebsocketEndpointDescriptor endpointDescriptor = new WebsocketEndpointDescriptor();
        endpointDescriptor.setName("accountWs");
        Field response = new Field();
        response.setType(Type.OBJECT);
        endpointDescriptor.setMessage(response);
        Assert.assertEquals("com.test.exchange.myapi.pojo.TestExchangeMyApiAccountWsMessage", 
        					ExchangeApiGeneratorUtil.generateWebsocketEndpointMessageClassName(exchangeDescriptor, 
        																				  apiDescriptor, 
        																				  endpointDescriptor));
    }

    @Test
    public void testGetWebsocketSubscribeMethodName() {
        WebsocketEndpointDescriptor endpointDescriptor = new WebsocketEndpointDescriptor();
        endpointDescriptor.setName("accountWs");
        Assert.assertEquals("subscribeAccountWs", 
        					ExchangeApiGeneratorUtil.getWebsocketSubscribeMethodName(endpointDescriptor));
    }

    @Test
    public void testGetWebsocketUnsubscribeMethodName() {
        WebsocketEndpointDescriptor endpointDescriptor = new WebsocketEndpointDescriptor();
        endpointDescriptor.setName("accountWs");
        Assert.assertEquals("unsubscribeAccountWs", 
        					ExchangeApiGeneratorUtil.getWebsocketUnsubscribeMethodName(endpointDescriptor));
    }
    
    @Test
	public void testGetClassNameForField_STRING_Type() {
        Set<String> imports = new HashSet<>();
        Field f = new Field();
        f.setType(Type.STRING);
        Assert.assertEquals(String.class.getSimpleName(), 
        					ExchangeApiGeneratorUtil.getClassNameForField(f, imports, null));
        Assert.assertTrue(imports.isEmpty());
    }

    @Test
	public void testGetClassNameForField_BIGDECIMAL_Type() {
        Set<String> imports = new HashSet<>();
        Field f = new Field();
        f.setType(Type.BIGDECIMAL);
        Assert.assertEquals(BigDecimal.class.getSimpleName(), 
        					ExchangeApiGeneratorUtil.getClassNameForField(f, imports, null));
        Assert.assertEquals(1, imports.size());
        Assert.assertTrue(imports.contains(BigDecimal.class.getName()));
    }

    @Test
    public void testGetClassNameForField_OBJECT_NoObjectName() {
        Set<String> imports = new HashSet<>();
        Field f = new Field();
        f.setName("bar");
        f.setType(Type.OBJECT);
        Assert.assertEquals("FooBar", 
        					ExchangeApiGeneratorUtil.getClassNameForField(f, imports, "com.x.y.gen.pojo.Foo"));
        Assert.assertEquals(1, imports.size());
        Assert.assertTrue(imports.contains("com.x.y.gen.pojo.FooBar"));
    }

    @Test
    public void testGetClassNameForField_OBJECT_WithObjectName() {
        Set<String> imports = new HashSet<>();
        Field f = new Field();
        f.setName("bar");
        f.setType(Type.OBJECT);
        f.setObjectName("MyCustomObjectName");
        Assert.assertEquals("MyCustomObjectName", 
        					ExchangeApiGeneratorUtil.getClassNameForField(f, imports, "com.x.y.gen.pojo.Foo"));
        Assert.assertEquals(1, imports.size());
        Assert.assertTrue(imports.contains("com.x.y.gen.pojo.MyCustomObjectName"));
    }

    @Test
    public void testGetClassNameForField_OBJECT_MAP_LIST() {
        Set<String> imports = new HashSet<>();
        Field f = new Field();
        f.setName("bar");
        f.setType(Type.fromTypeName("OBJECT_MAP_LIST"));
        Assert.assertEquals("List<Map<String, FooBar>>", 
        					ExchangeApiGeneratorUtil.getClassNameForField(f, imports, "com.x.y.gen.pojo.Foo"));
        Assert.assertEquals(3, imports.size());
        Assert.assertTrue(imports.contains("com.x.y.gen.pojo.FooBar"));
        Assert.assertTrue(imports.contains(Map.class.getName()));
        Assert.assertTrue(imports.contains(List.class.getName()));
    }

    @Test
    public void testGetFieldObjectClassName_WithObjectName() {
        Field f = new Field();
        f.setName("bar");
        f.setType(Type.OBJECT);
        f.setObjectName("MyCustomObjectName");
        Assert.assertEquals("com.x.y.gen.pojo.MyCustomObjectName", 
        					ExchangeApiGeneratorUtil.getFieldObjectClassName(f, "com.x.y.gen.pojo.Foo"));
    }

    @Test
    public void testGetFieldObjectClassName_WithoutObjectName() {
        Field f = new Field();
        f.setName("bar");
        f.setType(Type.OBJECT);
        Assert.assertEquals("com.x.y.gen.pojo.FooBar", 
        					ExchangeApiGeneratorUtil.getFieldObjectClassName(f, "com.x.y.gen.pojo.Foo"));
    }

    @Test
    public void testGetLeafObjectFieldClassName_OBJECT() {
        String endpointParameterName = "bar";
        Assert.assertEquals("com.x.y.gen.pojo.FooBar", 
        					ExchangeApiGeneratorUtil.getLeafObjectFieldClassName(
        							endpointParameterName, 
        							Type.OBJECT, 
        							null, 
        							"com.x.y.gen.pojo.Foo"));
    }

    @Test
    public void testGetLeafObjectFieldClassName_OBJECT_LIST_MAP() {
        String endpointParameterName = "bar";
        Assert.assertEquals("com.x.y.gen.pojo.FooBar", 
        					ExchangeApiGeneratorUtil.getLeafObjectFieldClassName(
        							endpointParameterName, 
        							Type.fromTypeName("OBJECT_LIST_MAP"), 
        							null, 
        							"com.x.y.gen.pojo.Foo"));
    }
}
