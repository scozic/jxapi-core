package com.scz.jxapi.util;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestCompleteFutureTask {
	
	private static final Logger log = LoggerFactory.getLogger(TestCompleteFutureTask.class);

	public static void main(String[] args) {
		try {
			CompletableFuture<String> myFuture = new CompletableFuture<>();
			new Thread("WORKER") {
				@Override
				public void run() {
					try {
						Thread.sleep(2000);
						log.info("Completing...");
						myFuture.complete("Hello world");
					} catch (InterruptedException e) {
						log.error("Interrupted", e);
					}
				}
			}.start();;
			
			Thread.sleep(1000);
			myFuture.thenAccept(s -> {
				log.info("thenAccept:" + s);
			});
		} catch (Throwable t) {
			log.error("FATAL", t);
		}
	}
}
