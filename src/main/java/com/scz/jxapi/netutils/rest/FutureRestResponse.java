package com.scz.jxapi.netutils.rest;

import java.util.concurrent.CompletableFuture;

/**
 * Generic callback called with response to asynchronous REST request execution. 
 * @see RestEndpoint
 * @param <R> the type of data payload carried in response
 */
public class FutureRestResponse<R> extends CompletableFuture<RestResponse<R>> {
}
