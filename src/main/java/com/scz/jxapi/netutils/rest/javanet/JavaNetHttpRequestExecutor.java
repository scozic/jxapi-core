package com.scz.jxapi.netutils.rest.javanet;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scz.jxapi.netutils.rest.Callback;
import com.scz.jxapi.netutils.rest.HttpRequest;
import com.scz.jxapi.netutils.rest.HttpRequestExecutor;
import com.scz.jxapi.netutils.rest.HttpResponse;

/**
 * A {@link HttpRequestExecutor} implementation relying on {@link java.net.http.HttpRequest}
 *
 */
public class JavaNetHttpRequestExecutor implements HttpRequestExecutor {
	
	private static final Logger log = LoggerFactory.getLogger(JavaNetHttpRequestExecutor.class);
	
	private final HttpClient httpClient;

	public JavaNetHttpRequestExecutor(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

	@Override
	public void execute(HttpRequest request, Callback<HttpResponse> callback) {
		final HttpResponse response = new HttpResponse();
		try {
			if (log.isDebugEnabled())
				log.debug("Executing request:" + request);
			Builder builder = java.net.http.HttpRequest.newBuilder().uri(new URI(request.getUrl()));
			switch (request.getHttpMethod()) {
			case "GET":
				builder.GET();
				break;
			case "POST":
				builder.POST(BodyPublishers.ofString(request.getBody()));
				break;
			case "DELETE":
				builder.DELETE();
				break;
			case "PUT":
				builder.PUT(BodyPublishers.ofString(request.getBody()));
				break;
			default:
				throw new IllegalArgumentException("Unexpected verb:[" + request.getHttpMethod() + "] for request:" + request);
			}
			
			for (Entry<String, String> entry: request.getHeaders().entrySet()) {
				builder = builder.header(entry.getKey(), entry.getValue());
			}
			
			httpClient.sendAsync(builder.build(), BodyHandlers.ofString())
		    	.thenAccept(r -> {
		    		response.setResponseCode(r.statusCode());
		    		r.headers().map().forEach((headerName, headerValues) -> {
		    			response.setHeader(headerName, headerValues.toString());
		    		});
		    		response.setBody(r.body());
		    		if (log.isDebugEnabled()) {
		    			log.debug("Got response to request:[" + request + "], response[" + response + "]");
		    		}
		    		callback.handle(response);
		    	});
			
			
		} catch (URISyntaxException e) {
			response.setException(e);
			callback.handle(response);
		}   
	}

}
