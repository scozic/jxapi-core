package com.scz.jcex.netutils.websocket.okhttp;

import java.util.Objects;
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
    private final SubscriptionOptions options;
    
    public WebsocketWatchDog(SubscriptionOptions subscriptionOptions) {
        this.options = Objects.requireNonNull(subscriptionOptions);
        long t = 1000;
        ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
        exec.scheduleAtFixedRate(() -> {
            TIME_HELPER.forEach(connection -> {
                if (connection.getState() == ConnectionState.DELAY_CONNECT) {
                    connection.reConnect();
                } else if (connection.getState() == ConnectionState.CLOSED_ON_ERROR) {
                	if (log.isInfoEnabled()) {
                		log.info("Connection timed out, reconnect:" + options.isAutoReconnect());
                	}
                    if (options.isAutoReconnect()) {
                        connection.reConnect(options.getConnectionDelayOnFailure());
                    }
                }
            });
        }, t, t, TimeUnit.MILLISECONDS);
        Runtime.getRuntime().addShutdownHook(new Thread(exec::shutdown));
    }

    void onConnectionCreated(OkHttpWebsocketConnection connection) {
        TIME_HELPER.addIfAbsent(connection);
    }

    void onClosedNormally(OkHttpWebsocketConnection connection) {
        TIME_HELPER.remove(connection);
    }
}
