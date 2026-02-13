package org.jxapi.exchange;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Properties;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jxapi.netutils.rest.mock.MockHttpRequestExecutorFactory;
import org.jxapi.netutils.rest.mock.MockHttpRequestInterceptorFactory;
import org.jxapi.netutils.rest.ratelimits.RequestThrottler;
import org.jxapi.netutils.websocket.mock.MockWebsocketFactory;
import org.jxapi.netutils.websocket.mock.MockWebsocketHookFactory;

/**
 * Unit tests for {@link AbstractExchange}.
 */
public class AbstractExchangeTest {

    private AbstractExchange exchange;
    private Properties properties;

    @Before
    public void setUp() {
        properties = new Properties();
        properties.setProperty("testKey", "testValue");

        exchange = new TestExchange(false);
    }
    
    @After
    public void tearDown() {
        exchange.dispose();
    }

    @Test
    public void testGetters() {
      assertEquals("testId", exchange.getId());
      assertEquals("TestExchange", exchange.getName());
      assertEquals("http://example.com", exchange.getHttpUrl());
      assertSame(properties, exchange.getProperties());
      assertNotNull(exchange.getNetwork());
    }

    @Test
    public void testGetVersion() {
        assertEquals("1.0", exchange.getVersion());
    }

    @Test
    public void testGetProperties() {
        properties = exchange.getProperties();
        assertNotNull(properties);
        assertEquals("testValue", properties.getProperty("testKey"));
    }

    @Test
    public void testAddApi() {
        ExchangeApi api = new ExchangeApi() {
        };
        exchange.addApi("api1", api);
        assertEquals(1, exchange.getApis().size());
        assertSame(api, exchange.getApis().get(0));
    }

    @Test
    public void testSubscribeAndUnsubscribeObserver() {
        ExchangeObserver observer = event -> {};
        exchange.subscribeObserver(observer);

        assertTrue(exchange.unsubscribeObserver(observer));
        assertFalse(exchange.unsubscribeObserver(observer));
    }

    @Test
    public void testDispatchApiEvent() {
        ExchangeEvent event = new ExchangeEvent(ExchangeEventType.HTTP_REQUEST);
        exchange.dispatchApiEvent(event);
        assertEquals(ExchangeEventType.HTTP_REQUEST, event.getType());
        assertEquals("testId", event.getExchangeId());
        assertEquals("TestExchange", event.getExchangeName());
    }

    @Test
    public void testDispose() {
        exchange.dispose();
        assertTrue(exchange.isDisposed());
    }
    
    @Test
    public void testCreateHttpClientWithDefaultExecutorAndNoInterceptor() {
        // Act
        exchange.createHttpClient("testClient", null, null, 5000L);

        // Assert
        assertNotNull(exchange.getHttpClient("testClient"));
        
        
    }
    
    @Test
    public void testCreateHttpClientWithCustomExecutorAndInterceptor() {
      
      // Act
      exchange.createHttpClient(
          "customClient", 
          MockHttpRequestInterceptorFactory.class.getName(), 
          MockHttpRequestExecutorFactory.class.getName(), 
          5000L);

      // Assert
      assertNotNull(exchange.getHttpClient("customClient"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetHttpClientThrowsExceptionForUnregisteredClient() {
        // Act
        exchange.getHttpClient("nonExistentClient");
    }

    @Test
    public void testCreateWebsocketClientWithDefaultFactory() {
        // Act
        exchange.createWebsocketClient("testWebsocket", null, null, null);

        // Assert
        assertNotNull(exchange.getNetwork().getWebsocket("testWebsocket"));
    }

    @Test
    public void testCreateWebsocketClientWithCustomFactory() {
        // Arrange
        String customWebsocketFactoryClass = MockWebsocketFactory.class.getName();
        String customWebsocketHookFactoryClass = MockWebsocketHookFactory.class.getName();

        // Act
        exchange.createWebsocketClient("customWebsocket", "ws://example.com", customWebsocketFactoryClass, customWebsocketHookFactoryClass);

        // Assert
        assertNotNull(exchange.getNetwork().getWebsocket("customWebsocket"));
    }
    
    @Test
    public void testAfterInit() {
      exchange.afterInit(null);
      exchange.afterInit(MockExchangeHookFactory.class.getName());
      MockExchangeHook hook = (MockExchangeHook) exchange.getProperties().get(MockExchangeHook.MOCK_EXCHANGE_HOOK_PROPERTY);
      assertNotNull(hook);
      Assert.assertEquals(1, hook.size());
      Assert.assertSame(exchange, hook.pop());
    }
    
    @Test
    public void testCreateWithRequestThrottler() {
      TestExchange rateLimitedExchange = new TestExchange(true);
      RequestThrottler throttler = rateLimitedExchange.getRequestThrottler();
      Assert.assertNotNull(throttler);
      rateLimitedExchange.dispose();
      Assert.assertTrue(throttler.isDisposed());
    }
    
    private class TestExchange extends AbstractExchange {
      
      public TestExchange(boolean hasRateLimiting) {
        super("testId", "1.0", "TestExchange", AbstractExchangeTest.this.properties, "http://example.com", hasRateLimiting);
      }

      @Override
      public void dispose() {
        super.dispose();
      }
      
      public RequestThrottler getRequestThrottler() {
        return this.requestThrottler;
      }
    }
    
    
}

