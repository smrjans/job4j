package com.talentica.job4j.api;

import java.util.List;

public interface JobManager {
	
	public List<Job> getJobList();
	public boolean processAction(String name, String action);
}
