package com.scz.jxapi.netutils.websocket;

/**
 * Abstract implementation of a {@link WebsocketHook}. It provides a reference
 * to the {@link WebsocketManager} that is passed to the hook in the
 * {@link #init(WebsocketManager)} method.
 */
public abstract class AbstractWebsocketHook implements WebsocketHook {

	/**
	 * The {@link WebsocketManager} instance that is passed to the hook in the
	 * {@link #init(WebsocketManager)} method.
	 */
	protected WebsocketManager websocketManager;
	
	@Override
	public void init(WebsocketManager websocketManager) {
		this.websocketManager = websocketManager;
	}

}
