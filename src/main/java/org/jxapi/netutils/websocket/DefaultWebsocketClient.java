package org.jxapi.netutils.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import org.jxapi.netutils.websocket.multiplexing.WebsocketMessageTopicMatchStatus;
import org.jxapi.netutils.websocket.multiplexing.WebsocketMessageTopicMatcher;
import org.jxapi.netutils.websocket.multiplexing.WebsocketMessageTopicMatcherFactory;
import org.jxapi.util.DefaultDisposable;
import org.jxapi.util.EncodingUtil;
import org.jxapi.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

/**
 * Default implementation of {@link WebsocketClient}.
 * 
 * <p>
 * Uses object pooling to reduce creation of objects in incoming message
 * matching against topics.
 * <p>
 * Some synchronization is performed over instance monitor, to protect agains
 * thread race between 'writer' threads modifying topics subscription list and
 * socket dispatcher threads iterating over that list.
 */
public class DefaultWebsocketClient extends DefaultDisposable implements WebsocketClient {
  
  /**
   * Default keep alive time for write executor.
   */
  public static final long WRITE_EXECUTOR_KEEP_ALIVE = 30000L;
  
  private static final Logger log = LoggerFactory.getLogger(DefaultWebsocketClient.class);
  
  /**
   * A topic manager for a topic, with its message handler and message matcher.
   */
  protected final Map<String, TopicManager> topics = new HashMap<>();
  
  /**
   * A list of system message handlers, that are not topic based.
   */
  protected final List<TopicManager> systemMessageHandlers = new ArrayList<>();
  
  /**
   * Flag to track connection status.
     */
  protected final AtomicBoolean connected = new AtomicBoolean(false);
  
  /**
   * The websocket implementation used by this manager.
   */
  protected final Websocket websocket;
  
  /**
   * The hook to provide additional websocket handling.
   */
  protected final WebsocketHook websocketHook;
  
  /**
   * The executor to schedule write operations.
   */
  protected ScheduledExecutorService writeExecutor = null;
  
  /**
   * The last time a message was received.
   */
  protected AtomicLong lastHeartBeatTime = new AtomicLong(0L);
  
  private final List<WebsocketErrorHandler> errorHandlers = new ArrayList<>();
  private final AtomicLong messageReceivedCount = new AtomicLong(0);
  private final RawWebsocketMessageHandler rawMessageHandler = this::dispatchMessage;
  private final WebsocketErrorHandler websocketErrorHandler = this::notifyError;
  private final Object waitReconnectDelayMonitor = new Object();
  private final List<List<TopicMatcher>> topicMatcherListPool = new ArrayList<>();
  
  private long reconnectDelay = -1L;
  private long noMessageTimeout = -1L;
  private NoMessageTimeoutTask noMessageTimeoutTask = null;
  private long heartBeatInterval = -1L;
  private long  noHeartBeatResponseTimeout = -1L;
  private AtomicBoolean heartBeatTaskCancelled = null;
  private AtomicBoolean heartBeatTimeoutTaskCancelled = null;
  private long lastConnectTime = 0L;
  
  /**
   * Constructor
   * 
   * @param websocket     the websocket implementation used by this manager
   * @param websocketHook the hook to provide additional websocket handling
   */
  public DefaultWebsocketClient(Websocket websocket, WebsocketHook websocketHook) {
    this.websocket = websocket;
    this.websocketHook = websocketHook;
    this.writeExecutor = Executors.newSingleThreadScheduledExecutor();
    this.websocket.addErrorHandler(websocketErrorHandler);
    this.websocket.addMessageHandler(rawMessageHandler);
    if (websocketHook != null) {
      websocketHook.init(this);
    }
  }
  
  @Override
  public long getHeartBeatInterval() {
    return heartBeatInterval;
  }

  @Override
  public void setHeartBeatInterval(long heartBeatInterval) {
    if (heartBeatInterval > 0 && websocketHook == null) {
      throw new IllegalStateException("Cannot set heartbeat interval to " + heartBeatInterval + " > 0, because no websocket hook is set to provide heartbeat message to send");
    }
    this.heartBeatInterval = heartBeatInterval;
  }

  @Override
  public long getNoHeartBeatResponseTimeout() {
    return noHeartBeatResponseTimeout;
  }

  @Override
  public void setNoHeartBeatResponseTimeout(long noHeartBeatResponseTimeout) {
    this.noHeartBeatResponseTimeout = noHeartBeatResponseTimeout;
  }
  
  /**
   * @return the websocket hook
   */
  public WebsocketHook getWebsocketHook() {
    return websocketHook;
  }

  @Override
  public void subscribe(String topic, 
              WebsocketMessageTopicMatcherFactory matcherFactory,
              RawWebsocketMessageHandler messageHandler) {
    checkNotDisposed();
    log.debug("Scheduling subscribe request for topic:{}", topic);
    writeExecutor.execute(() -> {
      try {
        String top = Optional.ofNullable(topic).orElse("");
        log.debug("Executing subscribe request for topic:[{}]", top);
        getOrCreateTopicManager(topic, matcherFactory, messageHandler);

        // Remark: registered TopicManager before checking connection status and connecting if not already connected.
        // This is because the url provided for handshake may stand for a global stream endpoint (no topic/subscription)
        // and message would be disseminated right after handshake. Message handler must be registered before.
        if (!isConnected()) {
          log.debug("Executing subscribe request for topic:[{}]: not connected, connecting", top);
          connect();  
        }
        sendToTopicSubscription(topic);
        log.debug("DONE Executing subscribe request for topic:[{}]", top);
      } catch (Exception ex) {
        notifyError(new WebsocketException("Error while subscribing to websocket for topic [" + topic + "]", ex));
      }
    });
  }
  
  private synchronized TopicManager getOrCreateTopicManager(String topic, 
        WebsocketMessageTopicMatcherFactory matcherFactory,
        RawWebsocketMessageHandler messageHandler) {
    String top = Optional.ofNullable(topic).orElse("");
    TopicManager t = topics.get(top);
    if (t == null) {
      t = new TopicManager(top, matcherFactory, messageHandler, false);
      topics.put(top, t);
    } else {
      throw new IllegalArgumentException("Already have a subscription for this topic");
    }
    return t;
  }
  
  private void sendToTopicSubscription(String topic) {
    try {
      String subscribeRequestMessage = websocketHook == null? null: 
                        websocketHook.getSubscribeRequestMessage(topic);
      if (subscribeRequestMessage != null) {
        log.debug("Sending topic subscribe request:{}", topic);
        websocket.send(subscribeRequestMessage);
      }
    } catch (WebsocketException e) {
      onError(e);
    }
  }
  
  private void unbscribeFromTopic(String topic) throws WebsocketException {
    String unsubscribeRequestMessage = websocketHook == null? null: 
                      websocketHook.getUnSubscribeRequestMessage(topic);
    if (unsubscribeRequestMessage != null) {
      log.debug("Sending topic unsubscribe request:{}", topic);
      websocket.send(unsubscribeRequestMessage);
    }
  }
  
  private void scheduleHeartBeatTask(HeartBeakTask heartBeakTask) {
    log.debug("Scheduling heartbeat task in {}ms", heartBeatInterval);
    this.writeExecutor.schedule(heartBeakTask, heartBeatInterval, TimeUnit.MILLISECONDS);
  }
  
  private void scheduleHeartBeatTimeoutTask(HeartBeakTimeoutTask heartBeakTimeoutTask) {
    log.debug("Scheduling heartbeat timeout task in {} ms:{}", 
          noHeartBeatResponseTimeout, 
          heartBeatTimeoutTaskCancelled.get());
    this.writeExecutor.schedule(heartBeakTimeoutTask, noHeartBeatResponseTimeout, TimeUnit.MILLISECONDS);
  }
  
  @Override
  public void unsubscribe(String topic) {
    writeExecutor.execute(() -> {
      try {
        TopicManager t = removeTopic(topic);
        if (t != null) {
          unbscribeFromTopic(topic);
        }
      } catch (Exception ex) {
        dispatchWebsocketError(new WebsocketException("Error while unsubscribing from websocket topic [" + topic + "]", ex));
      }
    });
  }
  
  private synchronized TopicManager removeTopic(String topic) {
    return topics.remove(topic);
  }
  
  /**
   * Connects to the websocket.
   * @throws WebsocketException if an error occurs while connecting
   */
  protected final void connect() throws WebsocketException {
    checkNotDisposed();
    if (connected.getAndSet(true)) {
      // Already connected
      return;
    }
    log.info("Connecting WS:{}", this);
    lastConnectTime = System.currentTimeMillis();
    try {
      if (websocketHook != null) {
        websocketHook.beforeConnect();
      }
      websocket.connect();
      if (websocketHook != null) {
        websocketHook.afterConnect();
      }
      scheduleNoMessageTimeoutTask(this.messageReceivedCount.get());
      if (heartBeatInterval > 0 || noHeartBeatResponseTimeout > 0) {
        initHearBeats();
      }
      
      log.info("Connected WS:{}", this);
    } catch (Exception exception) {
      // Remark: Intentionally catch all exceptions to avoid any RuntimeException to escape and break the connection.
      String msg = "Error while connecting websocket";
      log.error(msg, exception);
      connected.set(false);
      throw new WebsocketException(msg, exception);
    }
  }
  
  private void initHearBeats() {
    lastHeartBeatTime.set(System.currentTimeMillis());
    if (noHeartBeatResponseTimeout > 0) {
      if (this.heartBeatTimeoutTaskCancelled != null) {
        this.heartBeatTimeoutTaskCancelled.set(true);
      }
      this.heartBeatTimeoutTaskCancelled = new AtomicBoolean(false);
      scheduleHeartBeatTimeoutTask(new HeartBeakTimeoutTask(this.heartBeatTimeoutTaskCancelled));
    }
    if (heartBeatInterval > 0) {
      if (this.heartBeatTaskCancelled != null) {
        this.heartBeatTaskCancelled.set(true);
      }
      this.heartBeatTaskCancelled = new AtomicBoolean(false);
      scheduleHeartBeatTask(new HeartBeakTask(this.heartBeatTaskCancelled));
    }
  }

  /**
   * Disconnects from the websocket.
   */
  protected final void disconnect() {
    if (!isConnected()) {
      return;
    }
    try {
      log.info("Disconnecting {}", this);
      if (this.heartBeatTaskCancelled != null) {
        this.heartBeatTaskCancelled.set(true);
      }
      if (this.noMessageTimeoutTask != null) {
        this.noMessageTimeoutTask.cancelled.set(true);
      }
      if (this.heartBeatTimeoutTaskCancelled != null) {
        this.heartBeatTimeoutTaskCancelled.set(true);
      }
      beforeDisconnectWebsocketHook();
       websocket.disconnect();
       if (websocketHook != null) {
        websocketHook.afterDisconnect();
      }
      log.info("Disconnected {}", this);
    } catch (Exception e) {
      String errMsg = "Error while disconnecting websocket";
      log.error(errMsg, e);
      dispatchWebsocketError(new WebsocketException(errMsg, e));
    }
    connected.set(false);
  }
  
  private void beforeDisconnectWebsocketHook() {
    if (websocketHook != null) {
      try {
        websocketHook.beforeDisconnect();
      } catch (Exception e) {
        String errMsg = "Error while calling WebsocketHook#beforeDisconnect";
        log.error(errMsg, e);
        dispatchWebsocketError(new WebsocketException(errMsg, e));
      }
      
    }
  }
   
  @Override
  protected void doDispose() {
    this.websocket.removeMessageHandler(rawMessageHandler);
    synchronized(waitReconnectDelayMonitor) {
      waitReconnectDelayMonitor.notifyAll();
    }
    writeExecutor.execute(() -> {
      disconnect();
      this.websocket.removeErrorHandler(websocketErrorHandler);
    });
    
    writeExecutor.shutdown();
    writeExecutor = null;
  }
  
  /**
   * @return <code>true</code> if the websocket is currently connected, <code>false</code> otherwise.
   */
  public boolean isConnected() {
    return this.connected.get();
  }

  @Override
  public void subscribeErrorHandler(WebsocketErrorHandler websocketErrorHandler) {
    errorHandlers.add(websocketErrorHandler);
  }

  @Override
  public boolean unsubscribeErrorHandler(WebsocketErrorHandler websocketErrorHandler) {
    return errorHandlers.remove(websocketErrorHandler);
  }
  
  @Override
  public synchronized void addSystemMessageHandler(String topic, WebsocketMessageTopicMatcherFactory matcher, RawWebsocketMessageHandler messageHandler) {
    this.systemMessageHandlers.add(new TopicManager(Optional.ofNullable(topic).orElse(""), matcher, messageHandler, true));
  }
  
  public long getReconnectDelay() {
    return reconnectDelay;
  }

  public void setReconnectDelay(long reconnectDelay) {
    this.reconnectDelay = reconnectDelay;
  }
  
  public long getNoMessageTimeout() {
    return noMessageTimeout;
  }

  public void setNoMessageTimeout(long noMessageTimeout) {
    this.noMessageTimeout = noMessageTimeout;
  }

  /**
   * @return the {@link Websocket} used by this manager
   */
  public Websocket getWebsocket() {
    return websocket;
  }
  
  @Override
  public void send(String msg) throws WebsocketException {
    if (!isConnected()) {
      connect();
    }
    websocket.send(msg);
  }

  @Override
  public void hearbeatReceived() {
    log.debug("{}:Received heartbeat response", this);
    this.lastHeartBeatTime.set(System.currentTimeMillis());
  }
  
  public String getUrl() {
    return websocket.getUrl();
  }
  
  public void setUrl(String url) {
    websocket.setUrl(url);
  }

  @Override
  public CompletableFuture<WebsocketException> sendAsync(String msg) {
    CompletableFuture<WebsocketException> res = new CompletableFuture<>();
    if (isDisposed()) {
      res.complete(new WebsocketException(toString() + " is disposed"));
    } else {
      this.writeExecutor.schedule(() -> {
        try {
          send(msg);
          res.complete(null);
        } catch (WebsocketException ex) {
          res.complete(ex);
          onError("Error while sending message:" + msg, ex);
        }
      }, 0L, TimeUnit.MILLISECONDS);
    }
    return res;
  }

  /**
   * Dispatches an error to the error handlers.
   * @param error the error to dispatch
   */
  protected void dispatchWebsocketError(WebsocketException error) {
    errorHandlers.forEach(h -> h.handleWebsocketError(error));
  }
  
  /**
   * Dispatches a message to the message handlers.
   * 
   * @param message the message to dispatch
   */
  protected void dispatchMessage(String message) {
    this.dispatchSingleMessage(message);
  }
  
  private void dispatchSingleMessage(String message) {
    messageReceivedCount.incrementAndGet();
    List<TopicMatcher> allTopics = getTopicMatcherList();
    try (JsonParser jsonParser = JsonUtil.DEFAULT_JSON_FACTORY.createParser(message.getBytes())) {
      for (JsonToken tok = jsonParser.nextToken(); tok != null && !allTopics.isEmpty(); tok = jsonParser.nextToken()) {
        if (tok == JsonToken.FIELD_NAME) {
          String fieldName = jsonParser.currentName();
          String value = null;
          switch (jsonParser.nextToken()) {
          case START_OBJECT:
            // Continue searching matching fields in nested structure.
            continue;
          case VALUE_FALSE:
            value = Boolean.FALSE.toString();
            break;
          case VALUE_TRUE:
            value = Boolean.TRUE.toString();
            break;
          default:
            value = jsonParser.getText();
            break;
          }
          
          if (dispatchToMessageTopicMatchers(fieldName, value, allTopics, message)) {
            break;
          }
        }
      }
       
    } catch (IOException e) {
      log.error("Error parsing websocket message [" + message + "]", e);
    } finally {
      allTopics.forEach(m -> m.matcher.reset());
      releaseTopicMatcherList(allTopics);
    }
  }
  
  private boolean dispatchToMessageTopicMatchers(
            String name, 
            String value, 
            List<TopicMatcher> topics,
            String rawMessage) {
    for (Iterator<TopicMatcher> it = topics.iterator(); it.hasNext();) {
      TopicMatcher topic = it.next();
      WebsocketMessageTopicMatchStatus matchResult = topic.matcher.matches(name, value);
      switch (matchResult) {
      case MATCHED:
        log.debug("Dispatching message to handler for topic:[{}]  :[{}]", topic.manager.topic, rawMessage);
        topic.manager.messageHandler.handleWebsocketMessage(rawMessage);
        it.remove();
        if (topic.manager.systemMessage) {
// System message handler matched that message. No need to keep processing to find other handlers.
          return true;
        }
        break;
      case CANT_MATCH:
        // Remark: Could do it.remove() here, but benchmark shows removing item in
        // middle of array list could underperform having to keep item that is in final
        // state for nothing in list
        break;
      default: // no match
        break;
      }
    }
    return false;
  }
  
  private synchronized List<TopicMatcher> getTopicMatcherList() {
    List<TopicMatcher> topicMatchers = null;
    if (topicMatcherListPool.isEmpty()) {
      topicMatchers = new ArrayList<>();
    } else {
      topicMatchers = topicMatcherListPool.remove(topicMatcherListPool.size() - 1);
    }
    for (TopicManager m : systemMessageHandlers) {
      topicMatchers.add(m.getTopicMatcher());
    }
    for (TopicManager m : topics.values()) {
      topicMatchers.add(m.getTopicMatcher());
    }
    return topicMatchers;
  }
  
  private synchronized void releaseTopicMatcherList(List<TopicMatcher> topicMatchers) {
    topicMatchers.forEach(m -> m.manager.releaseTopicMatcher(m));
    topicMatchers.clear();
    topicMatcherListPool.add(topicMatchers);
  }
  
  /**
   * To be called from {@link #writeExecutor} thread when an error occurred. Will
   * disconnect websocket, and try reconnect it after reconnect delay.
   * 
   * @param msg error message to log
   * @param t  the exception that caused the error
   * @see #onError(WebsocketException)
   */
  protected void onError(String msg, Throwable t) {
    onError(new WebsocketException(msg, t));
  }
  
  /**
   * To be called from {@link #writeExecutor} thread when an error occurred.
   * Will disconnect websocket, and try reconnect it after reconnect delay.
   * @param exception the exception that caused the error
   */
  protected void onError(WebsocketException exception) {
    log.error("Error raised on Websocket [{}]", this, exception);
    this.dispatchWebsocketError(exception);
    if (!isDisposed()) {
      if (reconnectDelay > 0) {
        disconnect();
        if (waitReconnectDelay()) {
          try {
            connect();
            resubscribeTopics();
          } catch (WebsocketException e) {
            // Avoid reentrant call, retry in a distinct runnable submitted to executor
            notifyError(e);
          }
        } else {
          log.info("Websocket {} has been disposed or interrupted while waiting delay before reconnect", this);
        }
      } else {
        log.warn("No reconnect delay set for websocket [{}], now disconnected", this);
      }
    }
  }
  
  /**
   * Waits for reconnect delay to have elapsed, or this manager has been disposed
   * or executing thread has been interrupted while waiting.
   * 
   * @return <code>true</code> if delay has elapsed without being disposed or
   *         interrupted, and the reconnection can be performed.
   */
  private boolean waitReconnectDelay() {
    synchronized(waitReconnectDelayMonitor) {
      long nextTimeForReconnect = lastConnectTime + reconnectDelay;
      if (log.isDebugEnabled()) {
      log.debug("Last connection attempt:{} Waiting {}ms until {}, for reconnect delay {}ms to be elapsed for websocket [{}]", 
        EncodingUtil.formatTimestamp(lastConnectTime), 
        nextTimeForReconnect - System.currentTimeMillis(),
        EncodingUtil.formatTimestamp(nextTimeForReconnect),
        reconnectDelay,
        this);
      }
      try {
        for (long now = System.currentTimeMillis(); 
            !isDisposed() && now < nextTimeForReconnect; 
            now = System.currentTimeMillis()) {
          long delay = nextTimeForReconnect - now;
          log.info("Will try to reconnect websocket [{}] in {}ms", this, delay);
          waitReconnectDelayMonitor.wait(delay);
        }
        return !isDisposed();
      } catch (InterruptedException ex) {
        String errorMsg = String.format("Interrupted while sleeping till reconnect delay has elapsed for websocket [%s]", this);
        log.error(errorMsg, ex);
        dispose();
        Thread.currentThread().interrupt();
        dispatchWebsocketError(new WebsocketException(errorMsg, ex));
        return false;
      }
    }
  }
  
  @Override
  public void notifyError(WebsocketException exception) {
    writeExecutor.execute(() -> onError(exception));
  }
  
  private void resubscribeTopics() {
    log.info("Resubscribing {} topics after successful reconnection", topics.size());
    for (String topic : topics.keySet()) {
      sendToTopicSubscription(topic);
    }
    if (log.isInfoEnabled())
      log.info("Successfully resubscribed to {} topics after successful reconnection", topics.size());
  }

  /**
   * A topic manager for a topic, with its message handler and message matcher.
   * Uses object pooling of {@link TopicMatcher} to reduce creation of objects in incoming message.
   */
  protected class TopicManager {
    final WebsocketMessageTopicMatcherFactory matcherFactory;
    final RawWebsocketMessageHandler messageHandler;
    final String topic;
    final boolean systemMessage;
    private final List<TopicMatcher> matcherPool = new ArrayList<>();

    /**
     * Constructor
     * @param topic the topic to manage
     * @param matcherFactory the factory to create message matchers
     * @param messageHandler the message
     * @param systemMessage whether this is a system message topic
     */
    public TopicManager(String topic, 
              WebsocketMessageTopicMatcherFactory matcherFactory,
              RawWebsocketMessageHandler messageHandler, 
              boolean systemMessage) {
      this.topic = topic;
      this.matcherFactory = matcherFactory;
      this.messageHandler = messageHandler;
      this.systemMessage = systemMessage;
    }
    
    /**
     * Gets a matcher from the pool, or creates a new one if the pool is empty.
     * 
     * @return a matcher to use for matching a message to this topic
     */
    public TopicMatcher getTopicMatcher() {
      if (matcherPool.isEmpty()) {
        return new TopicMatcher(this, matcherFactory.createWebsocketMessageTopicMatcher());
      } else {
        return matcherPool.remove(matcherPool.size() - 1);
      }
    }
    
    /**
     * Releases a matcher to the pool. to be called from inside a monitor. 
     * @param matcher matcher to release, its message matcher must have been reset before.
     */
    public void releaseTopicMatcher(TopicMatcher matcher) {
      matcherPool.add(matcher);
    }
  }
  
  private void scheduleNoMessageTimeoutTask(long lastReceivedMessageCount) {
    if (this.noMessageTimeout > 0) {
      if (this.noMessageTimeoutTask != null) {
        this.noMessageTimeoutTask.cancelled.set(true);
      }
      this.noMessageTimeoutTask = new NoMessageTimeoutTask(lastReceivedMessageCount);
      log.debug("Will check in {}ms if received message count has increased, current:{}", this.noMessageTimeout, lastReceivedMessageCount);
      writeExecutor.schedule(noMessageTimeoutTask, this.noMessageTimeout, TimeUnit.MILLISECONDS);
    }
  }

  private class NoMessageTimeoutTask implements Runnable {
    
    final AtomicBoolean cancelled = new AtomicBoolean(false);
    
    final long lastReceivedMessageCount;
    public NoMessageTimeoutTask(long lastReceivedMessageCount) {
      this.lastReceivedMessageCount = lastReceivedMessageCount;
    }

    @Override
    public void run() {
      if (isDisposed() || cancelled.get() || !isConnected()) {
        log.debug("Not executing {}", this);
        return;
      }
      long newLastReceivedMessageCount = messageReceivedCount.get();
      try {
        log.debug("Checking message count has increased, lastReceivedMessageCount:{}, current:{}", lastReceivedMessageCount, newLastReceivedMessageCount);
        if (newLastReceivedMessageCount <= this.lastReceivedMessageCount) {
          onError(new WebsocketException("No message received last " 
                          + noMessageTimeout 
                          + "ms, on websocket" 
                          + DefaultWebsocketClient.this 
                          + " reconnecting websocket"));
        }
      } catch (Exception ex) {
        // Normally no reachable, but better log exception here or it will missed.
        log.error("Error while running NoMessageTimeout task", ex);
      }
      scheduleNoMessageTimeoutTask(newLastReceivedMessageCount);
    }
    
  }
  
  private class HeartBeakTask implements Runnable {
    
    private final AtomicBoolean cancelled;

    public HeartBeakTask(AtomicBoolean cancelled) {
      this.cancelled = cancelled;
    }

    @Override
    public void run() {
      try {
        if (cancelled.get()) {
          log.debug("Not running cancelled heartbeat task");
          return;
        }
        
        String hearBeatMessage = websocketHook.getHeartBeatMessage();
        if (hearBeatMessage == null) {
          String msg = "null heartbeat message provided by websocket hook" 
              + websocketHook 
              + " while heartbeat interval is set to " 
              + heartBeatInterval;
          dispatchWebsocketError(new WebsocketException(msg));
        } else {
          websocket.send(hearBeatMessage);
        }
        if (log.isDebugEnabled()) {
          log.debug("Sending heartbeat:{}", hearBeatMessage);
        }
        
        scheduleHeartBeatTask(this);
      } catch (Exception ex) {        
        onError(new WebsocketException("Error while sending heartbeat", ex));
      }
      
    }
  }
  
  private class HeartBeakTimeoutTask implements Runnable {
    
    private final AtomicBoolean cancelled;
    
    HeartBeakTimeoutTask(AtomicBoolean cancelled) {
      this.cancelled = cancelled;
    }
 
    @Override
    public void run() {
      try {
        if (cancelled.get()) {
          log.debug("Not running cancelled heartbeat task");
          return;
        }
        
        // Raise error if max delay without heartbeat response timeout has elapsed
        long timeElapsedSinceLastHeartBeat = System.currentTimeMillis() - lastHeartBeatTime.get();
        if (timeElapsedSinceLastHeartBeat > noHeartBeatResponseTimeout) {
          onError("No heartbeat response since " 
                      + timeElapsedSinceLastHeartBeat 
                      + "ms, timeout:" + noHeartBeatResponseTimeout 
                      + " reconnect delay:" + getReconnectDelay(), null);
        } else {
          scheduleHeartBeatTimeoutTask(this);
        }
      } catch (Exception ex) {
        onError("Error while running heartbeat timeout task", ex);
      }
      
    }    
  }
  
  private class TopicMatcher {
    final TopicManager manager;
    final WebsocketMessageTopicMatcher matcher;
    
    TopicMatcher(TopicManager topicManager, WebsocketMessageTopicMatcher topicMatcher) {
      this.manager = topicManager;
      this.matcher = topicMatcher;
    }
  }
}
