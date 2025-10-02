package org.jxapi.netutils.rest;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jxapi.util.EncodingUtil;
import org.jxapi.util.ExceptionToStringJsonSerializer;
import org.jxapi.util.JsonUtil;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Represents a HTTP response, raised upon completion of a {@link FutureHttpResponse}.
 * 
 * @see FutureHttpResponse
 */
public class HttpResponse {
  
  private static final ObjectMapper TOSTRING_OBJECT_MAPPER = EncodingUtil.createDefaultPojoToToStringObjectMapper(
      new ExceptionToStringJsonSerializer(),
      new HttpRequestToStringJsonSerializer(),
      new HttpResponseToStringJsonSerializer()
    );
  
  /**
   * Checks if a HTTP status code is in the 2XX range.
   * @param httpStatus The HTTP status code to check
   * @return <code>true</code> if status is 2XX [200-299]
   */
  public static boolean isStatusCodeOk(int httpStatus) {
    return httpStatus / 100 == 2;
  }

  private int responseCode;
  
  private String body;
  
  private Exception exception;
  
  private Map<String, List<String>> headers;
  
  private HttpRequest request;
  
  private Date time;

  /**
   * @return HTTP response code
   */
  public int getResponseCode() {
    return responseCode;
  }

  /**
   * @param responseCode HTTP response code
   */
  public void setResponseCode(int responseCode) {
    this.responseCode = responseCode;
  }

  /**
   * @return Body of the HTTP response
   */
  public String getBody() {
    return body;
  }

  /**
   * @param body Body of the HTTP response
   */
  public void setBody(String body) {
    this.body = body;
  }

  /**
   * @return Exception raised during request processing
   */
  public Exception getException() {
    return exception;
  }

  /**
   * @param exception Exception raised during request processing
   */
  public void setException(Exception exception) {
    this.exception = exception;
  }
  
  /**
   * @return Headers to be sent in request
   */
  public Map<String, List<String>> getHeaders() {
    return headers;
  }

  /**
   * Sets a header with a single value (erasing previous values).
   * @param headerName The name of the header
   * @param headerValue The value of the header
   */
  public void setHeader(String headerName, String headerValue) {
    setHeader(headerName, List.of(headerValue));
  }
  
  /**
   * Sets a header with multiple values
   * @param headerName   The name of the header
   * @param headerValues The values of the header
   */
  public void setHeader(String headerName, List<String> headerValues) {
    if (headers == null) {
      headers = new HashMap<>();
    }
    headers.put(headerName, headerValues);
  }
  
  /**
   * Set all headers values, erasing previous values.
   * @param headers The headers to set
   */
  public void setHeaders(Map<String, List<String>> headers) {
    this.headers = headers;
  }
  
  /**
   * @return The request that originated this response
   */
  public HttpRequest getRequest() {
    return request;
  }

  /**
   * @param request The request that originated this response
   */
  public void setRequest(HttpRequest request) {
    this.request = request;
  }
  
  /**
   * @return The time when the response was received
   */
  public Date getTime() {
    return time;
  }

  /**
   * @param time The time when the response was received
   */
  public void setTime(Date time) {
    this.time = time;
  }
  
  /**
   * @return The round trip time (time elapsed between request was sent and
   *         response was received) in milliseconds
   */
  public long getRoundTrip() {
    if (this.time != null && this.request != null && this.request.getTime() != null) {
      return this.time.getTime() - this.request.getTime().getTime();
    }
    return 0L;
  }
  
  /**
   * Returns a JSON like string representation of this object, with properties ordered by importance.
   * Some fields are pretty printed or shortened to enhance readability.
   * @return A string representation of this object, with properties in JSON format ordered by importance.
   * @see HttpResponseToStringJsonSerializer
   */
  @Override
  public String toString() {
    return JsonUtil.pojoToJsonString(this, TOSTRING_OBJECT_MAPPER);
  }

}
