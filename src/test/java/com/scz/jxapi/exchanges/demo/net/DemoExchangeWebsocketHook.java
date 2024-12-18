package com.scz.jxapi.exchanges.demo.net;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.scz.jxapi.exchanges.demo.gen.DemoExchangeConstants;
import com.scz.jxapi.exchanges.demo.gen.DemoExchangeProperties;
import com.scz.jxapi.netutils.websocket.AbstractWebsocketHook;
import com.scz.jxapi.netutils.websocket.WebsocketException;
import com.scz.jxapi.netutils.websocket.WebsocketManager;

public class DemoExchangeWebsocketHook extends AbstractWebsocketHook {

	private String host;
	private int port;
	private String url;
	
	public DemoExchangeWebsocketHook(Properties props) {
		this.port = DemoExchangeProperties.getWebsocketPort(props);
		this.host = DemoExchangeProperties.getHost(props);
	}
	
	@Override
	public void init(WebsocketManager websocketManager) {
		// FIXME
		System.out.println(">>> init:"+ host + ":"  + port);
		
		super.init(websocketManager);
		Properties props = websocketManager.getExchangeApi().getProperties();
		this.port = DemoExchangeProperties.getWebsocketPort(props);
		this.host = DemoExchangeProperties.getHost(props);
		url = websocketManager.getUrl();
		url = StringUtils.replace(url, DemoExchangeConstants.WEBSOCKET_SERVER_HOST_WILDCARD, host);
		url = StringUtils.replace(url, DemoExchangeConstants.WEBSOCKET_SERVER_PORT_WILDCARD, String.valueOf(port));
		System.out.println("URL>>>> " + url);
		websocketManager.setUrl(url);
	}
	
	@Override
	public void afterConnect() throws WebsocketException {
		websocketManager.send(DemoExchangeConstants.WEBSOCKET_LOGIN_MESSAGE);
	}
	
	@Override
	public void beforeDisconnect() throws WebsocketException {
		websocketManager.send(DemoExchangeConstants.WEBSOCKET_LOGOUT_MESSAGE);
	}

}
