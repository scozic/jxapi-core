package org.jxapi.observability;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * The GenericObserver class is a generic observer implementation that allows
 * tracking and waiting for events of a specific type.
 * <br>
 * Useful for tests.
 *
 * @param <T> the type of events to observe
 */
public class GenericObserver<T> {

  /**
   * The default timeout value in milliseconds.
   */
  public static final long DEFAULT_TIMEOUT = 2000L;

  /**
   * The list of observed events.
   */
  private final List<T> events = Collections.synchronizedList(new ArrayList<>());
  
  private long defaulTimeout = DEFAULT_TIMEOUT;

  /**
   * Handles the incoming event by adding it to the list of observed events.
   *
   * @param event the event to handle
   */
  public void handleEvent(T event) {
    events.add(event);
  }

  /**
   * Returns the number of events currently observed.
   *
   * @return the number of events
   */
  public int size() {
    return events.size();
  }

  /**
   * Clears the list of observed events.
   */
  public void clear() {
    events.clear();
  }

  /**
   * Returns a copy of all the observed events.
   *
   * @return a list of all the observed events
   */
  public List<T> getAllEvents() {
    synchronized (events) {
      List<T> res = new ArrayList<>(events.size());
      events.forEach(res::add);
      return res;
    }
  }

  /**
   * Removes and returns the first observed event from the list.
   *
   * @return the first observed event
   * @throws IndexOutOfBoundsException if observed event list is empty
   */
  public T pop() {
    return events.remove(0);
  }

  /**
   * Waits until the number of observed events reaches the specified count.
   * Uses the default timeout value.
   *
   * @param count the desired number of observed events
   * @return the current instance of GenericObserver
   * @throws TimeoutException if the timeout is reached before the desired count is reached
   */
  public GenericObserver<T> waitUntilCount(int count) throws TimeoutException {
    return waitUntilCount(count, defaulTimeout);
  }
  
  /**
   * Checks no events are raised for a given period
   * @param delay delay during which no events should be raied.
   * @throws InterruptedException eventually thrown during <code>Thread.sleep(delay)</code> 
   */
  public void checkNoEvents(long delay) throws InterruptedException {
    if (events.isEmpty()) {
      Thread.sleep(delay);
    }
    if (!events.isEmpty()) {
      throw new IllegalStateException("Received " + events.size() + " events, first:" + events.get(0));
    }
  }

  /**
   * Waits until the number of observed events reaches the specified count within the given timeout.
   *
   * @param count   the desired number of observed events
   * @param timeout the maximum time to wait in milliseconds
   * @return the current instance of GenericObserver
   * @throws TimeoutException if the timeout is reached before the desired count is reached
   */
  public GenericObserver<T> waitUntilCount(int count, long timeout) throws TimeoutException {
    long start = System.currentTimeMillis();
    while (events.size() < count && System.currentTimeMillis() - start < timeout) {
      try {
        Thread.sleep(1);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new IllegalArgumentException("Interrupted waiting for " + count + " events", e);
      }
    }

    if (size() < count) {
      throw new TimeoutException("Got only " + size()
          + " events, expecting " + count
          + " after " + timeout + "ms");
    }
    return this;
  }

  /**
   * Waits until at least one event is observed and returns the first observed event.
   * Uses the default timeout value.
   *
   * @return the first observed event
   * @throws TimeoutException if the timeout is reached before an event is observed
   */
  public T await() throws TimeoutException {
    return await(DEFAULT_TIMEOUT);
  }
  
  /**
   * Waits until at least one event is observed and returns the first observed event.
   *
   * @param timeout the maximum time to wait in milliseconds
   * @return the first observed event
   * @throws TimeoutException if the timeout is reached before an event is observed
   */
  public T await(long timeout) throws TimeoutException {
    return waitUntilCount(1).pop();
  }

  /**
   * Returns the default timeout value.
   * @return default timeout in milliseconds
   */
  public long getDefaulTimeout() {
    return defaulTimeout;
  }

  /**
   * Sets the default timeout value.
   * @param defaulTimeout default timeout in milliseconds
   */
  public void setDefaulTimeout(long defaulTimeout) {
    this.defaulTimeout = defaulTimeout;
  }
}
