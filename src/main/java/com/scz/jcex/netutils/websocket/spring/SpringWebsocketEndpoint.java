package com.scz.jcex.netutils.websocket.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import com.scz.jcex.generator.exchange.ExchangeApiDescriptor;
import com.scz.jcex.netutils.websocket.WebsocketEndpoint;
import com.scz.jcex.netutils.websocket.WebsocketListener;
import com.scz.jcex.netutils.websocket.WebsocketSubscribeParameters;
import com.scz.jcex.netutils.websocket.WebsocketSubscribeRequest;

public class SpringWebsocketEndpoint<T extends WebsocketSubscribeParameters, M> implements WebsocketEndpoint<T, M> {
	
	private static final Logger log = LoggerFactory.getLogger(SpringWebsocketEndpoint.class);
	
	public SpringWebsocketEndpoint(WebSocketClient websocketClient) {
		
	}

	@Override
	public String subscribe(WebsocketSubscribeRequest<T> request, WebsocketListener<M> listener) {
		WebSocketClient client = new StandardWebSocketClient();
//		client.
		
		return null;
	}

	@Override
	public boolean unsubscribe(String unsubscriptionId) {
		if (log.isDebugEnabled())
			log.debug("Unsubscribing:" + unsubscriptionId);
		return false;
	}
	
	private class EndpointWebsocketHandler implements WebSocketHandler {

		@Override
		public void afterConnectionEstablished(WebSocketSession session) throws Exception {
			if (log.isDebugEnabled()) {
				log.debug("Connection established, session:" + session);
			}
			
		}

		@Override
		public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean supportsPartialMessages() {
			// TODO Auto-generated method stub
			return false;
		}
		
	}

}
