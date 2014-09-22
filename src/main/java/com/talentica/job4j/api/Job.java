package com.talentica.job4j.api;

import java.util.concurrent.Future;


public interface Job<I,O>{ 
	
	public boolean start();	
	public boolean stop();
	public boolean pause();
	public boolean resume();
	public boolean abort();	
	
	public O process(I input);
	public Future<O> submit(I input);
	
	public String getName();
	public void setInputProducer(InputProducer<I> inputProducer);
	public void setTask(Task<I,O> task);
	public void setOutputConsumer(OutputConsumer<O> outputConsumer);
}