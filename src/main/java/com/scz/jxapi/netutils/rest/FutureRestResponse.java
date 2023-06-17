package com.scz.jxapi.netutils.rest;

import java.util.concurrent.CompletableFuture;

/**
 * Generic callback called with response to asynchronous REST HTTP request
 * execution. 
 * 
 * @param <R>
 */
public class FutureRestResponse<R> extends CompletableFuture<RestResponse<R>> {
}
