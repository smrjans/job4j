package com.talentica.job4j.test.job1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talentica.job4j.api.InputProducer;
import com.talentica.job4j.test.model.A;

public class Job1InputProducer implements InputProducer<A> {
	private static final Logger logger = LoggerFactory.getLogger(Job1InputProducer.class);
	private int num=1000;

	public A produceInput() {
		A a = null;
		if(num>0){
			num--;
			a = new A();
			a.setId(num);
			a.setName("A name"+num);
			a.setDescription("A description"+num);
		}else{
			num=1000;
		}
		logger.debug("Produced >> "+a);
		return a;
	}

}
