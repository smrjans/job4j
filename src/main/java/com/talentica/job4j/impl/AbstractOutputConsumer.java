package com.talentica.job4j.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talentica.job4j.api.Job;
import com.talentica.job4j.api.OutputConsumerX;

public abstract class AbstractOutputConsumer<O> implements OutputConsumerX<O> {

	private static final Logger logger = LoggerFactory.getLogger(AbstractOutputConsumer.class);
	
	protected boolean isFinished;
	protected long threadSleepTime = 1000l; 

	public boolean isFinished() {
		return isFinished;
	}
	
	public void setFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}

	public void setJob(Job job) {
		AbstractJob<?,O> queueJob = (AbstractJob) job;
		this.threadSleepTime = queueJob.getThreadSleepTime();
	}
}
