package com.talentica.job4j.test.job2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talentica.job4j.api.InputProducer;
import com.talentica.job4j.test.model.B;

public class Job2InputProducer implements InputProducer<B> {
	private static final Logger logger = LoggerFactory.getLogger(Job2InputProducer.class);
	private int num=1000;

	public B produceInput() {
		B b = null;
		if(num>0){
			num--;
			b = new B();
			b.setId(num);
			b.setName("B name"+num);
			b.setDescription("B description"+num);
		}else{
			num=1000;
		}
		logger.debug("Produced >> "+b);
		return b;
	}

}
