package org.jxapi.netutils.websocket.mock.server;

/**
 * Listener interface for the mock websocket server.
 * <p>
 * Implementations of this interface can be registered with the
 * {@link MockWebsocketServerSessionService}
 * to receive events from the mock websocket server, this is done automatically by the {@link MockWebsocketServer}.
 * <p>
 * The appropriate way to use a {@link MockWebsocketServer} is to retrieve the
 * events from it as it implements the GenericObserver interface so this
 * interface need usually not be used directly.
 * 
 * @see MockWebsocketServerSessionService
 */
public interface MockWebsocketServerListener {
  
  void handleEvent(MockWebsocketServerEvent event);

}
