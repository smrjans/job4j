package com.talentica.job4j.model;

import java.util.Date;
import java.util.List;

import com.talentica.job4j.api.JobControl;
import com.talentica.job4j.util.CronUtil;

public class JobFlow extends JobSchedule implements JobControl{
	private String name;
	private JobStatus jobStatus = new JobStatus();
	private List<JobGroup> jobGroupList;
	
	public void init() {
		if(name==null){
			name="";
			for(JobGroup jobGroup : jobGroupList){
				name+=jobGroup.getName();
			}
			name+="Flow";
		}
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
			//this.schedule();
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
		jobStatus.setStartTime(new Date());
		jobStatus.setStopTime(null);
		return true;
	}
	public boolean stop() {
		for(JobGroup jobGroup : jobGroupList){
			jobGroup.stop();
		}
		jobStatus.setStopTime(new Date());
		return true;
	}

	public boolean pause() {
		for(JobGroup jobGroup : jobGroupList){
			jobGroup.pause();
		}
		return true;
	}

	public boolean resume() {
		for(JobGroup jobGroup : jobGroupList){
			jobGroup.resume();
		}
		return true;
	}

	public boolean abort() {
		for(JobGroup jobGroup : jobGroupList){
			jobGroup.abort();
		}
		return true;
	}

	@Override
	public String toString() {
		return "JobFlow [name=" + name + ", jobGroupList=" + jobGroupList + "]";
	}	
}