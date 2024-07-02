package com.scz.jxapi.observability;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * Default (not thread safe) {@link Observable} implementation.
 * Allows to register or unregisters listeners (observers) to be notified of events dispatched.
 *  
 *
 * @param <L> The listener (observer) type
 * @param <E> The event type
 */
public class DefaultObservable<L, E> implements Observable<L, E> {

	protected final List<L> listeners;
	protected final BiConsumer<L, E> eventDispatchMethod;

	/**
	 * @param eventDispatchMethod function taking two arguments: listener and event,
	 *                            that should perform dispatch of an event to this
	 *                            listener.
	 */
	public DefaultObservable(BiConsumer<L, E> eventDispatchMethod) {
		this(new ArrayList<>(), eventDispatchMethod);
	}
	
	protected DefaultObservable(List<L> listeners, BiConsumer<L, E> eventDispatchMethod) {
		this.eventDispatchMethod = eventDispatchMethod;
		this.listeners = listeners;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void subscribe(L listener) {
		listeners.add(listener);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean unsubscribe(L listener) {
		return listeners.remove(listener);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dispatch(E event) {
		listeners.forEach(l -> eventDispatchMethod.accept(l, event));
	}

	@Override
	public boolean hasListener(L listener) {
		return listeners.contains(listener);
	}

}
