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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

import org.jxapi.netutils.rest.AbstractHttpRequestExecutor;
import org.jxapi.netutils.rest.FutureHttpResponse;
import org.jxapi.netutils.rest.HttpRequest;
import org.jxapi.netutils.rest.HttpRequestExecutor;
import org.jxapi.netutils.rest.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@link HttpRequestExecutor} implementation relying on {@link java.net.http.HttpClient}
 *
 *@see HttpClient
 */
public class JavaNetHttpRequestExecutor extends AbstractHttpRequestExecutor {
  
  private static final AtomicLong THREAD_COUNTER = new AtomicLong(1L);
  
  private static final Logger log = LoggerFactory.getLogger(JavaNetHttpRequestExecutor.class);
  
  private final HttpClient httpClient;
  
  private final ExecutorService executorService;

  private final boolean ownsExecutor;
  
  /**
   * Creates a new {@link ExecutorService} for use with
   * {@link java.net.http.HttpClient}
   * 
   * @param threadNamePrefix The prefix to use for threads created by the executor
   *                         service
   * @return The newly created {@link ExecutorService}
   */
  public static ExecutorService createJavaNetHttpClientExecutorService(String threadNamePrefix) {
    ThreadFactory threadFactory = runnable -> {
          Thread thread = Executors.defaultThreadFactory().newThread(runnable);
          thread.setName(threadNamePrefix + THREAD_COUNTER.getAndIncrement());
          return thread;
      };
   return Executors.newCachedThreadPool(threadFactory);
  }
  
  /**
   * Creates a new instance of this class. Will create and manage its own
   * HttpClient and ExecutorService.
   * @param name The name prefix to use for threads created by the internal executor service
   */
  public JavaNetHttpRequestExecutor(String name) {
    this(createJavaNetHttpClientExecutorService(name));
  }
  
  private JavaNetHttpRequestExecutor(ExecutorService executorService) {
	this(java.net.http.HttpClient.newBuilder()
	         .executor(executorService)
	         .build(), 
	     executorService, 
	     true);
  }

  /**
   * Creates a new instance of this class.
   * 
   * @param httpClient      The {@link HttpClient} to use for requests
   * @param executorService The {@link ExecutorService} to use for async
   *                        operations. Remember to shutdown the executor service
   *                        when no longer needed.
   */
  public JavaNetHttpRequestExecutor(HttpClient httpClient, ExecutorService executorService) {
    this(httpClient, executorService, false);
  }
  
  /**
   * Creates a new instance of this class.
   * @param httpClient The {@link HttpClient} to use for requests
   */
  private JavaNetHttpRequestExecutor(HttpClient httpClient, ExecutorService executorService, boolean ownsExecutor) {
    this.httpClient = httpClient;
    this.executorService = executorService;
    this.ownsExecutor = ownsExecutor;
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
      
      httpClient.sendAsync(builder.build(), BodyHandlers.ofString())
      .handleAsync((r, error) -> {
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
        return null; // Return value is ignored
      }, executorService);
    } catch (Exception e) {
      response.setException(e);
      callback.complete(response);
    }
    return callback;
  }
  
  /**
   * Subclasses must override this method to implement actual resource disposal.
   */
  @Override
  protected void doDispose() {
    if (ownsExecutor) {
      log.info("Shutting down executor service for JavaNetHttpRequestExecutor:{}", this);
      executorService.shutdownNow();
    }
  }
  


}
