package com.talentica.job4j.api;

public interface JobControl{ 	
	public boolean start();	
	public boolean stop();
	public boolean pause();
	public boolean resume();
	public boolean abort();	
}