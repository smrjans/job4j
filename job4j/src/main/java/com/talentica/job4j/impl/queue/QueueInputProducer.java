package com.talentica.job4j.impl.queue;

import java.util.Date;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talentica.job4j.api.InputProducer;
import com.talentica.job4j.api.Job;
import com.talentica.job4j.impl.AbstractInputProducer;
import com.talentica.job4j.util.ThreadUtil;

public abstract class QueueInputProducer<I> extends AbstractInputProducer<I>{

	private static final Logger logger = LoggerFactory.getLogger(QueueInputProducer.class);

	protected BlockingQueue<I> inputQueue;

	public void run() {
		logger.debug(this.getClass().getSimpleName()+" started!!");
		while (!isFinished) {
			try
			{
				I input = produceInput();	
				if (input != null) {
					inputQueue.put(input);
				}else{
					isFinished = true;
				}
			}catch(Exception e){
				logger.error(e.getMessage(),e);
				ThreadUtil.sleep(threadSleepTime);
			}
		}
		logger.debug(this.getClass().getSimpleName()+" stopped!!");
	}

	public void setJob(Job job) {
		QueueJob<I,?> queueJob = (QueueJob)job;
		this.inputQueue = queueJob.getInputQueue();
		this.threadSleepTime = queueJob.getThreadSleepTime();
	}	
}
