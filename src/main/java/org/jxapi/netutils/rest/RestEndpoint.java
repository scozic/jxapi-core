package org.jxapi.netutils.rest;

import java.util.List;

import org.jxapi.exchange.ExchangeEvent;
import org.jxapi.exchange.ExchangeObserver;
import org.jxapi.netutils.deserialization.MessageDeserializer;
import org.jxapi.netutils.rest.pagination.NextPageResolver;
import org.jxapi.netutils.rest.ratelimits.RateLimitRule;
import org.jxapi.netutils.serialization.MessageSerializer;
import org.jxapi.util.EncodingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestEndpoint<R, A> {
  
  private static final Logger log = LoggerFactory.getLogger(RestEndpoint.class);
  
  private HttpMethod httpMethod;
  private String name;
  private String url;
  private HttpRequestExecutor httpRequestExecutor;
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
  
  protected FutureRestResponse<A> submit(R request, NextPageResolver<A> nextPageResolver) {
    if (httpRequestExecutor == null) {
      throw new IllegalStateException("No " + HttpRequestExecutor.class.getSimpleName() + " set");
    }
    HttpRequest httpRequest = createHttpRequest(request);
    log.debug("{} {} > {}", httpRequest.getHttpMethod(), httpRequest.getEndpoint(), httpRequest);
    dispatchApiEvent(ExchangeEvent.createHttpRequestEvent(httpRequest));
    
    FutureRestResponse<A> callback = new FutureRestResponse<>();
    httpRequestExecutor.execute(httpRequest).thenAccept(httpResponse -> {
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

  public String getName() {
    return name;
  }

  public void setName(String endpointName) {
    this.name = endpointName;
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
  
  public ExchangeObserver getObserver() {
    return observer;
  }

  public void setObserver(ExchangeObserver observer) {
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
  
  public HttpMethod getHttpMethod() {
    return httpMethod;
  }

  public void setHttpMethod(HttpMethod httpMethod) {
    this.httpMethod = httpMethod;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }

}
