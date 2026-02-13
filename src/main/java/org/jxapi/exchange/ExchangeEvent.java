package org.jxapi.exchange;

import org.jxapi.netutils.rest.HttpRequest;
import org.jxapi.netutils.rest.HttpRequestToStringJsonSerializer;
import org.jxapi.netutils.rest.HttpResponseToStringJsonSerializer;
import org.jxapi.netutils.rest.RestResponse;
import org.jxapi.netutils.rest.RestResponseToStringJsonSerializer;
import org.jxapi.netutils.websocket.WebsocketException;
import org.jxapi.netutils.websocket.WebsocketSubscribeRequest;
import org.jxapi.util.EncodingUtil;
import org.jxapi.util.ExceptionToStringJsonSerializer;
import org.jxapi.util.JsonUtil;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Encapsulates an event that occurs during the execution of an exchange API
 * REST endpoint call or websocket subscription, unsubscription, message, or
 * error. Event is identified by its type, and carries additional information
 * depending on the type. <br>
 * {@link Exchange} client can subscribe to this event stream using
 * {@link Exchange#subscribeObserver(ExchangeObserver)}
 * 
 * 
 * @see ExchangeEventType
 * @see ExchangeObserver
 */
public class ExchangeEvent {
  
  private static final ObjectMapper TOSTRING_OBJECT_MAPPER = EncodingUtil.createDefaultPojoToToStringObjectMapper(
      new ExceptionToStringJsonSerializer(),
      new HttpRequestToStringJsonSerializer(),
      new HttpResponseToStringJsonSerializer(),
      new RestResponseToStringJsonSerializer(),
      new ExchangeApiEventToStringJsonSerializer()
    );

  /**
   * Factory method to create a new {@link ExchangeEvent} object for a REST
   * response.
   * 
   * @param response The REST response object.
   * @return an event of type {@link ExchangeEventType#HTTP_RESPONSE} with the
   *         given response.
   */
  public static ExchangeEvent createRestResponseEvent(RestResponse<?> response) {
    ExchangeEvent event = new ExchangeEvent(ExchangeEventType.HTTP_RESPONSE);
    event.setHttpResponse(response);
    return event;
  }

  /**
   * Factory method to create a new {@link ExchangeEvent} object for a REST
   * request.
   * 
   * @param request The REST request object.
   * @return an event of type {@link ExchangeEventType#HTTP_REQUEST} with the
   *         given request.
   */
  public static ExchangeEvent createHttpRequestEvent(HttpRequest request) {
    ExchangeEvent event = new ExchangeEvent(ExchangeEventType.HTTP_REQUEST);
    event.setHttpRequest(request);
    return event;
  }

  /**
   * Factory method to create a new {@link ExchangeEvent} object for a received
   * websocket message.
   * 
   * @param request The websocket subscription request to the topic that received
   *                the message is related to.
   * @param msg     The websocket message.
   * @return an event of type {@link ExchangeEventType#WEBSOCKET_MESSAGE} with
   *         the given subscription request and message.
   */
  public static ExchangeEvent createWebsocketMessageEvent(WebsocketSubscribeRequest request, String msg) {
    ExchangeEvent event = new ExchangeEvent(ExchangeEventType.WEBSOCKET_MESSAGE);
    event.setWebsocketSubscribeRequest(request);
    event.setWebsocketMessage(msg);
    return event;
  }

  /**
   * Factory method to create a new {@link ExchangeEvent} object for a
   * websocket subscription.
   * 
   * @param request The websocket subscription request.
   * @param subId   The subscription ID created for this subscription.
   * @return an event of type {@link ExchangeEventType#WEBSOCKET_SUBSCRIBE}
   *         with the given subscription request and ID.
   */
  public static ExchangeEvent createWebsocketSubscribeEvent(WebsocketSubscribeRequest request,
      String subId) {
    ExchangeEvent event = new ExchangeEvent(ExchangeEventType.WEBSOCKET_SUBSCRIBE);
    event.setWebsocketSubscribeRequest(request);
    event.setWebsocketSubscriptionId(subId);
    return event;
  }

  /**
   * Factory method to create a new {@link ExchangeEvent} object for a
   * websocket unsubscription.
   * 
   * @param request The websocket subscription request.
   * @param subId   The subscription ID created for this subscription.
   * @return an event of type {@link ExchangeEventType#WEBSOCKET_UNSUBSCRIBE}
   *         with the given subscription request and ID.
   */
  public static ExchangeEvent createWebsocketUnsubscribeEvent(WebsocketSubscribeRequest request,
      String subId) {
    ExchangeEvent event = new ExchangeEvent(ExchangeEventType.WEBSOCKET_UNSUBSCRIBE);
    event.setWebsocketSubscribeRequest(request);
    event.setWebsocketSubscriptionId(subId);
    return event;
  }

  /**
   * Factory method to create a new {@link ExchangeEvent} object for a
   * websocket error.
   * <p>
   * Remark: According to WebsocketClient specifications, a websocket error is
   * usually followed by a websocket reconnection (unless no reconnection is
   * configured).
   * Client watching for websocket errors should be aware that no action may be
   * needed, as the WebsocketClient will handle the reconnection.
   * However, when multiple errors are received in a short period of time, it may
   * be a sign of a more serious issue.
   * 
   * @param error The websocket error.
   * @return an event of type {@link ExchangeEventType#WEBSOCKET_ERROR} with
   *         the given error.
   */
  public static ExchangeEvent createWebsocketErrorEvent(WebsocketException error) {
    ExchangeEvent event = new ExchangeEvent(ExchangeEventType.WEBSOCKET_ERROR);
    event.setWebsocketError(error);
    return event;
  }

  private final ExchangeEventType type;
  private String exchangeName;
  private String exchangeId;
  private String exchangeApiName;
  private HttpRequest httpRequest;
  private RestResponse<?> httpResponse;
  private WebsocketSubscribeRequest websocketSubscribeRequest;
  private String websocketMessage;
  private WebsocketException websocketError;
  private String websocketSubscriptionId;

  /**
   * Creates a new {@link ExchangeEvent} object.
   * 
   * @param eventType The type of the event.
   */
  public ExchangeEvent(ExchangeEventType eventType) {
    this.type = eventType;
  }

  /**
   * @return The type of the event, see {@link ExchangeEventType}.
   */
  public ExchangeEventType getType() {
    return type;
  }

  /**
   * @return The name of the exchange this event is related to, see
   *         {@link Exchange#getName()}.
   */
  public String getExchangeName() {
    return exchangeName;
  }

  /**
   * Sets the name of the exchange this event is related to, see
   * {@link Exchange#getName()}.
   * 
   * @param exchangeName The name of the exchange.
   */
  public void setExchangeName(String exchangeName) {
    this.exchangeName = exchangeName;
  }

  /**
   * Returns the name of the exchange API this event is related to.<br>
   * Remark: The name of API group is generated as a constant named 'ID' in
   * corresponding {@link ExchangeApi} implementation of each API group.
   * 
   * @return The name of the exchange API this event is related to.
   */
  public String getExchangeApiName() {
    return exchangeApiName;
  }

  /**
   * Sets the name of the exchange API this event is related to<br>
   * Remark: The name of API group is generated as a constant named 'ID' in
   * corresponding {@link ExchangeApi} implementation of each API group.
   * 
   * @param exchangeApiName The name of the exchange API.
   */
  public void setExchangeApiName(String exchangeApiName) {
    this.exchangeApiName = exchangeApiName;
  }

  /**
   * @return The REST or websocket endpoint this event is related to. Is retrieved
   *         from the websocket subscribe request, the HTTP request or REST
   *         response object.
   */
  public String getEndpoint() {
    if (websocketSubscribeRequest != null) {
      return websocketSubscribeRequest.getEnpoint();
    }
    if (httpRequest != null) {
      return httpRequest.getEndpoint();
    }
    if (httpResponse != null) {
      return httpResponse.getEndpoint();
    }
    return null;
  }

  /**
   * @return The HTTP request object, if this event is of type
   *         {@link ExchangeEventType#HTTP_REQUEST} or
   *         {@link ExchangeEventType#HTTP_RESPONSE}.
   */
  public HttpRequest getHttpRequest() {
    return httpRequest;
  }

  /**
   * Sets the HTTP request object.
   * 
   * @param httpRequest The HTTP request object.
   */
  public void setHttpRequest(HttpRequest httpRequest) {
    this.httpRequest = httpRequest;
  }

  /**
   * @return The REST response object, if this event is of type
   *         {@link ExchangeEventType#HTTP_RESPONSE}.
   */
  @SuppressWarnings("unchecked")
  public RestResponse<Object> getHttpResponse() {
    return (RestResponse<Object>) httpResponse;
  }

  /**
   * Sets the REST response object.
   * 
   * @param httpResponse The REST response object.
   */
  public void setHttpResponse(RestResponse<?> httpResponse) {
    this.httpResponse = httpResponse;
  }

  /**
   * @return The websocket subscription request, if this event is of type
   *         {@link ExchangeEventType#WEBSOCKET_SUBSCRIBE},
   *         {@link ExchangeEventType#WEBSOCKET_UNSUBSCRIBE} or
   *         {@link ExchangeEventType#WEBSOCKET_MESSAGE}.
   */
  public WebsocketSubscribeRequest getWebsocketSubscribeRequest() {
    return websocketSubscribeRequest;
  }

  /**
   * Sets the websocket subscription request.
   * 
   * @param websocketSubscribeRequest The websocket subscription request.
   */
  public void setWebsocketSubscribeRequest(WebsocketSubscribeRequest websocketSubscribeRequest) {
    this.websocketSubscribeRequest = websocketSubscribeRequest;
  }

  /**
   * @return The websocket message, if this event is of type
   *         {@link ExchangeEventType#WEBSOCKET_MESSAGE}.
   */
  public String getWebsocketMessage() {
    return websocketMessage;
  }

  /**
   * Sets the websocket message.
   * 
   * @param websocketMessage The websocket message.
   */
  public void setWebsocketMessage(String websocketMessage) {
    this.websocketMessage = websocketMessage;
  }

  /**
   * @return The websocket error, if this event is of type
   *         {@link ExchangeEventType#WEBSOCKET_ERROR}.
   */
  public WebsocketException getWebsocketError() {
    return websocketError;
  }

  /**
   * Sets the websocket error.
   * 
   * @param websocketError The websocket error.
   */
  public void setWebsocketError(WebsocketException websocketError) {
    this.websocketError = websocketError;
  }

  /**
   * @return The subscription ID, if this event is of type
   *         {@link ExchangeEventType#WEBSOCKET_SUBSCRIBE} or
   *         {@link ExchangeEventType#WEBSOCKET_UNSUBSCRIBE}.
   */
  public String getWebsocketSubscriptionId() {
    return websocketSubscriptionId;
  }

  /**
   * Sets the subscription ID.
   * 
   * @param websocketSubscriptionId The subscription ID.
   */
  public void setWebsocketSubscriptionId(String websocketSubscriptionId) {
    this.websocketSubscriptionId = websocketSubscriptionId;
  }

  /**
   * @return The ID of the exchange this event is related to, see
   *         {@link Exchange#getId()}.
   */
  public String getExchangeId() {
    return exchangeId;
  }

  /**
   * Sets the ID of the exchange this event is related to, see
   * {@link Exchange#getId()}.
   * 
   * @param exchangeId The ID of the exchange.
   */
  public void setExchangeId(String exchangeId) {
    this.exchangeId = exchangeId;
  }

  /**
   * Provides a JSON like string representation of this object, with properties
   * ordered by importance. Some fields are shortened or pretty-printed to
   * enhance readability.
   * @return A JSON like string representation of this object.
   * @see ExchangeApiEventToStringJsonSerializer 
   */
  @Override
  public String toString() {
    return JsonUtil.pojoToJsonString(this, TOSTRING_OBJECT_MAPPER);
  }

}
