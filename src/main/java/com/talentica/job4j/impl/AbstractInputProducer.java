package com.talentica.job4j.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talentica.job4j.api.Job;
import com.talentica.job4j.api.XInputProducer;

public abstract class AbstractInputProducer<I> implements XInputProducer<I>{

	private static final Logger logger = LoggerFactory.getLogger(AbstractInputProducer.class);

	protected boolean isFinished;
	protected long threadSleepTime = 1000l; 

	public boolean isFinished() {
		return isFinished;
	}

	public void setFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}

	public void setJob(Job job) {
		AbstractJob abstractJob = (AbstractJob) job;
		this.threadSleepTime = abstractJob.getThreadSleepTime();
	}	
}
