package com.scz.jcex.netutils.websocket.okhttp;

import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scz.jcex.netutils.websocket.WebsocketListener;
import com.scz.jcex.netutils.websocket.WebsocketSubscribeRequest;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class OkHttpWebsocketConnection extends WebSocketListener {
	
    private static final Logger log = LoggerFactory.getLogger(OkHttpWebsocketConnection.class);

    private static final AtomicInteger CONNECTION_COUNTER = new AtomicInteger(0);

    public enum ConnectionState {
        IDLE, DELAY_CONNECT, CONNECTED, CLOSED_ON_ERROR
    }

    private WebSocket webSocket = null;
    private OkHttpClient client;
    
    private volatile long lastReceivedTime = 0;

    private volatile ConnectionState state = ConnectionState.IDLE;
    private int delayInSecond = 0;

    private final WebsocketSubscribeRequest<?> request;
    private final Request okhttpRequest;
    private final WebsocketWatchDog watchDog;
    private final int connectionId;

	private final WebsocketListener<String> messageListener;

    public OkHttpWebsocketConnection(WebsocketSubscribeRequest<?> request, WebsocketListener<String> messageListener, WebsocketWatchDog watchDog) {
        this.connectionId = CONNECTION_COUNTER.getAndIncrement();
        this.request = request;
        this.messageListener = messageListener;
        this.okhttpRequest = new Request.Builder().url(request.getBaseUrl() + request.getParameters().getTopic()).build();
        this.watchDog = watchDog;
    }

    int getConnectionId() {
        return this.connectionId;
    }

    public void connect() {
        if (state == ConnectionState.CONNECTED) {
            log.info("[" + this.connectionId + "] Already connected");
            return;
        }
        
        if (log.isInfoEnabled())
    		log.info("Opening webSocket:" + connectionId + " (request:" + this.request + ")");
        client = new OkHttpClient();
        webSocket = client.newWebSocket(okhttpRequest, this);
    }

    public void reConnect(int delayInSecond) {
        log.warn("[Sub][" + this.connectionId + "] Reconnecting after " + delayInSecond + " seconds later");
        if (webSocket != null) {
        	disposeWebSocket();
        }
        this.delayInSecond = delayInSecond;
        state = ConnectionState.DELAY_CONNECT;
    }

    public void reConnect() {
        if (delayInSecond > 0) {
            delayInSecond--;
        } else {
            connect();
        }
    }

    public long getLastReceivedTime() {
        return this.lastReceivedTime;
    }

    public void send(String str) {
        boolean result = false;
        log.debug("[Send]{}", str);
        if (webSocket != null) {
            result = webSocket.send(str);
        }
        if (!result) {
            log.error("[Sub][" + this.connectionId + "] Failed to send message");
            closeOnError();
        }
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        super.onMessage(webSocket, text);
        lastReceivedTime = System.currentTimeMillis();
        log.debug("[On Message]:{}", text);
        try {
        	messageListener.handleMessage(text);
        } catch (Exception e) {
            log.error("[" + connectionId + "]:Error handling message[" + text + "]", e);
            closeOnError();
        }
    }

    private void onError(String errorMessage, Throwable e) {
        log.error("[" + this.connectionId + "]:onError:" + errorMessage, e);
    }


    public ConnectionState getState() {
        return state;
    }

    public void close() {
        log.info("[" + this.connectionId + "] Closing normally");
        disposeWebSocket();
        watchDog.onClosedNormally(this);
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        super.onClosed(webSocket, code, reason);
        if (state == ConnectionState.CONNECTED) {
            state = ConnectionState.IDLE;
        }
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
        this.webSocket = webSocket;
        log.info("[Sub][" + this.connectionId + "] Connected to server");
        watchDog.onConnectionCreated(this);
//        if (request.connectionHandler != null) {
//            request.connectionHandler.handle(this);
//        }
        state = ConnectionState.CONNECTED;
        lastReceivedTime = System.currentTimeMillis();
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        onError("Unexpected error: " + t.getMessage(), t);
        closeOnError();
    }

    private void closeOnError() {
        if (webSocket != null) {
            disposeWebSocket();
            state = ConnectionState.CLOSED_ON_ERROR;
            log.error("[Sub][" + this.connectionId + "] Connection is closing due to error");
        }
    }
    
    private void disposeWebSocket() {
    	if (log.isInfoEnabled())
    		log.info("Disposing webSocket:" + connectionId + " (request:" + this.request + ")");
    	if (webSocket != null) {
    		webSocket.cancel();
    		webSocket = null;
    	}
    	if (client != null) {
    		client.connectionPool().evictAll();
            client.dispatcher().executorService().shutdown();
            client = null;
    	}
    }

}
