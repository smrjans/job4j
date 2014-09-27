package com.talentica.job4j.test.job1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talentica.job4j.api.Task;
import com.talentica.job4j.test.model.A;
import com.talentica.job4j.test.model.B;

public class Job1Task implements Task<A, B> {
	private static final Logger logger = LoggerFactory.getLogger(Job1Task.class);
	
	public B processTask(A a) throws Exception {
		logger.debug("Processing >> "+a);
		B b = new B();
		b.setId(a.getId());
		b.setName(a.getName());
		b.setDescription(a.getDescription());
		return b;
	}

}
