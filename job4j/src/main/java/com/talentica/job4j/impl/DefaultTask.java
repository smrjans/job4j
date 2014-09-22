package com.talentica.job4j.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talentica.job4j.api.Task;

public final class DefaultTask<I,O> extends AbstractTask<I,O>{
	private static final Logger logger = LoggerFactory.getLogger(DefaultTask.class);

	private Task<I,O> task;
		
	public DefaultTask(Task<I, O> task) {
		super();
		this.task = task;
	}

	public O processTask(I input) throws Exception {
		return task.processTask(input);
	}
}
