package com.talentica.job4j.api;

public interface Task<I,O> {	
	public abstract O processTask(I input) throws Exception;
}
