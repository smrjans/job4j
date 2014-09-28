package com.talentica.job4j.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.talentica.job4j.api.Job;
import com.talentica.job4j.api.JobManager;
import com.talentica.job4j.constant.JobTypeEnum;
import com.talentica.job4j.model.JobFlow;
import com.talentica.job4j.model.JobGroup;

public class JobManagerImpl implements JobManager{	
	private static final Logger logger = LoggerFactory.getLogger(JobManagerImpl.class);

	@Autowired	
	private List<Job> jobList;

	@Autowired	
	private List<JobGroup> jobGroupList;

	@Autowired	
	private List<JobFlow> jobFlowList;

	private Map<Job, List<JobGroup>> jobGroupByJobMap = new HashMap<Job, List<JobGroup>>();
	private Map<JobGroup, List<JobFlow>> jobFlowByJobGroupMap = new HashMap<JobGroup, List<JobFlow>>();

	@PostConstruct
	public void init() {		
		for(Job job : jobList){
			List<JobGroup> jobGroups = jobGroupByJobMap.get(job);
			if(jobGroups==null){
				jobGroups = new ArrayList<JobGroup>();
				jobGroupByJobMap.put(job, jobGroups);
			}
			for(JobGroup jobGroup : jobGroupList){
				if(jobGroup.getJobList().contains(job)){
					jobGroups.add(jobGroup);
				}				
			}
		}

		for(JobGroup jobGroup : jobGroupList){
			List<JobFlow> jobFlows = jobFlowByJobGroupMap.get(jobGroup);
			if(jobFlows==null){
				jobFlows = new ArrayList<JobFlow>();
				jobFlowByJobGroupMap.put(jobGroup, jobFlows);
			}
			for(JobFlow jobFlow : jobFlowList){
				if(jobFlow.getJobGroupList().contains(jobGroup)){
					jobFlows.add(jobFlow);
				}
			}
		}

		logger.debug("jobGroupByJobMap >> "+jobGroupByJobMap);
		logger.debug("jobFlowByJobGroupMap >> "+jobFlowByJobGroupMap);

		/*if(jobFlowList!=null && jobFlowList.size()>0){
			for(JobFlow jobFlow : jobFlowList){
				jobFlow.schedule();
			}
		}else if(jobGroupList!=null && jobGroupList.size()>0){
			for(JobGroup jobGroup : jobGroupList){
				jobGroup.schedule();
			}
		}else{		
			for(Job job : jobList){
				job.schedule();
			}
		}*/
	}

	public boolean processAction(String type, String name, String action){		
		boolean status=false;	
		if(type==null){
			type = "job";
		}
		if(name!=null && action!=null){
			switch (JobTypeEnum.valueOf(type)) {
			case flow:
				for(JobFlow jobFlow : jobFlowList){
					if(jobFlow.getName().equalsIgnoreCase(name)){
						logger.info("Invoking "+action+" action on jobFlow >> "+name);
						if(action.contains("start")){
							status = jobFlow.start();
						} else if(action.contains("pause")){
							status = jobFlow.pause();
						} else if(action.contains("resume")){
							status = jobFlow.resume();
						} else if(action.contains("stop")){
							status = jobFlow.stop();
						} 
					}
				}
				break;
			case group:
				for(JobGroup jobGroup : jobGroupList){
					if(jobGroup.getName().equalsIgnoreCase(name)){
						logger.info("Invoking "+action+" action on jobGroup >> "+name);
						if(action.contains("start")){
							status = jobGroup.start();
						} else if(action.contains("pause")){
							status = jobGroup.pause();
						} else if(action.contains("resume")){
							status = jobGroup.resume();
						} else if(action.contains("stop")){
							status = jobGroup.stop();
						} 
					}
				}
				break;
			case job:
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
				break;
			}
		}
		return status;			
	}


	public List<Job> getJobList() {
		return jobList;
	}
	public List<JobGroup> getJobGroupList() {
		return jobGroupList;
	}
	public List<JobFlow> getJobFlowList() {
		return jobFlowList;
	}	
}