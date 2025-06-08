package org.jxapi.netutils.rest.javanet;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jxapi.netutils.rest.AbstractHttpRequestExecutor;
import org.jxapi.netutils.rest.FutureHttpResponse;
import org.jxapi.netutils.rest.HttpRequest;
import org.jxapi.netutils.rest.HttpRequestExecutor;
import org.jxapi.netutils.rest.HttpResponse;

/**
 * A {@link HttpRequestExecutor} implementation relying on {@link java.net.http.HttpClient}
 *
 *@see HttpClient
 */
public class JavaNetHttpRequestExecutor extends AbstractHttpRequestExecutor {
  
  private static final Logger log = LoggerFactory.getLogger(JavaNetHttpRequestExecutor.class);
  
  private final HttpClient httpClient;

  /**
   * Creates a new instance of this class.
   * @param httpClient The {@link HttpClient} to use for requests
   */
  public JavaNetHttpRequestExecutor(HttpClient httpClient) {
    this.httpClient = httpClient;
  }

  @Override
  public FutureHttpResponse execute(HttpRequest request) {
    checkNotDisposed();
    FutureHttpResponse callback = new FutureHttpResponse();
    final HttpResponse response = new HttpResponse();
    response.setRequest(request);
    response.setTime(new Date());
    try {
      log.debug("Executing request:{}", request);
      Builder builder = java.net.http.HttpRequest.newBuilder().uri(new URI(request.getUrl()));
      long requestTimeout = getRequestTimeout();
      if (requestTimeout >= 0) {
        builder.timeout(Duration.ofMillis(requestTimeout));
      }
      switch (request.getHttpMethod()) {
      case GET:
        builder.GET();
        break;
      case POST:
        builder.POST(BodyPublishers.ofString(Optional.ofNullable(request.getBody()).orElse("")));
        break;
      case DELETE:
        builder.method("DELETE", BodyPublishers.ofString(Optional.ofNullable(request.getBody()).orElse("")));
        break;
      case PUT:
        builder.PUT(BodyPublishers.ofString(Optional.ofNullable(request.getBody()).orElse("")));
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
              throw new ExecutionException(error);
            }
            response.setResponseCode(r.statusCode());
            response.setHeaders(r.headers().map());
            response.setBody(r.body());
            response.setTime(new Date());
            log.debug("Got response to request:[{}], response[{}]", request, response);
          } catch (Exception ex) {
            log.error("Error executing request:" + request, ex);
            response.setException(ex);
          } finally {
            callback.complete(response);
          }
      }); 
    } catch (Exception e) {
      response.setException(e);
      callback.complete(response);
    }
    return callback;
  }

}
