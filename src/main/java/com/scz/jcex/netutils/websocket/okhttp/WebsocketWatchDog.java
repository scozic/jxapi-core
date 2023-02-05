package com.scz.jcex.netutils.websocket.okhttp;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scz.jcex.netutils.websocket.okhttp.OkHttpWebsocketConnection.ConnectionState;

public class WebsocketWatchDog {
	
	private static final Logger log = LoggerFactory.getLogger(OkHttpWebsocketConnection.class);

    private final CopyOnWriteArrayList<OkHttpWebsocketConnection> TIME_HELPER = new CopyOnWriteArrayList<>();
    
    public WebsocketWatchDog() {
    	this(false, -1);
    }
    
    public WebsocketWatchDog(boolean isAutoReconnect, int connectionDelayOnFailure) {
        ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
        exec.scheduleAtFixedRate(() -> {
            TIME_HELPER.forEach(connection -> {
                if (connection.getState() == ConnectionState.DELAY_CONNECT) {
                    connection.reConnect();
                } else if (connection.getState() == ConnectionState.CLOSED_ON_ERROR) {
                	if (log.isInfoEnabled()) {
                		log.info("Connection timed out, reconnect:" + isAutoReconnect);
                	}
                    if (isAutoReconnect) {
                        connection.reConnect(connectionDelayOnFailure);
                    }
                }
            });
        }, 1000L, 1000L, TimeUnit.MILLISECONDS);
        Runtime.getRuntime().addShutdownHook(new Thread(exec::shutdown));
    }

    void onConnectionCreated(OkHttpWebsocketConnection connection) {
        TIME_HELPER.addIfAbsent(connection);
    }

    void onClosedNormally(OkHttpWebsocketConnection connection) {
        TIME_HELPER.remove(connection);
    }
}
