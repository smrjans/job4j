package com.talentica.job4j.util;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadUtil {
	private static final Logger logger = LoggerFactory.getLogger(ThreadUtil.class);
	public static void sleep(long sleepTime){
		sleep(sleepTime, "");
	}
	
	public static void sleep(long sleepTime, String message){
		try {
			logger.debug(Thread.currentThread().getName()+" begin sleep " + sleepTime + " ms. "+message);
			long start_time = new Date().getTime();
			Thread.sleep(sleepTime);
			long end_time = new Date().getTime();
			logger.debug(Thread.currentThread().getName()+ " slept " + (end_time - start_time) + " ms. "+message);
		} catch (InterruptedException e) {
			logger.error(e.getMessage(),e);
		}
	}
}
