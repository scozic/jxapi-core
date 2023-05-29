package com.scz.jxapi.netutils.rest;

/**
 * Generic callback called with response to asynchronous REST HTTP request
 * execution.
 * 
 * @param <R>
 */
public interface Callback<R> {

	void handle(R response);
}
