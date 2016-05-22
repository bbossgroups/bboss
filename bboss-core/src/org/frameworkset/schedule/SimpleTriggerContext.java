package org.frameworkset.schedule;

import java.util.Date;

public class SimpleTriggerContext  implements TriggerContext {

	private volatile Date lastScheduledExecutionTime;

	private volatile Date lastActualExecutionTime;

	private volatile Date lastCompletionTime;


	/**
	 * Create a SimpleTriggerContext with all time values set to {@code null}.
	 */
	 public SimpleTriggerContext() {
	}

	/**
	 * Create a SimpleTriggerContext with the given time values.
	 * @param lastScheduledExecutionTime last <i>scheduled</i> execution time
	 * @param lastActualExecutionTime last <i>actual</i> execution time
	 * @param lastCompletionTime last completion time
	 */
	public SimpleTriggerContext(Date lastScheduledExecutionTime, Date lastActualExecutionTime, Date lastCompletionTime) {
		this.lastScheduledExecutionTime = lastScheduledExecutionTime;
		this.lastActualExecutionTime = lastActualExecutionTime;
		this.lastCompletionTime = lastCompletionTime;
	}


	/**
	 * Update this holder's state with the latest time values.
 	 * @param lastScheduledExecutionTime last <i>scheduled</i> execution time
	 * @param lastActualExecutionTime last <i>actual</i> execution time
	 * @param lastCompletionTime last completion time
	 */
	public void update(Date lastScheduledExecutionTime, Date lastActualExecutionTime, Date lastCompletionTime) {
		this.lastScheduledExecutionTime = lastScheduledExecutionTime;
		this.lastActualExecutionTime = lastActualExecutionTime;
		this.lastCompletionTime = lastCompletionTime;
	}


	@Override
	public Date lastScheduledExecutionTime() {
		return this.lastScheduledExecutionTime;
	}

	@Override
	public Date lastActualExecutionTime() {
		return this.lastActualExecutionTime;
	}

	@Override
	public Date lastCompletionTime() {
		return this.lastCompletionTime;
	}

}
