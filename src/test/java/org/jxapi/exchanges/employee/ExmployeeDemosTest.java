package org.jxapi.exchanges.employee;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jxapi.exchanges.employee.gen.EmployeeConstants;
import org.jxapi.exchanges.employee.gen.EmployeeExchange;
import org.jxapi.exchanges.employee.gen.EmployeeProperties;
import org.jxapi.exchanges.employee.gen.v1.demo.EmployeeV1AddEmployeeDemo;
import org.jxapi.exchanges.employee.gen.v1.demo.EmployeeV1DeleteEmployeeDemo;
import org.jxapi.exchanges.employee.gen.v1.demo.EmployeeV1EmployeeUpdatesDemo;
import org.jxapi.exchanges.employee.gen.v1.demo.EmployeeV1UpdateEmployeeDemo;
import org.jxapi.exchanges.employee.gen.v1.pojo.Employee;
import org.jxapi.exchanges.employee.gen.v1.pojo.EmployeeV1EmployeeUpdatesMessage;
import org.jxapi.netutils.rest.javanet.HttpServerUtil;
import org.jxapi.netutils.websocket.mock.MockWebsocketListener;
import org.jxapi.netutils.websocket.mock.server.MockWebsocketServerEvent;
import org.jxapi.netutils.websocket.mock.server.MockWebsocketServerEventType;
import org.jxapi.util.DemoProperties;
import org.jxapi.util.DemoUtil;

/**
 * Integration tests for the {@link EmployeeExchange} exchange demo snippets.
 */
public class ExmployeeDemosTest {
  
  private static final Logger log = LoggerFactory.getLogger(ExmployeeDemosTest.class);
  
  private EmployeeExchangeServer server;
  private Properties config;
  
  @Before
  public void setUp() throws IOException {
    int httpPort = HttpServerUtil.findAvailablePort();
    int wsPort = HttpServerUtil.findAvailablePort(httpPort + 1);
    server = new EmployeeExchangeServer(httpPort, wsPort);
    server.start();
    config = new Properties();
    config.setProperty(EmployeeProperties.Server.BASE_HTTP_URL.getName(), server.getHttpBaseUrl());
    config.setProperty(EmployeeProperties.Server.BASE_WEBSOCKET_URL.getName(), server.getWebSocketBaseUrl());
    config.setProperty(DemoProperties.DEMO_WS_SUBSCRIPTION_DURATION_PROPERTY.getName(), "500");
    config.setProperty(DemoProperties.DEMO_WS_DELAY_BEFORE_EXIT_AFTER_UNSUBSCRIPTION_PROPERTY.getName(), "50");
  }
  
  @After
  public void tearDown() {
    this.server.stop();
  }

  /**
   * Starts a server then starts WS subscription demo, the runs the REST 'add', 'update' and 'delete' demos.
   * All REST demos are run in the same thread, while the WS subscription demo runs in a separate thread.
   * <p>
   * Evevry demo are expected to run without throwing any exception.
   * After REST demos are run, WS demo is expected to terminate with received events for all operations run in REST demos.
   * <p>
   * At the end, the server is stopped.
   */
  @Test
  public void testEmployeeExchangeDemos() throws Exception {
    EmployeeWSDemoRunner wsDemoRunner = new EmployeeWSDemoRunner(config);
    Thread wsThread = new Thread(wsDemoRunner, "WS-Demo-Thread");
    wsThread.start();
    checkWsClientConnect();
    Employee e1 = createEmployee1();
    Assert.assertTrue(EmployeeV1AddEmployeeDemo.execute(e1, config, DemoUtil::logRestApiEvent).isOk());
    Employee e1Updated = createEmployee1();
    e1Updated.setFirstName("Jane");
    Assert.assertTrue(EmployeeV1UpdateEmployeeDemo.execute(e1Updated, config, DemoUtil::logRestApiEvent).isOk());
    Assert.assertTrue(EmployeeV1DeleteEmployeeDemo.execute(e1Updated.getId(), config, DemoUtil::logRestApiEvent).isOk());
    wsThread.join(5000L);
    if (wsThread.isAlive() || wsDemoRunner.error != null) {
      throw wsDemoRunner.error;
    }
    Assert.assertEquals(3, wsDemoRunner.listener.size());
    Assert.assertEquals(EmployeeWsEventType.EMPLOYEE_ADDED.code, wsDemoRunner.listener.pop().getEventType());
    Assert.assertEquals(EmployeeWsEventType.EMPLOYEE_UPDATED.code, wsDemoRunner.listener.pop().getEventType());
    Assert.assertEquals(EmployeeWsEventType.EMPLOYEE_DELETED.code, wsDemoRunner.listener.pop().getEventType());
  }
  
  private Employee createEmployee1() {
    Employee e = new Employee();
    e.setId(1);
    e.setFirstName("John");
    e.setLastName("Doe");
    e.setProfile(EmployeeConstants.Profile.ADMIN);
    return e;
  }
  
  private void checkWsClientConnect() throws TimeoutException {
    Assert.assertEquals(MockWebsocketServerEventType.CLIENT_CONNECT, server.popWsEvent().getType());
    MockWebsocketServerEvent event = server.popWsEvent();
    Assert.assertEquals(MockWebsocketServerEventType.MESSAGE_RECEIVED, event.getType());
    Assert.assertEquals("Hello!", event.getMessage());
  }
  
  private class EmployeeWSDemoRunner implements Runnable {
    
    final Properties config;
    
    Exception error = null;
    
    MockWebsocketListener<EmployeeV1EmployeeUpdatesMessage> listener = new MockWebsocketListener<>();
    
    public EmployeeWSDemoRunner(Properties config) {
      this.config = config;
    }
    
    @Override
    public void run() {
      try {
        EmployeeV1EmployeeUpdatesDemo.subscribe(listener, config, DemoUtil::logWsApiEvent);
      } catch (Exception e) {
        log.error("Error running WS demo", e);
        error = e;
      }
    }
  }
}
