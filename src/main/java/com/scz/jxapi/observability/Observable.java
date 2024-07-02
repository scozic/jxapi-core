package com.scz.jxapi.observability;

/**
 * Observer pattern: Implementations are objects that can be
 * subscribed or unsubscribed observers that will be notified of incoming
 * events.
 *
 * @param <L>
 * @param <E>
 */
public interface Observable<L, E> {

	/**
	 * Adds a listener to be notified of events dispatched.
	 * @param listener
	 */
	void subscribe(L listener);

	/**
	 * Removes a subscribed listener
	 * 
	 * @param listener listener to remove
	 * @return <code>true/<code> if listener has been actually removed, <code>false</code>
	 *         if there was no such listener.
	 */
	boolean unsubscribe(L listener);

	/**
	 * Dispatches an event to all subscribed listeners.
	 * @param event event to dispatch
	 */
	void dispatch(E event);
	
	/**
	 * @param listener
	 * @return <code>true</code> <code>listener</code> is subscribed.
	 */
	boolean hasListener(L listener);

}