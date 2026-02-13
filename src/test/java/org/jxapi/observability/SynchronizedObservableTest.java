package org.jxapi.observability;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link SynchronizedObservable}
 */
public class SynchronizedObservableTest {

    @Test
    public void testSubscribeDispatchEventsThenUnsubscribe() throws Exception {
        SynchronizedObservable<TestObserver, String> observable = new SynchronizedObservable<>((l, e) -> l.handleEvent(e));
        TestObserver observer1 = new TestObserver();
        TestObserver observer2 = new TestObserver();
        observable.subscribe(observer1);
        observable.subscribe(observer2);
        
        Assert.assertTrue(observable.hasListener(observer1));
        Assert.assertTrue(observable.hasListener(observer2));

        // Dispatch 2 events that should be received by both observers
        String event1 = "event1";
        observable.dispatch(event1);

        Assert.assertEquals(event1, observer1.pop());
        Assert.assertEquals(event1, observer2.pop());

        String event2 = "event2";
        observable.dispatch(event2);
        Assert.assertEquals(event2, observer1.pop());
        Assert.assertEquals(event2, observer2.pop());

        // Unsubscribe observer1
        Assert.assertTrue(observable.unsubscribe(observer1));
        Assert.assertFalse(observable.hasListener(observer1));

        // Dispatch 1 event that should be received only by observer2, not by observer1
        String event3 = "event3";
        observable.dispatch(event3);
        Assert.assertEquals(event3, observer2.pop());
        observer1.checkNoEvents(0L);

        // Unsubscribe observer2
        Assert.assertTrue(observable.unsubscribe(observer2));
        Assert.assertFalse(observable.hasListener(observer2));

        // Dispatch 1 event that should not be received by any observer
        String event4 = "event4";
        observable.dispatch(event4);
        observer2.checkNoEvents(0L);
        observer1.checkNoEvents(0L);
    }
    
    @Test
    public void testSubscribeUnsubscribeWhileDispatchAsyncDoesNotTriggerConcurrentModificationException() throws Exception {
      final int count = 2000000;
        SynchronizedObservable<TestObserver, String> observable = new SynchronizedObservable<>((l, e) -> l.handleEvent(e));
        TestObserver observer1 = new TestObserver();
        TestObserver observer2 = new TestObserver();
        TestObserver observer3 = new TestObserver();
        observable.subscribe(observer1);
        observable.subscribe(observer2);
        observable.subscribe(observer3);
        
        AtomicReference<Exception> ex = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1);
        new Thread(() -> {
          try {
            for (int i = 0; i < count; i++) {
                observable.dispatch("message" + i);
              }
            latch.countDown();
          } catch (Exception e) {
            ex.set(e);
          }
        }).start();
        
    for (int i = 0; i < count; i++) {
        observable.unsubscribe(observer2);
        observable.unsubscribe(observer3);
            observable.subscribe(observer2);
            observable.subscribe(observer3);
      }
    
    latch.await(10000, TimeUnit.MILLISECONDS);
    Exception e = ex.get();
    if (e != null) {
      throw new Exception("Error during message dispatch", e);
    }
    Assert.assertEquals(count, observer1.size());
    }

    private static class TestObserver extends GenericObserver<String> {
    }

}
