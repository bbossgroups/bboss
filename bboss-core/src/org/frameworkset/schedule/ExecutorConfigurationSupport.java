package org.frameworkset.schedule;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.frameworkset.spi.BeanNameAware;
import org.frameworkset.spi.DisposableBean;
import org.frameworkset.spi.InitializingBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ExecutorConfigurationSupport extends CustomizableThreadFactory
		implements BeanNameAware, InitializingBean, DisposableBean {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private ThreadFactory threadFactory = this;

	private boolean threadNamePrefixSet = false;

	private RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.AbortPolicy();

	private boolean waitForTasksToCompleteOnShutdown = false;

	private int awaitTerminationSeconds = 0;

	private String beanName;

	private ExecutorService executor;

	/**
	 * Set the ThreadFactory to use for the ExecutorService's thread pool.
	 * Default is the underlying ExecutorService's default thread factory.
	 * <p>
	 * In a Java EE 7 or other managed environment with JSR-236 support,
	 * consider specifying a JNDI-located ManagedThreadFactory: by default, to
	 * be found at "java:comp/DefaultManagedThreadFactory". Use the
	 * "jee:jndi-lookup" namespace element in XML or the programmatic
	 *   org.frameworkset.jndi.JndiLocatorDelegate} for convenient
	 * lookup. Alternatively, consider using Spring's
	 *   DefaultManagedAwareThreadFactory} with its fallback to local
	 * threads in case of no managed thread factory found.
	 * 
	 * @see java.util.concurrent.Executors#defaultThreadFactory()
	 */
	public void setThreadFactory(ThreadFactory threadFactory) {
		this.threadFactory = (threadFactory != null ? threadFactory : this);
	}

	@Override
	public void setThreadNamePrefix(String threadNamePrefix) {
		super.setThreadNamePrefix(threadNamePrefix);
		this.threadNamePrefixSet = true;
	}

	/**
	 * Set the RejectedExecutionHandler to use for the ExecutorService. Default
	 * is the ExecutorService's default abort policy.
	 * 
	 * @see java.util.concurrent.ThreadPoolExecutor.AbortPolicy
	 */
	public void setRejectedExecutionHandler(RejectedExecutionHandler rejectedExecutionHandler) {
		this.rejectedExecutionHandler = (rejectedExecutionHandler != null ? rejectedExecutionHandler
				: new ThreadPoolExecutor.AbortPolicy());
	}

	/**
	 * Set whether to wait for scheduled tasks to complete on shutdown, not
	 * interrupting running tasks and executing all tasks in the queue.
	 * <p>
	 * Default is "false", shutting down immediately through interrupting
	 * ongoing tasks and clearing the queue. Switch this flag to "true" if you
	 * prefer fully completed tasks at the expense of a longer shutdown phase.
	 * <p>
	 * Note that Spring's container shutdown continues while ongoing tasks are
	 * being completed. If you want this executor to block and wait for the
	 * termination of tasks before the rest of the container continues to shut
	 * down - e.g. in order to keep up other resources that your tasks may need
	 * -, set the {@link #setAwaitTerminationSeconds "awaitTerminationSeconds"}
	 * property instead of or in addition to this property.
	 * 
	 * @see java.util.concurrent.ExecutorService#shutdown()
	 * @see java.util.concurrent.ExecutorService#shutdownNow()
	 */
	public void setWaitForTasksToCompleteOnShutdown(boolean waitForJobsToCompleteOnShutdown) {
		this.waitForTasksToCompleteOnShutdown = waitForJobsToCompleteOnShutdown;
	}

	/**
	 * Set the maximum number of seconds that this executor is supposed to block
	 * on shutdown in order to wait for remaining tasks to complete their
	 * execution before the rest of the container continues to shut down. This
	 * is particularly useful if your remaining tasks are likely to need access
	 * to other resources that are also managed by the container.
	 * <p>
	 * By default, this executor won't wait for the termination of tasks at all.
	 * It will either shut down immediately, interrupting ongoing tasks and
	 * clearing the remaining task queue - or, if the
	 * {@link #setWaitForTasksToCompleteOnShutdown
	 * "waitForTasksToCompleteOnShutdown"} flag has been set to {@code true}, it
	 * will continue to fully execute all ongoing tasks as well as all remaining
	 * tasks in the queue, in parallel to the rest of the container shutting
	 * down.
	 * <p>
	 * In either case, if you specify an await-termination period using this
	 * property, this executor will wait for the given time (max) for the
	 * termination of tasks. As a rule of thumb, specify a significantly higher
	 * timeout here if you set "waitForTasksToCompleteOnShutdown" to
	 * {@code true} at the same time, since all remaining tasks in the queue
	 * will still get executed - in contrast to the default shutdown behavior
	 * where it's just about waiting for currently executing tasks that aren't
	 * reacting to thread interruption.
	 * 
	 * @see java.util.concurrent.ExecutorService#shutdown()
	 * @see java.util.concurrent.ExecutorService#awaitTermination
	 */
	public void setAwaitTerminationSeconds(int awaitTerminationSeconds) {
		this.awaitTerminationSeconds = awaitTerminationSeconds;
	}

	@Override
	public void setBeanName(String name) {
		this.beanName = name;
	}

	/**
	 * Calls {@code initialize()} after the container applied all property
	 * values.
	 * 
	 * @see #initialize()
	 */
	@Override
	public void afterPropertiesSet() {
		initialize();
	}

	/**
	 * Set up the ExecutorService.
	 */
	public void initialize() {
		if (logger.isInfoEnabled()) {
			logger.info("Initializing ExecutorService " + (this.beanName != null ? " '" + this.beanName + "'" : ""));
		}
		if (!this.threadNamePrefixSet && this.beanName != null) {
			setThreadNamePrefix(this.beanName + "-");
		}
		this.executor = initializeExecutor(this.threadFactory, this.rejectedExecutionHandler);
	}

	/**
	 * Create the target {@link java.util.concurrent.ExecutorService} instance.
	 * Called by {@code afterPropertiesSet}.
	 * 
	 * @param threadFactory
	 *            the ThreadFactory to use
	 * @param rejectedExecutionHandler
	 *            the RejectedExecutionHandler to use
	 * @return a new ExecutorService instance
	 * @see #afterPropertiesSet()
	 */
	protected abstract ExecutorService initializeExecutor(ThreadFactory threadFactory,
			RejectedExecutionHandler rejectedExecutionHandler);

	/**
	 * Calls {@code shutdown} when the BeanFactory destroys the task executor
	 * instance.
	 * 
	 * @see #shutdown()
	 */
	@Override
	public void destroy() {
		shutdown();
	}

	/**
	 * Perform a shutdown on the underlying ExecutorService.
	 * 
	 * @see java.util.concurrent.ExecutorService#shutdown()
	 * @see java.util.concurrent.ExecutorService#shutdownNow()
	 * @see #awaitTerminationIfNecessary()
	 */
	public void shutdown() {
		if (logger.isInfoEnabled()) {
			logger.info("Shutting down ExecutorService" + (this.beanName != null ? " '" + this.beanName + "'" : ""));
		}
		if (this.waitForTasksToCompleteOnShutdown) {
			this.executor.shutdown();
		} else {
			this.executor.shutdownNow();
		}
		awaitTerminationIfNecessary();
	}

	/**
	 * Wait for the executor to terminate, according to the value of the
	 * {@link #setAwaitTerminationSeconds "awaitTerminationSeconds"} property.
	 */
	private void awaitTerminationIfNecessary() {
		if (this.awaitTerminationSeconds > 0) {
			try {
				if (!this.executor.awaitTermination(this.awaitTerminationSeconds, TimeUnit.SECONDS)) {

					logger.warn("Timed out while waiting for executor"
							+ (this.beanName != null ? " '" + this.beanName + "'" : "") + " to terminate");

				}
			} catch (InterruptedException ex) {

				logger.warn("Interrupted while waiting for executor"
						+ (this.beanName != null ? " '" + this.beanName + "'" : "") + " to terminate");

				Thread.currentThread().interrupt();
			}
		}
	}

}
