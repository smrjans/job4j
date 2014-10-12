package com.talentica.job4j.api;

import java.lang.reflect.Type;

public interface JobPipe<I,O> extends InputProducer<I>, OutputConsumer<O>{
	public Type getDataType();
	public void setDataType(Type dataType);
}
