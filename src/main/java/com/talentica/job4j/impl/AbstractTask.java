package com.talentica.job4j.impl;

import com.talentica.job4j.api.XTask;

public abstract class AbstractTask<I,O> implements XTask<I,O>{
	
	protected I input;

	public O call() throws Exception{		
		return processTask(input);		
	}
	
	public I getInput() {
		return input;
	}
	public void setInput(I input) {
		this.input = input;
	}
	
}
