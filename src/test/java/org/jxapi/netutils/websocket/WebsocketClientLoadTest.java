package org.jxapi.netutils.websocket;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.jxapi.netutils.websocket.mock.MockWebsocket;
import org.jxapi.netutils.websocket.mock.MockWebsocketHook;
import org.jxapi.netutils.websocket.multiplexing.WSMTMFUtil;
import org.jxapi.util.DemoUtil;
import org.jxapi.util.JsonUtil;
import org.jxapi.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tests performances of {@link WebsocketClient} implementation.
 * Will instantiate one, using Mock WS implementation.
 * The {@link #runTest(int, int, int, int)} method starts a load test where X messages are
 * dispatched for 3 topics and some messages unrelated to any topic.<br>
 * Messages are dispatched from a distinct thread, or a configurable number of
 * threads.<br>
 * Main thread awaits for termination of those threads then for all messages to
 * be received.<br>
 * This is a snippet with main method to run it. The number of messages per
 * topic, iterations... can be configured using system properties, see
 * {@link #NB_MESSAGES_PER_TOPIC}, {@link #ITERATIONS}, {@link #NB_THREADS}.<br>
 * Actual slf4j logger implementation should be configured using INFO threshold.
 * Keeping debug is likely to output much verbose logs.
 */
public class WebsocketClientLoadTest {
  
  private static final Logger log = LoggerFactory.getLogger(WebsocketClientLoadTest.class);
  
  /**
   * Number of different message topics
   */
  public static final int TOPIC_COUNT = 3;
  
  /**
   * Number of messages sent for each topic, can be tuned using system property
   * <code>jxapi.wsmanager.loadTest.messagesPerTopicCount</code>
   */
  public static final int NB_MESSAGES_PER_TOPIC = PropertiesUtil.getInt(System.getProperties(), "jxapi.wsmanager.loadTest.messagesPerTopicCount", 200000);
  
  /**
   * Number of messages sent for each topic, can be tuned using system property
   * <code>jxapi.wsmanager.loadTest.iterations</code>
   */
  public static final int ITERATIONS = PropertiesUtil.getInt(System.getProperties(), "jxapi.wsmanager.loadTest.iterations", 50);
  
  /**
   * Number of threads spawned to dispatch messages, can be tuned using system
   * property <code>jxapi.wsmanager.loadTest.threadCount</code>
   */
  public static final int NB_THREADS = PropertiesUtil.getInt(System.getProperties(), "jxapi.wsmanager.loadTest.threadCount", 8);
  
  private static final AtomicInteger MESSAGE_COUNTER = new AtomicInteger();
  private static final AtomicInteger RAND_VALUE_COUNTER = new AtomicInteger();
  
  private static String createRandValue() {
    return "Hello#" + RAND_VALUE_COUNTER.getAndIncrement();
  }
  
  private final MockWebsocket ws = new MockWebsocket();
  private final MockWebsocketHook wsHook = new MockWebsocketHook();
  private final WebsocketClient wsManager = new DefaultWebsocketClient(ws, wsHook);
  private final GenericWebsocketErrorHandler errorHandler = new GenericWebsocketErrorHandler();
  
  /**
   * Constructor
   */
  public WebsocketClientLoadTest() {
    wsManager.subscribeErrorHandler(errorHandler);
  }
  
  /**
   * Runs load test
   * @param topicCount number of topics to subscribe
   * @param nbMessagesPerTopic number of messages per topic to send during test
   * @param iterations number of iterations to run
   * @param nbThreads number of threads to spawn to dispatch messages
   * @throws InterruptedException eventually thrown while waiting
   */
  public void runTest(int topicCount, 
      int nbMessagesPerTopic, 
      int iterations, 
      int nbThreads) throws InterruptedException {
    int totalMessageCount = (topicCount + 1) * nbMessagesPerTopic * iterations;
    int totalTopicRelatedMessageCount = topicCount * nbMessagesPerTopic * iterations;
    MsgHandler wsMessageHandler1 = new MsgHandler(nbMessagesPerTopic);
    MsgHandler wsMessageHandler2 = new MsgHandler(nbMessagesPerTopic);
    MsgHandler wsMessageHandler3 = new MsgHandler(nbMessagesPerTopic);
    List<MsgHandler> wsMessageHandlers = List.of(wsMessageHandler1, wsMessageHandler2, wsMessageHandler3);
    log.info("Preparing messages...");
    List<String> allMessages = prepareMessages(nbMessagesPerTopic, totalMessageCount);
    log.info("Preparing threads...");
    List<Thread> threads = prepareThreads(allMessages, nbThreads, iterations);
    log.info("Subscribing topics...");
    wsManager.subscribe("topic1", 
              WSMTMFUtil.value("f1", "val1"), 
              wsMessageHandler1);
    wsManager.subscribe("topic2", 
        WSMTMFUtil.and(List.of(WSMTMFUtil.value("f2", "val2"), WSMTMFUtil.value("f5", "val5"))), 
        wsMessageHandler2);
    wsManager.subscribe("topic3", 
        WSMTMFUtil.and(List.of(
            WSMTMFUtil.value("f1", "val1_2"), 
            WSMTMFUtil.value("f3", "val3"), 
            WSMTMFUtil.value("f6", "val6"))), 
        wsMessageHandler3);
    DemoUtil.sleep(500L);
    log.info("Starting dispatch of {} messages, with {} messages related to one of {} subsribed topics", 
         totalMessageCount, 
         totalTopicRelatedMessageCount, 
         topicCount);
    long startTime = System.currentTimeMillis();
    for (int i = 0; i < threads.size(); i++) {
      threads.get(i).start();
    }
    for (int i = 0; i < threads.size(); i++) {
      threads.get(i).join();
    }
    for (int i = 0; i < threads.size(); i++) {
      threads.get(i).join();
    }
    for (int i = 0; i < wsMessageHandlers.size(); i++) {
      Assert.assertTrue(wsMessageHandlers.get(i).latch.await(60000,TimeUnit.MILLISECONDS));
    }
    log.info("All messages received in {}", 
         DurationFormatUtils.formatDuration((System.currentTimeMillis() - startTime), 
                            "**H:mm:ss,SSS**", true));
  }
  
  private List<String> prepareMessages(int nbMessagesPerTopic, int totalMessageCount) {
    List<String> allMessages = new ArrayList<>(totalMessageCount);
    for (int i = 0; i < nbMessagesPerTopic; i++) {
      allMessages.add(createTopic1Message().toString());
      allMessages.add(createTopic2Message().toString());
      allMessages.add(createTopic3Message().toString());
      allMessages.add(createNotTopicRelatedMessage().toString());
    }
    return allMessages;
    
  }
  
  private List<Thread> prepareThreads(List<String> allMessages, int nbThreads, int iterations) {
    List<Thread> threads = new ArrayList<>(nbThreads);
    List<List<String>> messagesForThreads = new ArrayList<>(threads.size());
    for (int i = 0; i < nbThreads; i++) {
      messagesForThreads.add(new ArrayList<>());
    }
    for (int i = 0; i < allMessages.size(); i++) {
      messagesForThreads.get(i % messagesForThreads.size()).add(allMessages.get(i));
    }
    for (int i = 0; i < messagesForThreads.size(); i++) {
      List<String> l = messagesForThreads.get(i);
      threads.add(new Thread(() -> dispatchMessageList(l, iterations), "runner#" + i));
    }
    return threads;
  }
  
  private void dispatchMessageList(List<String> l, int iterations) {
    for (int k = 0; k < iterations; k++) {
      for (int j = 0; j < l.size(); j++) {
        log.debug("dispatching:[{}]", l.get(j));
        ws.dispatchMessage(l.get(j));
      }
    }
  }
  
  @After
  public void tearDown() {
    wsManager.dispose();
  }
  
  @Test
  public void testLoadTest() throws InterruptedException {
    runTest(2, 50, 10, 2);
  }

  public static void main(String[] args) {
    try {
      new WebsocketClientLoadTest().runTest(TOPIC_COUNT, NB_MESSAGES_PER_TOPIC, ITERATIONS, NB_THREADS);
      log.info("DONE");
    } catch (Throwable t) {
      log.error("Error raised", t);
      System.exit(-1);
    }
    System.exit(0);
  }
  
  /**
   * @return a random {@link LTMessage} with f1 = "val1" topic
   */
  public static LTMessage createTopic1Message() {
    return createLTMessage("val1", 
                 createRandValue(), 
                 createRandValue(), 
                 createRandValue(),
                 createRandValue(),
                 createRandValue());
  }
  
  /**
   * @return a random {@link LTMessage} with f2 = "val2", f5="val5" topic
   */
  public static LTMessage createTopic2Message() {
    return createLTMessage(createRandValue(), 
                 "val2", 
                 createRandValue(), 
                 createRandValue(),
                 "val5",
                 createRandValue());
  }
  
  /**
   * @return a random {@link LTMessage} with f1 = "val1_2", f3="val3", f6="val6" topic
   */
  public static LTMessage createTopic3Message() {
    return createLTMessage("val1_2", 
                 createRandValue(), 
                 "val3", 
                 createRandValue(),
                 createRandValue(),
                 "val6");
  }
  
  /**
   * @return a random {@link LTMessage} with f1 = "val1" topic
   */
  public static LTMessage createNotTopicRelatedMessage() {
    return createLTMessage(createRandValue(), 
                 createRandValue(), 
                 createRandValue(), 
                 createRandValue(),
                 createRandValue(),
                 createRandValue());
  }
  
  /**
   * Factory method 
   * @param f1 f1 property value
   * @param f2 f2 property value
   * @param f3 f3 property value
   * @param f4 f4 property value
   * @param f5 f5 property value
   * @param f6 f6 property value
   * @return a new {@link LTMessage} instance with given values
   */
  public static LTMessage createLTMessage(String f1,
      String f2,
      String f3,
      String f4,
      String f5,
      String f6
      ) {
    LTMessage m = new LTMessage();
    m.setF1(f1);
    m.setF2(f2);
    m.setF3(f3);
    m.setF4(f4);
    m.setF5(f5);
    m.setF6(f6);
    return m;
  }
  
  private class MsgHandler implements RawWebsocketMessageHandler {
    
    private final CountDownLatch latch;
    
    MsgHandler(int count) {
      latch = new CountDownLatch(count);
    }

    @Override
    public void handleWebsocketMessage(String message) {
      latch.countDown();
    }
    
  }
  
  /**
   * A test message to send during load tests.
   */
  public static class LTMessage {
    private String f1, f2, f3, f4, f5, f6;
    
    private int index = MESSAGE_COUNTER.getAndIncrement();
    
    public int getIndex() {
      return index;
    }

    public void setIndex(int index) {
      this.index = index;
    }

    public String getF6() {
      return f6;
    }

    public void setF6(String f6) {
      this.f6 = f6;
    }

    public String getF5() {
      return f5;
    }

    public void setF5(String f5) {
      this.f5 = f5;
    }

    public String getF4() {
      return f4;
    }

    public void setF4(String f4) {
      this.f4 = f4;
    }

    public String getF3() {
      return f3;
    }

    public void setF3(String f3) {
      this.f3 = f3;
    }

    public String getF2() {
      return f2;
    }

    public void setF2(String f2) {
      this.f2 = f2;
    }

    public String getF1() {
      return f1;
    }

    public void setF1(String f1) {
      this.f1 = f1;
    }
    
    @Override
    public String toString() {
      return JsonUtil.pojoToJsonString(this);
    }
    
    @Override
    public boolean equals(Object o) {
      if (o == null) {
        return false;
      }
      return toString().equals(o.toString());
    }
    
    @Override
    public int hashCode() {
      return toString().hashCode();
    }
  }
}
