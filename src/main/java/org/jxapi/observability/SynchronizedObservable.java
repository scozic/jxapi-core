package org.jxapi.observability;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * Thread {@link Observable} implementation, to be used when subscriptions,
 * unsubscription of listeners and dispatch of events may occur in distinct
 * threads.
 * <p>
 * Implementation takes care of not locking internal listener list during event
 * dispatch, see {@link #dispatch(Object)}.
 * Take care of potential deadlock of listener are likely to wait for another
 * thread that could subscribe/unsubscribe
 * 
 * @param <L> The observer (listener) type
 * @param <E> The event type
 * 
 * @see Observable
 * @see #dispatch(Object)
 */
public class SynchronizedObservable<L, E> extends DefaultObservable<L, E> {

  /**
   * Constructor for {@link SynchronizedObservable} with event dispatch method
   * 
   * @param eventDispatchMethod method to dispatch event to listener
   */
  public SynchronizedObservable(BiConsumer<L, E> eventDispatchMethod) {
    super(Collections.synchronizedList(new ArrayList<>()), eventDispatchMethod);
  }

  /**
   * Dispatches event to all subscribed listener in thread safe way using internal
   * listeners list as monitor. The lock is taken on internal listeners list only
   * to make a copy of it before dispatching the event so event is dispatched
   * outside lock. This could lead to events being dispatched to a listener
   * listener has been unsubscribe, but avoids performance issues because of
   * threads waiting too long on lock to add or remove a listener taken by
   * dispatch thread.
   * 
   * @param event event to dispatch
   */
  @Override
  public void dispatch(E event) {
    List<L> tmp = null;
    synchronized (listeners) {
      tmp = List.copyOf(listeners);
    }
    tmp.forEach(l -> eventDispatchMethod.accept(l, event));
  }

}
