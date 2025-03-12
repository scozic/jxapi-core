package org.jxapi.exchange;

import org.junit.Assert;
import org.junit.Test;

import org.jxapi.netutils.rest.HttpRequest;
import org.jxapi.netutils.rest.HttpResponse;
import org.jxapi.netutils.rest.RestResponse;
import org.jxapi.netutils.websocket.WebsocketException;
import org.jxapi.netutils.websocket.WebsocketSubscribeRequest;

/**
 * Unit test for {@link ExchangeApiEvent}
 */
public class ExchangeApiEventTest {

	@Test
    public void testGettersAndSetters() {
        ExchangeApiEvent event = new ExchangeApiEvent(ExchangeApiEventType.HTTP_RESPONSE);
        event.setExchangeName("exchangeName");
        event.setExchangeId("exchangeId");
        event.setExchangeApiName("exchangeApiName");
        event.setHttpResponse(new RestResponse<>());
        event.setHttpRequest(new HttpRequest());
        event.setWebsocketMessage("websocketMessage");
        event.setWebsocketSubscribeRequest(new WebsocketSubscribeRequest());
        event.setWebsocketSubscriptionId("websocketSubscriptionId");
        event.setWebsocketError(new WebsocketException("error"));
        
        Assert.assertEquals("exchangeName", event.getExchangeName());
        Assert.assertEquals("exchangeId", event.getExchangeId());
        Assert.assertEquals("exchangeApiName", event.getExchangeApiName());
        Assert.assertEquals(null, event.getEndpoint());
        Assert.assertNotNull(event.getHttpResponse());
        Assert.assertNotNull(event.getHttpRequest());
        Assert.assertEquals("websocketMessage", event.getWebsocketMessage());
        Assert.assertNotNull(event.getWebsocketSubscribeRequest());
        Assert.assertEquals("websocketSubscriptionId", event.getWebsocketSubscriptionId());
        Assert.assertNotNull(event.getWebsocketError());
    }

    @Test
    public void testGetEndpointNull() {
        ExchangeApiEvent event = new ExchangeApiEvent(ExchangeApiEventType.HTTP_RESPONSE);
        Assert.assertNull(event.getEndpoint());
    }
    
    @Test
    public void testToString() {
        HttpRequest request = new HttpRequest();
        request.setEndpoint("myRestEndpoint");
        ExchangeApiEvent event = ExchangeApiEvent.createHttpRequestEvent(request);
        Assert.assertEquals("ExchangeApiEvent{\"endpoint\":\"myRestEndpoint\",\"httpRequest\":{\"endpoint\":\"myRestEndpoint\",\"throttledTime\":0,\"weight\":0},\"type\":\"HTTP_REQUEST\"}", event.toString());
    }

    @Test
    public void testCreateRestResponseEvent() {
        RestResponse<?> response = new RestResponse<>();
        HttpResponse httpResponse = new HttpResponse();
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setEndpoint("myWsEndpoint");
        httpResponse.setRequest(httpRequest);
        response.setHttpResponse(httpResponse);
        ExchangeApiEvent event = ExchangeApiEvent.createRestResponseEvent(response);
        Assert.assertEquals(ExchangeApiEventType.HTTP_RESPONSE, event.getType());
        Assert.assertEquals("myWsEndpoint", event.getEndpoint());
        Assert.assertEquals(response, event.getHttpResponse());
    }

    @Test
    public void testCreateWebsocketMessageEvent() {
        WebsocketSubscribeRequest request = WebsocketSubscribeRequest.create(null, "wsTopic", null);
        ExchangeApiEvent event = ExchangeApiEvent.createWebsocketMessageEvent(request, "myWsMessage");
        Assert.assertEquals(ExchangeApiEventType.WEBSOCKET_MESSAGE, event.getType());
        Assert.assertEquals("wsTopic", event.getWebsocketSubscribeRequest().getTopic());
        Assert.assertEquals("myWsMessage", event.getWebsocketMessage());
    }
    
    @Test
    public void testCreateWebsocketSubcribeEvent() {
    	WebsocketSubscribeRequest request = WebsocketSubscribeRequest.create(null, "wsTopic", null);
        ExchangeApiEvent event = ExchangeApiEvent.createWebsocketSubscribeEvent(request, "myWsSubscriptionId");
        Assert.assertEquals(ExchangeApiEventType.WEBSOCKET_SUBSCRIBE, event.getType());
        Assert.assertEquals("wsTopic", event.getWebsocketSubscribeRequest().getTopic());
        Assert.assertEquals("myWsSubscriptionId", event.getWebsocketSubscriptionId());
    }

    @Test
    public void testCreateWebsocketUnsubscribeEvent() {
    	WebsocketSubscribeRequest request = WebsocketSubscribeRequest.create(null, "wsTopic", null);
        ExchangeApiEvent event = ExchangeApiEvent.createWebsocketUnsubscribeEvent(request, "myWsSubscriptionId");
        Assert.assertEquals(ExchangeApiEventType.WEBSOCKET_UNSUBSCRIBE, event.getType());
        Assert.assertEquals("wsTopic", event.getWebsocketSubscribeRequest().getTopic());
        Assert.assertEquals("myWsSubscriptionId", event.getWebsocketSubscriptionId());
    }

    @Test
    public void testCreateWebsocketErrorEvent() {
        WebsocketException error = new WebsocketException("error");
        ExchangeApiEvent event = ExchangeApiEvent.createWebsocketErrorEvent(error);
        Assert.assertEquals(ExchangeApiEventType.WEBSOCKET_ERROR, event.getType());
        Assert.assertEquals(null, event.getEndpoint());
        Assert.assertEquals(error, event.getWebsocketError());
    }

    @Test
    public void testCreateHttpRequestEvent() {
        HttpRequest request = new HttpRequest();
        request.setEndpoint("myRestEndpoint");
        ExchangeApiEvent event = ExchangeApiEvent.createHttpRequestEvent(request);
        Assert.assertEquals(ExchangeApiEventType.HTTP_REQUEST, event.getType());
        Assert.assertEquals("myRestEndpoint", event.getEndpoint());
    }
}
