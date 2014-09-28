package com.talentica.job4j.impl.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talentica.job4j.api.Job;
import com.talentica.job4j.api.OutputConsumer;

public final class DefaultQueueOutputConsumer<O> extends QueueOutputConsumer<O>{
	private static final Logger logger = LoggerFactory.getLogger(DefaultQueueOutputConsumer.class);
	private OutputConsumer<O> outputConsumer;

	public DefaultQueueOutputConsumer(OutputConsumer<O> outputConsumer) {
		super();
		this.outputConsumer = outputConsumer;
	}

	public void consumeOutput(O output, int counter) {
		outputConsumer.consumeOutput(output, counter);
	}

	@Override
	public String toString() {
		return "DefaultQueueOutputConsumer [outputConsumer=" + outputConsumer
				+ "]";
	}	
}
