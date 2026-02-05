package org.jxapi.netutils.websocket;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.jxapi.exchange.ExchangeEvent;
import org.jxapi.exchange.ExchangeObserver;
import org.jxapi.netutils.deserialization.MessageDeserializer;
import org.jxapi.util.EncodingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of a {@link WebsocketEndpoint}.
 * <p>
 * This class manages the subscriptions to a websocket topic, using a
 * {@link WebsocketClient} to wrap {@link Websocket} and subscribe/unsubscribe
 * to topic, and a {@link MessageDeserializer} to deserialize incoming messages.
 * 
 * @param <M> the type of messages that this endpoint will handle.
 * @see WebsocketEndpoint
 */
public class DefaultWebsocketEndpoint<M> implements WebsocketEndpoint<M> {
  
  private final Logger log = LoggerFactory.getLogger(DefaultWebsocketEndpoint.class);
  
  /**
   * The {@link WebsocketClient} that will be used to subscribe/unsubscribe to topics
   */
  private WebsocketClient websocketClient;
  
  /**
   * The {@link MessageDeserializer} that will be used to deserialize incoming messages
   */
  private MessageDeserializer<M> messageDeserializer;
  
  /**
   * Map of subscriptions by topic name (key is the topic name)
   */
  private final Map<String, Subscription> subscriptionsByTopic  = new HashMap<>();
  
  /**
   * Map of subscriptions by subscription id (key is the subscription id)
   */
  protected final Map<String, Subscription> subscriptionsById  = new HashMap<>();
  
  /**
   * The endpoint name
   */
  private String endpointName;
  
  /**
   * The {@link ExchangeObserver} that will be used to dispatch events
   */
  private ExchangeObserver observer;
  
  private AtomicInteger subscriptionCounter = new AtomicInteger(0);

  @Override
  public synchronized String subscribe(WebsocketSubscribeRequest request, WebsocketListener<M> listener) {
    request.setEnpoint(getEndpointName());
    String topic = request.getTopic();
    Subscription sub = subscriptionsByTopic.computeIfAbsent(topic, t -> new Subscription(request));
    String subId = generateSubscriptionId(request);
    sub.addListener(subId, listener);
    subscriptionsById.put(subId, sub);
    if (getObserver() != null) {
      dispatchApiEvent(ExchangeEvent.createWebsocketSubscribeEvent(request, subId));
    }
    log.debug("subscribeTickerStream > {} returned subscriptionId:{}", request, subId);
    return subId;
  }

  @Override
  public synchronized boolean unsubscribe(String unsubscriptionId) {
    Subscription sub = subscriptionsById.remove(unsubscriptionId);
    if (sub == null) {
      return false;
    }
    if (getObserver() != null) {
      dispatchApiEvent(ExchangeEvent.createWebsocketUnsubscribeEvent(sub.request, unsubscriptionId));
    }
    sub.removeListener(unsubscriptionId);
    String topic = sub.request.getTopic();
    int remaining = subscriptionsByTopic.size();
    if (remaining <= 0) {
      log.debug("Unsubscribing from topic {}", topic);
      subscriptionsByTopic.remove(topic);
    } else {
      log.debug("Unsubscribing from topic {} but still {} listeners", topic, remaining);
    }
    return true;
  }

  @Override
  public String getEndpointName() {
    return endpointName;
  }
  
  /**
   * Generates a subscription id for a given {@link WebsocketSubscribeRequest}.
   * 
   * @param request the {@link WebsocketSubscribeRequest} for which to generate a
   *                subscription id
   * @return the generated subscription id
   */
  protected String generateSubscriptionId(WebsocketSubscribeRequest request) {
    return String.valueOf(request.getTopic() + "-" + subscriptionCounter.getAndIncrement());
  }

  /**
   * Dispatches an {@link ExchangeEvent} to the {@link ExchangeObserver}.
   * @param event the {@link ExchangeEvent} to dispatch
   */
  protected void dispatchApiEvent(ExchangeEvent event) {
    this.getObserver().handleEvent(event);
  }
  
  /**
   * Returns the {@link WebsocketClient} used by this endpoint.
   * @return the {@link WebsocketClient} used by this endpoint
   */
  public WebsocketClient getWebsocketClient() {
    return websocketClient;
  }

  /**
   * Sets the {@link WebsocketClient} to be used by this endpoint.
   * @param websocketClient the {@link WebsocketClient} to be used by this endpoint
   */
  public void setWebsocketClient(WebsocketClient websocketClient) {
    this.websocketClient = websocketClient;
  }

  /**
   * Returns the {@link ExchangeObserver} used by this endpoint to dispatch events.
   * @return the {@link ExchangeObserver} used by this endpoint to dispatch events
   */
  public ExchangeObserver getObserver() {
    return observer;
  }

  /**
   * Sets the {@link ExchangeObserver} to be used by this endpoint to dispatch events.
   * @param observer the {@link ExchangeObserver} to be used by this endpoint to dispatch events
   */
  public void setObserver(ExchangeObserver observer) {
    this.observer = observer;
  }

  /**
   * Sets the endpoint name.
   * @param endpointName the endpoint name to set
   */
  public void setEndpointName(String endpointName) {
    this.endpointName = endpointName;
  }

  /**
   * Returns the {@link MessageDeserializer} used by this endpoint to deserialize incoming messages.
   * @return the {@link MessageDeserializer} used by this endpoint to deserialize incoming messages
   */
  public MessageDeserializer<M> getMessageDeserializer() {
    return messageDeserializer;
  }

  /**
   * Sets the {@link MessageDeserializer} to be used by this endpoint to deserialize incoming messages.
   * @param messageDeserializer the {@link MessageDeserializer} to be used by this endpoint to deserialize incoming messages
   */
  public void setMessageDeserializer(MessageDeserializer<M> messageDeserializer) {
    this.messageDeserializer = messageDeserializer;
  }

  private class Subscription {
    final WebsocketSubscribeRequest request;
    final Map<String , WebsocketListener<M>> listeners = new HashMap<>();
    
    public Subscription(WebsocketSubscribeRequest request) {
      this.request = request;
    }
    
    public void addListener(String subscriptionId, WebsocketListener<M> listener) {
      listeners.put(subscriptionId, listener);
      if (listeners.size() == 1) {
        // First subscription
        getWebsocketClient().subscribe(request, this::dispatch);
      }
    }
    
    public void removeListener(String subscriptionId) {
      listeners.remove(subscriptionId);
      if (listeners.size() <= 0) {
        getWebsocketClient().unsubscribe(request.getTopic());
      }
    }
    
    private void dispatch(String message) {
      try {
        if (!listeners.isEmpty()) {
          M msg = getMessageDeserializer().deserialize(message);
          listeners.values().forEach(l -> l.handleMessage(msg));
          if (getObserver() != null) {
            dispatchApiEvent(ExchangeEvent.createWebsocketMessageEvent(request, message));
          }
        }
      } catch (Exception ex) {
        String errMsg = "Error while dispatching message [" + EncodingUtil.prettyPrintLongString(message) + "]"; 
        log.error(errMsg, ex);
        dispatchApiEvent(ExchangeEvent.createWebsocketErrorEvent(new WebsocketException(errMsg, ex)));
      }
      
    }
  }

}
