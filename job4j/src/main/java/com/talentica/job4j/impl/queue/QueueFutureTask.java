package com.talentica.job4j.impl.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.FutureTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talentica.job4j.impl.AbstractTask;
import com.talentica.job4j.impl.DefaultTask;

public class QueueFutureTask<I,O> extends FutureTask<O> {
	private static final Logger logger = LoggerFactory.getLogger(QueueFutureTask.class);
	
	private AbstractTask<I,O> task;
	private BlockingQueue<I> inProcessQueue;
	private BlockingQueue<O> outputQueue;
	
	public FutureTask<O> getFutureTask(AbstractTask<I,O> task){
		QueueFutureTask futureTask = new QueueFutureTask<I, O>(task);
		futureTask.setInProcessQueue(inProcessQueue);
		futureTask.setOutputQueue(outputQueue);
		
		inProcessQueue.add(task.getInput());
		return futureTask;
	}

	private QueueFutureTask(AbstractTask<I,O> task) {
		super(task);
		this.task = task;
	}    	
	
	public QueueFutureTask(BlockingQueue<I> inProcessQueue, BlockingQueue<O> outputQueue) {
		super(new DefaultTask<I, O>(null));
		this.inProcessQueue = inProcessQueue;
		this.outputQueue = outputQueue;
	} 

	protected void done() {	
		try {        		
			O output= this.get();
			if(output!=null && outputQueue!=null){
				outputQueue.add(output); 
			}

		} catch (Exception e) {
			logger.error("Error processing Input: "+ task.getInput()+" >> "+e.getCause().getMessage(),e.getCause());
		} finally {		
			inProcessQueue.remove(task.getInput());
		}
	}

	public BlockingQueue<I> getInProcessQueue() {
		return inProcessQueue;
	}

	public void setInProcessQueue(BlockingQueue<I> inProcessQueue) {
		this.inProcessQueue = inProcessQueue;
	}

	public BlockingQueue<O> getOutputQueue() {
		return outputQueue;
	}

	public void setOutputQueue(BlockingQueue<O> outputQueue) {
		this.outputQueue = outputQueue;
	}	
	
}
