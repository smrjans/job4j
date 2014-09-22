package com.talentica.job4j.model;

import com.talentica.job4j.constant.RecoveryTypeEnum;

public class JobDetail{
	
	protected String name;
	protected String description;

	protected String timeZone;
	protected String startCronSchedule;
	protected String stopCronSchedule;
	protected boolean isContinous;
	
	protected int maxThreadCount;
	protected long threadSleepTime = 1000l;
	protected long maxIdleTime = 60*1000l;
	
	protected String mailingList;
	protected boolean isEmailEnabled;
	protected String recoveryType = RecoveryTypeEnum.DISK.name();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	public String getStartCronSchedule() {
		return startCronSchedule;
	}
	public void setStartCronSchedule(String startCronSchedule) {
		this.startCronSchedule = startCronSchedule;
	}
	public String getStopCronSchedule() {
		return stopCronSchedule;
	}
	public void setStopCronSchedule(String stopCronSchedule) {
		this.stopCronSchedule = stopCronSchedule;
	}
	public boolean isContinous() {
		return isContinous;
	}
	public void setContinous(boolean isContinous) {
		this.isContinous = isContinous;
	}
	public int getMaxThreadCount() {
		return maxThreadCount;
	}
	public void setMaxThreadCount(int maxThreadCount) {
		this.maxThreadCount = maxThreadCount;
	}
	public long getThreadSleepTime() {
		return threadSleepTime;
	}
	public void setThreadSleepTime(long threadSleepTime) {
		this.threadSleepTime = threadSleepTime;
	}
	public long getMaxIdleTime() {
		return maxIdleTime;
	}
	public void setMaxIdleTime(long maxIdleTime) {
		this.maxIdleTime = maxIdleTime;
	}
	public String getMailingList() {
		return mailingList;
	}
	public void setMailingList(String mailingList) {
		this.mailingList = mailingList;
	}
	public boolean isEmailEnabled() {
		return isEmailEnabled;
	}
	public void setEmailEnabled(boolean isEmailEnabled) {
		this.isEmailEnabled = isEmailEnabled;
	}
	public String getRecoveryType() {
		return recoveryType;
	}
	public void setRecoveryType(String recoveryType) {
		this.recoveryType = recoveryType;
	}	
}
