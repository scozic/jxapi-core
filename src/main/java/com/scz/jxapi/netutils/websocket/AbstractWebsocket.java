package com.scz.jxapi.netutils.websocket;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractWebsocket implements Websocket {
	
	private static final Logger log = LoggerFactory.getLogger(AbstractWebsocket.class);
	
	protected final AtomicBoolean connected = new AtomicBoolean(false);
	
	private final List<RawWebsocketMessageHandler> messageHandlers = new ArrayList<>();
	private final List<WebsocketErrorHandler> errorHandlers = new ArrayList<>();
	
	protected String url;

	@Override
	public void connect() throws WebsocketException {
		if (log.isDebugEnabled()) {
			log.debug("Connecting WS " + this);
		}
		doConnect();
		connected.set(true);
		if (log.isDebugEnabled()) {
			log.debug("Connected WS " + this);
		}
	}

	@Override
	public void disconnect() throws WebsocketException {
		if (log.isDebugEnabled()) {
			log.debug("Disonnecting WS " + this);
		}
		connected.set(false);
		doDisconnect();
		if (log.isDebugEnabled()) {
			log.debug("Disonnected WS " + this);
		}
	}
	
	@Override
	public void send(String message) throws WebsocketException {
		if (!isConnected()) {
			throw new WebsocketException("Not connected:" + this);
		}
		if (log.isDebugEnabled()) {
			log.debug(toString() + " > " + message);
		}
		doSend(message);
	}
	
	public boolean isConnected() {
		return connected.get();
	}

	@Override
	public void addMessageHandler(RawWebsocketMessageHandler messageHandler) {
		messageHandlers.add(messageHandler);
	}

	@Override
	public boolean removeMessageHandler(RawWebsocketMessageHandler messageHandler) {
		return messageHandlers.remove(messageHandler);
	}
	
	@Override
	public void addErrorHandler(WebsocketErrorHandler errorHandler) {
		this.errorHandlers.add(errorHandler);
	}
	
	@Override
	public boolean removeErrorHandler(WebsocketErrorHandler errorHandler) {
		return this.errorHandlers.remove(errorHandler);
	}
	
	@Override
	public void setUrl(String url) {
		this.url = url;
	}
	
	@Override
	public String getUrl() {
		return this.url;
	}
	
	protected void dispatchMessage(String message) {
		messageHandlers.forEach(h -> h.handleWebsocketMessage(message));
	}
	
	protected void dispatchError(String msg, Throwable t) {
		dispatchError(new WebsocketException(msg, t));
	}
	
	protected void dispatchError(WebsocketException error) {
		errorHandlers.forEach(h -> h.handleWebsocketError(error));
	}
	
	public String toString() {
		return getClass().getSimpleName() + "[" + url + "]";
	}
	
	protected abstract void doConnect() throws WebsocketException;
	protected abstract void doDisconnect() throws WebsocketException;
	protected abstract void doSend(String message) throws WebsocketException;
	

}
