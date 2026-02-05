package org.jxapi.netutils.rest;

import java.util.List;

import org.jxapi.exchange.ExchangeEvent;
import org.jxapi.exchange.ExchangeObserver;
import org.jxapi.netutils.deserialization.MessageDeserializer;
import org.jxapi.netutils.rest.pagination.NextPageResolver;
import org.jxapi.netutils.rest.ratelimits.RateLimitRule;
import org.jxapi.netutils.rest.ratelimits.RequestThrottler;
import org.jxapi.netutils.serialization.MessageSerializer;
import org.jxapi.util.EncodingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generic REST API endpoint. This class provides the basic functionality to submit
 * REST requests and handle responses, including request serialization,
 * response deserialization, and event dispatching.
 * <p>
 * This class is parameterized with the type of the request objects (R) and the
 * type of the response objects (A). Subclasses can specify these types to
 * provide type safety when submitting requests and handling responses.
 * 
 * @param <R> the type of the request objects that this endpoint will handle
 * @param <A> the type of the response objects that this endpoint will handle
 */
public class RestEndpoint<R, A> {
  
  private static final Logger log = LoggerFactory.getLogger(RestEndpoint.class);
  
  private HttpMethod httpMethod;
  private String name;
  private String url;
  private HttpClient httpClient;
  private MessageDeserializer<A> deserializer;
  private MessageSerializer<R> serializer;
  private List<RateLimitRule> rateLimitRules;
  private int weight;
  private ExchangeObserver observer;
  private HttpRequestUrlParamsSerializer<R> urlParamsSerializer;
  
  /**
   * Submits a REST request asynchronously using the specified request and message
   * deserializer to deserialize response. If a request throttler is set, the
   * request is submitted through the throttler. <br>
   * This method should used by subclasses to submit REST requests.
   * 
   * @param request      The request to submit
   * @return The response to the request, as a {@link FutureRestResponse}
   */
  public FutureRestResponse<A> submit(R request) {
    return submit(request, null);
  }
  
  /**
   * Creates an {@link HttpRequest} object from the specified request data, using
   * the URL and HTTP method of this endpoint, and applying any URL parameter
   * serialization and request body serialization as needed.
   * 
   * @param requestData The request data to create the HTTP request from
   * @return An {@link HttpRequest} object representing the HTTP request to be
   *         submitted for the given request data.
   */
  protected HttpRequest createHttpRequest(R requestData) {
    String u = url;
    String body = null;
    if (requestData != null) {
      if (urlParamsSerializer != null) {
        u = urlParamsSerializer.serializeUrlParams(requestData, u);
      }
      if (serializer != null) {
        body = serializer.serialize(requestData);
      }
    }
    return HttpRequest.create(
        name, 
        u, 
        httpMethod, 
        requestData, 
        rateLimitRules, 
        weight, 
        body);
  }
  
  /**
   * Submits a REST request asynchronously using the specified request and message
   * deserializer to deserialize response. If a request throttler is set, the
   * request is submitted through the throttler. <br>
   * This method should used by subclasses to submit REST requests.
   * 
   * @param request          The request to submit
   * @param nextPageResolver The {@link NextPageResolver} to use for paginated
   *                         responses, or <code>null</code> if the response is
   *                         not paginated.
   * @return The response to the request, as a {@link FutureRestResponse}
   */
  protected FutureRestResponse<A> submit(R request, NextPageResolver<A> nextPageResolver) {
    if (httpClient == null) {
      throw new IllegalStateException("No " + HttpRequestExecutor.class.getSimpleName() + " set");
    }
    HttpRequest httpRequest = createHttpRequest(request);
    log.debug("{} {} > {}", httpRequest.getHttpMethod(), httpRequest.getEndpoint(), httpRequest);
    dispatchApiEvent(ExchangeEvent.createHttpRequestEvent(httpRequest));
    
    FutureRestResponse<A> callback = new FutureRestResponse<>();
    httpClient.execute(httpRequest).thenAccept(httpResponse -> {
      RestResponse<A> response = new RestResponse<>(httpResponse);
      response.setHttpStatus(httpResponse.getResponseCode());
      if (nextPageResolver != null) {
        response.setNextPageResolver(nextPageResolver);
        response.setPaginated(true);
      }
      if(response.isOk()) {
        try {
          response.setResponse(deserializer.deserialize(httpResponse.getBody()));
        } catch (Exception ex) {
          response.setException(ex);
        }
      }
      log.debug("{} {} < {}", httpRequest.getHttpMethod(), httpRequest.getEndpoint(), response);
      dispatchApiEvent(ExchangeEvent.createRestResponseEvent(response));
      try {
        callback.complete(response);
      } catch (Exception ex) {
        log.error("Error completing request " + httpRequest, ex);
      }
    });
    return callback;
  }
  
  /**
   * Dispatches the specified exchange API event to all observers.
   * 
   * @param event The exchange API event to dispatch.
   */
  protected void dispatchApiEvent(ExchangeEvent event) {
    observer.handleEvent(event);
  }

  /**
   * Returns the name of this endpoint. The endpoint name is used for logging and
   * event
   * 
   * @return the name of this endpoint
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of this endpoint. The endpoint name is used for logging and event
   * dispatching, and should be set to a descriptive name for the endpoint, for
   * instance "GetAccountInfoEndpoint".
   * 
   * @param endpointName the name to set for this endpoint
   */
  public void setName(String endpointName) {
    this.name = endpointName;
  }

  /**
   * Returns the URL of this endpoint.
   * 
   * @return the URL of this endpoint
   */
  public String getUrl() {
    return url;
  }

  /**
   * Sets the URL of this endpoint. The URL should be set to the full URL of the
   * REST API endpoint, including any path parameters, but excluding any query
   * parameters (query parameters should be serialized using the
   * {@link HttpRequestUrlParamsSerializer}).
   * 
   * @param url the URL to set for this endpoint
   */
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   * Returns the {@link HttpClient} used by this endpoint to submit REST requests.
   * 
   * @return the {@link HttpClient} used by this endpoint to submit REST requests
   */
  public HttpClient getHttpClient() {
    return httpClient;
  }

  /**
   * Sets the {@link HttpClient} to be used by this endpoint to submit REST
   * requests. The {@link HttpClient} should be set to an instance that is
   * configured to communicate with the REST API that this endpoint is designed
   * for.
   * 
   * @param httpClient the {@link HttpClient} to set for this endpoint
   */
  public void setHttpClient(HttpClient httpClient) {
    this.httpClient = httpClient;
  }

  /**
   * Returns the {@link MessageDeserializer} used by this endpoint to deserialize
   * incoming messages.
   * 
   * @return the {@link MessageDeserializer} used by this endpoint to deserialize
   *         incoming messages
   */
  public MessageDeserializer<A> getDeserializer() {
    return deserializer;
  }

  /**
   * Sets the {@link MessageDeserializer} to be used by this endpoint to deserialize
   * incoming messages. The deserializer should be set to an instance that is
   * capable of deserializing the response format of the REST API that this
   * endpoint is designed for.
   * 
   * @param deserializer the {@link MessageDeserializer} to set for this endpoint
   */
  public void setDeserializer(MessageDeserializer<A> deserializer) {
    this.deserializer = deserializer;
  }

  /**
   * Returns the {@link MessageSerializer} used by this endpoint to serialize
   * outgoing messages.
   * 
   * @return the {@link MessageSerializer} used by this endpoint to serialize
   *         outgoing messages
   */
  public MessageSerializer<R> getSerializer() {
    return serializer;
  }

  /**
   * Sets the {@link MessageSerializer} to be used by this endpoint to serialize
   * outgoing messages. The serializer should be set to an instance that is
   * capable of serializing the request format of the REST API that this
   * endpoint is designed for.
   * 
   * @param serializer the {@link MessageSerializer} to set for this endpoint
   */
  public void setSerializer(MessageSerializer<R> serializer) {
    this.serializer = serializer;
  }

  /**
   * Returns the list of {@link RateLimitRule}s that apply to this endpoint. These
   * rules will be used by the {@link RequestThrottler} to determine how to
   * throttle requests submitted through this endpoint.
   * 
   * @return the list of {@link RateLimitRule}s that apply to this endpoint
   */
  public List<RateLimitRule> getRateLimitRules() {
    return rateLimitRules;
  }

  /**
   * Sets the list of {@link RateLimitRule}s that apply to this endpoint. These
   * rules will be used by the {@link RequestThrottler} to determine how to
   * throttle requests submitted through this endpoint.
   * 
   * @param rateLimitRules the list of {@link RateLimitRule}s to set for this
   *                       endpoint
   */
  public void setRateLimitRules(List<RateLimitRule> rateLimitRules) {
    this.rateLimitRules = rateLimitRules;
  }
  
  /**
   * Returns the weight of this endpoint when some weight-based rate limiting is
   * applied, see {@link RateLimitRule#getMaxTotalWeight()}. The weight of the
   * endpoint will be used by
   * 
   * @return the weight of this endpoint,
   */
  public int getWeight() {
    return weight;
  }

  /**
   * Sets the weight of this endpoint when some weight-based rate limiting is
   * applied, see {@link RateLimitRule#getMaxTotalWeight()}. The weight of the
   * endpoint will be used by the {@link RequestThrottler} to determine how to
   * throttle requests submitted through this endpoint when weight-based rate
   * limiting is applied.
   * 
   * @param weight the weight to set for this endpoint
   */
  public void setWeight(int weight) {
    this.weight = weight;
  }
  
  /**
   * Returns the {@link ExchangeObserver} used by this endpoint to dispatch events.
   * 
   * @return the {@link ExchangeObserver} used by this endpoint to dispatch events
   */
  public ExchangeObserver getObserver() {
    return observer;
  }

  /**
   * Sets the {@link ExchangeObserver} to be used by this endpoint to dispatch
   * events. The observer should be set to an instance that is capable of
   * handling the events dispatched by this endpoint, such as HTTP request and
   * response events.
   * 
   * @param observer the {@link ExchangeObserver} to set for this endpoint to
   *                 dispatch events
   */
  public void setObserver(ExchangeObserver observer) {
    this.observer = observer;
  }
  
  /**
   * Returns the {@link HttpRequestUrlParamsSerializer} used by this endpoint to
   * serialize URL parameters for outgoing requests.
   * 
   * @return the {@link HttpRequestUrlParamsSerializer} used by this endpoint to
   *         serialize URL parameters for outgoing requests
   */
  public HttpRequestUrlParamsSerializer<R> getUrlParamsSerializer() {
    return urlParamsSerializer;
  }

  /**
   * Sets the {@link HttpRequestUrlParamsSerializer} to be used by this endpoint to
   * serialize URL parameters for outgoing requests. The serializer should be set
   * to an instance that is capable of serializing the URL parameters for the REST
   * API that this endpoint is designed for.
   * 
   * @param urlParamsSerializer the {@link HttpRequestUrlParamsSerializer} to set
   *                            for this endpoint to serialize URL parameters for
   *                            outgoing requests
   */
  public void setUrlParamsSerializer(HttpRequestUrlParamsSerializer<R> urlParamsSerializer) {
    this.urlParamsSerializer = urlParamsSerializer;
  }

  /**
   * Returns whether the responses from this endpoint are paginated. If the
   * responses are paginated, a {@link NextPageResolver} should be provided when
   * submitting requests through this endpoint to handle pagination.
   * 
   * @return <code>true</code> if the responses from this endpoint are paginated,
   *         <code>false</code> otherwise
   */
  public boolean isPaginated() {
    return false;
  }
  
  /**
   * Returns the {@link HttpMethod} used by this endpoint for outgoing requests.
   * 
   * @return the {@link HttpMethod} used by this endpoint for outgoing requests
   */
  public HttpMethod getHttpMethod() {
    return httpMethod;
  }

  /**
   * Sets the {@link HttpMethod} to be used by this endpoint for outgoing requests.
   * The HTTP method should be set to the method that is appropriate for the REST
   * API endpoint that this class represents, such as GET, POST, PUT, or DELETE.
   * 
   * @param httpMethod the {@link HttpMethod} to set for this endpoint for outgoing
   *                   requests
   */
  public void setHttpMethod(HttpMethod httpMethod) {
    this.httpMethod = httpMethod;
  }
  
  /**
   * Returns a string representation of this endpoint, including its name, URL, HTTP
   * method, and other relevant information. This method is useful for logging and
   * debugging purposes.
   * 
   * @return a string representation of this endpoint
   */
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }

}
