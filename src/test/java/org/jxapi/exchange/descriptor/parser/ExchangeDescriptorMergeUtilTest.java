package org.jxapi.exchange.descriptor.parser;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.jxapi.exchange.descriptor.gen.ConfigPropertyDescriptor;
import org.jxapi.exchange.descriptor.gen.ConstantDescriptor;
import org.jxapi.exchange.descriptor.gen.ExchangeApiDescriptor;
import org.jxapi.exchange.descriptor.gen.ExchangeDescriptor;
import org.jxapi.exchange.descriptor.gen.HttpClientDescriptor;
import org.jxapi.exchange.descriptor.gen.NetworkDescriptor;
import org.jxapi.exchange.descriptor.gen.RateLimitRuleDescriptor;
import org.jxapi.exchange.descriptor.gen.RestEndpointDescriptor;
import org.jxapi.exchange.descriptor.gen.WebsocketClientDescriptor;
import org.jxapi.exchange.descriptor.gen.WebsocketEndpointDescriptor;
import org.jxapi.exchange.descriptor.gen.WebsocketTopicMatcherDescriptor;
import org.jxapi.pojo.descriptor.Field;
import org.jxapi.pojo.descriptor.Type;

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
    
    ExchangeApiDescriptor api2 = new ExchangeApiDescriptor();
    api2.setName("myApi");
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
    
    ExchangeApiDescriptor merged = ExchangeDescriptorMergeUtil.mergeExchangeApiDescriptors(api1, api2);
    Assert.assertEquals("myApi", merged.getName());
    Assert.assertEquals("My API description", merged.getDescription());
    Assert.assertEquals("http://myapi.com", merged.getHttpUrl());
    Assert.assertEquals(3, merged.getRestEndpoints().size());
    Assert.assertEquals(restApi1, merged.getRestEndpoints().get(0));
    Assert.assertEquals(restApi2, merged.getRestEndpoints().get(1));
    Assert.assertEquals(restApi3, merged.getRestEndpoints().get(2));
    Assert.assertEquals(3, merged.getWebsocketEndpoints().size());
    Assert.assertEquals(wsApi1, merged.getWebsocketEndpoints().get(0));
    Assert.assertEquals(wsApi2, merged.getWebsocketEndpoints().get(1));
    Assert.assertEquals(wsApi3, merged.getWebsocketEndpoints().get(2));
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
    NetworkDescriptor n1 = new NetworkDescriptor.Builder()
        .addToHttpClients(HttpClientDescriptor.builder().name("client1").build())
        .build();
    ex1.setNetwork(n1);
    
    ExchangeDescriptor ex2 = new ExchangeDescriptor();
    ex2.setId("ex1");
    ex2.setVersion("1.0.1");
    ex2.setJxapi("1.0.0");
    ex2.setHttpUrl("http://ex1.com");
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
    NetworkDescriptor n2 = new NetworkDescriptor.Builder()
        .addToWebsocketClients(WebsocketClientDescriptor.builder().name("ws1").build())
        .build();
    ex2.setNetwork(n2);
    
    ExchangeDescriptor merged = ExchangeDescriptorMergeUtil.mergeExchangeDescriptors(ex1, ex2);
    Assert.assertEquals("ex1", merged.getId());
    Assert.assertEquals("1.0.1", merged.getVersion());
    Assert.assertEquals("1.0.0", merged.getJxapi());
    Assert.assertEquals("Exchange 1 description", merged.getDescription());
    Assert.assertEquals("com.x.y.gen", merged.getBasePackage());
    Assert.assertEquals("https://ex1.com/docs", merged.getDocUrl());
    Assert.assertEquals("http://ex1.com", merged.getHttpUrl());
    Assert.assertEquals("com.x.y.gen.MyAfterInitHookFactory", merged.getAfterInitHookFactory());
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
    Assert.assertEquals(1, merged.getNetwork().getHttpClients().size());
    Assert.assertEquals("client1", merged.getNetwork().getHttpClients().get(0).getName());
    Assert.assertEquals(1, merged.getNetwork().getWebsocketClients().size());
    Assert.assertEquals("ws1", merged.getNetwork().getWebsocketClients().get(0).getName());
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
  public void testMergeNetworks() {
    NetworkDescriptor n1 = new NetworkDescriptor.Builder()
        .addToHttpClients(HttpClientDescriptor.builder().name("client1").build())
        .addToWebsocketClients(WebsocketClientDescriptor.builder().name("ws1").build())
        .build();
    NetworkDescriptor n2 = new NetworkDescriptor.Builder()
        .addToHttpClients(HttpClientDescriptor.builder().name("client2").build())
        .addToWebsocketClients(WebsocketClientDescriptor.builder().name("ws2").build())
        .build();
    NetworkDescriptor merged = ExchangeDescriptorMergeUtil.mergeNetworks(n1, n2);
    Assert.assertSame(n1, ExchangeDescriptorMergeUtil.mergeNetworks(n1, null));
    Assert.assertSame(n2, ExchangeDescriptorMergeUtil.mergeNetworks(null, n2));
    Assert.assertEquals(2, merged.getHttpClients().size());
    Assert.assertEquals("client1", merged.getHttpClients().get(0).getName());
    Assert.assertEquals("client2", merged.getHttpClients().get(1).getName());
    Assert.assertEquals(2, merged.getWebsocketClients().size());
    Assert.assertEquals("ws1", merged.getWebsocketClients().get(0).getName());
    Assert.assertEquals("ws2", merged.getWebsocketClients().get(1).getName());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testMergeNetworks_DuplicateHttpClientName() {
    NetworkDescriptor n1 = new NetworkDescriptor.Builder()
        .addToHttpClients(HttpClientDescriptor.builder().name("client1").build())
        .build();
    NetworkDescriptor n2 = new NetworkDescriptor.Builder()
        .addToHttpClients(HttpClientDescriptor.builder().name("client1").build())
        .build();
    ExchangeDescriptorMergeUtil.mergeNetworks(n1, n2);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testMergeNetworks_DuplicateWebsocketClientName() {
    NetworkDescriptor n1 = new NetworkDescriptor.Builder()
        .addToWebsocketClients(WebsocketClientDescriptor.builder().name("ws1").build())
        .build();
    NetworkDescriptor n2 = new NetworkDescriptor.Builder()
        .addToWebsocketClients(WebsocketClientDescriptor.builder().name("ws1").build())
        .build();
    ExchangeDescriptorMergeUtil.mergeNetworks(n1, n2);
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
  
  @Test(expected = IllegalArgumentException.class)
  public void testMergeFields_DifferentFieldNames() {
    Field f1 = Field.builder().name("field1").build();
    Field f2 = Field.builder().name("field2").build();
    ExchangeDescriptorMergeUtil.mergeFields(f1, f2);
  }
	 
  @Test
	public void testMergeFields() {
	    Field f1 = Field.builder()
	      .name("field1")
	      .type(Type.OBJECT)
	      .objectName("com.x.mypojos.MyData")
	      .objectDescription("MyData description")
	      .description("Field 1 description")
	      .implementedInterfaces(List.of("com.example.Interface1"))
	      .property(Field.builder()
            .name("prop1")
            .type(Type.INT)
            .objectDescription("T")
            .build())
	      .property(Field.builder()
            .name("prop2")
            .type(Type.BOOLEAN)
            .build())
	      .build();
	    Field f2 = Field.builder()
	        .name("field1")
	        .description("Field 1 description")
	        .property(Field.builder()
	            .name("prop1")
	            .description("Property 1 description")
	            .build())
	        .property(Field.builder()
	            .name("prop3")
	            .type(Type.BIGDECIMAL)
	            .build())
	        .implementedInterfaces(List.of("com.example.Interface2"))
	        .build();
	    
	    Assert.assertEquals(f1, ExchangeDescriptorMergeUtil.mergeFields(f1, null));
	    Assert.assertEquals(f1, ExchangeDescriptorMergeUtil.mergeFields(null, f1));
	    
	    Field merged = ExchangeDescriptorMergeUtil.mergeFields(f1, f2);
	    Assert.assertEquals("field1", merged.getName());
	    Assert.assertEquals(Type.OBJECT, merged.getType());
	    Assert.assertEquals("com.x.mypojos.MyData", merged.getObjectName());
	    Assert.assertEquals("MyData description", merged.getObjectDescription());
	    Assert.assertEquals("Field 1 description", merged.getDescription());
	    Assert.assertEquals(2, merged.getImplementedInterfaces().size());
	    Assert.assertTrue(merged.getImplementedInterfaces().contains("com.example.Interface1"));
	    Assert.assertTrue(merged.getImplementedInterfaces().contains("com.example.Interface2"));
	    Assert.assertEquals(3, merged.getProperties().size());
	    Assert.assertEquals("prop1", merged.getProperties().get(0).getName());
	    Assert.assertEquals("Property 1 description", merged.getProperties().get(0).getDescription());
      Assert.assertEquals(Type.INT, merged.getProperties().get(0).getType());
      Assert.assertEquals("prop2", merged.getProperties().get(1).getName());
      Assert.assertEquals(Type.BOOLEAN, merged.getProperties().get(1).getType());
      Assert.assertEquals("prop3", merged.getProperties().get(2).getName());
      Assert.assertEquals(Type.BIGDECIMAL, merged.getProperties().get(2).getType());
	  }
	 
    @Test(expected = IllegalArgumentException.class)
    public void testMergeRestEndpointDescriptors_DifferentEndpointNames() {
      RestEndpointDescriptor e1 = RestEndpointDescriptor.builder().name("endpoint1").build();
      RestEndpointDescriptor e2 = RestEndpointDescriptor.builder().name("endpoint2").build();
      ExchangeDescriptorMergeUtil.mergeRestEndpointDescriptors(e1, e2);

    }
    
    @Test
    public void testMergeRestEndpointDescriptors() {
      Field request1 = Field.builder()
          .name("endpoint1Request")
          .type(Type.STRING)
          .implementedInterfaces(List.of("com.example.Interface1"))
          .build();
      Field request2 = Field.builder()
            .name("endpoint1Request")
            .description("Field 1 description")
            .implementedInterfaces(List.of("com.example.Interface2"))
            .build();
      
      Field response1 = Field.builder()
          .name("endpoint1Response")
          .type(Type.INT)
          .implementedInterfaces(List.of("com.example.Interface3"))
          .build();
      Field response2 = Field.builder()
          .name("endpoint1Response")
          .description("Endpoint1 Response description")
          .implementedInterfaces(List.of("com.example.Interface4"))
          .build();
      
      RestEndpointDescriptor e1 = RestEndpointDescriptor.builder()
        .name("endpoint1")
        .httpMethod("GET")
        .addToRateLimits("rule1")
        .url("/api/v1/endpoint1")
        .httpClient("defaultClient")
        .request(request1)
        .response(response1)
        .build();
      RestEndpointDescriptor e2 = RestEndpointDescriptor.builder()
          .name("endpoint1")
          .addToRateLimits("rule2")
          .request(request2)
          .response(response2)
          .description("Endpoint 1 description")
          .docUrl("https://api.exchange.com/docs/endpoint1")
          .build();
      Assert.assertEquals(e1, ExchangeDescriptorMergeUtil.mergeRestEndpointDescriptors(e1, null));
      Assert.assertEquals(e2, ExchangeDescriptorMergeUtil.mergeRestEndpointDescriptors(null, e2));
      RestEndpointDescriptor merged = ExchangeDescriptorMergeUtil.mergeRestEndpointDescriptors(e1, e2);
      Assert.assertEquals("endpoint1", merged.getName());
      Assert.assertEquals("GET", merged.getHttpMethod());
      Assert.assertEquals("/api/v1/endpoint1", merged.getUrl());
      Assert.assertEquals("Endpoint 1 description", merged.getDescription());
      Assert.assertEquals("https://api.exchange.com/docs/endpoint1", merged.getDocUrl());
      Assert.assertEquals("defaultClient", merged.getHttpClient());
      Assert.assertEquals(2, merged.getRateLimits().size());
      Assert.assertTrue(merged.getRateLimits().contains("rule1"));
      Assert.assertTrue(merged.getRateLimits().contains("rule2"));
      Assert.assertNotNull(merged.getRequest());
      Assert.assertEquals("endpoint1Request", merged.getRequest().getName());
      Assert.assertEquals(Type.STRING, merged.getRequest().getType());
      Assert.assertEquals(2, merged.getRequest().getImplementedInterfaces().size());
      Assert.assertTrue(merged.getRequest().getImplementedInterfaces().contains("com.example.Interface1"));
      Assert.assertTrue(merged.getRequest().getImplementedInterfaces().contains("com.example.Interface2"));
      Assert.assertNotNull(merged.getResponse());
      Assert.assertEquals("endpoint1Response", merged.getResponse().getName());
      Assert.assertEquals(Type.INT, merged.getResponse().getType());
      Assert.assertEquals(2, merged.getResponse().getImplementedInterfaces().size());
      Assert.assertTrue(merged.getResponse().getImplementedInterfaces().contains("com.example.Interface3"));
      Assert.assertTrue(merged.getResponse().getImplementedInterfaces().contains("com.example.Interface4"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testMergeWebsocketEndpointDescriptors_DifferentEndpointNames() {
      WebsocketEndpointDescriptor e1 = WebsocketEndpointDescriptor.builder().name("endpoint1").build();
      WebsocketEndpointDescriptor e2 = WebsocketEndpointDescriptor.builder().name("endpoint2").build();
      ExchangeDescriptorMergeUtil.mergeWebsocketEndpointDescriptors(e1, e2);
    }
    
    @Test
    public void testMergeWebsocketEndpointDescriptors() {
      Field request1 = Field.builder()
          .name("endpoint1Request")
          .type(Type.STRING)
          .implementedInterfaces(List.of("com.example.Interface1"))
          .build();
      Field request2 = Field.builder()
            .name("endpoint1Request")
            .description("Field 1 description")
            .implementedInterfaces(List.of("com.example.Interface2"))
            .build();
      
      Field message1 = Field.builder()
          .name("endpoint1Response")
          .type(Type.INT)
          .implementedInterfaces(List.of("com.example.Interface3"))
          .build();
      Field message2 = Field.builder()
          .name("endpoint1Response")
          .description("Endpoint1 Response description")
          .implementedInterfaces(List.of("com.example.Interface4"))
          .build();
      
      WebsocketEndpointDescriptor e1 = WebsocketEndpointDescriptor.builder()
        .name("endpoint1")
        .description("Websocket Endpoint 1 description")
        .request(request1)
        .message(message1)
        .websocketClient("wsClient1")
        .build();
      WebsocketEndpointDescriptor e2 = WebsocketEndpointDescriptor.builder()
          .name("endpoint1")
          .request(request2)
          .message(message2)
          .topic("myTopic")
          .topicMatcher(WebsocketTopicMatcherDescriptor.builder()
              .fieldName("f1")
              .fieldValue("v1")
              .build())
          .docUrl("https://api.exchange.com/docs/ws/endpoint1")
          .build();
      Assert.assertEquals(e1, ExchangeDescriptorMergeUtil.mergeWebsocketEndpointDescriptors(e1, null));
      Assert.assertEquals(e2, ExchangeDescriptorMergeUtil.mergeWebsocketEndpointDescriptors(null, e2));
      WebsocketEndpointDescriptor merged = ExchangeDescriptorMergeUtil.mergeWebsocketEndpointDescriptors(e1, e2);
      Assert.assertEquals("endpoint1", merged.getName());
      Assert.assertEquals("https://api.exchange.com/docs/ws/endpoint1", merged.getDocUrl());
      Assert.assertEquals("Websocket Endpoint 1 description", merged.getDescription());
      Assert.assertEquals("wsClient1", merged.getWebsocketClient());
      Assert.assertEquals("myTopic", merged.getTopic());
      Assert.assertNotNull(merged.getTopicMatcher());
      Assert.assertEquals("f1", merged.getTopicMatcher().getFieldName());
      Assert.assertEquals("v1", merged.getTopicMatcher().getFieldValue());
      Assert.assertNotNull(merged.getRequest());
      Assert.assertEquals("endpoint1Request", merged.getRequest().getName());
      Assert.assertEquals(Type.STRING, merged.getRequest().getType());
      Assert.assertEquals(2, merged.getRequest().getImplementedInterfaces().size());
      Assert.assertTrue(merged.getRequest().getImplementedInterfaces().contains("com.example.Interface1"));
      Assert.assertTrue(merged.getRequest().getImplementedInterfaces().contains("com.example.Interface2"));
      Assert.assertNotNull(merged.getMessage());
      Assert.assertEquals("endpoint1Response", merged.getMessage().getName());
      Assert.assertEquals(Type.INT, merged.getMessage().getType());
      Assert.assertEquals(2, merged.getMessage().getImplementedInterfaces().size());
      Assert.assertTrue(merged.getMessage().getImplementedInterfaces().contains("com.example.Interface3"));
      Assert.assertTrue(merged.getMessage().getImplementedInterfaces().contains("com.example.Interface4"));
    }
}
