package com.talentica.job4j.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talentica.job4j.api.InputProducer;
import com.talentica.job4j.impl.queue.QueueInputProducer;

public final class DefaultInputProducer<I> extends QueueInputProducer<I> {	
	private static final Logger logger = LoggerFactory.getLogger(DefaultInputProducer.class);
	protected InputProducer<I> inputProducer;
	
	public DefaultInputProducer(InputProducer<I> inputProducer) {
		super();
		this.inputProducer = inputProducer;
	}

	public I produceInput() {
		return inputProducer.produceInput();
	}

}
