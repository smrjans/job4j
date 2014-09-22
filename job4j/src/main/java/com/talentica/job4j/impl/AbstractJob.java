package com.talentica.job4j.impl;

import it.sauronsoftware.cron4j.Predictor;
import it.sauronsoftware.cron4j.Scheduler;
import it.sauronsoftware.cron4j.SchedulingPattern;

import java.util.Date;
import java.util.TimeZone;
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
import com.talentica.job4j.model.JobDetail;
import com.talentica.job4j.util.ThreadUtil;

public abstract class AbstractJob<I,O> implements Job<I,O>, Runnable {
	private static final Logger logger = LoggerFactory.getLogger(AbstractJob.class);
	
	protected JobDetail jobDetail;
	protected String name;
	protected String description;

	protected String timeZone = "UTC";
	protected String startCronSchedule;
	protected String stopCronSchedule;
	protected boolean isContinous;
	
	protected int maxThreadCount;
	protected long threadSleepTime = 1000l;
	protected long maxIdleTime = 60*1000l;
	
	protected String mailingList;
	protected boolean isEmailEnabled;
	protected String recoveryType = RecoveryTypeEnum.DISK.name();
	protected boolean isAborted;

	protected String status;
	protected Date scheduledStartTime;
	protected Date startTime;	
	protected String elapsedTime;	
	protected Date endTime;
	protected Date scheduledEndTime;
	
	protected long submittedTaskCount;
	protected int activeTaskCount;
	protected long completedTaskCount;	
	protected int currentThreadCount;	

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
		while(abstractInputProducer==null || abstractOutputConsumer==null){
			logger.debug(abstractInputProducer+" : "+abstractOutputConsumer);
			ThreadUtil.sleep(100);
		}
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
		return true;
	}

	public boolean stop() {
		logger.info("Job "+ this.getClass().getSimpleName() +" stopped...");
		abstractInputProducer.setFinished(true);
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
	

	public JobDetail getJobDetail() {
		jobDetail.setName(name);
		jobDetail.setDescription(description);
		jobDetail.setTimeZone(timeZone);
		jobDetail.setStartCronSchedule(startCronSchedule);
		jobDetail.setStopCronSchedule(stopCronSchedule);
		jobDetail.setContinous(isContinous);
		
		jobDetail.setMaxThreadCount(maxThreadCount);
		jobDetail.setThreadSleepTime(threadSleepTime);
		jobDetail.setMaxIdleTime(maxIdleTime);
		
		jobDetail.setMailingList(mailingList);
		jobDetail.setEmailEnabled(isEmailEnabled);
		jobDetail.setRecoveryType(recoveryType);
		return jobDetail;
	}
	
	public void setJobDetail(JobDetail jobDetail) {
		this.name = jobDetail.getName();
		this.description = jobDetail.getDescription();
		this.timeZone = jobDetail.getTimeZone();
		this.startCronSchedule = jobDetail.getStartCronSchedule();
		this.stopCronSchedule = jobDetail.getStopCronSchedule();
		this.isContinous = jobDetail.isContinous();
		
		this.maxThreadCount = jobDetail.getMaxThreadCount();
		this.threadSleepTime = jobDetail.getThreadSleepTime();
		this.maxIdleTime = jobDetail.getMaxIdleTime();
		
		this.mailingList = jobDetail.getMailingList();
		this.isEmailEnabled = jobDetail.isEmailEnabled();
		this.recoveryType = jobDetail.getRecoveryType();
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
	

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public Date getScheduledStartTime() {
		return scheduledStartTime;
	}
	public void setScheduledStartTime(Date scheduledStartTime) {
		this.scheduledStartTime = scheduledStartTime;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public String getElapsedTime() {
		return elapsedTime;
	}
	public void setElapsedTime(String elapsedTime) {
		this.elapsedTime = elapsedTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Date getScheduledEndTime() {
		return scheduledEndTime;
	}
	public void setScheduledEndTime(Date scheduledEndTime) {
		this.scheduledEndTime = scheduledEndTime;
	}
	public long getSubmittedTaskCount() {
		return submittedTaskCount;
	}
	public void setSubmittedTaskCount(long submittedTaskCount) {
		this.submittedTaskCount = submittedTaskCount;
	}
	public int getActiveTaskCount() {
		return activeTaskCount;
	}
	public void setActiveTaskCount(int activeTaskCount) {
		this.activeTaskCount = activeTaskCount;
	}
	public long getCompletedTaskCount() {
		return completedTaskCount;
	}
	public void setCompletedTaskCount(long completedTaskCount) {
		this.completedTaskCount = completedTaskCount;
	}
	public int getCurrentThreadCount() {
		return currentThreadCount;
	}
	public void setCurrentThreadCount(int currentThreadCount) {
		this.currentThreadCount = currentThreadCount;
	}
	
	public void setInputProducer(InputProducer<I> inputProducer) {
		this.inputProducer = inputProducer;
	}

	public void setTask(Task<I, O> task) {
		this.task = task;
	}

	public void setOutputConsumer(OutputConsumer<O> outputConsumer) {
		this.outputConsumer = outputConsumer;
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
	
	protected void scheduleJob() {		
		try {
			Date nextScheduledStartTime = computeNextScheduledTime(startCronSchedule);
			Date nextScheduledEndTime = computeNextScheduledTime(stopCronSchedule);
			logger.info("Job: "+name +" startCronSchedule>> "+startCronSchedule+" stopCronSchedule>> "+stopCronSchedule);
			logger.info("Current Time: "+new Date()+ " nextScheduledStartTime: "+nextScheduledStartTime+" nextScheduledEndTime: "+nextScheduledEndTime);
			if(nextScheduledStartTime!=null && nextScheduledEndTime!=null && nextScheduledEndTime.before(nextScheduledStartTime)){
				logger.info("Starting Job Automatically: "+name+" at: "+new Date());
				start();
			}
			
			Scheduler startScheduler = new Scheduler();
			startScheduler.setTimeZone(TimeZone.getTimeZone(timeZone));
			startScheduler.schedule(startCronSchedule, new Runnable() {
				public void run() {
					logger.info("Starting Job by Schehuler: "+name+" at: "+new Date());
					start();
				}
			});
			startScheduler.start();
			
			Scheduler stopScheduler = new Scheduler();
			stopScheduler.setTimeZone(TimeZone.getTimeZone(timeZone));
			stopScheduler.schedule(stopCronSchedule, new Runnable() {
				public void run() {
					logger.info("Stopping Job by Schehuler: "+name+" at: "+new Date());
					stop();
				}
			});
			stopScheduler.start();

		}catch(Exception e){
			logger.error(e.getMessage(), e);
		}
	}
	
	protected Date computeNextScheduledTime(String cronExp){
		Date nextScheduledTime = null;
		if (cronExp!=null && SchedulingPattern.validate(cronExp)) {        	
			Predictor predictor = new Predictor(cronExp);
			predictor.setTimeZone(TimeZone.getTimeZone(timeZone));				
			nextScheduledTime = predictor.nextMatchingDate();
		}
		return nextScheduledTime;		
	}	
	
	@PostConstruct
	public void init() {
		if(abstractInputProducer==null){
			setAbstractInputProducer(null);
		}
		if(abstractOutputConsumer==null){
			setAbstractOutputConsumer(null);
		}

		scheduleJob();
	}
	
	@PreDestroy
	public void finish() {
		logger.info("Job removed by the spring context!!");
	}

	public void finalize(){
		logger.info("Job garbage collected!!");   	
	}
}
