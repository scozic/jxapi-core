package org.jxapi.netutils.rest.javanet;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jxapi.netutils.rest.HttpRequest;
import org.jxapi.netutils.rest.HttpResponse;

/**
 * Thin HTTP server based on GlassFish {@link HttpServer}. Will listen on a local port and wait for incoming requests.
 * Received requests are wrapped as {@link MockHttpRequest} objects and stored in a queue for client to retrieve and answer.
 * Client implementations are expected to serve incoming requests wrapped as {@link MockHttpRequest} objects retrieved using {@link #popRequest(long)}.
 * Every retrieved request must be answered with a {@link HttpResponse} object using {@link CompletableFuture#complete(Object)} method.
 */
public class MockHttpServer {
  
  static final Logger log = LoggerFactory.getLogger(MockHttpServer.class);
  
  private final int port;
  private HttpServer server;
  private AtomicBoolean started = new AtomicBoolean(false);
  private final String baseUrl;
  
  private final LinkedBlockingQueue<MockHttpRequest> requests = new LinkedBlockingQueue<>();
  
  private final HttpHandler httpHandler = new HttpHandler() {
    @Override
    public void service(Request request, Response response) throws Exception {
      serveRequest(request, response);
    }
  };
  
  /**
   * Creates server on first available port in range 8081-65000.
   */
  public MockHttpServer() {
    this(HttpServerUtil.findAvailablePort());
  }
  
  /**
   * Creates server listening on specified port.
   * @param port Port to listen on.
   */
  public MockHttpServer(int port) {
    this.port = port;
    this.baseUrl = "http://localhost:" + port;
  }
  
  /**
   * Starts server.
   * @throws IOException If server start fails.
   */
  public void start() throws IOException {
    if (started.getAndSet(true)) {
      return;
    }
    server = HttpServer.createSimpleServer(null, getPort());
    server.getServerConfiguration().addHttpHandler(httpHandler);
    if (log.isDebugEnabled()) {
      log.debug("Starting server on port:{}", getPort());
    }
    server.start();
  }
  
  /**
   * Stops server.
   */
  public void stop() {
    if (!started.getAndSet(false)) {
      return;
    }
    if (log.isDebugEnabled()) {
      log.debug("Stopping server on port:{}", getPort());
    }
    server.shutdown();
  }
  
  /**
   * Waits for incoming request for specified timeout.
   * @param timeout Timeout in milliseconds.
   * @return Request object.
   * @throws TimeoutException If timeout is reached.
   */
  public MockHttpRequest popRequest(long timeout) throws TimeoutException {
    MockHttpRequest r = null;
    try {
      r = requests.poll(timeout, TimeUnit.MILLISECONDS);
    } catch (InterruptedException e) {
      log.warn("Interrupted waiting for request");
    }
    if (r == null) {
      throw new TimeoutException("Timed out waiting for request");
    }
    return r;
  }
  
  /**
   * @return Number of requests in queue waiting to be served.
   */
  public int requestCount() {
    return requests.size();
  }
  
  /**
   * @return Port server is listening on.
   */
  public int getPort() {
    return port;
  }

  /**
   * @return <code>true</code> if server is started.
   */
  public boolean isStarted() {
    return started.get();
  }
  
  /**
   * @return Base local URL of the server, that is <code>http://localhost:port</code>
     */
  public String getBaseUrl() {
    return baseUrl;
  }
  
  private void serveRequest(Request request, Response response) {
    try {
      HttpRequest httpRequest = HttpServerUtil.convertRequest(request);
      log.debug("Serving request:{}", httpRequest);
      MockHttpRequest mockHttpRequest = new MockHttpRequest(httpRequest);
      requests.add(mockHttpRequest);
      HttpResponse httpResponse = mockHttpRequest.get();
      HttpServerUtil.fillResponse(httpResponse, response);
    } catch (Exception ex) {
      log.error("Error serving request:{}", request, ex);
    }
  }
}
