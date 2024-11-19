package com.scz.jxapi.util;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scz.jxapi.exchange.ExchangeApiEvent;
import com.scz.jxapi.netutils.rest.FutureRestResponse;
import com.scz.jxapi.netutils.rest.RestResponse;

/**
 * Helper methods used by generated demo snippets.
 */
public class DemoUtil {
	
	private DemoUtil() {}
	
	private static final Logger log = LoggerFactory.getLogger(DemoUtil.class);

	/**
	 * Awaits response to a REST API call and ogs successful response at INFO level or throws an exception.
	 * @param futureResponse response to check
	 * @throws NullPointerException if <code>futureResponse</code> is <code>null</code>
	 * @throws InterruptedException eventually thrown  by {@link CompletableFuture#get()}
	 * @throws ExecutionException eventually thrown  by {@link CompletableFuture#get()} or if received response is not OK see {@link RestResponse#isOk()}
	 * @return response to REST API call. This response is 'OK' otherwise an exception would be thrown.
	 */
	public static <T> RestResponse<T> checkResponse(FutureRestResponse<T> futureResponse) throws InterruptedException, ExecutionException {
		if (futureResponse == null) {
			throw new NullPointerException("null response");
		}
		RestResponse<T> response = futureResponse.get();
		if (!response.isOk()) {
			throw new ExecutionException("Error in response:" + response, response.getException());
		}
		if (log.isInfoEnabled())
			log.info("Got OK response:\n{}", JsonUtil.pojoToPrettyPrintJson(response.getResponse()));
		return response;
	}
	
	public static void logWsMessage(Object message) {
		if (log.isInfoEnabled())
			log.info("received message:\n{}", JsonUtil.pojoToPrettyPrintJson(message));
	}
	
	public static void logWsApiEvent(ExchangeApiEvent event) {
		switch (event.getType()) {
		case WEBSOCKET_ERROR:
			if (log.isErrorEnabled())
				log.error(String.format("Error raised from websocket:%s", event), event.getWebsocketError());
			break;
		case WEBSOCKET_MESSAGE:
			log.debug("Websocket message received:{}", event);
			break;
		case WEBSOCKET_SUBSCRIBE:
			log.info("Subscription event received: {}", event);
			break;
		case WEBSOCKET_UNSUBSCRIBE:
			log.info("Unsubscription event received: {}", event);
			break;
		default:
			break;
		}
	}
	
	public static void logRestApiEvent(ExchangeApiEvent event) {
		switch (event.getType()) {
		case HTTP_REQUEST:
		case HTTP_RESPONSE:
			if (log.isDebugEnabled())
				log.debug(event.toString());
			break;
		default:
			break;
		}
	}
	
	
}
