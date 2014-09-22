package com.talentica.job4j.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.talentica.job4j.api.Job;
import com.talentica.job4j.api.JobManager;

public class JobManagerImpl implements JobManager{	
	private static final Logger logger = LoggerFactory.getLogger(JobManagerImpl.class);
	
	@Autowired	
	private  List<Job> jobList;

	public List<Job> getJobList() {
		return jobList;
	}
		
	public boolean processAction(String name, String action){		
		boolean status=false;	
		for(Job job : jobList){
			if(job.getName().equalsIgnoreCase(name)){
				logger.info("Invoking "+action+" action on job >> "+name);
				if(action.contains("start")){
					status = job.start();
				} else if(action.contains("pause")){
					status = job.pause();
				} else if(action.contains("resume")){
					status = job.resume();
				} else if(action.contains("stop")){
					status = job.stop();
				} 
			}
		}
		return status;			
	}

}