package org.frameworkset.schedule;

import java.util.concurrent.ThreadFactory;

import org.frameworkset.util.CustomizableThreadCreator;

public class CustomizableThreadFactory  extends CustomizableThreadCreator implements ThreadFactory {

	/**
	 * Create a new CustomizableThreadFactory with default thread name prefix.
	 */
	public CustomizableThreadFactory() {
		super();
	}

	/**
	 * Create a new CustomizableThreadFactory with the given thread name prefix.
	 * @param threadNamePrefix the prefix to use for the names of newly created threads
	 */
	public CustomizableThreadFactory(String threadNamePrefix) {
		super(threadNamePrefix);
	}


	@Override
	public Thread newThread(Runnable runnable) {
		return createThread(runnable);
	}


}
