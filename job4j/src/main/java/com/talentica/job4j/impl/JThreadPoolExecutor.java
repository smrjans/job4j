package com.talentica.job4j.impl;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class JThreadPoolExecutor extends ThreadPoolExecutor {
	private boolean isPaused;
	private boolean isAborted;
	private ReentrantLock pauseLock = new ReentrantLock();
	private Condition unpaused = pauseLock.newCondition();
	
	public JThreadPoolExecutor(int coreThreadPoolSize, int maxThreadPoolSize, int keepAliveSeconds, BlockingQueue<Runnable> workQueue) {		
		super(coreThreadPoolSize, maxThreadPoolSize, keepAliveSeconds, TimeUnit.SECONDS, workQueue);
		this.allowCoreThreadTimeOut(true);
	}

	protected void beforeExecute(Thread t, Runnable r) {
		super.beforeExecute(t, r);
		pauseLock.lock();
		try {
			while(isPaused)
				unpaused.await();
		} catch (InterruptedException ie) {
			t.interrupt();
		} finally {
			pauseLock.unlock();
		}
	}


	public boolean pause() {
		pauseLock.lock();
		try {
			isPaused = true;
		} finally {
			pauseLock.unlock();
		}
		return isPaused;
	}

	public boolean resume() {
		pauseLock.lock();
		try {
			isPaused = false;
			unpaused.signalAll();
		} finally {
			pauseLock.unlock();
		}
		return !isPaused;
	}

	public boolean abort() {
		shutdownNow();
		return true;
	}

	public boolean isPaused() {
		return isPaused;
	}	
	
}