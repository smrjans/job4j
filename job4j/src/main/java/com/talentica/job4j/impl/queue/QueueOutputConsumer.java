package com.talentica.job4j.impl.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talentica.job4j.api.Job;
import com.talentica.job4j.impl.AbstractOutputConsumer;
import com.talentica.job4j.util.ThreadUtil;

public abstract class QueueOutputConsumer<O> extends AbstractOutputConsumer<O>{

	private static final Logger logger = LoggerFactory.getLogger(QueueOutputConsumer.class);
	
	protected BlockingQueue<O> outputQueue;

	public void run(){
		logger.debug(this.getClass().getSimpleName()+" started!!");
		if(outputQueue==null){
			isFinished = true;
		}

		int counter = 0;
		while(!isFinished){
			try{
				O output = outputQueue.poll(threadSleepTime, TimeUnit.MILLISECONDS);
				if(output!=null){
					consumeOutput(output, counter);				
					counter++;
				}
			}catch(Exception e){
				logger.error(e.getMessage(),e);
				ThreadUtil.sleep(threadSleepTime);
			}
		}
		logger.debug(this.getClass().getSimpleName()+" stopped!!");
	}
	
	public void setJob(Job job) {
		QueueJob<?,O> queueJob = (QueueJob) job;
		this.outputQueue = queueJob.getOutputQueue();
		this.threadSleepTime = queueJob.getThreadSleepTime();
	}
}
