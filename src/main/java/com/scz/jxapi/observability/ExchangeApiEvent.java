package com.scz.jxapi.observability;

import com.scz.jxapi.netutils.rest.HttpRequest;
import com.scz.jxapi.netutils.rest.RestResponse;
import com.scz.jxapi.netutils.websocket.WebsocketException;
import com.scz.jxapi.netutils.websocket.WebsocketSubscribeRequest;
import com.scz.jxapi.util.EncodingUtil;

public class ExchangeApiEvent {
	
	public static ExchangeApiEvent createRestResponseEvent(RestResponse<?> response) {
		ExchangeApiEvent event = new ExchangeApiEvent(ExchangeApiEventType.HTTP_RESPONSE);
		if (response != null) {
			event.setEndpointName(response.getEndpoint());
			event.setHttpResponse(response);
		}
		return event;
	}
	
	public static ExchangeApiEvent createRestRequestEvent(HttpRequest request) {
		ExchangeApiEvent event = new ExchangeApiEvent(ExchangeApiEventType.HTTP_REQUEST);
		if (request != null) {
			event.setEndpointName(request.getEndpoint());
			event.setHttpRequest(request);
		}
		return event;
	}
	
	public static ExchangeApiEvent createWebsocketMessageEvent(String endpointName, String msg) {
		ExchangeApiEvent event = new ExchangeApiEvent(ExchangeApiEventType.WEBSOCKET_MESSAGE);
		event.setWebsocketMessage(msg);
		return event;
	}
	
	public static ExchangeApiEvent createWebsocketSubscribeEvent(WebsocketSubscribeRequest request, 
																 String subId) {
		ExchangeApiEvent event = new ExchangeApiEvent(ExchangeApiEventType.WEBSOCKET_SUBSCRIBE);
		event.setWebsocketSubscribeRequest(request);
		event.setWebsocketSubscriptionId(subId);
		return event;
	}
	
	public static ExchangeApiEvent createWebsocketUnsubscribeEvent(String subId) {
		ExchangeApiEvent event = new ExchangeApiEvent(ExchangeApiEventType.WEBSOCKET_SUBSCRIBE);
		event.setWebsocketSubscriptionId(subId);
		return event;
	}
	
	public static ExchangeApiEvent createWebsocketErrorEvent(WebsocketException error) {
		ExchangeApiEvent event = new ExchangeApiEvent(ExchangeApiEventType.WEBSOCKET_ERROR);
		event.setWebsocketError(error);
		return event;
	}
	
	private final ExchangeApiEventType type;
	private String exchangeName;
	private String exchangeId;
	private String exchangeApiName;
	private String endpointName;
	private HttpRequest httpRequest;
	private RestResponse<?> httpResponse;
	private WebsocketSubscribeRequest websocketSubscribeRequest;
	private String websocketMessage;
	private WebsocketException websocketError;
	private String websocketSubscriptionId;

	public ExchangeApiEvent(ExchangeApiEventType eventType) {
		this.type = eventType;
	}
	
	public ExchangeApiEventType getType() {
		return type;
	}

	public String getExchangeName() {
		return exchangeName;
	}

	public void setExchangeName(String exchangeName) {
		this.exchangeName = exchangeName;
	}

	public String getExchangeApiName() {
		return exchangeApiName;
	}

	public void setExchangeApiName(String exchangeApiName) {
		this.exchangeApiName = exchangeApiName;
	}

	public String getEndpointName() {
		return endpointName;
	}

	public void setEndpointName(String endpointName) {
		this.endpointName = endpointName;
	}

	public HttpRequest getHttpRequest() {
		return httpRequest;
	}

	public void setHttpRequest(HttpRequest httpRequest) {
		this.httpRequest = httpRequest;
	}

	public RestResponse<?> getHttpResponse() {
		return httpResponse;
	}

	public void setHttpResponse(RestResponse<?> httpResponse) {
		this.httpResponse = httpResponse;
	}

	public WebsocketSubscribeRequest getWebsocketSubscribeRequest() {
		return websocketSubscribeRequest;
	}

	public void setWebsocketSubscribeRequest(WebsocketSubscribeRequest websocketSubscribeRequest) {
		this.websocketSubscribeRequest = websocketSubscribeRequest;
	}

	public String getWebsocketMessage() {
		return websocketMessage;
	}

	public void setWebsocketMessage(String websocketMessage) {
		this.websocketMessage = websocketMessage;
	}

	public WebsocketException getWebsocketError() {
		return websocketError;
	}

	public void setWebsocketError(WebsocketException websocketError) {
		this.websocketError = websocketError;
	}
	
	public String getWebsocketSubscriptionId() {
		return websocketSubscriptionId;
	}

	public void setWebsocketSubscriptionId(String websocketSubscriptionId) {
		this.websocketSubscriptionId = websocketSubscriptionId;
	}
	
	public String getExchangeId() {
		return exchangeId;
	}

	public void setExchangeId(String exchangeId) {
		this.exchangeId = exchangeId;
	}

	public String toString() {
		return EncodingUtil.pojoToString(this);
	}

}
