package org.jxapi.observability;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeoutException;
import static org.junit.Assert.*;

/**
 * Unit test for {@link GenericObserver}
 */
public class GenericObserverTest {
  
  private static final Logger log = LoggerFactory.getLogger(GenericObserverTest.class);

    private GenericObserver<String> observer;

    @Before
    public void setUp() {
        observer = new GenericObserver<>();
    }

    @Test
    public void testHandleEvent() {
        observer.handleEvent("event1");
        observer.handleEvent("event2");
        assertEquals(2, observer.size());
    }

    @Test
    public void testSize() {
        assertEquals(0, observer.size());
        observer.handleEvent("event1");
        assertEquals(1, observer.size());
    }

    @Test
    public void testClear() {
        observer.handleEvent("event1");
        observer.handleEvent("event2");
        observer.clear();
        assertEquals(0, observer.size());
    }

    @Test
    public void testGetAllEvents() {
        observer.handleEvent("event1");
        observer.handleEvent("event2");
        assertEquals(2, observer.getAllEvents().size());
    }

    @Test
    public void testPop() {
        observer.handleEvent("event1");
        observer.handleEvent("event2");
        String event = observer.pop();
        assertEquals("event1", event);
        assertEquals(1, observer.size());
    }

    @Test
    public void testWaitUntilCount() throws TimeoutException {
        new Thread(() -> {
            try {
                Thread.sleep(10L);
                observer.handleEvent("event1");
                observer.handleEvent("event2");
            } catch (InterruptedException e) {
                log.error("InterruptedException occurred", e);
            }
        }).start();

        observer.waitUntilCount(2);
        assertEquals(2, observer.size());
    }

    @Test(expected = TimeoutException.class)
    public void testWaitUntilCountWithTimeout() throws TimeoutException {
        observer.waitUntilCount(1, 10);
    }

    @Test
    public void testAwait() throws TimeoutException {
        new Thread(() -> {
            try {
                Thread.sleep(10L);
                observer.handleEvent("event1");
            } catch (InterruptedException e) {
                log.error("InterruptedException occurred", e);
            }
        }).start();

        String event = observer.await();
        assertEquals("event1", event);
    }

    @Test(expected = TimeoutException.class)
    public void testAwaitWithTimeout() throws TimeoutException {
      observer.setDefaulTimeout(10L);
        observer.await();
    }
    
    @Test
    public void testCheckNoEvents_NoEventsRaised() throws Exception {
      observer.checkNoEvents(10L);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testCheckNoEvents_OneEventsRaised() throws Exception {
      observer.handleEvent("event1");
      observer.checkNoEvents(10L);
    }
    
    @Test
    public void testGetDefaultTimeout() {
      assertEquals(GenericObserver.DEFAULT_TIMEOUT, observer.getDefaulTimeout());
      observer.setDefaulTimeout(123L);
      assertEquals(123L, observer.getDefaulTimeout());
    }
}