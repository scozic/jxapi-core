package com.scz.jxapi.generator.java.exchange.api;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.scz.jxapi.exchange.descriptor.ExchangeApiDescriptor;
import com.scz.jxapi.exchange.descriptor.ExchangeDescriptor;
import com.scz.jxapi.exchange.descriptor.Field;
import com.scz.jxapi.exchange.descriptor.RestEndpointDescriptor;
import com.scz.jxapi.exchange.descriptor.Type;
import com.scz.jxapi.exchange.descriptor.WebsocketEndpointDescriptor;
import com.scz.jxapi.generator.java.Imports;
import com.scz.jxapi.netutils.deserialization.RawBigDecimalMessageDeserializer;
import com.scz.jxapi.netutils.deserialization.RawBooleanMessageDeserializer;
import com.scz.jxapi.netutils.deserialization.RawIntegerMessageDeserializer;
import com.scz.jxapi.netutils.deserialization.RawLongMessageDeserializer;
import com.scz.jxapi.netutils.deserialization.RawStringMessageDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.BigDecimalJsonFieldDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.ListJsonFieldDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.MapJsonFieldDeserializer;

/**
 * Unit test for {@link ExchangeApiGeneratorUtil}
 */
public class ExchangeApiGeneratorUtilTest {

    @Test
    public void testGenerateEndpointRequestPojoClassName() {
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
        					ExchangeApiGeneratorUtil.generateRestEnpointRequestPojoClassName(
        							exchangeDescriptor, 
        							apiDescriptor, 
        							endpointDescriptor));
    }

    @Test
    public void testGenerateEndpointRequesPojotClassName_RequestWithObjectName() {
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
        					ExchangeApiGeneratorUtil.generateRestEnpointRequestPojoClassName(
        							exchangeDescriptor, 
        							apiDescriptor, 
        							endpointDescriptor));
    }

    @Test
    public void testGenerateRestEndpointRequestPojoClassName_NullRequestType() {
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
        					ExchangeApiGeneratorUtil.generateRestEnpointRequestPojoClassName(
        							exchangeDescriptor, 
        							apiDescriptor, 
        							endpointDescriptor));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGenerateRestEndpointRequestPojoClassName_RequestNotObjectType() {
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
        ExchangeApiGeneratorUtil.generateRestEnpointRequestPojoClassName(
        		exchangeDescriptor, 
        		apiDescriptor, 
        		endpointDescriptor);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGenerateRestEndpointRequestPojoClassName_NullRequest() {
        ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
        exchangeDescriptor.setName("TestExchange");
        exchangeDescriptor.setBasePackage("com.test.exchange");
        ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
        apiDescriptor.setName("MyApi");
        exchangeDescriptor.setApis(List.of(apiDescriptor));
        RestEndpointDescriptor endpointDescriptor = new RestEndpointDescriptor();
        endpointDescriptor.setName("GetAccount");
        ExchangeApiGeneratorUtil.generateRestEnpointRequestPojoClassName(
        		exchangeDescriptor, 
        		apiDescriptor, 
        		endpointDescriptor);
    }

    @Test
    public void testGenerateRestEndpointResponsePojoClassName() {
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
        					ExchangeApiGeneratorUtil.generateRestEnpointResponsePojoClassName(
        							exchangeDescriptor, 
        							apiDescriptor, 
        							endpointDescriptor));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGenerateRestEndpointResponsePojoClassName_NullResponse() {
        ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
        exchangeDescriptor.setName("TestExchange");
        exchangeDescriptor.setBasePackage("com.test.exchange");
        ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
        apiDescriptor.setName("MyApi");
        exchangeDescriptor.setApis(List.of(apiDescriptor));
        RestEndpointDescriptor endpointDescriptor = new RestEndpointDescriptor();
        endpointDescriptor.setName("GetAccount");
        ExchangeApiGeneratorUtil.generateRestEnpointResponsePojoClassName(
									exchangeDescriptor, 
									apiDescriptor, 
									endpointDescriptor);
    }

    @Test
    public void testGenerateWebsocketEndpointRequestPojoClassName() {
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
        					ExchangeApiGeneratorUtil.generateWebsocketEndpointRequestPojoClassName(
        							exchangeDescriptor, 
        							apiDescriptor, 
        							endpointDescriptor));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGenerateWebsocketEndpointRequestPojoClassName_NullRequest() {
        ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
        exchangeDescriptor.setName("TestExchange");
        exchangeDescriptor.setBasePackage("com.test.exchange");
        ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
        apiDescriptor.setName("MyApi");
        exchangeDescriptor.setApis(List.of(apiDescriptor));
        WebsocketEndpointDescriptor endpointDescriptor = new WebsocketEndpointDescriptor();
        endpointDescriptor.setName("accountWs");
        ExchangeApiGeneratorUtil.generateWebsocketEndpointRequestPojoClassName(
				exchangeDescriptor, 
				apiDescriptor, 
				endpointDescriptor);
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
        Field message = new Field();
        message.setType(Type.OBJECT);
        endpointDescriptor.setMessage(message);
        Assert.assertEquals("com.test.exchange.myapi.pojo.TestExchangeMyApiAccountWsMessage", 
        					ExchangeApiGeneratorUtil.generateWebsocketEndpointMessagePojoClassName(
        							exchangeDescriptor, 
        							apiDescriptor, 
        							endpointDescriptor));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGenerateWebsocketEndpointMessageClassName_NullResponse() {
        ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
        exchangeDescriptor.setName("TestExchange");
        exchangeDescriptor.setBasePackage("com.test.exchange");
        ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
        apiDescriptor.setName("MyApi");
        exchangeDescriptor.setApis(List.of(apiDescriptor));
        WebsocketEndpointDescriptor endpointDescriptor = new WebsocketEndpointDescriptor();
        endpointDescriptor.setName("accountWs");
        ExchangeApiGeneratorUtil.generateWebsocketEndpointMessagePojoClassName(
				exchangeDescriptor, 
				apiDescriptor, 
				endpointDescriptor);
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
        Imports imports = new Imports();
        Field f = new Field();
        f.setType(Type.STRING);
        Assert.assertEquals(String.class.getSimpleName(), 
        					ExchangeApiGeneratorUtil.getClassNameForField(f, imports, null));
        Assert.assertEquals(0, imports.size());
    }

    @Test
	public void testGetClassNameForField_BIGDECIMAL_Type() {
        Imports imports = new Imports();
        Field f = new Field();
        f.setType(Type.BIGDECIMAL);
        Assert.assertEquals(BigDecimal.class.getSimpleName(), 
        					ExchangeApiGeneratorUtil.getClassNameForField(f, imports, null));
        Assert.assertEquals(1, imports.size());
        Assert.assertTrue(imports.contains(BigDecimal.class.getName()));
    }

    @Test
    public void testGetClassNameForField_OBJECT_NoObjectName() {
        Imports imports = new Imports();
        Field f = new Field();
        f.setName("bar");
        f.setType(Type.OBJECT);
        Assert.assertEquals("FooBar", 
        					ExchangeApiGeneratorUtil.getClassNameForField(f, imports, "com.x.y.gen.pojo.Foo"));
        Assert.assertEquals(1, imports.size());
        Assert.assertTrue(imports.contains("com.x.y.gen.pojo.FooBar"));
    }

    @Test
    public void testGetClassNameForField_Implicit_OBJECT_WithObjectName() {
        Imports imports = new Imports();
        Field f = new Field();
        f.setName("bar");
        f.setObjectName("MyCustomObjectName");
        Assert.assertEquals("MyCustomObjectName", 
        					ExchangeApiGeneratorUtil.getClassNameForField(f, imports, "com.x.y.gen.pojo.Foo"));
        Assert.assertEquals(1, imports.size());
        Assert.assertTrue(imports.contains("com.x.y.gen.pojo.MyCustomObjectName"));
    }

    @Test
    public void testGetClassNameForField_OBJECT_MAP_LIST() {
        Imports imports = new Imports();
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
    public void testGetFieldLeafSubTypeClassName_OBJECT() {
        String endpointParameterName = "bar";
        Assert.assertEquals("com.x.y.gen.pojo.FooBar", 
        					ExchangeApiGeneratorUtil.getFieldLeafSubTypeClassName(
        							endpointParameterName, 
        							Type.OBJECT, 
        							null, 
        							"com.x.y.gen.pojo.Foo"));
    }

    @Test
    public void testGetFieldLeafSubTypeClassName_OBJECT_LIST_MAP() {
        String endpointParameterName = "bar";
        Assert.assertEquals("com.x.y.gen.pojo.FooBar", 
        					ExchangeApiGeneratorUtil.getFieldLeafSubTypeClassName(
        							endpointParameterName, 
        							Type.fromTypeName("OBJECT_LIST_MAP"), 
        							null, 
        							"com.x.y.gen.pojo.Foo"));
    }
    
    @Test
    public void testGetFieldLeafSubTypeClassName_OBJECTWithObjectName() {
        String endpointParameterName = "bar";
        Assert.assertEquals("com.x.y.gen.pojo.MyPojo", 
        					ExchangeApiGeneratorUtil.getFieldLeafSubTypeClassName(
        							endpointParameterName, 
        							Type.OBJECT, 
        							"MyPojo", 
        							"com.x.y.gen.pojo.Foo"));
    }
    
    @Test
    public void testGetFieldLeafSubTypeClassName_INT() {
        Assert.assertEquals("java.lang.Integer", 
				ExchangeApiGeneratorUtil.getFieldLeafSubTypeClassName(
						"bar", 
						Type.INT, 
						null, 
						"com.x.y.gen.pojo.Foo"));
    }

    @Test
    public void testGetNewMessageDeserializerInstruction_INT() {
        Imports imports = new Imports();
        Assert.assertEquals("RawIntegerMessageDeserializer.getInstance()", 
            ExchangeApiGeneratorUtil.getNewMessageDeserializerInstruction(Type.INT, null, imports));
            Assert.assertEquals(1, imports.size());
            Assert.assertTrue(imports.contains(RawIntegerMessageDeserializer.class.getName()));
    }

    @Test
    public void testGetNewMessageDeserializerInstruction_BIGDECIMAL() {
        Imports imports = new Imports();
        Assert.assertEquals("RawBigDecimalMessageDeserializer.getInstance()", 
            ExchangeApiGeneratorUtil.getNewMessageDeserializerInstruction(Type.BIGDECIMAL, null, imports));
            Assert.assertEquals(1, imports.size());
            Assert.assertTrue(imports.contains(RawBigDecimalMessageDeserializer.class.getName()));
    }

    @Test
    public void testGetNewMessageDeserializerInstruction_BOOLEAN() {
        Imports imports = new Imports();
        Assert.assertEquals("RawBooleanMessageDeserializer.getInstance()", 
            ExchangeApiGeneratorUtil.getNewMessageDeserializerInstruction(Type.BOOLEAN, null, imports));
            Assert.assertEquals(1, imports.size());
            Assert.assertTrue(imports.contains(RawBooleanMessageDeserializer.class.getName()));
    }

    @Test
    public void testGetNewMessageDeserializerInstruction_STRING() {
        Imports imports = new Imports();
        Assert.assertEquals("RawStringMessageDeserializer.getInstance()", 
            ExchangeApiGeneratorUtil.getNewMessageDeserializerInstruction(Type.STRING, null, imports));
            Assert.assertEquals(1, imports.size());
            Assert.assertTrue(imports.contains(RawStringMessageDeserializer.class.getName()));
    }

    @Test
    public void testGetNewMessageDeserializerInstruction_null() {
        Imports imports = new Imports();
        Assert.assertEquals("RawStringMessageDeserializer.getInstance()", 
            ExchangeApiGeneratorUtil.getNewMessageDeserializerInstruction(null, null, imports));
            Assert.assertEquals(1, imports.size());
            Assert.assertTrue(imports.contains(RawStringMessageDeserializer.class.getName()));
    }

    @Test
    public void testGetNewMessageDeserializerInstruction_LONG() {
        Imports imports = new Imports();
        Assert.assertEquals("RawLongMessageDeserializer.getInstance()", 
            ExchangeApiGeneratorUtil.getNewMessageDeserializerInstruction(Type.LONG, null, imports));
            Assert.assertEquals(1, imports.size());
            Assert.assertTrue(imports.contains(RawLongMessageDeserializer.class.getName()));
    }

    @Test
    public void testGetNewMessageDeserializerInstruction_TIMESTAMP() {
        Imports imports = new Imports();
        Assert.assertEquals("RawLongMessageDeserializer.getInstance()", 
            ExchangeApiGeneratorUtil.getNewMessageDeserializerInstruction(Type.TIMESTAMP, null, imports));
            Assert.assertEquals(1, imports.size());
            Assert.assertTrue(imports.contains(RawLongMessageDeserializer.class.getName()));
    }

    @Test
    public void testGetNewMessageDeserializerInstruction_OBJECT() {
        Imports imports = new Imports();
        Assert.assertEquals("new MyMessageDeserializer()", 
        				    ExchangeApiGeneratorUtil.getNewMessageDeserializerInstruction(Type.OBJECT, "com.x.y.z.MyMessage", imports));
        Assert.assertEquals(1, imports.size());
        Assert.assertTrue(imports.contains("com.x.y.z.deserializers.MyMessageDeserializer")); 
    }

    @Test
    public void testGetNewMessageDeserializerInstruction_OBJECT_MAP() {
        Imports imports = new Imports();
        Assert.assertEquals("new MapJsonFieldDeserializer<>(new MyMessageDeserializer())", 
        				    ExchangeApiGeneratorUtil.getNewMessageDeserializerInstruction(Type.fromTypeName("OBJECT_MAP"), "com.x.y.z.MyMessage", imports));
        Assert.assertEquals(2, imports.size());
        Assert.assertTrue(imports.contains(MapJsonFieldDeserializer.class.getName())); 
        Assert.assertTrue(imports.contains("com.x.y.z.deserializers.MyMessageDeserializer")); 
    }
    
    @Test
    public void testGetNewMessageDeserializerInstruction_BIGDECIMAL_LIST() {
        Imports imports = new Imports();
        Assert.assertEquals("new ListJsonFieldDeserializer<>(BigDecimalJsonFieldDeserializer.getInstance())", 
        				    ExchangeApiGeneratorUtil.getNewMessageDeserializerInstruction(Type.fromTypeName("BIGDECIMAL_LIST"), "com.x.y.z.MyMessage", imports));
        Assert.assertEquals(2, imports.size());
        Assert.assertTrue(imports.contains(BigDecimalJsonFieldDeserializer.class.getName())); 
        Assert.assertTrue(imports.contains(ListJsonFieldDeserializer.class.getName())); 
    }
    
    @Test
    public void testRestEndpointHasResponse_NullResponse() {
        ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
        apiDescriptor.setName("MyApi");
        RestEndpointDescriptor endpointDescriptor = new RestEndpointDescriptor();
        endpointDescriptor.setName("GetAccount");
        Assert.assertFalse(ExchangeApiGeneratorUtil.restEndpointHasResponse(endpointDescriptor, apiDescriptor));
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
        Assert.assertTrue(ExchangeApiGeneratorUtil.restEndpointHasResponse(endpointDescriptor, apiDescriptor));
    }
    
    @Test
    public void testRestEndpointHasResponse_NullResponseType() {
        ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
        apiDescriptor.setName("MyApi");
        RestEndpointDescriptor endpointDescriptor = new RestEndpointDescriptor();
        endpointDescriptor.setName("GetAccount");
        Field response = new Field();
        endpointDescriptor.setResponse(response);
        Assert.assertFalse(ExchangeApiGeneratorUtil.restEndpointHasResponse(endpointDescriptor, apiDescriptor));
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
        Assert.assertFalse(ExchangeApiGeneratorUtil.restEndpointHasResponse(endpointDescriptor, apiDescriptor));
    }
    
    @Test
    public void testRestEndpointHasResponse_ObjectResponseTypeWithParameters() {
        ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
        apiDescriptor.setName("MyApi");
        RestEndpointDescriptor endpointDescriptor = new RestEndpointDescriptor();
        endpointDescriptor.setName("GetAccount");
        Field response = new Field();
        response.setType(Type.OBJECT);
        Field objectParam = new Field();
        response.setProperties(List.of(objectParam));
        endpointDescriptor.setResponse(response);
        Assert.assertTrue(ExchangeApiGeneratorUtil.restEndpointHasResponse(endpointDescriptor, apiDescriptor));
    }
    
    @Test
    public void testGetRestEndpointNameStaticVariable() {
        Assert.assertEquals("GET_ACCOUNT_REST_API", ExchangeApiGeneratorUtil.getRestEndpointNameStaticVariable("GetAccount"));
    }
    
    @Test
    public void testRestEnpointHasArguments_NullRequest() {
        ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
        exchangeDescriptor.setName("TestExchange");
        exchangeDescriptor.setBasePackage("com.test.exchange");
        ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
        apiDescriptor.setName("MyApi");
        exchangeDescriptor.setApis(List.of(apiDescriptor));
        RestEndpointDescriptor endpointDescriptor = new RestEndpointDescriptor();
        endpointDescriptor.setName("GetAccount");
        Assert.assertFalse(ExchangeApiGeneratorUtil.restEndpointHasArguments(endpointDescriptor, apiDescriptor));
    }
    
    @Test
    public void testRestEnpointHasArguments_HasRequestWithArguments() {
        ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
        exchangeDescriptor.setName("TestExchange");
        exchangeDescriptor.setBasePackage("com.test.exchange");
        ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
        apiDescriptor.setName("MyApi");
        exchangeDescriptor.setApis(List.of(apiDescriptor));
        RestEndpointDescriptor endpointDescriptor = new RestEndpointDescriptor();
        endpointDescriptor.setName("GetAccount");
        Field request = new Field();
        request.setType(Type.INT);
        endpointDescriptor.setRequest(request);
        Assert.assertTrue(ExchangeApiGeneratorUtil.restEndpointHasArguments(endpointDescriptor, apiDescriptor));
    }
    
    @Test
    public void testWebsocketEnpointHasArguments_NullRequest() {
        ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
        exchangeDescriptor.setName("TestExchange");
        exchangeDescriptor.setBasePackage("com.test.exchange");
        ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
        apiDescriptor.setName("MyApi");
        exchangeDescriptor.setApis(List.of(apiDescriptor));
        WebsocketEndpointDescriptor endpointDescriptor = new WebsocketEndpointDescriptor();
        endpointDescriptor.setName("GetAccount");
        Assert.assertFalse(ExchangeApiGeneratorUtil.websocketEndpointHasArguments(endpointDescriptor, apiDescriptor));
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
		Assert.assertTrue(ExchangeApiGeneratorUtil.websocketEndpointHasArguments(endpointDescriptor, apiDescriptor));
	}
    
    @Test
    public void testEnpointHasArguments_NullType() {
        Assert.assertFalse(ExchangeApiGeneratorUtil.endpointHasArguments(new Field(), null));
    }
    
    @Test
    public void testEnpointHasArguments_NullField() {
        Assert.assertFalse(ExchangeApiGeneratorUtil.endpointHasArguments(null, null));
    }
    
    @Test
    public void testEnpointHasArguments_PrimitiveType() {
    	Field f = new Field();
    	f.setType(Type.INT);
        Assert.assertTrue(ExchangeApiGeneratorUtil.endpointHasArguments(f, null));
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
        Assert.assertTrue(ExchangeApiGeneratorUtil.endpointHasArguments(f, apiDescriptor));
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
        Assert.assertFalse(ExchangeApiGeneratorUtil.endpointHasArguments(f, apiDescriptor));
    }
    
    @Test
    public void testGetRequestArgName_Null() {
    	Assert.assertEquals(ExchangeApiGeneratorUtil.DEFAULT_REQUEST_ARG_NAME,  ExchangeApiGeneratorUtil.getRequestArgName(null));
    }
    
    @Test
    public void testGetRequestArgName_NotNull() {
    	String argName = "myRequestArg";
    	Assert.assertEquals(argName,  ExchangeApiGeneratorUtil.getRequestArgName(argName));
    }
    
    @Test
    public void testGetWebsocketEndpointNameStaticVariable() {
    	Assert.assertEquals("SUBSCRIBE_ACCOUNT_WS_API",  ExchangeApiGeneratorUtil.getWebsocketEndpointNameStaticVariable("subscribeAccount"));
    }
    
    @Test
    public void testResolveFieldProperties_NullField() {
    	Assert.assertNull(ExchangeApiGeneratorUtil.resolveFieldProperties(null, null));
    }
    
    @Test
    public void testResolveFieldProperties_NullFieldType() {
    	Field f = new Field();
    	f.setName("foo");
    	Assert.assertEquals(f, ExchangeApiGeneratorUtil.resolveFieldProperties(null, f));
    }
    
    @Test
    public void testResolveFieldProperties_PrimitiveFieldType() {
    	Field f = new Field();
    	f.setName("foo");
    	f.setType(Type.INT);
    	Assert.assertEquals(f, ExchangeApiGeneratorUtil.resolveFieldProperties(null, f));
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
        Assert.assertEquals(expected, ExchangeApiGeneratorUtil.resolveFieldProperties(apiDescriptor, f));
    }
    
    @Test
    public void testResolveFieldProperties_ObjectFieldTypeWithNullObjectNameAndNullParameters() {
		ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
		apiDescriptor.setName("MyApi");
    	Field f = new Field();
    	f.setName("foo");
    	f.setType(Type.OBJECT);
    	Assert.assertEquals(f, ExchangeApiGeneratorUtil.resolveFieldProperties(apiDescriptor, f));
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
    	Assert.assertEquals(f, ExchangeApiGeneratorUtil.resolveFieldProperties(apiDescriptor, f));
    }
    
    @Test
    public void testGetFieldPropertiesCount_NullField() {
    	Assert.assertEquals(0, ExchangeApiGeneratorUtil.getFieldPropertiesCount(null, null));
    }
    
    @Test
    public void testGetFieldPropertiesCount_PrimitiveField() {
    	Field f = new Field();
    	f.setType(Type.INT);
    	Assert.assertEquals(0, ExchangeApiGeneratorUtil.getFieldPropertiesCount(f, null));
    }
    
    @Test
    public void testGetFieldPropertiesCountNullParametersObjectField() {
    	Field f = new Field();
    	f.setType(Type.OBJECT);
    	Assert.assertEquals(0, ExchangeApiGeneratorUtil.getFieldPropertiesCount(f, null));
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
    	Assert.assertEquals(2, ExchangeApiGeneratorUtil.getFieldPropertiesCount(f, null));
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
    	Assert.assertEquals(1, ExchangeApiGeneratorUtil.getFieldPropertiesCount(f, apiDescriptor));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testFindPropertiesForObjectNameInApi_NullObjectName() {
		ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
		apiDescriptor.setName("MyApi");
    	ExchangeApiGeneratorUtil.findPropertiesForObjectNameInApi(null, apiDescriptor);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testFindPropertiesForObjectNameInApi_NullApiDescriptor() {
    	ExchangeApiGeneratorUtil.findPropertiesForObjectNameInApi("MyPojo", null);
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
    	
    	List<Field> actual = ExchangeApiGeneratorUtil.findPropertiesForObjectNameInApi(objectName, apiDescriptor);
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
    	
    	List<Field> actual = ExchangeApiGeneratorUtil.findPropertiesForObjectNameInApi(objectName, apiDescriptor);
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
    	
    	List<Field> actual = ExchangeApiGeneratorUtil.findPropertiesForObjectNameInApi(objectName, apiDescriptor);
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
    	
    	List<Field> actual = ExchangeApiGeneratorUtil.findPropertiesForObjectNameInApi(objectName, apiDescriptor);
    	Assert.assertEquals(1, actual.size());
    	Assert.assertEquals(messageProperty, actual.get(0));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testFindPropertiesForObjectNameInApi_ObjectNameNotFound_NullApiRestAndWebsocketEndpoints() {
    	ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
    	ExchangeApiGeneratorUtil.findPropertiesForObjectNameInApi("MyPojo", apiDescriptor);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testFindPropertiesForObjectNameInApi_ObjectNameNotFound_EmptyApiRestAndWebsocketEndpoints() {
    	ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
    	apiDescriptor.setRestEndpoints(List.of());
    	apiDescriptor.setWebsocketEndpoints(List.of());
    	ExchangeApiGeneratorUtil.findPropertiesForObjectNameInApi("MyPojo", apiDescriptor);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testFindPropertiesForObjectNameInApi_ObjectNameNotFoundInAnyRestOrWebsocketEndpoint() {
    	ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
    	RestEndpointDescriptor restEndpointDescriptor = new RestEndpointDescriptor();
    	apiDescriptor.setRestEndpoints(List.of(restEndpointDescriptor));
    	WebsocketEndpointDescriptor websocketEndpointDescriptor = new WebsocketEndpointDescriptor();
    	apiDescriptor.setWebsocketEndpoints(List.of(websocketEndpointDescriptor));
    	ExchangeApiGeneratorUtil.findPropertiesForObjectNameInApi("MyPojo", apiDescriptor);
    }
    
    @Test
    public void testFindPropertiesForObjectNameInField_NullField() {
    	Assert.assertNull(ExchangeApiGeneratorUtil.findPropertiesForObjectNameInField("MyPojo", null));
    }
    
    @Test
    public void testFindPropertiesForObjectNameInField_NullObjectName() {
        Assert.assertNull(ExchangeApiGeneratorUtil.findPropertiesForObjectNameInField(null, new Field()));
    }
    
    @Test
    public void testFindPropertiesForObjectNameInField_NullFieldProperties() {
        Assert.assertNull(ExchangeApiGeneratorUtil.findPropertiesForObjectNameInField("MyPojo", new Field()));
    }
    
    @Test
    public void testFindPropertiesForObjectNameInField_FieldCarriesExpectedObjectName() {
    	String objectName = "MyPojo";
    	Field f = new Field();
    	f.setObjectName(objectName);
    	
    	Field prop = new Field();
    	prop.setName("foo");
    	prop.setType(Type.STRING);
    	List<Field> expectedProperties = List.of(prop);
    	f.setProperties(expectedProperties);
        Assert.assertEquals(expectedProperties, ExchangeApiGeneratorUtil.findPropertiesForObjectNameInField(objectName, f));
    }
    
    @Test
    public void testFindPropertiesForObjectNameInField_FieldSubPropertyCarriesExpectedObjectName() {
    	String objectName = "MyPojo";
    	Field f = new Field();
    	Field prop = new Field();
    	prop.setName("foo");
    	prop.setType(Type.OBJECT);
    	prop.setObjectName(objectName);
    	Field subProp = new Field();
    	subProp.setName("bar");
    	subProp.setType(Type.STRING);
    	List<Field> expectedProperties = List.of(subProp);
    	prop.setProperties(List.of(subProp));
    	f.setProperties(List.of(prop));
        Assert.assertEquals(expectedProperties, ExchangeApiGeneratorUtil.findPropertiesForObjectNameInField(objectName, f));
    }
    @Test
    public void testFindPropertiesForObjectNameInField_ObjectNameNotFound() {
    	String objectName = "MyPojo";
    	Field f = new Field();
    	Field prop = new Field();
    	prop.setName("foo");
    	prop.setType(Type.OBJECT);
    	Field subProp = new Field();
    	subProp.setName("bar");
    	subProp.setType(Type.STRING);
    	prop.setProperties(List.of(subProp));
    	f.setProperties(List.of(prop));
        Assert.assertNull(ExchangeApiGeneratorUtil.findPropertiesForObjectNameInField(objectName, f));
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
        Assert.assertEquals(List.of(expected), ExchangeApiGeneratorUtil.resolveAllFieldProperties(apiDescriptor, List.of(f)));
    }

    @Test
    public void testGetRestEndpointUrlStaticVariableName() {
        RestEndpointDescriptor endpointDescriptor = new RestEndpointDescriptor();
        endpointDescriptor.setName("GetAccount");
        Assert.assertEquals("GET_ACCOUNT_URL", ExchangeApiGeneratorUtil.getRestEndpointUrlStaticVariableName(endpointDescriptor));
    }

    @Test
    public void testGetWebsocketEndpointUrlStaticVariableName() {
        ExchangeApiDescriptor endpointDescriptor = new ExchangeApiDescriptor();
        endpointDescriptor.setName("allTicker");
        Assert.assertEquals("ALL_TICKER_WS_URL", ExchangeApiGeneratorUtil.getWebsocketUrlStaticVariableName(endpointDescriptor));
    }

    @Test
    public void testGetHttpUrlVariableDeclaration_AbsoluteApiUrl() {
        ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
        ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
        apiDescriptor.setHttpUrl("https://api.exchange.com/v1");
        Imports imports = new Imports();
        Assert.assertEquals("\n"
        		+ "/**\n"
        		+ " * Base URL for <i>null</i> exchange <i>null</i> API REST endpoints\n"
        		+ " */\n"
        		+ "public static final String HTTP_URL = \"https://api.exchange.com/v1\";", 
                ExchangeApiGeneratorUtil.getHttpUrlVariableDeclaration(exchangeDescriptor, apiDescriptor, imports));
        Assert.assertEquals(0, imports.size());
    }
    
    @Test
    public void testGetHttpUrlVariableDeclaration_RelateApiUrl_AbsoluteExchangeUrl() {
        ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
        exchangeDescriptor.setName("MyExchange");
        exchangeDescriptor.setBasePackage("com.x.gen");
        exchangeDescriptor.setHttpUrl("\n"
        		+ "/**\n"
        		+ " * Base URL for <i>MyExchange</i> exchange <i>null</i> API REST endpoints\n"
        		+ " */\n"
        		+ "public static final String HTTP_URL = MyExchangeExchangeImpl.HTTP_URL + \"/v1\";");
        ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
        apiDescriptor.setHttpUrl("/v1");
        Imports imports = new Imports();
        Assert.assertEquals("\n"
        		+ "/**\n"
        		+ " * Base URL for <i>MyExchange</i> exchange <i>null</i> API REST endpoints\n"
        		+ " */\n"
        		+ "public static final String HTTP_URL = MyExchangeExchangeImpl.HTTP_URL + \"/v1\";", 
                ExchangeApiGeneratorUtil.getHttpUrlVariableDeclaration(exchangeDescriptor, apiDescriptor, imports));
        Assert.assertEquals(1, imports.size());
        Assert.assertEquals("com.x.gen.MyExchangeExchangeImpl", imports.iterator().next());
    }
    
    @Test
    public void testGetHttpUrlVariableDeclaration_NullApiUrl_AbsoluteExchangeUrl() {
        ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
        exchangeDescriptor.setName("MyExchange");
        exchangeDescriptor.setBasePackage("com.x.gen");
        exchangeDescriptor.setHttpUrl("https://api.exchange.com");
        ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
        Imports imports = new Imports();
        Assert.assertEquals("\n"
        		+ "/**\n"
        		+ " * Base URL for <i>MyExchange</i> exchange <i>null</i> API REST endpoints\n"
        		+ " */\n"
        		+ "public static final String HTTP_URL = MyExchangeExchangeImpl.HTTP_URL;", 
                ExchangeApiGeneratorUtil.getHttpUrlVariableDeclaration(exchangeDescriptor, apiDescriptor, imports));
        Assert.assertEquals(1, imports.size());
        Assert.assertEquals("com.x.gen.MyExchangeExchangeImpl", imports.iterator().next());
    }
    
    
    @Test
    public void testGetHttpUrlVariableDeclaration_NullApiUrl_NullExchangeUrl() {
        ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
        exchangeDescriptor.setBasePackage("com.x.gen");
        ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
        Imports imports = new Imports();
        Assert.assertNull(ExchangeApiGeneratorUtil.getHttpUrlVariableDeclaration(exchangeDescriptor, apiDescriptor, imports));
        Assert.assertEquals(0, imports.size());
    }
    
    @Test
    public void testGetRestEndpointUrlVariableDeclaration_AbsoluteRestEndpointUrl_NoBaseHttpUrl() {
        RestEndpointDescriptor endpointDescriptor = new RestEndpointDescriptor();
        endpointDescriptor.setName("GetMainAccount");
        endpointDescriptor.setUrl("https://api.exchange.com/account");
        Imports imports = new Imports();
        Assert.assertEquals("public static final String GET_MAIN_ACCOUNT_URL = \"https://api.exchange.com/account\";", 
                            ExchangeApiGeneratorUtil.getRestEndpointUrlVariableDeclaration(false, endpointDescriptor));
        Assert.assertEquals(0, imports.size());
    }
    
    @Test
    public void testGetRestEndpointUrlVariableDeclaration_RelativeRestEndpointUrl_WithBaseHttpUrl() {
        RestEndpointDescriptor endpointDescriptor = new RestEndpointDescriptor();
        endpointDescriptor.setName("GetMainAccount");
        endpointDescriptor.setUrl("/account");
        Imports imports = new Imports();
        Assert.assertEquals("public static final String GET_MAIN_ACCOUNT_URL = HTTP_URL + \"/account\";", 
                            ExchangeApiGeneratorUtil.getRestEndpointUrlVariableDeclaration(true, endpointDescriptor));
        Assert.assertEquals(0, imports.size());
    }
    
    @Test
    public void testGetRestEndpointUrlVariableDeclaration_NullRestEndpointUrl_WithBaseHttpUrl() {
        RestEndpointDescriptor endpointDescriptor = new RestEndpointDescriptor();
        endpointDescriptor.setName("GetMainAccount");
        Imports imports = new Imports();
        Assert.assertEquals("public static final String GET_MAIN_ACCOUNT_URL = HTTP_URL;", 
                            ExchangeApiGeneratorUtil.getRestEndpointUrlVariableDeclaration(true, endpointDescriptor));
        Assert.assertEquals(0, imports.size());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetRestEndpointUrlVariableDeclaration_RelativeRestEndpointUrl_NoBaseHttpUrl() {
        RestEndpointDescriptor endpointDescriptor = new RestEndpointDescriptor();
        endpointDescriptor.setName("GetMainAccount");
        endpointDescriptor.setUrl("/account");
        ExchangeApiGeneratorUtil.getRestEndpointUrlVariableDeclaration(false, endpointDescriptor);
    }
    
    @Test
    public void testGetWebsocketUrlVariableDeclaration_AbsoluteApiWsUrl() {
        ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
        ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
        apiDescriptor.setWebsocketUrl("https://api.exchange.com/ws/account");
        Imports imports = new Imports();
        Assert.assertEquals("/**\n"
        		+ " * Base URL for <i>null</i> exchange <i>null</i> API Websocket endpoints\n"
        		+ " */\n"
        		+ "public static final String WEBSOCKET_URL = \"https://api.exchange.com/ws/account\";", 
                ExchangeApiGeneratorUtil.getWebsocketUrlVariableDeclaration(exchangeDescriptor, apiDescriptor, imports));
        Assert.assertEquals(0, imports.size());
    }
    
    @Test
    public void testGetWebsocketUrlVariableDeclaration_RelateApiWsUrl_AbsoluteExchangeWsUrl() {
        ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
        exchangeDescriptor.setName("MyExchange");
        exchangeDescriptor.setBasePackage("com.x.gen");
        exchangeDescriptor.setWebsocketUrl("https://api.exchange.com");
        ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
        apiDescriptor.setWebsocketUrl("/ws/account");
        Imports imports = new Imports();
        Assert.assertEquals("/**\n"
        		+ " * Base URL for <i>MyExchange</i> exchange <i>null</i> API Websocket endpoints\n"
        		+ " */\n"
        		+ "public static final String WEBSOCKET_URL = MyExchangeExchangeImpl.WEBSOCKET_URL + \"/ws/account\";", 
                ExchangeApiGeneratorUtil.getWebsocketUrlVariableDeclaration(exchangeDescriptor, apiDescriptor, imports));
        Assert.assertEquals(1, imports.size());
        Iterator<String> it = imports.iterator();
        Assert.assertEquals("com.x.gen.MyExchangeExchangeImpl", it.next());
    }
    
    @Test
    public void testGetWebsocketUrlVariableDeclaration_NullApiWsUrl_AbsoluteExchangeWsUrl() {
        ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
        exchangeDescriptor.setName("MyExchange");
        exchangeDescriptor.setBasePackage("com.x.gen");
        exchangeDescriptor.setWebsocketUrl("https://api.exchange.com/ws");
        ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
        Imports imports = new Imports();
        Assert.assertEquals("/**\n"
        		+ " * Base URL for <i>MyExchange</i> exchange <i>null</i> API Websocket endpoints\n"
        		+ " */\n"
        		+ "public static final String WEBSOCKET_URL = MyExchangeExchangeImpl.WEBSOCKET_URL;", 
                ExchangeApiGeneratorUtil.getWebsocketUrlVariableDeclaration(exchangeDescriptor, apiDescriptor, imports));
        Assert.assertEquals(1, imports.size());
        Iterator<String> it = imports.iterator();
        Assert.assertEquals("com.x.gen.MyExchangeExchangeImpl", it.next());
    }
    
    @Test
    public void testGetWebsocketUrlVariableDeclaration_NullApiWsUrl_NullExchangeWsUrl() {
        ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptor();
        exchangeDescriptor.setName("MyExchange");
        exchangeDescriptor.setBasePackage("com.x.gen");
        ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
        Imports imports = new Imports(); 
        Assert.assertNull(ExchangeApiGeneratorUtil.getWebsocketUrlVariableDeclaration(exchangeDescriptor, apiDescriptor, imports));
    }
    
    @Test 
    public void testGetRestApiMethodName() {
    	RestEndpointDescriptor restEndpointDescriptor = new RestEndpointDescriptor();
    	restEndpointDescriptor.setName("MyRestApi");
    	Assert.assertEquals("myRestApi", ExchangeApiGeneratorUtil.getRestApiMethodName(restEndpointDescriptor));
    }
}
