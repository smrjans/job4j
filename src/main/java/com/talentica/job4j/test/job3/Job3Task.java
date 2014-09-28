package com.talentica.job4j.test.job3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talentica.job4j.api.Task;
import com.talentica.job4j.test.model.C;
import com.talentica.job4j.test.model.D;

public class Job3Task implements Task<C, D> {
	private static final Logger logger = LoggerFactory.getLogger(Job3Task.class);
	
	public D processTask(C c) throws Exception {
		logger.debug("Processing >> "+c);
		D d = new D();
		d.setId(c.getId());
		d.setName(c.getName());
		d.setDescription(c.getDescription());
		return d;
	}

}
