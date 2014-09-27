package com.talentica.job4j.model;

import java.util.List;

import javax.annotation.PostConstruct;

import com.talentica.job4j.util.CronUtil;

public class JobFlow extends JobSchedule{
	
	private List<JobGroup> jobGroupList;
	
	@PostConstruct
	public void init() {
		if(startCronSchedule==null){
			startCronSchedule = jobGroupList.get(0).getStartCronSchedule();
			stopCronSchedule = jobGroupList.get(0).getStopCronSchedule();
			timeZone = jobGroupList.get(0).getTimeZone();
			for(JobGroup jobGroup : jobGroupList){
				if(CronUtil.getNextScheduledTime(jobGroup.getStartCronSchedule(), "UTC").before(CronUtil.getNextScheduledTime(startCronSchedule, "UTC"))){
					startCronSchedule = jobGroup.getStartCronSchedule();
					timeZone = jobGroup.getTimeZone();
				}
				if(CronUtil.getNextScheduledTime(jobGroup.getStopCronSchedule(), "UTC").before(CronUtil.getNextScheduledTime(stopCronSchedule, "UTC"))){
					stopCronSchedule = jobGroup.getStopCronSchedule();
				}
			}	
		}
	}
	
	public List<JobGroup> getJobGroupList() {
		return jobGroupList;
	}
	public void setJobGroupList(List<JobGroup> jobGroupList) {
		this.jobGroupList = jobGroupList;
	}

	public boolean start() {
		for(JobGroup jobGroup : jobGroupList){
			jobGroup.start();
		}
		return true;
	}
	public boolean stop() {
		for(JobGroup jobGroup : jobGroupList){
			jobGroup.stop();
		}
		return true;
	}
	
}