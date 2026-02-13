package org.jxapi.netutils.rest;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jxapi.netutils.rest.ratelimits.RateLimitRule;
import org.jxapi.netutils.rest.ratelimits.RequestThrottler;
import org.jxapi.util.EncodingUtil;
import org.jxapi.util.ExceptionToStringJsonSerializer;
import org.jxapi.util.JsonUtil;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Generic HTTP request for a REST API call.
 */
public class HttpRequest {
  
  private static final ObjectMapper TOSTRING_OBJECT_MAPPER = EncodingUtil.createDefaultPojoToToStringObjectMapper(
      new ExceptionToStringJsonSerializer(),
      new HttpRequestToStringJsonSerializer()
    );
  
  /**
   * Creates a new {@link HttpRequest} object with <code>null</code> body.
   * 
   * @param endpoint   Name of the endpoint on which the request is made.
   * @param url        full request URL, including request parameters
   * @param httpMethod {@link HttpMethod} used for this request.
   * @param request    Unserialized request object.
   * @param rateLimits List of {@link RateLimitRule} to apply to this request.
   * @param weight     Weight of this request if some weight-based rate limiting
   *                   is applied.              
   * @return a new {@link HttpRequest} object with the given parameters, and the
   *         current time.
   */
  public static HttpRequest create(String endpoint,
      String url,
      HttpMethod httpMethod,
      Object request,
      List<RateLimitRule> rateLimits,
      int weight) {
    return create(endpoint, url, httpMethod, request, rateLimits, weight, null);
  }

  /**
   * Creates a new {@link HttpRequest} object with a body.
   * 
   * @param endpoint   Name of the endpoint on which the request is made.
   * @param url        full request URL, including request parameters
   * @param httpMethod {@link HttpMethod} used for this request.
   * @param request    Unserialized request object.
   * @param rateLimits List of {@link RateLimitRule} to apply to this request.
   * @param weight     Weight of this request if some weight-based rate limiting
   *                   is applied.
   * @param body        The HTTP request body                    
   * @return a new {@link HttpRequest} object with the given parameters, and the
   *         current time.
   */
  public static HttpRequest create(String endpoint,
      String url,
      HttpMethod httpMethod,
      Object request,
      List<RateLimitRule> rateLimits,
      int weight,
      String body) {
    HttpRequest r = new HttpRequest();
    r.setEndpoint(endpoint);
    r.setUrl(url);
    r.setHttpMethod(httpMethod);
    r.setRequest(request);
    r.setRateLimits(rateLimits);
    r.setWeight(weight);
    r.setTime(new Date());
    r.setBody(body);
    return r;
  }

  /**
   * Name of the endpoint on which the request is made.
   */
  private String endpoint;

  /**
   * Full request URL, including request parameters
   */
  private String url;

  /**
   * Headers to be sent in request
   */
  private Map<String, List<String>> headers;

  /**
   * {@link HttpMethod} used for this request.
   */
  private HttpMethod httpMethod;

  /**
   * Body of HTTP request to send
   */
  private String body;

  /**
   * Unserialized request object. Provided for convenience. It is duty of the
   * caller to serialize it either in the body or in the URL.
   */
  private Object request;

  /**
   * List of {@link RateLimitRule} to apply to this request.
   */
  private List<RateLimitRule> rateLimits;

  /**
   * Weight of this request if some weight-based rate limiting is applied, see
   * {@link RateLimitRule#getWeight()}
   */
  private int weight;

  /**
   * Time when the request was submitted.
   */
  private Date time;

  /**
   * Duration the request was throttled by {@link RequestThrottler} before being
   * actually sent.
   */
  private long throttledTime = 0L;

  /**
   * @return full request URL, including request parameters
   */
  public String getUrl() {
    return url;
  }

  /**
   * @param url full request URL, including request parameters
   */
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   * @return Headers to be sent in request
   */
  public Map<String, List<String>> getHeaders() {
    return headers;
  }

  /**
   * Sets a header (single value)
   * 
   * @param headerName  The name of the header
   * @param headerValue The value of the header
   */
  public void setHeader(String headerName, String headerValue) {
    setHeader(headerName, List.of(headerValue));
  }

  /**
   * Sets a header with multiple values
   * 
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
   * Sets all headers.
   * 
   * @param headers Headers to be sent in request
   */
  public void setHeaders(Map<String, List<String>> headers) {
    this.headers = headers;
  }

  /**
   * @return {@link HttpMethod} used for this request.
   */
  public HttpMethod getHttpMethod() {
    return httpMethod;
  }

  /**
   * @param httpMethod {@link HttpMethod} used for this request.
   */
  public void setHttpMethod(HttpMethod httpMethod) {
    this.httpMethod = httpMethod;
  }

  /**
   * @return body of HTTP request to send
   */
  public String getBody() {
    return body;
  }

  /**
   * @param body body of HTTP request to send
   */
  public void setBody(String body) {
    this.body = body;
  }

  /**
   * @return List of {@link RateLimitRule} to apply to this request.
   */
  public List<RateLimitRule> getRateLimits() {
    return rateLimits;
  }

  /**
   * @param rateLimits List of {@link RateLimitRule} to apply to this request.
   */
  public void setRateLimits(List<RateLimitRule> rateLimits) {
    this.rateLimits = rateLimits;
  }

  /**
   * @return Weight of this request if some weight-based rate limiting is applied,
   *         see {@link RateLimitRule#getMaxTotalWeight()}
   */
  public int getWeight() {
    return weight;
  }

  /**
   * @param weight Weight of this request if some weight-based rate limiting is
   *               applied, see {@link RateLimitRule#getMaxTotalWeight()}
   */
  public void setWeight(int weight) {
    this.weight = weight;
  }

  /**
   * @return Unserialized request object. Provided for convenience. It is duty of
   *         the caller to serialize it either in the body or in the URL.
   */
  public Object getRequest() {
    return request;
  }

  /**
   * @param request Unserialized request object. Provided for convenience. It is
   *                duty of the caller to serialize it either in the body or in
   *                the URL.
   */
  public void setRequest(Object request) {
    this.request = request;
  }

  /**
   * @return Time when the request was created. Must be set by the caller.
   */
  public Date getTime() {
    return time;
  }

  /**
   * @param time Time when the request was created. Must be set by the caller.
   */
  public void setTime(Date time) {
    this.time = time;
  }

  /**
   * @return Duration the request was throttled by {@link RequestThrottler} before
   *         being actually sent. Set by {@link RequestThrottler}.
   */
  public long getThrottledTime() {
    return throttledTime;
  }

  /**
   * @param throttledTime Duration the request was throttled by
   *                      {@link RequestThrottler} before being actually sent. Set
   *                      by {@link RequestThrottler}.
   */
  public void setThrottledTime(long throttledTime) {
    this.throttledTime = throttledTime;
  }

  /**
   * @return Name of the endpoint on which the request is made.
   */
  public String getEndpoint() {
    return endpoint;
  }

  /**
   * @param endpoint Name of the endpoint on which the request is made.
   */
  public void setEndpoint(String endpoint) {
    this.endpoint = endpoint;
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
