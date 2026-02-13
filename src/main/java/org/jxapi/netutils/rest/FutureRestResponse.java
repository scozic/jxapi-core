package org.jxapi.netutils.rest;

import java.util.concurrent.CompletableFuture;

/**
 * Generic callback called with response to asynchronous REST request execution. 
 * @param <A> the type of data payload carried in response, set {@link RestResponse#getResponse()}
 */
public class FutureRestResponse<A> extends CompletableFuture<RestResponse<A>> {
}
