package com.scz.jxapi.netutils.rest;

import java.util.concurrent.CompletableFuture;

public class FutureCallback<R> extends CompletableFuture<R> implements Callback<R> {

	@Override
	public void handle(R response) {
		this.complete(response);
	}

}
