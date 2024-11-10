package com.scz.jxapi.exchange;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.scz.jxapi.netutils.websocket.WebsocketSubscribeRequest;

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
        Properties properties = new Properties();
        exchange = new TestExchange(id, name, properties);
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
			super(apiName, exchange.getName(), exchange.getId(), exchange.getProperties());
		}
		
		@Override
		public void dispatchApiEvent(ExchangeApiEvent event) {
			super.dispatchApiEvent(event);
		}
    }

    // Helper class for testing AbstractExchange
    private static class TestExchange extends AbstractExchange {
        public TestExchange(String id, String name, Properties properties) {
            super(id, name, properties);
        }
        
        @Override
        public <T extends ExchangeApi> T addApi(T api) {
        	return super.addApi(api);
        }
    }
}
