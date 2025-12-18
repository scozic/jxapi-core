package org.jxapi.netutils.rest;

import java.util.List;

import org.jxapi.exchange.ExchangeApiEvent;
import org.jxapi.exchange.ExchangeApiObserver;
import org.jxapi.netutils.deserialization.MessageDeserializer;
import org.jxapi.netutils.rest.pagination.NextPageResolver;
import org.jxapi.netutils.rest.ratelimits.RateLimitRule;
import org.jxapi.netutils.rest.ratelimits.RequestThrottler;
import org.jxapi.netutils.serialization.MessageSerializer;
import org.jxapi.util.EncodingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestEndpoint<R, A> {
  
  private static final Logger log = LoggerFactory.getLogger(RestEndpoint.class);
  
  private HttpMethod httpMethod;
  private String endpointName;
  private String url;
  private HttpRequestExecutor httpRequestExecutor;
  private MessageDeserializer<A> deserializer;
  private MessageSerializer<R> serializer;
  private List<RateLimitRule> rateLimitRules;
  private int weight;
  private RequestThrottler requestThrottler;
  private ExchangeApiObserver observer;
  private HttpRequestUrlParamsSerializer<R> urlParamsSerializer;
  
//  /**
//   * Submits a REST request asynchronously using the specified request and message
//   * deserializer to deserialize response. If a request throttler is set, the
//   * request is submitted through the throttler. <br>
//   * This method should used by subclasses to submit REST requests.
//   * 
//   * @param <R>                   The type of the request to submit, must extend
//   *                              {@link PaginatedRestRequest}
//   * @param <A>                   The type of the response to the request
//   * @param request               The request to submit
//   * @param hasBody               Indicates whether the request has a body, and 
//   *                              thus needs its request to be serialized before submission.
//   * @param deserializer          The deserializer to use to deserialize the
//   *                              response
//   * @param paginatedRestEndpoint The function for calling the paginated REST
//   *                              endpoint, from endpint enclosing API group
//   *                              interface.
//   * @return The response to the request, as a {@link FutureRestResponse}
//   */
//  @SuppressWarnings("unchecked")
//  protected <R extends PaginatedRestRequest, A extends PaginatedRestResponse> FutureRestResponse<A> submitPaginated(
//      HttpRequest request, 
//      MessageDeserializer<A> deserializer, 
//      Function<R, FutureRestResponse<A>> paginatedRestEndpoint) {
//    return submit(request, deserializer, PaginationUtil.getNextPageResolver((R) request.getRequest(), paginatedRestEndpoint));
//  }
  
  /**
   * Submits a REST request asynchronously using the specified request and message
   * deserializer to deserialize response. If a request throttler is set, the
   * request is submitted through the throttler. <br>
   * This method should used by subclasses to submit REST requests.
   * 
   * @param <A>          The type of the response to the request
   * @param request      The request to submit
   * @param deserializer The deserializer to use to deserialize the response
   * @return The response to the request, as a {@link FutureRestResponse}
   */
  public FutureRestResponse<A> submit(R request) {
    return submit(request, null);
  }
  
  protected HttpRequest createHttpRequest(R requestData) {
    String u = url;
    if (urlParamsSerializer != null) {
      u = urlParamsSerializer.serializeUrlParams(requestData, u);
    }
    return HttpRequest.create(endpointName, u, httpMethod, requestData, rateLimitRules, weight);
  }
  
  protected FutureRestResponse<A> submit(R request, NextPageResolver<A> nextPageResolver) {
    HttpRequest httpRequest = createHttpRequest(request);
    log.debug("{} {} > {}", httpRequest.getHttpMethod(), httpRequest.getEndpoint(), httpRequest);
    dispatchApiEvent(ExchangeApiEvent.createHttpRequestEvent(httpRequest));
    if (requestThrottler != null) {
      return requestThrottler.submit(httpRequest, r -> { 
        try {
          return submitNow(r, nextPageResolver);
        } catch (Exception ex) {
          log.error("Error submitting request " + httpRequest, ex);
          FutureRestResponse<A> callback = new FutureRestResponse<>();
          RestResponse<A> response = new RestResponse<>();
          response.setException(ex);
          callback.complete(response);
          return callback;
        }
      });
    } else {
      return submitNow(httpRequest, nextPageResolver);
    }
  }

  private FutureRestResponse<A> submitNow(HttpRequest request, NextPageResolver<A> nextPageResolver) {
    if (httpRequestExecutor == null) {
      throw new IllegalStateException("No " + HttpRequestExecutor.class.getSimpleName() + " set");
    }
    
    FutureRestResponse<A> callback = new FutureRestResponse<>();
    httpRequestExecutor.execute(request).thenAccept(httpResponse -> {
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
      log.debug("{} {} < {}", request.getHttpMethod(), request.getEndpoint(), response);
      dispatchApiEvent(ExchangeApiEvent.createRestResponseEvent(response));
      try {
        callback.complete(response);
      } catch (Exception ex) {
        log.error("Error completing request " + request, ex);
      }
    });
    return callback;
  }
  
  /**
   * Dispatches the specified exchange API event to all observers.
   * <br>
   * Needs usually not be called by subclasses, as it is called for every call to
   * {@link #submit(HttpRequest, boolean, MessageDeserializer)} and
   * {@link #createWebsocketEndpoint(String, MessageDeserializer)}.
   * 
   * @param event The exchange API event to dispatch.
   */
  protected void dispatchApiEvent(ExchangeApiEvent event) {
    observer.handleEvent(event);
  }

  public String getEndpointName() {
    return endpointName;
  }

  public void setEndpointName(String endpointName) {
    this.endpointName = endpointName;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public HttpRequestExecutor getHttpRequestExecutor() {
    return httpRequestExecutor;
  }

  public void setHttpRequestExecutor(HttpRequestExecutor httpRequestExecutor) {
    this.httpRequestExecutor = httpRequestExecutor;
  }

  public MessageDeserializer<A> getDeserializer() {
    return deserializer;
  }

  public void setDeserializer(MessageDeserializer<A> deserializer) {
    this.deserializer = deserializer;
  }

  public MessageSerializer<R> getSerializer() {
    return serializer;
  }

  public void setSerializer(MessageSerializer<R> serializer) {
    this.serializer = serializer;
  }

  public List<RateLimitRule> getRateLimitRules() {
    return rateLimitRules;
  }

  public void setRateLimitRules(List<RateLimitRule> rateLimitRules) {
    this.rateLimitRules = rateLimitRules;
  }
  
  public int getWeight() {
    return weight;
  }
  
  public void setWeight(int weight) {
    this.weight = weight;
  }
  
  public ExchangeApiObserver getObserver() {
    return observer;
  }

  public void setObserver(ExchangeApiObserver observer) {
    this.observer = observer;
  }
  
  public HttpRequestUrlParamsSerializer<R> getUrlParamsSerializer() {
    return urlParamsSerializer;
  }

  public void setUrlParamsSerializer(HttpRequestUrlParamsSerializer<R> urlParamsSerializer) {
    this.urlParamsSerializer = urlParamsSerializer;
  }

  public boolean isPaginated() {
    return false;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }

}
