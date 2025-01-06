package com.scz.jxapi.util;

import java.util.List;
import java.util.Properties;

import com.scz.jxapi.exchange.descriptor.ConfigProperty;
import com.scz.jxapi.exchange.descriptor.DefaultConfigProperty;
import com.scz.jxapi.exchange.descriptor.Type;

public interface DemoProperties {
	
	public static final String DEMO_API_PROPERTIES_FILE_SYSTEM_PROPERTY = "jxapi.testProperties";

	/**
	 * Property controlling the duration of the subscription in
	 * WebSocket endpoint demo classes.
	 */
	ConfigProperty DEMO_WS_SUBSCRIPTION_DURATION_PROPERTY = DefaultConfigProperty.create(
			"jxapi.demo.ws.subscriptionDuration", 
			Type.LONG, 
			"The duration in ms of the subscription in websocket endpoint demo classes", 
			30000L);
	
	/**
	 * Property controlling the delay before exiting program after
	 * unsubscribing in WebSocket endpoint demo classes.
	 */
	ConfigProperty DEMO_WS_DELAY_BEFORE_EXIT_AFTER_UNSUBSCRIPTION_PROPERTY = DefaultConfigProperty.create(
			"jxapi.demo.ws.delayBeforeExitAfterUnsubscription", 
			Type.LONG, 
			"Delay in ms before exiting program after unsubscribing in websocked endpoint demo classes.", 
			1000L);
	
	List<ConfigProperty> ALL = List.of(DEMO_WS_SUBSCRIPTION_DURATION_PROPERTY, DEMO_WS_DELAY_BEFORE_EXIT_AFTER_UNSUBSCRIPTION_PROPERTY);
	
	static long getWebsocketSubscriptionDuration(Properties properties) {return PropertiesUtil.getLongProperty(properties, DEMO_WS_SUBSCRIPTION_DURATION_PROPERTY.getName(), (Long) DEMO_WS_SUBSCRIPTION_DURATION_PROPERTY.getDefaultValue());}
	
	static long getWebsocketDelayBeforeExit(Properties properties) {return PropertiesUtil.getLongProperty(properties, DEMO_WS_SUBSCRIPTION_DURATION_PROPERTY.getName(), (Long) DEMO_WS_SUBSCRIPTION_DURATION_PROPERTY.getDefaultValue());}
}
