package com.talentica.job4j.test.job3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talentica.job4j.api.Task;
import com.talentica.job4j.test.model.B;
import com.talentica.job4j.test.model.C;

public class Job3Task implements Task<B, C> {
	private static final Logger logger = LoggerFactory.getLogger(Job3Task.class);
	
	public C processTask(B b) throws Exception {
		logger.debug("Processing >> "+b);
		C c = new C();
		c.setId(b.getId());
		c.setName(b.getName());
		c.setDescription(b.getDescription());
		return c;
	}

}
