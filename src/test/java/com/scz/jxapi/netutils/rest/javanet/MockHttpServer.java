package com.scz.jxapi.netutils.rest.javanet;

import java.io.IOException;
import java.io.Writer;
import java.net.ServerSocket;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scz.jxapi.netutils.rest.HttpMethod;
import com.scz.jxapi.netutils.rest.HttpRequest;
import com.scz.jxapi.netutils.rest.HttpResponse;

/**
 * Thin HTTP server based on GlassFish {@link HttpServer}. Will listen on a local port and wait for incoming requests.
 * Received requests are wrapped as {@link MockHttpRequest} objects and stored in a queue for client to retrieve and answer.
 * Client implementations are expected to serve incoming requests wrapped as {@link MockHttpRequest} objects retrieved using {@link #popRequest(long)}.
 * Every retrieved request must be answered with a {@link HttpResponse} object using {@link MockHttpRequest#complete(HttpResponse)} method.
 */
public class MockHttpServer {
	
	private static final Logger log = LoggerFactory.getLogger(MockHttpServer.class);
	
	private static final int START_AVAILABLE_PORT_RANGE = 8081;
	private static final int MAX_AVAILABLE_PORT_RANGE = 65000;
	
	/**
	 * Tries to open socket on port 8081 and if that fails, tries next port 8082 and
	 * so one until socket opening succeeds or max port number 65000 is reached.
	 * Will close socket immediately and return available port.
	 * 
	 * @return Available port to open server socket on.
	 */
	public static final int findAvailablePort() {
		for (int port = START_AVAILABLE_PORT_RANGE; port < MAX_AVAILABLE_PORT_RANGE; port++) {
			try (ServerSocket s = new ServerSocket(port)) {
				return port;	
			} catch (IOException ex) {
				log.debug("Port " + port + " not available");
			}
		}
		throw new IllegalStateException("Unable to find available HTTP port");
	}

	private final int port;
	private HttpServer server;
	private boolean started = false;
	
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
		this(findAvailablePort());
	}
	
	/**
	 * Creates server listening on specified port.
	 * @param port Port to listen on.
	 */
	public MockHttpServer(int port) {
		this.port = port;
	}
	
	/**
	 * Starts server.
	 * @throws IOException If server start fails.
	 */
	public void start() throws IOException {
		if (started) {
			return;
		}
		server = HttpServer.createSimpleServer(null, getPort());
		server.getServerConfiguration().addHttpHandler(httpHandler);
		if (log.isDebugEnabled()) {
			log.debug("Starting server on port:" + getPort());
		}
		server.start();
		started = true;
	}
	
	/**
	 * Stops server.
	 */
	public void stop() {
		if (!started) {
			return;
		}
		if (log.isDebugEnabled()) {
			log.debug("Stopping server on port:" + getPort());
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
			r = requests.poll(getPort(), TimeUnit.MILLISECONDS);
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
		return started;
	}
	
	private void serveRequest(Request request, Response response) {
		try {
			HttpRequest httpRequest = convertRequest(request);
			if (log.isDebugEnabled()) {
				log.debug("Serving request:" + httpRequest);
			}
			MockHttpRequest mockHttpRequest = new MockHttpRequest(httpRequest);
			requests.add(mockHttpRequest);
			HttpResponse httpResponse = mockHttpRequest.get();
			fillResponse(httpResponse, response);
		} catch (Exception ex) {
			log.error("Error serving request:" + request, ex);
		}
	}
	
	private HttpRequest convertRequest(Request request) throws IOException {
		HttpRequest res = new HttpRequest();
		res.setHttpMethod(HttpMethod.valueOf(request.getMethod().getMethodString()));
		res.setUrl(request.getRequestURL().toString());
		Map<String, List<String>> headers = new TreeMap<>();
		request.getHeaderNames().forEach(headerName -> {
			headers.put(headerName, StreamSupport.stream(request.getHeaders(headerName).spliterator(), false)
												 .collect(Collectors.toList()));
		});
		res.setHeaders(headers);
		
		if (res.getHttpMethod().requestHasBody) {
			try {
				int contentLength = Integer.parseInt(headers.get("content-length").get(0));
				res.setBody(request.getPostBody(contentLength).toStringContent());
			} catch (Exception ex) {
				throw new IOException("Invalid or missing 'content-length' header");
			}
		}
		return res;
	}
	
	private void fillResponse(HttpResponse httpResponse, Response response) throws IOException {
		if (log.isDebugEnabled()) {
			log.debug("Filling response:" + httpResponse);
		}
		response.setStatus(httpResponse.getResponseCode());
		httpResponse.getHeaders().entrySet().forEach(e -> {
			e.getValue().forEach(v -> {
				response.addHeader(e.getKey(), v);
			});
		});
		String body = Optional.ofNullable(httpResponse.getBody()).orElse("");
		try (Writer writer = response.getWriter()) {
			writer.write(body);
		}
		if (log.isDebugEnabled()) {
			log.debug("DONE filling response:" + httpResponse);
		}
	}
}
