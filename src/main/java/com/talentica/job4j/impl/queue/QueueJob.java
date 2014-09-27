package com.talentica.job4j.impl.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talentica.job4j.constant.JobStatusEnum;
import com.talentica.job4j.impl.AbstractInputProducer;
import com.talentica.job4j.impl.AbstractJob;
import com.talentica.job4j.impl.AbstractOutputConsumer;
import com.talentica.job4j.impl.DefaultInputProducer;
import com.talentica.job4j.impl.DefaultOutputConsumer;
import com.talentica.job4j.impl.DefaultTask;
import com.talentica.job4j.impl.JThreadPoolExecutor;
import com.talentica.job4j.model.JobStatus;
import com.talentica.job4j.util.ThreadUtil;

public class QueueJob<I,O> extends AbstractJob<I, O> { 

	private static final Logger logger = LoggerFactory.getLogger(QueueJob.class);

	protected JThreadPoolExecutor threadPoolExecutor;
	private BlockingQueue<I> inputQueue = new LinkedBlockingQueue<I>();
	private BlockingQueue<I> inProcessQueue = new LinkedBlockingQueue<I>();
	private BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();
	private BlockingQueue<O> outputQueue = new LinkedBlockingQueue<O>();
	private QueueFutureTask<I,O> queueFutureTask = new QueueFutureTask<I,O>(inProcessQueue, outputQueue);

	protected void preStart(){
		if(threadPoolExecutor==null || threadPoolExecutor.isTerminated()){
			logger.info("Creating new TaskExecutor");
			this.threadPoolExecutor = new JThreadPoolExecutor(maxThreadCount, 2*maxThreadCount, (int) maxIdleTime/1000, workQueue);	
		}
	}

	public boolean pause() {
		return threadPoolExecutor.pause();
	}

	public boolean resume() {
		return threadPoolExecutor.resume();
	}

	public boolean abort() {
		return threadPoolExecutor.abort();
	}

	public Future<O> submit(I input) {
		DefaultTask task = createTask(input);
		FutureTask<O> futureTask = queueFutureTask.getFutureTask(task);
		logger.debug("task>> "+task+" futureTask >> "+futureTask+" threadPoolExecutor"+threadPoolExecutor);
		Future<O> futureResult = (Future<O>) threadPoolExecutor.submit(futureTask);
		return futureResult;
	}

	public void run() {	
		logger.info("Job "+name + " started running");
		int counter=0;
		while(true){
			try{
				I input = inputQueue.poll(threadSleepTime, TimeUnit.MILLISECONDS);
				if(input!=null){	
					while(workQueue.remainingCapacity()<=0){
						ThreadUtil.sleep(1000, "Waiting for WorkQueue space");
					}
					logger.info("Submitting "+task.getClass().getSimpleName()+" : ("+counter+") for input>> "+input);
					submit(input);	
					counter++;
				}
				logger.debug(abstractInputProducer.isFinished() +" >>>> "+ inputQueue.isEmpty());
				if(abstractInputProducer.isFinished() && inputQueue.isEmpty()){
					threadPoolExecutor.shutdown();
					while(!threadPoolExecutor.isTerminated()){
						ThreadUtil.sleep(1000, "Waiting for ThreadPoolExecuter be Terminated");
					}
					logger.debug("abstractOutputConsumer.setFinished >> ");
					abstractOutputConsumer.setFinished(true);
					break;
				}

			} catch (Exception e) {
				logger.error(e.getMessage(),e);
				ThreadUtil.sleep(1000);
			}	
		}
		logger.info(name+" stopped!!");		
	}

	public JobStatus getJobStatus() {
		String status = "ready";
		if(threadPoolExecutor!=null){
			jobStatus.setSubmittedTaskCount(threadPoolExecutor.getTaskCount());
			jobStatus.setActiveTaskCount(threadPoolExecutor.getActiveCount());
			jobStatus.setCompletedTaskCount(threadPoolExecutor.getCompletedTaskCount());	
			jobStatus.setCurrentThreadCount(threadPoolExecutor.getPoolSize());

			if(threadPoolExecutor.isTerminated()){
				status = JobStatusEnum.COMPLETED.name();
			} else if(threadPoolExecutor.isTerminating()){
				status = JobStatusEnum.STOPPING.name();
			} else if(threadPoolExecutor.isShutdown()){
				status = JobStatusEnum.STOPPING.name();		
			} else if(threadPoolExecutor.isPaused()){
				status = JobStatusEnum.PAUSED.name();
			} else if(threadPoolExecutor.getActiveCount() > 0){
				status = JobStatusEnum.RUNNING.name();
			} 
		}
		jobStatus.setStatus(status);
		return jobStatus;
	}

	public String getStatus() {
		String status = "ready";
		if(threadPoolExecutor!=null){
			jobStatus.setSubmittedTaskCount(threadPoolExecutor.getTaskCount());
			jobStatus.setActiveTaskCount(threadPoolExecutor.getActiveCount());
			jobStatus.setCompletedTaskCount(threadPoolExecutor.getCompletedTaskCount());	
			jobStatus.setCurrentThreadCount(threadPoolExecutor.getPoolSize());

			if(threadPoolExecutor.isTerminated()){
				status = JobStatusEnum.COMPLETED.name();
			} else if(threadPoolExecutor.isTerminating()){
				status = JobStatusEnum.STOPPING.name();
			} else if(threadPoolExecutor.isShutdown()){
				status = JobStatusEnum.STOPPING.name();		
			} else if(threadPoolExecutor.isPaused()){
				status = JobStatusEnum.PAUSED.name();
			} else if(threadPoolExecutor.getActiveCount() > 0){
				status = JobStatusEnum.RUNNING.name();
			} 
		}
		jobStatus.setStatus(status);
		return status;

	}

	public BlockingQueue<I> getInputQueue() {
		return inputQueue;
	}

	public void setInputQueue(BlockingQueue<I> inputQueue) {
		this.inputQueue = inputQueue;
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

	public void setAbstractInputProducer(AbstractInputProducer<I> abstractInputProducer) {
		if(abstractInputProducer==null){
			this.abstractInputProducer = new DefaultInputProducer<I>(inputProducer);
		}
		this.abstractInputProducer.setJob(this);
	}

	public void setAbstractOutputConsumer(AbstractOutputConsumer<O> abstractOutputConsumer) {
		if(abstractOutputConsumer==null){
			this.abstractOutputConsumer = new DefaultOutputConsumer<O>(outputConsumer);
		}
		this.abstractOutputConsumer.setJob(this);
	}

}