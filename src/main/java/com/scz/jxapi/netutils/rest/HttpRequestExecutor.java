package com.scz.jxapi.netutils.rest;

import java.util.function.Consumer;

public interface HttpRequestExecutor {

	void execute(HttpRequest request, Consumer<HttpResponse> callback);
}
