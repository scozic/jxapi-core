package org.jxapi.exchange;

import org.junit.Assert;
import org.junit.Test;

import org.jxapi.netutils.rest.HttpRequest;
import org.jxapi.netutils.rest.HttpResponse;
import org.jxapi.netutils.rest.RestResponse;
import org.jxapi.netutils.websocket.WebsocketException;
import org.jxapi.netutils.websocket.WebsocketSubscribeRequest;

/**
 * Unit test for {@link ExchangeEvent} and {@link ExchangeApiEventToStringJsonSerializer} through the {@link ExchangeEvent#toString()} method.
 */
public class ExchangeEventTest {

  @Test
    public void testGettersAndSetters() {
        ExchangeEvent event = new ExchangeEvent(ExchangeEventType.HTTP_RESPONSE);
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
        ExchangeEvent event = new ExchangeEvent(ExchangeEventType.HTTP_RESPONSE);
        Assert.assertNull(event.getEndpoint());
    }
    
    @Test
    public void testToString() {
        HttpRequest request = new HttpRequest();
        request.setEndpoint("myRestEndpoint");
        ExchangeEvent event = ExchangeEvent.createHttpRequestEvent(request);
        Assert.assertEquals("{\"type\":\"HTTP_REQUEST\",\"endpoint\":\"myRestEndpoint\",\"httpRequest\":{\"endpoint\":\"myRestEndpoint\"}}", 
                            event.toString());
        event = ExchangeEvent.createWebsocketErrorEvent(new WebsocketException("error"));
        event.setExchangeName("myExchange");
        event.setExchangeId("myExchangeId");
        event.setExchangeApiName("myExchangeApi");
        Assert.assertEquals("{\"type\":\"WEBSOCKET_ERROR\",\"exchangeName\":\"myExchange\",\"exchangId\":\"myExchangeId\",\"exchangeName\":\"myExchange\",\"exchangApiName\":\"myExchangeApi\",\"websocketError\":\"org.jxapi.netutils.websocket.WebsocketException: error\"}",
                            event.toString());
    }

    @Test
    public void testCreateRestResponseEvent() {
        RestResponse<?> response = new RestResponse<>();
        HttpResponse httpResponse = new HttpResponse();
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setEndpoint("myWsEndpoint");
        httpResponse.setRequest(httpRequest);
        response.setHttpResponse(httpResponse);
        ExchangeEvent event = ExchangeEvent.createRestResponseEvent(response);
        Assert.assertEquals(ExchangeEventType.HTTP_RESPONSE, event.getType());
        Assert.assertEquals("myWsEndpoint", event.getEndpoint());
        Assert.assertEquals(response, event.getHttpResponse());
    }

    @Test
    public void testCreateWebsocketMessageEvent() {
        WebsocketSubscribeRequest request = WebsocketSubscribeRequest.create(null, "wsTopic", null);
        ExchangeEvent event = ExchangeEvent.createWebsocketMessageEvent(request, "myWsMessage");
        Assert.assertEquals(ExchangeEventType.WEBSOCKET_MESSAGE, event.getType());
        Assert.assertEquals("wsTopic", event.getWebsocketSubscribeRequest().getTopic());
        Assert.assertEquals("myWsMessage", event.getWebsocketMessage());
    }
    
    @Test
    public void testCreateWebsocketSubcribeEvent() {
      WebsocketSubscribeRequest request = WebsocketSubscribeRequest.create(null, "wsTopic", null);
        ExchangeEvent event = ExchangeEvent.createWebsocketSubscribeEvent(request, "myWsSubscriptionId");
        Assert.assertEquals(ExchangeEventType.WEBSOCKET_SUBSCRIBE, event.getType());
        Assert.assertEquals("wsTopic", event.getWebsocketSubscribeRequest().getTopic());
        Assert.assertEquals("myWsSubscriptionId", event.getWebsocketSubscriptionId());
    }

    @Test
    public void testCreateWebsocketUnsubscribeEvent() {
      WebsocketSubscribeRequest request = WebsocketSubscribeRequest.create(null, "wsTopic", null);
        ExchangeEvent event = ExchangeEvent.createWebsocketUnsubscribeEvent(request, "myWsSubscriptionId");
        Assert.assertEquals(ExchangeEventType.WEBSOCKET_UNSUBSCRIBE, event.getType());
        Assert.assertEquals("wsTopic", event.getWebsocketSubscribeRequest().getTopic());
        Assert.assertEquals("myWsSubscriptionId", event.getWebsocketSubscriptionId());
    }

    @Test
    public void testCreateWebsocketErrorEvent() {
        WebsocketException error = new WebsocketException("error");
        ExchangeEvent event = ExchangeEvent.createWebsocketErrorEvent(error);
        Assert.assertEquals(ExchangeEventType.WEBSOCKET_ERROR, event.getType());
        Assert.assertEquals(null, event.getEndpoint());
        Assert.assertEquals(error, event.getWebsocketError());
    }

    @Test
    public void testCreateHttpRequestEvent() {
        HttpRequest request = new HttpRequest();
        request.setEndpoint("myRestEndpoint");
        ExchangeEvent event = ExchangeEvent.createHttpRequestEvent(request);
        Assert.assertEquals(ExchangeEventType.HTTP_REQUEST, event.getType());
        Assert.assertEquals("myRestEndpoint", event.getEndpoint());
    }
}
