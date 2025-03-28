package org.jxapi.netutils.rest;

import java.util.LinkedHashMap;
import java.util.Map;

import org.jxapi.exchange.ExchangeApi;
import org.jxapi.util.EncodingUtil;
import org.jxapi.util.JsonUtil;

/**
 * Represents the response from a call to a REST endpoint in an
 * {@link ExchangeApi} implementation.<br>
 * Raw response to an HTTP call is encapsulated in an {@link HttpResponse}
 * object.
 * The response to a REST wraps the HTTP response and the response object
 * deserialized from HTTP response.<br>
 * Client implementation should check if the response is OK using
 * {@link #isOk()} method, before accessing the response object.
 * 
 * @param <A> the type of the response object
 */
public class RestResponse<A> {

  private int httpStatus;

  private Exception exception;

  private A response;

  private HttpResponse httpResponse;

  /**
   * Default constructor using <code>null</code> HTTP response.
   */
  public RestResponse() {
    this(null);
  }

  /**
   * Constructor using the given HTTP response.
   * 
   * @param httpResponse the HTTP response
   */
  public RestResponse(HttpResponse httpResponse) {
    if (httpResponse != null) {
      this.httpResponse = httpResponse;
      this.httpStatus = httpResponse.getResponseCode();
      this.exception = httpResponse.getException();
    }
  }

  /**
   * @return the HTTP status code.
   */
  public int getHttpStatus() {
    return httpStatus;
  }

  /**
   * Sets the HTTP status code.
   * 
   * @param httpResponseCode the HTTP status code
   */
  public void setHttpStatus(int httpResponseCode) {
    this.httpStatus = httpResponseCode;
  }

  /**
   * Returns the exception that caused the response to fail. It can be
   * <code>null</code> if the response is successful.<br>
   * Otherwise, the exception may come from the HTTP response or from the
   * deserialization of the response object.
   * 
   * @return the exception that caused the response to fail
   */
  public Exception getException() {
    return exception;
  }

  /**
   * Sets the exception that caused the response to fail.
   * 
   * @param exception the exception that caused the response to fail
   */
  public void setException(Exception exception) {
    this.exception = exception;
  }

  /**
   * @return the response object deserialized from the HTTP response body.
   */
  public A getResponse() {
    return response;
  }

  /**
   * Sets the response object deserialized from the HTTP response body.
   * 
   * @param response the response object deserialized from the HTTP response body
   */
  public void setResponse(A response) {
    this.response = response;
  }

  /**
   * @return <code>true</code> if the response is OK, i.e. the HTTP status code is
   * 200, and there is no exception.
   */
  public boolean isOk() {
    return exception == null && HttpResponse.isStatusCodeOk(httpStatus);
  }

  /**
   * @return the raw HTTP response.
   */
  public HttpResponse getHttpResponse() {
    return httpResponse;
  }

  /**
   * Sets the raw HTTP response.
   * 
   * @param httpResponse the raw HTTP response
   */
  public void setHttpResponse(HttpResponse httpResponse) {
    this.httpResponse = httpResponse;
  }

  /**
   * Returns the endpoint name of the HTTP request.
   * 
   * @return the endpoint name of the HTTP request, or <code>null</code> if the
   *         HTTP response is <code>null</code> or the HTTP request in that
   *         response is <code>null</code>
   */
  public String getEndpoint() {
    if (httpResponse != null) {
      HttpRequest request = httpResponse.getRequest();
      if (request != null) {
        return request.getEndpoint();
      }
    }
    return null;
  }
  
  /**
   * @return String representation of the response.
   */
  public String toString() {
    Map<String, Object> fields = new LinkedHashMap<>();
    fields.put("httpStatus", httpStatus);
    if (exception != null) {
      fields.put("exception", exception.toString());
    }
    if (response != null) {
      fields.put("response", EncodingUtil.prettyPrintLongString(JsonUtil.pojoToJsonString(response), 512));
    } else if (httpResponse != null) {
      fields.put("body", httpResponse.getBody());
      fields.put("time", httpResponse.getTime());
      fields.put("roundtrip", httpResponse.getRoundTrip());
    }
    return getClass().getSimpleName() + JsonUtil.pojoToJsonString(fields);
  }

}
