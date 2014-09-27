package com.talentica.job4j.model;

import java.util.List;

import javax.annotation.PostConstruct;

import com.talentica.job4j.api.Job;
import com.talentica.job4j.util.CronUtil;

public class JobGroup extends JobSchedule{	
	private List<Job> jobList;

	@PostConstruct
	public void init() {
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
		return true;
	}

	public boolean stop() {
		for(Job job : jobList){
			job.stop();
		}
		return true;
	}
}