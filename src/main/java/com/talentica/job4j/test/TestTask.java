package com.talentica.job4j.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talentica.job4j.api.Task;

public class TestTask implements Task<A, B> {
	private static final Logger logger = LoggerFactory.getLogger(TestTask.class);
	
	public B processTask(A a) throws Exception {
		logger.debug("Processing >> "+a);
		B b = new B();
		b.setId(a.getId());
		b.setName(a.getName());
		b.setDescription(a.getDescription());
		return b;
	}

}
