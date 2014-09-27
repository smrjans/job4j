package com.talentica.job4j.util;

import it.sauronsoftware.cron4j.Predictor;
import it.sauronsoftware.cron4j.SchedulingPattern;

import java.util.Date;
import java.util.TimeZone;

public class CronUtil {
	public static Date getNextScheduledTime(String cronExp, String timeZone){
		Date nextScheduledTime = null;
		if (cronExp!=null && SchedulingPattern.validate(cronExp)) {        	
			Predictor predictor = new Predictor(cronExp);
			predictor.setTimeZone(TimeZone.getTimeZone(timeZone));				
			nextScheduledTime = predictor.nextMatchingDate();
		}
		return nextScheduledTime;		
	}
}
