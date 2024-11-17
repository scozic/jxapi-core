package com.scz.jxapi.util;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scz.jxapi.exchange.ExchangeApiEvent;
import com.scz.jxapi.netutils.rest.FutureRestResponse;
import com.scz.jxapi.netutils.rest.HttpRequest;
import com.scz.jxapi.netutils.rest.HttpResponse;
import com.scz.jxapi.netutils.rest.RestResponse;

/**
 * Helper methods used by generated demo snippets.
 */
public class DemoUtil {
	
	private DemoUtil() {}
	
	private static final int MAX_PRETTY_PRINT_STRING_LENGTH = 512;
	
	private static final Logger log = LoggerFactory.getLogger(DemoUtil.class);

	/**
	 * Awaits response to a REST API call and ogs successful response at INFO level or throws an exception.
	 * @param futureResponse response to check
	 * @throws NullPointerException if <code>futureResponse</code> is <code>null</code>
	 * @throws InterruptedException eventually thrown  by {@link CompletableFuture#get()}
	 * @throws ExecutionException eventually thrown  by {@link CompletableFuture#get()} or if received response is not OK see {@link RestResponse#isOk()}
	 */
	public static void checkResponse(FutureRestResponse<?> futureResponse) throws InterruptedException, ExecutionException {
		if (futureResponse == null) {
			throw new NullPointerException("null response");
		}
		RestResponse<?> response = futureResponse.get();
		if (!response.isOk()) {
			throw new ExecutionException("Error in response:" + response, response.getException());
		}
		if (log.isInfoEnabled())
			log.info("Got OK response:\n{}", prettyPrintResponse(response));
	}
	
	public static String prettyPrintResponse(RestResponse<?> response) {
		HttpResponse httpResponse = response.getHttpResponse();
		if (httpResponse != null) {
			httpResponse.setBody(EncodingUtil.prettyPrintLongString(httpResponse.getBody(), MAX_PRETTY_PRINT_STRING_LENGTH));
			HttpRequest httpRequest = httpResponse.getRequest();
			if (httpRequest != null) {
				httpRequest.setBody(EncodingUtil.prettyPrintLongString(httpRequest.getBody(), MAX_PRETTY_PRINT_STRING_LENGTH));
			}
		}
		
		return JsonUtil.pojoToPrettyPrintJson(response);
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
}
