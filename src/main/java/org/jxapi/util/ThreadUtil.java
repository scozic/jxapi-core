package org.jxapi.util;

import java.util.concurrent.ThreadFactory;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

/**
 * Helper methods around thread management.
 */
public class ThreadUtil {

	private ThreadUtil() {}
	
	/**
	 * Creates a {@link ThreadFactory} which creates Threads with given prefix as name.
	 * @param prefix The thread name prefix
	 * @return a new {@link ThreadFactory} instance
	 */
	public static ThreadFactory createNamePrefixThreadFactory(String prefix) {
		return new BasicThreadFactory.Builder().namingPattern(prefix + "%d").build();
	}
}
