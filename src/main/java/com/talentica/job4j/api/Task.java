package com.talentica.job4j.api;

public interface Task<I,O> {	
	public O processTask(I input) throws Exception;
}
