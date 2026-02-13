package org.jxapi.netutils.rest;

import java.util.concurrent.atomic.AtomicLong;

import org.jxapi.util.DefaultDisposable;

/**
 * Abstract implementation of the {@link HttpRequestExecutor} interface.
 * <p>
 * This class provides a default implementation for the
 * {@link #setRequestTimeout(long)} and {@link #getRequestTimeout()} methods.
 */
public abstract class AbstractHttpRequestExecutor extends DefaultDisposable implements HttpRequestExecutor {

  private AtomicLong requestTimeout = new AtomicLong(DEFAULT_REQUEST_TIMEOUT);

  @Override
  public void setRequestTimeout(long requestTimeout) {
    this.requestTimeout.set(requestTimeout);
    
  }

  @Override
  public long getRequestTimeout() {
    return this.requestTimeout.get();
  }

}
