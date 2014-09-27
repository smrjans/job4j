package com.talentica.job4j.model;

import it.sauronsoftware.cron4j.Scheduler;

import java.util.Date;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talentica.job4j.util.CronUtil;

public abstract class JobSchedule{
	private static final Logger logger = LoggerFactory.getLogger(JobSchedule.class);
	
	protected String timeZone;
	protected String startCronSchedule;
	protected String stopCronSchedule;
	protected boolean isContinous;
	
	public boolean schedule() {		
		try {
			Date nextScheduledStartTime = CronUtil.getNextScheduledTime(startCronSchedule, timeZone);
			Date nextScheduledEndTime = CronUtil.getNextScheduledTime(stopCronSchedule, timeZone);
			logger.info(this.getClass().getName() +" startCronSchedule>> "+startCronSchedule+" stopCronSchedule>> "+stopCronSchedule);
			logger.info("Current Time: "+new Date()+ " nextScheduledStartTime: "+nextScheduledStartTime+" nextScheduledEndTime: "+nextScheduledEndTime);
			if(nextScheduledStartTime!=null && nextScheduledEndTime!=null && nextScheduledEndTime.before(nextScheduledStartTime)){
				logger.info("Starting Job Automatically: "+this.getClass().getName()+" at: "+new Date());
				start();
			}
			
			Scheduler startScheduler = new Scheduler();
			startScheduler.setTimeZone(TimeZone.getTimeZone(timeZone));
			startScheduler.schedule(startCronSchedule, new Runnable() {
				public void run() {
					logger.info("Starting Job by Schehuler: "+this.getClass().getName()+" at: "+new Date());
					start();
				}
			});
			startScheduler.start();
			
			Scheduler stopScheduler = new Scheduler();
			stopScheduler.setTimeZone(TimeZone.getTimeZone(timeZone));
			stopScheduler.schedule(stopCronSchedule, new Runnable() {
				public void run() {
					logger.info("Stopping Job by Schehuler: "+this.getClass().getName()+" at: "+new Date());
					stop();
				}
			});
			stopScheduler.start();

		}catch(Exception e){
			logger.error(e.getMessage(), e);
		}
		return true;
	}
	
	public abstract boolean start();
	public abstract boolean stop();
	
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
	
	
}
