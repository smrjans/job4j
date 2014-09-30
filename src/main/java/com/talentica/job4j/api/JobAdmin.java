package com.talentica.job4j.api;

import java.util.List;

import com.talentica.job4j.model.JobFlow;
import com.talentica.job4j.model.JobGroup;

public interface JobAdmin {
	
	public List<JobFlow> getJobFlowList();
	public List<JobGroup> getJobGroupList();
	public List<Job> getJobList();
	public boolean process(String type, String name, String action);
}
