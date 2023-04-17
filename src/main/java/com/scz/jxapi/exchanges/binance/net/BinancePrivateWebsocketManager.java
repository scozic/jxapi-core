package com.scz.jxapi.exchanges.binance.net;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scz.jxapi.netutils.websocket.spring.SpringWebsocketManager;

public class BinancePrivateWebsocketManager extends SpringWebsocketManager {
	
	private static final Logger log = LoggerFactory.getLogger(BinancePrivateWebsocketManager.class);
	
	public static final long KEEP_ALIVE_LISTEN_KEY_INTERVAL = 30000L;
	
	private final BinanceListenKeyApi listenKeyApi;
	
	private String currentListenKey;
	
	private AtomicBoolean keepAliveListenKeyCancelled;

	public BinancePrivateWebsocketManager(String baseUrl, BinanceListenKeyApi listenKeyApi) {
		super(baseUrl);
		this.listenKeyApi = listenKeyApi;
	}
	
	@Override
	protected URI getHandShakeURI() throws IOException {
		try {
			createListenKey();
			return new URI(baseUrl + "/" + currentListenKey);
		} catch (URISyntaxException e) {
			throw new IOException("Error creating URI for websocket base URL:" + baseUrl);
		}
	}
	 
	private void createListenKey() throws IOException {
		if (log.isDebugEnabled()) {
			log.debug("Creating listen key");
		}
		this.currentListenKey = listenKeyApi.getListenKey();
		if (log.isDebugEnabled()) {
			log.debug("Got listen key:" + this.currentListenKey);
		}
	}
	
	@Override
	protected void doConnect() throws IOException {
		super.doConnect();
		if (keepAliveListenKeyCancelled != null) {
			keepAliveListenKeyCancelled.set(true);
		}
		keepAliveListenKeyCancelled = new AtomicBoolean(false);
		scheduleKeepAliveListenKeyTask(keepAliveListenKeyCancelled);
	}
	
	@Override
	protected void doDisconnect() throws IOException {
		super.doDisconnect();
		keepAliveListenKeyCancelled.set(true);
		if (currentListenKey != null) {
			try {
				listenKeyApi.deleteListenKey(currentListenKey);
			} catch (IOException ex) {
				// Non fatal error: A new listenKey will be created anyway when reconnecting.
				log.error("Error while trying to discard current listenKey:" + currentListenKey, ex);
			}
		}
	}
	
	@Override
	protected String createHeartBeatMessage() {
		return null;
	}

	@Override
	protected String getSubscribeRequestMessage(String topic) {
		return null;
	}

	@Override
	protected String getUnSubscribeRequestMessage(String topic) {
		return null;
	}
	
	private void scheduleKeepAliveListenKeyTask(AtomicBoolean cancelled) {
		this.writeExecutor.schedule(new KeepAliveListenKeyTask(cancelled), KEEP_ALIVE_LISTEN_KEY_INTERVAL, TimeUnit.MILLISECONDS);
	}

	private class KeepAliveListenKeyTask implements Runnable {
		
		final AtomicBoolean cancelled;
		
		public KeepAliveListenKeyTask(AtomicBoolean cancelled) {
			this.cancelled = cancelled;
		}

		@Override
		public void run() {
			if (cancelled.get())
				return;
			try {
				if (currentListenKey != null) {
					if (log.isDebugEnabled()) {
						log.debug("Keeping alive listenKey:" + currentListenKey);
					}
					listenKeyApi.keepAliveListenKey(currentListenKey);
					if (log.isDebugEnabled()) {
						log.debug("Done keeping alive listenKey:" + currentListenKey);
					}
				}
			} catch (Exception ex) {
				onError(new IOException("Error while keeping alive listen key:" + currentListenKey, ex));
			}
			
		}
		
	}
}
