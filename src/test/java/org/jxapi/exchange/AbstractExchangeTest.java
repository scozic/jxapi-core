package org.jxapi.exchange;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.jxapi.netutils.rest.ratelimits.RequestThrottler;
import org.jxapi.netutils.rest.ratelimits.RequestThrottlingMode;
import org.jxapi.netutils.websocket.WebsocketSubscribeRequest;

/**
 * Unit test for {@link AbstractExchange}
 */
public class AbstractExchangeTest {

    private TestExchange exchange;

    @Before
    public void setUp() {
        // Initialize the AbstractExchange instance with test data
        String id = "testId";
        String name = "testName";
        String version = "1.0.0";
        Properties properties = new Properties();
        exchange = new TestExchange(id, version, name, properties);
    }

    @Test
    public void testGetProperties() {
        // Test the getProperties() method
        Properties expectedProperties = new Properties();
        Properties actualProperties = exchange.getProperties();
        Assert.assertEquals(expectedProperties, actualProperties);
    }

    @Test
    public void testGetName() {
        Assert.assertEquals("testName", exchange.getName());
    }

    @Test
    public void testGetId() {
        Assert.assertEquals("testId", exchange.getId());
    }
    
    @Test
    public void testGetVersion() {
      Assert.assertEquals("1.0.0", exchange.getVersion());
    }

    @Test
    public void testSubscribeObserver() {
      TestExchangeApi api = new TestExchangeApi("myExchangeApi");
        exchange.addApi(api);
        TestExchangeApiObserver observer = new TestExchangeApiObserver();
        exchange.subscribeObserver(observer);
        ExchangeApiEvent event = ExchangeApiEvent.createWebsocketMessageEvent(WebsocketSubscribeRequest.create(observer, "greetings", null), "hello!");
        api.dispatchApiEvent(event);
        Assert.assertEquals(1, observer.events.size());
        Assert.assertEquals(event, observer.events.get(0));
    }
    
    @Test
    public void testUnsubscribeObserverReturnsFalseWhenObserverNeverSubscribed() {
        ExchangeApi api = new TestExchangeApi("myExchangeApi");
        exchange.addApi(api);
        ExchangeApiObserver observer = new TestExchangeApiObserver();
        Assert.assertFalse(exchange.unsubscribeObserver(observer));
    }
    
    @Test
    public void testUnsubscribeObserverReturnsFalseWhenNoExchangeApiAdded() {
        ExchangeApiObserver observer = new TestExchangeApiObserver();
        Assert.assertFalse(exchange.unsubscribeObserver(observer));
        exchange.subscribeObserver(observer);
        // Will return false because api was not  registered and listener was not removed from any api.  
        Assert.assertFalse(exchange.unsubscribeObserver(observer));
    }

    @Test
    public void testUnsubscribeObserverReturnsTrueWhenSubscribedFromOneExchangeApi() {
        ExchangeApiObserver observer = new TestExchangeApiObserver();
        Assert.assertFalse(exchange.unsubscribeObserver(observer));
        exchange.subscribeObserver(observer);
        // Will return false because api was not  registered and listener was not removed from any api.  
        Assert.assertFalse(exchange.unsubscribeObserver(observer));
        ExchangeApi api = new TestExchangeApi("myExchangeApi");
        exchange.addApi(api);
        exchange.subscribeObserver(observer);
        Assert.assertTrue(exchange.unsubscribeObserver(observer));
    }

    @Test
    public void testAddApi() {
        // Test the addApi() method
        ExchangeApi api = new TestExchangeApi("myExchangeApi");
        ExchangeApi result = exchange.addApi(api);
        Assert.assertEquals(api, result);
        Assert.assertEquals(1, exchange.getApis().size());
        Assert.assertEquals(api, exchange.getApis().get(0));
        
    }
    
    @Test
    public void testSetRequestThrottlingMode() {
        ExchangeApi api1 = exchange.addApi(new TestExchangeApi("myExchangeApi1"));
        ExchangeApi api2 = exchange.addApi(new TestExchangeApi("myExchangeApi2"));
        exchange.setRequestThrottlingMode(RequestThrottlingMode.BLOCK);
        Assert.assertEquals(RequestThrottlingMode.BLOCK, api1.getRequestThrottlingMode());
        Assert.assertEquals(RequestThrottlingMode.BLOCK, api2.getRequestThrottlingMode());
    }
    
    @Test
    public void testSetRequestMaxThrottlingDelay() {
        ExchangeApi api1 = exchange.addApi(new TestExchangeApi("myExchangeApi1"));
        ExchangeApi api2 = exchange.addApi(new TestExchangeApi("myExchangeApi2"));
        exchange.setMaxRequestThrottleDelay(500L);
        Assert.assertEquals(500L, api1.getMaxRequestThrottleDelay());
        Assert.assertEquals(500L, api2.getMaxRequestThrottleDelay());
    }
    
    @Test 
    public void testDispose() {
        ExchangeApi api1 = exchange.addApi(new TestExchangeApi("myExchangeApi1"));
        ExchangeApi api2 = exchange.addApi(new TestExchangeApi("myExchangeApi2"));
        exchange.dispose();
        Assert.assertTrue(api1.isDisposed());
        Assert.assertTrue(api2.isDisposed());
    }

    // Helper class for testing ExchangeApiObserver
    private static class TestExchangeApiObserver implements ExchangeApiObserver {
      
      List<ExchangeApiEvent> events = new ArrayList<>();

    @Override
    public void handleEvent(ExchangeApiEvent event) {
      events.add(event);
    }
    }

    // Helper class for testing ExchangeApi
    private class TestExchangeApi extends AbstractExchangeApi {

    public TestExchangeApi(String apiName) {
      super(apiName, 
            ExchangeStub.INSTANCE, 
            new RequestThrottler("TestApi"), 
            "http://localhost:8080/api", 
            "http://localhost:8080/ws");
    }
    
    @Override
    public void dispatchApiEvent(ExchangeApiEvent event) {
      super.dispatchApiEvent(event);
    }
    }

    // Helper class for testing AbstractExchange
    private static class TestExchange extends AbstractExchange {
        public TestExchange(String id, String version, String name, Properties properties) {
            super(id, version, name, properties, null, null);
        }
        
        @Override
        public <T extends ExchangeApi> T addApi(T api) {
          return super.addApi(api);
        }
    }
}
