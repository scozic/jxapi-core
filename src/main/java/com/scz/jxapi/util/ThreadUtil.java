package com.scz.jxapi.util;

import java.util.concurrent.ThreadFactory;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

public class ThreadUtil {

	private ThreadUtil() {}
	
	public static ThreadFactory createNamePrefixThreadFactory(String prefix) {
		return new BasicThreadFactory.Builder().namingPattern(prefix + "%d").build();
	}
}
