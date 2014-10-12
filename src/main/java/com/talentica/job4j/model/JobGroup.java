package com.talentica.job4j.model;

import java.util.Date;
import java.util.List;
import com.talentica.job4j.api.Job;
import com.talentica.job4j.api.JobControl;
import com.talentica.job4j.util.CronUtil;

public class JobGroup extends JobSchedule implements JobControl{	
	protected String name;
	protected JobStatus jobStatus = new JobStatus();
	protected long threadSleepTime;
	private List<Job> jobList;

	public void init() {
		if(name==null){
			name="";
			for(Job job : jobList){
				name+=job.getName();
			}
			name+="Group";
		}
		if(startCronSchedule==null){
			startCronSchedule = jobList.get(0).getStartCronSchedule();
			stopCronSchedule = jobList.get(0).getStopCronSchedule();
			timeZone = jobList.get(0).getTimeZone();
			for(Job job : jobList){
				if(CronUtil.getNextScheduledTime(job.getStartCronSchedule(), "UTC").before(CronUtil.getNextScheduledTime(startCronSchedule, "UTC"))){
					startCronSchedule = job.getStartCronSchedule();
					timeZone = job.getTimeZone();
				}
				if(CronUtil.getNextScheduledTime(job.getStopCronSchedule(), "UTC").before(CronUtil.getNextScheduledTime(stopCronSchedule, "UTC"))){
					stopCronSchedule = job.getStopCronSchedule();
				}				
			}
		}
		
		if(threadSleepTime==0){
			threadSleepTime = jobList.get(0).getThreadSleepTime();
			for(Job job : jobList){
				if(job.getThreadSleepTime()>threadSleepTime){
					threadSleepTime = job.getThreadSleepTime();
				}
			}
		}
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public JobStatus getJobStatus() {
		String status = "ready";
		jobStatus.setStatus(status);
		
		return jobStatus;
	}

	public long getThreadSleepTime() {
		return threadSleepTime;
	}

	public void setThreadSleepTime(long threadSleepTime) {
		this.threadSleepTime = threadSleepTime;
	}

	public List<Job> getJobList() {
		return jobList;
	}
	public void setJobList(List<Job> jobList) {
		this.jobList = jobList;
	}

	public boolean start() {
		for(Job job : jobList){
			job.start();
		}
		jobStatus.setStartTime(new Date());
		jobStatus.setStopTime(null);
		return true;
	}

	public boolean stop() {
		for(Job job : jobList){
			job.stop();
		}
		jobStatus.setStopTime(new Date());
		return true;
	}

	public boolean pause() {
		for(Job job : jobList){
			job.pause();
		}
		return true;
	}

	public boolean resume() {
		for(Job job : jobList){
			job.resume();
		}
		return true;
	}

	public boolean abort() {
		for(Job job : jobList){
			job.abort();
		}
		return true;
	}

	@Override
	public String toString() {
		return "JobGroup [name=" + name + ", jobList=" + jobList + "]";
	}	
	
}