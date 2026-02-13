package org.jxapi.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.junit.Assert;
import org.junit.Test;

import org.jxapi.exchanges.demo.gen.DemoExchangeExchange;
import org.jxapi.generator.java.JavaCodeGenUtil;
import org.jxapi.generator.java.exchange.ClassesGeneratorTestUtil;
import org.jxapi.netutils.rest.FutureRestResponse;
import org.jxapi.netutils.rest.HttpResponse;
import org.jxapi.netutils.rest.RestResponse;

/**
 * Unit test for {@link DemoUtil}
 */
public class DemoUtilTest {

    @Test(expected = NullPointerException.class)
    public void testCheckResponseNullResponse() throws InterruptedException, ExecutionException {
        DemoUtil.checkResponse(null);
    }

    @Test(expected = ExecutionException.class)
    public void testCheckResponseNotOk() throws InterruptedException, ExecutionException {
        FutureRestResponse<?> futureResponse = new FutureRestResponse<>();
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setResponseCode(404);
        futureResponse.complete(new RestResponse<>(httpResponse));
        DemoUtil.checkResponse(futureResponse);
    }

    @Test(expected = ExecutionException.class)
    public void testCheckResponseException() throws InterruptedException, ExecutionException {
        FutureRestResponse<?> futureResponse = new FutureRestResponse<>();
        futureResponse.completeExceptionally(new Exception("Test execution error"));
        DemoUtil.checkResponse(futureResponse);
    }

    @Test
    public void testCheckResponseOk() throws InterruptedException, ExecutionException {
        FutureRestResponse<String> futureResponse = new FutureRestResponse<>();
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setResponseCode(200);
        RestResponse<String> restResponse = new RestResponse<>(httpResponse);
        restResponse.setResponse("hello");
        futureResponse.complete(restResponse);
        Assert.assertEquals(restResponse, DemoUtil.checkResponse(futureResponse));
    }
    
    @Test
    public void testLogWsMessage() {
      Assert.assertNotNull(DemoUtil.logWsMessage("Hello World!"));
    }
    
    @Test
  public void testLoadDemoExchangeProperties_DemoExchange() {
    Properties p = DemoUtil.loadDemoExchangeProperties(DemoExchangeExchange.ID);
    Assert.assertNotNull(p);
    Assert.assertEquals(100L, DemoProperties.getWebsocketDelayBeforeExit(p));
    Assert.assertEquals(300L, DemoProperties.getWebsocketSubscriptionDuration(p));
  }
    
    @Test
  public void testLoadDemoExchangeProperties_MissingDefaultExchangePropertiesFile() {
    Properties p = DemoUtil.loadDemoExchangeProperties("foo");
    Assert.assertNotNull(p);
    Assert.assertEquals(DemoProperties.DEMO_WS_DELAY_BEFORE_EXIT_AFTER_UNSUBSCRIPTION_PROPERTY.getDefaultValue(), 
              DemoProperties.getWebsocketDelayBeforeExit(p));
    Assert.assertEquals(DemoProperties.DEMO_WS_SUBSCRIPTION_DURATION_PROPERTY.getDefaultValue(), 
              DemoProperties.getWebsocketSubscriptionDuration(p));
  }
    
    @Test
  public void testLoadDemoExchangeProperties_FromSystemProperty() throws IOException {
      Path tmpFolder = ClassesGeneratorTestUtil.generateTmpDir();
      Path propFile = tmpFolder.resolve("demo.properties");
      Properties p = new Properties();
      p.setProperty(DemoProperties.DEMO_WS_DELAY_BEFORE_EXIT_AFTER_UNSUBSCRIPTION_PROPERTY.getName(), "200");
      p.setProperty(DemoProperties.DEMO_WS_SUBSCRIPTION_DURATION_PROPERTY.getName(), "400");
      p.store(Files.newBufferedWriter(propFile), null);
      String previousSysPropValue = System.getProperty(DemoProperties.DEMO_API_PROPERTIES_FILE_SYSTEM_PROPERTY);
      System.setProperty(DemoProperties.DEMO_API_PROPERTIES_FILE_SYSTEM_PROPERTY, propFile.toString());
      try {
        p = DemoUtil.loadDemoExchangeProperties("foo");
        Assert.assertNotNull(p);
        Assert.assertEquals(200L, DemoProperties.getWebsocketDelayBeforeExit(p));
        Assert.assertEquals(400L, DemoProperties.getWebsocketSubscriptionDuration(p));
      } finally {
        if (previousSysPropValue == null) {
          System.clearProperty(DemoProperties.DEMO_API_PROPERTIES_FILE_SYSTEM_PROPERTY);
        } else {
          System.setProperty(DemoProperties.DEMO_API_PROPERTIES_FILE_SYSTEM_PROPERTY, previousSysPropValue);
        }
        JavaCodeGenUtil.deletePath(tmpFolder);
      }
  }
    
    @Test
  public void testLoadDemoExchangeProperties_FromSystemProperty_MissingFile() {
      String previousSysPropValue = System.getProperty(DemoProperties.DEMO_API_PROPERTIES_FILE_SYSTEM_PROPERTY);
      System.setProperty(DemoProperties.DEMO_API_PROPERTIES_FILE_SYSTEM_PROPERTY, "bar.properties");
      try {
        Properties p = DemoUtil.loadDemoExchangeProperties("foo");
        Assert.assertNotNull(p);
        Assert.assertEquals(DemoProperties.DEMO_WS_DELAY_BEFORE_EXIT_AFTER_UNSUBSCRIPTION_PROPERTY.getDefaultValue(), 
                  DemoProperties.getWebsocketDelayBeforeExit(p));
        Assert.assertEquals(DemoProperties.DEMO_WS_SUBSCRIPTION_DURATION_PROPERTY.getDefaultValue(), 
                  DemoProperties.getWebsocketSubscriptionDuration(p));
      } finally {
        if (previousSysPropValue == null) {
          System.clearProperty(DemoProperties.DEMO_API_PROPERTIES_FILE_SYSTEM_PROPERTY);
        } else {
          System.setProperty(DemoProperties.DEMO_API_PROPERTIES_FILE_SYSTEM_PROPERTY, previousSysPropValue);
        }
      }
    }
}
