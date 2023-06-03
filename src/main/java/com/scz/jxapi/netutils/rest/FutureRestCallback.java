package com.scz.jxapi.netutils.rest;

import java.util.concurrent.CompletableFuture;

public class FutureRestCallback<R> extends CompletableFuture<RestResponse<R>> implements RestCallback<R> {

	@Override
	public void accept(RestResponse<R> response) {
		this.complete(response);
	}

}
