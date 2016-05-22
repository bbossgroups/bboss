package org.frameworkset.schedule;

import java.util.Date;

public interface TriggerContext {
	/**
	 * Return the last <i>scheduled</i> execution time of the task,
	 * or {@code null} if not scheduled before.
	 */
	Date lastScheduledExecutionTime();

	/**
	 * Return the last <i>actual</i> execution time of the task,
	 * or {@code null} if not scheduled before.
	 */
	Date lastActualExecutionTime();

	/**
	 * Return the last completion time of the task,
	 * or {@code null} if not scheduled before.
	 */
	Date lastCompletionTime();
}
