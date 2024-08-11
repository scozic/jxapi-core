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
        Set<String> imports = new HashSet<>();
        Assert.assertEquals("RawIntegerMessageDeserializer.getInstance()", 
            ExchangeApiGeneratorUtil.getNewMessageDeserializerInstruction(Type.INT, null, imports));
            Assert.assertEquals(1, imports.size());
            Assert.assertTrue(imports.contains(RawIntegerMessageDeserializer.class.getName()));
    }

    @Test
    public void testGetNewMessageDeserializerInstruction_BIGDECIMAL() {
        Set<String> imports = new HashSet<>();
        Assert.assertEquals("RawBigDecimalMessageDeserializer.getInstance()", 
            ExchangeApiGeneratorUtil.getNewMessageDeserializerInstruction(Type.BIGDECIMAL, null, imports));
            Assert.assertEquals(1, imports.size());
            Assert.assertTrue(imports.contains(RawBigDecimalMessageDeserializer.class.getName()));
    }

    @Test
    public void testGetNewMessageDeserializerInstruction_BOOLEAN() {
        Set<String> imports = new HashSet<>();
        Assert.assertEquals("RawBooleanMessageDeserializer.getInstance()", 
            ExchangeApiGeneratorUtil.getNewMessageDeserializerInstruction(Type.BOOLEAN, null, imports));
            Assert.assertEquals(1, imports.size());
            Assert.assertTrue(imports.contains(RawBooleanMessageDeserializer.class.getName()));
    }

    @Test
    public void testGetNewMessageDeserializerInstruction_STRING() {
        Set<String> imports = new HashSet<>();
        Assert.assertEquals("RawStringMessageDeserializer.getInstance()", 
            ExchangeApiGeneratorUtil.getNewMessageDeserializerInstruction(Type.STRING, null, imports));
            Assert.assertEquals(1, imports.size());
            Assert.assertTrue(imports.contains(RawStringMessageDeserializer.class.getName()));
    }

    @Test
    public void testGetNewMessageDeserializerInstruction_null() {
        Set<String> imports = new HashSet<>();
        Assert.assertEquals("RawStringMessageDeserializer.getInstance()", 
            ExchangeApiGeneratorUtil.getNewMessageDeserializerInstruction(null, null, imports));
            Assert.assertEquals(1, imports.size());
            Assert.assertTrue(imports.contains(RawStringMessageDeserializer.class.getName()));
    }

    @Test
    public void testGetNewMessageDeserializerInstruction_LONG() {
        Set<String> imports = new HashSet<>();
        Assert.assertEquals("RawLongMessageDeserializer.getInstance()", 
            ExchangeApiGeneratorUtil.getNewMessageDeserializerInstruction(Type.LONG, null, imports));
            Assert.assertEquals(1, imports.size());
            Assert.assertTrue(imports.contains(RawLongMessageDeserializer.class.getName()));
    }

    @Test
    public void testGetNewMessageDeserializerInstruction_TIMESTAMP() {
        Set<String> imports = new HashSet<>();
        Assert.assertEquals("RawLongMessageDeserializer.getInstance()", 
            ExchangeApiGeneratorUtil.getNewMessageDeserializerInstruction(Type.TIMESTAMP, null, imports));
            Assert.assertEquals(1, imports.size());
            Assert.assertTrue(imports.contains(RawLongMessageDeserializer.class.getName()));
    }

    @Test
    public void testGetNewMessageDeserializerInstruction_OBJECT() {
        Set<String> imports = new HashSet<>();
        Assert.assertEquals("new MyMessageDeserializer()", 
        				    ExchangeApiGeneratorUtil.getNewMessageDeserializerInstruction(Type.OBJECT, "com.x.y.z.MyMessage", imports));
        Assert.assertEquals(1, imports.size());
        Assert.assertTrue(imports.contains("com.x.y.z.deserializers.MyMessageDeserializer")); 
    }

    @Test
    public void testGetNewMessageDeserializerInstruction_OBJECT_MAP() {
        Set<String> imports = new HashSet<>();
        Assert.assertEquals("new MapJsonFieldDeserializer<>(new MyMessageDeserializer())", 
        				    ExchangeApiGeneratorUtil.getNewMessageDeserializerInstruction(Type.fromTypeName("OBJECT_MAP"), "com.x.y.z.MyMessage", imports));
        Assert.assertEquals(2, imports.size());
        Assert.assertTrue(imports.contains(MapJsonFieldDeserializer.class.getName())); 
        Assert.assertTrue(imports.contains("com.x.y.z.deserializers.MyMessageDeserializer")); 
    }
    
    @Test
    public void testGetNewMessageDeserializerInstruction_BIGDECIMAL_LIST() {
        Set<String> imports = new HashSet<>();
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
        response.setParameters(List.of(objectParam));
        endpointDescriptor.setResponse(response);
        Assert.assertTrue(ExchangeApiGeneratorUtil.restEndpointHasResponse(endpointDescriptor, apiDescriptor));
    }
   
    @Test
    public void testGetFieldType_NullField() {
    	Assert.assertNull(ExchangeApiGeneratorUtil.getFieldType(null));
    }
    
    @Test
    public void testGetFieldType_FieldWithNullType() {
    	Assert.assertEquals(Type.OBJECT, ExchangeApiGeneratorUtil.getFieldType(new Field()));
    }
    
    @Test
    public void testGetFieldType_FieldWithPrimitiveType() {
    	Field field = new Field();
    	field.setType(Type.BIGDECIMAL);
    	Assert.assertEquals(Type.BIGDECIMAL, ExchangeApiGeneratorUtil.getFieldType(field));
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
        Field f = Field.createObject(Type.OBJECT.toString(), "foo", null, null, List.of(new Field()));
        Assert.assertTrue(ExchangeApiGeneratorUtil.endpointHasArguments(f, apiDescriptor));
    }
    
    @Test
    public void testEnpointHasArguments_ObjectTypeWithZeroArgs() {
        ExchangeApiDescriptor apiDescriptor = new ExchangeApiDescriptor();
        apiDescriptor.setName("MyApi");
        Field f = Field.createObject(Type.OBJECT.toString(), "foo", null, null, List.of());
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
        otherRequest.setParameters(List.of(otherRequestProperty));
        apiDescriptor.setRestEndpoints(List.of(endpointDescriptor, otherEndpointDescriptor));
        
        Field expected = f.clone();
        expected.setParameters(otherRequest.getParameters());
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
    	f.setParameters(List.of(prop));
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
    	f.setParameters(List.of(prop1, prop2));
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
        otherRequest.setParameters(List.of(otherRequestProperty));
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
        otherRequest.setParameters(List.of(otherRequestProperty));
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
        otherResponse.setParameters(List.of(otherResponseProperty));
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
        request.setParameters(List.of(otherRequestProperty));
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
        message.setParameters(List.of(messageProperty));
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
    	f.setParameters(expectedProperties);
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
    	prop.setParameters(List.of(subProp));
    	f.setParameters(List.of(prop));
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
    	prop.setParameters(List.of(subProp));
    	f.setParameters(List.of(prop));
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
        otherRequest.setParameters(List.of(otherRequestProperty));
        apiDescriptor.setRestEndpoints(List.of(endpointDescriptor, otherEndpointDescriptor));
        
        Field expected = f.clone();
        expected.setParameters(otherRequest.getParameters());
        expected.setType(Type.OBJECT);
        Assert.assertEquals(List.of(expected), ExchangeApiGeneratorUtil.resolveAllFieldProperties(apiDescriptor, List.of(f)));
    }
}
