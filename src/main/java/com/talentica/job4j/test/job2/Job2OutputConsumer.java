package com.talentica.job4j.test.job2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talentica.job4j.api.OutputConsumer;
import com.talentica.job4j.test.model.C;

public class Job2OutputConsumer implements OutputConsumer<C>{
	private static final Logger logger = LoggerFactory.getLogger(Job2OutputConsumer.class);
	public void consumeOutput(C output, int counter) {
		logger.debug("xxxx Consuming >> "+output);
	}

}
