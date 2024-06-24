package com.scz.jxapi.netutils.websocket;

import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scz.jxapi.observability.DefaultObservable;
import com.scz.jxapi.observability.Observable;

public abstract class AbstractWebsocket implements Websocket {
	
	private static final Logger log = LoggerFactory.getLogger(AbstractWebsocket.class);
	
	protected final AtomicBoolean connected = new AtomicBoolean(false);
	
	private final Observable<RawWebsocketMessageHandler, String> messageObservable = new DefaultObservable<>((handler, message) -> handler.handleWebsocketMessage(message));
	private final Observable<WebsocketErrorHandler, WebsocketException> errorObservable = new DefaultObservable<>((handler, error) -> handler.handleWebsocketError(error));
	
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
		messageObservable.subscribe(messageHandler);
	}

	@Override
	public boolean removeMessageHandler(RawWebsocketMessageHandler messageHandler) {
		return messageObservable.unsubscribe(messageHandler);
	}
	
	@Override
	public void addErrorHandler(WebsocketErrorHandler errorHandler) {
		this.errorObservable.subscribe(errorHandler);
	}
	
	@Override
	public boolean removeErrorHandler(WebsocketErrorHandler errorHandler) {
		return this.errorObservable.unsubscribe(errorHandler);
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
		messageObservable.dispatch(message);
	}
	
	protected void dispatchError(String msg, Throwable t) {
		dispatchError(new WebsocketException(msg, t));
	}
	
	protected void dispatchError(WebsocketException error) {
		errorObservable.dispatch(error);
	}
	
	public String toString() {
		return getClass().getSimpleName() + "[" + url + "]";
	}
	
	protected abstract void doConnect() throws WebsocketException;
	protected abstract void doDisconnect() throws WebsocketException;
	protected abstract void doSend(String message) throws WebsocketException;
	

}
