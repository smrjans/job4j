package com.talentica.job4j.model;

public class QueueJobDetail extends JobDetail {
	
	protected long queueSize;
	protected long maxWorkQueueSize;
	protected long maxInputQueueSize;
	protected long maxOutputQueueSize;
	
	public long getQueueSize() {
		return queueSize;
	}
	public void setQueueSize(long queueSize) {
		this.queueSize = queueSize;
	}
	public long getMaxWorkQueueSize() {
		return maxWorkQueueSize;
	}
	public void setMaxWorkQueueSize(long maxWorkQueueSize) {
		this.maxWorkQueueSize = maxWorkQueueSize>0?maxWorkQueueSize:queueSize;
	}
	public long getMaxInputQueueSize() {
		return maxInputQueueSize;
	}
	public void setMaxInputQueueSize(long maxInputQueueSize) {
		this.maxInputQueueSize = maxInputQueueSize>0?maxInputQueueSize:queueSize;
	}
	public long getMaxOutputQueueSize() {
		return maxOutputQueueSize;
	}
	public void setMaxOutputQueueSize(long maxOutputQueueSize) {
		this.maxOutputQueueSize = maxOutputQueueSize>0?maxOutputQueueSize:queueSize;
	}
}
