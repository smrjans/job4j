package com.talentica.job4j.impl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.talentica.job4j.api.InputProducer;
import com.talentica.job4j.api.Job;
import com.talentica.job4j.api.JobAdmin;
import com.talentica.job4j.api.JobPipe;
import com.talentica.job4j.api.OutputConsumer;
import com.talentica.job4j.constant.JobTypeEnum;
import com.talentica.job4j.model.JobFlow;
import com.talentica.job4j.model.JobGroup;

public class DefaultJobAdmin implements JobAdmin{	
	private static final Logger logger = LoggerFactory.getLogger(DefaultJobAdmin.class);

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
		//find parents
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
	
		//connect jobs
		for(JobFlow jobFlow :jobFlowList){
			List<JobGroup> jobGroupList = jobFlow.getJobGroupList();
			for(int jobGroupIndex=0; jobGroupIndex<jobGroupList.size()-1; jobGroupIndex++){ //last have no dependant
				JobGroup jobGroup = jobGroupList.get(jobGroupIndex);
				for(Job job: jobGroup.getJobList()){
					connectDependantJobs(job, getDependantJobList(job, jobGroupList.get(jobGroupIndex+1)));
				}
			}
		}
		
		for(JobFlow jobFlow : jobFlowList){
			for(JobGroup jobGroup : jobFlow.getJobGroupList()){
				jobGroup.init();
			}
			jobFlow.init();
		}
		
		//set job/group threadSleepTime = current+prev
		for(JobFlow jobFlow : jobFlowList){
			for(int jobGroupIndex=1; jobGroupIndex<jobGroupList.size(); jobGroupIndex++){ //last have no dependant
				JobGroup jobGroup = jobGroupList.get(jobGroupIndex);
				long threadSleepTime = jobGroup.getThreadSleepTime()+3*jobGroupList.get(jobGroupIndex-1).getThreadSleepTime();
				jobGroup.setThreadSleepTime(threadSleepTime);
				logger.debug(jobGroup.getName()+" threadSleepTime: "+threadSleepTime);
				for(Job job: jobGroup.getJobList()){
					job.setThreadSleepTime(threadSleepTime);
					logger.debug(job.getName()+" threadSleepTime: "+threadSleepTime);
				}
			}
		}
		
		//schedule jobs
		for(JobFlow jobFlow : jobFlowList){
			jobFlow.schedule();
		}
		for(JobGroup jobGroup : jobGroupList){
			if(jobFlowByJobGroupMap.get(jobGroup)==null || jobFlowByJobGroupMap.get(jobGroup).size()==0){
				jobGroup.init();
				jobGroup.schedule();
			}
		}
		for(Job job : jobList){
			if(jobGroupByJobMap.get(job)==null || jobGroupByJobMap.get(job).size()==0){
				job.schedule();
			}
		}
	}

	private List<Job> getDependantJobList(Job job, JobGroup nextJobGroup) {
		logger.debug("jobName>> "+job.getName()+" nextJobGroupName>> "+nextJobGroup.getName());
		List<Job> dependantJobList = new ArrayList<Job>();
		Type outputType = findParameterType(job.getOutputConsumer(), OutputConsumer.class);
		logger.debug(job.getName()+" using OutputConsumer outputType >> "+outputType);

		for(Job nextJob: nextJobGroup.getJobList()){
			Type nextInputType = null;
			if(nextJob.getInputProducer() instanceof JobPipe){
				nextInputType = ((JobPipe)nextJob.getInputProducer()).getDataType();
				logger.debug(nextJob.getName()+" using JobPipe nextInputType >> "+nextInputType);
			}else{
				nextInputType = findParameterType(nextJob.getInputProducer(), InputProducer.class);
				logger.debug(nextJob.getName()+" using InputProducer nextInputType >> "+nextInputType);
			}

			if(outputType==nextInputType){
				logger.debug(job.getName()+" added dependency >> "+nextJob.getName());
				dependantJobList.add(nextJob);
			}
		}
		
		logger.debug(job.getName()+" final dependantJobList >> "+dependantJobList);
		return dependantJobList;
	}
	
	private void connectDependantJobs(Job job, List<Job> dependantJobList) {
		if(dependantJobList!=null && dependantJobList.size()>0){
			logger.debug("Connecting "+job.getName()+" with dependantJobList >> "+dependantJobList);
			for(Job dependantJob : dependantJobList){
				JobPipe jobPipe = null;				
				if(dependantJob.getInputProducer() instanceof JobPipe){
					jobPipe = (JobPipe) dependantJob.getInputProducer();
					logger.debug(job.getName()+" Reusing dependantJob "+"dependantJob.getName()"+"'s Pipe");
				}else if(job.getOutputConsumer() instanceof JobPipe){
					jobPipe = (JobPipe) job.getOutputConsumer();
					logger.debug(job.getName()+" Reusing currentJob's Pipe");
				}else{
					jobPipe = new DefaultJobPipe(findParameterType(job.getOutputConsumer(), OutputConsumer.class));
					logger.debug(job.getName()+" Creating new Pipe");
				}
				
				job.setOutputConsumer(jobPipe);
				dependantJob.setInputProducer(jobPipe);
			}
			logger.debug(job.getName()+" Connected with dependantJobList >> "+dependantJobList);
		}		
	}

	private Type findParameterType(Object instance, Class<?> interfaceType){
		Type genericType = null;
		Type[] genericInterfaces = instance.getClass().getGenericInterfaces();
		for (Type genericInterface : genericInterfaces) {
			if (genericInterface.toString().contains(interfaceType.getCanonicalName()) && genericInterface instanceof ParameterizedType) {
				Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
				if(genericTypes!=null && genericTypes.length>0){
					genericType = genericTypes[0];
				}
			}
		}
		return genericType;
	}



	public boolean process(String type, String name, String action){		
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