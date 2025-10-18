package org.jxapi.netutils.rest;

import org.jxapi.exchange.ExchangeApi;
import org.jxapi.netutils.rest.pagination.NextPageResolver;
import org.jxapi.netutils.rest.pagination.PaginatedRestResponse;
import org.jxapi.util.EncodingUtil;
import org.jxapi.util.ExceptionToStringJsonSerializer;
import org.jxapi.util.JsonUtil;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Represents the response from a call to a REST endpoint in an
 * {@link ExchangeApi} implementation.<br>
 * Raw response to an HTTP call is encapsulated in an {@link HttpResponse}
 * object.
 * The response to a REST wraps the HTTP response and the deserialized payload from HTTP response body.<br>
 * Client implementation should check if the response is OK using
 * {@link #isOk()} method, before accessing the response object.
 * 
 * @param <A> the type of the response object
 */
public class RestResponse<A> {
  
  private static final ObjectMapper TOSTRING_OBJECT_MAPPER = EncodingUtil.createDefaultPojoToToStringObjectMapper(
      new ExceptionToStringJsonSerializer(),
      new HttpRequestToStringJsonSerializer(),
      new HttpResponseToStringJsonSerializer(),
      new RestResponseToStringJsonSerializer()      
    );

  private int httpStatus;

  private Exception exception;

  private A response;

  private HttpResponse httpResponse;
  
  private boolean isPaginated = false;
  
  private NextPageResolver<A> nextPageResolver;

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
   * @return <code>true</code> if the response is paginated, i.e. it contains
   *         pagination information. This means the payload object implements
   *          {@link PaginatedRestResponse} or a custom sub-interface of it.
   */
  public boolean isPaginated() {
    return isPaginated;
  }

  /**
   * Sets whether the response is paginated.
   * 
   * @param isPaginated <code>true</code> if the response is paginated,
   *                    <code>false</code> otherwise
   * 
   * @see #isPaginated()                   
   */
  public void setPaginated(boolean isPaginated) {
    this.isPaginated = isPaginated;
  }

  /**
   * Returns the resolver for the next page of the response when the response is paginated.
   * 
   * @return the resolver for the next page of the response, or <code>null</code>
   *         if there is no next page resolver set
   */
  public NextPageResolver<A> getNextPageResolver() {
    return nextPageResolver;
  }

  /**
   * Sets the resolver for the next page of the response when the response is
   * paginated.
   * 
   * @param nextPageResolver the resolver for the next page of the response
   */
  public void setNextPageResolver(NextPageResolver<A> nextPageResolver) {
    this.nextPageResolver = nextPageResolver;
  }
  
  /**
   * Returns a JSON like string representation of this object, with properties ordered by importance.
   * The body and response object are serialized as pretty-printed strings (limited length) to enhance readability.
   * @return JSON like String representation of the response.
   */
  public String toString() {
    return JsonUtil.pojoToJsonString(this, TOSTRING_OBJECT_MAPPER);
  }

}
