package com.talentica.job4j.api;

import java.util.concurrent.Callable;

public interface TaskX<I,O> extends Task<I,O>, Callable<O>{	
	public I getInput();
	public void setInput(I input);
}
