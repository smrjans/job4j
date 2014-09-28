package com.talentica.job4j.impl;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talentica.job4j.api.JobPipe;
import com.talentica.job4j.util.ThreadUtil;

public class DefaultJobPipe<I,O> implements JobPipe<I,O>{
	private static final Logger logger = LoggerFactory.getLogger(DefaultJobPipe.class);

	private BlockingQueue<I> pipeQueue = new LinkedBlockingQueue<I>();
	
	public I produceInput() {
		I input = null;
		try{
			logger.debug("remove >> pipeQueue size"+pipeQueue.size());
			input = pipeQueue.poll(1000, TimeUnit.MILLISECONDS);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
			ThreadUtil.sleep(1000, e.getMessage());
		}	
		return input;
	}

	public void consumeOutput(O output, int counter) {	
		try {	
			logger.debug("add >> pipeQueue size"+pipeQueue.size());
			pipeQueue.put((I) output);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			ThreadUtil.sleep(1000, e.getMessage());
		}	
	}
}
