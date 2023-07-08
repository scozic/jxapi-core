package com.scz.jxapi.netutils.websocket;

import com.scz.jxapi.netutils.deserialization.MessageDeserializer;
import com.scz.jxapi.util.HasProperties;

/**
 * Base {@link WebsocketEndpointFactory} implementation relying on a
 * {@link WebsocketManager}. The {@link WebsocketManager} instance can be passed
 * to constructor, or initialized later, but it should be available after
 * {@link #setApi(java.util.Properties)} has been called, or exception
 * will be thrown from {@link #createWebsocketEndpoint(MessageDeserializer)}
 * calls.
 * 
 */
public class AbstractWebsocketEndpointFactory implements WebsocketEndpointFactory {
	
	protected WebsocketManager websocketManager = null;
	protected HasProperties api;
	
	public AbstractWebsocketEndpointFactory() {
		this(null);
	}
	
	public AbstractWebsocketEndpointFactory(WebsocketManager websocketManager) {
		this.websocketManager = websocketManager;
	}
	
	public void setApi(HasProperties api) {
		this.api = api;
	}
	

	@Override
	public <S extends WebsocketSubscribeParameters, M> WebsocketEndpoint<S, M> createWebsocketEndpoint(MessageDeserializer<M> messageDeserializer) {
		if (websocketManager == null) {
			throw new IllegalStateException("null WebsocketManager");
		}
		return new DefaultWebsocketEndpoint<>(websocketManager, messageDeserializer);
	}

}
