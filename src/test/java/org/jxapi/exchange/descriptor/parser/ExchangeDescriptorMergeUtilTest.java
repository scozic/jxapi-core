package org.jxapi.exchange.descriptor.parser;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.jxapi.exchange.descriptor.gen.ConfigPropertyDescriptor;
import org.jxapi.exchange.descriptor.gen.ConstantDescriptor;
import org.jxapi.exchange.descriptor.gen.ExchangeApiDescriptor;
import org.jxapi.exchange.descriptor.gen.ExchangeDescriptor;
import org.jxapi.exchange.descriptor.gen.RateLimitRuleDescriptor;
import org.jxapi.exchange.descriptor.gen.RestEndpointDescriptor;
import org.jxapi.exchange.descriptor.gen.WebsocketEndpointDescriptor;

/**
 * Unit test for {@link ExchangeDescriptorMergeUtil}
 */
public class ExchangeDescriptorMergeUtilTest {
  
  @Test
  public void testMergeExchangeApiDescriptors() {
    ExchangeApiDescriptor api1 = new ExchangeApiDescriptor();
    api1.setName("myApi");
    api1.setDescription("My API description");
    api1.setHttpUrl("http://myapi.com");
    api1.setWebsocketUrl("ws://myapi.com");
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
    List<RateLimitRuleDescriptor> rateLimitRules1 = new ArrayList<>();
    RateLimitRuleDescriptor rule1 = new RateLimitRuleDescriptor();
    rule1.setId("rule1");
    rateLimitRules1.add(rule1);
    api1.setRateLimits(rateLimitRules1);
    
    ExchangeApiDescriptor api2 = new ExchangeApiDescriptor();
    api2.setName("myApi");
    api2.setHttpRequestExecutorFactory("com.x.y.MyHttpRequestExecutorFactory");
    api2.setHttpRequestInterceptorFactory("com.x.y.MyHttpRequestInterceptorFactory");
    api2.setWebsocketFactory("com.x.y.MyWebsocketFactory");
    api2.setHttpRequestTimeout(1000L);
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
    List<RateLimitRuleDescriptor> rateLimitRules2 = new ArrayList<>();
    RateLimitRuleDescriptor rule2 = new RateLimitRuleDescriptor();
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
    Assert.assertEquals(1000L, merged.getHttpRequestTimeout().longValue());
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
    ex1.setId("ex1");
    ExchangeDescriptor ex2 = new ExchangeDescriptor();
    ex2.setId("ex2");
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
    ex1.setId("ex1");
    ex1.setDescription("Exchange 1 description");
    ex1.setBasePackage("com.x.y.gen");
    ex1.setDocUrl("https://ex1.com/docs");
    ex1.setAfterInitHookFactory("com.x.y.gen.MyAfterInitHookFactory");
    
    ExchangeApiDescriptor api1 = new ExchangeApiDescriptor();
    api1.setName("myApi1");
    ex1.setApis(List.of(api1));
    ConstantDescriptor c1 = new ConstantDescriptor();
    c1.setName("const1");
    c1.setValue("value1");
    ex1.setConstants(List.of(c1));
    RateLimitRuleDescriptor rule1 = new RateLimitRuleDescriptor();
    rule1.setId("rule1");
    ex1.setRateLimits(List.of(rule1));
    ConfigPropertyDescriptor prop1 = new ConfigPropertyDescriptor();
    prop1.setName("prop1");
    ex1.setProperties(List.of(prop1));
    ConfigPropertyDescriptor demoProp1 = new ConfigPropertyDescriptor();
    demoProp1.setName("demoProp1");
    
    ExchangeDescriptor ex2 = new ExchangeDescriptor();
    ex2.setId("ex1");
    ex2.setVersion("1.0.1");
    ex2.setJxapi("1.0.0");
    ex2.setHttpUrl("http://ex1.com");
    ex2.setHttpRequestExecutorFactory("com.x.y.MyHttpRequestExecutorFactory");
    ex2.setHttpRequestInterceptorFactory("com.x.y.MyHttpRequestInterceptorFactory");
    ex2.setWebsocketFactory("com.x.y.MyWebsocketFactory");
    ex2.setWebsocketUrl("ws://ex1.com");
    ex2.setHttpRequestTimeout(1000L);
    ExchangeApiDescriptor api2 = new ExchangeApiDescriptor();
    api2.setName("myApi2");
    ex2.setApis(List.of(api2));
    ConstantDescriptor c2 = new ConstantDescriptor();
    c2.setName("const2");
    c2.setValue("value1");
    ex2.setConstants(List.of(c2));
    RateLimitRuleDescriptor rule2 = new RateLimitRuleDescriptor();
    rule2.setId("rule2");
    ex2.setRateLimits(List.of(rule2));
    ConfigPropertyDescriptor prop2 = new ConfigPropertyDescriptor();
    prop2.setName("prop2");
    ex2.setProperties(List.of(prop2));
    ConfigPropertyDescriptor demoProp2 = new ConfigPropertyDescriptor();
    demoProp2.setName("demoProp2");
    
    ExchangeDescriptor merged = ExchangeDescriptorMergeUtil.mergeExchangeDescriptors(ex1, ex2);
    Assert.assertEquals("ex1", merged.getId());
    Assert.assertEquals("1.0.1", merged.getVersion());
    Assert.assertEquals("1.0.0", merged.getJxapi());
    Assert.assertEquals("Exchange 1 description", merged.getDescription());
    Assert.assertEquals("com.x.y.gen", merged.getBasePackage());
    Assert.assertEquals("https://ex1.com/docs", merged.getDocUrl());
    Assert.assertEquals("http://ex1.com", merged.getHttpUrl());
    Assert.assertEquals("com.x.y.MyHttpRequestExecutorFactory", merged.getHttpRequestExecutorFactory());
    Assert.assertEquals("com.x.y.MyHttpRequestInterceptorFactory", merged.getHttpRequestInterceptorFactory());
    Assert.assertEquals("com.x.y.MyWebsocketFactory", merged.getWebsocketFactory());
    Assert.assertEquals("ws://ex1.com", merged.getWebsocketUrl());
    Assert.assertEquals("com.x.y.gen.MyAfterInitHookFactory", merged.getAfterInitHookFactory());
    Assert.assertEquals(1000L, merged.getHttpRequestTimeout().longValue());
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
  
  @Test
  public void testMergeConstants() {
    Assert.assertEquals(List.of(), ExchangeDescriptorMergeUtil.mergeConstants(null, null));
    
    ConstantDescriptor c1 = new ConstantDescriptor();
    c1.setName("const1");
    c1.setDescription("Constant 1 description");
    c1.setValue("value1");
    
    ConstantDescriptor c2 = new ConstantDescriptor();
    c2.setDescription("Constant 2 description");
    c2.setName("const2");
    
    ConstantDescriptor c3 = new ConstantDescriptor();
    c3.setName("const3");
    c3.setName("Constant3 description");
    c3.setValue("value3");
    
    Assert.assertEquals(List.of(c1, c2, c3), 
                        ExchangeDescriptorMergeUtil.mergeConstants(
                            List.of(c1), 
                            List.of(c2, c3)));
    
    ConstantDescriptor c1b = new ConstantDescriptor();
    c1b.setName("const1");
    c1b.setDescription("Constant 1 description");
    c1b.setValue("value1");
    
    Assert.assertEquals(List.of(c1, c2), 
        ExchangeDescriptorMergeUtil.mergeConstants(
            List.of(c1), 
            List.of(c2, c1b)));
  }
  
  @Test
  public void testMergeConstantGroups() {
    ConstantDescriptor c1 = ConstantDescriptor.builder()
        .name("const1")
        .description("Constant 1 description")
        .value("value1").build();
    
    ConstantDescriptor c1b = ConstantDescriptor.builder()
        .name("const1")
        .description("Constant 1 description")
        .value("value1")
        .build();
    
    ConstantDescriptor c2 = ConstantDescriptor.builder()
        .name("const2")
        .description("Constant 2 description")
        .build();
    
    ConstantDescriptor c3 = ConstantDescriptor.builder()
        .name("const3")
        .description("Constant3 description")
        .value("value3")
        .build();
    
    ConstantDescriptor c4 = ConstantDescriptor.builder()
        .name("const4")
        .description("Constant 4 description")
        .value("value4")
        .build();
    
    ConstantDescriptor g1 = ConstantDescriptor.builder()
        .name("group1")
        .description("Group 1 description")
        .constants(List.of(c1, c2)).build();
    
    ConstantDescriptor g1b = ConstantDescriptor.builder()
        .name("group1")
        .description("Group 1 description")
        .constants(List.of(c3, c1b)).build();
    
    ConstantDescriptor g2 = ConstantDescriptor.builder()
        .name("group2")
        .description("Group 2 description")
        .constants(List.of(c4)).build();
    
    Assert.assertEquals(List.of(c1, c2, c3), 
                        ExchangeDescriptorMergeUtil.mergeConstants(
                            List.of(c1), 
                            List.of(c2, c3)));

    Assert.assertEquals(List.of(c4, g1, g2), 
        ExchangeDescriptorMergeUtil.mergeConstants(
            List.of(c4, g1), 
            List.of(g1b, g2)));
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testMergeConstants_SameNameForASingleAndAGroupConstant() {
    ConstantDescriptor c1 = ConstantDescriptor.builder()
        .name("const1")
        .description("Constant 1 description")
        .value("value1")
        .build();
    
    
    ConstantDescriptor c2 = ConstantDescriptor.builder()
        .name("const2")
        .description("Constant 2 description")
        .build();
    
    ConstantDescriptor g1 = ConstantDescriptor.builder()
        .name("const1")
        .description("Constant 1 description")
        .value("value1")
        .constants(List.of(c2)).build();
    
    ExchangeDescriptorMergeUtil.mergeConstants(
        List.of(c1), 
        List.of(g1));
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testMergeConstants_SameNameForASingleAndAGroupConstan2() {
    ConstantDescriptor c1 = ConstantDescriptor.builder()
        .name("const1")
        .description("Constant 1 description")
        .value("value1")
        .build();
    
    ConstantDescriptor c2 = ConstantDescriptor.builder()
        .name("const2")
        .description("Constant 2 description")
        .build();
    
    ConstantDescriptor g1 = ConstantDescriptor.builder()
        .name("const1")
        .description("Constant 1 description")
        .constants(List.of(c2))
        .build();
    
    ExchangeDescriptorMergeUtil.mergeConstants(
        List.of(g1), 
        List.of(c1));
  }
  
}
