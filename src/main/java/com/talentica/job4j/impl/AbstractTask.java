package com.talentica.job4j.impl;

import java.util.concurrent.Callable;

import com.talentica.job4j.api.Task;

public abstract class AbstractTask<I,O> implements Task<I,O>, Callable<O>{
	
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
