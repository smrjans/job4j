package com.talentica.job4j.model;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class JobStatus {
	
	protected boolean isAborted;
	protected String status;
	protected Date scheduledStartTime;
	protected Date startTime;	
	protected String elapsedTime;	
	protected Date stopTime;
	protected Date scheduledStopTime;
	
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
		Date beginTime = startTime!=null?startTime:new Date();
		Date endTime = stopTime!=null?stopTime:new Date();
		long millis = endTime.getTime() - beginTime.getTime();
		long hours = TimeUnit.MILLISECONDS.toHours(millis);
		millis -= TimeUnit.HOURS.toMillis(hours);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
		millis -= TimeUnit.MINUTES.toMillis(minutes);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
		elapsedTime = String.format("%d:%d:%d", hours,minutes,seconds);

		return elapsedTime;
	}
	public void setElapsedTime(String elapsedTime) {
		this.elapsedTime = elapsedTime;
	}
	public Date getStopTime() {
		return stopTime;
	}
	public void setStopTime(Date stopTime) {
		this.stopTime = stopTime;
	}
	public Date getScheduledStopTime() {
		return scheduledStopTime;
	}
	public void setScheduledStopTime(Date scheduledStopTime) {
		this.scheduledStopTime = scheduledStopTime;
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
				+ stopTime + ", scheduledEndTime=" + scheduledStopTime
				+ ", submittedTaskCount=" + submittedTaskCount
				+ ", activeTaskCount=" + activeTaskCount
				+ ", completedTaskCount=" + completedTaskCount
				+ ", currentThreadCount=" + currentThreadCount + "]";
	}
	
}
