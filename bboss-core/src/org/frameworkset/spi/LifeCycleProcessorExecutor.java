package org.frameworkset.spi;

import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;
import org.frameworkset.spi.support.DefaultLifecycleProcessor;

public class LifeCycleProcessorExecutor {
	
	public LifeCycleProcessorExecutor() {
		// TODO Auto-generated constructor stub
	}
	private static Logger log = Logger.getLogger(LifeCycleProcessorExecutor.class);
	protected LifecycleProcessor lifecycleProcessor;
	protected BaseApplicationContext context;
	protected LifecycleProcessor initLifecycleProcessor() {
		 if(lifecycleProcessor == null)
		 {
			DefaultLifecycleProcessor defaultProcessor = new DefaultLifecycleProcessor();
			defaultProcessor.setApplicationContext(context);
			this.lifecycleProcessor = defaultProcessor;
			return defaultProcessor;
		 }
		 return lifecycleProcessor;
			// registerSingleton(LIFECYCLE_PROCESSOR_BEAN_NAME, this.lifecycleProcessor);
			  
	}
	public void setLifecycleProcessor(LifecycleProcessor lifecycleProcessor) {
		 
		this.lifecycleProcessor = lifecycleProcessor;
//		return lifecycleProcessor;
		 	  
	}
	
	/**
	 * Finish the refresh of this context, invoking the LifecycleProcessor's
	 * onRefresh() method and publishing the
	 * {@link org.springframework.context.event.ContextRefreshedEvent}.
	 */
	public void startProcessor() {
		if(!this.active.compareAndSet(false, true))
			return;
		// Initialize lifecycle processor for this context.
		initLifecycleProcessor();

		// Propagate refresh to lifecycle processor first.
		getLifecycleProcessor().onRefresh();

		 
	}
	/** Flag that indicates whether this context is currently active */
	private final AtomicBoolean active = new AtomicBoolean();

	/** Flag that indicates whether this context has been closed already */
	private final AtomicBoolean closed = new AtomicBoolean();

	public void stopProcessor() {
		if (this.active.get() && this.closed.compareAndSet(false, true)) {
			if (log.isInfoEnabled()) {
				log.info("Closing " + this);
			}

			 

			// Stop all Lifecycle beans, to avoid delays during individual destruction.
			try {
				if(lifecycleProcessor != null)
				{
					lifecycleProcessor.onClose();
					lifecycleProcessor = null;
				}
			}
			catch (Throwable ex) {
				log.warn("Exception thrown from LifecycleProcessor on context close", ex);
			}

			 
		 

			this.active.set(false);
		}
	}
	public boolean isProcessorActive() {
		return this.active.get();
	}
	/**
	 * Return the internal LifecycleProcessor used by the context.
	 * @return the internal LifecycleProcessor (never {@code null})
	 * @throws IllegalStateException if the context has not been initialized yet
	 */
	LifecycleProcessor getLifecycleProcessor() throws IllegalStateException {
		if (this.lifecycleProcessor == null) {
			throw new IllegalStateException("LifecycleProcessor not initialized - " +
					"call 'refresh' before invoking lifecycle methods via the context: " + this);
		}
		return this.lifecycleProcessor;
	}

	public BaseApplicationContext getContext() {
		return context;
	}

	public void setContext(BaseApplicationContext context) {
		this.context = context;
	}

}
