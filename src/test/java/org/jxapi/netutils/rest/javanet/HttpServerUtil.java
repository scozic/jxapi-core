package org.jxapi.netutils.rest.javanet;

import java.io.IOException;
import java.io.Writer;
import java.net.ServerSocket;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.jxapi.netutils.rest.HttpMethod;
import org.jxapi.netutils.rest.HttpRequest;
import org.jxapi.netutils.rest.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

public class HttpServerUtil {
  
  private static final Logger log = LoggerFactory.getLogger(HttpServerUtil.class);

  static final int START_AVAILABLE_PORT_RANGE = 8081;
  static final int MAX_AVAILABLE_PORT_RANGE = 65000;

  private HttpServerUtil() {}

  /**
   * Tries to open socket on port on port 8081 and if that fails, tries next port
   * 8082 and so on until socket opening succeeds or max port number 65000 is
   * reached.
   * 
   * @see #findAvailablePort(int)
   */
  public static final int findAvailablePort() {
    return findAvailablePort(START_AVAILABLE_PORT_RANGE);
  }
  
  /**
   * Tries to open socket on port <code>startPort</code> and if that fails, tries next port <code>startPort + 1</code> and
   * so one until socket opening succeeds or max port number 65000 is reached.
   * Will close socket immediately and return available port.
   * 
   * @param startPort Port to start searching from.
   * @return Available port to open server socket on.
   */
  public static final int findAvailablePort(int startPort) {
    for (int port = startPort; port < HttpServerUtil.MAX_AVAILABLE_PORT_RANGE; port++) {
      try (ServerSocket s = new ServerSocket(port)) {
        return port;  
      } catch (IOException ex) {
        log.debug("Port {} not available", port);
      }
    }
    throw new IllegalStateException("Unable to find available HTTP port");
  }

  public static HttpRequest convertRequest(Request request) throws IOException {
    HttpRequest res = new HttpRequest();
    res.setHttpMethod(HttpMethod.valueOf(request.getMethod().getMethodString()));
    res.setUrl(HttpServerUtil.getRequestUrl(request));
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

  public static String getRequestUrl(Request request) {
    StringBuilder url = new StringBuilder().append(request.getRequestURL().toString());
    String query = request.getQueryString();
    if (!ObjectUtils.isEmpty(query)) {
      url.append("?").append(query);
    }
    return url.toString();
  }

  public static void fillResponse(HttpResponse httpResponse, Response response) throws IOException {
    log.debug("Filling response:{}", httpResponse);
    response.setStatus(httpResponse.getResponseCode());
    if (httpResponse.getHeaders() != null) {
      httpResponse.getHeaders().entrySet().forEach(e -> {
        e.getValue().forEach(v -> {
          response.addHeader(e.getKey(), v);
        });
      });
    }
    String body = Optional.ofNullable(httpResponse.getBody()).orElse("");
    try (Writer writer = response.getWriter()) {
      writer.write(body);
    }
    log.debug("DONE filling response:{}", httpResponse);
  }

}
