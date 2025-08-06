package org.jxapi.netutils.websocket;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.jxapi.exchange.ExchangeApiEvent;
import org.jxapi.exchange.ExchangeApiObserver;
import org.jxapi.netutils.deserialization.MessageDeserializer;
import org.jxapi.util.EncodingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of a {@link WebsocketEndpoint}.
 * <p>
 * This class manages the subscriptions to a websocket topic, using a
 * {@link WebsocketManager} to wrap {@link Websocket} and subscribe/unsubscribe
 * to topic, and a {@link MessageDeserializer} to deserialize incoming messages.
 * 
 * @param <M> the type of messages that this endpoint will handle.
 * @see WebsocketEndpoint
 */
public class DefaultWebsocketEndpoint<M> implements WebsocketEndpoint<M> {
  
  private final Logger log = LoggerFactory.getLogger(DefaultWebsocketEndpoint.class);
  
  /**
   * The {@link WebsocketManager} that will be used to subscribe/unsubscribe to topics
   */
  protected final WebsocketManager websocketManager;
  
  /**
   * The {@link MessageDeserializer} that will be used to deserialize incoming messages
   */
  protected final MessageDeserializer<M> messageDeserializer;
  
  /**
   * Map of subscriptions by topic name (key is the topic name)
   */
  protected final Map<String, Subscription> subscriptionsByTopic  = new HashMap<>();
  
  /**
   * Map of subscriptions by subscription id (key is the subscription id)
   */
  protected final Map<String, Subscription> subscriptionsById  = new HashMap<>();
  
  /**
   * The endpoint name
   */
  protected final String endpointName;
  
  /**
   * The {@link ExchangeApiObserver} that will be used to dispatch events
   */
  protected final ExchangeApiObserver observer;
  
  private AtomicInteger subscriptionCounter = new AtomicInteger(0);

  /**
   * Constructor
   * 
   * @param endpointName        the endpoint name
   * @param websocketManager    the {@link WebsocketManager} that will be used to
   *                            subscribe/unsubscribe to topics
   * @param messageDeserializer the {@link MessageDeserializer} that will be used
   *                            to deserialize incoming messages
   * @param observer            the {@link ExchangeApiObserver} that will be used
   *                            to dispatch events
   */
  public DefaultWebsocketEndpoint(String endpointName,  
                  WebsocketManager websocketManager, 
                  MessageDeserializer<M> messageDeserializer,
                  ExchangeApiObserver observer) {
    this.endpointName = endpointName;
    this.messageDeserializer = messageDeserializer;
    this.websocketManager = websocketManager;
    this.observer = observer;
  }

  @Override
  public synchronized String subscribe(WebsocketSubscribeRequest request, WebsocketListener<M> listener) {
    request.setEnpoint(endpointName);
    String topic = request.getTopic();
    Subscription sub = subscriptionsByTopic.computeIfAbsent(topic, t -> new Subscription(request));
    String subId = generateSubscriptionId(request);
    sub.addListener(subId, listener);
    subscriptionsById.put(subId, sub);
    if (observer != null) {
      dispatchApiEvent(ExchangeApiEvent.createWebsocketSubscribeEvent(request, subId));
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
    if (observer != null) {
      dispatchApiEvent(ExchangeApiEvent.createWebsocketUnsubscribeEvent(sub.request, unsubscriptionId));
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
   * Dispatches an {@link ExchangeApiEvent} to the {@link ExchangeApiObserver}.
   * @param event the {@link ExchangeApiEvent} to dispatch
   */
  protected void dispatchApiEvent(ExchangeApiEvent event) {
    this.observer.handleEvent(event);
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
        websocketManager.subscribe(request.getTopic(), 
                       request.getMessageTopicMatcherFactory(), 
                       this::dispatch);
      }
    }
    
    public void removeListener(String subscriptionId) {
      listeners.remove(subscriptionId);
      if (listeners.size() <= 0) {
        websocketManager.unsubscribe(request.getTopic());
      }
    }
    
    private void dispatch(String message) {
      try {
        if (!listeners.isEmpty()) {
          M msg = messageDeserializer.deserialize(message);
          listeners.values().forEach(l -> l.handleMessage(msg));
          if (observer != null) {
            dispatchApiEvent(ExchangeApiEvent.createWebsocketMessageEvent(request, message));
          }
        }
      } catch (Exception ex) {
        String errMsg = "Error while dispatching message [" + EncodingUtil.prettyPrintLongString(message) + "]"; 
        log.error(errMsg, ex);
        dispatchApiEvent(ExchangeApiEvent.createWebsocketErrorEvent(new WebsocketException(errMsg, ex)));
      }
      
    }
  }

}
