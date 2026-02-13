package org.jxapi.util;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Helper methods around thread management.
 */
public class ThreadUtil {
  
  private static final AtomicLong THREAD_COUNTER = new AtomicLong(0);

  private ThreadUtil() {}
  
  /**
   * Creates a {@link ThreadFactory} which creates Threads with given prefix as name.
   * @param prefix The thread name prefix
   * @return a new {@link ThreadFactory} instance
   */
  public static ThreadFactory createNamePrefixThreadFactory(String prefix) {
      return runnable -> {
          Thread t = new Thread(runnable);
          t.setName(prefix + THREAD_COUNTER.getAndIncrement());
          return t;
      };
  }

}
