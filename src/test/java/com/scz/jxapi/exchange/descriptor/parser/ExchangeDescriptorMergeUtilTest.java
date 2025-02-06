package com.scz.jxapi.exchange.descriptor.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.junit.Assert;
import org.junit.Test;

import com.scz.jxapi.exchange.descriptor.Constant;
import com.scz.jxapi.exchange.descriptor.DefaultConfigProperty;
import com.scz.jxapi.exchange.descriptor.ExchangeApiDescriptor;
import com.scz.jxapi.exchange.descriptor.ExchangeDescriptor;
import com.scz.jxapi.exchange.descriptor.RestEndpointDescriptor;
import com.scz.jxapi.exchange.descriptor.WebsocketEndpointDescriptor;
import com.scz.jxapi.netutils.rest.ratelimits.RateLimitRule;

/**
 * Unit test for {@link ExchangeDescriptorMergeUtil}
 */
public class ExchangeDescriptorMergeUtilTest {

	@Test
	public void testMerge() {
		Assert.assertEquals("foo", ExchangeDescriptorMergeUtil.merge(null, "foo", null));
		Assert.assertEquals("foo", ExchangeDescriptorMergeUtil.merge(null,  null, "foo"));
		Assert.assertNull(ExchangeDescriptorMergeUtil.merge(null, null, null));
		Assert.assertEquals("foo", ExchangeDescriptorMergeUtil.merge(null,  "foo", "foo"));
		try {
			ExchangeDescriptorMergeUtil.merge("property 'hello'", "foo", "bar");
			Assert.fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// expected
			Assert.assertEquals("Conflict: Found distinct values for property 'hello':[foo] and [bar]", e.getMessage());
		}
	}
	
	@Test
	public void testMergeLongs() {
		Assert.assertEquals(1L, ExchangeDescriptorMergeUtil.mergePositiveLongs(null, 1L, -1L));
		Assert.assertEquals(1L, ExchangeDescriptorMergeUtil.mergePositiveLongs(null, -1L, 1L));
		Assert.assertEquals(1L, ExchangeDescriptorMergeUtil.mergePositiveLongs(null, 1L, 1L));
		Assert.assertEquals(-1L, ExchangeDescriptorMergeUtil.mergePositiveLongs(null, -1L, -1L));
		try {
			ExchangeDescriptorMergeUtil.mergePositiveLongs("property 'hello'", 1L, 2L);
			Assert.fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// expected
			Assert.assertEquals("Conflict: Found distinct values for property 'hello':[1] and [2]", e.getMessage());
		}
	}
	
	@Test
	public void testMergeLists() {
		List<MyType> list1 = List.of(new MyType("foo"), new MyType("bar"));
		List<MyType> list2 = List.of(new MyType("hello"), new MyType("world"));
		List<MyType> list3 = List.of(new MyType("bye"), new MyType("foo"));
		Function<MyType, String> idExtractor = MyType::getValue;
		
		Assert.assertEquals(list1, ExchangeDescriptorMergeUtil.mergeLists(null, list1, null, idExtractor));
		Assert.assertEquals(list1, ExchangeDescriptorMergeUtil.mergeLists(null, null, list1, idExtractor));
		Assert.assertEquals(List.of(), ExchangeDescriptorMergeUtil.mergeLists(null, null, null, null));
		List<MyType> mergedL1L2 = ExchangeDescriptorMergeUtil.mergeLists(null, list1, list2, idExtractor);
		Assert.assertEquals(4, mergedL1L2.size());
		Assert.assertEquals("foo", mergedL1L2.get(0).getValue());
		Assert.assertEquals("bar", mergedL1L2.get(1).getValue());
		Assert.assertEquals("hello", mergedL1L2.get(2).getValue());
		Assert.assertEquals("world", mergedL1L2.get(3).getValue());
		
		try {
			ExchangeDescriptorMergeUtil.mergeLists("list property 'hello'", list1, list3, idExtractor);
			Assert.fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// expected
			Assert.assertEquals("Duplicate ID found in values of list property list property 'hello':[foo]", e.getMessage());
		}
		
	}
	
	@Test
	public void testMergeExchangeApiDescriptors() {
		ExchangeApiDescriptor api1 = new ExchangeApiDescriptor();
		api1.setName("myApi");
		api1.setDescription("My API description");
		api1.setHttpUrl("http://myapi.com");
		api1.setWebsocketUrl("ws://myapi.com");
		List<Constant> constants1 = new ArrayList<>();
		Constant c1 = new Constant();
		c1.setName("const1");
		c1.setValue("value1");
		constants1.add(c1);
		Constant c2 = new Constant();
		c2.setName("const2");
		c2.setValue("value2");
		constants1.add(c2);
		api1.setConstants(constants1);
		List<RestEndpointDescriptor> restEndpoints1 = new ArrayList<>();
		RestEndpointDescriptor restApi1 = new RestEndpointDescriptor();
		restApi1.setName("restApi1");
		restEndpoints1.add(restApi1);
		api1.setRestEndpoints(restEndpoints1);
		List<WebsocketEndpointDescriptor> websocketEndpoints1 = new ArrayList<>();
		WebsocketEndpointDescriptor wsApi1 = new WebsocketEndpointDescriptor();
		wsApi1.setName("wsApi1");
		websocketEndpoints1.add(wsApi1);
		api1.setWebsocketEndpoints(websocketEndpoints1);
		List<RateLimitRule> rateLimitRules1 = new ArrayList<>();
		RateLimitRule rule1 = new RateLimitRule();
		rule1.setId("rule1");
		rateLimitRules1.add(rule1);
		api1.setRateLimits(rateLimitRules1);
		
		ExchangeApiDescriptor api2 = new ExchangeApiDescriptor();
		api2.setName("myApi");
		api2.setHttpRequestExecutorFactory("com.x.y.MyHttpRequestExecutorFactory");
		api2.setHttpRequestInterceptorFactory("com.x.y.MyHttpRequestInterceptorFactory");
		api2.setWebsocketFactory("com.x.y.MyWebsocketFactory");
		api2.setHttpRequestTimeout(1000L);
		List<Constant> constants2 = new ArrayList<>();
		Constant c3 = new Constant();
		c3.setName("const3");
		c3.setValue("value3");
		constants2.add(c3);
		api2.setConstants(constants2);
		List<RestEndpointDescriptor> restEndpoints2 = new ArrayList<>();
		RestEndpointDescriptor restApi2 = new RestEndpointDescriptor();
		restApi2.setName("restApi2");
		restEndpoints2.add(restApi2);
		RestEndpointDescriptor restApi3 = new RestEndpointDescriptor();
		restApi3.setName("restApi3");
		restEndpoints2.add(restApi3);
		api2.setRestEndpoints(restEndpoints2);
		List<WebsocketEndpointDescriptor> websocketEndpoints2 = new ArrayList<>();
		WebsocketEndpointDescriptor wsApi2 = new WebsocketEndpointDescriptor();
		wsApi2.setName("wsApi2");
		websocketEndpoints2.add(wsApi2);
		WebsocketEndpointDescriptor wsApi3 = new WebsocketEndpointDescriptor();
		wsApi3.setName("wsApi3");
		websocketEndpoints2.add(wsApi3);
		api2.setWebsocketEndpoints(websocketEndpoints2);
		List<RateLimitRule> rateLimitRules2 = new ArrayList<>();
		RateLimitRule rule2 = new RateLimitRule();
		rule2.setId("rule2");
		rateLimitRules2.add(rule2);
		api2.setRateLimits(rateLimitRules2);
		
		ExchangeApiDescriptor merged = ExchangeDescriptorMergeUtil.mergeExchangeApiDescriptors(api1, api2);
		Assert.assertEquals("myApi", merged.getName());
		Assert.assertEquals("My API description", merged.getDescription());
		Assert.assertEquals("http://myapi.com", merged.getHttpUrl());
		Assert.assertEquals("ws://myapi.com", merged.getWebsocketUrl());
		Assert.assertEquals("com.x.y.MyHttpRequestExecutorFactory", merged.getHttpRequestExecutorFactory());
		Assert.assertEquals("com.x.y.MyHttpRequestInterceptorFactory", merged.getHttpRequestInterceptorFactory());
		Assert.assertEquals("com.x.y.MyWebsocketFactory", merged.getWebsocketFactory());
		Assert.assertEquals(1000L, merged.getHttpRequestTimeout());
		Assert.assertEquals(3, merged.getConstants().size());
		Assert.assertEquals(c1, merged.getConstants().get(0));
		Assert.assertEquals(c2, merged.getConstants().get(1));
		Assert.assertEquals(c3, merged.getConstants().get(2));
		Assert.assertEquals(3, merged.getRestEndpoints().size());
		Assert.assertEquals(restApi1, merged.getRestEndpoints().get(0));
		Assert.assertEquals(restApi2, merged.getRestEndpoints().get(1));
		Assert.assertEquals(restApi3, merged.getRestEndpoints().get(2));
		Assert.assertEquals(3, merged.getWebsocketEndpoints().size());
		Assert.assertEquals(wsApi1, merged.getWebsocketEndpoints().get(0));
		Assert.assertEquals(wsApi2, merged.getWebsocketEndpoints().get(1));
		Assert.assertEquals(wsApi3, merged.getWebsocketEndpoints().get(2));
		Assert.assertEquals(2, merged.getRateLimits().size());
		Assert.assertEquals(rule1, merged.getRateLimits().get(0));
		Assert.assertEquals(rule2, merged.getRateLimits().get(1));
	}
	
	@Test
	public void testMergeExchangeApiDescriptorLists_DistinctApis() {
		ExchangeApiDescriptor api1 = new ExchangeApiDescriptor();
		api1.setName("myApi1");
		ExchangeApiDescriptor api2 = new ExchangeApiDescriptor();
		api2.setName("myApi2");
		List<ExchangeApiDescriptor> list1 = List.of(api1);
		List<ExchangeApiDescriptor> list2 = List.of(api2);
		List<ExchangeApiDescriptor> merged = ExchangeDescriptorMergeUtil.mergeExchangeApiDescriptorLists(list1, list2);
		Assert.assertEquals(2, merged.size());
		Assert.assertEquals(api1, merged.get(0));
		Assert.assertEquals(api2, merged.get(1));
	}
	
	@Test
	public void testMergeExchangeApiDescriptorLists_SameApis() {
		ExchangeApiDescriptor api1 = new ExchangeApiDescriptor();
		api1.setName("myApi1");
		api1.setDescription("API1 description");
		ExchangeApiDescriptor api2 = new ExchangeApiDescriptor();
		api2.setName("myApi1");
		api2.setHttpUrl("http://myapi2.com");
		List<ExchangeApiDescriptor> list1 = List.of(api1);
		List<ExchangeApiDescriptor> list2 = List.of(api2);
		List<ExchangeApiDescriptor> merged = ExchangeDescriptorMergeUtil.mergeExchangeApiDescriptorLists(list1, list2);
		Assert.assertEquals(1, merged.size());
		ExchangeApiDescriptor mergedApi = merged.get(0);
		Assert.assertEquals("API1 description", mergedApi.getDescription());
		Assert.assertEquals("http://myapi2.com", mergedApi.getHttpUrl());
	}
	
	@Test
	public void testMergeExchangeApiDescriptorLists_OneNullList() {
		ExchangeApiDescriptor api1 = new ExchangeApiDescriptor();
		api1.setName("myApi1");
		List<ExchangeApiDescriptor> list1 = List.of(api1);
		List<ExchangeApiDescriptor> merged = ExchangeDescriptorMergeUtil.mergeExchangeApiDescriptorLists(list1, null);
		Assert.assertEquals(1, merged.size());
		Assert.assertEquals(api1, merged.get(0));
		
		merged = ExchangeDescriptorMergeUtil.mergeExchangeApiDescriptorLists(null, list1);
		Assert.assertEquals(1, merged.size());
		Assert.assertEquals(api1, merged.get(0));
	}
	
	@Test
	public void testMergeExchangeApiDescriptors_NotSameName() {
		ExchangeApiDescriptor api1 = new ExchangeApiDescriptor();
		api1.setName("myApi1");
		ExchangeApiDescriptor api2 = new ExchangeApiDescriptor();
		api2.setName("myApi2");
		try {
			ExchangeDescriptorMergeUtil.mergeExchangeApiDescriptors(api1, api2);
			Assert.fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// expected
			Assert.assertEquals("Cannot merge API groups with different names:'myApi1' and 'myApi2'",
					e.getMessage());
		}
	}
	
	@Test
	public void testMergeExchangeDescriptors_DifferentExchangeNames() {
		ExchangeDescriptor ex1 = new ExchangeDescriptor();
		ex1.setName("ex1");
		ExchangeDescriptor ex2 = new ExchangeDescriptor();
		ex2.setName("ex2");
		try {
			ExchangeDescriptorMergeUtil.mergeExchangeDescriptors(ex1, ex2);
			Assert.fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// expected
			Assert.assertEquals("Cannot merge exchanges with different IDs:'ex1' and 'ex2'", e.getMessage());
		}
	}
	
	@Test
	public void testMergeExchangeDescriptors() {
		ExchangeDescriptor ex1 = new ExchangeDescriptor();
		ex1.setName("ex1");
		ex1.setDescription("Exchange 1 description");
		ex1.setBasePackage("com.x.y.gen");
		ex1.setDocUrl("https://ex1.com/docs");
		
		ExchangeApiDescriptor api1 = new ExchangeApiDescriptor();
		api1.setName("myApi1");
		ex1.setApis(List.of(api1));
		Constant c1 = new Constant();
		c1.setName("const1");
		c1.setValue("value1");
		ex1.setConstants(List.of(c1));
		RateLimitRule rule1 = new RateLimitRule();
		rule1.setId("rule1");
		ex1.setRateLimits(List.of(rule1));
		DefaultConfigProperty prop1 = new DefaultConfigProperty();
		prop1.setName("prop1");
		ex1.setProperties(List.of(prop1));
		
		ExchangeDescriptor ex2 = new ExchangeDescriptor();
		ex2.setName("ex1");
		ex2.setHttpUrl("http://ex1.com");
		ex2.setHttpRequestExecutorFactory("com.x.y.MyHttpRequestExecutorFactory");
		ex2.setHttpRequestInterceptorFactory("com.x.y.MyHttpRequestInterceptorFactory");
		ex2.setWebsocketFactory("com.x.y.MyWebsocketFactory");
		ex2.setWebsocketUrl("ws://ex1.com");
		ex2.setHttpRequestTimeout(1000L);
		ExchangeApiDescriptor api2 = new ExchangeApiDescriptor();
		api2.setName("myApi2");
		ex2.setApis(List.of(api2));
		Constant c2 = new Constant();
		c2.setName("const2");
		c2.setValue("value1");
		ex2.setConstants(List.of(c2));
		RateLimitRule rule2 = new RateLimitRule();
		rule2.setId("rule2");
		ex2.setRateLimits(List.of(rule2));
		DefaultConfigProperty prop2 = new DefaultConfigProperty();
		prop2.setName("prop2");
		ex2.setProperties(List.of(prop2));
		
		ExchangeDescriptor merged = ExchangeDescriptorMergeUtil.mergeExchangeDescriptors(ex1, ex2);
		Assert.assertEquals("ex1", merged.getName());
		Assert.assertEquals("Exchange 1 description", merged.getDescription());
		Assert.assertEquals("com.x.y.gen", merged.getBasePackage());
		Assert.assertEquals("https://ex1.com/docs", merged.getDocUrl());
		Assert.assertEquals("http://ex1.com", merged.getHttpUrl());
		Assert.assertEquals("com.x.y.MyHttpRequestExecutorFactory", merged.getHttpRequestExecutorFactory());
		Assert.assertEquals("com.x.y.MyHttpRequestInterceptorFactory", merged.getHttpRequestInterceptorFactory());
		Assert.assertEquals("com.x.y.MyWebsocketFactory", merged.getWebsocketFactory());
		Assert.assertEquals("ws://ex1.com", merged.getWebsocketUrl());
		Assert.assertEquals(1000L, merged.getHttpRequestTimeout());
		Assert.assertEquals(2, merged.getApis().size());
		Assert.assertEquals(api1, merged.getApis().get(0));
		Assert.assertEquals(api2, merged.getApis().get(1));
		Assert.assertEquals(2, merged.getConstants().size());
		Assert.assertEquals(c1, merged.getConstants().get(0));
		Assert.assertEquals(c2, merged.getConstants().get(1));
		Assert.assertEquals(2, merged.getRateLimits().size());
		Assert.assertEquals(rule1, merged.getRateLimits().get(0));
		Assert.assertEquals(rule2, merged.getRateLimits().get(1));
		Assert.assertEquals(2, merged.getProperties().size());
		Assert.assertEquals(prop1, merged.getProperties().get(0));
		Assert.assertEquals(prop2, merged.getProperties().get(1));
	}
	
	private class MyType {
		String value;

		public MyType(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
		
		@Override
		public String toString() {
			return "'" + value + "'";
		}
		
	}

}
