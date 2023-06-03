package com.scz.jxapi.netutils.rest;

import java.util.function.Consumer;

/**
 * Generic callback called with response to asynchronous REST HTTP request
 * execution. Is a functional interface by extending {@link Consumer} with no additional methods.
 * 
 * @param <R>
 */
public interface RestCallback<R> extends Consumer<RestResponse<R>> {
}
