package com.talentica.job4j.model;

import java.util.Date;

public class JobStatus {
	
	protected boolean isAborted;
	protected String status;
	protected Date scheduledStartTime;
	protected Date startTime;	
	protected String elapsedTime;	
	protected Date endTime;
	protected Date scheduledEndTime;
	
	protected long submittedTaskCount;
	protected int activeTaskCount;
	protected long completedTaskCount;
	protected int currentThreadCount;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public Date getScheduledStartTime() {
		return scheduledStartTime;
	}
	public void setScheduledStartTime(Date scheduledStartTime) {
		this.scheduledStartTime = scheduledStartTime;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public String getElapsedTime() {
		return elapsedTime;
	}
	public void setElapsedTime(String elapsedTime) {
		this.elapsedTime = elapsedTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Date getScheduledEndTime() {
		return scheduledEndTime;
	}
	public void setScheduledEndTime(Date scheduledEndTime) {
		this.scheduledEndTime = scheduledEndTime;
	}
	public long getSubmittedTaskCount() {
		return submittedTaskCount;
	}
	public void setSubmittedTaskCount(long submittedTaskCount) {
		this.submittedTaskCount = submittedTaskCount;
	}
	public int getActiveTaskCount() {
		return activeTaskCount;
	}
	public void setActiveTaskCount(int activeTaskCount) {
		this.activeTaskCount = activeTaskCount;
	}
	public long getCompletedTaskCount() {
		return completedTaskCount;
	}
	public void setCompletedTaskCount(long completedTaskCount) {
		this.completedTaskCount = completedTaskCount;
	}
	public int getCurrentThreadCount() {
		return currentThreadCount;
	}
	public void setCurrentThreadCount(int currentThreadCount) {
		this.currentThreadCount = currentThreadCount;
	}
	@Override
	public String toString() {
		return "JobStatus [isAborted=" + isAborted + ", status=" + status
				+ ", scheduledStartTime=" + scheduledStartTime + ", startTime="
				+ startTime + ", elapsedTime=" + elapsedTime + ", endTime="
				+ endTime + ", scheduledEndTime=" + scheduledEndTime
				+ ", submittedTaskCount=" + submittedTaskCount
				+ ", activeTaskCount=" + activeTaskCount
				+ ", completedTaskCount=" + completedTaskCount
				+ ", currentThreadCount=" + currentThreadCount + "]";
	}
	
}
