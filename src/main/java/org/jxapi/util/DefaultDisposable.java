package org.jxapi.util;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Base {@link Disposable} implementation.
 */
public class DefaultDisposable implements Disposable {
  
  private final AtomicBoolean disposed = new AtomicBoolean();

  @Override
  public void dispose() {
    if (!disposed.getAndSet(true)) {
      doDispose();
    }
  }

  @Override
  public boolean isDisposed() {
    return disposed.get();
  }
  
  /**
   * This method may be called by subclasses to throw an exception if this instance is disposed, to prevent using object already disposed.
   */
  protected void checkNotDisposed() {
    if (isDisposed())
      throw new IllegalStateException("Disposed");
  }

  /**
   * Subclasses must override this method to implement actual resource disposal.
   */
  protected void doDispose() {
    // Nothing by default. Can be safely overridden.
  }
}
