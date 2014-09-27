package com.talentica.job4j.test.job1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talentica.job4j.api.OutputConsumer;
import com.talentica.job4j.test.model.B;

public class Job1OutputConsumer implements OutputConsumer<B>{
	private static final Logger logger = LoggerFactory.getLogger(Job1OutputConsumer.class);
	public void consumeOutput(B output, int counter) {
		logger.debug("Consuming >> "+output);
	}

}
