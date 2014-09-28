package com.talentica.job4j.impl;

import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.talentica.job4j.api.InputProducer;
import com.talentica.job4j.api.Job;
import com.talentica.job4j.api.OutputConsumer;
import com.talentica.job4j.api.Task;
import com.talentica.job4j.constant.RecoveryTypeEnum;
import com.talentica.job4j.model.JobSchedule;
import com.talentica.job4j.model.JobStatus;

public abstract class AbstractJob<I,O> extends JobSchedule implements Job<I,O>, Runnable {
	private static final Logger logger = LoggerFactory.getLogger(AbstractJob.class);
	
	protected String name;
	protected String description;
	
	protected int maxThreadCount;
	protected long threadSleepTime = 1000l;
	protected long maxIdleTime = 60*1000l;
	
	protected String mailingList;
	protected boolean isEmailEnabled;
	protected String recoveryType = RecoveryTypeEnum.DISK.name();
	
	protected JobStatus jobStatus = new JobStatus();
	
	protected InputProducer<I> inputProducer;
	protected OutputConsumer<O> outputConsumer;
	protected Task<I,O> task;
	
	protected AbstractInputProducer<I> abstractInputProducer;
	protected AbstractOutputConsumer<O> abstractOutputConsumer;
	protected DefaultTask<I,O> defaultTask;
	protected Thread jobThread;
	protected BlockingQueue<Runnable> workQueue;
	protected Thread inputProducerThread;
	protected Thread outputConsumerThread;
	
	@Autowired
	private ApplicationContext applicationContext;
		
	public boolean start() {
		preStart();
		if(inputProducerThread==null || !inputProducerThread.isAlive()){
			logger.info("Creating new inputProducerThread...");			
			this.abstractInputProducer.setFinished(false);
			this.inputProducerThread = new Thread(abstractInputProducer, abstractInputProducer.getClass().getSimpleName());
			inputProducerThread.start(); 
		}
		if(outputConsumerThread==null || !outputConsumerThread.isAlive()){
			logger.info("Creating new OutputConsumerThread...");
			this.outputConsumerThread = new Thread(abstractOutputConsumer, abstractOutputConsumer.getClass().getSimpleName());
			outputConsumerThread.start();
		}
		if(jobThread==null || !jobThread.isAlive()){
			logger.info("Creating new jobThread");
			this.jobThread = new Thread(this, name); 
			jobThread.start();
		}
		
		jobStatus.setStartTime(new Date());
		jobStatus.setStopTime(null);
		return true;
	}

	public boolean stop() {
		logger.info("Job "+ this.getClass().getSimpleName() +" stopped...");
		abstractInputProducer.setFinished(true);
		jobStatus.setStopTime(new Date());
		postStop();		
		return true;
	}

	protected void preStart(){}
	protected void postStop(){}
	
	public O process(I input) {
		Future<O> futureResult = submit(input);
		O output = null;
		try {
			output = futureResult.get();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} 
		return output;
	}
	

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	public String getStartCronSchedule() {
		return startCronSchedule;
	}
	public void setStartCronSchedule(String startCronSchedule) {
		this.startCronSchedule = startCronSchedule;
	}
	public String getStopCronSchedule() {
		return stopCronSchedule;
	}
	public void setStopCronSchedule(String stopCronSchedule) {
		this.stopCronSchedule = stopCronSchedule;
	}
	public boolean isContinous() {
		return isContinous;
	}
	public void setContinous(boolean isContinous) {
		this.isContinous = isContinous;
	}
	public int getMaxThreadCount() {
		return maxThreadCount;
	}
	public void setMaxThreadCount(int maxThreadCount) {
		this.maxThreadCount = maxThreadCount;
	}
	public long getThreadSleepTime() {
		return threadSleepTime;
	}
	public void setThreadSleepTime(long threadSleepTime) {
		this.threadSleepTime = threadSleepTime;
	}
	public long getMaxIdleTime() {
		return maxIdleTime;
	}
	public void setMaxIdleTime(long maxIdleTime) {
		this.maxIdleTime = maxIdleTime;
	}
	public String getMailingList() {
		return mailingList;
	}
	public void setMailingList(String mailingList) {
		this.mailingList = mailingList;
	}
	public boolean isEmailEnabled() {
		return isEmailEnabled;
	}
	public void setEmailEnabled(boolean isEmailEnabled) {
		this.isEmailEnabled = isEmailEnabled;
	}
	public String getRecoveryType() {
		return recoveryType;
	}
	public void setRecoveryType(String recoveryType) {
		this.recoveryType = recoveryType;
	}	
	
	public abstract JobStatus getJobStatus();

	public InputProducer<I> getInputProducer() {
		return inputProducer;
	}
	public void setInputProducer(InputProducer<I> inputProducer) {
		this.inputProducer = inputProducer;
		setAbstractInputProducer(null);
	}

	public Task<I, O> getTask() {
		return task;
	}
	public void setTask(Task<I, O> task) {
		this.task = task;
	}

	public OutputConsumer<O> getOutputConsumer() {
		return outputConsumer;
	}
	public void setOutputConsumer(OutputConsumer<O> outputConsumer) {
		this.outputConsumer = outputConsumer;
		setAbstractOutputConsumer(null);
	}	
	
	// Start: Temporary	
	public BlockingQueue<Runnable> getWorkQueue() {
		return workQueue;
	}

	public void setWorkQueue(BlockingQueue<Runnable> workQueue) {
		this.workQueue = workQueue;
	}
	// End: Temporary
	
	public abstract void setAbstractInputProducer(AbstractInputProducer<I> abstractInputProducer);
	public abstract void setAbstractOutputConsumer(AbstractOutputConsumer<O> abstractOutputConsumer);

	public DefaultTask<I, O> createTask(I input) {
		Task<I,O> task = applicationContext.getBean(this.task.getClass());
		DefaultTask<I, O> defaultTask = new DefaultTask<I,O>(task);
		defaultTask.setInput(input);
		return defaultTask;
	}
	
	
	/*@PostConstruct
	public void init() {
		if(abstractInputProducer==null){
			setAbstractInputProducer(null);
		}
		if(abstractOutputConsumer==null){
			setAbstractOutputConsumer(null);
		}
	}*/
	
	@PreDestroy
	public void finish() {
		logger.info("Job removed by the spring context!!");
	}

	public void finalize(){
		logger.info("Job garbage collected!!");   	
	}

	@Override
	public String toString() {
		return "AbstractJob [name=" + name + ", description=" + description
				+ ", maxThreadCount=" + maxThreadCount + ", threadSleepTime="
				+ threadSleepTime + ", maxIdleTime=" + maxIdleTime
				+ ", mailingList=" + mailingList + ", isEmailEnabled="
				+ isEmailEnabled + ", recoveryType=" + recoveryType
				+ ", jobStatus=" + jobStatus + ", inputProducer="
				+ inputProducer + ", outputConsumer=" + outputConsumer
				+ ", task=" + task + ", abstractInputProducer="
				+ abstractInputProducer + ", abstractOutputConsumer="
				+ abstractOutputConsumer + ", defaultTask=" + defaultTask
				+ ", workQueue=" + workQueue + "]";
	}	
	
}
