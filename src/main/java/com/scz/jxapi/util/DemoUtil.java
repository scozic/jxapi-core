package com.scz.jxapi.util;

import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scz.jxapi.netutils.rest.FutureRestResponse;
import com.scz.jxapi.netutils.rest.RestResponse;

/**
 * Helper methods used by generated demo snippets.
 */
public class DemoUtil {
	
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
			log.info("Got OK response:" + response);
		} catch (InterruptedException | ExecutionException e) {
			throw new IllegalStateException("Error fecthing response", e);
		}
		
	}
}
