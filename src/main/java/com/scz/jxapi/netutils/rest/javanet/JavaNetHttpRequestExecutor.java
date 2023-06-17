package com.scz.jxapi.netutils.rest.javanet;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	public void execute(HttpRequest request, Consumer<HttpResponse> callback) {
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
				builder.POST(BodyPublishers.ofString(Optional.ofNullable(request.getBody()).orElse("")));
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
			
			if (request.getHeaders() != null) {
				for (Entry<String, List<String>> entry: request.getHeaders().entrySet()) {
					for (String val : entry.getValue()) {
						builder = builder.header(entry.getKey(), val);
					}
				}
			}
			
			httpClient.sendAsync(builder.build(), BodyHandlers.ofString())
		    	.thenAccept(r -> {
		    		response.setResponseCode(r.statusCode());
		    		response.setHeaders(r.headers().map());
		    		response.setBody(r.body());
		    		if (log.isDebugEnabled()) {
		    			log.debug("Got response to request:[" + request + "], response[" + response + "]");
		    		}
		    		callback.accept(response);
		    	});
			
			
		} catch (URISyntaxException e) {
			response.setException(e);
			callback.accept(response);
		}   
	}

}
