package com.talentica.job4j.impl.queue;

import com.talentica.job4j.impl.AbstractTask;

public abstract class QueueTask<I,O> extends AbstractTask<I, O>{
	
	protected I input;

	public O call() throws Exception{
		return processTask(input);		
	}
	
	public void setInput(I input) {
		this.input = input;
	}

}
