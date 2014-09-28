package com.talentica.job4j.impl.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talentica.job4j.api.InputProducer;

public final class DefaultQueueInputProducer<I> extends QueueInputProducer<I> {	
	private static final Logger logger = LoggerFactory.getLogger(DefaultQueueInputProducer.class);
	protected InputProducer<I> inputProducer;
	
	public DefaultQueueInputProducer(InputProducer<I> inputProducer) {
		super();
		this.inputProducer = inputProducer;
	}

	public I produceInput() {
		return inputProducer.produceInput();
	}

	@Override
	public String toString() {
		return "DefaultQueueInputProducer [inputProducer=" + inputProducer
				+ "]";
	}
	
}
