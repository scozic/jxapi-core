package com.scz.jxapi.netutils.rest.javanet;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scz.jxapi.netutils.rest.FutureHttpResponse;
import com.scz.jxapi.netutils.rest.HttpRequest;
import com.scz.jxapi.netutils.rest.HttpRequestExecutor;
import com.scz.jxapi.netutils.rest.HttpResponse;

/**
 * A {@link HttpRequestExecutor} implementation relying on {@link java.net.http.HttpRequest}
 *
 */
public class JavaNetHttpRequestExecutor implements HttpRequestExecutor {
	
	private static final Logger log = LoggerFactory.getLogger(JavaNetHttpRequestExecutor.class);
	
	private static final Duration REQUEST_TIMEOUT = Duration.ofMillis(5000L);
	
	private final HttpClient httpClient;

	public JavaNetHttpRequestExecutor(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

	@Override
	public FutureHttpResponse execute(HttpRequest request) {
		FutureHttpResponse callback = new FutureHttpResponse();
		final HttpResponse response = new HttpResponse();
		response.setRequest(request);
		response.setTime(new Date());
		try {
			if (log.isDebugEnabled())
				log.debug("Executing request:" + request);
			Builder builder = java.net.http.HttpRequest.newBuilder().uri(new URI(request.getUrl())).timeout(REQUEST_TIMEOUT);
			switch (request.getHttpMethod()) {
			case GET:
				builder.GET();
				break;
			case POST:
				builder.POST(BodyPublishers.ofString(Optional.ofNullable(request.getBody()).orElse("")));
				break;
			case DELETE:
				builder.DELETE();
				break;
			case PUT:
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
			
			httpClient.sendAsync(builder.build(), BodyHandlers.ofString()).whenComplete((r, error) -> {
	    		try {
	    			if (error != null) {
	    				throw new Exception(error);
	    			}
	    			response.setResponseCode(r.statusCode());
		    		response.setHeaders(r.headers().map());
		    		response.setBody(r.body());
		    		response.setTime(new Date());
		    		if (log.isDebugEnabled()) {
		    			log.debug("Got response to request:[" + request + "], response[" + response + "]");
		    		}
	    		} catch (Exception ex) {
	    			log.error("Error executing request:" + request, ex);
	    			response.setException(ex);
	    		} finally {
	    			callback.complete(response);
	    		}
			}); 
		} catch (URISyntaxException e) {
			response.setException(e);
			callback.complete(response);
		}
		return callback;
	}

}
