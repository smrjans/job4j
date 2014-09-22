package com.talentica.job4j.impl;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.SerializationUtils;

import com.talentica.job4j.api.JobSerializer;
import com.talentica.job4j.impl.queue.QueueJob;

public class DiskJobSerializer implements JobSerializer, Serializable{
	private static final Logger logger = LoggerFactory.getLogger(DiskJobSerializer.class);
	
	private static final long serialVersionUID = 1L; 
	private long jobInstanceId;
	private QueueJob job;
	protected File serializationFile;	
	protected File backupSerializationFile;
	
	public boolean serialize(){	
			try {						
				byte[] bytes = SerializationUtils.serialize(this);
				FileCopyUtils.copy(bytes, backupSerializationFile);
				FileCopyUtils.copy(bytes, serializationFile);

			} catch (IOException e) {
				e.printStackTrace();
			}	
			return true;
	
	}

	public boolean deserialize(){
		if(serializationFile.exists()) {
			DiskJobSerializer jobInstance = getSerializedJobInstance(serializationFile);
			if(jobInstance==null){
				jobInstance = getSerializedJobInstance(backupSerializationFile);
			}
		}
		return true;
	}

	private DiskJobSerializer getSerializedJobInstance(File serializationFile) {
		DiskJobSerializer jobInstance = null;
		try {
			byte[] bytes = FileCopyUtils.copyToByteArray(serializationFile);
			jobInstance = (DiskJobSerializer) SerializationUtils.deserialize(bytes);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return jobInstance;
	}
}