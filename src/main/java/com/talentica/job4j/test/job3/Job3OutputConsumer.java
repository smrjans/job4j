package com.talentica.job4j.test.job3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talentica.job4j.api.OutputConsumer;
import com.talentica.job4j.test.model.D;

public class Job3OutputConsumer implements OutputConsumer<D>{
	private static final Logger logger = LoggerFactory.getLogger(Job3OutputConsumer.class);
	public void consumeOutput(D output, int counter) {
		logger.debug("Consuming >> "+output);
	}

}
