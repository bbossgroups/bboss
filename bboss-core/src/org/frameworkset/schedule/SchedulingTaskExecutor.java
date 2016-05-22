package org.frameworkset.schedule;

public interface SchedulingTaskExecutor extends AsyncTaskExecutor {

	/**
	 * Does this {@code TaskExecutor} prefer short-lived tasks over
	 * long-lived tasks?
	 * <p>A {@code SchedulingTaskExecutor} implementation can indicate
	 * whether it prefers submitted tasks to perform as little work as they
	 * can within a single task execution. For example, submitted tasks
	 * might break a repeated loop into individual subtasks which submit a
	 * follow-up task afterwards (if feasible).
	 * <p>This should be considered a hint. Of course {@code TaskExecutor}
	 * clients are free to ignore this flag and hence the
	 * {@code SchedulingTaskExecutor} interface overall. However, thread
	 * pools will usually indicated a preference for short-lived tasks, to be
	 * able to perform more fine-grained scheduling.
	 * @return {@code true} if this {@code TaskExecutor} prefers
	 * short-lived tasks
	 */
	boolean prefersShortLivedTasks();


}
