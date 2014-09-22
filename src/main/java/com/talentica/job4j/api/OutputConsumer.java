package com.talentica.job4j.api;

public interface OutputConsumer<O> {
	public void consumeOutput(O output, int counter);
}
