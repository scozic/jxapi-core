package com.scz.jxapi.netutils.websocket;

import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scz.jxapi.observability.DefaultObservable;
import com.scz.jxapi.observability.Observable;

/**
 * Abstract class for Websocket implementations. All websocket implementations
 * should extend this class, implementing the abstract methods.
 */
public abstract class AbstractWebsocket implements Websocket {
	
	private static final Logger log = LoggerFactory.getLogger(AbstractWebsocket.class);
	
	protected final AtomicBoolean connected = new AtomicBoolean(false);
	
	private final Observable<RawWebsocketMessageHandler, String> messageObservable = new DefaultObservable<>((handler, message) -> handler.handleWebsocketMessage(message));
	private final Observable<WebsocketErrorHandler, WebsocketException> errorObservable = new DefaultObservable<>((handler, error) -> handler.handleWebsocketError(error));
	
	protected String url;

	@Override
	public final void connect() throws WebsocketException {
		if (log.isDebugEnabled()) {
			log.debug("Connecting WS " + this);
		}
		if (!connected.getAndSet(true)) {
			try {
				doConnect();
			} catch (WebsocketException ex) {
				connected.set(false);
				throw ex;
			}
		}
		if (log.isDebugEnabled()) {
			log.debug("Connected WS " + this);
		}
	}

	@Override
	public final void disconnect() throws WebsocketException {
		if (log.isDebugEnabled()) {
			log.debug("Disonnecting WS " + this);
		}
		if (connected.getAndSet(false)) {
			doDisconnect();
		}
		if (log.isDebugEnabled()) {
			log.debug("Disonnected WS " + this);
		}
	}
	
	@Override
	public final void send(String message) throws WebsocketException {
		if (!isConnected()) {
			throw new WebsocketException("Not connected:" + this);
		}
		if (log.isDebugEnabled()) {
			log.debug(toString() + " > " + message);
		}
		doSend(message);
	}
	
	public final boolean isConnected() {
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
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + url + "]";
	}
	
	/**
	 * Actual implementations must implement this method to establish actual
	 * websocket connection. This method will be called from connect only when this
	 * instance is in 'disconnected' state. {@link #connected} flag will be switched
	 * to true just before calling it. When this method returns, it is assumed that
	 * websocket is ready to send outgoing messages using {@link #send(String)} and
	 * dispatch incoming messages. <br/>
	 * If an error occurs trying to establish websocket link, a
	 * {@link WebsocketException} must be thrown. In this case, {@link #connected}
	 * flag will be set back to <code>false</code> and it is assumed that all
	 * resources needed for connection are disposed before throwing the exception
	 *  and {@link #connect()} can be retried.
	 * 
	 * @throws WebsocketException if failed to establish socket link.
	 */
	protected abstract void doConnect() throws WebsocketException;
	
	/**
	 * Actual implementations must implement this method to disconnect socket link
	 * and dispose all resources associated to it. This method will be called from
	 * {@link #disconnect()} method right after switching {@link #connected} flag to
	 * <code>false</code> Unlike {@link #doConnect()} method, no exception is
	 * expected to be thrown. This means disconnection action should fail.
	 */
	protected abstract void doDisconnect();
	
	/**
	 * Actual implementations must implement this method to write synchronously a message to socket output stream.
	 *  
	 * @param message Bytes of this string will be sent on websocket output stream.
	 * @throws WebsocketException if I/O error occurs when writing to socket or this websocket is not
	 *                            connected.
	 */
	protected abstract void doSend(String message) throws WebsocketException;
	

}
