package org.jxapi.util;

/**
 * Interfaces to implement by classes which instances should not be left to garbage collector before disposing used resources (threads, sockets) using .
 */
public interface Disposable {
  
  /**
   * Frees any resouces used. After this method is called, this instance is not
   * supposed to be used again. It can safely be left to garbage collector.
   * Has no effect if already disposed.
   */
  void dispose();
  
  /**
   * @return <code>true</code> if object is disposed e.g. {@link #dispose()} has been called.
   */
  boolean isDisposed();

}
