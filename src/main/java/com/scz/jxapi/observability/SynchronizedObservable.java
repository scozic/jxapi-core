package com.scz.jxapi.observability;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.BiConsumer;

/**
 * Thread {@link Observable} implementation, to be used when subscriptions,
 * unsubscription of listeners and dispatch of events may occur in distinct
 * threads.
 * <p>
 * Take care of potential deadlock of listener are likely to wait for another thread that could subscribe/unsubscribe 
 * @param <L>
 * @param <E>
 */
public class SynchronizedObservable<L, E> extends DefaultObservable<L, E> {

	public SynchronizedObservable(BiConsumer<L, E> eventDispatchMethod) {
		super(Collections.synchronizedList(new ArrayList<>()), eventDispatchMethod);
	}
	
	/**
	 * Dispatches event to all subscribed listener in thread safe way synchronized
	 * around internal list.
	 * 
	 * @param event event to dispatch
	 */
	public void dispatch(E event) {
		synchronized (listeners) {
			super.dispatch(event);
		}
	}

}
