package com.talentica.job4j.api;

import java.util.concurrent.Future;

import com.talentica.job4j.model.JobStatus;


public interface Job<I,O> extends JobControl{ 
	
	public O process(I input);
	public Future<O> submit(I input);
	public boolean schedule();	
	
	public String getName();
	public void setName(String name); 
	public String getStartCronSchedule();
	public void setStartCronSchedule(String startCronSchedule);
	public String getStopCronSchedule();
	public void setStopCronSchedule(String stopCronSchedule);
	public String getTimeZone();
	public void setTimeZone(String timeZone);
	public JobStatus getJobStatus();
	
	public InputProducer<I> getInputProducer();
	public void setInputProducer(InputProducer<I> inputProducer);
	public Task<I,O> getTask();
	public void setTask(Task<I,O> task);
	public OutputConsumer<O> getOutputConsumer();
	public void setOutputConsumer(OutputConsumer<O> outputConsumer);
}