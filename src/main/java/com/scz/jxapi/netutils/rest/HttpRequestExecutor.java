package com.scz.jxapi.netutils.rest;

public interface HttpRequestExecutor {

	void execute(HttpRequest request, Callback<HttpResponse> callback);
}
