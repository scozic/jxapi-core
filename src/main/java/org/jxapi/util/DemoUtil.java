package org.jxapi.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jxapi.exchange.ExchangeEvent;
import org.jxapi.exchange.ExchangeEventType;
import org.jxapi.netutils.rest.FutureRestResponse;
import org.jxapi.netutils.rest.RestResponse;

/**
 * Helper methods used by generated demo snippets.
 */
public class DemoUtil {
  
  private DemoUtil() {}
  
  private static final Logger log = LoggerFactory.getLogger(DemoUtil.class);

  /**
   * Awaits response to a REST API call and ogs successful response at INFO level or throws an exception.
   * @param futureResponse response to check
   * @param <T> REST call response data type
   * @throws NullPointerException if <code>futureResponse</code> is <code>null</code>
   * @throws InterruptedException eventually thrown  by {@link CompletableFuture#get()}
   * @throws ExecutionException eventually thrown  by {@link CompletableFuture#get()} or if received response is not OK see {@link RestResponse#isOk()}
   * @return response to REST API call. This response is 'OK' otherwise an exception would be thrown.
   */
  public static <T> RestResponse<T> checkResponse(FutureRestResponse<T> futureResponse) throws InterruptedException, ExecutionException {
    if (futureResponse == null) {
      throw new NullPointerException("null response");
    }
    RestResponse<T> response = futureResponse.get();
    if (!response.isOk()) {
      throw new ExecutionException("Error in response:" + response, response.getException());
    }
    if (log.isInfoEnabled()) {
      log.info("Got OK response:\n{}", JsonUtil.pojoToPrettyPrintJson(response.getResponse()));
    }
    return response;
  }
  
  /**
   * Logs content of message data with INFO level.
   * @param message Message to format as string
   * @return the <code>message</code>
   */
  public static Object logWsMessage(Object message) {
    if (log.isInfoEnabled()) {
      log.info("Received message:\n{}", JsonUtil.pojoToPrettyPrintJson(message));
    }
    return message;
  }
  
  /**
   * Logs incoming websocket related event with level:
   * <ul> 
   * <li>DEBUG if event type is {@link ExchangeEventType#WEBSOCKET_MESSAGE},
   * <li>INFO if event type is {@link ExchangeEventType#WEBSOCKET_SUBSCRIBE}, or {@link ExchangeEventType#WEBSOCKET_UNSUBSCRIBE},
   * <li>ERROR if event type is {@link ExchangeEventType#WEBSOCKET_ERROR}.
   * </ul>
   * Nothing is logged for other event types.
   * 
   * @param event Event to log
   */
  public static void logWsApiEvent(ExchangeEvent event) {
    switch (event.getType()) {
    case WEBSOCKET_ERROR:
      if (log.isErrorEnabled())
        log.error(String.format("Error raised from websocket:%s", event), event.getWebsocketError());
      break;
    case WEBSOCKET_MESSAGE:
      log.debug("Websocket message received:{}", event);
      break;
    case WEBSOCKET_SUBSCRIBE:
      log.info("Subscription event received: {}", event);
      break;
    case WEBSOCKET_UNSUBSCRIBE:
      log.info("Unsubscription event received: {}", event);
      break;
    default:
      break;
    }
  }
  
  /**
   * Logs incoming REST API relate event with DEBUG level. Nothing is logged for
   * other event types other than {@link ExchangeEventType#HTTP_REQUEST} and
   * {@link ExchangeEventType#HTTP_RESPONSE}.
   * 
   * @param event Event to log
   */
  public static void logRestApiEvent(ExchangeEvent event) {
    if (event.getType() == ExchangeEventType.HTTP_REQUEST 
        || event.getType() == ExchangeEventType.HTTP_RESPONSE) {
      log.debug("{}", event);
    }
  }

  /**
   * Returns expected demo exchange properties file name, for instance 'demo-&lt;exchangeId&gt;.properties'
   * This file is expected to be with same name as generated template file but without '.dist' suffix.
   * @param exchangeId exchange ID
   * @return default demo exchange properties file name
   */
  public static String getDefaultDemoExchangePropertiesFileName(String exchangeId) {
    return new StringBuilder()
        .append("demo-")
        .append(exchangeId)
        .append(".properties")
        .toString();
  }
  
  /**
   * Loads demo exchange properties from file with name
   * 'demo-&lt;exchangeId&gt;.properties' or from file specified by system property
   * 'demo.api.properties'.
   * 
   * @param exchangeId exchange ID
   * @return loaded properties
   */
  public static Properties loadDemoExchangeProperties(String exchangeId) {
    Properties props = new Properties();
    File propsFile = null;
    String propsFileName = System.getProperty(DemoProperties.DEMO_API_PROPERTIES_FILE_SYSTEM_PROPERTY);
    if (propsFileName != null) {
      propsFile = new File(propsFileName);
    } else {
      URL url = DemoUtil.class.getClassLoader().getResource(getDefaultDemoExchangePropertiesFileName(exchangeId));
      if (url != null) {
        propsFile = new File(url.getFile());
      }
    }
    try {
      if (propsFile != null && propsFile.exists()) { 
        try (InputStream in = new BufferedInputStream(new FileInputStream(propsFile))) {
          props.load(in);
        }
      }
    } catch (Exception ex) {
      throw new IllegalArgumentException(String.format("Failed to load %s properties file", propsFileName), ex);
    }
    return props;
  }
  
  /**
   * Makes current thread sleep for given number of milliseconds.
   * This method is a wrapper around {@link Thread#sleep(long)}, which Sonar does not recommend to call directly.
   * It will be used in generated demo snippets to make thread sleep, so these snippets do not raise such Sonar issues. 
   * 
   * @param millis number of milliseconds to sleep
   * @throws InterruptedException eventually thrown by {@link Thread#sleep(long)}
   * @see Thread#sleep(long)
   */
  public static void sleep(long millis) throws InterruptedException {
    Thread.sleep(millis);
  }
}
