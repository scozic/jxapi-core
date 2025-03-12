package org.jxapi.netutils.rest;

import java.util.concurrent.CompletableFuture;

/**
 * Generic callback called with response to asynchronous HTTP request execution
 * submitted to a {@link HttpRequestExecutor}
 * 
 * @see HttpRequestExecutor#execute(HttpRequest)
 */
public class FutureHttpResponse extends CompletableFuture<HttpResponse> {

}
