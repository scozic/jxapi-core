package com.scz.jxapi.util;

import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	 * Logs successful response at INFO level or throws an exception.
	 * @param futureResponse
	 * @throws NullPointerException if <code>futureResponse</code> is <code>null</code>
	 * @throws IllegalStateException response is not OK 
	 */
	public static void checkResponse(FutureRestResponse<?> futureResponse) {
		if (futureResponse == null) {
			throw new NullPointerException("null response");
		}
		try {
			RestResponse<?> response = futureResponse.get();
			if (!response.isOk()) {
				throw new IllegalStateException("Error in response:" + response, response.getException());
			}
			log.info("Got OK response:" + prettyPrintResponse(response));
		} catch (InterruptedException | ExecutionException e) {
			throw new IllegalStateException("Error fecthing response", e);
		}
		
	}
	
	static String prettyPrintResponse(RestResponse<?> response) {
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
		log.info("received message:\n" + JsonUtil.pojoToPrettyPrintJson(message));
	}
}
