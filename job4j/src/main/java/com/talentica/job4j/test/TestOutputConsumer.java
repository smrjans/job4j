package com.talentica.job4j.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talentica.job4j.api.OutputConsumer;

public class TestOutputConsumer implements OutputConsumer<B>{
	private static final Logger logger = LoggerFactory.getLogger(TestOutputConsumer.class);
	public void consumeOutput(B output, int counter) {
		logger.debug("Consuming >> "+output);
	}

}
