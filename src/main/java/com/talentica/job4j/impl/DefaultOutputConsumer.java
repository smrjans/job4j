package com.talentica.job4j.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talentica.job4j.api.Job;
import com.talentica.job4j.api.OutputConsumer;
import com.talentica.job4j.impl.queue.QueueOutputConsumer;

public final class DefaultOutputConsumer<O> extends QueueOutputConsumer<O>{
	private static final Logger logger = LoggerFactory.getLogger(DefaultOutputConsumer.class);
	private OutputConsumer<O> outputConsumer;

	public DefaultOutputConsumer(OutputConsumer<O> outputConsumer) {
		super();
		this.outputConsumer = outputConsumer;
	}

	public void consumeOutput(O output, int counter) {
		outputConsumer.consumeOutput(output, counter);
	}
}
