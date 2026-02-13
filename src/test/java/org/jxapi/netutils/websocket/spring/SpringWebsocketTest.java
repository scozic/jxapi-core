package org.jxapi.netutils.websocket.spring;

import java.util.concurrent.TimeoutException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.jxapi.netutils.rest.javanet.HttpServerUtil;
import org.jxapi.netutils.websocket.GenericRawWebsocketMessageHandler;
import org.jxapi.netutils.websocket.GenericWebsocketErrorHandler;
import org.jxapi.netutils.websocket.Websocket;
import org.jxapi.netutils.websocket.WebsocketException;
import org.jxapi.netutils.websocket.mock.server.MockWebsocketServer;
import org.jxapi.netutils.websocket.mock.server.MockWebsocketServerEvent;
import org.jxapi.netutils.websocket.mock.server.MockWebsocketServerEventType;
import org.jxapi.netutils.websocket.mock.server.MockWebsocketServerSession;

/**
 * Unit test for {@link SpringWebsocket}
 */
public class SpringWebsocketTest {
  
  private static final long NO_EVENTS_DELAY = 200L;
  
  private int port = 8088;
  private GenericRawWebsocketMessageHandler clientListener;
  private GenericWebsocketErrorHandler clientErrorHandler;
  private MockWebsocketServer server;
  private String appName = "springWsTest";
  private String url;
  Websocket ws;
  
  @Before
  public void setUp() {
    clientListener = new GenericRawWebsocketMessageHandler();
    clientErrorHandler = new GenericWebsocketErrorHandler();
    port = HttpServerUtil.findAvailablePort();
    server = new MockWebsocketServer(port, appName);
    ws = new SpringWebsocket();
    url = server.getUrl();
    ws.setUrl(url);
    ws.addMessageHandler(clientListener);
    ws.addErrorHandler(clientErrorHandler);
  }
  
  @After
  public void tearDown() {
    if (ws != null) {
      ws.disconnect();
    }
    if (server != null) {
      server.stop();
    }
  }

  @Test
  public void testConnectAndSendMessageAndReceiveMessageAndDisconnect() throws Exception {
    // Start server
    server.start();
    server.checkNoEvents(NO_EVENTS_DELAY);

    // Connect ws client: session should be opened on server side
    ws.connect();
    Assert.assertTrue(ws.isConnected());
    MockWebsocketServerSession clientSession = popClientConnectEvent();
    
    // Send message from client to server: server should receive it
    String msg1 = "Ping!";
    clientSession.sendSync(msg1);
    popMessageReceivedFromServer(msg1);
    
    // Send message from server to client: client should receive it
    String msg2 = "Pong!";
    ws.send(msg2);
    popMessageReceivedFromClient(msg2);
    
    // Disconnect client
    ws.disconnect();
    Assert.assertFalse(ws.isConnected());
    popClientDisconnectEvent();
    server.checkNoEvents(NO_EVENTS_DELAY);
    clientListener.checkNoEvents(NO_EVENTS_DELAY);
    clientErrorHandler.checkNoEvents(NO_EVENTS_DELAY);
    server.stop();
  }
  
  @Test
  public void testClientRaisesErrorIfServerShutsDown() throws Exception {
    // Start server
    server.start();
    server.checkNoEvents(NO_EVENTS_DELAY);

    // Connect ws client: session should be opened on server side
    ws.connect();
    Assert.assertTrue(ws.isConnected());
    popClientConnectEvent();
    server.stop();  
    clientErrorHandler.waitUntilCount(1).pop();
    clientListener.checkNoEvents(NO_EVENTS_DELAY);
    clientErrorHandler.checkNoEvents(NO_EVENTS_DELAY);
    server.checkNoEvents(NO_EVENTS_DELAY);
  }
  
  @Test(expected = WebsocketException.class)
  public void testClientConnectRaisesErrorOnConnectIfServerISDown() throws Exception {
    ws.connect();
  }
  
  @Test(expected = WebsocketException.class)
  public void testWrongClientUrlInvalidScheme() throws WebsocketException {
    ws.setUrl("foo");
    ws.connect();
  }
  
  @Test(expected = WebsocketException.class)
  public void testWrongClientUrlInvalidUrlSyntax() throws WebsocketException {
    ws.setUrl("\"!");
    ws.connect();
  }
  
  private MockWebsocketServerSession popClientConnectEvent() throws TimeoutException {
    MockWebsocketServerEvent event = server.waitUntilCount(1).pop();
    Assert.assertEquals(MockWebsocketServerEventType.CLIENT_CONNECT, event.getType());
    MockWebsocketServerSession session = event.getSession();
    Assert.assertNotNull(session);
    return session;
  }
  
  private MockWebsocketServerSession popMessageReceivedFromClient(String msg) throws TimeoutException {
    MockWebsocketServerEvent event = server.waitUntilCount(1).pop();
    Assert.assertEquals(MockWebsocketServerEventType.MESSAGE_RECEIVED, event.getType());
    MockWebsocketServerSession session = event.getSession();
    Assert.assertNotNull(session);
    Assert.assertEquals(msg, event.getMessage());
    return session;
  }
  
  private MockWebsocketServerSession popClientDisconnectEvent() throws TimeoutException {
    MockWebsocketServerEvent event = server.waitUntilCount(1).pop();
    Assert.assertEquals(MockWebsocketServerEventType.CLIENT_DISCONNECT, event.getType());
    MockWebsocketServerSession session = event.getSession();
    Assert.assertNotNull(session);
    return session;
  }
  
  private void popMessageReceivedFromServer(String msg) throws TimeoutException {
    Assert.assertEquals(msg, clientListener.waitUntilCount(1).pop());
  }
}
